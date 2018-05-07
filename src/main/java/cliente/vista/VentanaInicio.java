package cliente.vista;

import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author Andr�s Felipe Esguerra Restrepo
 * @author Luis Eduardo Naranjo Contreras
 */
@Slf4j
public class VentanaInicio extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final LayoutManager LAYOUT = new GridLayout(3, 1);
    private static final Dimension DIMENSION = new Dimension(300, 200);

    private final JTextField nomUser = new JTextField();
    private final Consumer<String> onGameStart;

    public VentanaInicio(Consumer<String> onGameStart) {
        this.onGameStart = onGameStart;

        log.info("Building!");

        setTitle("Sketching");
        setSize(DIMENSION);
        setLayout(LAYOUT);
        setLocationRelativeTo(null);
        setResizable(false);

        nomUser.setBorder(BorderFactory.createTitledBorder("Ingrese su nombre de usuario"));

        JButton boton = new JButton("Enviar");
        boton.addActionListener(this::onSend);

        add(nomUser);
        add(boton);
    }

    private String getValidUsername() {
        String username = nomUser.getText();
        if (Objects.isNull(username) || username.equals("")) {
            throw new IllegalArgumentException("Username cannot be blank");
        }

        if (username.contains(";")) {
            throw new IllegalArgumentException("Username cannot contain semicolons");
        }

        return username;
    }

    private void onSend(ActionEvent event) {
        log.info("Sending join game request");
        final String username;

        try {
            username = this.getValidUsername();
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Invalid input",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        try {
            onGameStart.accept(username);
        } finally {
            this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }

        this.dispose();
    }
}