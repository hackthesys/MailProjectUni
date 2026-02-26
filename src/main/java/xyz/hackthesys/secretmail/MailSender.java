package xyz.hackthesys.secretmail;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class MailSender {
    private Session session;
    private String Subject;
    private String text;
    ArrayList<String> cc_list = new ArrayList<>();
    ArrayList<String> rec_list = new ArrayList<>();



    public MailSender(){

        session = LoginController.outClient.getSession();
    }
    public void sendEmail() throws MessagingException {
        MimeMessage message = new MimeMessage(session);


        message.setFrom(new InternetAddress(LoginController.inClient.getUSER(), false));

        if (cc_list.size() > 0) {
            for (String s : cc_list
            ) {

                message.addRecipient(Message.RecipientType.CC, new InternetAddress(s, false));
            }
        }

        if (rec_list.size() > 0) {
            for (String s : rec_list
            ) {
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(s, false));
            }
        }
        message.setSubject(Subject);
        message.setText(text);
        message.setSentDate(new Date());

        message.addHeader("Content-type", "text/plain; charset=UTF-8");
        message.addHeader("format", "flowed");
        message.addHeader("Content-Transfer-Encoding", "8bit");

        Transport.send(message);
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void addToCcList(String st) {
        cc_list.add(st);
    }

    public void addToRecList(String st) {
        rec_list.add(st);
    }
}