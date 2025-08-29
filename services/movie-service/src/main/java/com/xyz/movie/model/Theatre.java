package com.example.movieservice.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    private List<Show> shows;

    // getters and setters
}
