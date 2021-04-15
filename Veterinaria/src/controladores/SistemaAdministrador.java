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
import vistas.VistaAdministrador;

public class SistemaAdministrador{
    private final VistaAdministrador sistem;
    private final Usuario user;
    private final ConsultasUsuario consultasUser;
    private final ConsultasCliente consultasClient;
    private final ConsultasMascota consultasPet;
    private final ConsultasConsulta consultasConsult;
    private final ConsultasProducto consultasProduct;
    private final ConsultasPromocion consultasPromo;
    private final ConsultasVenta consultasVent;
    private Usuario userSelect;
    private Producto productSelect;
    private Promocion promoSelect;
    private final DecimalFormat formatoPrecio, formatoDescuento;
    private Set<Usuario> listUsuario;
    private Set<Producto> listProducto;
    private Set<Promocion> listPromocion;
    private DefaultTableModel tablaUsuarios,tablaClientes,tablaMascotas,tablaConsultas,tablaProductos,tablaPromociones,tablaHVentas;
    private final String consulta[] = {"","","",""};
    private boolean modificar = false, modificarPromocion = false;
    
    public SistemaAdministrador(Usuario user, VistaAdministrador sistem){
        this.listUsuario = new HashSet<>();
        this.listProducto = new HashSet<>();
        this.listPromocion = new HashSet<>();
        this.formatoPrecio = new DecimalFormat("0.00");
        this.formatoDescuento = new DecimalFormat("0.##");
        this.consultasUser = new ConsultasUsuario();
        this.consultasClient = new ConsultasCliente();
        this.consultasPet = new ConsultasMascota();
        this.consultasConsult = new ConsultasConsulta();
        this.consultasProduct = new ConsultasProducto();
        this.consultasPromo = new ConsultasPromocion();
        this.consultasVent = new ConsultasVenta();
        
        this.user = user;
        this.sistem = sistem;
        this.sistem.btnTrabajadores.addActionListener(this::eventListener);
        this.sistem.btnInventario.addActionListener(this::eventListener);
        this.sistem.btnPromociones.addActionListener(this::eventListener);
        this.sistem.btnReportes.addActionListener(this::eventListener);
        this.sistem.btnAgregar.addActionListener(this::eventListener);
        this.sistem.btnAgregarProducto.addActionListener(this::eventListener);
        this.sistem.btnAgregarPromo.addActionListener(this::eventListener);
        this.sistem.btnGuardarProducto.addActionListener(this::eventListener);
        this.sistem.btnEliminar.addActionListener(this::eventListener);
        this.sistem.btnEliminarProducto.addActionListener(this::eventListener);
        this.sistem.btnEliminarPromo.addActionListener(this::eventListener);
        this.sistem.comboTipoPromo.addActionListener(this::eventListener);
        this.sistem.txtConsultaUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.txtConsultas.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.txtConsultaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.txtConsultaPromo.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.tablaTrabajadores.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClicTable(evt);
            }
        });
        this.sistem.tablaProductos.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClicTable(evt);
            }
        });
        this.sistem.tablaPromociones.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mouseClicTable(evt);
            }
        });
    }
    
    public void tecla(KeyEvent tecla){
        if(tecla.getSource() == sistem.txtConsultaUsuario){
            if(sistem.txtConsultaUsuario.getText().equals("")){
                consulta[0] = "";
            }else{consulta[0] = sistem.txtConsultaUsuario.getText();}

            mostrarUsuarios(consultasUser.buscarUsuario(consulta[0]));
        }
        if(tecla.getSource() == sistem.txtConsultas){
            if(sistem.txtConsultas.getText().equals("")){
                consulta[1] = "";
            }else{consulta[1] = sistem.txtConsultas.getText();}

            mostrarClientes(consultasClient.buscarCliente(consulta[1]));
            mostrarMascotas(consultasPet.buscarMascota(consulta[1]));
            mostrarConsultas(consultasConsult.buscarConsulta(consulta[1],user));
            mostrarVentas(consultasVent.buscarVenta(consulta[1], user));
        }
        if(tecla.getSource() == sistem.txtConsultaProducto){
            if(sistem.txtConsultaProducto.getText().equals("")){
                consulta[2] = "";
            }else{consulta[2] = sistem.txtConsultaProducto.getText();}
            mostrarProductos(consultasProduct.buscarProductos(consulta[2]));
        }
        if(tecla.getSource() == sistem.txtConsultaPromo){
            if(sistem.txtConsultaPromo.getText().equals("")){
                consulta[3] = "";
            }else{consulta[3] = sistem.txtConsultaPromo.getText();}

            mostrarPromos(consultasPromo.buscarPromocion(consulta[3]));
        }
    }
    
    public void mouseClicTable(MouseEvent evt){
        if(evt.getSource() == sistem.tablaTrabajadores){
            for(Usuario usuarios : listUsuario){
                if(usuarios.getId() == Integer.parseInt(sistem.tablaTrabajadores.getValueAt(sistem.tablaTrabajadores.getSelectedRow(), 0).toString())){
                    sistem.txtNombre.setText(usuarios.getNombre());
                    sistem.txtApellidos.setText(usuarios.getApellidos());
                    sistem.txtCorreo.setText(usuarios.getCorreo());
                    sistem.txtTelefono.setText(usuarios.getTelefono());
                    sistem.txtDireccion.setText(usuarios.getDireccion());
                    sistem.txtCP.setText(usuarios.getCPostal());
                    sistem.txtUser.setText(usuarios.getUsario());
                    sistem.comboRoles.setSelectedIndex(usuarios.getRol());
                    userSelect = usuarios;
                    sistem.btnAgregar.setText("Modificar");
                    modificar = true;
                }
            }
        }
        
        if(evt.getSource() == sistem.tablaProductos){
            for(Producto productos : listProducto){
                if(productos.getCodigo().equals((String) sistem.tablaProductos.getValueAt(sistem.tablaProductos.getSelectedRow(), 0))){
                    sistem.txtCodigoM.setText(productos.getCodigo());
                    sistem.txtProductoM.setText(productos.getProducto());
                    sistem.txtDescripcionM.setText(productos.getDescripcion());
                    sistem.txtCantM.setText(Integer.toString(productos.getTotal()));
                    sistem.txtCostoM.setText(Float.toString(productos.getCosto()));
                    productSelect = productos;
                    sistem.panelProducto1.setVisible(false);
                    sistem.panelProducto2.setVisible(true);
                }
            }
        }
        
        if(evt.getSource() == sistem.tablaPromociones){
            for(Promocion promo : listPromocion){
                if(promo.getId() == Integer.parseInt(sistem.tablaPromociones.getValueAt(sistem.tablaPromociones.getSelectedRow(), 0).toString())){
                    sistem.txtNombrePromo.setText(promo.getNombre());
                    sistem.txtDescripcionPromo.setText(promo.getDescripcion());
                    sistem.txtCodigoPromo.setText(promo.getCodigoProducto());
                    if(promo.getPromocion() < 1){
                        sistem.txtPromocion.setText(formatoDescuento.format(promo.getPromocion() * 100));
                        sistem.comboTipoPromo.setSelectedIndex(0);
                        sistem.lblTipoPromo.setText("%");
                    }else{
                        sistem.txtPromocion.setText(formatoDescuento.format(promo.getPromocion()));
                        sistem.comboTipoPromo.setSelectedIndex(1);
                        sistem.lblTipoPromo.setText("x1");
                    }
                    promoSelect = promo;
                    sistem.btnAgregarPromo.setText("Guardar");
                    modificarPromocion = true;
                }
            }
        }
    }
    
    public void eventListener(ActionEvent clic){
        if(clic.getSource() == sistem.btnTrabajadores){
            cerrarVentanas();
            sistem.panelTrabajadores.setVisible(true);
            mostrarUsuarios(consultasUser.buscarUsuario(consulta[0]));
        }
        if(clic.getSource() == sistem.btnReportes){
            cerrarVentanas();
            sistem.panelReportes.setVisible(true);
            mostrarClientes(consultasClient.buscarCliente(consulta[1]));
            mostrarMascotas(consultasPet.buscarMascota(consulta[1]));
            mostrarConsultas(consultasConsult.buscarConsulta(consulta[1],user));
            mostrarVentas(consultasVent.buscarVenta(consulta[1], user));
        }
        
        if(clic.getSource() == sistem.btnInventario){
            cerrarVentanas();
            sistem.panelInventario.setVisible(true);
            sistem.panelProducto1.setVisible(true);
            sistem.panelProducto2.setVisible(false);
            mostrarProductos(consultasProduct.buscarProductos(consulta[2]));
        }
        
        if(clic.getSource() == sistem.btnPromociones){
            cerrarVentanas();
            sistem.panelPromociones.setVisible(true);
            mostrarPromos(consultasPromo.buscarPromocion(consulta[3]));
        }
        
        if(clic.getSource() == sistem.comboTipoPromo){
            if(sistem.comboTipoPromo.getSelectedIndex() == 0){sistem.lblTipoPromo.setText("%");}
            else{sistem.lblTipoPromo.setText("x1");}
        }
        
        if(clic.getSource() == sistem.btnAgregar){
            if(consultasUser.textoVacio(sistem.txtNombre.getText()) || consultasUser.textoVacio(sistem.txtApellidos.getText()) || consultasUser.textoVacio(sistem.txtTelefono.getText())
                    || consultasUser.textoVacio(sistem.txtDireccion.getText()) || consultasUser.textoVacio(sistem.txtCP.getText()) || consultasUser.textoVacio(sistem.txtUser.getText()) 
                    || consultasUser.textoVacio(sistem.txtContra.getText())){
                JOptionPane.showMessageDialog(null, "No se han ingresado todos los campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                if(verificarCorreo(sistem.txtCorreo.getText())){
                    Usuario nuevoUsuario = new Usuario();
                    nuevoUsuario.setNombre(sistem.txtNombre.getText());
                    nuevoUsuario.setApellidos(sistem.txtApellidos.getText());
                    nuevoUsuario.setCorreo(sistem.txtCorreo.getText());
                    nuevoUsuario.setTelefono(sistem.txtTelefono.getText());
                    nuevoUsuario.setDireccion(sistem.txtDireccion.getText());
                    nuevoUsuario.setCPostal(sistem.txtCP.getText());
                    nuevoUsuario.setRol(sistem.comboRoles.getSelectedIndex());
                    nuevoUsuario.setUsario(sistem.txtUser.getText());
                    nuevoUsuario.setContra(sistem.txtContra.getText());
                    
                    if(!modificar){
                        if(consultasUser.agregarUsuario(nuevoUsuario)){
                            JOptionPane.showMessageDialog(null, "El trabajador se ha agregado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                            resetTrabajadores();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        nuevoUsuario.setId(userSelect.getId());
                        if(consultasUser.modificarUsuario(nuevoUsuario)){
                            JOptionPane.showMessageDialog(null, "El trabajador se ha modificado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                            resetTrabajadores();
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "El correo electronico no es valido.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }if(clic.getSource() == sistem.btnAgregarPromo){
            if(consultasUser.textoVacio(sistem.txtNombrePromo.getText()) || consultasUser.textoVacio(sistem.txtDescripcionPromo.getText()) || consultasUser.textoVacio(sistem.txtCodigoPromo.getText())
                    || consultasUser.textoVacio(sistem.txtPromocion.getText()) || Float.parseFloat(sistem.txtPromocion.getText()) < 1){
                JOptionPane.showMessageDialog(null, "No se han ingresado todos los campos o los datos son invalidos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                Promocion promoNueva = new Promocion();
                promoNueva.setNombre(sistem.txtNombrePromo.getText());
                promoNueva.setDescripcion(sistem.txtDescripcionPromo.getText());
                promoNueva.setCodigoProducto(sistem.txtCodigoPromo.getText());
                if(sistem.comboTipoPromo.getSelectedIndex() == 1){promoNueva.setPromocion(Float.parseFloat(sistem.txtPromocion.getText()));}
                else{promoNueva.setPromocion(Float.parseFloat(sistem.txtPromocion.getText())/100);}
                
                if(!modificarPromocion){
                    if(consultasPromo.agregarPromocion(promoNueva)){
                        JOptionPane.showMessageDialog(null, "La promocion se ha agregado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                        resetPromocion();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    promoNueva.setId(promoSelect.getId());
                    if(consultasPromo.modificarPromocion(promoNueva)){
                        JOptionPane.showMessageDialog(null, "La promocion se ha modificado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                        resetPromocion();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        
        if(clic.getSource() == sistem.btnAgregarProducto){
            if(consultasUser.textoVacio(sistem.txtCodigo.getText()) || consultasUser.textoVacio(sistem.txtProducto.getText()) || consultasUser.textoVacio(sistem.txtDescripcionP.getText())
                    || !esEntero(sistem.txtCantindad.getText()) || !esFlotante(sistem.txtCostoP.getText())){
                JOptionPane.showMessageDialog(null, "No se han ingresado todos los campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                Producto nuevoProducto = new Producto();
                nuevoProducto.setCodigo(sistem.txtCodigo.getText());
                nuevoProducto.setProducto(sistem.txtProducto.getText());
                nuevoProducto.setDescripcion(sistem.txtDescripcionP.getText());
                nuevoProducto.setStockInicial(Integer.parseInt(sistem.txtCantindad.getText()));
                nuevoProducto.setTotal(Integer.parseInt(sistem.txtCantindad.getText()));
                nuevoProducto.setCosto(Float.parseFloat(sistem.txtCostoP.getText()));

                if(consultasProduct.agregarProducto(nuevoProducto)){
                    JOptionPane.showMessageDialog(null, "El producto se ha agregado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                    resetProductos();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
               
            }
        }
        
        if(clic.getSource() == sistem.btnGuardarProducto){
            if(consultasUser.textoVacio(sistem.txtCodigoM.getText()) || consultasUser.textoVacio(sistem.txtProductoM.getText()) || consultasUser.textoVacio(sistem.txtDescripcionM.getText())
                    || consultasUser.textoVacio(sistem.txtCantM.getText()) || consultasUser.textoVacio(sistem.txtCostoM.getText())){
                JOptionPane.showMessageDialog(null, "No se han ingresado todos los campos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                productSelect.setCodigo(sistem.txtCodigoM.getText());
                productSelect.setProducto(sistem.txtProductoM.getText());
                productSelect.setDescripcion(sistem.txtDescripcionM.getText());
                if(productSelect.getTotal() < Integer.parseInt(sistem.txtCantM.getText())){
                    productSelect.setEntradas(productSelect.getEntradas() + Integer.parseInt(sistem.txtCantM.getText())-productSelect.getTotal());
                }else{
                    productSelect.setSalidas(productSelect.getSalidas() + productSelect.getTotal()-Integer.parseInt(sistem.txtCantM.getText()));
                }
                productSelect.setTotal(Integer.parseInt(sistem.txtCantM.getText()));
                productSelect.setCosto(Float.parseFloat(sistem.txtCostoM.getText()));

                if(consultasProduct.modificarProducto(productSelect)){
                    JOptionPane.showMessageDialog(null, "El producto se ha modificado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                    resetProductos();
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
               
            }
        }
        
        if(clic.getSource() == sistem.btnEliminar && modificar){
            if(consultasUser.borrarUsuario(userSelect)){
                JOptionPane.showMessageDialog(null, "El trabajador se ha borrado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                resetTrabajadores();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if(clic.getSource() == sistem.btnEliminarPromo && modificarPromocion){
            if(consultasPromo.borrarPromocion(promoSelect)){
                JOptionPane.showMessageDialog(null, "La promocion se ha borrado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                resetPromocion();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        if(clic.getSource() == sistem.btnEliminarProducto && productSelect != null){
            if(consultasProduct.borrarProducto(productSelect)){
                JOptionPane.showMessageDialog(null, "El producto se ha borrado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
                resetProductos();
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void resetTrabajadores(){
        sistem.txtNombre.setText(null);
        sistem.txtApellidos.setText(null);
        sistem.txtCorreo.setText(null);
        sistem.txtTelefono.setText(null);
        sistem.txtDireccion.setText(null);
        sistem.txtCP.setText(null);
        sistem.txtUser.setText(null);
        sistem.txtContra.setText(null);
        sistem.comboRoles.setSelectedIndex(0);
        modificar = false;
        userSelect = null;
        sistem.btnAgregar.setText("Agregar");
        mostrarUsuarios(consultasUser.buscarUsuario(consulta[0]));
    }
    
    public void resetPromocion(){
        sistem.txtNombrePromo.setText(null);
        sistem.txtDescripcionPromo.setText(null);
        sistem.txtCodigoPromo.setText(null);
        sistem.comboTipoPromo.setSelectedIndex(0);
        sistem.txtPromocion.setText(null);
        modificarPromocion = false;
        sistem.btnAgregarPromo.setText("Agregar");
        promoSelect = null;
        mostrarPromos(consultasPromo.buscarPromocion(consulta[1]));
    }
    
    public void resetProductos(){
        sistem.txtCodigoM.setText(null);
        sistem.txtCodigo.setText(null);
        sistem.txtProductoM.setText(null);
        sistem.txtProducto.setText(null);
        sistem.txtDescripcionM.setText(null);
        sistem.txtDescripcionP.setText(null);
        sistem.txtCantM.setText(null);
        sistem.txtCantindad.setText(null);
        sistem.txtCostoM.setText(null);
        sistem.txtCostoP.setText(null);
        sistem.panelProducto1.setVisible(true);
        sistem.panelProducto2.setVisible(false);
        productSelect = null;
        mostrarProductos(consultasProduct.buscarProductos(consulta[2]));
    }
    
    ////////// Imprimir tabla de usuarios //////////////////
    
    public void mostrarUsuarios(Set<Usuario> listaUsers){
        listUsuario = listaUsers;
        tablaUsuarios = new DefaultTableModel();
        tablaUsuarios.addColumn("ID");
        tablaUsuarios.addColumn("Nombre");
        tablaUsuarios.addColumn("Apellidos");
        tablaUsuarios.addColumn("Correo");
        tablaUsuarios.addColumn("Telefono");
        tablaUsuarios.addColumn("Direccion");
        tablaUsuarios.addColumn("Codigo Postal");
        tablaUsuarios.addColumn("Usuario");
        tablaUsuarios.addColumn("Rol de trabajo");
        sistem.tablaTrabajadores.setModel(tablaUsuarios);
        for(Usuario usuarioTabla : listaUsers){
            String[] datos = new String[9];
            datos[0] = Integer.toString(usuarioTabla.getId());
            datos[1] = usuarioTabla.getNombre();
            datos[2] = usuarioTabla.getApellidos();
            datos[3] = usuarioTabla.getCorreo();
            datos[4] = usuarioTabla.getTelefono();
            datos[5] = usuarioTabla.getDireccion();
            datos[6] = usuarioTabla.getCPostal();
            datos[7] = usuarioTabla.getUsario();
            switch (usuarioTabla.getRol()) {
                case 0:
                    datos[8] = "Cajero";
                    break;
                case 1:
                    datos[8] = "Veterinario";
                    break;
                default:
                    datos[8] = "Administrador";
                    break;
            }
            
            try{
                tablaUsuarios.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaUsuarios);
        sistem.tablaTrabajadores.setRowSorter(sorter);
        sistem.tablaTrabajadores.getRowSorter().toggleSortOrder(0);
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
    
    ////////// Imprimir tabla de consultas //////////////////
    
    public void mostrarConsultas(Set<Consulta> listaconsults){
        tablaConsultas = new DefaultTableModel();
        tablaConsultas.addColumn("ID");
        tablaConsultas.addColumn("ID Cliente");
        tablaConsultas.addColumn("ID Veterinario");
        tablaConsultas.addColumn("ID Mascota");
        tablaConsultas.addColumn("Fecha");
        tablaConsultas.addColumn("Descripcion");
        tablaConsultas.addColumn("Costo");
        sistem.tablaConsultas.setModel(tablaConsultas);
        for(Consulta consultaTabla : listaconsults){
            String[] datos = new String[7];
            datos[0] = Integer.toString(consultaTabla.getId());
            datos[1] = Integer.toString(consultaTabla.getIdCliente());
            datos[2] = Integer.toString(consultaTabla.getIdVeterinario());
            datos[3] = Integer.toString(consultaTabla.getIdMascota());
            datos[4] = consultaTabla.getFecha();
            datos[5] = consultaTabla.getDescripcion();
            datos[6] = formatoPrecio.format(consultaTabla.getCosto());        
            try{
                tablaConsultas.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaConsultas);
        sistem.tablaConsultas.setRowSorter(sorter);
        sistem.tablaConsultas.getRowSorter().toggleSortOrder(0);
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
    
    ////////// Imprimir tabla de promos //////////////////
    
    public void mostrarPromos(Set<Promocion> listaProms){
        listPromocion = listaProms;
        tablaPromociones = new DefaultTableModel();
        tablaPromociones.addColumn("ID");
        tablaPromociones.addColumn("Nombre");
        tablaPromociones.addColumn("Descripcion");
        tablaPromociones.addColumn("Codigo del Producto");
        tablaPromociones.addColumn("Promocion");
        sistem.tablaPromociones.setModel(tablaPromociones);
        for(Promocion promocionTabla : listaProms){
            String[] datos = new String[9];
            datos[0] = Integer.toString(promocionTabla.getId());
            datos[1] = promocionTabla.getNombre();
            datos[2] = promocionTabla.getDescripcion();
            datos[3] = promocionTabla.getCodigoProducto();
            if(promocionTabla.getPromocion() < 1)
            {
                datos[4] = formatoDescuento.format(promocionTabla.getPromocion()*100) + "%";
            }else{
                datos[4] = formatoDescuento.format(promocionTabla.getPromocion())+"x1";
            }
                        
            try{
                tablaPromociones.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaPromociones);
        sistem.tablaPromociones.setRowSorter(sorter);
        sistem.tablaPromociones.getRowSorter().toggleSortOrder(0);
    }
    
    ////////// Imprimir tabla de productos //////////////////
    
    public void mostrarProductos(Set<Producto> listaProducts){
        listProducto = listaProducts;
        tablaProductos = new DefaultTableModel();
        tablaProductos.addColumn("Codigo del Producto");
        tablaProductos.addColumn("Producto");
        tablaProductos.addColumn("Descripcion");
        tablaProductos.addColumn("Stock Inicial");
        tablaProductos.addColumn("Entradas");
        tablaProductos.addColumn("Salidas");
        tablaProductos.addColumn("Total");
        tablaProductos.addColumn("Costo");
        sistem.tablaProductos.setModel(tablaProductos);
        for(Producto productoTabla : listaProducts){
            String[] datos = new String[8];
            datos[0] = productoTabla.getCodigo();
            datos[1] = productoTabla.getProducto();
            datos[2] = productoTabla.getDescripcion();
            datos[3] = Integer.toString(productoTabla.getStockInicial());
            datos[4] = Integer.toString(productoTabla.getEntradas());
            datos[5] = Integer.toString(productoTabla.getSalidas());
            datos[6] = Integer.toString(productoTabla.getTotal());
            datos[7] = formatoPrecio.format(productoTabla.getCosto());
            
            try{
                tablaProductos.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaProductos);
        sistem.tablaProductos.setRowSorter(sorter);
        sistem.tablaProductos.getRowSorter().toggleSortOrder(0);
    }
    
    ////////// Imprimir tabla de ventas //////////////////
    public void mostrarVentas(Set<Venta> listaVentas){
        tablaHVentas = new DefaultTableModel();
        tablaHVentas.addColumn("ID");
        tablaHVentas.addColumn("Codigo del producto");
        tablaHVentas.addColumn("Cantidad");
        tablaHVentas.addColumn("Costo total");
        tablaHVentas.addColumn("Fecha");
        tablaHVentas.addColumn("Hora");
        tablaHVentas.addColumn("Promocion");
        sistem.tablaVentas.setModel(tablaHVentas);
        for(Venta ventaTabla : listaVentas){
            String[] datos = new String[7];
            datos[0] = Integer.toString(ventaTabla.getId());
            datos[1] = ventaTabla.getCodigoProducto();
            datos[2] = Integer.toString(ventaTabla.getCantidad());
            datos[3] = formatoPrecio.format(ventaTabla.getCostoTotal());
            datos[4] = ventaTabla.getFecha();
            datos[5] = ventaTabla.getHora();
            datos[6] = ventaTabla.getPromocion();
            try{
                tablaHVentas.addRow(datos);
            }
            catch(java.lang.IndexOutOfBoundsException e){}
        }
        RowSorter<TableModel> sorter = new TableRowSorter<>(tablaHVentas);
        sistem.tablaVentas.setRowSorter(sorter);
        sistem.tablaVentas.getRowSorter().toggleSortOrder(0);
    }
    
    /////// Otros Metodos /////////////////////
    
    public void cerrarVentanas(){
        sistem.panelTrabajadores.setVisible(false);
        sistem.panelReportes.setVisible(false);
        sistem.panelInventario.setVisible(false);
        sistem.panelPromociones.setVisible(false);
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
    
    public boolean verificarCorreo(String correo){
        String partes1[] = correo.split("@");
        if(partes1.length > 1){
            String partes2[] = partes1[1].split("\\.");
            if(partes2.length > 1){
                if(!consultasUser.textoVacio(partes2[partes2.length-1])){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public void iniciar(){
        sistem.setVisible(true);
        sistem.setLocationRelativeTo(null);
        cerrarVentanas();   
        sistem.lblBienvenida.setText("BIENVENIDO/A: "+user.getNombre());
    }
}
