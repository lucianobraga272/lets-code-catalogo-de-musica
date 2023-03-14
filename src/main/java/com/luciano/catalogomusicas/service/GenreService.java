package com.luciano.catalogomusicas.service;

import org.springframework.stereotype.Service;

import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Genre;
import com.luciano.catalogomusicas.repository.GenreRepository;

import java.util.List;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class GenreService {

    GenreRepository genreRepository;

    public List<Genre> all(){
        return genreRepository.findAll();
    }

    public Genre findByUid(String uid) throws NotFoundException {
        var list = genreRepository.findByUid(uid);
        var genre = list.stream().findFirst();
        return genre.orElseThrow(() -> new NotFoundException("Categoria "+ uid +" não encontrada."));
    }

    public Genre create(Genre genre){
        try{
           return genreRepository.saveAndFlush(genre);
        }catch (Exception e) {
            return null;
        }
    }

    public Genre update(String uid, Genre genreSaved) throws NotFoundException{
        Genre existingGenre = findByUid(uid);

        if (existingGenre == null) {
            return null;
        }

        existingGenre.setName(genreSaved.getName());
        try {
            return genreRepository.saveAndFlush(existingGenre);
        } catch (Exception e) {
            return null;
        }
    }

    public void delete(String uid) throws NotFoundException{
        
        var genre = findByUid(uid);
        
        genreRepository.delete(genre);

    }
}