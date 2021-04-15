package modelos;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ConsultasConsulta extends Conexion{
    private String sqlQuery;
    private PreparedStatement ps;
    private ResultSet rs;    
    
    ///////////// Consultas de Consultas ///////////////////////
    public boolean agregarConsulta(Consulta conn){
        ps = null;
        sqlQuery = "INSERT INTO tablaConsultas(IDCliente,IDVeterinario,IDMascota,Fecha,Descripcion,Costo) VALUES(?,?,?,?,?,?)";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setInt(1, conn.getIdCliente());
            ps.setInt(2, conn.getIdVeterinario());
            ps.setInt(3, conn.getIdMascota());
            ps.setString(4, conn.getFecha());
            ps.setString(5, conn.getDescripcion());
            ps.setFloat(6, conn.getCosto());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public Set<Consulta> buscarConsulta(String consultar, Usuario user){
        Set<Consulta> consultas = new HashSet<>();
        Statement set = null;
        if(user.getRol() == 2){
            if(consultar.equals("")){
                sqlQuery = "SELECT * FROM tablaConsultas";
            }else{sqlQuery = "SELECT * FROM tablaConsultas WHERE Fecha Like '%"+consultar+"%' OR Descripcion Like '%"+consultar+"%' OR Costo Like '%"+consultar+"%'";}
        }else{
            if(consultar.equals("")){
                sqlQuery = "SELECT * FROM tablaConsultas WHERE IDVeterinario ='"+user.getId()+"'";
            }else{sqlQuery = "SELECT * FROM tablaConsultas WHERE IDVeterinario ='"+user.getId()+"' AND (Fecha Like '%"+consultar+"%' OR Descripcion Like '%"+consultar+"%' OR Costo Like '%"+consultar+"%')";}
        }
        try{
            set = getConnection().createStatement();
            rs = set.executeQuery(sqlQuery);
            while(rs.next()){
                Consulta conn = new Consulta();
                conn.setId(rs.getInt("ID"));
                conn.setIdCliente(rs.getInt("IDCliente"));
                conn.setIdVeterinario(rs.getInt("IDVeterinario"));
                conn.setIdMascota(rs.getInt("IDMascota"));
                conn.setFecha(rs.getString("Fecha"));
                conn.setDescripcion(rs.getString("Descripcion"));
                conn.setCosto(rs.getFloat("Costo"));
                consultas.add(conn);
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return consultas;
    }
}
