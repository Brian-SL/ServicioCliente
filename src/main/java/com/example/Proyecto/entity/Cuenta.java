package com.example.Proyecto.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Cuentas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cuenta {

    @Id
    @Column(name="ID")
    private String cbu;


    private Float saldo;
    private String moneda;
    private String tipoDeCuenta;




    @OneToMany(cascade = CascadeType.ALL)
    @Column(name = "Cuenta_Movimiento")
    private List<Movimiento> movimientos;

    @OneToOne
    private TarjetaDebito tarjetaDebito;
}
