package com.luciano.catalogomusicas.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "music")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Music {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String uid;

    @ManyToOne(targetEntity = Genre.class)
    private Genre genre;

    @ManyToMany(mappedBy = "musics", cascade = CascadeType.ALL)
    private List<Playlist> playlists;

    @ManyToMany(mappedBy = "likedMusics", cascade = CascadeType.ALL)
    private List<Person> likes;

}