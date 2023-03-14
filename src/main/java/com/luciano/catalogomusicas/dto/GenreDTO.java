package com.luciano.catalogomusicas.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GenreDTO {

    private String uid;

    @NotBlank(message = "O nome é obrigatório.")
    @Size(min = 3, message = "Nome muito curto.")
    private String name;

}
