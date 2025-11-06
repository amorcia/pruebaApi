package dtos;

import java.time.LocalDateTime;

// Este DTO es para MOSTRAR un usuario.
// Fíjate que NO INCLUYE el campo 'password' por seguridad.

public class UsuarioDTO {

    private Integer id;
    private String nombre;
    private String email;
    private String avatarUrl;
    private Integer rolId;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaSesion;
    private Boolean activo;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaUltimaSesion() {
        return fechaUltimaSesion;
    }

    public void setFechaUltimaSesion(LocalDateTime fechaUltimaSesion) {
        this.fechaUltimaSesion = fechaUltimaSesion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}

