package com.deliverytogo.user_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String zip;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}