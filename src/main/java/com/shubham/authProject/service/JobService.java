package com.shubham.authProject.service;

import org.springframework.stereotype.Service;

import com.shubham.authProject.model.JobDetails;
import com.shubham.authProject.repo.JobDetailsRepo;

import java.util.List;

@Service
public class JobService {


    private final JobDetailsRepo repo;

    public JobService(JobDetailsRepo repo) {
        this.repo = repo;
    }

    public List<JobDetails> getAllJobs() {
        return repo.findAll();
    }

    public JobDetails addJob(JobDetails jobDetails) {
        return repo.save(jobDetails);
    }
}
