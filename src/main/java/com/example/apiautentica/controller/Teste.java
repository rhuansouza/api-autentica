package com.example.apiautentica.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("autentica")
public class Teste {

    @GetMapping("/teste")
    String ola(){

        return "teste";
    }
}
