package com.luciano.catalogomusicas.model;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "person")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "uid")
    private String uid;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)    
    private List<Music> likedMusics;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Playlist> playlists;

    @Override
    public String toString() {
        return "Person [id=" + id + ", name=" + name + ", uid=" + uid + ", likedMusics=" + likedMusics + ", playlists="
                + playlists + "]";
    }
    
}
