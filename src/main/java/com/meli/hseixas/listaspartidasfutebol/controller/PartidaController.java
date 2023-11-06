package com.meli.hseixas.listaspartidasfutebol.controller;

import com.meli.hseixas.listaspartidasfutebol.service.PartidaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("partidas")
public class PartidaController {

    @Autowired
    private PartidaService partidaService;

}
