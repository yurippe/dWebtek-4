package dk.teamrisk;

import java.util.Objects;

/**
 * One object to take care of saving responses for later retrieval
 */
public class EasyXMLResponse {

    //Error messages to be saved
    private String errorMessage;
    private Exception fatalException;

    private Object obj;

    //The response
    private String response;

    /**
     * Basic constructor
     */
    public EasyXMLResponse (){
        errorMessage = "";
        fatalException = null;
        response = "";
    }

    /**
     * Constructor, for when you expect XML as an answer to HTTP.
     * @param easyResponse
     */
    public EasyXMLResponse(EasyResponse easyResponse){
        this.errorMessage = easyResponse.getErrorMessage();
        this.fatalException = easyResponse.getFatalException();
        this.response = easyResponse.getResponse();
    }

    /**
     * Copies the content from an EasyResponse object.
     * @param easyResponse The EasyResponse object to be copied.
     * @return This object
     */
    public EasyXMLResponse fromEasyResponse(EasyResponse easyResponse) {
        this.errorMessage = easyResponse.getErrorMessage();
        this.fatalException = easyResponse.getFatalException();
        this.response = easyResponse.getResponse();
        return this;
    }

    /**
     * Checks if no errors or exceptions have been made themselves noticed.
     * @return The happiness of the XML.
     */
    public boolean wasSuccessful(){
        if(this.errorMessage.equals("") && this.fatalException == null){
            return true;
        } else{
            return false;
        }
    }

    public String getResponse(){return this.response;}
    public EasyXMLResponse setResponse(String response){this.response = response; return this;}

    public String getErrorMessage(){return this.errorMessage;}
    public EasyXMLResponse setErrorMessage(String errorMessage){this.errorMessage = errorMessage; return this;}

    public Exception getFatalException(){return this.fatalException;}
    public EasyXMLResponse setFatalException(Exception fatalException){this.fatalException = fatalException; return this;}

    public Object getData(){return this.obj;}
    public EasyXMLResponse setData(Object data){this.obj = data; return this;}
}
