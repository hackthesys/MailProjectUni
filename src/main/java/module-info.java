module xyz.hackthesys.secretmail {
    requires javafx.controls;
    requires javafx.fxml;
    requires mail;
    requires com.google.gson;


    opens xyz.hackthesys.secretmail to javafx.fxml;
    exports xyz.hackthesys.secretmail;
}