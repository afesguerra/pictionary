package servidor.modelo;

import java.net.DatagramPacket;
import java.net.InetAddress;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Jugador {
    private InetAddress ip; //Direcci�n IP del jugador
    private int puerto; //Puerto que usa el cliente
    private String nombre; //Nombre del jugador

    /**
     * Se encarga de crear un objeto Jugador
     *
     * @param p
     */
    public Jugador(DatagramPacket p) {
        this.ip = p.getAddress();
        this.puerto = p.getPort();
        this.nombre = new String(p.getData(), 0, p.getLength());
    }

    /**
     * Retorna la IP del jugador
     *
     * @return Direcci�n IP
     */
    public InetAddress getIp() {
        return ip;
    }

    /**
     * Retorna el puerto del jugador
     *
     * @return
     */
    public int getPuerto() {
        return puerto;
    }

    /**
     * Retorna el nombre del jugador
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }
}
