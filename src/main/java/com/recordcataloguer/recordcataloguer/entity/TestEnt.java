package com.recordcataloguer.recordcataloguer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "test", schema = "discogs")
@Data
public class TestEnt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "test")
    private String test = "test";

    public TestEnt(String test) {
        this.test = test;
    }

    public TestEnt() {
    }
}
