package elec5620.sydney.edu.au.smarthealth;
/*
The code below is modiefied base on
the android chatbot tutorial on:
https://www.geeksforgeeks.org/how-to-create-a-chatbot-in-android-with-brainshop-api/
 */
public class MessageModal {

    // string to store our message and sender
    private String message;
    private String sender;

    // constructor.
    public MessageModal(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    // getter and setter methods.
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
