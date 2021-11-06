/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author Okami
 */
public class Message implements Serializable {

    private String message;
    private int messageInt;
    private Object object;

    public Message() {
    }

    public Message(String message, Object object, int mess) {
        this.message = message;
        this.messageInt = mess;
        this.object = object;
    }

    public Message(String message, Object object) {
        this.message = message;
        this.object = object;
    }

    public Message(String message, int messInt) {
        this.message = message;
        this.messageInt = messInt;
    }

    public int getMessageInt() {
        return messageInt;
    }

    public void setMessageInt(int messageInt) {
        this.messageInt = messageInt;
    }

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
