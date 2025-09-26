module com.example.pepe_photoshop {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.pepe_photoshop to javafx.fxml;
    exports com.example.pepe_photoshop;
}