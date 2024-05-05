package xyz.hackthesys.secretmail;

import com.sun.mail.pop3.POP3Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginController implements Initializable, Serializable {
    private static LoginController instance;

    public static List<MessageItem> messages = new ArrayList<>();
    public static List<MessageItem> oldMessages = new ArrayList<>();

    private ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static LoginController getInstance()
    {
        return instance == null ? (instance = new LoginController()) : instance;
    }
    public static InClient inClient;
    public static OutClient outClient;

    @FXML
    private TextField userID;

    @FXML
    private TextField fullname;
    @FXML
    private TextField IMS_Port_text;

    @FXML
    private TextField IMS_text;

    @FXML
    private TextField OMS_Port_text;

    @FXML
    private TextField OMS_text;

    @FXML
    private PasswordField PASS_text;

    @FXML
    private TextField email_text;

    @FXML
    private Button login_bt;

    @FXML
    private RadioButton saveCredentials_bt;


    @FXML
    void IMS(ActionEvent event) {

    }

    @FXML
    void IMS_Port(ActionEvent event) {

    }

    @FXML
    void OMS(ActionEvent event) {

    }

    @FXML
    void OMS_Port(ActionEvent event) {

    }

    @FXML
    void PASS(ActionEvent event) {

    }

    @FXML
    private ImageView sendingEmail;

    @FXML
    void email(ActionEvent event) {

    }

    @FXML
    void login(ActionEvent event) throws IOException {
        // POP3-Server

        inClient = new InClient();

        inClient.setUSER(email_text.getText());
        inClient.setPASS(PASS_text.getText());

        inClient.setIncomingServer(IMS_text.getText());
        inClient.setIncomingPort(IMS_text.getText());
        inClient.connectToInServer();

        // SMTP-Server
        outClient = new OutClient();

        try {
            outClient.setSenderAddress(email_text.getText());
            outClient.setPASS(PASS_text.getText());

            outClient.setOut_PORT(Integer.parseInt(OMS_Port_text.getText()));
            outClient.setSMTP_Server(OMS_text.getText());
            outClient.connectToOutServer();
        } catch (NoSuchProviderException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("SMTP-Server Error !!!");
            alert.setContentText("Unable to connect to SMTP-Server !!");
            alert.showAndWait();

            Platform.exit();
        }

        if (inClient.isCheckInServer() && outClient.isCheckOutServer()) {
            messages = Utility.deserializeMessages();
            if(messages == null)
                messages = new ArrayList<>();


            App.clientStage.show();

            ClientController.getInstance().getLoggedInAs().setText(LoginController.inClient.getUSER());
            //inClient.storeMsgFolder(inClient);

            //AtmoicInteger in anonymous class
            final Message[][] messages = new Message[1][1];

            Thread t1 = new Thread(() -> {
                //launch loader
                Platform.runLater(() -> ClientController.getInstance().getLoadingIndicator().setProgress(ProgressIndicator.INDETERMINATE_PROGRESS));
                messages[0] = inClient.getMassages();
                //load all messages

                int n = messages[0].length - LoginController.messages.size();
                for (int i = messages[0].length - 1; i >= messages[0].length - n; i--) {
                    try {
                        String s = messages[0][i].getSubject();
                        LoginController.messages.add(new MessageItem(messages[0][i]));
                    } catch (MessagingException | IOException e) {
                        e.printStackTrace();
                    }

                }

                oldMessages.addAll(LoginController.messages);

                //Applied changes to FXThread
                Platform.runLater(() -> {
                    // Fix The Length size of each Cell on the ListView,
                    ClientController.getInstance().getListView().setFixedCellSize(75);

                    List<MessageItem> reversedMessages = new ArrayList<>(LoginController.messages);



                    /////////////////



                    ////////////////


                    reversedMessages
                            .forEach((message) -> {
                                ClientController.getInstance().getListView().getItems().add(message.getSubject());

                            });

                    ClientController.getInstance().getLoadingIndicator().setVisible(false);



                });
            });


            t1.setDaemon(true);
            t1.start();
        }
    }

    @FXML
    void saveCredentials(ActionEvent event) throws IOException {
        SaveingCredentials.getInstance().getCredentials(fullname.getText(), userID.getText(), email_text.getText(), PASS_text.getText(), IMS_text.getText(), Integer.valueOf(IMS_Port_text.getText()), OMS_text.getText(), Integer.valueOf(OMS_Port_text.getText()));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File file = new File("credentials.hts");

        if (file.exists())
        {
            SaveingCredentials.getInstance().loadCredentials(new File("credentials.hts"));
            userID.setText(SaveingCredentials.getInstance().getUserId());
            fullname.setText(SaveingCredentials.getInstance().getName());
            email_text.setText(SaveingCredentials.getInstance().getEmail());
            IMS_text.setText(SaveingCredentials.getInstance().getInServer());
            OMS_text.setText(SaveingCredentials.getInstance().getOutServer());
            IMS_Port_text.setText(String.valueOf(SaveingCredentials.getInstance().getInServerPort()));
            OMS_Port_text.setText(String.valueOf(SaveingCredentials.getInstance().getOutServerPort()));
        }

    }
}
