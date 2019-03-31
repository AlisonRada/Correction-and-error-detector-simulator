package Modelo;

/**
 *
 * @author Alison
 * @author Leonardo
 */
public class Word {

    String dataword;
    String codeword;
    int datawordLength;
    int codewordLength;
    int extension;
    boolean correct;

    public Word(String word, boolean code) {
        if (!code) { //Verifica si le entra un codeword o un dataword
            this.dataword = word;
            this.datawordLength = word.length();
            this.codeword = DeterminateCodeword(word);
            this.codewordLength = codeword.length();
            this.correct = true;
        } else {
            this.codeword = word;
            this.dataword = word.substring(0, codeword.length() - 1);
            this.datawordLength = this.dataword.length();
            this.codewordLength = word.length();
            this.correct = ParityBit(word);
        }
    }

    public String getDataword() {
        return dataword;
    }

    public String getCodeword() {
        return codeword;
    }

    public int getDatawordLength() {
        return datawordLength;
    }

    public int getCodewordLength() {
        return codewordLength;
    }

    public boolean getCorrect() {
        return correct;
    }

    private String DeterminateCodeword(String dataword) {
        if (ParityBit(dataword)) {
            return dataword.concat("0");
        } else {
            return dataword.concat("1");
        }
    }

    /*  Me dice el bit de paridad correspondiente a un dataword
    - Si el número de 1s es par retorna falso, lo que indica que el bit de paridad es 0
    - Si el número de 1s es impar retorna verdadero, siendo el bit de paridad 1.
     */
    private boolean ParityBit(String dataword) {
        return contarUnos(dataword) % 2 != 0;
    }

    /*  Cuenta el número de unos que contiene un String  */
    private int contarUnos(String cadena) {
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
}
