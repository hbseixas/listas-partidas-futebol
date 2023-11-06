package com.meli.hseixas.listaspartidasfutebol.service;

import com.meli.hseixas.listaspartidasfutebol.repository.PartidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PartidaService {

    @Autowired
    private PartidaRepository partidaRepository;


}
