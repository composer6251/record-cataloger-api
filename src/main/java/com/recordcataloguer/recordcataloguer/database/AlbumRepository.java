package com.recordcataloguer.recordcataloguer.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AlbumRepository {

    @PersistenceContext
    private EntityManager entityManager;

//    @Transactional
//    public void insertEntity(Album album) {
//        entityManager.createNativeQuery("Insert into ")
//    }
}
