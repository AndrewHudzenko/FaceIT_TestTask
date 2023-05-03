package com.example.service;

import com.example.model.Job;
import com.example.model.LocationStatistic;
import com.example.repository.JobRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    private final String API_URL = "https://www.arbeitnow.com/api/job-board-api";
    private final Long PAGE_LIMIT = 5L;
    private RestTemplate restTemplate;
    private JobRepository jobRepository;

    public JobServiceImpl(RestTemplate restTemplate, JobRepository jobRepository) {
        this.restTemplate = restTemplate;
        this.jobRepository = jobRepository;
    }

    @Scheduled(cron = "${cron.expression}")
    @Override
    public void loadJobsFromApiAndSaveToH2Db() {
        for (int i = 0; i < PAGE_LIMIT; i++) {
            String pageableUrl = API_URL + "?page=" + i;

            String jsonResponse = restTemplate.getForObject(pageableUrl, String.class);

            List<Job> jobs = parseJsonResponse(jsonResponse);

            jobRepository.saveAll(jobs);
        }
    }

    @Override
    public List<Job> findAll(PageRequest pageRequest) {
        return jobRepository.findAll(pageRequest).toList();
    }

    @Override
    public Long getJobAmount() {
        return jobRepository.count();
    }

    @Override
    public Page<Job> getTop10(Pageable pageable) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(API_URL)
                .queryParam("sort_by", "views");
        String url = builder.build().toUriString();

        List<Job> jobs = parseJsonResponse(restTemplate.getForObject(url, String.class));

        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        int fromIndex = pageNumber * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, jobs.size());

        List<Job> top10Jobs = jobs.subList(fromIndex, toIndex);

        return new PageImpl<>(top10Jobs, pageable, jobs.size());

    }

    @Override
    public List<LocationStatistic> getLocationStatistics() {
        return jobRepository.getLocationStatistics().stream()
                .map(o -> new LocationStatistic((String) o[0], (Long) o[1]))
                .collect(Collectors.toList());
    }

    private List<Job> parseJsonResponse(String jsonResponse) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode jobsNode = root.get("data");
            if (jobsNode != null && jobsNode.isArray()) {
                return objectMapper.readValue(jobsNode.toString(), new TypeReference<>() {});
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Can't parse json", e);
        }
        return Collections.emptyList();
    }
}
