module com.example.acciojob_brickbreakergame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.acciojob_brickbreakergame to javafx.fxml;
    exports com.example.acciojob_brickbreakergame;
}