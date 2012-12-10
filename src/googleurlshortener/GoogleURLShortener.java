/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googleurlshortener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author javier
 */
public class GoogleURLShortener {

    /**
     * @param args the command line arguments
     */
    private String status;
    private String id;
    private String url;
    private DefaultHttpClient httpclient = new DefaultHttpClient();
    private HttpPost httpPost = new HttpPost("https://www.googleapis.com/urlshortener/v1/url");

    public static void main(String[] args) throws IOException, JSONException {
        GoogleURLShortener gus = new GoogleURLShortener();
        gus.calculateID("http://google.com");
        System.out.println(gus.getId());

        GoogleURLShortener gus2 = new GoogleURLShortener("https://www.facebook.com/nycjv321");
        gus2.calculateID();
        System.out.println(gus2.getId());

    }

    GoogleURLShortener(String url) {
        this.url = url;
    }

    GoogleURLShortener() {
    }

    public void calculateID() throws IOException, JSONException {


        // Define HTTP Post Entity        
        StringEntity stringentity = new StringEntity(
                "{\"longUrl\": \"" + this.url + "\"}",
                ContentType.create("application/json", "UTF-8"));
        // Set Entity in Post Object
        this.httpPost.setEntity(stringentity);
        // Perform Post
        HttpResponse response2 = this.httpclient.execute(this.httpPost);
        // Get Status of Post
        this.setStatus(String.valueOf(response2.getStatusLine()));
        // Get Output Stream
        HttpEntity httpentity = response2.getEntity();

        String output = EntityUtils.toString(httpentity);

        // Lets use JSON Library to interact with String (Lazy Much?)
        JSONObject jsonobject = new JSONObject(output);
        // Lets save the field
        this.setId(jsonobject.getString("id"));
        // Close Stream
        EntityUtils.consume(httpentity);
        // Disconnect
        this.httpPost.releaseConnection();
    }

    public void calculateID(String url) throws IOException, JSONException {
        this.url = url;
        this.calculateID();
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
