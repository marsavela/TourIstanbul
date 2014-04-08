package lbs.erasmus.touristanbul;

import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import org.json.JSONObject;

/**
 * Created by David on 08/04/2014.
 */

/*
Thread safe singleton wrapper for Bing oauth token
*/
public class BingOauth {
    private static final int oauthTimeout = 10000;
    private static final String oauthEndpoint = "/v2/OAuth2-13/";
    private static final String oauthServerName = "datamarket.accesscontrol.windows.net";
    private static final String clientId = "erasmus-lbs-spain";
    private static final String clientSecret = "eVRrtV6YIJNNqVxdbyobEPZQJI+dUoXd9O6C56TU2Ok=";
    private static final String oauthScope = "http://api.microsofttranslator.com";
    private static final String oauthGrantType = "client_credentials";

    private static String oauthToken = null;

    /**
     * Thread safe lazy initializer for oauth token
     */
    public static String getOauthToken() {
        if (oauthToken == null) {
            synchronized (BingOauth.class) {
                if (oauthToken == null) {
                    oauthToken = processOauthToken();
                }
            }
        }
        return oauthToken;
    }

    public static String getStringByScanner(InputStreamReader inputStream){
        return new Scanner((java.nio.channels.ReadableByteChannel) inputStream,"UTF-8").next();
    }

    private static String processOauthToken() {
        String token = "";
        try {
            // Create request
            final HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, oauthTimeout);
            HttpClient client = new DefaultHttpClient(httpParams);
            HttpPost post = new HttpPost(getOauthUri());
            post.setEntity(getOauthBody());

            // Parse response
            HttpResponse response = client.execute(post);
            InputStreamReader reader = new InputStreamReader(response.getEntity().getContent(), Charset.defaultCharset());

            //String responseBody = CharStreams.toString(reader);
            String responseBody = getStringByScanner(reader);

            token = (new JSONObject(responseBody)).getString("access_token");
        } catch (Exception e) {
            System.out.println(String.format("Error processing oauth token:\n%s", e.toString()));
            e.printStackTrace();
        }
        return token;
    }

    private static URI getOauthUri() throws Exception {
        return new URI("https", oauthServerName, oauthEndpoint, null);
    }

    private static UrlEncodedFormEntity getOauthBody() throws Exception {
        List<NameValuePair> nameValuePairs =  new ArrayList<NameValuePair>(4);
        nameValuePairs.add(new BasicNameValuePair("client_id", clientId));
        nameValuePairs.add(new BasicNameValuePair("client_secret", clientSecret));
        nameValuePairs.add(new BasicNameValuePair("scope", oauthScope));
        nameValuePairs.add(new BasicNameValuePair("grant_type", oauthGrantType));
        return new UrlEncodedFormEntity(nameValuePairs);
    }
}