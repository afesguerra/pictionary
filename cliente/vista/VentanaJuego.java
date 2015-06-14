package cliente.vista;

import java.awt.*;

import javax.swing.*;

/**
 * @author Andrés Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class VentanaJuego extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel panelDibujo; // Panel en el cual se dibuja
	private JPanel panelExtra; // Panel donde aparecen todas las palabras
	private JTextField texto; // Campo donde se escriben palabras a adivinar
	private JTextArea palabras; // Campo donde aparecen las palabras enviadas
	private JButton boton; // Boton que permite enviar los datos
	private JScrollPane scroll; // Panel deslizable para visualizar todas las
								// palabras
	private Color color; // Color para dibujar
	private Graphics g; // Objeto Graphics

	/**
	 * Crea un objeto de la clase VentanaJuego
	 */
	public VentanaJuego() {
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(new GridLayout(1, 2));
		panelDibujo = new JPanel();
		panelExtra = new JPanel();
		boton = new JButton("Enviar");
		panelExtra.setLayout(new FlowLayout());
		palabras = new JTextArea("");
		palabras.setEditable(false);
		palabras.setBackground(this.getBackground());
		texto = new JTextField();
		texto.setPreferredSize(new Dimension(350, 40));
		texto.setBorder(BorderFactory.createTitledBorder("Ingrese la palabra"));
		texto.setBackground(this.getBackground());
		scroll = new JScrollPane(palabras);
		scroll.setBorder(BorderFactory.createTitledBorder("Pistas"));
		scroll.setPreferredSize(new Dimension(350, 400));
		panelExtra.add(scroll);
		panelExtra.add(texto);
		panelExtra.add(boton);
		panelDibujo.setBorder(BorderFactory.createTitledBorder("Dibujo"));
		panelExtra.setBorder(BorderFactory.createTitledBorder("Palabras"));
		texto.setFocusable(true);
		add(panelExtra);
		add(panelDibujo);
	}

	public void cambiarColor(Color c) {
		color = c;
		g=panelDibujo.getGraphics();
		g.setColor(color);
		g.fillRect(10, 20, 20, 20);
	}

	/**
	 * Dibuja en el panel de dibujo las coordenadas recibidas
	 * 
	 * @param x
	 *            Coordenada del eje X
	 * @param y
	 *            Coordenada del eje Y
	 */
	public void dibujar(int x, int y) {
		if (color != null) {
			g.fillOval(x-5, y-5, 10, 10);
		}else{
			panelDibujo.getGraphics().fillOval(x, y, 10, 10);
		}
	}

	/**
	 * Agrega la palabra recibida a la lista de palabras que han enviado todos
	 * los usuarios
	 * 
	 * @param a
	 *            palabra a agregar
	 */
	public void escribir(String a) {
		if (palabras.getText().equals(""))
			palabras.setText(a);
		else
			palabras.setText(palabras.getText() + "\n" + a);
	}

	/**
	 * Retorna el panel donde aparece el dibujo
	 * 
	 * @return
	 */
	public JPanel getPanelDibujo() {
		return panelDibujo;
	}

	/**
	 * Retorna el campo de texto donde se escriben palabras referentes al dibujo
	 * 
	 * @return
	 */
	public JTextField getTexto() {
		return texto;
	}

	/**
	 * Retorna el campo donde aparecen las palabras enviadas
	 * 
	 * @return
	 */
	public JTextArea getPalabras() {
		return palabras;
	}

	/**
	 * Retorna el botón que envia la información al servidor
	 * 
	 * @return
	 */
	public JButton getBoton() {
		return boton;
	}
}