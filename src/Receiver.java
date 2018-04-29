/**
 * Created by Bogdan on 4/27/2018.
 */
import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;


public class Receiver {
    private Database db = new Database();
    String token = "";
    public void getMessage() {
        Member member = new Member();
        String msg_received = "";
        String[] separated = new String[5];
        try {
            /* Open the server socket on an open port*/
            ServerSocket socket = new ServerSocket(1755);
            System.out.println("Opened socket");
            System.out.println(token);
            /* Get a client socket to listen to incoming connections */
            Socket clientSocket = socket.accept();       //This is blocking. It will wait.
            /*Get the data received from the socket*/
            DataInputStream DIS = new DataInputStream(clientSocket.getInputStream());
            msg_received = DIS.readUTF();
            if (msg_received.charAt(0) == 't') {
                token = msg_received.substring(5);
            }
            if (msg_received.charAt(0) == 'A') {
                try {
                    separated = msg_received.split("!");
                } catch (Exception e) {
                    System.out.println("Separation error");
                }

                //member.setMember(separated[1], separated[2], separated[3], separated[4]);
                try {
                    db.setMember(separated[1], separated[2], separated[3], separated[4], token);
                } catch (Exception e) {
                    System.out.println("Database error");
                }
            }
            // when new car comes to the station
            if (msg_received.charAt(0) == 'D') {
                String licence = msg_received.substring(1);
                System.out.println(licence);
                String result = db.searchForLicence(licence);
                if (result.equalsIgnoreCase("NULLTOKEN")) {
                    System.out.println("The user is not registered");
                }
                else {
                    // DONE implement pusher app service
                    Pusher.send("refill",result);
                    System.out.println("Sent my refill notification");
                }
            }
            // when the car is leaving the station
            if (msg_received.charAt(0) == 'L') {
                String licence = msg_received.substring(1);
                System.out.println(licence);
                String result = db.searchForLicence(licence);
                if (result.equalsIgnoreCase("NULLTOKEN")) {
                    System.out.println("The user is not registered");
                }
                else {
                    // DONE implement pusher app service
                    Pusher.send("leaving",result);
                    System.out.println("Sent my leaving notification");
                }
            }

            /* Close the connection to the client*/
            clientSocket.close();
            /* Close the connection to the server*/
            socket.close();

        } catch (Exception e) {
            System.out.println("error socket");
        }
    }
    public static void main(String[] args) {
        Receiver rec = new Receiver();

        while (true) {
            rec.getMessage();
        }
    }
}
