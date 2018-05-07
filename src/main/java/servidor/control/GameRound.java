package servidor.control;

import com.google.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import servidor.modelo.Player;
import servidor.modelo.WordSupplier;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
@Slf4j
public class GameRound {
    private static final int MINIMUM_PLAYERS = 3;
    private static final int BUFFER_SIZE = 100;
    private static final Charset CHAR_SET = StandardCharsets.UTF_8;


    private final List<Player> players;
    private final PlayersManager playersManager;
    private final String expectedWord = new WordSupplier().get();
    private Player sketcher;
    private Status status;
    private boolean isWon = false;

    @Inject
    public GameRound(PlayersManager playersManager) {
        this.playersManager = playersManager;
        this.players = new ArrayList<>();
        this.status = Status.PENDING;
    }

    public final void waitForPlayers() throws IOException {
        while (playersManager.getPlayers().size() < MINIMUM_PLAYERS) {
            log.info("{} out of {} players", playersManager.getPlayers().size(), MINIMUM_PLAYERS);

            final Player player = playersManager.waitForPlayerRegistration();
            log.info("New player: {}", player);
        }
    }

    public void startGame() {
        log.info("Starting game");
        status = Status.IN_PROGRESS;
        Random random = new Random();
        int sketcherIndex = random.nextInt(playersManager.getPlayers().size());
        sketcher = playersManager.getPlayers().get(sketcherIndex);
        playersManager.broadcastGameStart(sketcher.getName(), expectedWord);
        while (Status.IN_PROGRESS.equals(status)) {
            final String message = playersManager.listen();
            descifrar(message);
        }
    }

    /**
     * Se encarga de verificar si la expectedWord enviada por el cliente es la expectedWord correcta
     *
     * @return Si la expectedWord era o no correcta
     */
    public boolean isCorrectWord(String receivedWord) {
        if (expectedWord.equalsIgnoreCase(receivedWord)) {
            isWon = true;
            return true;
        }
        return false;
    }

    public void descifrar(String mensaje) {
        String[] msj = mensaje.split(";");
        log.info("Decoding message: {}", Arrays.toString(msj));
        switch (Integer.parseInt(msj[0])) {
            case 1:
                // coordenadas
                int x = Integer.parseInt(msj[1]);
                int y = Integer.parseInt(msj[2]);
                int rgb = Integer.parseInt(msj[3]);
                playersManager.broadcastCoordinates(x, y, rgb);
                break;
            case 2:
                // expectedWord
                final String username = msj[1];
                final String guess = msj[2];
                final boolean isCorrectWord = isCorrectWord(guess);
                playersManager.broadcastGuessAttempt(username, guess, isCorrectWord);
                if (isCorrectWord) {
                    status = Status.FINISHED;
                }
                break;
            case 3:
                final String goneUsername = msj[1];
                playersManager.broadcastLostConection(goneUsername);
                throw new RuntimeException(mensaje);
            default:
                break;
        }
    }

    private enum Status {
        PENDING,
        IN_PROGRESS,
        FINISHED
    }
}
