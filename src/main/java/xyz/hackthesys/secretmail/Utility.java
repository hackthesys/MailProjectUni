package xyz.hackthesys.secretmail;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public final class Utility {

    private Utility(){}

    public static List<MessageItem> deserializeMessages() {
        File file = new File("backup.serial");
        if(!file.exists())
            return null;

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            return (List<MessageItem>) ois.readObject();
        } catch (IOException e){
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void serializeMessages(List<MessageItem> messageItems){
        File file = new File("backup.serial");
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))){
            oos.writeObject(new ArrayList<>(messageItems));
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static String getMessageContentFromSubject(List<MessageItem> messages, String subject){
        return messages
                .stream()
                .filter(messageItem -> messageItem.getSubject().equalsIgnoreCase(subject))
                .findFirst()
                .map(messageItem -> messageItem.getContent())
                .get();
    }

    public static Message getUnseenMessageWithSubject(List<MessageItem> messageItems, String subject) throws MessagingException {
        for (MessageItem messageItem : messageItems) {
            if(messageItem.getMessageObject() != null && !messageItem.getMessageObject().getFlags().contains(Flags.Flag.SEEN)){
                return messageItem.getMessageObject();
            }
        }

        return null;
    }

    public static List<String> getListOfNewSubjects(List<MessageItem> currentMessage, List<MessageItem> oldMessages){
        List<String> subjects = new ArrayList<>();
        for (MessageItem messageItem : currentMessage) {
            boolean found = false;
            for (MessageItem oldMessage : oldMessages) {
                if(oldMessage.getSubject().equals(messageItem.getSubject())){
                    found = true;

                }
            }

            if(!found)
                subjects.add(messageItem.getSubject());
        }

        System.out.println(subjects);
        return subjects;
    }

}
