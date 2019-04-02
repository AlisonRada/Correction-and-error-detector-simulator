package Controlador;

import Modelo.HammingCode;
import Modelo.Word;
import Vista.Inicio;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;

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

    public static String bintoString(String bin) {
        String word = "";
        char letter = (char) Integer.parseInt(bin, 2);
        word += letter;
        return word;
    }

    /*Aún no decido si esto va a estar aquí y qué parte hará exactamente*/
    public void CreateNose(String nombre, String Path) {
        File Archivo;
        FileWriter w;
        BufferedWriter bw;
        PrintWriter wr;
        nombre = nombre.substring(0, nombre.length() - 4);
        Archivo = new File(Path + ".ham"); // Crea el archivo en la direccion dada con el nombre escogido
        if (Archivo.exists()) {
            Archivo.delete();
            Archivo = new File(Path + ".ham");
        }
        try {
            if (Archivo.createNewFile()) { // Verifica que el archivo se haya creado exitosamente
                w = new FileWriter(Archivo); // Se prepara para escribir en el archivo
                bw = new BufferedWriter(w);
                wr = new PrintWriter(bw);
                String dato;
                for (int i = 0; i < code1.size(); i++) {
                    dato = code1.get(i).toString();
                    wr.write(dato);
                    wr.write("\r\n");
                }
                wr.close();
                bw.close();
                JOptionPane.showMessageDialog(null, "Se ha creado exitosamente el archivo .ham");
            }
        } catch (IOException e) {
            System.out.println("Ha habido un error creando el Archivo");
        }
    }

    /*Esto tampoco*/
    public static void Lectura(File f, int tipo) {
        String line, text = "";
        try {
            FileReader fr = new FileReader(f.getAbsolutePath());
            BufferedReader br = new BufferedReader(fr);
            line = br.readLine();            
            br.close();
            fr.close();
        } catch (IOException e) {
            System.out.println("Error en GetInfo");
        }
    }
}
