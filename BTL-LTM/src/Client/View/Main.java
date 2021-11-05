/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client.View;

import Client.ControlListener.Connection;
import Client.ControlListener.LoginController;
import Client.ControlListener.MainViewController;

/**
 *
 * @author 07duc
 */
public class Main {
    public static void main(String[] args) {
        // TODO code application logic here
        LoginView viewLog = new LoginView();
        Connection con = new Connection();
        LoginController controller = new LoginController(viewLog, con);
    }
}
