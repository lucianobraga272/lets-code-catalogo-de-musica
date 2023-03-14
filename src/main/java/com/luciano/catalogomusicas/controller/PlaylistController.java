package com.luciano.catalogomusicas.controller;

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
import com.luciano.catalogomusicas.dto.PlaylistDTO;
import com.luciano.catalogomusicas.exception.ExistsException;
import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Playlist;
import com.luciano.catalogomusicas.service.PlaylistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/playlist", name = "Operações sobre playlists")
@AllArgsConstructor
public class PlaylistController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PlaylistController.class);
    private PlaylistService playlistService;

    @Operation(summary = "Lista todas as playlists da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos as playlists encontradas.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PlaylistDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> readAll() {
        return ResponseEntity.ok().body(playlistService.all().stream()
                .map(FactoryDTO::entityToDTO)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Retorna uma playlist pelo UID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PlaylistDTO.class))),
            @ApiResponse(responseCode = "400", description = "Uid invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Playlist não encontrada.", content = @Content)
    })
    @GetMapping("/{uid}")
    public ResponseEntity<PlaylistDTO> read(@PathVariable("uid") String uid) {
        try {
            return ResponseEntity.ok().body(FactoryDTO.entityToDTO(playlistService.findByUid(uid)));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cadastra uma playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist cadastrada.")
    })
    @PostMapping()
    public ResponseEntity<PlaylistDTO> create(@RequestBody PlaylistDTO playlistDTO) {

        playlistDTO.setUid(UUID.randomUUID().toString());
        Playlist playlist = FactoryDTO.dtoToEntity(playlistDTO);

        return ResponseEntity.ok().body(FactoryDTO.entityToDTO(playlistService.create(playlist)));

    }

    @Operation(summary = "Atualiza um playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist atualizada.")
    })
    @PutMapping("/{uid}")
    public ResponseEntity<PlaylistDTO> update(@PathVariable("uid") String uid, @RequestBody PlaylistDTO playlistDTO) {
        Playlist playlist = FactoryDTO.dtoToEntity(playlistDTO);
        try {
            playlistService.update(uid, playlist);
            return ResponseEntity.ok(FactoryDTO.entityToDTO(playlist));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deleta uma playlist.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist deletada.")
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<String> delete(@PathVariable("uid") String uid) {
        try {
            playlistService.delete(uid);
            return ResponseEntity.ok().body("Playlist deletada com sucesso!");
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{uid}/adicionar/{uidMusic}")
    public ResponseEntity<PlaylistDTO> addMusic(@PathVariable("uid") String uidPlaylist,@PathVariable("uidMusic") String uidMusic) throws ExistsException {
    
        try {
            var playlist = playlistService.addMusic(uidPlaylist, uidMusic);

            return ResponseEntity.ok(FactoryDTO.entityToDTO(playlist));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{uid}/remover/{uidMusic}")
    public ResponseEntity<PlaylistDTO> removeMusic(@PathVariable("uid") String uidPlaylist,@PathVariable("uidMusic") String uidMusic) {
    
        try {
            var playlist = playlistService.removeMusic(uidPlaylist, uidMusic);

            return ResponseEntity.ok(FactoryDTO.entityToDTO(playlist));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }
}
