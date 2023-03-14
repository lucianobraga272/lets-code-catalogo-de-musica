package com.luciano.catalogomusicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luciano.catalogomusicas.model.Playlist;

@Repository
public interface PlaylistRepository extends JpaRepository<Playlist, Integer> {
    
    @Query("SELECT p FROM Playlist p WHERE p.uid = :uid")
    List<Playlist> findByUid(@Param("uid") String uid);
    
}
