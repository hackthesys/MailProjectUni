package xyz.hackthesys.secretmail;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.mail.MessagingException;


public class SendingEmailController {
    MailSender ms;

    private static SendingEmailController instance;

    public static SendingEmailController getInstance() {
        return instance == null ? (instance = new SendingEmailController()) : instance;
    }

    @FXML
    private Label User;
    public void setUserLabel(String tx){User.setText(tx);}
    @FXML
    private TextField CC_text;
    @FXML
    private TextField an;
    @FXML
    private TextField Betriff;
    @FXML
    private TextArea Text;

    @FXML
    void cc(ActionEvent event) {
    }

    @FXML
    void cancelSending(ActionEvent event) {
        App.sendingEmailsStage.close();
        CC_text.clear();
        an.clear();
        Betriff.clear();
        Text.clear();
    }
    @FXML
    void send(ActionEvent event) throws MessagingException {

        ms = new MailSender();
        //ms.addToRecList(an.getText());

        ms.setSubject(Betriff.getText());
        ms.setText(Text.getText());

        String recc = CC_text.getText();
        if(recc.length() > 0)
        {
            String[] recs = recc.split(",");
            for (String st : recs) {
                ms.addToCcList(st);
            }
        }
        String recT = an.getText();
        if(recT.isBlank())
        {
            an.setText("Enter Emails pls !!!!");
        }
        else
        {
            String[] rec = recT.split(",");
            for (String st : rec) {
                ms.addToRecList(st);
            }
            ms.sendEmail();
        }

    }
}