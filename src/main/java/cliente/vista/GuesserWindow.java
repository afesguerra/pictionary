package cliente.vista;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.function.Consumer;

public class GuesserWindow extends VentanaJuego {

    private final Consumer<String> onNewWordGuess;

    public GuesserWindow(String username, Consumer<String> onNewWordGuess) {
        super(username);
        this.onNewWordGuess = onNewWordGuess;

        getBoton().addActionListener(e -> sendWord());
        getTexto().addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    sendWord();
                }
            }
        });
    }

    private void sendWord() {
        String word = getTexto().getText().replace(';', ' ');
        if (!word.equalsIgnoreCase("")) {
            getTexto().setText("");
            onNewWordGuess.accept(word);
        }
    }
}
