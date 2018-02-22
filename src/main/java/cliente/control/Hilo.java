package cliente.control;

import cliente.vista.VentanaJuego;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Hilo extends Thread {
    private VentanaJuego window; // Ventana en la cual se juega
    private DatagramPacket pck; // Paquete en el cual se guarda la informaci�n
    // recibida
    private DatagramSocket sck; // Socket por el cual se recibe informaci�n

    /**
     * Crea una instancia de la clase
     *
     * @param window Ventana en la cual se juega
     * @param pck    Paquete en el cual se guarda la informaci�n recibida
     * @param sck    Socket por el cual se recibe informaci�n
     */
    public Hilo(VentanaJuego window, DatagramPacket pck, DatagramSocket sck) {
        this.window = window;
        this.pck = pck;
        this.sck = sck;
    }

    /**
     * Se encarga de recibir constantemente informaci�n del servidor y
     * descifrarla
     */
    public void run() {
        byte[] a;
        while (!isInterrupted()) {
            try {
                a = new byte[10000];
                pck = new DatagramPacket(a, a.length);
                sck.receive(pck);
                desifrarMensaje();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Se encarga de interpretar la informaci�n recibida del servidor y realizar
     * la accion correspondiente
     */
    public void desifrarMensaje() {
        String[] msj = (new String(pck.getData(), 0, pck.getLength()))
                .split(";");
        switch (Integer.parseInt(msj[0])) {
            case 0:
                // Alguien gan�
                JOptionPane.showMessageDialog(window, msj[2], "��GANADOR!!",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                break;
            case 1:
                // coordenadas
                int x = Integer.parseInt(msj[1]);
                int y = Integer.parseInt(msj[2]);
                window.dibujar(x, y);
                break;
            case 2:
                // palabra+
                window.escribir((msj[1] + ": " + msj[2]));
                break;
            case 3:
                // alguien se desconecto
                JOptionPane.showMessageDialog(window, msj[2], "Error de conexi�n",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
            case 4:
                window.cambiarColor(new Color(Integer.parseInt(msj[1]), Integer.parseInt(msj[2]), Integer.parseInt(msj[3])));
                break;
            default:
                break;
        }
    }
}