## 업무의 기록과 일정 관리를 하는 메모 웹, 워크로그

<img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/031f1149-7c12-48d5-9522-e8d7346a596e" alt="image-20240207001441527" width="80%"/>

last updated at: 2024.02.12 



### 목차

------

- [프로젝트 설명](#프로젝트-설명)
- [실행 방법](#실행-방법)
- [사용 방법](#사용-방법)
- [요구 사항](#요구-사항)
- [★중점 구현 사항★](#중점-구현-사항)
  - [빈틈없이, 낭비없이 보내는 알림](#빈틈없이-낭비없이-보내는-알림)
  - [기존 기능과 느슨하게 결합된 알림](#기존-기능과-느슨하게-결합된-알림)
  - [DB 접근 최소화](#DB-접근-최소화)
  - [Spring Security와 결합도 분리](#Spring-Security와-결합도-분리)
  - [String, Wrapper 타입이 아닌 필드의 유효성 검사](#String-Wrapper-타입이-아닌-필드의-유효성-검사)
  - [응답 통일](#응답-통일)




### 프로젝트 설명

------

#### 개요

- 웹사이트 주소 : https://today.worklog.shop (임시 계정 ID: 3, PW: 3)

- API 서버: https://worklog.shop

- API 명세서 : [구글 스프레드 시트](https://docs.google.com/spreadsheets/d/1ELjduGpp7mbwTTfVWQHOoaWxEAEbjf6rfnRuwG0GM5Y/edit#gid=0)
- 개발 인원 : 백엔드 1인(<img src="https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png" width="30" height="28"> [본인](https://github.com/kwonghyun/worklog_backend)), 프론드엔드 1인 (<img src="https://github.githubassets.com/assets/GitHub-Mark-ea2971cee799.png" width="30" height="28">[dmchoi77](https://github.com/dmchoi77/worklog))

- 개발 기간: 2023.11.10 ~ 진행중

#### 개발환경

- 언어 : Java17
- 빌드 툴 : Gradle
- 프레임워크 : Springboot(3.1.x), Spring Framework, 
  Spring Data JPA, Spring Security, Spring AOP, Spring Validation
- 라이브러리: Quartz Scheduler(알림 기능), Quarydsl
- ORM : JPA
- DB: MySQL
- 프록시서버: nginx
- 캐시 서버(리프레시 토큰, 알림 확인 Flag): Redis
- Infra Structure: AWS(EC2, RDS, CodeDeploy, S3), GIthub Actions

#### ERD

<img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/ddeddec0-fc75-4e12-8d12-7e1537f7feea" alt="image" width="50%" />

#### 프로젝트 구조

<img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/9c5dbb99-aeca-4deb-a078-4e49ac28fddd" alt="image-20240206200557127" width="70%"/>

#### 배포 구조

<img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/1a68df4e-515e-4bae-bb23-f25fee53e458" alt="image-20240206200639822" width="70%"/>

[목차](#목차)



### 실행 방법

------

로컬 실행시 

1. IntelliJ, MySQL 8.0 설치
2. redis 클라이언트 설치
   - Windows 사용자: https://github.com/microsoftarchive/redis/releases에서 .msi 파일 설치
   - Mac 사용자: ```brew install redis```
3. 환경변수 설정
   - JWT_EXPIRATION : 엑세스 토큰 유효시간(초)
   - JWT_REFRESH_EXPIRATION : 리프레시토큰 유효시간(초)
   - JWT_SECRET : JWT 시크릿 키(임의 문자열)
   - LOCAL_DB_URL : MySQL 스키마 URL
   - LOCAL_DB_USERNAME :  MySQL 사용자 이름
   - LOCAL_DB_PASSWORD : MySQL 비밀번호
4. 프로필 설정
   - Active Profiles: dev

5. 실행

[목차](#목차)

### 요구 사항

------

- 업무 마감 임박 알림(프론트 미구현)
  - 생성된 업무가 알림을 보낼 시간이 지났다면 바로 알림을 전송합니다.
  - 생성된 업무의 알림을 보낼 시간이 24시간 이내라면 알림을 예약합니다.
  - 업무의 생성 또는 수정이 발생하면 알림을 동작할지 확인합니다.
- 회원
  - 로그인 및 회원가입을 할 수 있습니다.
  - 로그인된 사용자에 한해 서비스를 이용할 수 있습니다.
- 업무
  - 사용자는 해당일의 업무를 생성/조회/수정/삭제 할 수 있습니다.
  - 업무에는 제목, 내용, 업무유형, 진행상태를 포함합니다. 
  - 날짜별로 업무가 표시되는 순서를 저장할 수 있습니다.
  - 제목, 내용을 검색할 수 있습니다.
- 메모
  - 사용자는 해당일의 메모를 생성/조회/수정/삭제 할 수 있습니다.
  - 메모에는 내용만 기록할 수 있습니다.
  - 날짜별로 메모가 표시되는 순서를 저장할 수 있습니다.
  - 내용을 검색할 수 있습니다.
  
- 달력
  - 업무 또는 메모가 존재하는 년월일에 한해 날짜를 제공합니다.
  - 날짜는 년월일 순으로 제공됩니다.

[목차](#목차)

### 중점 구현 사항

------

#### 알림 기능

##### 빈틈없이, 낭비없이 보내는 알림



##### 기존 기능과 느슨하게 결합된 알림

- Redis를 인터페이스로 활용해 DB의 접근을 줄이고 기능간 결합도 낮추기

  - 토큰 발급시(로그인, 토큰 재발급)에는 Flag만 발급
    <img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/bcac7006-0f87-488b-b3d6-8e58ff527ea2" width="70%"/>
  - SSE 연결시 Flag 있는지 여부에 따라 알림 검색<img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/9472ae56-654f-48e9-8963-c6c50c0bccc4" width="70%"/>

- EventPublisher/Listener를 이용해 트랜젝션 분리 및 기능간 결합도 낮추기

  - 업무 생성·수정·삭제 시 알림 조건 확인 및 전송 로직에 EventPublisher·Listener 적용

    ```java
    public class WorkService{   
        // ...
        @Transactional
        public void createWork(WorkPostDto dto, CustomUserDetails userDetails) {
            Work work = workRepository.save(
                    Work.builder()//...
                            .build()
            );
            // JPA에서 Flush 후 이벤트 발행
            applicationEventPublisher.publishEvent(
                    WorkChangeEvent.builder().work(work).build()
            );
        }
        // ...
    }
    ```
    
    ```java
    public class EventHandler {
        private final NotificationService notificationService;
        @TransactionalEventListener
        // 이벤트 받아 로직 실행
        public void onWorkChanged(WorkChangeEvent workChangeEvent) {
            Work work = workChangeEvent.getWork();
            Long userId = work.getUser().getId();
            //... 조건 확인 후 전송하거나 알림 예약
    		Notification notification = notificationService.createNotificationFrom(work);
    		notificationService.sendNotification(notification);
    }
    ```

  - 업무 생성 후 로직
    	<img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/bb8789db-55ce-4be6-89c2-9ca9b99624bb" width="70%"/>
  - 업무 삭제 후 로직
    <img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/8a5b472c-0e75-468c-9c14-5a56cef595a9" width="60%" height="70%"/>
  - 업무 수정 후 로직
    <img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/ab24fe9a-b09c-44f7-ab5e-274136b3d6f8"  width="70%" height="100%"/>

[목차](#목차)

#### DB 접근 최소화

- Refresh Token을 Redis에 저장해 Access Token 재발급시 DB접근 불필요
  <img src="https://github.com/kwonghyun/worklog_backend/assets/61932809/43612254-1ca1-4d3c-9e16-df632f50588e"  width="60%" height="100%"/>

- JWT에 userId를 담아 user 테이블과 조인하는 모든 쿼리 FK(userId)로만 조회

  ```java
  public class WorkServiceImpl {
      // ... 생략
  	private Work getValidatedWorkByUserIdAndWorkId(Long userId, Long workId) {
              Work work = workRepository.findById(workId)
                      .orElseThrow(() -> new CustomException(ErrorCode.WORK_NOT_FOUND));
      		// Work Select 쿼리 발생 
              if (!work.getUser().getId().equals(userId)) { 
      		// userId는 Work의 FK이므로 추가 조회 불필요
  			// 만약 getId() 대신 getUsername() 호출시 지연로딩에 의해 User SELECT 쿼리 발생
                  throw new CustomException(ErrorCode.WORK_USER_NOT_MATCHED);
              } else {
                  return work;
              }
          }
  }
  ```

- 연관된 엔티티의 필드가 함께 사용되는 경우 지연로딩에 의한 추가 쿼리 방지를 위해 Fetch Join 적용

  ```java
  public interface NotificationRepository extends JpaRepository<Notification, Long> {
      // ... 생략
      @Query(
              "SELECT n FROM Notification n " +
                      "JOIN FETCH n.receiver " + // Notification의 User receiver; 필드
                      "WHERE n.id = :id"
      ) // 이미 receiver가 로딩된 상태이므로 notification.getReceiver().getUsername()을 호출해도 추가 쿼리 미발생
      Optional<Notification> findByIdFetchReceiver(@Param("id") Long id);
  ```

- 로그인, 회원가입, 비밀번호 변경 시 ID/PW가 형식에 맞지 않으면 DB 조회하지 않고 로그인 실패 처리
  ```java
  public class UserServiceImpl {
  	// ...
  	public JwtDto login(UserLoginDto dto, HttpServletRequest request) {
          Pattern usernamePattern = Pattern.compile(Constant.USERNAME_REGEX);
          Pattern passwordPattern = Pattern.compile(Constant.PASSWORD_REGEX);
          if (
                  !usernamePattern.matcher(dto.getUsername()).matches()
                  || !passwordPattern.matcher(dto.getPassword()).matches()
          ) {     
              throw new CustomException(ErrorCode.LOGIN_FAILED);            
          }
  		// 이후 ID/PW 확인 및 토큰 발급...
      }
      // ...
  }
  ```

[목차](#목차)

#### Spring Security와 결합도 분리

- Spring Security에서 인증 객체를 Controller계층에 전달, 요청별 권한 설정, PasswordEncoder의 기능만 제한적으로 사용
- Login, Logout, 엑세스 토큰 재발급 등의 API를 다른 API와 통일성을 위해 Controller, Service 레이어에서 구현
- UserDetails와 UserDetailsService를 구현하지 않고 기존의 User 엔티티와 UserService만 사용하도록 구현
  ```java
  public class CustomAuthenticationToken extends AbstractAuthenticationToken {
      private final User user;
      private final Object credentials;
      // ...
  }
  ```

  ```java
  public class JwtValidationFilter extends OncePerRequestFilter {
  	// ...
      @Override
      protected void doFilterInternal(*/ ... */) throws ServletException, IOException {
          // ...
          Claims claims = jwtTokenUtils.parseClaims(token); // request header에서 token 찾아 파싱
          User user = jwtTokenUtils.generateUserFromClaims(claims); // 파싱한 정보로 User 객체 생성
          Authentication authentication // 인증 객체에 User 저장
              = new CustomAuthenticationToken(user, token, user.getAuthorities());
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          context.setAuthentication(authentication);
          SecurityContextHolder.setContext(context);
          filterChain.doFilter(request, response); // 다음 필터 실행
      }
  ```

[목차](#목차)

#### String, Wrapper 타입이 아닌 필드의 유효성 검사

- 특정 타입으로 역직렬화가 불가능한 형식의 값이 요청으로 오면 유효성 검사 이전인 객체가 생성 전에 예외가 발생,
  어떤 상황에서도 유효성 검사가 가능하도록 DTO에 String으로 1차 저장 후 검사

  ```java
  public class WorkCategoryPatchDto {
      @NotNull(message = Constants.CATEGORY_NOT_BLANK)
      @EnumValueCheck(enumClass = Category.class) // Enum으로 변환시 예외 발생여부 확인
      private String category;
  }
  ```

  ```java
  public class ValueOfEnumValidator implements ConstraintValidator<EnumValueCheck, String> {
      private EnumValueCheck enumValueCheck;
      
      @Override
      public void initialize(EnumValueCheck constraintAnnotation) {
          this.enumValueCheck = constraintAnnotation;
      }
  
      @Override
      public boolean isValid(String value, ConstraintValidatorContext context) {
          try {
              Method fromMethod = this.enumValueCheck.enumClass().getMethod("from", String.class);
              fromMethod.invoke(null, value);
          } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
              log.error("Error validating enum value: {}", e.getMessage());
              return false;
          }
          return true;
      }
  }
  ```

- 유효성 검사 후 Controller -> Service 계층 이동시 필요한 타입으로 변환

  ```java
  public class WorkController{
      // ...
  	@PatchMapping("/{workId}/category")
      public ResponseEntity<ResponseDto> updateWorkCategory(
              @PathVariable("workId") Long workId,
              @Valid @RequestBody WorkCategoryPatchDto dto, // DTO 내부에서 유효성 검사
              @AuthenticationPrincipal User user
      ) {
      	// 원하는 타입으로 변환 후 Service 계층 전달
          workService.updateWorkCategory(Category.from(dto.getCategory()), workId, user.getId());
          return ResponseEntity
                  .status(HttpStatus.OK)
                  .body(ResponseDto.fromSuccessCode(SuccessCode.WORK_EDIT_SUCCESS));
      }
      // ...
  }
  ```

[목차](#목차)

#### 응답 통일

- 응답 형식

  - 자원을 반환하는 응답
    ```json
    "status": 200,
    "count": 0,
    "data": []
    ```

  - 자원을 반환하지 않는 모든 응답(예외 포함)

    ```json
    "status": 201,
    "code": "CREATED",
    "message": "업무일지가 생성되었습니다."
    ```



- 예외 처리

  - DispatcherSevlet 안에서 발생하는 예외 : @RestControllerAdvice으로 처리

    - 사용자 지정 예외 : CustomException 클래스 생성해 사용
      ```java
      @ExceptionHandler(CustomException.class)
      protected ResponseEntity handleCustomException(CustomException ex) {
      	return new ResponseEntity(
                  ResponseDto.fromErrorCode(
                      ex.getErrorCode()), 
                  	HttpStatus.valueOf(ex.getErrorCode().getStatus()));
      }
      ```

    - Validation에서 발생하는 예외
      ```java
      @ExceptionHandler(MethodArgumentNotValidException.class)
      @ResponseStatus(HttpStatus.BAD_REQUEST)
      protected ResponseDto handleValidationException(
              MethodArgumentNotValidException exception
      ) {
              return ResponseDto.fromValidationException(exception);
      }
      ```

    - 그 외의 예외
      ```java
      @ExceptionHandler(Exception.class)
      protected ResponseEntity handleServerException(Exception ex) {
          return new ResponseEntity(
          	ResponseDto.fromErrorCode(INTERNAL_SERVER_ERROR), 		
          	HttpStatus.INTERNAL_SERVER_ERROR); 
          }
      ```

      

  - DispatcherServlet 밖에서 발생하는 예외

    - 필터 실행중 발생하는 예외: ObjectMapper 사용
      ex) JWT 파싱중 예외 응답해 401 응답시 프론트에서 토큰 재발급 요청

      ```java
      public class FilterExceptionHandler {
          public static void jwtExceptionHandler(
              HttpServletResponse response, ErrorCode error
          ) {
              response.setStatus(error.getStatus());
              response.setContentType(MediaType.APPLICATION_JSON_VALUE);
              response.setCharacterEncoding("UTF-8");
              try {
                  ObjectMapper objectMapper = new ObjectMapper();
                  objectMapper.writeValue(response.getWriter(), ResponseDto.fromErrorCode(error));
              } catch (Exception e) {
                  log.error(e.getMessage());
              }
          }
      }
      ```

    - 그 외의 예외 : DefaultErrorAttributes
      ex) 맵핑되지 않은 Request URI 

      ```java
      @Component
      public class CustomErrorAttributes extends DefaultErrorAttributes {
          @Override
          public Map<String, Object> getErrorAttributes(
          	WebRequest webRequest, ErrorAttributeOptions options
          ) {
              ResponseDto responseDto = ResponseDto.fromErrorAttributes(
                      super.getErrorAttributes(webRequest, options)
              );
              return BeanMap.create(responseDto);
          }
      }
      ```



[목차](#목차)

