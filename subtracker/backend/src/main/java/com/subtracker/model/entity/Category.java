package com.subtracker.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents a subscription category (e.g., ENTERTAINMENT, CLOUD, HEALTH).
 * 
 * Designed as a JPA Entity (rather than a simple Enum) to allow for 
 * greater scalability, such as allowing users to create their own 
 * custom categories in the database in the future.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter 
@NoArgsConstructor
public class Category {
    @Id // defines as primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generates as an auto-increment
    private Integer id;

    @Column(name = "name",nullable = false, unique = true)
    private String name;

    public Category(String name) {
        this.name = name;
    }

    @PrePersist
    @PreUpdate
    // runs before inserting or updating a value
    private void formatData() {
        if(this.name != null) {
            this.name = this.name.trim().toUpperCase();
        }
    }
}