import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;

import java.io.IOException;

public class Pusher {

    public static void send(String modifier, String token) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://fcm.googleapis.com/fcm/send");
        post.setHeader("Content-type", "application/json");
        // Server key (Firebase)
        post.setHeader("Authorization", "key=AAAAFYWN4_0:APA91bGA2f7Rvae4JcvJcsdqKoTvWnquVVp3KE5BAWZQ4tC5BCN2xRqOPqMZvGH4XAaz2d67SHqKzEshlfv8yrNMvg52jhmE8MiGshzTKOXETWBzdHP9OsI728lBd6YnXqFFhcPK3bxO");

        JSONObject message = new JSONObject();
        // Token - where to send the notification
        message.put("to", token);
        message.put("priority", "high");

        JSONObject notification = new JSONObject();
        if (modifier == "refill") {
            notification.put("title", "Refill notification");
            notification.put("body", "Hello! Would you like to refill?");
        }
        if(modifier == "leaving"){
            notification.put("title", "Leaving notification");
            notification.put("body", "Thank you for choosing us. Safe travel!");
        }
        message.put("notification", notification);

        post.setEntity(new StringEntity(message.toString(), "UTF-8"));
        HttpResponse response = client.execute(post);
        System.out.println(response);
        System.out.println(message);
    }
}
