
package Modelo;

/**
 *
 * @author Alison
 * @author Leonardo
 */
public class HammingCode {

    int ParityBits;
    int extension;
    String codeword;
    String dataword;
//    boolean correct;
    int error;

    public HammingCode(String word, int extension, boolean code) {
        if (code) { //Si es necesario codificar para hallar el codigo Hamming
            this.codeword = CodificarHamming(word, extension);
            this.dataword = word;
            this.extension = extension;
//            this.correct = true;
            this.error = 0;
        } else { //Si ya me dan el codigo Hamming, pero debo corregir
            this.codeword = word;
            this.dataword = getDataword(word);
//            this.correct = VerificarHamming(word);
            this.error = getDecimal(SindromeHamming(word));
        }

    }
    
     private String CodificarHamming(String dataword, int extension) {
        String b1, b2, b4, b8;
        if (extension == 1) {
            String t = "" + dataword.charAt(7) + dataword.charAt(6) + dataword.charAt(4) + dataword.charAt(3) + dataword.charAt(1);
            b1 = XOR(t);
            t = "" + dataword.charAt(7) + dataword.charAt(5) + dataword.charAt(4) + dataword.charAt(2) + dataword.charAt(1);
            b2 = XOR(t);
            t = "" + dataword.charAt(6) + dataword.charAt(5) + dataword.charAt(4) + dataword.charAt(0);
            b4 = XOR(t);
            t = "" + dataword.charAt(3) + dataword.charAt(2) + dataword.charAt(1) + dataword.charAt(0);
            b8 = XOR(t);
            return dataword.substring(0, 4).concat(b8).concat(dataword.substring(4, 7)).concat(b4).concat(dataword.substring(7)).concat(b2).concat(b1);
        } else {
            String t = "" + dataword.charAt(15) + dataword.charAt(14) + dataword.charAt(12) + dataword.charAt(11) + dataword.charAt(9) + dataword.charAt(7) + dataword.charAt(5) + dataword.charAt(4) + dataword.charAt(2) + dataword.charAt(0);
//            System.out.println("t1: " + t);
            b1 = XOR(t);
            t = "" + dataword.charAt(15) + dataword.charAt(13) + dataword.charAt(12) + dataword.charAt(10) + dataword.charAt(9) + dataword.charAt(6) + dataword.charAt(5) + dataword.charAt(3) + dataword.charAt(2);
//            System.out.println("t2: " + t);
            b2 = XOR(t);
            t = "" + dataword.charAt(14) + dataword.charAt(13) + dataword.charAt(12) + dataword.charAt(8) + dataword.charAt(7) + dataword.charAt(6) + dataword.charAt(5) + dataword.charAt(1) + dataword.charAt(0);
//            System.out.println("t4: " + t);
            b4 = XOR(t);
            t = "" + dataword.charAt(11) + dataword.charAt(10) + dataword.charAt(9) + dataword.charAt(8) + dataword.charAt(7) + dataword.charAt(6) + dataword.charAt(5);
//            System.out.println("t8: " + t);
            b8 = XOR(t);
            t = "" + dataword.charAt(4) + dataword.charAt(3) + dataword.charAt(2) + dataword.charAt(1) + dataword.charAt(0);
//            System.out.println("t16: " + t);
            String b16 = XOR(t);
            System.out.println("ParityBits" + b16 + b8 + b4 + b2 + b1);
            return dataword.substring(0, 5).concat(b16).concat(dataword.substring(5, 12)).concat(b8).concat(dataword.substring(12, 15)).concat(b4).concat(dataword.substring(15)).concat(b2).concat(b1);
        }
    }
    
    private boolean VerificarHamming(String codeword){
        if (getDecimal(SindromeHamming(codeword))==0) {
            return true;
        } else{
            return false;
        }
    }

    private String SindromeHamming(String codeword) {
        String c1, c2, c4, c8, temp;
        if (this.extension == 1) {
            temp = "" +codeword.charAt(11)+codeword.charAt(9) + codeword.charAt(7) + codeword.charAt(5) + codeword.charAt(3) + codeword.charAt(1);
            c1 = XOR(temp);
            temp = "" + codeword.charAt(10) + codeword.charAt(9) + codeword.charAt(6) + codeword.charAt(5) + codeword.charAt(2) + codeword.charAt(1);
            c2 = XOR(temp);
            temp = "" + codeword.charAt(8) + codeword.charAt(7) + codeword.charAt(6) + codeword.charAt(5) + codeword.charAt(0);
            c4 = XOR(temp);
            temp = "" + codeword.charAt(4) + codeword.charAt(3) + codeword.charAt(2) + codeword.charAt(1) + codeword.charAt(0);
            c8 = XOR(temp);
            return c8+c4+c2+c1;
        } else {
            temp = "" + codeword.charAt(20) + codeword.charAt(18) + codeword.charAt(16) + codeword.charAt(14) + codeword.charAt(12) + codeword.charAt(10) + codeword.charAt(8) + codeword.charAt(6) + codeword.charAt(4) + codeword.charAt(2) + codeword.charAt(0);
            c1 = XOR(temp);
            temp = "" + codeword.charAt(19) + codeword.charAt(18) + codeword.charAt(15) + codeword.charAt(14) + codeword.charAt(11) + codeword.charAt(10) + codeword.charAt(7) + codeword.charAt(6) + codeword.charAt(3) + codeword.charAt(2);
            c2 = XOR(temp);
            temp = "" + codeword.charAt(17) + codeword.charAt(16) + codeword.charAt(15) + codeword.charAt(14) + codeword.charAt(9) + codeword.charAt(8) + codeword.charAt(7) + codeword.charAt(6) + codeword.charAt(1) + codeword.charAt(0);
            c4= XOR(temp);
            temp = "" + codeword.charAt(13) + codeword.charAt(12) + codeword.charAt(11) + codeword.charAt(10) + codeword.charAt(9) + codeword.charAt(8) + codeword.charAt(7) + codeword.charAt(6);
            c8 = XOR(temp);
            temp = "" + codeword.charAt(5) + codeword.charAt(4) + codeword.charAt(3) + codeword.charAt(2) + codeword.charAt(1) + codeword.charAt(0);
            String c16 = XOR(temp);
            return c16+c8+c4+c2+c1;
        }
    }
    
    private int getDecimal(String binario){
        return Integer.parseInt(binario, 2);
    }

    private static String reverse(String palabra) {
        if (palabra.length() == 1) {
            return palabra;
        } else {
            return reverse(palabra.substring(1)) + palabra.charAt(0);
        }
    }

    private static String XOR(String dataword) {
        if (contarUnos(dataword) % 2 == 0) {
            return "0";
        } else {
            return "1";
        }
    }

    /*  Cuenta el número de unos que contiene un String  */
    private static int contarUnos(String cadena) {
        char caracter = '1';
        int posicion, contador = 0;
        //se busca la primera vez que aparece
        posicion = cadena.indexOf(caracter);
        while (posicion != -1) { //mientras se encuentre el caracter
            contador++;           //se cuenta
            //se sigue buscando a partir de la posición siguiente a la encontrada
            posicion = cadena.indexOf(caracter, posicion + 1);
        }
        return contador;
    }

    public static String XOR(char a, char b) {
        if (a == b) {
            return "0";
        } else {
            return "1";
        }
    }
    
    /* Corrige el bit dañado */
    private String CorregirCodeword(){
        int bit = this.error;
        String wrong = this.codeword;
        String correctBit;
        int posicion = wrong.length()-bit;
        if (wrong.substring(posicion, posicion+1).compareTo("0")==0) {
            correctBit = "1";
        } else{
            correctBit = "0";
        }
        return wrong.substring(0,posicion)+correctBit+wrong.substring(posicion+1);
    }

    /* Obtiene el dataword del codeword */
    private String getDataword(String codeword) {
        if (this.extension==1) {
            return codeword.substring(0,4).concat(codeword.substring(5,8).concat(codeword.substring(9)));
        } else{
            return codeword.substring(0,5)+codeword.substring(6,13)+codeword.substring(14,17)+codeword.substring(18);
        }
    }

}
