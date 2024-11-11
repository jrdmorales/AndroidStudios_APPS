package jordan.morales.verduritassa;

import java.io.Serializable;

public class Cultivo implements Serializable {
    private String alias;
    private String planta;
    private String fechaSiembra;
    private String fechaCosecha;

    public Cultivo(String alias, String planta, String fechaSiembra, String fechaCosecha) {
        this.alias = alias;
        this.planta = planta;
        this.fechaSiembra = fechaSiembra;
        this.fechaCosecha = fechaCosecha;
    }

    public String getAlias() { return alias; }
    public String getPlanta() { return planta; }
    public String getFechaSiembra() { return fechaSiembra; }
    public String getFechaCosecha() { return fechaCosecha; }
}
