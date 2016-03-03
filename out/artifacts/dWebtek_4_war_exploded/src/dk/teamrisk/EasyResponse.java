package dk.teamrisk;

/**
 * One object to take care of saving responses for later retrieval
 */
public class EasyResponse {

    //Response code. Is -1 as an error code for nothing found yet
    private int responseCode;

    //Errors are handled by saving them and returning them.
    private String errorMessage;
    private Exception fatalException;

    //The type of the request.
    private String requestMethod;

    //A string containing all the response
    private String response;

    /**
     * Constructor...
     */
    public EasyResponse(){
        responseCode = -1;
        errorMessage = "";
        fatalException = null;
        requestMethod = "GET";
        response = "";
    }

    /**
     * Check if the responseCode was 200 and no exception was caught.
     * @return If everything is cool
     */
    public boolean wasSuccessful(){
        if(this.responseCode == 200 && this.fatalException == null){
            return true;
        } else{
            return false;
        }
    }

    public int getResponseCode(){return this.responseCode;}
    public EasyResponse setResponseCode(int responseCode){this.responseCode = responseCode; return this;}

    public String getResponse(){return this.response;}
    public EasyResponse setResponse(String response){this.response = response; return this;}

    public String getErrorMessage(){return this.errorMessage;}
    public EasyResponse setErrorMessage(String errorMessage){this.errorMessage = errorMessage; return this;}

    public Exception getFatalException(){return this.fatalException;}
    public EasyResponse setFatalException(Exception fatalException){this.fatalException = fatalException; return this;}

    public String getRequestMethod(){return this.requestMethod;}
    public EasyResponse setRequestMethod(String requestMethod) {this.requestMethod = requestMethod; return this;}
}
