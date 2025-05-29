package com.abhi.authProject.model;

import jakarta.persistence.Column; // Make sure this is imported
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table; // Add this import for @Table annotation
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users") // Good practice to explicitly name your table
public class Users {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;

    // --- START: CRITICAL CHANGES FOR isVerified FIELD ---
    @Column(name = "is_verified", nullable = false, columnDefinition = "boolean default false")
    private boolean isVerified = false; // Initialize to false directly. This is the default value for new users.

    // Also consider adding a constructor to ensure it's false when creating new instances programmatically
    // If you're using Lombok's @Builder, ensure the builder also sets isVerified to false by default.
    /*
    public Users(String username, String password, String email, String role, String verificationToken, LocalDateTime verificationTokenExpires) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.verificationToken = verificationToken;
        this.verificationTokenExpires = verificationTokenExpires;
        this.isVerified = false; // Explicitly set to false for new users
    }
    */
    // --- END: CRITICAL CHANGES FOR isVerified FIELD ---

    private String verificationToken; // This will now store the OTP code
    private LocalDateTime verificationTokenExpires;
}