package com.example.Proyecto.service;

import com.example.Proyecto.configuration.ClienteException;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    public void addCliente(Cliente cliente) throws ClienteException {
        Optional<Cliente> optionalCliente = buscarClientePorUsuario(cliente.getUsuario());
        if(optionalCliente.isPresent()){
            //excepcion, el cliente ya está dado de alta
            throw new ClienteException("El cliente ya esta dado de alta");
        }else{
            if(cliente.getEdad()<18){
                //excepcion
                throw new ClienteException("Eres menor de edad");
            }else{
                if(cliente.getCelular().length() != 10){
                    //excepcion
                    throw new ClienteException("La longitud del número de celular debe de ser de 10 dígitos");
                }else{
                    if(cliente.getEmail().contains("@")){
                        if (cliente.getContraseña().length()<6){
                            throw new ClienteException("La longitud de la contraseña debe ser mínimo 6 caracteres");
                        }else{
                            boolean num=false;
                            boolean alfanum=false;

                            for (int i=0; i<cliente.getContraseña().length(); i++){
                                if(cliente.getContraseña().charAt(i) >48 && cliente.getContraseña().charAt(i) <58)
                                    num=true;

                                if(cliente.getContraseña().charAt(i) >72 && cliente.getContraseña().charAt(i) <123)
                                    alfanum=true;
                            }

                            if (num==true && alfanum==true){
                                clienteRepository.addCliente(cliente);
                            }else{
                                //excepcion
                                throw new ClienteException("La contraseña debe de contener al menos un caracter numérico y un alfanumérico");
                            }
                        }

                    }else{
                        //excepcion
                        throw new ClienteException("El email debe contener un @");
                    }
                }
            }
        }
    }

    public void deleteCliente(String usuario) {
        clienteRepository.deleteCliente(usuario);
    }

    public List<Cliente> getClientes() {
        return clienteRepository.getAll();
    }

    public void updateCliente(Cliente cliente) {
        clienteRepository.updateCliente(cliente);
    }

    public Optional<Cliente> buscarClientePorUsuario(String usuario){
        return clienteRepository.buscarClientePorUsuario(usuario);
    }
}
