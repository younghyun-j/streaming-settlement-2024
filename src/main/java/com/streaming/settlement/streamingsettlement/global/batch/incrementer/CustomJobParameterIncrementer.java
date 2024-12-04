package com.streaming.settlement.streamingsettlement.global.batch.incrementer;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CustomJobParameterIncrementer implements JobParametersIncrementer {

    @Override
    public JobParameters getNext(JobParameters parameters) {
        LocalDate date = LocalDate.now().minusDays(1L);
        return new JobParametersBuilder()
                .addLocalDate("date", date)
                .toJobParameters();
    }
}
