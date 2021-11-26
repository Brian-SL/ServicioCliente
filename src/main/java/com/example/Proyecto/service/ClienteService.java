package com.example.Proyecto.service;

import com.example.Proyecto.configuration.ClienteException;
import com.example.Proyecto.entity.Autenticacion;
import com.example.Proyecto.entity.Cliente;
import com.example.Proyecto.entity.Cuenta;
import com.example.Proyecto.repository.AutenticacionDao;
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

    @Autowired
    AutenticacionDao autenticacionDao;

    private int intentos=0;

    public void addCliente(Cliente cliente) throws ClienteException {
        intentos=0;
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


    public Optional<Cliente> loginCliente(String usuario, String contraseña) throws ClienteException {
        Optional<Cliente> clienteOptional= buscarClientePorUsuario(usuario);
        if (clienteOptional.isPresent()) {
            if (clienteOptional.get().isBloqueado()){
                throw new ClienteException("Usuario bloqueado :(");
            }else {
                Optional<Cliente> cliente=clienteRepository.loginCliente(usuario, contraseña);
                if (cliente.isPresent()){
                    intentos=0;
                    return clienteRepository.loginCliente(usuario, contraseña);
                }else{
                    intentos++;

                    if (intentos>=3){
                        clienteRepository.bloquearUsuario(usuario);
                        throw new ClienteException("Usuario bloqueado");
                    }

                    throw new ClienteException("Contraseña incorrecta. Intentos: "+ intentos);
                }

            }

        }else{
                throw new ClienteException("Usuario incorrecto");
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
        intentos=0;
        Optional<Cliente> clienteOptional= buscarClientePorUsuario(usuario);
        if(clienteOptional.isPresent()){
            if(clienteOptional.get().getCelular().equals(celular)){
                clienteOptional.get().setBloqueado(false);
                clienteRepositoryDao.save(clienteOptional.get());
                return clienteOptional;
            }else{
                throw new ClienteException("Fallida");
            }


        }else {
            throw new ClienteException("Fallida");
        }
    }

    public void login(String usuario, String contraseña){
        Autenticacion autenticacion= new Autenticacion(usuario, contraseña);
        autenticacionDao.save(autenticacion);
    }

    public boolean islogged(String user){
        Optional<Autenticacion> login= autenticacionDao.findById(user);
        return login.isPresent() ? true:false;
    }

    public Optional<Cliente> cambiarContraseña(String usuario, String contraseña) throws ClienteException {
        Optional<Cliente> clienteOptional= buscarClientePorUsuario(usuario);
        if(clienteOptional.isPresent()){
            if (contraseña.length()<6){
                throw new ClienteException("La longitud de la contraseña debe ser mínimo 6 caracteres");
            }else{
                boolean num=false;
                boolean alfanum=false;

                for (int i=0; i<contraseña.length(); i++){
                    if(contraseña.charAt(i) >48 && contraseña.charAt(i) <58)
                        num=true;

                    if(contraseña.charAt(i) >72 && contraseña.charAt(i) <123)
                        alfanum=true;
                }

                if (num==true && alfanum==true){
                    clienteOptional.get().setContraseña(contraseña);
                    clienteRepositoryDao.save(clienteOptional.get());
                    return clienteOptional;
                }else{
                    throw new ClienteException("La contraseña debe de contener al menos un caracter numérico y un alfanumérico");
                }
            }
        }else{
            throw new ClienteException("Usuario incorrecto");
        }
    }

    public Optional<Cliente> recuperarUsuario( String email) throws ClienteException {
        Optional<Cliente> clienteOptional= clienteRepository.buscarClientePorEmail(email);
        if (clienteOptional.isPresent()){
            return clienteOptional;
        }else{
            throw new ClienteException("Email incorrecto");
        }
    }

    public Optional<Cliente> cambiarCelular(String usuario, String celular) throws ClienteException {
        Optional<Cliente> clienteOptional= buscarClientePorUsuario(usuario);
        if(clienteOptional.isPresent()){
            if (celular.length() !=10){
                throw new ClienteException("La longitud del número de celular debe ser de 10 caracteres");
            }else{
                clienteOptional.get().setCelular(celular);
                clienteRepositoryDao.save(clienteOptional.get());
                return clienteOptional;
            }
        }else{
            throw new ClienteException("Usuario incorrecto");
        }
    }

    public Optional<Cliente> cambiarEmail(String usuario, String email) throws ClienteException {
        Optional<Cliente> clienteOptional= buscarClientePorUsuario(usuario);
        if(clienteOptional.isPresent()){
            if (email.contains("@")){
                clienteOptional.get().setEmail(email);
                clienteRepositoryDao.save(clienteOptional.get());
                return clienteOptional;
            }else{
                throw new ClienteException("El nuevo Email debe contener un @");
            }
        }else{
            throw new ClienteException("Usuario incorrecto");
        }
    }


}
