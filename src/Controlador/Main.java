package Controlador;

import Modelo.HammingCode;
import Modelo.Word;
import Vista.Inicio;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

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
    
    public static boolean ValidFile(File sourceFile, Component w){
        if (sourceFile==null) {
            JOptionPane.showMessageDialog(w, "No ha seleccionado ningún archivo", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else{
            FileReader f1 = null;
            char character;
            int value;
            int control = 0;
            String binary, aux;
            try {
                f1 = new FileReader(sourceFile);
                BufferedReader reader = new BufferedReader(f1);
                String linea = reader.readLine(); // leemos la unica linea

                if(linea==null){                 //Verificamos que no este vacia
                    JOptionPane.showMessageDialog(w, "Por favor verifique que el archivo contiene texto","Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }else if (reader.readLine()!=null){ //Comprobamos que tenga solo una linea
                    JOptionPane.showMessageDialog(w, "Por favor verifique que el archivo tiene solo una linea", "Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }else{
                    binary = "";
                    int j = 0;
                    while (j < linea.length() ) {
                        character = linea.charAt(j);
                        value = (int)character;
                        //Si es un carácter válido
                        if (value > 64 && value<91 || value > 96 && value < 123 ||
                            value == 58 && value == 59 || value ==44 || value == 46) {
                            aux=decimalToBinary(value);
                            while(aux.length()<8) aux="0"+aux;
                            binary = binary.concat(aux);
                            control++;
                        } else{
                            JOptionPane.showMessageDialog(w, "El archivo contiene caracteres invalidos", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        if(control>=16 || j+1>=linea.length()){
                            control=0;
                            binary="";
                        }
                        j++;
                    }
                }
                reader.close();
                JOptionPane.showMessageDialog(w, "Proceso exitoso", "Archivo actualizado con éxito", JOptionPane.INFORMATION_MESSAGE);
                return true;
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(w, "El archivo se ha movido o eliminado","Error", JOptionPane.ERROR_MESSAGE);
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
    
    public static void GenerateParitytxt(File sourceFile, Component w){
        ArrayList<Word> palabras = new ArrayList<>();
            FileReader f1 = null; //Bandera para carácteres válidos
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
                while (j < linea.length()) {
                    character = linea.charAt(j);
                    value = (int)character;
                    aux=decimalToBinary(value);
                    while(aux.length()<8) aux="0"+aux;
                    binary = binary.concat(aux);
                    control++;
                    if(control>=16 || j+1>=linea.length()){
                        control=0;
                        palabras.add(new Word(binary, false));
                        binary="";
                    }
                    j++;
                }
                reader.close();
                try{
                    String name = sourceFile.getName();
                    FileWriter fw = new FileWriter("resources/"+name.substring(0, name.length()-4)+".btp");
                    BufferedWriter bw = new BufferedWriter(fw);
                    for(Word codeword : palabras){
                        String line = codeword.getCodeword();
                        bw.write(line);
                        bw.newLine();
                    }
                    bw.close();
                    fw.close();
                    JOptionPane.showMessageDialog(w, "Archivo .btp generado exitosamente", "Hecho", JOptionPane.INFORMATION_MESSAGE);
                } catch(IOException e) {
                    JOptionPane.showMessageDialog(w, "Ha fallado la creacion del archivo, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (FileNotFoundException ex) {
                JOptionPane.showMessageDialog(w, "El archivo se ha movido o eliminado","Error", JOptionPane.ERROR_MESSAGE);
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
    
    public static void DetectParitytxt(File sourceFile, Component w, JTextField namebtp){
        File dir = new File("resources/");
            boolean correct = true;
            File[] matches = dir.listFiles(new FilenameFilter()
            {
              @Override
              public boolean accept(File dir, String name)
              {
                 return (name.equals(namebtp.getText()+".btp"));
              }
            });
            if(matches.length!=0){
                File datawords = matches[0];
                ArrayList<Word> dataword = new ArrayList<>();
                try {
                    FileReader fr = new FileReader(datawords);
                    BufferedReader reader = new BufferedReader(fr);
                    String linea = reader.readLine();

                    do {
                        Word aux = new Word(linea, true);
                        dataword.add(aux);
                        if(aux.getCorrect())linea = reader. readLine();
                        else correct = false;
                    } while (linea!=null && correct);

                    if(!correct) JOptionPane.showMessageDialog(w, "Se han detectado errores en los datos", "Archivo dañado", JOptionPane.ERROR_MESSAGE);

                } catch (Exception e) {
                    JOptionPane.showMessageDialog(w, "Error inesperado, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
                    
                }
                if(correct){
                    try{
                        String name = datawords.getName();
                        FileWriter fw = new FileWriter("resources/"+name.substring(0, name.length())+".txt");
                        BufferedWriter bw = new BufferedWriter(fw);
                        String line = "";
                        for(Word codeword : dataword){
                            for (int i = 0; i < codeword.getDatawordLength(); i+=8) {
                                line = line.concat(bintoString(codeword.getDataword().substring(i, i+8)));
                                System.out.println();
                            }
                        }
                        bw.write(line);
                        bw.close();
                        fw.close();
                        JOptionPane.showMessageDialog(w, "Archivo .txt final generado", "Generado", JOptionPane.INFORMATION_MESSAGE);
                    } catch(IOException e) {
                        JOptionPane.showMessageDialog(w, "Ha fallado la creacion del archivo, intente nuevamente", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }else{
                JOptionPane.showMessageDialog(w, "No se ha encontrado el archivo.\nPor favor verifique el nombre en la carpeta resources", "Error", JOptionPane.ERROR_MESSAGE);
            }
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
    public static String decimalToBinary(int n){
        if (n<=1) {
            return ""+n;
        } else{
            //decimalToBinary(n/2);
            return ""+decimalToBinary(n/2)+n%2;
        }
    }
}
