package Lib;
import java.awt.*;
import javax.swing.*;

public class Layout_2 extends JPanel{
    public Layout_2(){

        setLayout(null);
        setBounds(0,0,1900,115);

        Font fontTille = new Font("SansSerif",Font.BOLD,20);
        Font fontSub = new Font("SansSerif",Font.BOLD,18);
        Font fontNormal = new Font("SansSerif",Font.BOLD,16);

        JLabel b1 = new JLabel("นางสาวอัจฉราพร สุขศรี");
        JLabel b2 = new JLabel("ผู้ดูแลระบบ");

        b1.setFont(fontSub);

        b1.setBounds(1600,30,300,30);
        b2.setBounds(1600,60,300,30);

        add(b1);
        add(b2);

    }

    @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawRect(290,10,1580,100);

        }
}
