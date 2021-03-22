/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rt0803;

import java.util.HashMap;

/**
 *
 * @author DELL
 */
public class Message {

    private int senderID;
    private int receiverID;
    private int message;
    private HashMap<String, String> params;

    public Message() {
    }

    public Message(int senderID, int receiverID, int message) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
    }

    public Message(int senderID, int receiverID, int message, HashMap params) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.message = message;
        this.params = params;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }

    public int getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(int receiverID) {
        this.receiverID = receiverID;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "Message{" + "senderID=" + senderID + ", receiverID=" + 
                receiverID + ", message=" + message + ", params=" + params + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Message other = (Message) obj;
        if (this.receiverID != other.receiverID) {
            return false;
        }
        return true;
    }
    
    
    
    
    
}
