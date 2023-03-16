module io.github.ppetrbednar.palladium {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;
    requires json.simple;
    requires java.sql;
    requires org.apache.commons.text;
    requires org.apache.logging.log4j;
    requires fontawesomefx;
    requires java.desktop;
    requires com.jfoenix;

    opens io.github.ppetrbednar.stp to javafx.fxml;
    opens io.github.ppetrbednar.stp.ui to javafx.fxml;

    opens io.github.ppetrbednar.stp.ui.module.panel to javafx.fxml;
    opens io.github.ppetrbednar.stp.ui.module.root to javafx.fxml;

    opens io.github.ppetrbednar.stp.ui.style to javafx.fxml;

    opens io.github.ppetrbednar.stp.ui.window.alert to javafx.fxml;

    exports io.github.ppetrbednar.stp;
    exports io.github.ppetrbednar.stp.logic;
    exports io.github.ppetrbednar.stp.logic.structures;
    opens io.github.ppetrbednar.stp.logic to javafx.fxml;
}