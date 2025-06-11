package com.shubham.authProject.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubham.authProject.model.ApplicationStatus;
import com.shubham.authProject.model.JobApplication;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    // Custom queries if needed, e.g., find by applicant email
    Optional<JobApplication> findByApplicantEmailAndJobId(String email, String jobId);
    List<JobApplication> findByStatus(ApplicationStatus status);
}