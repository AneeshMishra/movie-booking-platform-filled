package com.xyz.payment.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name="payments")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long bookingId;
    private String method;
    private String status;
    private String externalRef;
    private OffsetDateTime createdAt;
    public Long getId(){return id;} public void setId(Long id){this.id=id;} }
