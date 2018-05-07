package cliente.vista;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;

@Getter
@Slf4j
public abstract class VentanaJuego extends JFrame {
    private static final long serialVersionUID = 1L;

    private final String username;
    private final JPanel panelDibujo;
    private final JTextArea palabras;
    private final JButton boton;
    private final JTextField texto;

    public VentanaJuego(String username) {
        this.username = username;

        setTitle("Sketching: " + username);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridLayout(1, 2));

        this.palabras = new JTextArea("");
        palabras.setEditable(false);
        palabras.setBackground(this.getBackground());
        palabras.setText("");
        JScrollPane scroll = new JScrollPane(palabras);
        scroll.setBorder(BorderFactory.createTitledBorder("Pistas"));
        scroll.setPreferredSize(new Dimension(350, 400));

        this.texto = new JTextField();
        texto.setPreferredSize(new Dimension(350, 40));
        texto.setBorder(BorderFactory.createTitledBorder("Ingrese la palabra"));
        texto.setBackground(this.getBackground());
        texto.setFocusable(true);

        this.boton = new JButton("Enviar");

        JPanel panelExtra = new JPanel();
        panelExtra.setLayout(new FlowLayout());
        panelExtra.add(scroll);
        panelExtra.add(texto);
        panelExtra.add(boton);
        panelExtra.setBorder(BorderFactory.createTitledBorder("Palabras"));
        add(panelExtra);

        this.panelDibujo = new JPanel();
        panelDibujo.setBorder(BorderFactory.createTitledBorder("Dibujo"));
        add(panelDibujo);
    }

    public final void dibujar(Point p, int rgb) {
        Color color = new Color(rgb);
        panelDibujo.getGraphics().setColor(color);
        panelDibujo.getGraphics().fillOval(p.x - 5, p.y - 5, 10, 10);
    }

    public final void addWordGuess(String a) {
        palabras.append(a + System.lineSeparator());
    }
}