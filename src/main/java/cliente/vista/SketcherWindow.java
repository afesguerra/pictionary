package cliente.vista;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.util.Random;
import java.util.function.BiConsumer;

public class SketcherWindow extends VentanaJuego {
    private Color color = Color.BLACK;

    public SketcherWindow(String username, BiConsumer<Point, Color> onDrawing) {
        super(username);

        getPanelDibujo().setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        getPanelDibujo().addMouseWheelListener(this::changeDrawingColor);
        getPanelDibujo().addMouseMotionListener(
                new MouseMotionAdapter() {
                    public void mouseDragged(MouseEvent e) {
                        final Point point = new Point(e.getX(), e.getY());
                        dibujar(point, color.getRGB());
                        onDrawing.accept(point, color);
                    }
                });
    }

    private void changeDrawingColor(MouseWheelEvent event) {
        final int bound = 256;
        final Random random = new Random();
        color = new Color(random.nextInt(bound));

        getPanelDibujo().getGraphics().setColor(color);
        getPanelDibujo().getGraphics().fillRect(10, 20, 20, 20);
    }
}
