package com.shubham.authProject.repo;




import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shubham.authProject.model.Interview;

import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Long> {
    Optional<Interview> findByJobApplication_Id(Long jobApplicationId);
    List<Interview> findByJobApplication_ApplicantEmail(String applicantEmail);
}