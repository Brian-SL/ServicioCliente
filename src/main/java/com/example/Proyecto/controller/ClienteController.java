package com.example.Proyecto.controller;

import com.example.Proyecto.configuration.ClienteException;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/addCliente")
    public ResponseEntity<Cliente> addCliente(@RequestBody Cliente cliente) throws ClienteException {
        clienteService.addCliente(cliente);
        System.out.println("Cliente añadido");
        return ResponseEntity.ok(cliente);
        }

        //return ResponseEntity.ok(cliente);
}
