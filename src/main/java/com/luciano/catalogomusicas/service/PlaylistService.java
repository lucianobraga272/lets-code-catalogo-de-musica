package com.luciano.catalogomusicas.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.luciano.catalogomusicas.exception.ExistsException;
import com.luciano.catalogomusicas.exception.NotFoundException;
import com.luciano.catalogomusicas.model.Music;
import com.luciano.catalogomusicas.model.Playlist;
import com.luciano.catalogomusicas.repository.PlaylistRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlaylistService {

    PlaylistRepository playlistRepository;
    MusicService musicService;

    public List<Playlist> all() {
        return playlistRepository.findAll();
    }

    public Playlist findByUid(String uid) throws NotFoundException {
        var list = playlistRepository.findByUid(uid);
        var playlist = list.stream().findFirst();
        return playlist.orElseThrow(() -> new NotFoundException("Pessoa " + uid + " não encontrada."));
    }

    public Playlist create(Playlist playlist) {
        return playlistRepository.saveAndFlush(playlist);
    }

    public Playlist update(String uid, Playlist playlistSaved) throws NotFoundException {
        Playlist existingPlaylist = findByUid(uid);

        if (existingPlaylist == null) {
            return null;
        }

        existingPlaylist.setName(playlistSaved.getName());
        existingPlaylist.setMusics(playlistSaved.getMusics());
        try {
            return playlistRepository.saveAndFlush(existingPlaylist);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(String uid) throws NotFoundException {

        Playlist existingPlaylist = playlistRepository.findByUid(uid).stream().findFirst().orElse(null);

        if (existingPlaylist == null) {
            return false;
        }

        playlistRepository.delete(existingPlaylist);

        return true;

    }

    public Playlist addMusic(String uidPlaylist, String uidMusic) throws NotFoundException, ExistsException {
        Playlist playlist = findByUid(uidPlaylist);
        Music music = musicService.findByUid(uidMusic);
       
        var musics = playlist.getMusics();
        
        if(musics.contains(music)){
            throw new ExistsException("Música +" + music.getName() + " já existe na playlist!");
        }

        musics.add(music);

        playlist.setMusics(musics);

        return update(uidPlaylist, playlist);
    }

    public Playlist removeMusic(String uidPlaylist, String uidMusic) throws NotFoundException {
        Playlist playlist = findByUid(uidPlaylist);
        Music music = musicService.findByUid(uidMusic);
       
        var musics = playlist.getMusics();
        musics.remove(music);

        playlist.setMusics(musics);

        return update(uidPlaylist, playlist);
    }

}
