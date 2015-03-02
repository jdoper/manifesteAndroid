package com.example.pedro.manifeste;

import com.orm.SugarRecord;

/**
 * Created by pedro on 15/01/15.
 */
public class Ocorrencia extends SugarRecord<Ocorrencia> {
    private double latitude;
    private double longitude;
    private String tipo;

    public Ocorrencia() {}

    public Ocorrencia(double latitude, double longitude, String tipo) {
        this.latitude = latitude;
        this. longitude = longitude;
        this.tipo = tipo;
    }

    // Latitude
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    // Longitude
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    // Tipo
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }
}
