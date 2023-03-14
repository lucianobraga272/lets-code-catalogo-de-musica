package com.luciano.catalogomusicas.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luciano.catalogomusicas.dto.FactoryDTO;
import com.luciano.catalogomusicas.dto.MusicDTO;
import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Music;
import com.luciano.catalogomusicas.model.Person;
import com.luciano.catalogomusicas.model.Playlist;
import com.luciano.catalogomusicas.service.MusicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/musica", name = "Operações sobre músicas")
@AllArgsConstructor
public class MusicController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MusicController.class);
    private MusicService musicService;

    @Operation(summary = "Lista todas as músicas da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos as músicas encontradas.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MusicDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<MusicDTO>> readAll() {
        return ResponseEntity.ok().body(musicService.all().stream()
                .map(FactoryDTO::entityToDTO)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Retorna uma música pelo UID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Música encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MusicDTO.class))),
            @ApiResponse(responseCode = "400", description = "Uid invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Música não encontrada.", content = @Content)
    })
    @GetMapping("/{uid}")
    public ResponseEntity<MusicDTO> read(@PathVariable("uid") String uid) {
        try {
            return ResponseEntity.ok().body(FactoryDTO.entityToDTO(musicService.findByUid(uid)));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cadastra uma música.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Música cadastrada.")
    })
    @PostMapping()
    public ResponseEntity<MusicDTO> create(@RequestBody MusicDTO musicDTO) {

        musicDTO.setUid(UUID.randomUUID().toString());
        Music music = FactoryDTO.dtoToEntity(musicDTO);
        music.setLikes(new ArrayList<Person>());
        music.setPlaylists(new ArrayList<Playlist>());
        
        try {
            return ResponseEntity.ok().body(FactoryDTO.entityToDTO(musicService.create(music)));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }

    }

    @Operation(summary = "Atualiza um música.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Música atualizada.")
    })
    @PutMapping("/{uid}")
    public ResponseEntity<MusicDTO> update(@PathVariable("uid") String uid, @RequestBody MusicDTO musicDTO) {
        Music music = FactoryDTO.dtoToEntity(musicDTO);
        try {
            music = musicService.update(uid, music);
            return ResponseEntity.ok(FactoryDTO.entityToDTO(music));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

   

    @Operation(summary = "Deleta uma música.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Música deletada.")
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<String> delete(@PathVariable("uid") String uid) {
        try {
            musicService.delete(uid);
            return ResponseEntity.ok().body("Música deletada com sucesso!");
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

}
