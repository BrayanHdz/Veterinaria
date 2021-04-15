package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private String driver = "com.mysql.jdbc.Driver";
    private String url = "jdbc:mysql://localhost:3306/Veterinaria";
    private String user = "root";
    private String password = "1Puerc@Volador9";
    private Connection connection;
    
    public Conexion(){
        try {
                Class.forName(driver);
                connection = DriverManager.getConnection(url, user, password);
        }
        catch (ClassNotFoundException e) {}
        catch (SQLException e) {}	
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
	
    public void setDriver(String controlador){
            driver = controlador;
    }

    public void setPassword(String contrasena){
            password = contrasena;
    }

    public void setURL(String URL){
            url = URL;
    }

    public void setUser(String usuario){
            user = usuario;
    }

    public String getDriver(){
            return driver;
    }

    public String getPassword(){
            return password;
    }

    public String getURL(){
            return url;
    }

    public String getUser(){
            return user;
    }    
}
