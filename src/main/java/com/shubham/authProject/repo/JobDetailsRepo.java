package com.shubham.authProject.repo;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubham.authProject.model.JobDetails;

@Repository
public interface JobDetailsRepo extends JpaRepository <JobDetails,Integer> {
  
}
