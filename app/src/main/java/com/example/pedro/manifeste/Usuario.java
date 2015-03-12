package com.example.pedro.manifeste;

import com.orm.SugarRecord;

/**
 * Created by pedro on 03/03/15.
 */
public class Usuario extends SugarRecord<Usuario> {
    private String login;
    private String senha;
    private String token;
    private double latitude;

    private double longitude;

    public Usuario() {}

    public Usuario(String login, String senha, String token, double latitude, double longitude) {
        this.login = login;
        this.senha = senha;
        this.token = token;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
