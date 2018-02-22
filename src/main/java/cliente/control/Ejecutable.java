package cliente.control;

import cliente.vista.VentanaInicio;
import cliente.vista.VentanaJuego;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
public class Ejecutable {
    private static VentanaInicio ventana = new VentanaInicio(); // Ventana donde
    // el usuario se
    // registra
    private static VentanaJuego window = new VentanaJuego(); // Ventana donde
    // juega el
    // cliente
    private static DatagramPacket pck; // Paquete que se usa para enviar al
    // servidor
    private static DatagramPacket p; // Paquete donde se recibe la informaci�n
    // del servidor
    private static DatagramSocket sck; // Socket por donde se envia y recibe
    // informaci�n
    private static String nombre; // Nombre del cliente
    private static InetAddress ip; // Direcci�n IP del servidor
    private static int puerto; // Puerto por el cual el servidor escucha

    /**
     * Se encarga de encarga de crear conexion con el servidor y registrar el
     * usuario para que �ste est� listo para jugar
     *
     * @param args Nadie sabe XD
     */
    public static void main(String[] args) {
        try {
            sck = new DatagramSocket();
            ventana.getBoton().addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    ventana.setCursor(new Cursor(3));
                    try {
                        if (!ventana.getNomUser().getText().equals("")
                                && !ventana.getIpServer().getText().equals("")) {
                            byte[] a = new byte[10000];
                            p = new DatagramPacket(a, a.length);
                            String palabra = ventana.getNomUser().getText();
                            nombre = palabra.replaceAll(";", " ");
                            pck = new DatagramPacket(nombre.getBytes(), nombre
                                    .getBytes().length,
                                    InetAddress.getByName(ventana.getIpServer()
                                            .getText()), 1337);
                            sck.send(pck);
                            sck.receive(p);
                            String[] m = (new String(p.getData(), 0, p
                                    .getLength())).split(";");
                            if (m[0].equals("error")) {
                                JOptionPane.showMessageDialog(ventana, m[1],
                                        "ERROR, nombre en uso",
                                        JOptionPane.CLOSED_OPTION);
                                System.exit(0);
                            } else {
                                ip = p.getAddress();
                                puerto = p.getPort();
                                ventana();
                                ventana.setVisible(false);
                                if (m[0].equals(nombre)) {
                                    dibujante();
                                    JOptionPane.showMessageDialog(window,
                                            "T� eres el dibujante, la palabra a dibujar es: "
                                                    + m[1],
                                            "Tu eres el dibujante",
                                            JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    escritor();
                                    JOptionPane.showMessageDialog(window,
                                            "El jugador " + m[0]
                                                    + " es el dibujante",
                                            "A jugar",
                                            JOptionPane.INFORMATION_MESSAGE);
                                }
                                new Hilo(window, p, sck).start();
                                window.setTitle("Sketching: " + nombre);
                                window.setVisible(true);
                            }
                        } else {
                            JOptionPane.showMessageDialog(window,
                                    "Datos incorrectos\nAplicaci�n Finalizada",
                                    "Error", JOptionPane.INFORMATION_MESSAGE);
                            System.exit(0);
                        }
                    } catch (UnknownHostException e1) {
                    } catch (IOException e1) {
                    }
                }
            });
            ventana.setVisible(true);
        } catch (SocketException e) {
        }
    }

    /**
     * Se encarga de enviar al servidor las coordenadas y color donde el dibujante hizo
     * clic sostenido
     */
    public static void dibujante() {
        window.getPanelDibujo().setCursor(new Cursor(1));
        window.getPanelDibujo().addMouseWheelListener(new MouseWheelListener() {
            public void mouseWheelMoved(MouseWheelEvent e) {
                try {

                    String p = "4;" + new Random().nextInt(256) + ";" + new Random().nextInt(256) + ";" + new Random().nextInt(256);
                    pck = new DatagramPacket(p.getBytes(), p.getBytes().length,
                            ip, puerto);
                    sck.send(pck);
                } catch (IOException e1) {

                }
            }
        });
        window.getPanelDibujo().addMouseMotionListener(
                new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        try {
                            e.getButton();
                            String p = "1;" + e.getX() + ";" + e.getY();
                            pck = new DatagramPacket(p.getBytes(),
                                    p.getBytes().length, ip, puerto);
                            sck.send(pck);
                        } catch (IOException e1) {
                        }
                    }
                });
    }

    /**
     * Se encarga de enviar al servidor las palabras que escribe el cliente cada
     * vez que se haga clic en enviar
     */
    public static void escritor() {
        window.getBoton().addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!window.getTexto().getText().equalsIgnoreCase("")) {
                        String a = window.getTexto().getText();
                        String p = "2;" + nombre + ";" + a.replace(';', ' ');
                        pck = new DatagramPacket(p.getBytes(),
                                p.getBytes().length, ip, puerto);
                        sck.send(pck);
                        window.getTexto().setText("");
                    }
                } catch (IOException e1) {
                }
            }
        });
        window.getTexto().addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    try {
                        if (!window.getTexto().getText().equalsIgnoreCase("")) {
                            String a = window.getTexto().getText();
                            String p = "2;" + nombre + ";" + a.replace(';', ' ');
                            pck = new DatagramPacket(p.getBytes(),
                                    p.getBytes().length, ip, puerto);
                            sck.send(pck);
                            window.getTexto().setText("");
                        }
                    } catch (IOException e1) {
                    }
                }
            }
        });
    }

    /**
     * Se encarga de agregar a la ventana las intrucciones a seguir cuando esta
     * se cierra
     */
    public static void ventana() {
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                try {
                    String p = "3;" + nombre;
                    pck = new DatagramPacket(p.getBytes(), p.getBytes().length,
                            ip, puerto);
                    sck.send(pck);
                } catch (IOException e1) {
                }
            }
        });
    }
}