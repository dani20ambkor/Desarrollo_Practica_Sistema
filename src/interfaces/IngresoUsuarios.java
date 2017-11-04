/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author PC
 */
public class IngresoUsuarios extends javax.swing.JFrame {

    /**
     * Creates new form IngresoUsuarios
     */
    DefaultTableModel model;

    public IngresoUsuarios() {
        initComponents();
        botonesInicio();
        txtBloqueo();
        cargarTablaUsuarios();
    }

    public void cargarTablaUsuarios() {

        String[] titulos = {"CÉDULA", "NOMBRE", "APELLIDO", "PERFIL", "CLAVE"}; //Se conoce la cantidad
        String[] registros = new String[5]; //No se conoce la cantidad
        model = new DefaultTableModel(null, titulos) {

            @Override
            public boolean isCellEditable(int row, int column) { //Bloquear la primera columna Clave primaria 
                if (column == 0) {
                    return false;
                }
                return true;
            }
        };


        conexionViaje cc = new conexionViaje();
        Connection cn = cc.conectar();
        String sql = "";
        sql = "select * from usuarios"; //Para hacer un select en la base de datos
        //sql = "select * from usuarios where USU_CEDULA = '" + Dato +"; //Para hacer un select en la base de datos
        try {
            Statement psd = cn.createStatement(); //Devuelve toda la informacion de la base
            ResultSet rs = psd.executeQuery(sql); //Permite manejar celda por celda el resultado que obtenemos con el Statement
            while (rs.next()) { //Mientras tenga una fila, que recorra
                registros[0] = rs.getString("USU_CEDULA");
                registros[1] = rs.getString("USU_NOMBRE");
                registros[2] = rs.getString("USU_APELLIDO");
                registros[3] = rs.getString("USU_PERFIL");
                registros[4] = rs.getString("USU_CLAVE");
                model.addRow(registros);


            }
            tbl_Usuarios.setModel(model); //Setear el modelo una sola vez
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    public void guardar() {


        if (txt_CedulaUsu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el numero de Cédula");
            txt_CedulaUsu.requestFocus(); // Para posicionar el raton
        } else if (txt_NombreUsu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Nombre");
        } else if (txt_ApellidoUsu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Apellido");
        } else if (txt_PerfilUsu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar el Perfil");
        } else if (txt_ClaveUsu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe ingresar la Clave");
        } else if (txt_ConfirmacionClaveUsu.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe confirmar la Clave");
        } else {
            String clav = String.valueOf(txt_ClaveUsu.getPassword());
            String clav2 = String.valueOf(txt_ConfirmacionClaveUsu.getPassword());
            if (clav.equals(clav2)) {
                conexionViaje cc = new conexionViaje();
                Connection cn = cc.conectar();
                String USU_CEDULA, USU_NOMBRE, USU_APELLIDO, USU_PERFIL, USU_CLAVE;
                USU_CEDULA = txt_CedulaUsu.getText();
                USU_NOMBRE = txt_NombreUsu.getText();
                USU_APELLIDO = txt_ApellidoUsu.getText();
                USU_PERFIL = txt_PerfilUsu.getText();
                USU_CLAVE = encriptarClave(clav);

                String sql = "";
                sql = "insert into usuarios(USU_CEDULA, USU_NOMBRE, USU_APELLIDO, USU_PERFIL, USU_CLAVE)"
                        + "values(?,?,?,?,?)";
                try {
                    PreparedStatement psd = cn.prepareStatement(sql);
                    psd.setString(1, USU_CEDULA); //(Numero de campo/ nombre)
                    psd.setString(2, USU_NOMBRE);
                    psd.setString(3, USU_APELLIDO);
                    psd.setString(4, USU_PERFIL);
                    psd.setString(5, USU_CLAVE);


                    int n = psd.executeUpdate();

                    if (n > 0) {
                        JOptionPane.showMessageDialog(null, "Se insertó la información correctamente");
                        cargarTablaUsuarios(); //Actualizar la carga de datos
                        //cargarTablaUsuarios("");
                        txtLimpiar();

                    }

                } catch (SQLException ex) { //permite manejar la excepcion de la base de datos
                    Logger.getLogger(AutosViaje.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(
                            null, ex);
                }
            }else{
            JOptionPane.showMessageDialog(null, "Clave no coincide");
            txt_ClaveUsu.setText("");
            txt_ConfirmacionClaveUsu.setText("");
            txt_ClaveUsu.requestFocus(); 
            }

        }

    }

    public String encriptarClave(String texto) {
        int clave = 6;
        String tabla = "abcdefghijklmnñopqrstuvwxyzáéíóúABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ1234567890.,;_:+-*/ @$#¿?!Â¡=()[]{}\\\"";

        String resultado = "";
        for (int i = 0; i < texto.length(); i++) {

            int pos = tabla.indexOf(texto.charAt(i));

            if ((pos + clave) < tabla.length()) {
                resultado += tabla.charAt(pos + clave);
            } else {
                resultado += tabla.charAt((pos + clave) - tabla.length());
            }

        }
        return resultado;
    }

  

    public void txtLimpiar() {
        txt_CedulaUsu.setText("");
        txt_NombreUsu.setText("");
        txt_ApellidoUsu.setText("");
        txt_PerfilUsu.setText("");
        txt_ClaveUsu.setText("");
        txt_ConfirmacionClaveUsu.setText("");
    }

    public void botonesInicio() {
        btn_Nuevo.setEnabled(true); //Para que este activo
        btn_Guardar.setEnabled(false);
        btn_Actualizar.setEnabled(false);
        btn_Cancelar.setEnabled(false);
        btn_Borrar.setEnabled(false);
        btn_Salir.setEnabled(true);
    }

    public void txtBloqueo() {
        txt_CedulaUsu.setEnabled(false);
        txt_NombreUsu.setEnabled(false);
        txt_ApellidoUsu.setEnabled(false);
        txt_PerfilUsu.setEnabled(false);
        txt_ClaveUsu.setEnabled(false);
        txt_ConfirmacionClaveUsu.setEnabled(false);
    }

    public void txtDesbloqueo() {
        txt_CedulaUsu.requestFocus();
        txt_CedulaUsu.setEnabled(true);
        txt_NombreUsu.setEnabled(true);
        txt_ApellidoUsu.setEnabled(true);
        txt_PerfilUsu.setEnabled(true);
        txt_ClaveUsu.setEnabled(true);
        txt_ConfirmacionClaveUsu.setEnabled(true);
    }

    public void botonesNuevo() {
        btn_Nuevo.setEnabled(false);
        btn_Guardar.setEnabled(true);
        btn_Actualizar.setEnabled(true);
        btn_Cancelar.setEnabled(true);
        btn_Borrar.setEnabled(true);
        btn_Salir.setEnabled(true);
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
        txt_CedulaUsu = new javax.swing.JTextField();
        txt_NombreUsu = new javax.swing.JTextField();
        txt_ApellidoUsu = new javax.swing.JTextField();
        txt_PerfilUsu = new javax.swing.JTextField();
        txt_ClaveUsu = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        txt_ConfirmacionClaveUsu = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        btn_Nuevo = new javax.swing.JButton();
        btn_Guardar = new javax.swing.JButton();
        btn_Actualizar = new javax.swing.JButton();
        btn_Cancelar = new javax.swing.JButton();
        btn_Borrar = new javax.swing.JButton();
        btn_Salir = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbl_Usuarios = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos"));

        jLabel1.setText("Cédula:");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Apellido:");

        jLabel4.setText("Perfil:");

        jLabel5.setText("Clave:");

        jLabel6.setText("Confirmación \nClave:");

        txt_ConfirmacionClaveUsu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_ConfirmacionClaveUsuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_ConfirmacionClaveUsu, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(txt_ClaveUsu)
                    .addComponent(txt_PerfilUsu)
                    .addComponent(txt_ApellidoUsu)
                    .addComponent(txt_NombreUsu)
                    .addComponent(txt_CedulaUsu))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txt_CedulaUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel2))
                    .addComponent(txt_NombreUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txt_ApellidoUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txt_PerfilUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel5))
                    .addComponent(txt_ClaveUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(txt_ConfirmacionClaveUsu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel6.getAccessibleContext().setAccessibleName("Confirmación \nClave:");

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
        btn_Guardar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        btn_Guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_GuardarActionPerformed(evt);
            }
        });

        btn_Actualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/actualizar.png"))); // NOI18N
        btn_Actualizar.setText("Actualizar");
        btn_Actualizar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Actualizar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        btn_Cancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cancelar.png"))); // NOI18N
        btn_Cancelar.setText("Cancelar");
        btn_Cancelar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Cancelar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        btn_Borrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/borrar.png"))); // NOI18N
        btn_Borrar.setText("Borrar");
        btn_Borrar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Borrar.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        btn_Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/salir.png"))); // NOI18N
        btn_Salir.setText("Salir");
        btn_Salir.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btn_Salir.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_Guardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Cancelar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Borrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Nuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Actualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_Salir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tbl_Usuarios.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane1.setViewportView(tbl_Usuarios);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txt_ConfirmacionClaveUsuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_ConfirmacionClaveUsuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_ConfirmacionClaveUsuActionPerformed

    private void btn_GuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_GuardarActionPerformed
        // TODO add your handling code here:
        guardar();
    }//GEN-LAST:event_btn_GuardarActionPerformed

    private void btn_NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NuevoActionPerformed
        // TODO add your handling code here:
        txtDesbloqueo();
        botonesNuevo();
    }//GEN-LAST:event_btn_NuevoActionPerformed

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
            java.util.logging.Logger.getLogger(IngresoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(IngresoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(IngresoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(IngresoUsuarios.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new IngresoUsuarios().setVisible(true);
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
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbl_Usuarios;
    private javax.swing.JTextField txt_ApellidoUsu;
    private javax.swing.JTextField txt_CedulaUsu;
    private javax.swing.JPasswordField txt_ClaveUsu;
    private javax.swing.JPasswordField txt_ConfirmacionClaveUsu;
    private javax.swing.JTextField txt_NombreUsu;
    private javax.swing.JTextField txt_PerfilUsu;
    // End of variables declaration//GEN-END:variables
}
