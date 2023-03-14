package com.luciano.catalogomusicas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Music;
import com.luciano.catalogomusicas.repository.MusicRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MusicService {

    MusicRepository musicRepository;
    GenreService genreService;

    public List<Music> all() {
        var musics = musicRepository.findAll();

        return musics;
    }

    public Music findByUid(String uid) throws NotFoundException {
        var list = musicRepository.findByUid(uid);
        var music = list.stream().findFirst();
        return music.orElseThrow(() -> new NotFoundException("Musica " + uid + " não encontrada."));
    }

    public Music create(Music music) throws NotFoundException {
        var genre = genreService.findByUid(music.getGenre().getUid());

        music.setGenre(genre);

        return musicRepository.saveAndFlush(music);
    }

    public Music update(String uid, Music musicSaved) throws NotFoundException {
        Music existingMusic = findByUid(uid);

        if (existingMusic == null) {
            return null;
        }

        existingMusic.setName(musicSaved.getName());
        existingMusic.setGenre(musicSaved.getGenre());

        try {
            return musicRepository.save(existingMusic);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(String uid) throws NotFoundException {

        Music existingMusic = musicRepository.findByUid(uid).stream().findFirst().orElse(null);

        if (existingMusic == null) {
            return false;
        }

        musicRepository.delete(existingMusic);

        return true;

    }

    
}
