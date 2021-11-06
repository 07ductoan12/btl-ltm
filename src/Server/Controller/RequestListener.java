/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Server.Controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Server.View.ServerView;
import Server.DAO.NguoiChoiDAO;
import Server.DAO.NguoiChoiVanChoiDAO;
import Server.DAO.TranDauDAO;
import Server.DAO.VanChoiDAO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

/**
 *
 * @author toan
 */
public class RequestListener extends Thread {

    private Socket socket;
    private NguoiChoi currentUser;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private TranDau tranDau;
    private ServerView view;
    private NguoiChoiDAO userDAO;
    private NguoiChoiVanChoiDAO ncvcDAO;
    private VanChoiDAO vanChoiDAO;
    private TranDauDAO tranDauDAO;
    HashMap<NguoiChoi, Socket> listSocket;
    HashMap<Integer, ObjectInputStream> listOis;
    HashMap<Integer, ObjectOutputStream> listOos;
    static HashMap<Integer, String> listNuocChoi;
    private Message request;
    ArrayList<TranDau> listTranDau;
    ArrayList<Phong> listPhong;

    public RequestListener(Socket socket, HashMap<Integer, ObjectInputStream> listOis,
            HashMap<Integer, ObjectOutputStream> listOos, HashMap<NguoiChoi, Socket> listSocket, ArrayList<TranDau> tranDaus, ArrayList<Phong> listPhong)
            throws IOException {
        this.listSocket = listSocket;
        this.listOis = listOis;
        this.listOos = listOos;
        this.listTranDau = tranDaus;
        this.listPhong = listPhong;
        listNuocChoi = new HashMap<>();
        listNuocChoi.put(0, "Không chọn");
        listNuocChoi.put(1, "Kéo");
        listNuocChoi.put(2, "Búa");
        listNuocChoi.put(3, "Bao");
        this.userDAO = new NguoiChoiDAO();
        this.tranDauDAO = new TranDauDAO();
        this.vanChoiDAO = new VanChoiDAO();
        this.ncvcDAO = new NguoiChoiVanChoiDAO();
        System.out.println("Start listen ...");
        view = new ServerView();
        this.socket = socket;
        ois = new ObjectInputStream(this.socket.getInputStream());
        oos = new ObjectOutputStream(this.socket.getOutputStream());
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.request = (Message) ois.readObject();
                switch (request.getMessage()) {
                    case "login": {
                        FunctionLogin();
                        break;
                    }
                    case "Get list user online": {
                        FunctionPlayerOnl();
                        break;
                    }
                    case "Challenge to": {
                        FunctionChallenge();
                        break;
                    }
                    case "Accept": {
                        FunctionAccept();
                        break;
                    }
                    case "Decline": {
                        FunctionDecline();
                        break;
                    }
                    case "Send turn result": {
                        FunctionSendResult();
                        break;
                    }
                    case "Get result turn": {
                        FunctionGetResultTurn();
                        break;
                    }
                    case "Get result game": {
                        FunctionGetGame();
                        break;
                    }
                    case "Tao phong": {
                        FuncTaoPhong();
                        break;
                    }
                    case "vao phong": {
                        FuncVaoPhong();
                        break;
                    }
                    case "Close game": {
                        FunctionCloseGame();
                        break;
                    }
                    case "start game": {
                        FuncStartGame();
                        break;
                    }
                    case "san sang": {
                        FuncSS();
                        break;
                    }
                }
            } catch (Exception ex) {
                listSocket.remove(currentUser);
                listOis.remove(currentUser.getId());
                listOos.remove(currentUser.getId());
                currentUser.setUserStatus(0);
                userDAO.updateStatus(currentUser);
                FunctionPlayerOnl();
                view.log(currentUser.getUsername() + "disconnect ...");
                ex.printStackTrace();
                break;
            }
        }
    }

    private void FunctionLogin() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        view.log("Nguoi dung login");
        NguoiChoi user = (NguoiChoi) request.getObject();
        view.log("Login: " + user.getUsername() + user.getPassword());
        NguoiChoi result = userDAO.checkLogin(user);
        Message response = new Message();
        if (result != null) {
            if (listSocket.get(result) != null) {
                response.setMessage("Login fail");
                response.setObject("Tài khoản đang đăng nhập ở nơi khác!!!");
            } else {
                result.setUserStatus(1);
                listSocket.put(result, socket);
                listOis.put(result.getId(), ois);
                listOos.put(result.getId(), oos);
                this.currentUser = result;
                userDAO.updateStatus(result);
                response.setMessage("Login success");
                response.setObject(result);
            }
        } else {
            view.log("Login fail");
            response.setMessage("Login fail");
            response.setObject("Tai khoan hoac mat khau ko dung");
        }
        oos.writeObject(response);
        if (response.getMessage().equals("Login success")) {
            FunctionPlayerOnl();
            FunctionGetListPhong();
        }
    }

    public void FunctionGetListPhong() {
        //ArrayList<NguoiChoi> listUserOnl = userDAO.getListUser(0);
//        for (int i = 0; i < this.listPhong.size(); i++) {
//            if (this.listPhong.get(i).getDsng().isEmpty()) {
//                this.listPhong.remove(i);
//            }
//        } 
        Set<Integer> keys = listOos.keySet();

        keys.forEach(i -> {
            try {
                //listOos.get(i).writeObject(new Message("List phong",phongList));
                Message message = new Message("List phong");
                message.setObject(listPhong);
                ArrayList<Phong> phongList = (ArrayList<Phong>) message.getObject();
                message.setMessageInt(phongList.size());
                System.out.println("size mess " + phongList.size());
                listOos.get(i).writeObject(message);
            } catch (IOException ex) {
                Logger.getLogger(RequestListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void FunctionPlayerOnl() {
        ArrayList<NguoiChoi> listUserOnl = userDAO.getListUser(0);
        Set<Integer> keys = listOos.keySet();
        keys.forEach(i -> {
            try {
                listOos.get(i).writeObject(new Message("List user online", listUserOnl));
            } catch (IOException ex) {
                Logger.getLogger(RequestListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //writeObject(new Message("List user online", listUserOnl));
    }

    private void FunctionChallenge() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        NguoiChoi competitor = (NguoiChoi) this.request.getObject();
        view.log(this.currentUser.getUsername() + "challenge to" + competitor.getUsername());
        System.out.println(competitor.getId());
        listOos.get(competitor.getId()).writeObject(new Message("Challenge from", currentUser));
    }

    private void FunctionAccept() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        NguoiChoi competitor = (NguoiChoi) this.request.getObject();
        view.log(this.currentUser.getUsername() + " accept challenge to " + competitor.getUsername());
        initGame(currentUser, competitor);
        Message response = new Message("Start game", tranDau);
        listOos.get(competitor.getId()).writeObject(response);
        listOos.get(this.currentUser.getId()).writeObject(response);
        FunctionPlayerOnl();
    }

    private void FunctionDecline() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        NguoiChoi competitor = (NguoiChoi) request.getObject();
        view.log(this.currentUser.getUsername() + " decline challenge to " + competitor.getUsername());
        Message reponse = new Message("Decline", competitor.getUsername() + " decline");
        listOos.get(competitor.getId()).writeObject(reponse);
    }

    private void FunctionSendResult() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        NguoiChoiVanChoi ncvc = (NguoiChoiVanChoi) request.getObject();
        view.log(ncvc.getNguoiChoi().getUsername() + " choose: " + listNuocChoi.get(ncvc.getNuocChoi()));
        ncvcDAO.updateNCVC(ncvc);
    }

    private void FunctionGetResultTurn() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.\
        NguoiChoiVanChoi ncvc = (NguoiChoiVanChoi) request.getObject();
        String rs = ncvcDAO.getResultTurn(ncvc.getVanChoi());
        view.log("Result: " + ncvc.getNguoiChoi().getUsername() + " " + rs);
        Message response = new Message();
        response.setMessage("Result turn");
        response.setObject(rs);
        oos.writeObject(response);
    }

    private void FunctionGetGame() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        TranDau td = (TranDau) request.getObject();
        String rs = ncvcDAO.getResultGame(td);
        view.log(currentUser.getUsername() + " get result game: " + rs);
        Message response = new Message();
        response.setMessage("Result game");
//        userDAO.updateUserStatus(currentUser.getId(), 1);
        response.setObject(rs);
        oos.writeObject(response);
    }

    public void initGame(NguoiChoi user1, NguoiChoi user2) {

        tranDau = tranDauDAO.createTranDau();
        tranDau.setIdUser(user1.getId(), user2.getId());
        user1.setUserStatus(2);
        userDAO.updateStatus(currentUser);
        user2.setUserStatus(2);
        userDAO.updateStatus(user2);
        ArrayList<VanChoi> listVanChoi = new ArrayList<>();

        for (int i = 1; i <= 3; i++) {
            VanChoi vanChoi = vanChoiDAO.createTurn(tranDau.getId());
            NguoiChoiVanChoi ncvc1 = ncvcDAO.createNCVC(user1.getId(), vanChoi.getId());
            ncvc1.setNguoiChoi(user1);
            ncvc1.setVanChoi(vanChoi);
            NguoiChoiVanChoi ncvc2 = ncvcDAO.createNCVC(user2.getId(), vanChoi.getId());
            ncvc2.setNguoiChoi(user2);
            ncvc2.setVanChoi(vanChoi);
            vanChoi.setNcvc1(ncvc1);
            vanChoi.setNcvc2(ncvc2);
            listVanChoi.add(vanChoi);
        }

        tranDau.setListVanChoi(listVanChoi);
        listTranDau.add(tranDau);
    }

    private void FunctionCloseGame() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Server.Controller.RequestListener.FunctionCloseGame()");
        int idTranDau = (Integer) request.getMessageInt();
        int i = 0;
        if (!listTranDau.isEmpty()) {
            for (; i < listTranDau.size(); i++) {
                if (listTranDau.get(i).getId() == idTranDau) {
                    userDAO.updateUserStatus(listTranDau.get(i).getIdUser1(), 1);
                    userDAO.updateUserStatus(listTranDau.get(i).getIdUser2(), 1);
                    System.out.println(listTranDau.get(i).getIdUser1());
                    System.out.println(listTranDau.get(i).getIdUser2());
                    listOos.get(listTranDau.get(i).getIdUser1()).writeObject(new Message("Close game"));
                    listOos.get(listTranDau.get(i).getIdUser2()).writeObject(new Message("Close game"));
                    break;
                }
            }
            listTranDau.remove(i);
        }
        FunctionPlayerOnl();
    }

    private void FuncTaoPhong() throws IOException, InterruptedException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("Tao Phong server");
        ArrayList<NguoiChoi> dsng = new ArrayList<>(2);
        NguoiChoi nc = (NguoiChoi) request.getObject();
        dsng.add(nc);
        Phong phong = new Phong(dsng);
        oos.writeObject(new Message("Phong duoc tao", phong));
        this.listPhong.add(phong);
        System.out.println("size list phong sau add " + listPhong.size());
        FunctionGetListPhong();
    }

    private void FuncVaoPhong() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        System.out.println("vao phong server");
        NguoiChoi nc = (NguoiChoi) request.getObject();
        System.out.println(nc.getFullName());
        int idPhong = (Integer) request.getMessageInt();
        for (Phong phong : listPhong) {
            if (phong.getId() == idPhong) {
                ArrayList<NguoiChoi> listUser = new ArrayList<>(2);
                listUser.add(phong.getDsng().get(0));
                listUser.add(nc);
                phong.setDsng(listUser);
                System.out.println(phong.getDsng().size());
                listOos.get(phong.getDsng().get(0).getId()).writeObject(new Message("update listUser", listUser));
                oos.writeObject(new Message("Phong duoc tao", phong));
                listOos.get(phong.getDsng().get(1).getId()).writeObject(new Message("update listUser", listUser));
                break;
            }
        }
    }

    private void FuncSS() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int idPhong = (Integer) request.getMessageInt();
        //int idNc = (Integer)request.getObject();
        NguoiChoi nc = (NguoiChoi) request.getObject();
        for (Phong phong : listPhong) {
            if (phong.getId() == idPhong) {
                if (phong.getDsng().get(0).getId() == nc.getId()) {
                    if (phong.getUserStatus1() == 0) {
                        phong.setUserStatus1(1);
                    } else {
                        phong.setUserStatus1(0);
                    }
                } else {
                    if (phong.getUserStatus2() == 0) {
                        phong.setUserStatus2(1);
                    } else {
                        phong.setUserStatus2(0);
                    }
                }
                if (phong.getDsng().size() == 2) {
                    if (nc.getId() == phong.getDsng().get(0).getId()) {
                        oos.writeObject(new Message("update status", nc.getId(), phong.getUserStatus1()));
                        listOos.get(phong.getDsng().get(1).getId()).writeObject(new Message("update status", nc.getId(), phong.getUserStatus1()));
                    } else {
                        oos.writeObject(new Message("update status", nc.getId(), phong.getUserStatus2()));
                        listOos.get(phong.getDsng().get(0).getId()).writeObject(new Message("update status", nc.getId(), phong.getUserStatus2()));
                    }
                } else {
                    oos.writeObject(new Message("update status", nc.getId(), phong.getUserStatus1()));
                }
                break;
            }
        }
    }

    private void FuncStartGame() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int idphong = (Integer) request.getMessageInt();
        for (Phong phong : listPhong) {
            if (phong.getId() == idphong) {
                NguoiChoi nc1 = phong.getDsng().get(0);
                NguoiChoi nc2 = phong.getDsng().get(1);
                initGame(nc1, nc2);
                Message response = new Message("Start game", tranDau);
                listOos.get(nc1.getId()).writeObject(response);
                listOos.get(nc2.getId()).writeObject(response);
                FunctionPlayerOnl();
                break;
            }
        }
    }
}
