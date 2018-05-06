/**
 * Created by Bogdan on 4/27/2018.
 * Updated by Catalin on 5/6/2018.
 */
import java.io.PrintStream;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.DataInputStream;

import java.sql.*;

public class Receiver {
    private Database db = new Database();

    public void updateLocalDatabase(){
        // Declare the JDBC objects.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Establish the connection.
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","CATALINPIRVU","Jocwowmult*123");

            // Create and execute an SQL statement that returns some data.
            String SQL = "SELECT * from Users";
            stmt = con.createStatement();
            System.out.println("fine");
            rs = stmt.executeQuery(SQL);

            // Iterate through the data in the result set and display it.
            while (rs.next()) {
                //System.out.println( rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4)+rs.getString(5));
                db.setMember(rs.getString(2),rs.getString(4),rs.getString(1),rs.getString(3),rs.getString(5));
            }
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (rs != null) try { rs.close(); } catch(Exception e) {}
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (con != null) try { con.close(); } catch(Exception e) {}
        }
    }


    public void insertUser(String Username, String Password, String Email, String Licence, String Token){
        // Declare the JDBC objects.
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            // Establish the connection.
            Class.forName("oracle.jdbc.driver.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","CATALINPIRVU","Jocwowmult*123");

            // Create and execute an SQL statement that returns some data.
            String SQL = "INSERT INTO USERS VALUES ('"+Email+"', '"+Username+"', '"+Licence+"', '"+Password+"', '"+Token+"')";
            stmt = con.createStatement();
            System.out.println("fine");
            rs = stmt.executeQuery(SQL);

        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (stmt != null) try { stmt.close(); } catch(Exception e) {}
            if (con != null) try { con.close(); } catch(Exception e) {}
            if (rs != null) try { rs.close(); } catch(Exception e) {}
        }
       
    }


    String token = "";

    public void getMessage() {
        Member member = new Member();
        String msg_received = "";
        String[] separated = new String[5];
        try {
            /* Open the server socket on an open port*/
            ServerSocket socket = new ServerSocket(2727);
            System.out.println("Opened Socket");
            System.out.println(token);
            /* Get a client socket to listen to incoming connections */
            Socket clientSocket = socket.accept();       //This is blocking. It will wait.
            /*Get the data received from the socket*/
            DataInputStream DIS = new DataInputStream(clientSocket.getInputStream());
            msg_received = DIS.readUTF();

            // when someone tries to log in
            // TODO: send true/false back to the android application
            if (msg_received.charAt(0) == 'l') {
                PrintStream DOS = new PrintStream(clientSocket.getOutputStream());

                try {
                    separated = msg_received.split("!");
                } catch (Exception e) {
                    System.out.println("Separation error");
                }
                if ( db.searchForUser(separated[1], separated[2]) ){
                    DOS.println("true"); // he can log in
                }
                else {
                    DOS.println("false"); // username/password invalid
                }
            }

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
                    insertUser(separated[1], separated[2], separated[3], separated[4], token);
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
        rec.updateLocalDatabase();
        // TODO: this server can only handle an user connecting at a given time; do some multithreading;
        while (true) {
            rec.getMessage();
        }
    }
}
