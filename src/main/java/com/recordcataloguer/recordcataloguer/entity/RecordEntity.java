package com.recordcataloguer.recordcataloguer.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@Table(name = "user_data")
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