package com.xyz.movie.model;

import jakarta.persistence.*;

@Entity
@Table(name = "movies")
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String language;
    private String genre;
    private Integer durationMinutes;
    public Long getId(){return id;} public void setId(Long id){this.id=id;} }
