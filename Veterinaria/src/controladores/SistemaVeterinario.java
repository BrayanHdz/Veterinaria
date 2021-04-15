package controladores;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import modelos.*;
import vistas.VistaVeterinario;

public class SistemaVeterinario{
    private final VistaVeterinario sistem;
    private final Usuario user;
    private final DecimalFormat formatoPrecio;
    private final ConsultasUsuario consultasUser;
    private final ConsultasCliente consultasClient;
    private final ConsultasMascota consultasPet;
    private final ConsultasConsulta consultasConsult;
    private final ConsultasCita consultasCit;
    private Set<Cita> listCita;
    private Cita citaSelect;
    private DefaultTableModel tablaClientes,tablaMascotas,tablaCitas,tablaConsultas;
    private final String consulta[] = {"","","",""};
    private boolean modificarCita;
    
    public SistemaVeterinario(Usuario user, VistaVeterinario sistem){
        this.listCita = new HashSet<>();
        this.formatoPrecio = new DecimalFormat("0.00");
        this.consultasUser = new ConsultasUsuario();
        this.consultasClient = new ConsultasCliente();
        this.consultasPet = new ConsultasMascota();
        this.consultasConsult = new ConsultasConsulta();
        this.consultasCit = new ConsultasCita();
        
        this.user = user;
        this.sistem = sistem;
        this.sistem.btnClientes.addActionListener(this::eventListener);
        this.sistem.btnCitas.addActionListener(this::eventListener);
        this.sistem.btnConsultas.addActionListener(this::eventListener);
        this.sistem.btnMascotas.addActionListener(this::eventListener);
        this.sistem.btnAgregarConsulta.addActionListener(this::eventListener);
        this.sistem.btnAgendarCita.addActionListener(this::eventListener);
        this.sistem.btnCancelarCita.addActionListener(this::eventListener);
        this.sistem.txtConsultaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.txtConsultaMascota.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.txtConsultaCita.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.txtConsultaConosulta.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.tablaCitas.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClicTable(evt);
            }
        });
    }
    
    ///////////////////////// Se busco una consulta o cita //////////////
    public void tecla(KeyEvent tecla){
        if(tecla.getSource() == sistem.txtConsultaCliente){
            if(sistem.txtConsultaCliente.getText().equals("")){
                consulta[0] = "";
            }else{consulta[0] = sistem.txtConsultaCliente.getText();}

            mostrarClientes(consultasClient.buscarCliente(consulta[0]));
        }
        if(tecla.getSource() == sistem.txtConsultaCita){
            if(sistem.txtConsultaCita.getText().equals("")){
                consulta[1] = "";
            }else{consulta[1] = sistem.txtConsultaCita.getText();}

            mostrarCitas(consultasCit.buscarCita(user,consulta[1]));
        }
        if(tecla.getSource() == sistem.txtConsultaMascota){
            if(sistem.txtConsultaMascota.getText().equals("")){
                consulta[2] = "";
            }else{consulta[2] = sistem.txtConsultaMascota.getText();}

            mostrarMascotas(consultasPet.buscarMascota(consulta[2]));
        }
        if(tecla.getSource() == sistem.txtConsultaConosulta){
            if(sistem.txtConsultaConosulta.getText().equals("")){
                consulta[3] = "";
            }else{consulta[3] = sistem.txtConsultaConosulta.getText();}

            mostrarConsultas(consultasConsult.buscarConsulta(consulta[3],user));
        }
    }
    
    ///////////////////////// Se selecciono una cita //////////////
    public void mouseClicTable(MouseEvent evt){
        if(evt.getSource() == sistem.tablaCitas){
            modificarCita = true;
            for(Cita citaTabla : listCita){
                if(Integer.parseInt(sistem.tablaCitas.getValueAt(sistem.tablaCitas.getSelectedRow(), 0).toString())== citaTabla.getId()){
                    citaSelect = citaTabla;
                    sistem.txtNombreClienteCita.setText(citaSelect.getNombreCliente());
                    sistem.txtApellidosClienteCita.setText(citaSelect.getApellidosCliente());
                    sistem.txtTelefonoClienteCita.setText(citaSelect.getTelefonoCliente());
                    sistem.txtFechaCita.setText(citaSelect.getFecha());
                    sistem.txtHoraCita.setText(citaSelect.getHora());
                    sistem.txtDescripcionC.setText(citaSelect.getDescripcion());
                }
            }
            sistem.btnAgendarCita.setText("Reagendar");
        }
    }
    
    public void eventListener(ActionEvent clic){
        
        if(clic.getSource() == sistem.btnConsultas){
            cerrarVentanas();
            sistem.panelConsultas.setVisible(true);
        }
        
        if(clic.getSource() == sistem.btnClientes){
            cerrarVentanas();
            sistem.panelClientes.setVisible(true);
            mostrarClientes(consultasClient.buscarCliente(consulta[0]));
        }
        
        if(clic.getSource() == sistem.btnCitas){
            cerrarVentanas();
            sistem.panelCitas.setVisible(true);
            mostrarCitas(consultasCit.buscarCita(user,consulta[1]));
        }
        
        if(clic.getSource() == sistem.btnMascotas){
            cerrarVentanas();
            sistem.panelMascotas.setVisible(true);
            mostrarMascotas(consultasPet.buscarMascota(consulta[2]));
        }
        
    ///////////////////////// Se solicito agendar o modificar una cita //////////////
        if(clic.getSource() == sistem.btnAgendarCita){
            if(consultasUser.textoVacio(sistem.txtNombreClienteCita.getText()) || consultasUser.textoVacio(sistem.txtApellidosClienteCita.getText()) || consultasUser.textoVacio(sistem.txtNombreClienteCita.getText())
                    || consultasUser.textoVacio(sistem.txtFechaCita.getText()) || consultasUser.textoVacio(sistem.txtHoraCita.getText()) || consultasUser.textoVacio(sistem.txtDescripcionC.getText())){
                JOptionPane.showMessageDialog(null, "No se han llenado todos los campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }else{
                Cita citaNueva = new Cita();
                citaNueva.setIdVeterinario(user.getId());
                citaNueva.setNombreCliente(sistem.txtNombreClienteCita.getText());
                citaNueva.setApellidosCliente(sistem.txtApellidosClienteCita.getText());
                citaNueva.setTelefonoCliente(sistem.txtTelefonoClienteCita.getText());
                citaNueva.setFecha(sistem.txtFechaCita.getText());
                citaNueva.setHora(sistem.txtHoraCita.getText());
                citaNueva.setDescripcion(sistem.txtDescripcionC.getText());
                if(modificarCita){
                    citaNueva.setId(citaSelect.getId());
                    if(consultasCit.modificarCita(citaNueva)){
                        JOptionPane.showMessageDialog(null, "Cita guardada correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                        resetCita();
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    if(consultasCit.agregarCita(citaNueva)){
                        JOptionPane.showMessageDialog(null, "Cita guardada correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                        resetCita();
                    }else{
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        
    ///////////////////////// Se solicito cancelar una cita //////////////
        if(clic.getSource() == sistem.btnCancelarCita && modificarCita){
            if(consultasCit.borrarCita(citaSelect)){
                JOptionPane.showMessageDialog(null, "Cita cancelada correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                resetCita();
            }else{
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    ///////////////////////// Se solicito registar una consulta //////////////
        if(clic.getSource() == sistem.btnAgregarConsulta){
            if(consultasUser.textoVacio(sistem.txtNombreClienteConsulta.getText()) || consultasUser.textoVacio(sistem.txtApellidosClienteConsulta.getText()) || consultasUser.textoVacio(sistem.txtTelefonoClienteConsulta.getText())
                    || consultasUser.textoVacio(sistem.txtMascotaConsulta.getText()) || consultasUser.textoVacio(sistem.txtNombreMascotaConsulta.getText()) || consultasUser.textoVacio(sistem.txtDescripcionMascotaConsulta.getText())
                    || consultasUser.textoVacio(sistem.txtDescripcionConsulta.getText()) && (!consultasUser.textoVacio(sistem.txtCosto1.getText()) || !consultasUser.textoVacio(sistem.txtCosto2.getText()))){
                JOptionPane.showMessageDialog(null, "No se han llenado todos los campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }else{
                Consulta consultaNueva= new Consulta();
                consultaNueva.setIdVeterinario(user.getId());
                consultaNueva.setFecha(sistem.txtFechaConsulta.getText());
                consultaNueva.setDescripcion(sistem.txtDescripcionConsulta.getText());
                consultaNueva.setCosto(Float.parseFloat(sistem.txtCosto1.getText()) + Float.parseFloat(sistem.txtCosto2.getText()));
                
                Cliente clienteNuevo= new Cliente();
                clienteNuevo.setNombre(sistem.txtNombreClienteConsulta.getText());
                clienteNuevo.setApellidos(sistem.txtApellidosClienteConsulta.getText());
                clienteNuevo.setTelefono(sistem.txtTelefonoClienteConsulta.getText());
                
                Mascota mascotaNuevo= new Mascota();
                mascotaNuevo.setMascota(sistem.txtMascotaConsulta.getText());
                mascotaNuevo.setNombre(sistem.txtNombreMascotaConsulta.getText());
                mascotaNuevo.setDescripcion(sistem.txtDescripcionMascotaConsulta.getText());
                
                if(consultasClient.existeCliente(clienteNuevo) != 0){
                    consultaNueva.setIdCliente(consultasClient.existeCliente(clienteNuevo));
                    mascotaNuevo.setIdCliente(consultasClient.existeCliente(clienteNuevo));
                    if(consultasPet.existeMascota(mascotaNuevo) != 0){
                        consultaNueva.setIdMascota(consultasPet.existeMascota(mascotaNuevo));
                    }else{
                        consultasPet.agregarMascota(mascotaNuevo);
                        consultaNueva.setIdMascota(consultasPet.existeMascota(mascotaNuevo));
                    }
                    clienteNuevo.setId(consultasClient.existeCliente(clienteNuevo));
                    clienteNuevo.setUltimaConsulta(sistem.txtFechaConsulta.getText());
                    consultasClient.modificarCliente(clienteNuevo);
                }else{
                    clienteNuevo.setUltimaConsulta(sistem.txtFechaConsulta.getText());
                    consultasClient.agregarCliente(clienteNuevo);
                    consultaNueva.setIdCliente(consultasClient.existeCliente(clienteNuevo));
                    mascotaNuevo.setIdCliente(consultasClient.existeCliente(clienteNuevo));
                    consultasPet.agregarMascota(mascotaNuevo);
                    consultaNueva.setIdMascota(consultasPet.existeMascota(mascotaNuevo));
                }
                
                if(consultasConsult.agregarConsulta(consultaNueva)){
                    JOptionPane.showMessageDialog(null, "Consulta guardada correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                    resetConsulta();
                }else{
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    ////////// Imprimir tabla de clientes //////////////////
    
    public void mostrarClientes(Set<Cliente> listaClients){
        tablaClientes = new DefaultTableModel();
        tablaClientes.addColumn("ID");
        tablaClientes.addColumn("Nombre");
        tablaClientes.addColumn("Apellidos");
        tablaClientes.addColumn("Telefono");
        tablaClientes.addColumn("Ultima Consulta");
        sistem.tablaClientes.setModel(tablaClientes);
        for(Cliente clienteTabla : listaClients){
            String[] datos = new String[5];
            datos[0] = Integer.toString(clienteTabla.getId());
            datos[1] = clienteTabla.getNombre();
            datos[2] = clienteTabla.getApellidos();
            datos[3] = clienteTabla.getTelefono();
            datos[4] = clienteTabla.getUltimaConsulta();
            
            try{
                tablaClientes.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaClientes);
        sistem.tablaClientes.setRowSorter(sorter);
        sistem.tablaClientes.getRowSorter().toggleSortOrder(0);
    }
    
    ////////// Imprimir tabla de mascotas //////////////////
    
    public void mostrarMascotas(Set<Mascota> listaPets){
        tablaMascotas = new DefaultTableModel();
        tablaMascotas.addColumn("ID");
        tablaMascotas.addColumn("ID Cliete");
        tablaMascotas.addColumn("Mascota");
        tablaMascotas.addColumn("Nombre");
        tablaMascotas.addColumn("Descripcion");
        sistem.tablaMascotas.setModel(tablaMascotas);
        for(Mascota mascotaTabla : listaPets){
            String[] datos = new String[5];
            datos[0] = Integer.toString(mascotaTabla.getId());
            datos[1] = Integer.toString(mascotaTabla.getIdCliente());
            datos[2] = mascotaTabla.getMascota();
            datos[3] = mascotaTabla.getNombre();
            datos[4] = mascotaTabla.getDescripcion();
            
            try{
                tablaMascotas.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaMascotas);
        sistem.tablaMascotas.setRowSorter(sorter);
        sistem.tablaMascotas.getRowSorter().toggleSortOrder(0);
    }
    
    ////////// Imprimir tabla de citas //////////////////
    
    public void mostrarCitas(Set<Cita> listaCits){
        listCita = listaCits;
        tablaCitas = new DefaultTableModel();
        tablaCitas.addColumn("ID");
        tablaCitas.addColumn("Nombre");
        tablaCitas.addColumn("Apellidos");
        tablaCitas.addColumn("Telefono");
        tablaCitas.addColumn("Fecha");
        tablaCitas.addColumn("Hora");
        tablaCitas.addColumn("Descripcion");
        sistem.tablaCitas.setModel(tablaCitas);
        for(Cita citaTabla : listaCits){
            String[] datos = new String[7];
            datos[0] = Integer.toString(citaTabla.getId());
            datos[1] = citaTabla.getNombreCliente();
            datos[2] = citaTabla.getApellidosCliente();
            datos[3] = citaTabla.getTelefonoCliente();
            datos[4] = citaTabla.getFecha();
            datos[5] = citaTabla.getHora();
            datos[6] = citaTabla.getDescripcion();
            
            try{
                tablaCitas.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaCitas);
        sistem.tablaCitas.setRowSorter(sorter);
        sistem.tablaCitas.getRowSorter().toggleSortOrder(0);
    }
    
    ////////// Imprimir tabla de consultas //////////////////
    public void mostrarConsultas(Set<Consulta> listaconsults){
        tablaConsultas = new DefaultTableModel();
        tablaConsultas.addColumn("ID");
        tablaConsultas.addColumn("ID Cliente");
        tablaConsultas.addColumn("ID Mascota");
        tablaConsultas.addColumn("Fecha");
        tablaConsultas.addColumn("Descripcion");
        tablaConsultas.addColumn("Costo");
        sistem.tablaConsultas.setModel(tablaConsultas);
        for(Consulta consultaTabla : listaconsults){
            String[] datos = new String[6];
            datos[0] = Integer.toString(consultaTabla.getId());
            datos[1] = Integer.toString(consultaTabla.getIdCliente());
            datos[2] = Integer.toString(consultaTabla.getIdMascota());
            datos[3] = consultaTabla.getFecha();
            datos[4] = consultaTabla.getDescripcion();
            datos[5] = formatoPrecio.format(consultaTabla.getCosto());        
            try{
                tablaConsultas.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaConsultas);
        sistem.tablaConsultas.setRowSorter(sorter);
        sistem.tablaConsultas.getRowSorter().toggleSortOrder(0);
    }
    
    /////// Otros Metodos /////////////////////
    
    public void cerrarVentanas(){
        sistem.panelClientes.setVisible(false);
        sistem.panelConsultas.setVisible(false);
        sistem.panelMascotas.setVisible(false);
        sistem.panelCitas.setVisible(false);
    }
    
    public boolean esEntero(String texto){
        try{
            Integer.parseInt(texto);
            return true;
        }catch(java.lang.NumberFormatException e){
            return false;
        }
    }
    public boolean esFlotante(String texto){
        try{
            Float.parseFloat(texto);
            return true;
        }catch(java.lang.NumberFormatException e){
            return false;
        }
    }
    
    public void resetCita(){
        mostrarCitas(consultasCit.buscarCita(user, consulta[1]));
        sistem.txtNombreClienteCita.setText(null);
        sistem.txtApellidosClienteCita.setText(null);
        sistem.txtTelefonoClienteCita.setText(null);
        sistem.txtFechaCita.setText(null);
        sistem.txtHoraCita.setText(null);
        sistem.txtDescripcionC.setText(null);
        modificarCita= false;
        citaSelect = null;
    }
    
    public void resetConsulta(){
        mostrarConsultas(consultasConsult.buscarConsulta(consulta[3],user));
        sistem.txtNombreClienteConsulta.setText(null);
        sistem.txtApellidosClienteConsulta.setText(null);
        sistem.txtTelefonoClienteConsulta.setText(null);
        sistem.txtMascotaConsulta.setText(null);
        sistem.txtNombreMascotaConsulta.setText(null);
        sistem.txtDescripcionMascotaConsulta.setText(null);
        sistem.txtFechaConsulta.setText(null);
        sistem.txtDescripcionConsulta.setText(null);
        sistem.txtCosto1.setText(null);
        sistem.txtCosto2.setText(null);
    }
    
    public void iniciar(){
        sistem.setVisible(true);
        sistem.setLocationRelativeTo(null);
        cerrarVentanas();   
        mostrarConsultas(consultasConsult.buscarConsulta(consulta[3],user));
        sistem.panelConsultas.setVisible(true);
        sistem.lblBienvenida.setText("BIENVENIDO/A: "+user.getNombre());
    }
}
