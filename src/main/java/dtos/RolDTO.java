package dtos;

// Este es un DTO (Data Transfer Object)
// Es un objeto simple (POJO) sin anotaciones de JPA.
// Lo usamos para transferir datos de forma segura a través de la API.

public class RolDTO {

    private Integer id;
    private String nombre;
    private String color;
    private String permisos; // JSON como String
    private String descripcion;

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPermisos() {
        return permisos;
    }

    public void setPermisos(String permisos) {
        this.permisos = permisos;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

