package com.luciano.catalogomusicas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MusicDTO {

    private String uid;

    private String name;

    private GenreDTO genre;

    private Integer likes;

}
