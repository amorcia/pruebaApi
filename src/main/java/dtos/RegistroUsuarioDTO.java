package dtos;

// Este DTO se usa para RECIBIR datos del frontend (al crear o editar)
public class RegistroUsuarioDTO {

    private String nombre;
    private String email;
    private Integer rolId; // <-- ¡EL CAMPO QUE FALTABA!
    private Boolean activo;
    
    // NOTA: 'password' no se incluye aquí, 
    // se asignará 'tsk006' en el servicio.

    // --- Getters y Setters ---

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

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}