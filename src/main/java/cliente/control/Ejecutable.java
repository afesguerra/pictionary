package cliente.control;

import cliente.vista.GuesserWindow;
import cliente.vista.SketcherWindow;
import cliente.vista.VentanaInicio;
import cliente.vista.VentanaJuego;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.InvocationTargetException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Slf4j
public class Ejecutable {
    private static final Charset CHAR_SET = StandardCharsets.UTF_8;
    private static final String SEPARATOR = ";";
    private static final int PORT = 1337;

    public static void main(String[] args) throws InterruptedException, InvocationTargetException, IOException {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.connect(new InetSocketAddress(PORT));

            SwingUtilities.invokeAndWait(() -> {
                VentanaInicio ventanaInicio = new VentanaInicio(username -> {
                    try {
                        onGameStart(username, channel);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                });
                ventanaInicio.setVisible(true);
            });

            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            while (Thread.currentThread().isInterrupted()) {
                channel.connect(new InetSocketAddress(PORT));
                log.info("waiting for message");
                byteBuffer.clear();
                channel.receive(byteBuffer);
                byteBuffer.flip();
                String[] msj = CHAR_SET.decode(byteBuffer).toString().split(SEPARATOR);
                desifrarMensaje(msj, vj);
            }
        }
    }

    private static VentanaJuego onGameStart(String username, DatagramChannel channel) throws IOException {
        log.info("Starting game");
        ByteBuffer buffer = ByteBuffer.allocate(100);

        buffer.put(username.getBytes(CHAR_SET));
        buffer.flip();
        channel.write(buffer);

        buffer.clear();
        channel.receive(buffer);
        buffer.flip();
        String[] message = CHAR_SET.decode(buffer).toString().split(SEPARATOR);
        log.info("message: {}", Arrays.toString(message));

        if (message[0].equals("error")) {
            throw new IllegalArgumentException("Username already exists");
        }

        String sketcher = message[1];
        String expectedWord = message[2];
        VentanaJuego window;

        if (sketcher.equals(username)) {
            window = new SketcherWindow(username, (point, color) -> {
                final String drawMessage = String.format("1;%s;%s;%s", point.x, point.y, color.getRGB());
                sendMessage(channel, drawMessage);
            });
            JOptionPane.showMessageDialog(window,
                    String.format("Tú eres el dibujante, la palabra a dibujar es: %s", expectedWord),
                    "Tú eres el dibujante",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            window = new GuesserWindow(username, word -> {
                final String guessMessage = String.format("2;%s;%s", username, word);
                sendMessage(channel, guessMessage);
            });
            JOptionPane.showMessageDialog(window,
                    String.format("El jugador %s es el dibujante", sketcher),
                    "A jugar",
                    JOptionPane.INFORMATION_MESSAGE);
        }
        window.setVisible(true);

        return window;
    }

    private static void desifrarMensaje(String[] msj, VentanaJuego window) {

        switch (Integer.parseInt(msj[0])) {
            case 0:
                // Alguien gan�
                JOptionPane.showMessageDialog(
                        window,
                        msj[2],
                        "WINNER!!",
                        JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
                break;
            case 1:
                // coordenadas
                int x = Integer.parseInt(msj[1]);
                int y = Integer.parseInt(msj[2]);
                int rgb = Integer.parseInt(msj[3]);
                window.dibujar(new Point(x, y), rgb);
                break;
            case 2:
                // palabra+
                final String guess = String.format("%s: %s", msj[1], msj[2]);
                window.addWordGuess(guess);
                break;
            case 3:
                // alguien se desconecto
                JOptionPane.showMessageDialog(
                        window,
                        msj[1],
                        "Error de conexión",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(0);
                break;
            default:
                break;
        }
    }

    protected static final void sendMessage(DatagramChannel channel, String message) {
        log.info("Sending message: {}", message);
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        byteBuffer.clear();
        byteBuffer.put(message.getBytes());
        byteBuffer.flip();

        try {
            channel.write(byteBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}