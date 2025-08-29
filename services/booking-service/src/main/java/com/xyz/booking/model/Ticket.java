package com.xyz.booking.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;

    private BigDecimal price;

    private BigDecimal finalPrice;

    // getters & setters
}
