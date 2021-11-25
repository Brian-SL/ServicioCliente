package com.example.Proyecto.controller;

import com.example.Proyecto.configuration.ClienteException;
import com.example.Proyecto.entity.Autenticacion;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.Cuenta;
import com.example.Proyecto.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/buscarCliente/{id}")
    public ResponseEntity<Cliente> buscarCliente(@PathVariable("id") String usuario){
        Optional<Cliente> cliente=clienteService.buscarClientePorUsuario(usuario);
        if(cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(cliente.get());
        }
    }

    @GetMapping("/loginCliente")
    public ResponseEntity<String> logginCliente(@RequestBody Cliente cliente) throws ClienteException {
        clienteService.loginCliente(cliente);
        //System.out.println("Cliente añadido");
        clienteService.login(cliente);
        return ResponseEntity.ok("Bienvenido");
    }

    @PostMapping("/agregarCuenta/{usuario}")
    public ResponseEntity<Cliente> addCuenta(@PathVariable("usuario") String usuario, @RequestBody Cuenta cuenta) {
        Optional<Cliente> cliente=clienteService.addcuenta(usuario,cuenta);
        if(cliente.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok(cliente.get());
        }
    }

    @PutMapping("/desbloqueoUsuario/{usuario}/{celular}")
    public ResponseEntity<String> desbloqueoUsuario(@PathVariable("usuario") String usuario,
                                                     @PathVariable("celular") String celular) throws ClienteException {
        Optional<Cliente> clienteOptional=clienteService.desbloqueoUsuario(usuario,celular);
        if(clienteOptional.isEmpty()){
            return ResponseEntity.notFound().build();
        }else{
            return ResponseEntity.ok("Finalizada");
        }
    }

    @GetMapping("islogged/{usuario}")
    public boolean islogged(@PathVariable String usuario){
        return clienteService.islogged(usuario);
    }
}
