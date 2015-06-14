package servidor.vista;
import javax.swing.*;
/**
 * @author Andrés Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Ventana extends JFrame{
	private static final long serialVersionUID = 1L;
	private JLabel label; //Etiqueta que muestra la dirección IP del equipo local
	/**
	 * Crea una ventana que muestra la dirección IP del equipo local
	 * @param ip Dirección IP del equipo local
	 */
	public Ventana(String ip){
		setSize(150, 50);
		setResizable(false);
		setLocationRelativeTo(null);
		setTitle("Mi IP");
		label=new JLabel(ip);
		add(label);
		setVisible(true);
	}
}