package cl.inacap.evaluacion1.clases;

import java.util.Date;

public class verduras {
   public String tipo;
    public int diasCocecha;
    public Date fechaCocecha;
    public Date fechaInicio;

    public verduras(String tipo, int diasCocecha, Date fechaCocecha, Date fechaInicio) {
        this.tipo = tipo;
        this.diasCocecha = diasCocecha;
        this.fechaCocecha = fechaCocecha;
        this.fechaInicio = fechaInicio;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getDiasCocecha() {
        return diasCocecha;
    }

    public void setDiasCocecha(int diasCocecha) {
        this.diasCocecha = diasCocecha;
    }

    public Date getFechaCocecha() {
        return fechaCocecha;
    }

    public void setFechaCocecha(Date fechaCocecha) {
        this.fechaCocecha = fechaCocecha;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}

