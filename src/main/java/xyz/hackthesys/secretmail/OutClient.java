package xyz.hackthesys.secretmail;

import javafx.scene.control.Alert;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Properties;

public class OutClient {
    private Session session;
    private String senderAddress, PASS, SMTP_Server;
    private int out_PORT;

    private boolean checkOutServer = false;
    public void connectToOutServer() throws NoSuchProviderException
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_Server);
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.port", out_PORT);
        props.put("mail.smtp.ssl.enable", "false");
        props.put("mail.smtp.starttls.enable", "false");
        props.put("mail.transport.protocol", "smtp");


         session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderAddress, PASS); // SenderAddress , PASS
            }
        });
        try
        {
            session.getTransport().connect();


            ////

            checkOutServer = true;
        }
        catch (Exception e){
            System.out.println("session.getTransport().connect(): Error !!!");
            e.printStackTrace();
        }
    }

    public boolean isCheckOutServer() {
        return checkOutServer;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getPASS() {
        return PASS;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public String getSMTP_Server() {
        return SMTP_Server;
    }

    public void setSMTP_Server(String SMTP_Server) {
        this.SMTP_Server = SMTP_Server;
    }

    public int getOut_PORT() {
        return out_PORT;
    }

    public void setOut_PORT(int out_PORT) {
        this.out_PORT = out_PORT;
    }

    public Session getSession() {
        return session;
    }
}
