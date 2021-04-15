package modelos;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ConsultasProducto extends Conexion{
    private String sqlQuery;
    private PreparedStatement ps;
    private ResultSet rs;
    
    ///////////// Consultas de productos ///////////////////////
    public boolean agregarProducto(Producto objeto){
        ps = null;
        sqlQuery = "INSERT INTO tablaInventario(Codigo,Producto,Descripcion,StockInicial,Total,Costo) VALUES(?,?,?,?,?,?)";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setString(1, objeto.getCodigo());
            ps.setString(2, objeto.getProducto());
            ps.setString(3, objeto.getDescripcion());
            ps.setInt(4, objeto.getStockInicial());
            ps.setInt(5, objeto.getTotal());
            ps.setFloat(6, objeto.getCosto());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public Set<Producto> buscarProductos(String consulta){
        Set<Producto> productos = new HashSet<>();
        Statement set = null;
        if(consulta.equals("")){
            sqlQuery = "SELECT * FROM tablaInventario";
        }else{sqlQuery = "SELECT * FROM tablaInventario WHERE Codigo Like '%"+consulta+"%' OR Producto Like '%"+consulta+"%' OR Descripcion Like '%"+consulta+"%'";}
        try{
            set = getConnection().createStatement();
            rs = set.executeQuery(sqlQuery);
            while(rs.next()){
                Producto objeto = new Producto();
                objeto.setCodigo(rs.getString("Codigo"));
                objeto.setProducto(rs.getString("Producto"));
                objeto.setDescripcion(rs.getString("Descripcion"));
                objeto.setStockInicial(rs.getInt("StockInicial"));
                objeto.setEntradas(rs.getInt("Entradas"));
                objeto.setSalidas(rs.getInt("Salidas"));
                objeto.setTotal(rs.getInt("Total"));
                objeto.setCosto(rs.getFloat("Costo"));
                productos.add(objeto);
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return productos;
    }
    
    public boolean modificarProducto(Producto objeto){
        ps = null;
        sqlQuery = "UPDATE tablaInventario SET Producto=?,Descripcion=?,Entradas=?,Salidas=?,Total=?,Costo=? WHERE Codigo = ?";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setString(1, objeto.getProducto());
            ps.setString(2, objeto.getDescripcion());
            ps.setInt(3, objeto.getEntradas());
            ps.setInt(4, objeto.getSalidas());
            ps.setInt(5, objeto.getTotal());
            ps.setFloat(6, objeto.getCosto());
            ps.setString(7, objeto.getCodigo());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public boolean comprarProducto(Producto objeto){
        ps = null;
        Statement set = null;
        sqlQuery = "SELECT * FROM tablaInventario WHERE Codigo = '"+objeto.getCodigo()+"'";
        try{
            set = getConnection().createStatement();
            rs = set.executeQuery(sqlQuery);
            while(rs.next()){
                int salidas = (rs.getInt("Salidas"))+objeto.getSalidas();
                int total = (rs.getInt("Total"))-objeto.getSalidas();
                sqlQuery = "UPDATE tablaInventario SET Salidas=?,Total=? WHERE Codigo = ?";
                try{
                    ps = getConnection().prepareStatement(sqlQuery);
                    ps.setInt(1, salidas);
                    ps.setInt(2, total);
                    ps.setString(3, objeto.getCodigo());
                    ps.execute();
                    return true;
                }
                catch(SQLException e){
                    System.err.println(e);
                    return false;
                }
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        return false;
    }
    
    public boolean borrarProducto(Producto objeto){
        ps = null;
        sqlQuery = "DELETE FROM tablaInventario WHERE Codigo=?";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setString(1, objeto.getCodigo());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
}
