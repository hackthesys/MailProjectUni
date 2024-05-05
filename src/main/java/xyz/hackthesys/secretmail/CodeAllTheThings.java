package xyz.hackthesys.secretmail;

import com.google.gson.Gson;
import com.sun.mail.pop3.POP3Folder;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;
import java.util.concurrent.CompletionService;
public class CodeAllTheThings {
    private static CodeAllTheThings instance;

    public static CodeAllTheThings getInstance() {
        return instance == null ? (instance = new CodeAllTheThings()) : instance;
    }


    public String removeBadChars(String str) {
        ArrayList<String> badChars = new ArrayList<String>();
        if (str.contains("<") && str.contains(">"))
        {
            return str;
        }
        else if(!str.contains("<") && str.contains(">"))
        {
            StringBuilder bt = new StringBuilder();
            bt.append("<").append(str);
            return bt.toString();
        }else if (str.contains("<") && !str.contains(">"))
        {
            StringBuilder bt = new StringBuilder();
            bt.append(str).append(">");
            return bt.toString();
        }else{ return str;}

    }

    public void reconnect() {
        LoginController.inClient.connectToInServer();
        ClientController.getInstance().getListView().getItems().clear();
        final Message[][] messages = new Message[1][1];
        Thread t1 = new Thread(() -> {
            //launch loader
            Platform.runLater(() -> ClientController.getInstance().getLoadingIndicator().setProgress(ProgressIndicator.INDETERMINATE_PROGRESS));
            messages[0] = LoginController.inClient.getMassages();
            //load all messages
            int n = messages[0].length - LoginController.messages.size();
            for (int i = messages[0].length - 1; i >= messages[0].length - n; i--) {
                try {
                    String s = messages[0][i].getSubject();
                    LoginController.messages.add(0, new MessageItem(messages[0][i]));
                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                }

            }
            //Applied changes to FXThread
            Platform.runLater(() -> {
                // Fix The Length size of each Cell on the ListView,
                ClientController.getInstance().getListView().setFixedCellSize(75);

                List<MessageItem> reversedMessages = new ArrayList<>(LoginController.messages);


                reversedMessages
                        .forEach((message) -> {
                            ClientController refrence = ClientController.getInstance();

                            ClientController.getInstance().getListView().getItems().add(message.getSubject());

                        });

                ClientController.getInstance().getLoadingIndicator().setVisible(false);


                var subjects = Utility.getListOfNewSubjects(LoginController.messages, LoginController.oldMessages);
                System.out.println(subjects);



                var list = ClientController.getInstance().getListView().getItems();

                for (int i = 0; i < list.size(); i++) {
                    if(subjects.contains(list.get(i))){
                        list.set(i, "New Message: " + list.get(i));
                    }
                }

                LoginController.oldMessages = new ArrayList<>(LoginController.messages);

            });


        });
        t1.setDaemon(true);
        t1.start();

    }
}
