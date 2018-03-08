package br.com.rtools.perifericosws.utils;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONS {

    public static JSONObject get(String uri) throws Exception {
        // BufferedReader reader = null;
        try {
            return (JSONObject) new JSONTokener(IOUtils.toString(new URL(uri), "UTF-8")).nextValue();
        } catch (IOException e) {
            return null;
        }
    }

    public static JSONObject post(String uri) throws Exception {
        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://localhost:8080/RESTfulExample/rest/json/metallica/post");

            String input = "{\"singer\":\"Metallica\",\"title\":\"Fade To Black\"}";

            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, input);

            if (response.getStatus() != 201) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }

            System.out.println("Output from Server .... \n");
            String output = response.getEntity(String.class);
            System.out.println(output);

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

}
