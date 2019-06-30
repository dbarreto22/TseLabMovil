package com.example.fakenews.apiModel;

public class LoginResponse {
    private String jwt;
    private String rol;
    private String idPeriferico;

    public LoginResponse() {
    }

    public LoginResponse(String jwt, String rol, String idPeriferico) {
        this.jwt = jwt;
        this.rol = rol;
        this.idPeriferico = idPeriferico;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getIdPeriferico() {
        return idPeriferico;
    }

    public void setIdPeriferico(String idPeriferico) {
        this.idPeriferico = idPeriferico;
    }
}
