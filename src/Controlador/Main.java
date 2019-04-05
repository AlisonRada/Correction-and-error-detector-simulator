package Controlador;

import Modelo.HammingCode;
import Modelo.Word;
import Vista.Inicio;
import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import static java.lang.Integer.parseInt;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 *
 * @author Alison
 * @author Leonardo
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    ArrayList<Word> code1 = new ArrayList<>();
    ArrayList<HammingCode> code2 = new ArrayList<>();

    public static void main(String[] args) {
        Inicio i = new Inicio();
        i.setVisible(true);
    }

    public static String binToString(String bin) {
        String word = "";
        int aux = 1;
        while (aux * 8 <= bin.length()) {
            char letter = (char) Integer.parseInt(bin.substring(aux * 8 - 8, aux * 8), 2);
            int value = (int) letter;
            if (value > 64 && value < 91 || value > 96 && value < 123 || value == 32
                    || value == 58 && value == 59 || value == 44 || value == 46) { //Si es un carácter válido
                word += letter;
            } else {
                word += "?";
            }

            aux++;
        }

        return word;
    }

    public static boolean ValidFile(File sourceFile, Component w) {
        if (sourceFile == null) {
            JOptionPane.showMessageDialog(w, "No ha seleccionado ningún archivo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            FileReader f1 = null;
            char character;
            int value;
            int control = 0;
            String binary, aux;
            try {
                f1 = new FileReader(sourceFile);
                BufferedReader reader = new BufferedReader(f1);
                String linea = reader.readLine(); // leemos la unica linea

                if (linea == null) {                 //Verificamos que no este vacia
                    JOptionPane.showMessageDialog(w, "Por favor verifique que el archivo contiene texto", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else if (reader.readLine() != null) { //Comprobamos que tenga solo una linea
                    JOptionPane.showMessageDialog(w, "Por favor verifique que el archivo tiene solo una linea", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                } else {
                    binary = "";
                    int j = 0;
                    while (j < linea.length()) {
                        character = linea.charAt(j);
                        value = (int) character;
                        //Si es un carácter válido
                        if (value > 64 && value < 91 || value > 96 && value < 123 || value == 32
                                || value == 58 && value == 59 || value == 44 || value == 46) {
                            aux = decimalToBinary(value);
                            while (aux.length() < 8) {
                                aux = "0" + aux;
                            }
                            binary = binary.concat(aux);
                            control++;
                        } else {
                            JOptionPane.showMessageDialog(w, "El archivo contiene caracteres inválidos", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if (control >= 16 || j + 1 >= linea.length()) {
                            control = 0;
                            binary = "";
                        }
                        j++;
                    }
                }
                reader.close();
                JOptionPane.showMessageDialog(w, "Proceso exitoso", "Archivo actualizado con éxito", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(w, "El archivo se ha movido o eliminado", "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            } catch (IOException ex) {
                System.out.println("Error inesperado");
                return false;
            } finally {
                try {
                    if (f1 != null) {
                        f1.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    /**
     * MÉTODO GENERAR PALABRAS DE CÓDIGO
     *
     * @param sourceFile Archivo cargado
     * @param tipo indica si se codifica con Bit de paridad o Hamming (0 o 1,
     * respectivamente)
     * @param w Ventana Inicio
     * @param extensionASCII nos indica la longitud de caracteres ASCII para el
     * dataword (en caso de ser escogido Hamming)
     */
    public static void GenerateCode(File sourceFile, int extensionASCII, Component w, int tipo, JTextPane textPane) {
        textPane.setText("");
        String extension;
        ArrayList<Word> palabras = new ArrayList<>();
        ArrayList<HammingCode> palabras1 = new ArrayList<>();
        if (tipo == 0) { //ParityBit
            extension = ".btp";
        } else { //HammingCode
            extension = ".ham";
        }
        FileReader f1 = null;
        char character;
        int value;
        int control = 0;
        String binary, aux;
        try {
            f1 = new FileReader(sourceFile);
            BufferedReader reader = new BufferedReader(f1);
            String linea = reader.readLine(); // leemos la unica linea
            binary = "";
            int j = 0;

            SimpleAttributeSet attrs = new SimpleAttributeSet();
            StyleConstants.setFontSize(attrs, 12);

            while (j < linea.length()) {
                character = linea.charAt(j);
                value = (int) character;
                aux = decimalToBinary(value);
                while (aux.length() < 8) {
                    aux = "0" + aux;
                }
                binary = binary.concat(aux);
                control++;
                if (tipo == 0) { //ParityBit
                    if (control >= 16 || j + 1 >= linea.length()) {
                        control = 0;
                        Word word = new Word(binary, false);
                        palabras.add(new Word(binary, false));
                        textoNegrita(attrs, textPane, word.getCodeword());
                        nuevaLinea(textPane);
                        binary = "";
                    }
                } else { //HammingCode
                    if (extensionASCII == control || j == linea.length() - 1) { //Si ya cumple la extensión de caracteres o si se encuentra en la última linea
                        control = 0;
                        HammingCode t = new HammingCode(binary,true);
                        textoNegrita(attrs, textPane, t.getCodeword());
                        nuevaLinea(textPane);
                        palabras1.add(t);
                        binary = "";
                    }
                }

                j++;
            }
            reader.close();
            try {
                String name = sourceFile.getName();
                FileWriter fw = new FileWriter("resources/" + name.substring(0, name.length() - 4) + extension);
                BufferedWriter bw = new BufferedWriter(fw);
                if (tipo == 0) {
                    for (Word codeword : palabras) {
                        String line = codeword.getCodeword();
                        bw.write(line);
                        bw.newLine();
                    }
                } else {
                    for (HammingCode codeword : palabras1) {
                        String line = codeword.getCodeword();
                        bw.write(line);
                        bw.newLine();
                    }
                }

                bw.close();
                fw.close();
                JOptionPane.showMessageDialog(w, "Archivo " + extension + " generado exitosamente", "Hecho", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(w, "Ha fallado la creacion del archivo, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(w, "El archivo se ha movido o eliminado", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (NullPointerException ex) {

        } finally {
            try {
                if (f1 != null) {
                    f1.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void DetectParityTxt(File sourceFile, Component w, JTextField namebtp) {
        File dir = new File("resources/");
        boolean correct = true;
        File[] matches = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.equals(namebtp.getText() + ".btp"));
            }
        });
        if (matches.length != 0) {
            File datawords = matches[0];
            ArrayList<Word> dataword = new ArrayList<>();
            try {
                FileReader fr = new FileReader(datawords);
                BufferedReader reader = new BufferedReader(fr);
                String linea = reader.readLine();

                do {
                    Word aux = new Word(linea, true);
                    dataword.add(aux);
                    if (aux.getCorrect()) {
                        linea = reader.readLine();
                    } else {
                        correct = false;
                    }
                } while (linea != null && correct);

                if (!correct) {
                    JOptionPane.showMessageDialog(w, "Se han detectado errores en los datos", "Archivo dañado", JOptionPane.ERROR_MESSAGE);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(w, "Error inesperado, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);

            }
            if (correct) {
                try {
                    String name = datawords.getName();
                    FileWriter fw = new FileWriter("resources/" + name.substring(0, name.length()) + ".txt");
                    BufferedWriter bw = new BufferedWriter(fw);
                    String line = "";
                    for (Word codeword : dataword) {
                        for (int i = 0; i < codeword.getDatawordLength(); i += 8) {
                            line = line.concat(binToString(codeword.getDataword().substring(i, i + 8)));
                            System.out.println();
                        }
                    }
                    bw.write(line);
                    bw.close();
                    fw.close();
                    JOptionPane.showMessageDialog(w, "Archivo .txt final generado", "Generado", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(w, "Ha fallado la creacion del archivo, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(w, "No se ha encontrado el archivo.\nPor favor verifique el nombre en la carpeta resources", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void CorrectHammingTxt(File sourceFile, Component w, String hamFile, JTextPane textPane) {
        textPane.setText("");
        File dir = new File("resources/");
        boolean correct = true;
        File[] matches = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return (name.equals(hamFile + ".ham"));
            }
        });
        if (matches.length != 0) {
            File datawords = matches[0];
            System.out.println(datawords.getAbsolutePath());
            try {
                FileReader fr = new FileReader(datawords);
                BufferedReader reader = new BufferedReader(fr);

                String name = datawords.getName();
                FileWriter fw = new FileWriter("resources/" + name.substring(0, name.length()) + ".txt");
                BufferedWriter bw = new BufferedWriter(fw);

                String line = ""; //Para escribir
                String linea = reader.readLine(); //Para leer

                boolean plural = false;
                int cont = 1;

                SimpleAttributeSet attrs = new SimpleAttributeSet();
                StyleConstants.setFontSize(attrs, 14);
                String caracter;

                do {
                    HammingCode codeword_temp = new HammingCode(linea, false);
                    if (codeword_temp.getError() != 0) { //Si hay error
                        if (codeword_temp.getError() <= linea.length()) { //Un error que puede corregir
                            System.out.println("Hubo error, pero lo puedo corregir");
                            codeword_temp.CorregirCodeword();
                            caracter = binToString(codeword_temp.getDataword());
                            int value = (int) caracter.charAt(0);
                            if (value > 64 && value < 91 || value > 96 && value < 123 || value == 32
                                    || value == 58 && value == 59 || value == 44 || value == 46) { //Si es un carácter válido
                                textoVerde(attrs, textPane, caracter);
                            } else { //No es un caracter válido
                                textoRojo(attrs, textPane, "?");
                            }
                            line = line.concat(caracter);
                        } else { //Un error que no puedo corregir
                            System.out.println("Hubo error y no puedo corregir");
                            textoRojo(attrs, textPane, "?");
                            line = line.concat("?");
                            plural = true;
                        }
                        correct = false;
                    } else {
                        System.out.println("No hubo error");
                        caracter = binToString(codeword_temp.getDataword());
                        textoNegrita(attrs, textPane, caracter);
                        line = line.concat(caracter);
                    }

                    cont++;
                    linea = reader.readLine();
//                    nuevaLinea(textPane);

                } while (linea != null);

                bw.write(line);
                bw.close();
                fw.close();

                if (!correct) {
                    JOptionPane.showMessageDialog(w, "Se han corregido errores en los datos", "Archivo corregido", JOptionPane.INFORMATION_MESSAGE);
                }
                if (plural) {
                    JOptionPane.showMessageDialog(w, "Parece que por lo menos un código tiene más de un error, se ha corregido hasta donde se ha podido. Aquellos que no se pueden se ha reemplazado por ?", "Misión fallida", JOptionPane.ERROR_MESSAGE);
                }
                JOptionPane.showMessageDialog(w, "Archivo .txt final generado", "Generado", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(w, "Error inesperado, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(w, "No se ha encontrado el archivo.\nPor favor verifique el nombre en la carpeta resources", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static File errorRandom(File sourceFile, Component w, JTextField nrand, JTextField namebtp) {
        File dir = new File("resources/");
        File[] matches = dir.listFiles((File dir1, String name) -> name.equals(namebtp.getText() + ".btp"));
        if (matches.length != 0) {
            File codewords = matches[0];
            try {
                FileReader fr = new FileReader(codewords);
                BufferedReader reader = new BufferedReader(fr);
                int n = parseInt(nrand.getText());
                int randomNum;
                ArrayList<String> modfile = new ArrayList<>();
                while (reader.readLine() != null) {
                    modfile.add(reader.readLine()); //Mueve el archivo a un array de Strings (modfile).
                }
                reader.close();
                //Aqui empieza la modificacion random
                String linea;
                int[][] changed = new int[modfile.size()][modfile.get(0).length()];
                int j = 0;
                boolean ended = true;
                while (j < modfile.size() && !ended) { //Dos while anidados para iterar entre los caracteres del archivo
                    linea = modfile.get(j);
                    int i = 0;
                    while (i < linea.length() && n > 0) {
                        randomNum = ThreadLocalRandom.current().nextInt(0, 10 + 1); //0 es el minimo y 10 el maximo
                        if (randomNum < 5 && changed[j][i] != 1) { //Como es la mitad del rango es 50% de probabilidad
                            char[] aux = linea.toCharArray();
                            aux[i] = negatebin(aux[i]);
                            linea = String.valueOf(aux);
                            changed[j][i] = 1; //La matriz changed controla si ese bit ya fue cambiado en caso de que esté volviendo a leer el archivo de nuevo
                            n--;
                        }
                        i++;
                    }
                    /*
                    Ahora verifica si se modificaron los n bits. En caso de no haber modificado los n bits
                    y de haberse acabado el archivo, se resetea j para iterar nuevamente sobre todo el
                    archivo, teniendo control sobre cual fué cambiado. En los otros dos casos solo indica
                    como terminado si n es 0, y aumenta j cuando aun no se modifican los n bits.
                     */
                    if (n > 0 && j + 1 < modfile.size()) {
                        modfile.add(j, linea);
                        j++;
                    } else if (n > 0) {
                        j = 0;
                    } else {
                        ended = true;
                    }
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(w, "Error inesperado, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(w, "No se ha encontrado el archivo.\nPor favor verifique el nombre en la carpeta resources", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    public static String decimalToBinary(int n) {
        if (n <= 1) {
            return "" + n;
        } else {
            //decimalToBinary(n/2);
            return "" + decimalToBinary(n / 2) + n % 2;
        }
    }

    private static char negatebin(char bin) {
        if (bin == '0') {
            return '1';
        } else {
            return '0';
        }
    }

    /*MANEJO DEL JTEXTPANE*/
    public static void showText(File sourceFile, JTextPane textPane) {
        if (sourceFile == null) {
            JOptionPane.showMessageDialog(textPane, "No ha seleccionado ningún archivo", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            FileReader f1 = null;
            int value;
            int control = 0;
            String binary, aux;
            try {
                f1 = new FileReader(sourceFile);
                BufferedReader reader = new BufferedReader(f1);
                String linea = reader.readLine(); // leemos la unica linea

                if (linea == null) {                 //Verificamos que no este vacia
                    JOptionPane.showMessageDialog(textPane, "Por favor verifique que el archivo contiene texto", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    binary = "";
                    int j;
                    while (linea != null) {
                        SimpleAttributeSet attrs = new SimpleAttributeSet();
                        j = 0;
                        do {
                            value = (int) linea.charAt(j);
                            if (value > 64 && value < 91 || value > 96 && value < 123 || value == 32
                                    || value == 58 && value == 59 || value == 44 || value == 46) { //Si es un carácter válido
                                textoNegrita(attrs, textPane, linea.substring(j, j + 1));
                            } else {
                                textoRojo(attrs, textPane, linea.substring(j, j + 1));
                            }
                            j++;
                        } while (j < linea.length());
                        nuevaLinea(textPane);
                        linea = reader.readLine();
                    }
                }
                reader.close();
                JOptionPane.showMessageDialog(textPane, "Proceso exitoso", "Archivo cargado con éxito", JOptionPane.INFORMATION_MESSAGE);
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(textPane, "El archivo se ha movido o eliminado", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                System.out.println("Error inesperado");
            } finally {
                try {
                    if (f1 != null) {
                        f1.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    //Método para texto en negrita
    private static void textoNegrita(SimpleAttributeSet attrs, JTextPane text, String string) {

        /*Para modificar el valor de estos atributos, nos ayuda la clase StyleConstants. 
		Esta clase tiene muchos métodos para cambiar valores a una clase SimpleAttributeSet. 
		En este caso concreto hemos usado setBold() para ponerlo en negrita.
         */
        StyleConstants.setBold(attrs, true);
        StyleConstants.setForeground(attrs, Color.BLACK);

        /*Obtenemos el StyledDocument, que es lo que el JTextPane tiene dentro y 
		representa al texto que estamos viendo.
		El StyledDocument tiene un método insert() que admite tres parámetros:
			- Posición en la que se quiere insetar el texto dentro del documento.
			- El texto
			- Los atributos del texto.
		Como queremos insertar al final, la posición es justo la longitud del texto,
		esto se obtiene con el método getLength().
         */
        try {
            text.getStyledDocument().insertString(
                    text.getStyledDocument().getLength(), string, attrs);
        } catch (BadLocationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void textoRojo(SimpleAttributeSet attrs, JTextPane text, String string) {
        StyleConstants.setForeground(attrs, Color.red);
        try {
            text.getStyledDocument().insertString(
                    text.getStyledDocument().getLength(), string, attrs);
        } catch (BadLocationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void textoVerde(SimpleAttributeSet attrs, JTextPane textPane, String string) {
        StyleConstants.setForeground(attrs, Color.GREEN);
        try {
            textPane.getStyledDocument().insertString(
                    textPane.getStyledDocument().getLength(), string, attrs);
        } catch (BadLocationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void nuevaLinea(JTextPane text) {
        try {
            text.getStyledDocument().insertString(
                    text.getStyledDocument().getLength(),
                    System.getProperty("line.separator"), null);
        } catch (BadLocationException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
