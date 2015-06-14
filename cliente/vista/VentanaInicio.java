package cliente.vista;

import java.awt.*;
import javax.swing.*;

/**
 * @author Andrés Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class VentanaInicio extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField ipServer; // Campo donde se ingresa la dirección IP del
									// servidor
	private JTextField nomUser; // Campo donde se ingresa el nombre de usuario
								// deseado
	private JButton boton; // Botón para enviar la información al servidor

	/**
	 * Se encarga de crear una ventana donde el usuario puede registrarse para
	 * jugar
	 */
	public VentanaInicio() {
		setTitle("Sketching");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridLayout(3, 1));
		ipServer = new JTextField();
		ipServer.setBorder(BorderFactory
				.createTitledBorder("Ingrese la IP del servidor"));
		nomUser = new JTextField();
		nomUser.setBorder(BorderFactory
				.createTitledBorder("Ingrese su nombre de usuario"));
		boton = new JButton("Enviar");
		add(ipServer);
		add(nomUser);
		add(boton);
	}

	/**
	 * Retorna el campo de texto donde se escribe la IP del servidor
	 * 
	 * @return Objeto JTextField
	 */
	public JTextField getIpServer() {
		return ipServer;
	}

	/**
	 * Retorna el campo de texto donde se escribe el nombre de usuario deseado
	 * @return
	 */
	public JTextField getNomUser() {
		return nomUser;
	}

	/**
	 * Se encarga de retornar el botón de enviar
	 * @return
	 */
	public JButton getBoton() {
		return boton;
	}
}