/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Invitado_PC16
 */
public class conexionViaje {
    Connection connect = null; 
    //crear metodo para conectar
    public Connection conectar(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection("jdbc:mysql://localhost/viajes","root",""); //toda conexionViaje tine 3 parametros (base de datos, usuario, password)
            //JOptionPane.showMessageDialog(null, "Listo ok");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "ERROR EN LA CONEXION VUELVA A INTENTARLO MAS TARDE"+ ex);
        }
        return connect;
    }
    
}
