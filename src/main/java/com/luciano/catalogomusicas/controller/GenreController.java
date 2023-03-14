package com.luciano.catalogomusicas.controller;

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
import com.luciano.catalogomusicas.dto.GenreDTO;
import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Genre;
import com.luciano.catalogomusicas.service.GenreService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/genero", name = "Operações sobre gêneros")
@AllArgsConstructor
public class GenreController {

        private static final Logger LOGGER = LoggerFactory.getLogger(GenreController.class);
        private GenreService genreService;

        @Operation(summary = "Lista todos os gêneros da base de dados.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Todos os gêneros encontrados.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = GenreDTO.class))))
        })
        @GetMapping
        public ResponseEntity<List<GenreDTO>> readAll() {
                return ResponseEntity.ok().body(genreService.all().stream()
                                .map(FactoryDTO::entityToDTO)
                                .collect(Collectors.toList()));
        }

        @Operation(summary = "Retorna um gênero pelo UID.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Gênero encontrado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GenreDTO.class))),
                        @ApiResponse(responseCode = "400", description = "Uid invalido", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Gênero não encontrado.", content = @Content)
        })
        @GetMapping("/{uid}")
        public ResponseEntity<GenreDTO> read(@PathVariable("uid") String uid) {
                try {
                        return ResponseEntity.ok().body(FactoryDTO.entityToDTO(genreService.findByUid(uid)));
                } catch (NotFoundException e) {
                        LOGGER.warn(e.toString());
                        return ResponseEntity.notFound().build();
                }
        }

        @Operation(summary = "Cadastra um gênero.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Gênero cadastrado.")
        })
        @PostMapping()
        public ResponseEntity<GenreDTO> create(@RequestBody GenreDTO genreDTO) {
                genreDTO.setUid(UUID.randomUUID().toString());
                var genre = FactoryDTO.dtoToEntity(genreDTO);
                genre = genreService.create(genre);
                return ResponseEntity.ok().body(FactoryDTO.entityToDTO(genre));
        }

        @Operation(summary = "Atualiza um gênero.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Gênero atualizado.")
        })
        @PutMapping("/{uid}")
        public ResponseEntity<GenreDTO> update(@PathVariable("uid") String uid, @RequestBody GenreDTO genreDTO) {
                var newGenre = new Genre();
                var oldGenre = new Genre();
                try {
                        oldGenre = genreService.findByUid(uid);
                        newGenre.setId(oldGenre.getId());
                        newGenre.setUid(uid);
                        newGenre.setName(genreDTO.getName());
                        newGenre = genreService.update(uid, newGenre);
                        return ResponseEntity.ok().body(FactoryDTO.entityToDTO(newGenre));
                } catch (NotFoundException e) {
                        LOGGER.warn(e.toString());
                        return ResponseEntity.notFound().build();
                }
        }

        @Operation(summary = "Deleta um gênero.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Gênero deletado.")
        })
        @DeleteMapping("/{uid}")
        public ResponseEntity<String> delete(@PathVariable("uid") String uid) {
                try {
                        genreService.delete(uid);
                        return ResponseEntity.ok().body("Gênero deletado com sucesso!");
                } catch (NotFoundException e) {
                        LOGGER.warn(e.toString());
                        return ResponseEntity.notFound().build();
                }
        }
}
