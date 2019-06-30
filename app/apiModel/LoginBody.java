package com.example.fakenews.apiModel;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * POJO para representar el cuerpo de la petición POST para el login
 */
public class LoginBody implements Serializable {
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("token_id")
    @Expose
    private String token_id;

    public LoginBody(String mail, String token_id) {
        this.mail = mail;
        this.token_id = token_id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTokenId() {
        return token_id;
    }

    public void setTokenId(String token_id) {
        this.token_id = token_id;
    }
}
