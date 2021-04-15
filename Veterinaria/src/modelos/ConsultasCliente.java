package modelos;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ConsultasCliente extends Conexion{
    private String sqlQuery;
    private PreparedStatement ps;
    private ResultSet rs;
    
    ///////////// Consultas de cllientes ///////////////////////
    
    public boolean agregarCliente(Cliente client){
        ps = null;
        sqlQuery = "INSERT INTO tablaCliente(Nombre,Apellidos,Telefono,UltimaConsulta) VALUES(?,?,?,?)";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setString(1, client.getNombre());
            ps.setString(2, client.getApellidos());
            ps.setString(3, client.getTelefono());
            ps.setString(4, client.getUltimaConsulta());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public Set<Cliente> buscarCliente(String consulta){
        Set<Cliente> clientes = new HashSet<>();
        Statement set = null;
        if(consulta.equals("")){
            sqlQuery = "SELECT * FROM tablaCliente";
        }else{sqlQuery = "SELECT * FROM tablaCliente WHERE Nombre Like '%"+consulta+"%' OR Apellidos Like '%"+consulta+"%' OR Telefono Like '%"+consulta+"%'";}
        try{
            set = getConnection().createStatement();
            rs = set.executeQuery(sqlQuery);
            while(rs.next()){
                Cliente client = new Cliente();
                client.setId(rs.getInt("ID"));
                client.setNombre(rs.getString("Nombre"));
                client.setApellidos(rs.getString("Apellidos"));
                client.setTelefono(rs.getString("Telefono"));
                client.setUltimaConsulta(rs.getString("UltimaConsulta"));
                clientes.add(client);
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return clientes;
    }
    
    public int existeCliente(Cliente client){
        int id = 0;
        ps = null;
        sqlQuery = "SELECT * FROM tablaCliente WHERE Nombre = ? AND Apellidos = ? AND Telefono = ?";
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setString(1, client.getNombre());
            ps.setString(2, client.getApellidos());
            ps.setString(3, client.getTelefono());
            rs = ps.executeQuery();
            while(rs.next()){
                id = rs.getInt("ID");
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        return id;
    }
    
    public boolean modificarCliente(Cliente client){
        ps = null;
        sqlQuery = "UPDATE tablaCliente SET UltimaConsulta=? WHERE ID=?";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setString(1, client.getUltimaConsulta());
            ps.setInt(2, client.getId());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
}
