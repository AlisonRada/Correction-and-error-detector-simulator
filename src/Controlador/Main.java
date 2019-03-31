package Controlador;

import Vista.Inicio;

/**
 *
 * @author Alison
 * @author Leonardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Inicio i = new Inicio();
        i.setVisible(true);
    }

    public static String bintoString(String bin) {
        String word = "";
        char letter = (char) Integer.parseInt(bin, 2);
        word += letter;
        return word;
    }
}
