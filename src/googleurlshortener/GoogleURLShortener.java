/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package googleurlshortener;

import java.io.IOException;
import org.json.JSONException;
import org.json.JSONObject;
import utility.system.HttpClientUtility;

/**
 *
 * @author javier
 */
public class GoogleURLShortener {

    /**
     * @param args the command line arguments
     */
    private String id;
    private String url;
    private String googleURL;
    private HttpClientUtility httpclient;

    public static void main(String[] args) throws IOException, JSONException {
        GoogleURLShortener gus = new GoogleURLShortener();


        gus.calculateID("http://google.com");
        System.out.println(gus.getId());

        GoogleURLShortener gus2 = new GoogleURLShortener("https://www.facebook.com/nycjv321");
        gus2.calculateID();
        System.out.println(gus2.getUrl());
        gus2.setId("http://goo.gl/fbsS");
        gus2.calculateURL();
        System.out.println(gus2.getUrl());


    }

    GoogleURLShortener(String url) {
        this.init();
        this.url = url;
    }

    GoogleURLShortener() {
        this.init();
    }

    public void init() {
        this.googleURL = "https://www.googleapis.com/urlshortener/v1/url";
        this.httpclient = new HttpClientUtility();
    }
    
    public void calculateID() throws IOException, JSONException {


        String output = httpclient.performPost(
                this.getGoogleURL(),
                "{\"longUrl\": \"" + this.getUrl() + "\"}", "application/json",
                "UTF-8");


        // Lets use JSON Library to interact with String (Lazy Much?)
        JSONObject jsonobject = new JSONObject(output);
        // Lets save the field
        this.setId(jsonobject.getString("id"));
    }

    public void calculateID(String url) throws IOException, JSONException {
        this.setUrl(url);
        this.calculateID();
    }

    public void calculateURL() throws IOException, JSONException {



        String output = httpclient.performGet(this.googleURL + "?shortUrl=" + this.getId());


        // Lets use JSON Library to interact with String (Lazy Much?)
        JSONObject jsonobject = new JSONObject(output);
        // Lets save the field
        this.setUrl(jsonobject.getString("longUrl"));
    }

    public void calculateURL(String id) throws IOException, JSONException {
        this.setId(id);
        this.calculateURL();
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
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

    /**
     * @return the googleURL
     */
    public String getGoogleURL() {
        return googleURL;
    }
}
