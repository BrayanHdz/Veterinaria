package modelos;

import conexion.Conexion;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class ConsultasMascota extends Conexion{
    private String sqlQuery;
    private PreparedStatement ps;
    private ResultSet rs;
    
    ///////////// Consultas de Mascotas ///////////////////////
    public boolean agregarMascota(Mascota pet){
        ps = null;
        sqlQuery = "INSERT INTO tablaMascotas(IDCliente,Mascota,Nombre,Descripcion) VALUES(?,?,?,?)";
        
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setInt(1, pet.getIdCliente());
            ps.setString(2, pet.getMascota());
            ps.setString(3, pet.getNombre());
            ps.setString(4, pet.getDescripcion());
            ps.execute();
            return true;
        }
        catch(SQLException e){
            System.err.println(e);
            return false;
        }
    }
    
    public Set<Mascota> buscarMascota(String consulta){
        Set<Mascota> mascotas = new HashSet<>();
        Statement set = null;
        if(consulta.equals("")){
            sqlQuery = "SELECT * FROM tablaMascotas";
        }else{sqlQuery = "SELECT * FROM tablaMascotas WHERE Nombre Like '%"+consulta+"%' OR Mascota Like '%"+consulta+"%' OR Descripcion Like '%"+consulta+"%'";}
        try{
            set = getConnection().createStatement();
            rs = set.executeQuery(sqlQuery);
            while(rs.next()){
                Mascota pet = new Mascota();
                pet.setId(rs.getInt("ID"));
                pet.setIdCliente(rs.getInt("IDCliente"));
                pet.setMascota(rs.getString("Mascota"));
                pet.setNombre(rs.getString("Nombre"));
                pet.setDescripcion(rs.getString("Descripcion"));
                mascotas.add(pet);
            }
        }
        catch(SQLException e){
            System.err.println(e);
        }
        
        return mascotas;
    }
    
    public int existeMascota(Mascota pet){
        ps = null;
        sqlQuery = "SELECT * FROM tablaMascotas WHERE IDCliente = ? AND Mascota = ? AND Nombre = ?";
        int id = 0;
        try{
            ps = getConnection().prepareStatement(sqlQuery);
            ps.setInt(1, pet.getIdCliente());
            ps.setString(2, pet.getMascota());
            ps.setString(3, pet.getNombre());
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
}
