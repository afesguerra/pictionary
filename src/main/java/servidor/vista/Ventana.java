package servidor.vista;

import javax.swing.*;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Ventana extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel label; //Etiqueta que muestra la direcci�n IP del equipo local

    /**
     * Crea una ventana que muestra la direcci�n IP del equipo local
     *
     * @param ip Direcci�n IP del equipo local
     */
    public Ventana(String ip) {
        setSize(150, 50);
        setResizable(false);
        setLocationRelativeTo(null);
        setTitle("Mi IP");
        label = new JLabel(ip);
        add(label);
        setVisible(true);
    }
}