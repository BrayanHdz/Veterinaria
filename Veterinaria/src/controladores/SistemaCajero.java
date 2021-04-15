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
import vistas.VistaCajero;

public class SistemaCajero{
    private final VistaCajero sistem;
    private final Usuario user;
    private Producto productSelect;
    private final ConsultasUsuario consultasUser;
    private final ConsultasProducto consultasProduct;
    private final ConsultasPromocion consultasPromo;
    private final ConsultasVenta consultasVent;
    private final DecimalFormat formatoPrecio, formatoDescuento;
    private Set<Producto> listProducto;
    private DefaultTableModel tablaVentas,tablaProductos,tablaPromociones,tablaHVentas;
    private final String consulta[] = {"",""};
    private boolean promocionActivada = false;
    private float total, descuento;
    
    public SistemaCajero(Usuario user, VistaCajero sistem){
        this.listProducto = new HashSet<>();
        this.formatoPrecio = new DecimalFormat("0.00");
        this.formatoDescuento = new DecimalFormat("0.##");
        this.consultasUser = new ConsultasUsuario();
        this.consultasProduct = new ConsultasProducto();
        this.consultasPromo = new ConsultasPromocion();
        this.consultasVent = new ConsultasVenta();
        
        this.user = user;
        this.sistem = sistem;
        this.sistem.btnAgregarCarrito.addActionListener(this::eventListener);
        this.sistem.btnEliminarCarrito.addActionListener(this::eventListener);
        this.sistem.btnComprar.addActionListener(this::eventListener);
        this.sistem.btnVentas.addActionListener(this::eventListener);
        this.sistem.btnReporte.addActionListener(this::eventListener);
        this.sistem.txtConsultaProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
            }
        });
        this.sistem.txtConsultaVenta.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tecla(evt);
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
    
    ///////////////////////// Se busco un producto o promocion //////////////
    public void tecla(KeyEvent tecla){
        if(tecla.getSource() == sistem.txtConsultaProducto){
            if(sistem.txtConsultaProducto.getText().equals("")){
                consulta[0] = "";
            }else{consulta[0] = sistem.txtConsultaProducto.getText();}
            mostrarProductos(consultasProduct.buscarProductos(consulta[0]));
            mostrarPromos(consultasPromo.buscarPromocion(consulta[0]));
        }
        
        if(tecla.getSource() == sistem.txtConsultaVenta){
            if(sistem.txtConsultaVenta.getText().equals("")){
                consulta[1] = "";
            }else{consulta[1] = sistem.txtConsultaVenta.getText();}
            mostrarVentas(consultasVent.buscarVenta(consulta[1], user));
        }
    }
    
    ///////////////////////// Se selecciono un producto o promocion //////////////
    public void mouseClicTable(MouseEvent evt){
        if(evt.getSource() == sistem.tablaProductos){
            sistem.txtCProducto.setText((String) sistem.tablaProductos.getValueAt(sistem.tablaProductos.getSelectedRow(), 0));
            sistem.sCantidad.setValue(1);
            promocionActivada = false;
            sistem.sCantidad.setEnabled(true);
        }
        
        if(evt.getSource() == sistem.tablaPromociones){
            sistem.txtCProducto.setText(sistem.tablaPromociones.getValueAt(sistem.tablaPromociones.getSelectedRow(), 1).toString());
            promocionActivada = true;
            String texto = sistem.tablaPromociones.getValueAt(sistem.tablaPromociones.getSelectedRow(), 4).toString();
            if(texto.split("x").length>1)
            {
                String datos[] = texto.split("x");
                sistem.sCantidad.setValue(Integer.parseInt(datos[0].toString()));
                sistem.sCantidad.setEnabled(false);
                descuento = Float.parseFloat(datos[0]);
            }else{
                String datos[] = texto.split("%");
                sistem.sCantidad.setValue(1);
                sistem.sCantidad.setEnabled(true);
                descuento = Float.parseFloat(datos[0])/100;
            }
        }
    }
    
    public void eventListener(ActionEvent clic){    
        if(clic.getSource() == sistem.btnVentas){
            cerrarVentanas();
            sistem.panelVentas.setVisible(true);
            mostrarProductos(consultasProduct.buscarProductos(consulta[0]));
            mostrarPromos(consultasPromo.buscarPromocion(consulta[0]));
        }    
        if(clic.getSource() == sistem.btnReporte){
            cerrarVentanas();
            sistem.panelHistorialVentas.setVisible(true);
            mostrarVentas(consultasVent.buscarVenta(consulta[1], user));
        }
        ///////////////////////// Se preciono el boton para agregar un producto a la Compra //////////////    
        if(clic.getSource() == sistem.btnAgregarCarrito){
            if(consultasUser.textoVacio(sistem.txtCProducto.getText()) || Integer.parseInt(sistem.sCantidad.getValue().toString()) < 1 || !esEntero(sistem.sCantidad.getValue().toString())){
                JOptionPane.showMessageDialog(null, "Datos invalidos.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                for(Producto producto : listProducto){
                    if(producto.getCodigo().equals(sistem.txtCProducto.getText())){
                        if(producto.getTotal() < (int)sistem.sCantidad.getValue()){
                            JOptionPane.showMessageDialog(null, "Stock en inventario insuficiente.", "ERROR", JOptionPane.ERROR_MESSAGE);
                        }else{
                            boolean existe = false;
                            int pos;
                            for(pos= 0; pos<sistem.tablaCompras.getRowCount(); pos++){
                                if(producto.getCodigo().equals((String)sistem.tablaCompras.getValueAt(pos, 0)) && sistem.tablaCompras.getValueAt(pos, 5).toString().equals("")){
                                    existe = true;
                                    break;
                                }
                            }
                            
                            if (existe){
                                int cantidad = Integer.parseInt(sistem.tablaCompras.getValueAt(pos, 3).toString()) + Integer.parseInt(sistem.sCantidad.getValue().toString());
                                sistem.tablaCompras.setValueAt(cantidad, pos, 3);
                                total += Integer.parseInt(sistem.sCantidad.getValue().toString()) * producto.getCosto();
                            }
                            else{    
                                String[] datos = new String[6];
                                datos[0] = producto.getCodigo();
                                datos[1] = producto.getProducto();
                                datos[2] = producto.getDescripcion();
                                datos[3] = sistem.sCantidad.getValue().toString();
                                datos[4] = formatoPrecio.format(producto.getCosto());
                                if(promocionActivada){
                                    if(descuento < 1){
                                        datos[4] = formatoPrecio.format(producto.getCosto()-producto.getCosto()* descuento);
                                        datos[5] = formatoDescuento.format(descuento*100) + "%";
                                        total += producto.getCosto() - ((int) sistem.sCantidad.getValue() * producto.getCosto()) * descuento;
                                    }
                                    else{
                                        datos[5] = formatoDescuento.format(descuento)+"x1";
                                        total += producto.getCosto();
                                    }
                                }else{
                                    datos[5] = "";
                                    total += (int) sistem.sCantidad.getValue() * producto.getCosto();
                                }
                            
                                try{
                                    tablaVentas.addRow(datos);
                                }
                                catch(java.lang.IndexOutOfBoundsException e){}
                            }
                            sistem.txtCProducto.setText(null);
                            sistem.sCantidad.setValue(0);
                            sistem.sCantidad.setEnabled(true);
                            sistem.lblTotal.setText("TOTAL: "+formatoPrecio.format(total));
                        }
                    }
                }
                sistem.tablaCompras.setModel(tablaVentas);
            }
        }
        
        ///////////////////////// Se preciono el boton para quitar un elemento de la Compra //////////////
        if(clic.getSource() == sistem.btnEliminarCarrito && sistem.tablaCompras.getRowCount() > 0){
            total -= Float.parseFloat(sistem.tablaCompras.getValueAt(sistem.tablaCompras.getSelectedRow(), 4).toString()) * Integer.parseInt(sistem.tablaCompras.getValueAt(sistem.tablaCompras.getSelectedRow(), 3).toString());
            try{
                tablaVentas.removeRow(sistem.tablaCompras.getSelectedRow());
            }
            catch(java.lang.IndexOutOfBoundsException e){System.err.println(e);}
            
            sistem.tablaCompras.setModel(tablaVentas);
            sistem.sCantidad.setValue(0);
            sistem.lblTotal.setText("TOTAL: "+formatoPrecio.format(total));
        }
        
        ///////////////////////// Se preciono el boton para terminar la Compra //////////////
        if(clic.getSource() == sistem.btnComprar && sistem.tablaCompras.getRowCount() > 0){
            boolean exito = true;
            while(sistem.tablaCompras.getRowCount() > 0){
                productSelect = new Producto();
                productSelect.setCodigo((String) sistem.tablaCompras.getValueAt(sistem.tablaCompras.getRowCount()-1, 0));
                productSelect.setSalidas(Integer.parseInt(sistem.tablaCompras.getValueAt(sistem.tablaCompras.getRowCount()-1, 3).toString()));
                if(consultasProduct.comprarProducto(productSelect)){
                    Float totalCompra = Float.parseFloat((String) sistem.tablaCompras.getValueAt(sistem.tablaCompras.getRowCount()-1, 4)) * Integer.parseInt(sistem.tablaCompras.getValueAt(sistem.tablaCompras.getRowCount()-1, 3).toString());
                    total -= totalCompra;
                    Venta venta = new Venta();
                    venta.setIdCajero(user.getId());
                    venta.setCodigoProducto(productSelect.getCodigo());
                    venta.setCantidad(productSelect.getSalidas());
                    venta.setCostoTotal(totalCompra);
                    venta.setFecha(java.time.LocalDate.now().toString());
                    venta.setHora(java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("kk:mm")));
                    venta.setPromocion((String) sistem.tablaCompras.getValueAt(sistem.tablaCompras.getRowCount()-1, 5));
                    consultasVent.agregarVenta(venta);
                    try{
                        tablaVentas.removeRow(sistem.tablaCompras.getRowCount()-1);
                    }
                    catch(java.lang.IndexOutOfBoundsException e){System.err.println(e);}

                    sistem.tablaCompras.setModel(tablaVentas);
                    sistem.sCantidad.setValue(0);
                    mostrarProductos(consultasProduct.buscarProductos(consulta[0]));
                    if(sistem.tablaCompras.getRowCount() == 0){total = 0.0f;}
                    sistem.lblTotal.setText("TOTAL: "+formatoPrecio.format(total));
                }else{exito = false;}
            }
            if(exito){
                JOptionPane.showMessageDialog(null, "La compra se ha realizado correctamente.", "AVISO", JOptionPane.INFORMATION_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Ha ocurrido un error.", "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    ////////// Imprimir tabla de promociones //////////////////
    public void mostrarPromos(Set<Promocion> listaProms){
        tablaPromociones = new DefaultTableModel();
        tablaPromociones.addColumn("ID");
        tablaPromociones.addColumn("Codigo del producto");
        tablaPromociones.addColumn("Nombre");
        tablaPromociones.addColumn("Descripcion");
        tablaPromociones.addColumn("Promocion");
        sistem.tablaPromociones.setModel(tablaPromociones);
        for(Promocion promocionTabla : listaProms){
            String[] datos = new String[5];
            datos[0] = Integer.toString(promocionTabla.getId());
            datos[1] = promocionTabla.getCodigoProducto();
            datos[2] = promocionTabla.getNombre();
            datos[3] = promocionTabla.getDescripcion();
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
        tablaProductos.addColumn("Total");
        tablaProductos.addColumn("Costo");
        sistem.tablaProductos.setModel(tablaProductos);
        for(Producto productoTabla : listaProducts){
            String[] datos = new String[5];
            datos[0] = productoTabla.getCodigo();
            datos[1] = productoTabla.getProducto();
            datos[2] = productoTabla.getDescripcion();
            datos[3] = Integer.toString(productoTabla.getTotal());
            datos[4] = formatoPrecio.format(productoTabla.getCosto());
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
        sistem.panelVentas.setVisible(false);
        sistem.panelHistorialVentas.setVisible(false);
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
    
    public void iniciar(){
        sistem.setVisible(true);
        sistem.setLocationRelativeTo(null);
        cerrarVentanas();   
        sistem.panelVentas.setVisible(true);
        mostrarProductos(consultasProduct.buscarProductos(consulta[0]));
        mostrarPromos(consultasPromo.buscarPromocion(consulta[0]));
        tablaVentas = new DefaultTableModel();
        tablaVentas.addColumn("Codigo del producto");
        tablaVentas.addColumn("Producto");
        tablaVentas.addColumn("Descripcion");
        tablaVentas.addColumn("Cantidad");
        tablaVentas.addColumn("Costo");
        tablaVentas.addColumn("Promocion");
        sistem.tablaCompras.setModel(tablaVentas);
        sistem.lblBienvenida.setText("BIENVENIDO/A: "+user.getNombre());
    }
}
