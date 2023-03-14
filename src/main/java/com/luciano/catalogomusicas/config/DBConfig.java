package com.luciano.catalogomusicas.config;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Configuration;

import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Genre;
import com.luciano.catalogomusicas.model.Music;
import com.luciano.catalogomusicas.model.Person;
import com.luciano.catalogomusicas.model.Playlist;
import com.luciano.catalogomusicas.repository.GenreRepository;
import com.luciano.catalogomusicas.repository.MusicRepository;
import com.luciano.catalogomusicas.repository.PersonRepository;
import com.luciano.catalogomusicas.repository.PlaylistRepository;
import com.luciano.catalogomusicas.service.GenreService;

@Configuration
@AllArgsConstructor
public class DBConfig {
    private MusicRepository musicRepository;
    
    private GenreRepository genreRepository;
    
    private GenreService genreService;
        
    private PersonRepository personRepository;
    
    private PlaylistRepository playlistRepository;

    @PostConstruct
    public void initDB() throws NotFoundException{
        initGenreDB();

        initMusicDB();

    }

    public void initGenreDB() {
        Genre genre1 = new Genre();
        genre1.setUid("b59c8b8b-2326-4f1d-bacf-dc45f30b13fa");
        genre1.setName("Rock");
        genreRepository.saveAndFlush(genre1);
        Genre genre2 = new Genre();
        genre2.setUid(UUID.randomUUID().toString());
        genre2.setName("Sertanejo");
        genreRepository.saveAndFlush(genre2);
        Genre genre3 = new Genre();
        genre3.setUid(UUID.randomUUID().toString());
        genre3.setName("Pagode");
        genreRepository.saveAndFlush(genre3);
    }

    public void initMusicDB() throws NotFoundException {
        Music music1 = new Music();
        music1.setUid("m59c8b8b-2326-4f1d-bacf-dc45f30b13fa");
        music1.setName("Sweet Child O'Mine");
        music1.setGenre(genreService.findByUid("b59c8b8b-2326-4f1d-bacf-dc45f30b13fa"));
        
        Person person1 = new Person();
        person1.setUid("p19c8b8b-2326-4f1d-bacf-dc45f30b13fa");
        person1.setName("Marcelo");
        person1.setLikedMusics(new ArrayList<Music>());
        person1.setPlaylists(new ArrayList<Playlist>());
        
        person1.setLikedMusics(List.of(music1));

        music1.setLikes(List.of(person1));

        musicRepository.saveAndFlush(music1);

        Playlist playlist1 = new Playlist();
        playlist1.setUid("pl1c8b8b-2326-4f1d-bacf-dc45f30b13fa");
        playlist1.setName("Favoritas");

        List<Music> musics = musicRepository.findAll();
        playlist1.setMusics(musics);
        
        List<Person> persons = personRepository.findAll();
        playlist1.setPersons(persons);

        playlistRepository.saveAndFlush(playlist1);

        person1.setPlaylists(List.of(playlist1));

        personRepository.saveAndFlush(person1);
    }

}
