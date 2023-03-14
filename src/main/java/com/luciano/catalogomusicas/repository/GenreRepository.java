package com.luciano.catalogomusicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luciano.catalogomusicas.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    
    @Query("SELECT g FROM Genre g WHERE g.uid = :uid")
    List<Genre> findByUid(@Param("uid") String uid);
}