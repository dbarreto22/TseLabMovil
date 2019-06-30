package com.example.fakenews.apiModel;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String email;
    private String nickname;
    private String telefono;
    private String nombre;

    public Usuario() {

    }

    public Usuario(String email, String nickname, String telefono, String nombre) {
        this.email = email;
        this.nickname = nickname;
        this.telefono = telefono;
        this.nombre   = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
