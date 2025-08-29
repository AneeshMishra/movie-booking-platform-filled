package com.xyz.seat.model;

import jakarta.persistence.*;

@Entity
@Table(name = "seatmaps")
public class SeatMap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long theatreId;
    @Lob
    private String seatmapJson;
    public Long getId(){return id;} public void setId(Long id){this.id=id;} }
