/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.ControlListener;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import model.Message;
import model.NguoiChoi;

/**
 *
 * @author toan
 */
public class Connection {

    private Socket socket;
    private static final int port = 8000;
    private static final String hostname = "127.0.0.1";

    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private NguoiChoi CurrentUser;
    private NguoiChoi competitorUser;

    public Connection() {
        super();
        try {
            socket = new Socket(hostname, port);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Khong the ket noi den server");
        }
    }
    public void SendRequest(Message request){
        try{
            oos.writeObject(request);
            oos.flush();
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("khong ket noi dc den may chu");
        }
    }
    public Message receiverResponse(){
        try{
           synchronized (ois){
               return (Message) ois.readObject();
           }
        } catch(Exception ex){
            ex.printStackTrace();
            return new Message("Fail");
        }
    }
}
