package controladores;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import modelos.ConsultasUsuario;
import modelos.Usuario;
import vistas.*;

public class Login{
    private final VistaLogin login;
    private final Usuario user;
    private ConsultasUsuario consultasUser = new ConsultasUsuario();
    private SistemaAdministrador sistemAdmin;
    private SistemaVeterinario sistemVet;
    private SistemaCajero sistemCaja;
    private VistaAdministrador viewAdmin= new VistaAdministrador();
    private VistaVeterinario viewVet= new VistaVeterinario();
    private VistaCajero viewCaja= new VistaCajero();

    public Login(VistaLogin login, Usuario user) {
        this.login = login;
        this.user = user;
        this.login.btnLogin.addActionListener(this::clickListener);
    }
    
    public void clickListener(ActionEvent clic){
        if(clic.getSource() == login.btnLogin){
            user.setUsario(login.txtUsuario.getText());
            user.setContra(login.txtContra.getText());
            if(consultasUser.textoVacio(login.txtUsuario.getText())){
                JOptionPane.showMessageDialog(null, "No se ha ingresado ningun usuario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            else if(consultasUser.iniciarSesion(user)){
                login.dispose();
                switch(user.getRol()){
                    case 0:
                        sistemCaja = new SistemaCajero(user, viewCaja);
                        sistemCaja.iniciar();
                    break;
                    case 1:
                        sistemVet = new SistemaVeterinario(user, viewVet);
                        sistemVet.iniciar();
                    break;
                    default:
                        sistemAdmin = new SistemaAdministrador(user, viewAdmin);
                        sistemAdmin.iniciar();
                    break;
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Usuario o contraseña invalidos.", "Error", JOptionPane.ERROR_MESSAGE);
                login.txtContra.setText(null);
                login.txtUsuario.setText(null);
            }
        }
    }
    
    public void iniciar(){
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }
}
