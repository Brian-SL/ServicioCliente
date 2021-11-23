package com.example.Proyecto.configuration;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClienteException extends Exception{
    private String body;

    public ClienteException(String message) {
        this.body = message;
    }

    public ClienteException(String message, String message1) {
        super(message);
        this.body = message1;
    }

    /*
    public ClienteException(String message, String message1) {
        super(message);
        this.message = message1;
    }*/
}
