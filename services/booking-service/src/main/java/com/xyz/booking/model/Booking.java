package com.xyz.booking.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "bookings")
public class Booking {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="customer_id") private Long customerId;
    @Column(name="show_id") private Long showId;
    @Column(name="seat_code") private String seatCode;
    private String status;
    private Double amount;
    private OffsetDateTime createdAt;

    private LocalDateTime showTime;

    private BigDecimal totalAmount;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Ticket> tickets;
    public Long getId(){return id;} public void setId(Long id){this.id=id;} }
