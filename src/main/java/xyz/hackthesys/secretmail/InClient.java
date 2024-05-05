package xyz.hackthesys.secretmail;

import com.sun.mail.pop3.POP3Folder;
import javafx.scene.control.Alert;

import java.io.*;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.MimeMessage;

public class InClient implements Serializable {
    private String USER, PASS;
    private String incomingServer, incomingPort;
    private Session sessionEmail;
    private Store store;
    private boolean checkInServer = false;

    private POP3Folder pop3Folder;
    private  Message[] massages;


    public void connectToInServer()
    {
        try
        {

            Properties property = new Properties();
            property.put("mail.pop3.host", incomingServer);
            property.put("mail.pop3.port", incomingPort);
            property.put("mail.pop3.starttls.enable", "false");
            property.put("mail.pop3.auth", "true");


            sessionEmail = Session.getDefaultInstance(property);

            store = sessionEmail.getStore("pop3");

            store.connect(incomingServer, USER, PASS);
            //storeMsgFolder(store);
            fetchMessages();

            checkInServer = true;
        }
        catch (MessagingException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Unable to connect to POP3-Server !!");
            alert.showAndWait();
            e.printStackTrace();
            //connectToInServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("initializeJavaMailClient has been initialized !!");
    }
    public boolean checkStoreConnection()
    {
        return store.isConnected();
    }

    public void storeMsgFolder(Store  pop3Folder) throws IOException {
        File file = new File("POP3Folder.dat");


            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(pop3Folder);
            out.flush();
            out.close();
            System.out.println(file.getName() + " has been exported at: " + file.getAbsolutePath());

    }

    public void fetchMessages() throws IOException, MessagingException
    {

            Folder[] folders = store.getDefaultFolder().list();

           /* String inboxFolder = folders.getFullName();
            System.out.println("inboxFolder: " + inboxFolder);
*/

            folders[0].open(POP3Folder.READ_ONLY);
            //massages = folders[0].getMessages();
            pop3Folder = (POP3Folder) folders[0];
            massages = pop3Folder.getMessages();
            int folderSize = pop3Folder.getSize();



            System.out.println("Number of all ClientController: " + massages.length);
            System.out.println("Size of all ClientController: " + folderSize);
            System.out.println("----------------------------------------------");


    }





    public Message[] getMassages() {
        return massages;
    }




    public boolean isCheckInServer() {
        return checkInServer;
    }


    public static String readMessage(Part messageSegment) throws MessagingException, IOException {
        StringBuilder sb = new StringBuilder();
        messageReader(messageSegment, sb);


        return sb.toString();
    }


    private static void messageReader(Part messageSegment, StringBuilder sb) throws MessagingException, IOException {
        if (messageSegment instanceof Message) sb.append(getMessageHeaders((Message) messageSegment));

        if (messageSegment.isMimeType("text/plain")) {
            sb.append("\n").append((String) messageSegment.getContent()).append("\n");
        }
        else if (messageSegment.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) messageSegment.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                messageReader(multipart.getBodyPart(i), sb);
            }
        }
        else if (messageSegment.isMimeType("message/rfc822")) {
            sb.append("Nested-Message:").append("\n");
            messageReader((Part) messageSegment.getContent(), sb);
        }
        else if (messageSegment.isMimeType("image/jpeg")) {
            InputStream is = (InputStream) messageSegment.getContent();
            byte[] buffer = is.readAllBytes();
            FileOutputStream fos = new FileOutputStream("output.jpg");
            fos.write(buffer);
            fos.close();
        }
    }

    private static void messageLoadedReader(Part messageSegment, StringBuilder sb) throws MessagingException, IOException {
        if (messageSegment instanceof Message) sb.append(loadMessageReader((Message) messageSegment));

        if (messageSegment.isMimeType("text/plain")) {
            sb.append("\n").append((String) messageSegment.getContent()).append("\n");
        }
        else if (messageSegment.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) messageSegment.getContent();
            for (int i = 0; i < multipart.getCount(); i++) {
                messageReader(multipart.getBodyPart(i), sb);
            }
        }

    }

    private static String getMeFrom(String str){
        char[] chars = str.toCharArray();
        boolean temp = false;
        StringBuilder st = new StringBuilder();
        for (Character c:chars
             ) {
            if(temp) st.append(c);
            if(c.equals('<')){
                temp = true;
            }else if(c.equals('>')){
                temp = false;
            }
        }
        //return st.substring(0,st.length()-1);
        return st.toString();
    }
    private static String getMessageHeaders(Message message) throws MessagingException {
        StringBuilder builder = new StringBuilder();

        Address[] addresses;

        if ((addresses = message.getFrom()) != null) for (Address address : addresses){
            if(address.toString().contains("<") && address.toString().contains(">"))
            {
                builder.append("From:").append(" ").append(getMeFrom(address.toString())).append("\n");
            }
            else
            {
                builder.append("From:").append(" ").append((address.toString())).append("\n");
            }
        }


        if ((addresses = message.getRecipients(Message.RecipientType.TO)) != null) for (Address address : addresses)
            builder.append("To:").append(" ").append(address.toString()).append("\n");

        if (message.getSubject() != null) builder.append("Subject: ").append(message.getSubject()).append("\n");

        if (message.getDescription() != null)
            builder.append("Description: ").append(message.getDescription()).append("\n");

        return builder.toString();
    }

    public static String loadMessageReader(Message message) throws MessagingException, IOException {
        StringBuilder builder = new StringBuilder();

        Address[] addresses;

        if ((addresses = message.getFrom()) != null) for (Address address : addresses){
            if(address.toString().contains("<") && address.toString().contains(">"))
            {
                builder.append("--HackTheSys--").append("\n").append("From:").append(" ").append(getMeFrom(address.toString())).append("\n");
            }
            else
            {
                builder.append("--HackTheSys--").append("\n").append("From:").append(" ").append((address.toString())).append("\n");
            }
        }


        if ((addresses = message.getRecipients(Message.RecipientType.TO)) != null) for (Address address : addresses)
            builder.append("To:").append(" ").append(address.toString()).append("\n");

        if (message.getSubject() != null) builder.append("Subject: ").append(message.getSubject()).append("\n");

        if (message.getDescription() != null)
            builder.append("Description: ").append(message.getDescription()).append("\n");



        return builder.toString();
    }
    public static String readLoadedMessage(Part messageSegment) throws MessagingException, IOException {
        StringBuilder sb = new StringBuilder();
        messageLoadedReader(messageSegment, sb);


        return sb.toString();
    }



    public void setUSER(String USER) {
        this.USER = USER;
    }

    public String getUSER() {
        return USER;
    }

    public void setPASS(String PASS) {
        this.PASS = PASS;
    }

    public String getIncomingServer() {
        return incomingServer;
    }

    public void setIncomingServer(String incomingServer) {
        this.incomingServer = incomingServer;
    }

    public String getIncomingPort() {
        return incomingPort;
    }

    public void setIncomingPort(String incomingPort) {
        this.incomingPort = incomingPort;
    }

    public POP3Folder getPop3Folder() {
        return pop3Folder;
    }
}
