package model;

import controller.Handler;
import view.LoginNasabah;


public class GUIATMTransaction {
        public static void main(String[] args) {
        
        Aplikasi model = new Aplikasi();
        LoginNasabah view = new LoginNasabah();
        view.setLocationRelativeTo(null);
        Handler handler = new Handler();
        
        handler.setModel(model);
        handler.setView(view);
        handler.setListener();
        view.setVisible(true);
    }
}
