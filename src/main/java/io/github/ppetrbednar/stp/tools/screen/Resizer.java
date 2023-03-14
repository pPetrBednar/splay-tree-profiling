package io.github.ppetrbednar.stp.tools.screen;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 *
 * @author Petr Bednář
 */
public class Resizer implements EventHandler<MouseEvent> {

    /**
     * Adds listeners to all nodes in specified Node.
     *
     * @param resizer Resizer
     * @param node Node
     */
    public static void addResizerRecursive(Resizer resizer, Node node) {
        node.addEventHandler(MouseEvent.MOUSE_MOVED, resizer);
        node.addEventHandler(MouseEvent.MOUSE_PRESSED, resizer);
        node.addEventHandler(MouseEvent.MOUSE_DRAGGED, resizer);
        node.addEventHandler(MouseEvent.MOUSE_EXITED, resizer);
        node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizer);

        if (node instanceof ScrollPane) {
            ScrollPane pane = (ScrollPane) node;
            Node content = (Parent) pane.getContent();

            if (content != null) {
                Parent parent = (Parent) content;

                for (Node child : parent.getChildrenUnmodifiable()) {
                    addResizerRecursive(resizer, child);
                }
            }
            return;
        }

        if (node instanceof Parent) {
            Parent parent = (Parent) node;
            ObservableList<Node> children = parent.getChildrenUnmodifiable();

            for (Node child : children) {
                addResizerRecursive(resizer, child);
            }
        }
    }

    private Stage stage;
    private Cursor cursorEvent = Cursor.DEFAULT;

    // Size of resize selection area (px)
    private int border = 5;
    private double startX = 0;
    private double startY = 0;

    public Resizer(Stage stage) {
        this.stage = stage;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
        Scene scene = stage.getScene();

        double mouseEventX = mouseEvent.getSceneX();
        double mouseEventY = mouseEvent.getSceneY();
        double sceneWidth = scene.getWidth();
        double sceneHeight = scene.getHeight();

        // Change mouse style on mouse move
        if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) {
            if (mouseEventX < border && mouseEventY < border) {
                cursorEvent = Cursor.NW_RESIZE;
            } else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
                cursorEvent = Cursor.SW_RESIZE;
            } else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
                cursorEvent = Cursor.NE_RESIZE;
            } else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
                cursorEvent = Cursor.SE_RESIZE;
            } else if (mouseEventX < border) {
                cursorEvent = Cursor.W_RESIZE;
            } else if (mouseEventX > sceneWidth - border) {
                cursorEvent = Cursor.E_RESIZE;
            } else if (mouseEventY < border) {
                cursorEvent = Cursor.N_RESIZE;
            } else if (mouseEventY > sceneHeight - border) {
                cursorEvent = Cursor.S_RESIZE;
            } else {
                cursorEvent = Cursor.DEFAULT;
            }
            scene.setCursor(cursorEvent);

            // Change mouse to normal on area exit
        } else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType) || MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
            scene.setCursor(Cursor.DEFAULT);

            // Save strating position at mouse press
        } else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
            startX = stage.getWidth() - mouseEventX;
            startY = stage.getHeight() - mouseEventY;

            // Update window when dragging with mouse
        } else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) {
            if (!Cursor.DEFAULT.equals(cursorEvent)) {

                // Vertical resizing
                if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
                    double minHeight = stage.getMinHeight() > (border * 2) ? stage.getMinHeight() : (border * 2);
                    if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent) || Cursor.NE_RESIZE.equals(cursorEvent)) {
                        if (stage.getHeight() > minHeight || mouseEventY < 0) {

                            double newHeight = stage.getY() - mouseEvent.getScreenY() + stage.getHeight();
                            stage.setHeight(newHeight > minHeight ? newHeight : minHeight);
                            stage.setY(mouseEvent.getScreenY());
                        }
                    } else {
                        if (stage.getHeight() > minHeight || mouseEventY + startY - stage.getHeight() > 0) {
                            double newHeight = mouseEventY + startY;
                            stage.setHeight(newHeight > minHeight ? newHeight : minHeight);
                        }
                    }
                }

                // Horizontal resizing
                if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
                    double minWidth = stage.getMinWidth() > (border * 2) ? stage.getMinWidth() : (border * 2);
                    if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent) || Cursor.SW_RESIZE.equals(cursorEvent)) {
                        if (stage.getWidth() > minWidth || mouseEventX < 0) {

                            double newWidth = stage.getX() - mouseEvent.getScreenX() + stage.getWidth();
                            stage.setWidth(newWidth > minWidth ? newWidth : minWidth);
                            stage.setX(mouseEvent.getScreenX());
                        }
                    } else {
                        if (stage.getWidth() > minWidth || mouseEventX + startX - stage.getWidth() > 0) {

                            double newWidth = mouseEventX + startX;
                            stage.setWidth(newWidth > minWidth ? newWidth : minWidth);
                        }
                    }
                }
            }
        }
    }
}
