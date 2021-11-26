package com.example.Proyecto.repository;

import com.example.Proyecto.entity.Cliente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepositoryDao extends CrudRepository<Cliente, Integer> {

    @Query (value = "select * from Clientes where usuario= :usuario", nativeQuery = true)
    public Optional<Cliente> buscarClientePorUsuario(String usuario);

    @Query (value = "select * from Clientes where usuario = :usuario and contraseña = :contraseña", nativeQuery = true)
    Optional<Cliente> loginCliente(String usuario, String contraseña);

    @Query (value = "select * from Clientes where email= :email", nativeQuery = true)
    Optional<Cliente> buscarClientePorEmail(String email);
}
