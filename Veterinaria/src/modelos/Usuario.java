package modelos;

public class Usuario {
    private int id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String telefono;
    private String direccion;
    private String cPostal;
    private String usario;
    private String contra;
    private int rol;

    public Usuario() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCPostal() {
        return cPostal;
    }

    public void setCPostal(String cPostal) {
        this.cPostal = cPostal;
    }

    public String getUsario() {
        return usario;
    }

    public void setUsario(String usario) {
        this.usario = usario;
    }

    public String getContra() {
        return contra;
    }

    public void setContra(String contraseña) {
        this.contra = contraseña;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + ", nombre=" + nombre + ", apellidos=" + apellidos + ", correo=" + correo + ", telefono=" + telefono + ", direccion=" + direccion + ", cPostal=" + cPostal + ", usario=" + usario + ", contra=" + contra + ", rol=" + rol + '}';
    }
    
}
