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
}
