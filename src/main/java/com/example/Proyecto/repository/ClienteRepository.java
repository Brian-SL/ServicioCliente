package com.example.Proyecto.repository;

import com.example.Proyecto.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ClienteRepository {
    @Autowired
    ClienteRepositoryDao clienteRepositoryDao;

    public void addCliente(Cliente cliente){
        clienteRepositoryDao.save(cliente);
    }

    public void deleteCliente(String usuario){
         //clienteRepositoryDao.deleteById(usuario);
    }

    public List<Cliente> getAll(){
        return (List<Cliente>) clienteRepositoryDao.findAll();
    }

    public void updateCliente(Cliente cliente){
        clienteRepositoryDao.save(cliente);
    }


    public Optional<Cliente> buscarClientePorUsuario(String usuario){
        return clienteRepositoryDao.buscarClientePorUsuario(usuario);
    }

    public Optional<Cliente> loginCliente(String usuario, String contraseña) {
        return clienteRepositoryDao.loginCliente(usuario, contraseña);
    }

    public void bloquearUsuario(String usuario){
        Optional<Cliente> optionalCliente = buscarClientePorUsuario(usuario);
        if (optionalCliente.isPresent()){
            optionalCliente.get().setBloqueado(true);
            clienteRepositoryDao.save(optionalCliente.get());
        }
    }

    public Optional<Cliente> buscarClientePorEmail(String email){
        return clienteRepositoryDao.buscarClientePorEmail(email);
    }
}
