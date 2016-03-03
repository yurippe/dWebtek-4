package dk.teamrisk.Easy;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * One object to take care of everything HTTP!
 */
public class EasyHTTP {

    //The URL to which to be sent.
    private URL url;

    /**
     * Constructor, with URL specified by URL object.
     * @param url The url to send to.
     */
    public EasyHTTP(URL url) {
        this.url = url;
    }

    /**
     * Constructor, with URL specified by a String.
     * @param url
     */
    public EasyHTTP(String url) {
        try {
            this.url = new URL(url);
        } catch (MalformedURLException e) {e.printStackTrace();} //This shouldn't happen goddamn it
    }

    /**
     * postHTTP for
     * @param body The body for the response
     * @return EasyResponse making it possible to read the response from this request.
     */
    public EasyResponse postHTTP(String body){
        Map<String, String> headers = new HashMap<>();
        //Use xml as default headers
        headers.put("Content-Type", "text/xml");
        return postHTTP(body, headers);
    }

    /**
     * Fully modifiable postHTTP request
     * @param body The body to be sent with the request
     * @param headers A String map containing all the parameters
     * @return EasyResponse making it possible to read the response from this request.
     */
    public EasyResponse postHTTP(String body, Map<String, String> headers){
        EasyResponse response = new EasyResponse();
        response.setRequestMethod("POST");

        HttpURLConnection connection = null;

        //Get a connection object, return null if error
        try {
            connection = (HttpURLConnection) this.url.openConnection();
        } catch (IOException e) {
            connection.disconnect();
            return response.setErrorMessage("Could not establish connection").setFatalException(e);
        }

        connection.setDoInput(true);
        connection.setDoOutput(true);

        //Set request method to POST
        try {
            connection.setRequestMethod("POST");
        } catch (ProtocolException e) {
            connection.disconnect();
            return response.setErrorMessage("POST not a valid request method (wat?").setFatalException(e);
        }

        //Set headers
        for(String k : headers.keySet()){
            connection.setRequestProperty(k, headers.get(k));
        }

        //Write the body of the post request
        try {
            DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
            writer.write(body.getBytes());
            writer.flush();
            writer.close();
        } catch (Exception e){
            connection.disconnect();
            return response.setErrorMessage("Error writing post body to server").setFatalException(e);
        }

        //Check status code
        try {
            if(connection.getResponseCode() != 200){
                response.setErrorMessage("Wrong status code").setResponseCode(connection.getResponseCode());
            } else {
                response.setResponseCode(200);
            }
        } catch (Exception e){
            connection.disconnect();
            return response.setErrorMessage("Stream unexpectedly closed").setFatalException(e);
        }

        //Read response body
        try {
            InputStream inp = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            while (true) {
                int i = inp.read();         //Get the next character from the server.
                if (i == -1) break;         //If we get a -1, we're finished with reading.
                buffer.append((char) i);    //Append said character.
            }
            response.setResponse(buffer.toString());
            connection.disconnect();
            return response;
        } catch (IOException e) {
            connection.disconnect();
            return response.setErrorMessage("Could not read response").setFatalException(e);
        }
    }

    /**
     * A simple getHTTP request using no parameters.
     * @return EasyReponse making it possible to read the response.
     */
    public EasyResponse getHTTP(){
        return getHTTP(new HashMap<String, String>());
    }

    /**
     * A fully customizable getHTTP request with parameters
     * @param headers A map of String
     * @return EasyReponse making it possible to read the response.
     */
    public EasyResponse getHTTP(Map<String, String> headers){
        EasyResponse response = new EasyResponse();

        HttpURLConnection connection = null;

        //Get a connection object, return null if error
        try {
            connection = (HttpURLConnection) this.url.openConnection();
        } catch (IOException e) {
            connection.disconnect();
            return response.setErrorMessage("Could not establish connection").setFatalException(e);
        }

        //Set headers
        for(String k : headers.keySet()){
            connection.setRequestProperty(k, headers.get(k));
        }

        //Check status code
        try {
            if(connection.getResponseCode() != 200){
                response.setErrorMessage("Wrong status code").setResponseCode(connection.getResponseCode());
            } else {
                response.setResponseCode(200);
            }
        } catch (Exception e){
            connection.disconnect();
            return response.setErrorMessage("Stream unexpectedly closed").setFatalException(e);
        }

        //Read response body
        try {
            InputStream inp = connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            while (true) {
                int i = inp.read();         //Get the next character from the server.
                if (i == -1) break;         //If we get a -1, we're finished with reading.
                buffer.append((char) i);    //Append said character.
            }
            connection.disconnect();
            return response.setResponse(buffer.toString());
        } catch (IOException e) {
            connection.disconnect();
            return response.setErrorMessage("Could not read response").setFatalException(e);
        }
    }
}
