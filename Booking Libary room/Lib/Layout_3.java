package Lib;
import java.awt.*;
import javax.swing.*;

public class Layout_3 extends JPanel {
    public Layout_3(){

        setLayout(null);
        setBounds(0,90,1525,235);

        Font fontTille = new Font("SansSerif",Font.BOLD,25);
        Font fontSub = new Font("SansSerif",Font.BOLD,18);
        Font fontNormal = new Font("SansSerif",Font.BOLD,16);
        
        JLabel c1 = new JLabel("ห้องทั้งหมด");
        JLabel c2 = new JLabel("10 ห้อง");
        JLabel c3 = new JLabel("ใช้งานได้ 10 ห้อง");
        JLabel c4 = new JLabel("ห้องว่าง");
        JLabel c5 = new JLabel("7 ห้อง");
        JLabel c6 = new JLabel("70% ของห้องทั้งหมด");
        JLabel c7 = new JLabel("ห้องไม่ว่าง");
        JLabel c8 = new JLabel("3 ห้อง");
        JLabel c9 = new JLabel("30% ของห้องทั้งหมด");
        JLabel c10 = new JLabel("ผู้ใช้ทั้งหมด");
        JLabel c11 = new JLabel("80 คน");
        JLabel c12 = new JLabel("นักศึกษาจำนวน 80 คน");

        c1.setFont(fontSub);
        c2.setFont(fontTille);
        c4.setFont(fontSub);
        c5.setFont(fontTille);
        c7.setFont(fontSub);
        c8.setFont(fontTille);
        c10.setFont(fontSub);
        c11.setFont(fontTille);
        
        c1.setBounds(320,120,300,30);
        c2.setBounds(320,150,300,30);
        c3.setBounds(320,180,300,30);
        c4.setBounds(632,120,300,30);
        c5.setBounds(632,150,300,30);
        c6.setBounds(632,180,300,30);
        c7.setBounds(943,120,300,30);
        c8.setBounds(943,150,300,30);
        c9.setBounds(943,180,300,30);
        c10.setBounds(1254,120,300,30);
        c11.setBounds(1254,150,300,30);
        c12.setBounds(1254,180,300,30);

        add(c1);
        add(c2);
        add(c3);
        add(c4);
        add(c5);
        add(c6);
        add(c7);
        add(c8);
        add(c9);
        add(c10);
        add(c11);
        add(c12);


    }

    @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawRect(290,31,300,200);
            g.drawRect(601,31,300,200);
            g.drawRect(912,31,300,200);
            g.drawRect(1223,31,300,200);

        }
    
}
