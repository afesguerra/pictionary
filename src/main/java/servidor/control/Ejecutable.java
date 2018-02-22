package servidor.control;

import servidor.modelo.Palabrero;
import servidor.vista.Ventana;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Ejecutable {
    static Palabrero palabras = new Palabrero(); // Objeto palabrero con la
    // lista de palabras

    /**
     * Se encarga de recibir jugadores con nombres diferentes e iniciar el juego
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            Ventana w = new Ventana(InetAddress.getLocalHost().getHostAddress());
            w.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            DatagramSocket sck = new DatagramSocket(1337);
            String error = "error;Lo sentimos, el nombre elejido ya est� en uso, hasta pronto";
            while (true) {
                byte[][] a = new byte[3][99999];
                DatagramPacket[] pck = new DatagramPacket[3];
                String[] nombres = new String[3];
                for (int i = 0; i < 3; i++) {
                    nombres[i] = "." + i + ".";
                    pck[i] = new DatagramPacket(a[i], a[i].length);
                }
                for (int i = 0; i < 3; i++) {
                    sck.receive(pck[i]);
                    nombres[i] = new String(pck[i].getData(), 0, pck[i]
                            .getLength());
                    if ((nombres[0].equalsIgnoreCase(nombres[1]))
                            || (nombres[0].equalsIgnoreCase(nombres[2]))
                            || (nombres[1].equalsIgnoreCase(nombres[2]))) {

                        pck[i] = new DatagramPacket(error.getBytes(), error
                                .getBytes().length, pck[i].getAddress(), pck[i]
                                .getPort());
                        sck.send(pck[i]);
                        i--;
                    }
                }
                new Hilo(pck, palabras.obtenerPalabra()).start();
            }
        } catch (SocketException e) {
//			System.exit(0);
        } catch (IOException e) {
//			System.exit(0);
        }

    }

}
