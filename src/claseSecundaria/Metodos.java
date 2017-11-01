/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package claseSecundaria;

import java.awt.event.KeyEvent;
import javax.swing.JTextField;

/**
 *
 * @author PC
 */
public class Metodos {

    public static void validarNumeros(KeyEvent evt, JTextField texto) {
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            evt.consume();
        }
        if (texto.getText().length() >= 10) {
            evt.consume();
        }
    }

    public static void validarLetras(KeyEvent evt, JTextField texto) {
        // TODO add your handling code here:
        char c = evt.getKeyChar();
        if (Character.isDigit(c)) {
            evt.consume();
        }
        if (texto.getText().length() >= 10) {
            evt.consume();
        }
    }
}
