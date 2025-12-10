package com.example.groupbuying.domain.groupbuy.service.scheduler;

import com.example.groupbuying.domain.groupbuy.entity.Form;
import com.example.groupbuying.domain.groupbuy.enums.FormStatus;
import com.example.groupbuying.domain.groupbuy.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormScheduler {

    private final FormRepository formRepository;


    @Scheduled(cron = "0 0/1 * * * *")
    @Transactional
    public void closeExpiredForms() {
        LocalDateTime now = LocalDateTime.now();

        List<Form> expiredForms = formRepository.findByStatusAndDeadlineBefore(FormStatus.OPEN, now);

        if (!expiredForms.isEmpty()) {
            log.info("마감 기한이 지난 공구 {}건을 자동으로 마감 처리합니다.", expiredForms.size());

            for (Form form : expiredForms) {
                form.close();
            }
        }
    }
}