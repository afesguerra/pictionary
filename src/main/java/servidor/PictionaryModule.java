package servidor;

import com.google.inject.AbstractModule;
import servidor.control.GameRound;
import servidor.control.PlayersManager;

public class PictionaryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameRound.class);
        bind(PlayersManager.class);
    }
}
