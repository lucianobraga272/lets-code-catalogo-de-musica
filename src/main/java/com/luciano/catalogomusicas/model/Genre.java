package com.luciano.catalogomusicas.model;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Table(name = "genre")
@Entity
@AllArgsConstructor 
@NoArgsConstructor 
@Getter 
@Setter
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    private String uid;

    @Override
    public String toString() {
        return "Genre [id=" + id + ", name=" + name + ", uid=" + uid + "]";
    }

    @OneToMany(targetEntity = Music.class, mappedBy = "genre")
    private Set<Music> musics;

    
}