/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Client.ControlListener;

import Client.View.LoginView;
import Client.View.MainView;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;
import javax.swing.text.View;
import model.Message;
import model.NguoiChoi;

/**
 *
 * @author toan
 */
public class LoginController {

    private LoginView view;
    private Connection con;

    public LoginController(LoginView view, Connection con) {
        this.view = view;
        this.con = con;
        view.addLoginListener(new LoginListener());
        view.addLoginListener2(new LoginListener2());
        view.setVisible(true);
    }

    class LoginListener implements ActionListener{

        public LoginListener() {
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            try {
                NguoiChoi model = view.getUser();
                Message request = new Message("login", model);
                con.SendRequest(request);
                Message reponse = (Message) con.receiverResponse();
                if (reponse.getMessage().equals("Login success")) {
                    NguoiChoi currentUser = (NguoiChoi) reponse.getObject();
                    MainView mainView = new MainView();
                    MainViewController mainViewController = new MainViewController(mainView, con, currentUser);
                    mainView.setVisible(true);
                    view.dispose();
                    view.setVisible(false);
                    mainViewController.start();
                } else {
                    JOptionPane.showMessageDialog(view, reponse.getMessage());
                }
            } catch (HeadlessException e) {
                e.printStackTrace();
            }
        }

        

    }
    
    class LoginListener2 implements KeyListener{
        public LoginListener2() {
        }
        
        @Override
        public void keyTyped(KeyEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                NguoiChoi model = view.getUser();
                Message request = new Message("login", model);
                con.SendRequest(request);
                Message reponse = (Message) con.receiverResponse();
                if (reponse.getMessage().equals("Login success")) {
                    NguoiChoi currentUser = (NguoiChoi) reponse.getObject();
                    MainView mainView = new MainView();
                    MainViewController mainViewController = new MainViewController(mainView, con, currentUser);
                    mainView.setVisible(true);
                    view.dispose();
                    view.setVisible(false);
                    mainViewController.start();
                } else {
                    JOptionPane.showMessageDialog(view, reponse.getMessage());
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
//            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
}
