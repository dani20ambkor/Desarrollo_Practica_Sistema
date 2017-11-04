/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

/**
 *
 * @author PC
 */
public class Prueba {

    public static void main(String[] args) {
        String palabra;

        palabra = "Holaadios";
        System.out.println(palabra);
        Prueba p = new Prueba();
        System.out.println(p.encriptarClave(palabra));
        System.out.println(p.desencriptarClave(p.encriptarClave(palabra)));

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

    public String desencriptarClave(String texto) {
        String tabla = "abcdefghijklmnñopqrstuvwxyzáéíóúABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚ1234567890.,;_:+-*/ @$#¿?!Â¡=()[]{}\\\"";
        int clave = 6;

        String resultado = "";

        for (int i = 0; i < texto.length(); i++) {

            int pos = tabla.indexOf(texto.charAt(i));
            if ((pos - clave) < 0) {
                resultado += tabla.charAt((pos - clave) + tabla.length());
            } else {
                resultado += tabla.charAt(pos - clave);
            }

        }

        return resultado;
    }
}