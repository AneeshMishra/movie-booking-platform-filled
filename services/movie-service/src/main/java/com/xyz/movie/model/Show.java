package com.xyz.movie.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "shows")
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    private Double price;

    @ManyToOne
    @JoinColumn(name = "theatre_id")
    private Theatre theatre;

    private LocalDateTime showTime;

    // getters and setters
}
