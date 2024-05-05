package xyz.hackthesys.secretmail;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;
import java.io.Serializable;

public class MessageItem implements Serializable {
    private transient Message message;

    private String content;
    private String subject;


    public MessageItem(Message message) throws MessagingException, IOException {
        this.content = InClient.readMessage(message);
        this.subject = message.getSubject();
        this.message = message;

    }

    public Message getMessageObject() {
        return message;
    }

    public String getContent() {
        return content;
    }

    public String getSubject() {
        return subject;
    }

    @Override
    public String toString() {
        return subject == null || subject.isBlank() ? "No Subject" : subject;
    }
}
