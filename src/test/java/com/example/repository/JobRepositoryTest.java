package com.example.repository;

import com.example.model.Job;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {
    @Autowired
    private JobRepository jobRepository;

    @Test
    void getLocationStatistics() {
        Job first = Job.builder()
                .company_name("First Company")
                .location("Berlin")
                .build();
        Job second = Job.builder()
                .company_name("Second Company")
                .location("Munich")
                .build();
        Job third = Job.builder()
                .company_name("Third Company")
                .location("Berlin")
                .build();
        jobRepository.saveAll(List.of(first, second, third));

        List<Object[]> locationStatistics = jobRepository.getLocationStatistics();
        assertEquals(2, locationStatistics.size());
        assertEquals("Berlin", locationStatistics.get(0)[0]);
        assertEquals(2L, locationStatistics.get(0)[1]);
        assertEquals("Munich", locationStatistics.get(1)[0]);
        assertEquals(1L, locationStatistics.get(1)[1]);
    }
}
