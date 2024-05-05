package xyz.hackthesys.secretmail;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class App extends Application {
/*
    static {
        ClientController.getInstance();
        LoginController.getInstance();
    }

 */
    public static Stage loginStage;
    public static Stage clientStage;
    public static Stage sendingEmailsStage;

    public static void shutdownSequence(){
        Utility.serializeMessages(LoginController.messages);
    }

    private void initialize() throws IOException {
        //init login Stage
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("LoginController.fxml"));
        fxmlLoader.setController(LoginController.getInstance());
        Scene scene = new Scene(fxmlLoader.load());
        loginStage = new Stage();
        loginStage.setResizable(false);
        loginStage.setTitle("Secret Mail v1.0");
        loginStage.setScene(scene);
        loginStage.show();

        //Client Stage
        fxmlLoader = new FXMLLoader(App.class.getResource("ClientController.fxml"));
        fxmlLoader.setController(ClientController.getInstance());
        scene = new Scene(fxmlLoader.load());
        //Sscene.getStylesheets().add("style.css");
        clientStage = new Stage();
        clientStage.setResizable(false);
        clientStage.setTitle("Secret Mail v1.0");
        clientStage.setScene(scene);


        //Sendeing-Emails Stage
        fxmlLoader = new FXMLLoader(App.class.getResource("SendingController.fxml"));
        fxmlLoader.setController(SendingEmailController.getInstance());
        scene = new Scene(fxmlLoader.load());
        //Sscene.getStylesheets().add("style.css");
        sendingEmailsStage = new Stage();
        sendingEmailsStage.setResizable(false);
        sendingEmailsStage.setTitle("Secret Mail v1.0");
        sendingEmailsStage.setScene(scene);

        clientStage.setOnCloseRequest((e) -> {
            shutdownSequence();
            System.out.println("Data Serialized");
        });
    }

    @Override
    public void start(Stage stage) throws IOException {
        ClientController.getInstance();
        LoginController.getInstance();
       // SendingEmailController.getInstance();
        initialize();
    }

    public Stage getClientStage() {
        return clientStage;
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public static void main(String[] args) {
        launch();
    }
}