package dtos;

import java.time.LocalDateTime;

// Este DTO es para MOSTRAR un usuario.
// Fíjate que NO INCLUYE el campo 'password' ni 'id'.

public class UsuarioDTO {

    // --- ID ELIMINADO ---
    private String nombre;
    private String email;
    private String avatarUrl;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaUltimaSesion;
    private Boolean activo;

    // --- Getters y Setters (sin los de ID) ---

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