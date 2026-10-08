package Lib;
import java.awt.*;
import javax.swing.*;

public class Layout_4 extends JPanel{
    public Layout_4(){

        setLayout(null);
        setBounds(290,130,1235,505);

        Font fontTille = new Font("SansSerif",Font.BOLD,25);
        Font fontSub = new Font("SansSerif",Font.BOLD,18);
        Font fontNormal = new Font("SansSerif",Font.BOLD,16);

        JLabel e1 = new JLabel("ปฎิทินการจอง");
        JLabel e2 = new JLabel("A101 - A105");
        JLabel e3 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel e4 = new JLabel("B101 - B105");
        JLabel e5 = new JLabel("1 TV, 2 Table, 6 chairs");

        e1.setFont(fontSub);
        e2.setFont(fontSub);
        e3.setFont(fontNormal);
        e4.setFont(fontSub);
        e5.setFont(fontNormal);

        e1.setBounds(30,225,300,30);
        e2.setBounds(80,290,300,30);
        e3.setBounds(80,310,300,30);
        e4.setBounds(80,380,300,30);
        e5.setBounds(80,400,300,30);

        add(e1);
        add(e2);
        add(e3);
        add(e4);
        add(e5);


    }

    @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawRect(0,202,1232,300);
            
        }

}
