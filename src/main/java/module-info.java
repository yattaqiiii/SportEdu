module com.sportedu {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;

    opens com.sportedu to javafx.fxml;
    exports com.sportedu;
}
