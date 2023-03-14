package com.luciano.catalogomusicas.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.luciano.catalogomusicas.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    @Query("SELECT p FROM Person p WHERE p.uid = :uid")
    List<Person> findByUid(@Param("uid") String uid);

}
