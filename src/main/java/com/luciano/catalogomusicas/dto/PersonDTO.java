package com.luciano.catalogomusicas.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PersonDTO {

    private String uid;
    private String name;
    private List<MusicDTO> likedMusics;
    private List<PlaylistDTO> playlists;
}
