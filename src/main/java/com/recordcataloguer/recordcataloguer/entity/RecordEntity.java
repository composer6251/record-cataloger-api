package com.recordcataloguer.recordcataloguer.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
//@Table(name = "user_data")
@Builder
@NoArgsConstructor
public class RecordEntity {

    @Id
    @GeneratedValue
    private int id;

    private String name;
    private String email;
    private int currentLevel;
    private String password;

    public RecordEntity(int id, String name, String email, int currentLevel, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.currentLevel = currentLevel;
        this.password = password;
    }
}