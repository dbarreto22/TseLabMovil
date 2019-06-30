package com.example.fakenews.apiModel;

import java.io.Serializable;
import java.util.Date;

public class Hecho implements Serializable {

    private Long id;
    private String titulo;
    private String url;
    private EnumTipoCalificacion calificacion;
    private Date fechaInicioVerificacion;
    private Date fechaFinVerificacion;
    private String justificacion;
    private EnumHechoEstado estado;
    private Usuario submitter;
    private Usuario checker;
/*    private List<ResultadoMecanismo> resultadosMecanismos;*/


    public Hecho() {

    }

    public Hecho(Long id, String titulo, String url) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
    }

    public Hecho(String titulo, String url) {
        this.titulo = titulo;
        this.url = url;
    }

    public Hecho(Long id, String titulo, String url, EnumTipoCalificacion calificacion, Date fechaInicioVerificacion,
                 Date fechaFinVerificacion, String justificacion, EnumHechoEstado estado, Usuario submitter,
                 Usuario checker) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.calificacion = calificacion;
        this.fechaInicioVerificacion = fechaInicioVerificacion;
        this.fechaFinVerificacion = fechaFinVerificacion;
        this.justificacion = justificacion;
        this.estado = estado;
        this.submitter = submitter;
        this.checker = checker;
    }
/*
    public Hecho(Long id, String titulo, String url, EnumTipoCalificacion calificacion, Date fechaInicioVerificacion,
                 Date fechaFinVerificacion, String justificacion, EnumHechoEstado estado){, Submitter submitter,
                 Checker checker, List<ResultadoMecanismo> resultadosMecanismos) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.calificacion = calificacion;
        this.fechaInicioVerificacion = fechaInicioVerificacion;
        this.fechaFinVerificacion = fechaFinVerificacion;
        this.justificacion = justificacion;
        this.estado = estado;
        this.submitter = submitter;
        this.checker = checker;
        this.resultadosMecanismos = resultadosMecanismos;
    }*/

    public Hecho(Long id, EnumTipoCalificacion calificacion, String justificacion) {
        this.id = id;
        this.calificacion = calificacion;
        this.justificacion = justificacion;
    }

    public Hecho(Long id, EnumHechoEstado estado) {
        this.id = id;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public EnumTipoCalificacion getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(EnumTipoCalificacion calificacion) {
        this.calificacion = calificacion;
    }

    public Date getFechaInicioVerificacion() {
        return fechaInicioVerificacion;
    }

    public void setFechaInicioVerificacion(Date fechaInicioVerificacion) {
        this.fechaInicioVerificacion = fechaInicioVerificacion;
    }

    public Date getFechaFinVerificacion() {
        return fechaFinVerificacion;
    }

    public void setFechaFinVerificacion(Date fechaFinVerificacion) {
        this.fechaFinVerificacion = fechaFinVerificacion;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public EnumHechoEstado getEstado() {
        return estado;
    }

    public void setEstado(EnumHechoEstado estado) {
        this.estado = estado;
    }

    public Usuario getSubmitter() {
        return submitter;
    }

    public void setSubmitter(Usuario submitter) {
        this.submitter = submitter;
    }

    public Usuario getChecker() {
        return checker;
    }

    public void setChecker(Usuario checker) {
        this.checker = checker;
    }

    /*public List<ResultadoMecanismo> getResultadosMecanismos() {
        return resultadosMecanismos;
    }

    public void setResultadosMecanismos(List<ResultadoMecanismo> resultadosMecanismos) {
        this.resultadosMecanismos = resultadosMecanismos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((calificacion == null) ? 0 : calificacion.hashCode());
        result = prime * result + ((checker == null) ? 0 : checker.hashCode());
        result = prime * result + ((estado == null) ? 0 : estado.hashCode());
        result = prime * result + ((fechaFinVerificacion == null) ? 0 : fechaFinVerificacion.hashCode());
        result = prime * result + ((fechaInicioVerificacion == null) ? 0 : fechaInicioVerificacion.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((justificacion == null) ? 0 : justificacion.hashCode());
        result = prime * result + ((submitter == null) ? 0 : submitter.hashCode());
        result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Hecho other = (Hecho) obj;
        if (calificacion != other.calificacion)
            return false;
        if (checker == null) {
            if (other.checker != null)
                return false;
        } else if (!checker.equals(other.checker))
            return false;
        if (estado != other.estado)
            return false;
        if (fechaFinVerificacion == null) {
            if (other.fechaFinVerificacion != null)
                return false;
        } else if (!fechaFinVerificacion.equals(other.fechaFinVerificacion))
            return false;
        if (fechaInicioVerificacion == null) {
            if (other.fechaInicioVerificacion != null)
                return false;
        } else if (!fechaInicioVerificacion.equals(other.fechaInicioVerificacion))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (justificacion == null) {
            if (other.justificacion != null)
                return false;
        } else if (!justificacion.equals(other.justificacion))
            return false;
        if (submitter == null) {
            if (other.submitter != null)
                return false;
        } else if (!submitter.equals(other.submitter))
            return false;
        if (titulo == null) {
            if (other.titulo != null)
                return false;
        } else if (!titulo.equals(other.titulo))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }
*/
}