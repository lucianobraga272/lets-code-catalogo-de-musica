package com.luciano.catalogomusicas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.luciano.catalogomusicas.exception.ExistsException;
import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Music;
import com.luciano.catalogomusicas.model.Person;
import com.luciano.catalogomusicas.model.Playlist;
import com.luciano.catalogomusicas.repository.PersonRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PersonService {
    PersonRepository personRepository;

    PlaylistService playlistService;
    
    MusicService musicService;

    public List<Person> all() {
        return personRepository.findAll();
    }

    public Person findByUid(String uid) throws NotFoundException {
        var list = personRepository.findByUid(uid);
        var person = list.stream().findFirst();
        return person.orElseThrow(() -> new NotFoundException("Pessoa " + uid + " não encontrada."));
    }

    public Person create(Person person) {
        return personRepository.saveAndFlush(person);
    }

    public Person update(String uid, Person personSaved) throws NotFoundException {
        Person existingPerson = findByUid(uid);

        if (existingPerson == null) {
            return null;
        }

        existingPerson.setLikedMusics(personSaved.getLikedMusics());
        existingPerson.setName(personSaved.getName());
        existingPerson.setPlaylists(personSaved.getPlaylists());
        
        try {
            return personRepository.saveAndFlush(existingPerson);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(String uid) throws NotFoundException {

        Person existingPerson = personRepository.findByUid(uid).stream().findFirst().orElse(null);

        if (existingPerson == null) {
            return false;
        }

        personRepository.delete(existingPerson);

        return true;

    }

    public Person addPlaylist(String personUid, String playlistUid) throws NotFoundException {
        Playlist playlist = playlistService.findByUid(playlistUid);

        Person person = findByUid(personUid);

        var playlists = person.getPlaylists();
        playlists.add(playlist);
        person.setPlaylists(playlists);

        person = update(playlistUid, person);

        return person;
    }
    public Person addPlaylist(String personUid, Playlist playlist) throws NotFoundException {
        
        Person person = findByUid(personUid);

        var playlists = person.getPlaylists();
        playlists.add(playlist);
        person.setPlaylists(playlists);

        person = update(personUid, person);

        return person;
    }

    public Person like(String musicUid, String personUid) throws NotFoundException, ExistsException {
        Music music = musicService.findByUid(musicUid);
        Person person = findByUid(personUid);
        if(music.getLikes().contains(person) || person.getLikedMusics().contains(music)){
           throw new ExistsException("Música " + music.getName() +  " já curtida por " + person.getName() +  "!");
        }

        var likes = music.getLikes();
        likes.add(person);
        music.setLikes(likes);
        
        var likedMusics = person.getLikedMusics();
        likedMusics.add(music);
        person.setLikedMusics(likedMusics);

        music = musicService.update(musicUid, music);

        update(personUid, person);

        

        return person;
    }
}
