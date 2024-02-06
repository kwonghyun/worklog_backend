package com.example.worklog.repository;

import com.example.worklog.dto.work.WorkSearchRepoParamDto;
import com.example.worklog.entity.User;
import com.example.worklog.entity.Work;
import com.example.worklog.entity.enums.Category;
import com.example.worklog.entity.enums.Importance;
import com.example.worklog.entity.enums.WorkState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-dev.yaml", properties = {})
@ActiveProfiles("test")
@DataJpaTest
public class EnumInQueryParamTest {
    @Autowired
    private WorkRepository workRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void searchWorksByParamsAndUser() {
        User user = User.builder()
                .username("Asd")
                .password("asd")
                .email("asd@asd.com")
                .build();
        Work work1 = Work.builder()
                .category(Category.REFACTOR)
                .content("asd")
                .title("asddasd")
                .state(WorkState.IN_PROGRESS)
                .date(LocalDate.now())
                .displayOrder(1)
                .importance(Importance.MID)
                .user(user)
                .build();
        userRepository.save(user);
        workRepository.save(work1);

        WorkSearchRepoParamDto workSearchRepoParamDto = new WorkSearchRepoParamDto();
        workSearchRepoParamDto.setCategory(Category.REFACTOR);
        Pageable pageable = Pageable.unpaged();
        Page<Work> worksByCategory = workRepository.searchWorksByParamsAndUser(workSearchRepoParamDto, user, pageable);
        for (Work work : worksByCategory) {
            System.out.println("work = " + work);
        }
    }
}
