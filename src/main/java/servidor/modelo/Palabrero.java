package servidor.modelo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Palabrero {
    private ArrayList<String> palabras = new ArrayList<String>(); //Lista de palabras

    /**
     * Crea una instancia de la clase
     */
    public Palabrero() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("lista.txt"));
            String palabra = "";
            do {
                palabra = reader.readLine();
                if (palabra != null) {
                    palabras.add(palabra);
                }
            } while (palabra != null);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    /**
     * Escoge una palabra al azar y la retorna
     *
     * @return Palabra a dibujar
     */
    public String obtenerPalabra() {
        return palabras.get(new Random().nextInt(palabras.size()));
    }
}
