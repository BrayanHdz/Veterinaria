package modelos;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ConsultasVenta extends Conexion{
    private String sqlQuery;
    private PreparedStatement ps;
    private ResultSet rs;
    
    ///////////// Consultas de Ventas ///////////////////////
    public boolean agregarVenta(Venta ven){
        ps = null;
        sqlQuery = "INSERT INTO tablaVentas(IDCajero,CodigoProducto,Cantidad,CostoTotal,Fecha,Hora,Promocion) VALUES(?,?,?,?,?,?,?)";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setInt(1, ven.getIdCajero());
            ps.setString(2, ven.getCodigoProducto());
            ps.setInt(3, ven.getCantidad());
            ps.setFloat(4, ven.getCostoTotal());
            ps.setString(5, ven.getFecha());
            ps.setString(6, ven.getHora());
            ps.setString(7, ven.getPromocion());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public Set<Venta> buscarVenta(String consultar, Usuario user){
        Set<Venta> ventas = new HashSet<>();
        Statement set = null;
        if(consultar.equals("")){
            if(user.getRol() == 0){
                sqlQuery = "SELECT * FROM tablaVentas WHERE IDCajero = '"+user.getId()+"'";
            }else{
                sqlQuery = "SELECT * FROM tablaVentas";
            }
        }else{
            if(user.getRol() == 0){
                sqlQuery = "SELECT * FROM tablaVentas WHERE IDCajero = '"+user.getId()+"' AND (Fecha Like '%"+consultar+"%' OR Hora Like '%"+consultar+"%' OR CodigoProducto Like '%"+consultar+"%')";
            }else{
                sqlQuery = "SELECT * FROM tablaVentas WHERE Fecha Like '%"+consultar+"%' OR Hora Like '%"+consultar+"%' OR CodigoProducto Like '%"+consultar+"%'";
            }
        }
        try{
            set = getConnection().createStatement();
            rs = set.executeQuery(sqlQuery);
            while(rs.next()){
                Venta ven = new Venta();
                ven.setId(rs.getInt("ID"));
                ven.setCodigoProducto(rs.getString("CodigoProducto"));
                ven.setCantidad(rs.getInt("Cantidad"));
                ven.setCostoTotal(rs.getFloat("CostoTotal"));
                ven.setFecha(rs.getString("Fecha"));
                ven.setHora(rs.getString("Hora"));
                ven.setPromocion(rs.getString("Promocion"));
                ventas.add(ven);
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return ventas;
    }
    
}
