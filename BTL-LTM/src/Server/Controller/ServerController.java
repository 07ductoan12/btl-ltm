 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.Controller;

import Server.DAO.NguoiChoiDAO;
import Server.DAO.NguoiChoiVanChoiDAO;
import Server.DAO.TranDauDAO;
import Server.DAO.VanChoiDAO;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import Server.View.ServerView;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 *
 * @author toan
 */
public class ServerController {

    private static final int port = 8000;
    ServerView view;

    public ServerController() {
        view = new ServerView();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        view.log("Server is running ....");
        HashMap<NguoiChoi, Socket> listSocket = new HashMap<>();
        HashMap<Integer, ObjectInputStream> listOis = new HashMap<>();
        HashMap<Integer, ObjectOutputStream> listOos = new HashMap<>();
        ArrayList<TranDau> tranDaus = new ArrayList<>();
        ArrayList<Phong> listPhong = new ArrayList<>();
        while (true) {
            Socket socket = serverSocket.accept();
            view.log("Connecet success to: " + socket);
            RequestListener requestListener = new RequestListener(socket,listOis,listOos,listSocket,tranDaus,listPhong);
            requestListener.start();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerController serverController = new ServerController();
        serverController.start();
    }
}
