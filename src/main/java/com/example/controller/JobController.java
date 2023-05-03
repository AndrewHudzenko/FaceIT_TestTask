package com.example.controller;

import com.example.model.Job;
import com.example.model.LocationStatistic;
import com.example.service.JobService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public List<Job> getAllJobs(@RequestParam (defaultValue = "0") Integer page,
                                @RequestParam (defaultValue = "50") Integer size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        return jobService.findAll(pageRequest);
    }

    @GetMapping("/top10")
    public Page<Job> top10(@RequestParam (defaultValue = "0") Integer page,
                           @RequestParam (defaultValue = "10") Integer size) {
        return jobService.getTop10(PageRequest.of(page, size, Sort.by("views").descending()));
    }

    @GetMapping("/location-stats")
    public List<LocationStatistic> getLocationStatistics() {
        return jobService.getLocationStatistics();
    }

    @PostMapping("/load-data-from-api")
    public String loadDataFromApi() {
        jobService.loadJobsFromApiAndSaveToH2Db();
        return jobService.getJobAmount() + " Jobs added successfully!";
    }
}
