package Lib;
import java.awt.*;
import javax.swing.*;

public class Layout_1 extends JPanel {
    public Layout_1(){
        
        setLayout(null);
        setBounds(0,0,289,1000);

        JLabel a1 = new JLabel("Library Booking");
        JLabel a2 = new JLabel("ระบบจองห้องสมุด");
        JButton a3 = new JButton("Dashboard");
        JButton a4 = new JButton("ออกจากระบบ");

        Font fontTille = new Font("SansSerif",Font.BOLD,20);
        Font fontSub = new Font("SansSerif",Font.BOLD,18);
        Font fontNormal = new Font("SansSerif",Font.BOLD,16);

        a1.setFont(fontTille);
        a3.setFont(fontSub);
        a4.setFont(fontSub);

        a1.setBounds(20,30,200,30);
        a2.setBounds(20,60,200,30);
        a3.setBounds(16,120,259,30);
        a4.setBounds(16,904,259,30); 

        add(a1);
        add(a2);
        add(a3);
        add(a4);

    }


    @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawRect(10,10,270,930);
            
        }
        
}
