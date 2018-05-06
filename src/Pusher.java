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
        post.setHeader("Authorization", "key=AAAAqM3PkjE:APA91bHU21VXuRedSVEjfw-Udy_N6ztAhGTQqIiRdDV_AHLTjX0B3XPC7jcuRbKS-TlHu6J0zcebkPM0QBfpSFpqDMpLnhUhYIrVZyvygvNt1c2zNmU6ugf1OaQb8Ltv11wEeLfgVIT_");

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
