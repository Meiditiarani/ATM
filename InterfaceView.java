package view;

import java.awt.event.ActionListener;


public interface InterfaceView {

    public void addListener(ActionListener e);
    public void viewErrorMsg(String errorMsg);
    public void setVisible(boolean b);

    
}
