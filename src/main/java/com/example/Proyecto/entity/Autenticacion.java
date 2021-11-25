package com.example.Proyecto.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Autenticacion")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Autenticacion {
    @Id
    private String usuario;
    private String contraseña;
}
