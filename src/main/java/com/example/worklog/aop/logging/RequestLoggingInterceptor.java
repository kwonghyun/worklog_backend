package com.example.worklog.aop.logging;

import com.example.worklog.aop.logging.querycount.ApiQueryCounter;
import com.example.worklog.jwt.JwtTokenUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {
    private final RequestLogger logger;
    private final JwtTokenUtils jwtTokenUtils;
    private static final String QUERY_COUNT_LOG_FORMAT = "{}STATUS_CODE: {}, QUERY_COUNT: {}, EXECUTION_TIME: {}ms";
    private final ApiQueryCounter apiQueryCounter;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            String token = authorizationHeader.split(" ")[1];
            String username = jwtTokenUtils.parseClaims(token).getSubject();
            if (username != null) {
                logger.setUsername(username);
            }
        }
        if (logger.getUsername() == null) {
            logger.setUsername("AnonymousUser");
        }
        logger.setRequestMethod(request.getMethod());
        logger.setRequestURL(request.getRequestURI());
        return true;
    }
    @Override
    public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response,
                                final Object handler, final Exception ex) {
        final int queryCount = apiQueryCounter.getCount();
        logger.setEndTimeMillis(System.currentTimeMillis());
        log.info(QUERY_COUNT_LOG_FORMAT, logger, response.getStatus(),
                queryCount, logger.getExecutionTime());
    }
}
