package com.luciano.catalogomusicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luciano.catalogomusicas.model.Music;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {
    
    @Query("SELECT m FROM Music m WHERE m.uid = :uid")
    List<Music> findByUid(@Param("uid") String uid);
    
}