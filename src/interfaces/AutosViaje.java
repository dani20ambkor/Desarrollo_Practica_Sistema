/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * AutosViaje.java
 *
 * Created on 16-oct-2017, 10:42:01
 */
package interfaces;

import java.sql.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Invitado_PC16
 */
public class AutosViaje extends javax.swing.JInternalFrame {

    /**
     * Creates new form AutosViaje
     */
    DefaultTableModel model;

    public AutosViaje() {
        initComponents();

        botonesInicio();
        txtBloqueo();
        cargarTablaAutos("");
        tbl_Autos.getSelectionModel().addListSelectionListener(new ListSelectionListener() { //Para seleccionar datos de la tabla y crear una nueva lista con los datos seleccionados

            @Override
            public void valueChanged(ListSelectionEvent e) { //Cambiar automaticamente los valor de la lista a la hora de seleccionar
                if (tbl_Autos.getSelectedRow() != -1) {  //Si esta seleccionada una fila 
                    int fila = tbl_Autos.getSelectedRow(); //Se coge la fila en una variable porque no sabemos en cual esta ubicado lo que queremos coger

                    txt_Placa.setText(tbl_Autos.getValueAt(fila, 0).toString().trim()); //trim = quita los espacios en blanco
                    txt_Marca.setText(tbl_Autos.getValueAt(fila, 1).toString().trim());
                    txt_Modelo.setText(tbl_Autos.getValueAt(fila, 2).toString().trim());
                    txt_Color.setText(tbl_Autos.getValueAt(fila, 3).toString().trim());
                    // txt_.setText(tbl_Autos.getValueAt(fila, 0).toString());
                    txt_Descripcion.setText(tbl_Autos.getValueAt(fila, 5).toString().trim());
                    txtDesbloqueo();
                    txt_Placa.setEnabled(false);
                    botonesActualizar();
                    bonotesBorrar();
                }
            }
        });
        model.addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE) {
                    int columna = e.getColumn();
                    int fila = e.getLastRow();
                    String nombreColumna = null;

                    if (columna == 1) {
                        nombreColumna = "AUT_MARCA";
                    } else if (columna == 2) {
                        nombreColumna = "AUT_MODELO";
                    } else if (columna == 3) {
                        nombreColumna = "AUT_COLOR";
                    } else if (columna == 4) {
                        nombreColumna = "AUT_ANIO";
                    } else if (columna == 5) {
                        nombreColumna = "AUT_DESCRIPCION";
                    }
                    String sql = "update auto set " + nombreColumna + "='" + tbl_Autos.getValueAt(fila, columna) + "'where AUT_PLACA='" + tbl_Autos.getValueAt(fila, 0) + "'";
                    conexionViaje cc = new conexionViaje();
                    Connection cn = cc.conectar();
                    PreparedStatement pst;
                    try {
                        pst = cn.prepareStatement(sql);
                        pst.executeUpdate();;
                    } catch (SQLException ex) {
                    }

                }


            }
        });
    }

    //Método para la tabla
    public void cargarTablaAutos(String Dato) {

        String[] titulos = {"PLACA", "MARCA", "MODELO", "COLOR", "AÑO", "DESCRIPCION"}; //Se conoce la cantidad
        String[] registros = new String[6]; //No se conoce la cantidad
        model = new DefaultTableModel(null, titulos) {

            @Override
            public boolean isCellEditable(int row, int column) {
                if (column == 0) {
                    return false;
                }
                return true;
            }
        };


        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "select * from auto where ESTADO=" + 0 + " AND AUT_PLACA LIKE'%" + Dato + "%'"; //Para hacer un select en la base de datos
        //sql = "select * from auto where AUT_PLACA LIKE'%" + Dato + "%'"; //Para hacer un select en la base de datos
        try {
            Statement psd = cn.createStatement(); //Devuelve toda la informacion de la base
            ResultSet rs = psd.executeQuery(sql); //Permite manejar celda por celda el resultado que obtenemos con el Statement
            while (rs.next()) { //Mientras tenga una fila, que recorra
                registros[0] = rs.getString("AUT_PLACA");
                registros[1] = rs.getString("AUT_MARCA");
                registros[2] = rs.getString("AUT_MODELO");
                registros[3] = rs.getString("AUT_COLOR");
                registros[4] = rs.getString("AUT_ANIO");
                registros[5] = rs.getString("AUT_DESCRIPCION");
                model.addRow(registros);


            }
            tbl_Autos.setModel(model); //Setear el modelo una sola vez
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void borrar() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "update auto set ESTADO ='" + 1 + "' where AUT_PLACA ='" + txt_Placa.getText() + "'";
        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            psd.executeUpdate();
            cargarTablaAutos("");
            txtBloqueo();
            botonesInicio();
            txtLimpiar();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }


    }

    public void txtBloqueo() {
        txt_Placa.setEnabled(false);
        txt_Marca.setEnabled(false);
        txt_Modelo.setEnabled(false);
        txt_Color.setEnabled(false);
        jy_Anio_Auto.setEnabled(false);
        txt_Descripcion.setEnabled(false);
    }

    public void txtDesbloqueo() {
        txt_Placa.requestFocus();
        txt_Placa.setEnabled(true);
        txt_Marca.setEnabled(true);
        txt_Modelo.setEnabled(true);
        txt_Color.setEnabled(true);
        jy_Anio_Auto.setEnabled(true);
        txt_Descripcion.setEnabled(true);
    }

    public void botonesInicio() {
        btn_Nuevo.setEnabled(true); //Para que este activo
        btn_Guardar.setEnabled(false);
        btn_Actualizar.setEnabled(false);
        btn_Cancelar.setEnabled(false);
        btn_Borrar.setEnabled(false);
        btn_Salir.setEnabled(true);
    }

    public void botonesNuevo() {
        btn_Nuevo.setEnabled(false);
        btn_Guardar.setEnabled(true);
        btn_Actualizar.setEnabled(true);
        btn_Cancelar.setEnabled(true);
        btn_Borrar.setEnabled(true);
        btn_Salir.setEnabled(true);
    }

    public void botonesActualizar() {
        btn_Nuevo.setEnabled(false);
        btn_Guardar.setEnabled(false);
        btn_Actualizar.setEnabled(true);
        btn_Cancelar.setEnabled(true);
        btn_Borrar.setEnabled(false);
        btn_Salir.setEnabled(true);
    }

    public void bonotesBorrar() {
        btn_Nuevo.setEnabled(false);
        btn_Guardar.setEnabled(false);
        btn_Actualizar.setEnabled(true);
        btn_Cancelar.setEnabled(true);
        btn_Borrar.setEnabled(true);
        btn_Salir.setEnabled(true);
    }

    public void txtLimpiar() {
        txt_Placa.setText("");
        txt_Placa.setText("");
        txt_Marca.setText("");
        txt_Modelo.setText("");
        txt_Color.setText("");
        txt_Descripcion.setText("");
    }

    public void guardar() {


        if (txt_Placa.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Placa");
            txt_Placa.requestFocus(); // Para posicionar el raton
        } else if (txt_Marca.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Marca");
        } else if (txt_Modelo.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Modelo");
        } else if (txt_Color.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Color");
        } else {

            conexionViaje cc = new conexionViaje();
            Connection cn = cc.conectar();
            String AUT_PLACA, AUT_MARCA, AUT_MODELO, AUT_COLOR, AUT_ANIO, AUT_DESCRIPCION, ESTADO;
            AUT_PLACA = txt_Placa.getText();
            AUT_MARCA = txt_Marca.getText();
            AUT_MODELO = txt_Modelo.getText();
            AUT_COLOR = txt_Color.getText();
            AUT_ANIO = String.valueOf(jy_Anio_Auto.getYear());
            if (txt_Descripcion.getText().isEmpty()) {
                AUT_DESCRIPCION = "Sin información";
            } else {
                AUT_DESCRIPCION = txt_Descripcion.getText();
            }
            ESTADO = "0";


            String sql = "";
            sql = "insert into auto(AUT_PLACA, AUT_MARCA, AUT_MODELO, AUT_COLOR, AUT_ANIO, AUT_DESCRIPCION, ESTADO)"
                    + "values(?,?,?,?,?,?,?)";
            try {
                PreparedStatement psd = cn.prepareStatement(sql);
                psd.setString(1, AUT_PLACA); //(Numero de campo/ nombre)
                psd.setString(2, AUT_MARCA);
                psd.setString(3, AUT_MODELO);
                psd.setString(4, AUT_COLOR);
                psd.setString(5, AUT_ANIO);
                psd.setString(6, AUT_DESCRIPCION);
                psd.setString(7, ESTADO);

                int n = psd.executeUpdate();

                if (n > 0) {
                    JOptionPane.showMessageDialog(null, "Se insertó la información correctamente");
                    cargarTablaAutos(""); //Actualizar la carga de datos
                    txtLimpiar();













                }

            } catch (SQLException ex) { //permite manejar la excepcion de la base de datos
                Logger.getLogger(AutosViaje.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(
                        null, ex);
            }
        }


    }

    public void actualizar() {
        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";

        sql = "update auto set "
                + "AUT_MARCA ='" + txt_Marca.getText() + "' , "
                + "AUT_MODELO ='" + txt_Modelo.getText() + "' , "
                + "AUT_COLOR ='" + txt_Color.getText() + "' , "
                + "AUT_ANIO ='" + jy_Anio_Auto.getYear() + "' , "
                + "AUT_DESCRIPCION ='" + txt_Descripcion.getText() + "' "
                + "where AUT_PLACA ='" + txt_Placa.getText() + "'";

        try {
            PreparedStatement psd = cn.prepareStatement(sql);
            int n = psd.executeUpdate();

            if (n > 0) {
                JOptionPane.showMessageDialog(null, "Se actualizo el registro correctamente");
                cargarTablaAutos("");
                txtLimpiar();
                txtBloqueo();
                botonesInicio();
            }


        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txt_Placa = new javax.swing.JTextField();
        txt_Marca = new javax.swing.JTextField();
        txt_Modelo = new javax.swing.JTextField();
        txt_Color = new javax.swing.JTextField();
        txt_Descripcion = new javax.swing.JTextField();
        jy_Anio_Auto = new com.toedter.calendar.JYearChooser();
        jLabel7 = new javax.swing.JLabel();
        txt_BuscarPlaca = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        btn_Nuevo = new javax.swing.JButton();
        btn_Guardar = new javax.swing.JButton();
        btn_Actualizar = new javax.swing.JButton();
        btn_Cancelar = new javax.swing.JButton();
        btn_Borrar = new javax.swing.JButton();
        btn_Salir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Autos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Placa:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Marca:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel3.setText("Modelo:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel4.setText("Color:");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Año:");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Descripción:");

        txt_Placa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_PlacaKeyTyped(evt);
            }
        });

        txt_Marca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_MarcaKeyTyped(evt);
            }
        });

        txt_Modelo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ModeloKeyTyped(evt);
            }
        });

        txt_Color.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_ColorKeyTyped(evt);
            }
        });

        txt_Descripcion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_DescripcionKeyTyped(evt);
            }
        });

        jy_Anio_Auto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                jy_Anio_AutoKeyTyped(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Buscar:");

        txt_BuscarPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_BuscarPlacaKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_Placa, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_Marca, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(txt_Color, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)
                                .addComponent(jy_Anio_Auto, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(72, 107, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(txt_Modelo, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_BuscarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txt_Descripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 244, Short.MAX_VALUE)
                                .addContainerGap())))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel1))
                    .addComponent(txt_Placa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel2))
                    .addComponent(txt_Marca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel3))
                    .addComponent(txt_Modelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel4))
                    .addComponent(txt_Color, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jy_Anio_Auto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel6))
                    .addComponent(txt_Descripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txt_BuscarPlaca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Opciones"));

        btn_Nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/nuevo.png"))); // NOI18N
        btn_Nuevo.setText("Nuevo");
        btn_Nuevo.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Nuevo.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btn_Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NuevoActionPerformed(evt);
            }
        });

        btn_Guardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/guardar.png"))); // NOI18N
        btn_Guardar.setText("Guardar");
        btn_Guardar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_Actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar.png"))); // NOI18N
        btn_Actualizar.setText("Actualizar");
        btn_Actualizar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Actualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ActualizarActionPerformed(evt);
            }
        });

        btn_Cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        btn_Cancelar.setText("Cacelar");
        btn_Cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Cancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CancelarActionPerformed(evt);
            }
        });

        btn_Borrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.png"))); // NOI18N
        btn_Borrar.setText("Borrar");
        btn_Borrar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Borrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BorrarActionPerformed(evt);
            }
        });

        btn_Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        btn_Salir.setText("Salir");
        btn_Salir.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_SalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btn_Borrar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Cancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Nuevo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Guardar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Actualizar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Salir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_Nuevo)
                .addGap(18, 18, 18)
                .addComponent(btn_Guardar)
                .addGap(18, 18, 18)
                .addComponent(btn_Actualizar)
                .addGap(18, 18, 18)
                .addComponent(btn_Cancelar)
                .addGap(18, 18, 18)
                .addComponent(btn_Borrar)
                .addGap(18, 18, 18)
                .addComponent(btn_Salir)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        tbl_Autos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbl_Autos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
// TODO add your handling code here:
    guardar();
}//GEN-LAST:event_btn_GuardarActionPerformed

private void btn_NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NuevoActionPerformed
// TODO add your handling code here:
    txtDesbloqueo();
    botonesNuevo();
}//GEN-LAST:event_btn_NuevoActionPerformed

private void btn_CancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CancelarActionPerformed
// TODO add your handling code here:
    txtLimpiar();
    txtBloqueo();
    botonesInicio();
}//GEN-LAST:event_btn_CancelarActionPerformed

private void btn_SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_SalirActionPerformed
// TODO add your handling code here:
    this.dispose(); //Dispose se cierra solo la ventana en la que se encuentra
}//GEN-LAST:event_btn_SalirActionPerformed

private void btn_ActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ActualizarActionPerformed
// TODO add your handling code here:
    actualizar();
}//GEN-LAST:event_btn_ActualizarActionPerformed

private void txt_BuscarPlacaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_BuscarPlacaKeyReleased
// TODO add your handling code here:
    cargarTablaAutos(txt_BuscarPlaca.getText());
}//GEN-LAST:event_txt_BuscarPlacaKeyReleased

private void btn_BorrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BorrarActionPerformed
// TODO add your handling code here:
    int dialogButton = JOptionPane.YES_NO_OPTION;
    int n = JOptionPane.showConfirmDialog(null, "Esta seguro que desea borrar el dato", "Aviso", dialogButton);

    if (n == JOptionPane.YES_OPTION) {
        borrar();
    }






}//GEN-LAST:event_btn_BorrarActionPerformed

    private void txt_PlacaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_PlacaKeyTyped
        // TODO add your handling code here:
        if (txt_Placa.getText().length() >= 7) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_PlacaKeyTyped

    private void txt_MarcaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_MarcaKeyTyped
        // TODO add your handling code here:
        if (txt_Marca.getText().length() >= 15) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_MarcaKeyTyped

    private void txt_ModeloKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ModeloKeyTyped
        // TODO add your handling code here:
        if (txt_Modelo.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_ModeloKeyTyped

    private void txt_ColorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_ColorKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
        if (txt_Color.getText().length() >= 10) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_ColorKeyTyped

    private void jy_Anio_AutoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jy_Anio_AutoKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_jy_Anio_AutoKeyTyped

    private void txt_DescripcionKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_DescripcionKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
        if (txt_Descripcion.getText().length() >= 30) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_DescripcionKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;












                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AutosViaje.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new AutosViaje().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Actualizar;
    private javax.swing.JButton btn_Borrar;
    private javax.swing.JButton btn_Cancelar;
    private javax.swing.JButton btn_Guardar;
    private javax.swing.JButton btn_Nuevo;
    private javax.swing.JButton btn_Salir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private com.toedter.calendar.JYearChooser jy_Anio_Auto;
    private javax.swing.JTable tbl_Autos;
    private javax.swing.JTextField txt_BuscarPlaca;
    private javax.swing.JTextField txt_Color;
    private javax.swing.JTextField txt_Descripcion;
    private javax.swing.JTextField txt_Marca;
    private javax.swing.JTextField txt_Modelo;
    private javax.swing.JTextField txt_Placa;
    // End of variables declaration//GEN-END:variables
}
