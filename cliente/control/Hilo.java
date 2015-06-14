package cliente.control;

import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import cliente.vista.*;

/**
 * @author Andrés Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Hilo extends Thread {
	private VentanaJuego window; // Ventana en la cual se juega
	private DatagramPacket pck; // Paquete en el cual se guarda la información
	// recibida
	private DatagramSocket sck; // Socket por el cual se recibe información

	/**
	 * Crea una instancia de la clase
	 * 
	 * @param window
	 *            Ventana en la cual se juega
	 * @param pck
	 *            Paquete en el cual se guarda la información recibida
	 * @param sck
	 *            Socket por el cual se recibe información
	 */
	public Hilo(VentanaJuego window, DatagramPacket pck, DatagramSocket sck) {
		this.window = window;
		this.pck = pck;
		this.sck = sck;
	}

	/**
	 * Se encarga de recibir constantemente información del servidor y
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
	 * Se encarga de interpretar la información recibida del servidor y realizar
	 * la accion correspondiente
	 */
	public void desifrarMensaje() {
		String[] msj = (new String(pck.getData(), 0, pck.getLength()))
				.split(";");
		switch (Integer.parseInt(msj[0])) {
		case 0:
			// Alguien ganó
			JOptionPane.showMessageDialog(window, msj[2], "¡¡GANADOR!!",
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
			JOptionPane.showMessageDialog(window, msj[2], "Error de conexión",
					JOptionPane.ERROR_MESSAGE);
			System.exit(0);
			break;
		case 4:
			window.cambiarColor(new Color(Integer.parseInt(msj[1]),Integer.parseInt(msj[2]),Integer.parseInt(msj[3])));
			break;
		default:
			break;
		}
	}
}