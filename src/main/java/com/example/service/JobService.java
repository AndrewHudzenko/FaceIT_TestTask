package com.example.service;

import com.example.model.Job;
import com.example.model.LocationStatistic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface JobService {
    void loadJobsFromApiAndSaveToH2Db();

    Long getJobAmount();

    List<Job> findAll(PageRequest pageRequest);

    Page<Job> getTop10(Pageable pageable);

    List<LocationStatistic> getLocationStatistics();
}
