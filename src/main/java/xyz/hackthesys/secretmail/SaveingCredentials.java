package xyz.hackthesys.secretmail;

import java.io.*;

public class SaveingCredentials implements Serializable {
    @Serial
    private static final long serialVersionUID = -7183102836871064432L;
    private static SaveingCredentials instance;
    public static SaveingCredentials getInstance()
    {
        return instance == null ? (instance = new SaveingCredentials()) : instance;
    }

    public static void setInstance(SaveingCredentials instance) {
        SaveingCredentials.instance = instance;
    }

    private String name, userId, email, PASS, inServer, outServer;
    private int inServerPort, outServerPort;


    public String getInServer() {
        return inServer;
    }

    public int getInServerPort() {
        return inServerPort;
    }

    public String getOutServer() {
        return outServer;
    }

    public int getOutServerPort() {
        return outServerPort;
    }

    public static void loadCredentials(File pCredentials)
    {
        try (FileInputStream file = new FileInputStream(pCredentials);
             ObjectInputStream objectFile = new ObjectInputStream(file)) {
            setInstance((SaveingCredentials) objectFile.readObject());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void getCredentials(String name, String userId ,String email, String PASS, String inServer, int inServerPort, String outServer, int outServerPort) throws IOException {

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("credentials.hts"));
        SaveingCredentials.getInstance().name = name;
        SaveingCredentials.getInstance().userId = userId;
        SaveingCredentials.getInstance().inServer = inServer;
        SaveingCredentials.getInstance().outServer = outServer;
        SaveingCredentials.getInstance().inServerPort = inServerPort;
        SaveingCredentials.getInstance().outServerPort = outServerPort;
        /*
        SaveingCredentials.getInstance().email = email;
        SaveingCredentials.getInstance().PASS = PASS;

        SaveingCredentials.getInstance().inServer = inServer;
        SaveingCredentials.getInstance().inServerPort = inServerPort;

        SaveingCredentials.getInstance().outServer = outServer;
        SaveingCredentials.getInstance().outServerPort = outServerPort;
        */

        out.writeObject(SaveingCredentials.getInstance());
        out.close();
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return email;
    }


    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
