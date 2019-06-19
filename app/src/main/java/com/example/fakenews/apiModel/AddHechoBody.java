package com.example.fakenews.apiModel;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

import java.io.Serializable;


public class AddHechoBody implements Serializable {
    @SerializedName("titulo")
    @Expose
    private String titulo;

    @SerializedName("url")
    @Expose
    private String utl;

    public AddHechoBody(String titulo, String url) {
        this.utl = url;
        this.titulo = titulo;
    }

    public String getUtl() {
        return utl;
    }

    public void setUtl(String utl) {
        this.utl = utl;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

}
