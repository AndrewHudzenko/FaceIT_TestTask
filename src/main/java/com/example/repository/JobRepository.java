package com.example.repository;

import com.example.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j.location, COUNT(j) FROM Job j GROUP BY j.location ORDER BY COUNT(j) DESC")
    List<Object[]> getLocationStatistics();
}
