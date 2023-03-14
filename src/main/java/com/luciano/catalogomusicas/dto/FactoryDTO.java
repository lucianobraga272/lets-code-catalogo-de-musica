package com.luciano.catalogomusicas.dto;

import java.util.ArrayList;
import java.util.stream.Collectors;

import com.luciano.catalogomusicas.model.Genre;
import com.luciano.catalogomusicas.model.Music;
import com.luciano.catalogomusicas.model.Person;
import com.luciano.catalogomusicas.model.Playlist;

public class FactoryDTO {

    public static GenreDTO entityToDTO(Genre genre) {
        if (genre == null) {
            return null;
        }
        return new GenreDTO(genre.getUid(), genre.getName());
    }

    public static MusicDTO entityToDTO(Music music) {
        if (music == null) {
            return null;
        }
        MusicDTO musicDTO = new MusicDTO();

        musicDTO.setUid(music.getUid());
        musicDTO.setName(music.getName());
        musicDTO.setGenre(entityToDTO(music.getGenre()));

        if (music.getLikes() != null && music.getLikes().size() > 0) {
            musicDTO.setLikes(music.getLikes().size());
        }

        return musicDTO;
    }

    public static PersonDTO entityToDTO(Person user) {
        if (user == null) {
            return null;
        }
        var personDTO =  new PersonDTO();
        personDTO.setName(user.getName());
        personDTO.setUid(user.getUid());
        if(user.getPlaylists()!=null && user.getPlaylists().size()>0){
            personDTO.setPlaylists(user.getPlaylists().stream().map(p -> entityToDTO(p)).collect(Collectors.toList()));
        }else {
            personDTO.setPlaylists(new ArrayList<PlaylistDTO>());
        }

        if(user.getLikedMusics()!=null && user.getLikedMusics().size()>0){
            personDTO.setLikedMusics(user.getLikedMusics().stream().map(m -> entityToDTO(m)).collect(Collectors.toList()));
        }else {
            personDTO.setPlaylists(new ArrayList<PlaylistDTO>());
        }
        return personDTO;
    }

    public static PlaylistDTO entityToDTO(Playlist playlist) {
        if (playlist == null) {
            return null;
        }
        var playlistDTO = new PlaylistDTO();

        playlistDTO.setUid(playlist.getUid());
        playlistDTO.setName(playlist.getName());
        if (playlist.getMusics() != null && playlist.getMusics().size() > 0) {
            playlistDTO.setMusics(playlist.getMusics().stream()
                    .map((Music music) -> {
                        return entityToDTO(music);
                    })
                    .collect(Collectors.toList()));
        }

        return playlistDTO;
    }

    public static Genre dtoToEntity(GenreDTO genreDTO) {
        if (genreDTO == null) {
            return null;
        }
        var genre = new Genre();
        genre.setUid(genreDTO.getUid());
        genre.setName(genreDTO.getName());
        return genre;
    }

    public static Music dtoToEntity(MusicDTO musicDTO) {
        if (musicDTO == null) {
            return null;
        }
        var music = new Music();
        music.setUid(musicDTO.getUid());
        music.setName(musicDTO.getName());
        music.setGenre(dtoToEntity(musicDTO.getGenre()));

        return music;
    }

    public static Person dtoToEntity(PersonDTO personDTO) {
        if (personDTO == null) {
            return null;
        }
        var person = new Person();
        person.setUid(personDTO.getUid());
        person.setName(personDTO.getName());
        return person;
    }

    public static Playlist dtoToEntity(PlaylistDTO playlistDTO) {
        if (playlistDTO == null) {
            return null;
        }
        var playlist = new Playlist();
        playlist.setUid(playlistDTO.getUid());
        playlist.setName(playlistDTO.getName());
        if(playlistDTO.getMusics() != null && playlistDTO.getMusics().size()>0){
            playlist.setMusics(playlistDTO.getMusics().stream()
            .map(m -> dtoToEntity(m))
            .collect(Collectors.toList()));
        } else{
            playlist.setMusics(new ArrayList<Music>());
        }
        
        return playlist;
    }
}
