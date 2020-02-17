

import javax.swing.*;

public class Notify extends Thread{
    private String message;
    private String title;
    private int type; // 1 - information message; 2 - Warning message

    public Notify(){
    }

    public Notify(String message, String title, int type){
        this.message = message;
        this.title = title;
        this.type = type;
    }

    @Override
    public void run() {
        infoBox();
    }

    public void infoBox(){
//        JOptionPane.showMessageDialog(null, message, title, type);
        JOptionPane pane = new JOptionPane(message,type);
        JDialog d = pane.createDialog((JFrame)null, title);
        d.setLocation(500,10);
        d.setVisible(true);
    }

}
