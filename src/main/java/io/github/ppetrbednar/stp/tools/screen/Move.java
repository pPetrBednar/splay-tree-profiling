package io.github.ppetrbednar.stp.tools.screen;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import io.github.ppetrbednar.stp.ui.handler.IScreen;

/**
 *
 * @author Petr Bednář
 */
public class Move {

    protected static final int TRANSITION_LEAWAY = 50;

    public static void addMoveListener(IScreen callback, Stage stage, Node node) {
        MoveListener moveListener = new MoveListener(callback, stage, node);
        ClickListener clickListener = new ClickListener(callback, stage, node);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, moveListener);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, moveListener);
        node.addEventHandler(MouseEvent.MOUSE_CLICKED, clickListener);
    }

    public static class ClickListener implements EventHandler<MouseEvent> {

        private final IScreen callback;
        private final Stage stage;
        private final Node node;

        public ClickListener(IScreen callback, Stage stage, Node node) {
            this.callback = callback;
            this.stage = stage;
            this.node = node;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {

            if (MouseButton.PRIMARY.equals(mouseEvent.getButton()) && mouseEvent.getClickCount() == 2) {

                if (callback.isMaximized()) {
                    callback.restore();
                    return;
                }

                ObservableList<Screen> screens = Screen.getScreensForRectangle(mouseEvent.getScreenX(), mouseEvent.getScreenY(), 1, 1);
                if (screens.isEmpty()) {
                    return;
                }
                Rectangle2D screen = screens.get(0).getVisualBounds();

                callback.setMaximized();

                stage.setX(screen.getMinX());
                stage.setY(screen.getMinY());
                stage.setWidth(screen.getWidth());
                stage.setHeight(screen.getHeight());

                stage.show();
            }
        }
    }

    public static class MoveListener implements EventHandler<MouseEvent> {

        private final IScreen callback;
        private final Stage stage;
        private final Node node;

        private double xOffset;
        private double yOffset;

        public MoveListener(IScreen callback, Stage stage, Node node) {
            this.callback = callback;
            this.stage = stage;
            this.node = node;
        }

        @Override
        public void handle(MouseEvent mouseEvent) {

            if (MouseEvent.MOUSE_PRESSED.equals(mouseEvent.getEventType())) {
                xOffset = stage.getX() - mouseEvent.getScreenX();
                yOffset = stage.getY() - mouseEvent.getScreenY();
                return;
            }

            ObservableList<Screen> screens = Screen.getScreensForRectangle(mouseEvent.getScreenX(), mouseEvent.getScreenY(), 1, 1);
            if (screens.isEmpty()) {
                return;
            }
            Rectangle2D screen = screens.get(0).getVisualBounds();

            // Aero Snap Top Right.
            if (mouseEvent.getScreenY() <= screen.getMinY() + TRANSITION_LEAWAY && mouseEvent.getScreenX() >= screen.getMaxX() - TRANSITION_LEAWAY) {
                stage.setX(screen.getWidth() / 2);
                stage.setY(screen.getMinY());
                stage.setWidth(screen.getWidth() / 2);
                stage.setHeight(screen.getHeight() / 2);

                stage.show();
            } // Aero Snap Top Left Corner
            else if (mouseEvent.getScreenY() <= screen.getMinY() + TRANSITION_LEAWAY && mouseEvent.getScreenX() <= screen.getMinX() + TRANSITION_LEAWAY) {

                stage.setX(screen.getMinX());
                stage.setY(screen.getMinY());
                stage.setWidth(screen.getWidth() / 2);
                stage.setHeight(screen.getHeight() / 2);

                stage.show();
            } // Aero Snap Bottom Right Corner
            else if (mouseEvent.getScreenY() >= screen.getMaxY() - TRANSITION_LEAWAY && mouseEvent.getScreenX() >= screen.getMaxX() - TRANSITION_LEAWAY) {

                stage.setX(screen.getWidth() / 2 - (screen.getWidth() - screen.getWidth()));
                stage.setY(screen.getHeight() / 2 - (screen.getHeight() - screen.getHeight()));
                stage.setWidth(screen.getWidth() / 2);
                stage.setHeight(screen.getHeight() / 2);

                stage.show();
            } // Aero Snap Bottom Left Corner
            else if (mouseEvent.getScreenY() >= screen.getMaxY() - TRANSITION_LEAWAY && mouseEvent.getScreenX() <= screen.getMinX() + TRANSITION_LEAWAY) {

                stage.setX(screen.getMinX());
                stage.setY(screen.getHeight() / 2 - (screen.getHeight() - screen.getHeight()));
                stage.setWidth(screen.getWidth() / 2);
                stage.setHeight(screen.getHeight() / 2);

                stage.show();
            } else // Aero Snap Left.
            if (mouseEvent.getScreenX() <= screen.getMinX()) {
                if (mouseEvent.getScreenY() >= TRANSITION_LEAWAY && mouseEvent.getScreenY() <= screen.getMaxY() - TRANSITION_LEAWAY) {
                    stage.setY(screen.getMinY());
                    stage.setHeight(screen.getHeight());

                    stage.setX(screen.getMinX());
                    if (screen.getWidth() / 2 < stage.getMinWidth()) {
                        stage.setWidth(stage.getMinWidth());
                    } else {
                        stage.setWidth(screen.getWidth() / 2);
                    }

                    stage.show();
                }
            } else // Aero Snap Right. 
            if (mouseEvent.getScreenX() >= screen.getMaxX() - 1) {
                if (mouseEvent.getScreenY() >= TRANSITION_LEAWAY && mouseEvent.getScreenY() <= screen.getMaxY() - TRANSITION_LEAWAY) {
                    stage.setY(screen.getMinY());
                    stage.setHeight(screen.getHeight());

                    if (screen.getWidth() / 2 < stage.getMinWidth()) {
                        stage.setWidth(stage.getMinWidth());
                    } else {
                        stage.setWidth(screen.getWidth() / 2);
                    }
                    stage.setX(screen.getMaxX() - stage.getWidth());

                    stage.show();
                }
            } else // Aero Snap Top. || Aero Snap Bottom.
            if (mouseEvent.getScreenY() <= screen.getMinY() || mouseEvent.getScreenY() >= screen.getMaxY() - 1) {
                if (mouseEvent.getScreenX() >= TRANSITION_LEAWAY && mouseEvent.getScreenX() <= screen.getMaxX() - TRANSITION_LEAWAY) {
                    callback.setMaximized();

                    stage.setX(screen.getMinX());
                    stage.setY(screen.getMinY());
                    stage.setWidth(screen.getWidth());
                    stage.setHeight(screen.getHeight());

                    stage.show();
                }
            } else {
                stage.setX(mouseEvent.getScreenX() + xOffset);
                stage.setY(mouseEvent.getScreenY() + yOffset);
            }
        }

    }
}
