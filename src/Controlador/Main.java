
package Controlador;

import Vista.Inicio;

/**
 *
 * @author Alison
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       Inicio h = new Inicio();
       h.setVisible(true);
    }
    
    public static String bintoString (String bin){
        String word = "";   
        char letter= (char)Integer.parseInt(bin, 2);
        word += letter;
        return word;
    }
}
