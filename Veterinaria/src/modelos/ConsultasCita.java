package modelos;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ConsultasCita extends Conexion{
    private String sqlQuery;
    private PreparedStatement ps;
    private ResultSet rs;
    
    ///////////// Consultas de citas ///////////////////////
    public boolean agregarCita(Cita cit){
        ps = null;
        sqlQuery = "INSERT INTO tablaCitas(IDVeterinario,NombreCliente, ApellidosCliente, TelefonoCliente,Fecha,Hora,Descripcion) VALUES(?,?,?,?,?,?,?)";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setInt(1, cit.getIdVeterinario());
            ps.setString(2, cit.getNombreCliente());
            ps.setString(3, cit.getApellidosCliente());
            ps.setString(4, cit.getTelefonoCliente());
            ps.setString(5, cit.getFecha());
            ps.setString(6, cit.getHora());
            ps.setString(7, cit.getDescripcion());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public Set<Cita> buscarCita(Usuario user, String consulta){
        Set<Cita> citas = new HashSet<>();
        Statement set = null;
        if(consulta.equals("")){
            sqlQuery = "SELECT * FROM tablaCitas WHERE IDVeterinario = '"+user.getId()+"'";
        }else{sqlQuery = "SELECT * FROM tablaCitas WHERE IDVeterinario = '"+user.getId()+"' AND (Fecha Like '%"+consulta+"%' OR Descripcion Like '%"+consulta+"%')";}
        try{
            set = getConnection().createStatement();
            rs = set.executeQuery(sqlQuery);
            while(rs.next()){
                Cita cit = new Cita();
                cit.setId(rs.getInt("ID"));
                cit.setIdVeterinario(rs.getInt("IDVeterinario"));
                cit.setNombreCliente(rs.getString("NombreCliente"));
                cit.setApellidosCliente(rs.getString("ApellidosCliente"));
                cit.setTelefonoCliente(rs.getString("TelefonoCliente"));
                cit.setFecha(rs.getString("Fecha"));
                cit.setHora(rs.getString("Hora"));
                cit.setDescripcion(rs.getString("Descripcion"));
                citas.add(cit);
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return citas;
    }
    
    public boolean modificarCita(Cita cit){
        ps = null;
        sqlQuery = "UPDATE tablaCitas SET IDVeterinario=?,NombreCliente=?,ApellidosCliente=?,TelefonoCliente=?,Fecha=?,Hora=?,Descripcion=? WHERE ID = ?";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setInt(1, cit.getIdVeterinario());
            ps.setString(2, cit.getNombreCliente());
            ps.setString(3, cit.getApellidosCliente());
            ps.setString(4, cit.getTelefonoCliente());
            ps.setString(5, cit.getFecha());
            ps.setString(6, cit.getHora());
            ps.setString(7, cit.getDescripcion());
            ps.setInt(8, cit.getId());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public boolean borrarCita(Cita cit){
        ps = null;
        sqlQuery = "DELETE FROM tablaCitas WHERE ID=?";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setInt(1, cit.getId());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }    
}
