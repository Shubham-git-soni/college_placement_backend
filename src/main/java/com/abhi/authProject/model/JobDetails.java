package com.abhi.authProject.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "jobdetails")
public class JobDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Apply link is required")
    @Lob
    @Column(name = "apply_link", nullable = false)
    private String apply_link;

    @NotBlank(message = "Title is required")
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull(message = "Last date is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")

    @Temporal(TemporalType.DATE)
    @Column(name = "last_date", nullable = false)
    private Date last_date;

    @NotBlank(message = "Company name is required")
    @Column(name = "company_name", nullable = false)
    private String company_name;

    @NotBlank(message = "Description is required")
    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Min(value = 0, message = "Salary must be non-negative")
    @Column(name = "salary", nullable = false)
    private int salary;
}
