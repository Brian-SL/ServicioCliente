package com.example.Proyecto.repository;

import com.example.Proyecto.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryDao extends CrudRepository<Cliente, Integer> {

    @Query (value = "select * from Clientes where usuario= :usuario", nativeQuery = true)
    public Optional<Cliente> buscarClientePorUsuario(String usuario);
}
