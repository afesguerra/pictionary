package servidor;

import com.google.inject.Guice;
import com.google.inject.Injector;
import servidor.control.GameRound;

import java.io.IOException;

public class Pictionary {
    public static void main(String[] args) throws IOException {
        final Injector injector = Guice.createInjector(new PictionaryModule());

        final GameRound gameRound = injector.getInstance(GameRound.class);

        gameRound.waitForPlayers();

        gameRound.startGame();
    }
}
