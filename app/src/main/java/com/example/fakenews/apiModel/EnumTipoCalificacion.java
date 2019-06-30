package com.example.fakenews.apiModel;

public enum EnumTipoCalificacion {
    VERDADERO("Verdadero"),
    VERD_A_MEDIAS("Verdadero a medias"),
    INFLADO("Inflado"),
    ENGANOSO("Engañoso"),
    FALSO("Falso"),
    RIDICULO("Ridículo");

    private String tipoCalificacionStr;

    EnumTipoCalificacion(String tipoCalificacionStr){
        this.tipoCalificacionStr = tipoCalificacionStr;
    }

    public String tipoCalificacionStr() {
        return tipoCalificacionStr;
    }
}