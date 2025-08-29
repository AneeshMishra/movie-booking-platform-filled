package com.xyz.partner.model;

import jakarta.persistence.*;

@Entity
@Table(name = "partners")
public class Partner {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contactEmail;
    private String contactPhone;

    public Long getId(){return id;} public void setId(Long id){this.id=id;} public String getName(){return name;} public void setName(String name){this.name=name;} public String getContactEmail(){return contactEmail;} public void setContactEmail(String contactEmail){this.contactEmail=contactEmail;} public String getContactPhone(){return contactPhone;} public void setContactPhone(String contactPhone){this.contactPhone=contactPhone;}
}
