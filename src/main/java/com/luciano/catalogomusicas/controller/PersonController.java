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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.luciano.catalogomusicas.dto.FactoryDTO;
import com.luciano.catalogomusicas.dto.PersonDTO;
import com.luciano.catalogomusicas.dto.PlaylistDTO;
import com.luciano.catalogomusicas.exception.ExistsException;
import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Person;
import com.luciano.catalogomusicas.service.PersonService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import lombok.AllArgsConstructor;

@RestController
@RequestMapping(value = "/pessoa", name = "Operações sobre pessoas")
@AllArgsConstructor
public class PersonController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);
    private PersonService personService;

    @Operation(summary = "Lista todas as pessoas da base de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Todos as pessoas encontradas.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))))
    })
    @GetMapping
    public ResponseEntity<List<PersonDTO>> readAll() {
        return ResponseEntity.ok().body(personService.all().stream()
                .map(FactoryDTO::entityToDTO)
                .collect(Collectors.toList()));
    }

    @Operation(summary = "Retorna uma pessoa pelo UID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PersonDTO.class))),
            @ApiResponse(responseCode = "400", description = "Uid invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada.", content = @Content)
    })
    @GetMapping("/{uid}")
    public ResponseEntity<PersonDTO> read(@PathVariable("uid") String uid) {
        try {
            return ResponseEntity.ok().body(FactoryDTO.entityToDTO(personService.findByUid(uid)));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Cadastra uma pessoa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa cadastrada.")
    })
    @PostMapping()
    public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO personDTO) {

        personDTO.setUid(UUID.randomUUID().toString());
        Person person = FactoryDTO.dtoToEntity(personDTO);

        return ResponseEntity.ok().body(FactoryDTO.entityToDTO(personService.create(person)));

    }

    @Operation(summary = "Atualiza um pessoa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada.")
    })
    @PutMapping("/{uid}")
    public ResponseEntity<PersonDTO> update(@PathVariable("uid") String uid, @RequestBody PersonDTO personDTO) {
        Person person = FactoryDTO.dtoToEntity(personDTO);
        try {
            personService.update(uid, person);
            return ResponseEntity.ok(FactoryDTO.entityToDTO(person));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Deleta uma pessoa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pessoa deletada.")
    })
    @DeleteMapping("/{uid}")
    public ResponseEntity<String> delete(@PathVariable("uid") String uid) {
        try {
            personService.delete(uid);
            return ResponseEntity.ok().body("Pessoa deletada com sucesso!");
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Adiciona uma playlist a uma pessoa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist adicionada.")
    })
    @PutMapping("/{personUid}/playlist/{playlistUid}")
    public ResponseEntity<PersonDTO> addPlaylist(@PathVariable("personUid") String personUid,
            @PathVariable("playlistUid") String playlistUid) {

        try {
            Person person = personService.addPlaylist(personUid, playlistUid);

            return ResponseEntity.ok(FactoryDTO.entityToDTO(person));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }
    @Operation(summary = "Cria uma playlist para uma pessoa.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Playlist criada.")
    })
    @PostMapping("/{personUid}/playlist")
    public ResponseEntity<PersonDTO> createPlaylist(@PathVariable("personUid") String personUid,
            PlaylistDTO playlistDTO) {

        try {
            Person person = personService.addPlaylist(personUid, FactoryDTO.dtoToEntity(playlistDTO));

            return ResponseEntity.ok(FactoryDTO.entityToDTO(person));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Curte uma música.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Música curtida.")
    })
    @PutMapping("/curtir/{muid}/{puid}")
    public ResponseEntity<PersonDTO> like(@PathVariable("muid") String musicUid, @PathVariable("puid") String personUid)
            throws ExistsException {
        try {
            Person person = personService.like(musicUid, personUid);
            return ResponseEntity.ok(FactoryDTO.entityToDTO(person));
        } catch (NotFoundException e) {
            LOGGER.warn(e.toString());
            return ResponseEntity.notFound().build();
        }
    }
}
