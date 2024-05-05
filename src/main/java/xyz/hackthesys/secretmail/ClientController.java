package xyz.hackthesys.secretmail;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;
import javax.mail.search.SubjectTerm;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private static ClientController instance;
    public static ClientController getInstance()
    {
        return instance == null ? (instance = new ClientController()) : instance;
    }
    private ClientController()
    {

    }





    @FXML
    private ImageView refreshEmails;
    @FXML
    private ImageView sendingEmail;

    public ImageView getSendingEmail()
    {
        return  sendingEmail;
    }

    @FXML
    void sendEmail(MouseEvent event) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                App.sendingEmailsStage.show();
                SendingEmailController.getInstance().setUserLabel(LoginController.inClient.getUSER());
            }
        });

    }

    public ListView<String> getListView() {
        return listView;
    }

    public String getContentEmail() {
        return contentEmail;
    }


    public void setContentEmail(String contentEmail) {
        this.contentEmail = contentEmail;
    }

    @FXML
    void exitApplication(MouseEvent event) {
        App.shutdownSequence();
        Platform.exit();
    }
    @FXML
    private String contentEmail;

    @FXML
    private TextField loggedInAs;

    public TextField getLoggedInAs() {
        return loggedInAs;
    }

    @FXML
    private ListView<String> listView;
    @FXML
    private ProgressIndicator loadingIndicator;
    @FXML
    private TextArea emailTextArea;

    public TextArea getEmailTextArea() {
        return emailTextArea;
    }
    @FXML
    private TextField search_tb;
    public ProgressIndicator getLoadingIndicator() {
        return loadingIndicator;
    }

    @FXML
    void searchBox(ActionEvent event) throws MessagingException {

    }

    @FXML
    void updateInbox(MouseEvent event) throws IOException {
        CodeAllTheThings.getInstance().reconnect();

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("backup.txt");
        search_tb.textProperty().addListener((observableValue, old, newValue) -> {
            var list = listView.getItems();
            for (String s : list) {
                if(newValue.equalsIgnoreCase(s)){
                    listView.getSelectionModel().select(s);
                    listView.scrollTo(s);
                    System.out.println(s);
                    break;
                }
            }
        });

        listView.getSelectionModel().selectedItemProperty().addListener((list, old, newItem) -> {

            if(newItem == null)
                return;

            String[] tokens = newItem.split(":");
            System.out.println(Arrays.toString(tokens));
            if(tokens.length == 2){
                emailTextArea.setText(Utility.getMessageContentFromSubject(LoginController.messages, tokens[1].trim()));
                for (int i = 0; i < listView.getItems().size(); i++) {
                    if(tokens[1].equals(listView.getItems().get(i))){
                        listView.getItems().set(i, tokens[1]);
                    }
                }
            }
            else
                emailTextArea.setText(Utility.getMessageContentFromSubject(LoginController.messages, newItem));

        });
    }
}
