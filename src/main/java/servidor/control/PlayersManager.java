package servidor.control;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import servidor.modelo.Player;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class PlayersManager implements AutoCloseable {
    private static final int PORT = 1337;
    private static final int BUFFER_SIZE = 100;
    private static final Charset CHAR_SET = StandardCharsets.UTF_8;
    private final DatagramChannel channel;

    @Getter
    private final List<Player> players = new ArrayList<>();

    public PlayersManager() throws IOException {
        this.channel = DatagramChannel.open();
        channel.bind(new InetSocketAddress(PORT));
    }

    public Player waitForPlayerRegistration() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        byteBuffer.clear();
        SocketAddress socketAddress = channel.receive(byteBuffer);
        byteBuffer.flip();
        String playerName = CHAR_SET.decode(byteBuffer).toString();
        if (players.stream().map(Player::getName).anyMatch(playerName::equalsIgnoreCase)) {
            final String error = "error;Lo sentimos, el nombre elejido ya está en uso, hasta pronto";
            byteBuffer.clear();
            byteBuffer.put(error.getBytes());
            byteBuffer.flip();
            channel.send(byteBuffer, socketAddress);
            throw new IllegalArgumentException();
        }

        Player player = new Player(playerName, socketAddress);
        players.add(player);
        return player;
    }


    public void broadcastGuessAttempt(String username, String guess, boolean correct) {
        String msj = String.format("0;%s;%s;%s", username, guess, correct);
        broadcast(msj);
    }

    public void broadcastCoordinates(int x, int y, int rgb) {
        String message = String.format("1;%s;%s;%s", x, y, rgb);
        broadcast(message);
    }

    public void broadcastLostConection(String username) {
        String mensaje = String.format("2;%s", username);
        broadcast(mensaje);
    }

    public void broadcastGameStart(String sketcherName, String expectedWord) {
        String msg = String.format("3;%s;%s", sketcherName, expectedWord);
        broadcast(msg);
    }

    private void broadcast(String message) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes(CHAR_SET));
        for (Player player : players) {
            try {
                channel.send(byteBuffer, player.getIp());
                byteBuffer.flip();
            } catch (IOException e) {
                throw new RuntimeException("Error broadcasting message", e);
            }
        }
    }

    public String listen() {
        final ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        buffer.clear();
        try {
            channel.receive(buffer);
            return CHAR_SET.decode(buffer).toString();
        } catch (IOException e) {
            log.error("Error receiving datagram packet", e);
            return "";
        }
    }

    @Override
    public void close() throws Exception {
        channel.close();
    }
}
