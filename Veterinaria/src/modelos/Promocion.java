package modelos;

public class Promocion {
    private int id;
    private String nombre;
    private String descripcion;
    private String codigoProducto;
    private float promocion;

    public Promocion() {
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public float getPromocion() {
        return promocion;
    }

    public void setPromocion(float promocion) {
        this.promocion = promocion;
    }
    
}
