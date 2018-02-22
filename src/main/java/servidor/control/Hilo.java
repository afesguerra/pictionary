package servidor.control;

import servidor.modelo.Jugador;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Hilo extends Thread {

    private Jugador[] jugadores = new Jugador[3]; //Arreglo de jugadores
    private DatagramSocket sck; //Socket por donde se comunica con los clientes
    private DatagramPacket pck; //Paquete donde se guardan los datos recibidos
    private String palabra; //Palabra que se debe dibujar
    private String mensaje; //Mensaje a enviar
    private DatagramPacket pckEnviar; //Paquete que se envia a los clientes

    /**
     * Crea una instancia de la clase
     *
     * @param p       paquetes para identificar a los clientes
     * @param palabra palabra a dibujar
     */
    public Hilo(DatagramPacket[] p, String palabra) {
        this.palabra = palabra;
        for (int i = 0; i < jugadores.length; i++) {
            jugadores[i] = new Jugador(p[i]);
        }
        mensaje = jugadores[new Random().nextInt(3)].getNombre() + ";" + palabra;
        try {
            sck = new DatagramSocket();
            redireccionarMensaje();
        } catch (SocketException e) {
        }
    }

    /**
     * Se encarga de recibir mensajes de los clientes
     */
    public void run() {
        byte[] a;
        while (!isInterrupted()) {
            try {
                a = new byte[10000];
                pck = new DatagramPacket(a, a.length);
                sck.receive(pck);
                descifrar();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Se encarga de enviar los mensajes a todos los usuarios
     */
    public void redireccionarMensaje() {
        try {
            for (int i = 0; i < 3; i++) {
                pckEnviar = new DatagramPacket(mensaje.getBytes(), mensaje
                        .getBytes().length, jugadores[i].getIp(), jugadores[i]
                        .getPuerto());
                sck.send(pckEnviar);
            }
        } catch (IOException e) {
        }
    }

    /**
     * Se encarga de verificar si la palabra enviada por el cliente es la palabra correcta
     *
     * @return Si la palabra era o no correcta
     */
    public boolean verificarWord() {
        if ((mensaje.split(";")[2]).split(" ").length == 1) {
            if (mensaje.split(";")[2].equalsIgnoreCase(this.palabra)) {
                mensaje = "0; ;El jugador "
                        + mensaje.split(";")[1]
                        + " fue quien adivin� la palabra: " + palabra;
                return true;
            }
        }
        return false;
    }

    /**
     * Se encarga de interpretar los mensajes recibidos de parte de los clientes, y enviar la informacion pertinente a todos
     */
    public void descifrar() {
        mensaje = new String(pck.getData(), 0, pck.getLength());
        switch (Integer.parseInt(mensaje.split(";")[0])) {
            case 1:
                // coordenadas
                redireccionarMensaje();
                break;
            case 2:
                // palabra
                if (verificarWord()) {
                    redireccionarMensaje();
                    sck.close();
                    this.interrupt();
                }
                redireccionarMensaje();
                break;
            case 3:
                mensaje = "3;;Error de conectividad, el concursante\n" + mensaje.split(";")[1] + " se ha desconectado";
                redireccionarMensaje();
                interrupt();
                break;
            case 4:
                redireccionarMensaje();
                break;
            default:
                break;
        }
    }
}