package dtos;

public class EstadoTareaDTO {

    private Integer id;
    private String nombre;
    private Integer proyectoId;
    private String color;
    private Integer orden;
    private Boolean esEstadoFinal;

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean getEsEstadoFinal() {
        return esEstadoFinal;
    }

    public void setEsEstadoFinal(Boolean esEstadoFinal) {
        this.esEstadoFinal = esEstadoFinal;
    }
}

