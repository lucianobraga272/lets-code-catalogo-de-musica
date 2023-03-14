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
public class PlaylistDTO {
    
    private String uid;

    private String name;
    
    private List<MusicDTO> musics;
           
}
