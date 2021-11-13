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
                    case "Thoat Phong": {
                        funcThoatPhong();
                        break;
                    }
                    default: {
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
        Message message = new Message("List phong", this.listPhong);
        FuncSendAll(message);
    }

    public void addPhongToList(Phong phong) {
        Message message = new Message("add phong", phong);
        FuncSendAll(message);
    }

    public void FuncSendAll(Message message) {
        Set<Integer> keys = listOos.keySet();
        keys.forEach(i -> {
            try {
                listOos.get(i).writeObject(message);
            } catch (IOException ex) {
                Logger.getLogger(RequestListener.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private void FunctionPlayerOnl() {
        ArrayList<NguoiChoi> listUserOnl = userDAO.getListUser(0);
        Message message = new Message("List user online", listUserOnl);
        FuncSendAll(message);
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
        System.out.println("Nguoi choi: " + ncvc.getNguoiChoi().getUsername() + " " + ncvc.getNuocChoi());
        ncvcDAO.updateNCVC(ncvc);
    }

    private void FunctionGetResultTurn() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.\
        NguoiChoiVanChoi ncvc = (NguoiChoiVanChoi) request.getObject();
        String rs = ncvcDAO.getResultTurn(ncvc.getVanChoi());
        if ((Integer) request.getMessageInt() != 0) {
            System.out.println(rs);
            view.log("Result: " + ncvc.getNguoiChoi().getUsername() + " " + rs);
            Message response = new Message();
            response.setMessage("Result turn");
            response.setObject(rs);
            oos.writeObject(response);
        }
        sendDiemGame();
    }

    private void FunctionGetGame() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        TranDau td = (TranDau) request.getObject();
        String rs = ncvcDAO.getResultGame(td);
        view.log(currentUser.getUsername() + " get result game: " + rs);
        Message response = new Message();
        response.setMessage("Result game");
        userDAO.updateUserStatus(currentUser.getId(), 1);
        ArrayList<VanChoi> vanChois = vanChoiDAO.getVanChoiByTranDauId(td.getId());
        for (int i = 0; i < vanChois.size(); i++) {
            int diem = ncvcDAO.getDiemGameUser(vanChois.get(i), currentUser.getId());
            int diemOther = ncvcDAO.getDiemGameOtherUser(vanChois.get(i), currentUser.getId());
        }
        response.setObject(rs);
        oos.writeObject(response);
        FunctionPlayerOnl();
    }

    public void initGame(NguoiChoi user1, NguoiChoi user2) {

        this.tranDau = tranDauDAO.createTranDau();
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
        ArrayList<NguoiChoi> dsng = new ArrayList<>();
        dsng.add(currentUser);
        Phong phong = new Phong(dsng);
        oos.writeObject(new Message("Phong duoc tao", phong));
        this.listPhong.add(phong);
        addPhongToList(phong);
        System.out.println("size list phong sau add " + listPhong.size());
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
                listUser.add(currentUser);
                phong.setDsng(listUser);
                for (int i = 0; i < phong.getDsng().size(); i++) {
                    System.out.println("user name user in zoom " + phong.getDsng().get(i).getFullName());
                }
                System.out.println("size phong sau khi add user " + nc.getFullName() + " " + phong.getDsng().size());
                listOos.get(phong.getDsng().get(0).getId()).writeObject(new Message("update phong", listUser));
                oos.writeObject(new Message("Phong duoc tao", phong));
                oos.writeObject(new Message("update phong", listUser));
                sendFixPhong(phong);
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

    private void funcThoatPhong() throws IOException {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        int idPhong = (Integer) request.getMessageInt();
        int i = 0;
        for (; i < listPhong.size(); i++) {
            if (listPhong.get(i).getId() == idPhong) {
                ArrayList<NguoiChoi> listNc = listPhong.get(i).getDsng();
                int j = 0;
                for (; j < listNc.size(); j++) {
                    if (listNc.get(j).getId() == currentUser.getId()) {
                        break;
                    }
                }
                listNc.remove(j);
                listPhong.get(i).setDsng(listNc);
                break;
            }
        }
        if (listPhong.get(i).getDsng().isEmpty()) {
            Message message = new Message("remove phong", listPhong.get(i).getId());
            FuncSendAll(message);
            listPhong.remove(i);
        } else {
            listOos.get(listPhong.get(i).getDsng().get(0).getId()).writeObject(new Message("remove user", this.currentUser.getId()));
            sendFixPhong(listPhong.get(i));
        }
    }

    public void sendDiemGame() throws IOException {
        TranDau td = null;
        for (TranDau tranDauL : listTranDau) {
            if (tranDauL.getIdUser1() == currentUser.getId() || tranDauL.getIdUser2() == currentUser.getId()) {
                td = tranDauL;
            }
        }
        if (td != null) {
            int diemUser = 0;
            int diemUserOther = 0;
            ArrayList<VanChoi> listVanChoi = vanChoiDAO.getVanChoiByTranDauId(td.getId());
            for (VanChoi vc : listVanChoi) {
                int diem = ncvcDAO.getDiemGameUser(vc, td.getIdUser1());
                int diemOther = ncvcDAO.getDiemGameUser(vc, td.getIdUser2());
                if (diem != -1) {
                    diemUser += diem;
                }
                if (diemUserOther != -1) {
                    diemUserOther += diemOther;
                }
            }
            Message message1 = new Message("Diem user");
            Message message2 = new Message("Diem user");
            message2.setObject(currentUser.getId());
            message1.setMessageInt(diemUser);
            message2.setMessageInt(diemUserOther);
            message1.setObject(message2);
            oos.writeObject(message1);
            System.out.println("send diem");
        }
    }

    public void sendFixPhong(Phong phong) {
        Message message = new Message("Fix phong", phong);
        FuncSendAll(message);
    }
}
