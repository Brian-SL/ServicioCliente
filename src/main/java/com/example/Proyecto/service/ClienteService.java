package com.example.Proyecto.service;

import com.example.Proyecto.configuration.ClienteException;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.Cuenta;
import com.example.Proyecto.repository.ClienteRepository;
import com.example.Proyecto.repository.ClienteRepositoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {
    @Autowired
    ClienteRepository clienteRepository;

    @Autowired
    ClienteRepositoryDao clienteRepositoryDao;

    private int intentos=0;

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


    public Optional<Cliente> loginCliente(Cliente cliente) throws ClienteException {
        Optional<Cliente> optionalCliente =clienteRepository.loginCliente(cliente.getUsuario(), cliente.getContraseña());;
        if (optionalCliente.isPresent()) {
            //System.out.println("bloqueado " + optionalCliente.get().isBloqueado());
            if (optionalCliente.get().isBloqueado()){
                throw new ClienteException("Usuario bloqueado :(");
            }else {
                //Optional<Cliente> optionalCliente2 =clienteRepository.loginCliente(cliente.getUsuario(), cliente.getContraseña());
                intentos=0;
                //if (op)
                return clienteRepository.loginCliente(cliente.getUsuario(), cliente.getContraseña());
            }
        }
        else {
            intentos++;

            if (intentos>=3){
                clienteRepository.bloquearUsuario(cliente.getUsuario());
                throw new ClienteException("Usuario bloqueado");
            }
            throw new ClienteException("Contraseña incorrecta. Intentos: "+ intentos);
        }
    }


    public Optional<Cliente> addcuenta(String usuario, Cuenta cuenta) {
        Optional<Cliente> clienteOptional= buscarClientePorUsuario(usuario);
        if(clienteOptional.isPresent()){
            List<Cuenta> listacuentas=clienteOptional.get().getCuentas();
            listacuentas.add(cuenta);
            clienteOptional.get().setCuentas(listacuentas);
            clienteRepositoryDao.save(clienteOptional.get());
        }

        return clienteOptional;
    }


    public Optional<Cliente> desbloqueoUsuario(String usuario, String celular) throws ClienteException {
        Optional<Cliente> clienteOptional= buscarClientePorUsuario(usuario);
        if(clienteOptional.isPresent()){

                clienteOptional.get().setBloqueado(false);
                clienteRepositoryDao.save(clienteOptional.get());
                return clienteOptional;

        }else {
            throw new ClienteException("Fallida");
        }
    }

}
