package jordan.morales.verduritassa;

import java.io.Serializable;

public class Cultivo implements Serializable {
    private String id;  // Este es el ID del cultivo
    private String alias;
    private String planta;
    private String fechaSiembra;
    private String fechaCosecha;

    // Constructor
    public Cultivo(String id, String alias, String planta, String fechaSiembra, String fechaCosecha) {
        this.id = id;
        this.alias = alias;
        this.planta = planta;
        this.fechaSiembra = fechaSiembra;
        this.fechaCosecha = fechaCosecha;
    }

    // Métodos getter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id; // Establecer el ID
    }

    public String getAlias() {
        return alias;
    }

    public String getPlanta() {
        return planta;
    }

    public String getFechaSiembra() {
        return fechaSiembra;
    }

    public String getFechaCosecha() {
        return fechaCosecha;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Cultivo cultivo = (Cultivo) obj;
        return id != null && id.equals(cultivo.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}

