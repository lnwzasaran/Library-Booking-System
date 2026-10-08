package Lib;
import java.awt.*;
import javax.swing.*;


public class Layout_5 extends JPanel{
    public Layout_5(){

        setLayout(null);
        setBounds(280,632,1250,720);

        Font fontTille = new Font("SansSerif",Font.BOLD,25);
        Font fontSub = new Font("SansSerif",Font.BOLD,18);
        Font fontNormal = new Font("SansSerif",Font.BOLD,16);

        JLabel h1 = new JLabel("การจองล่าสุด");
        JLabel h2 = new JLabel("ลำดับการจอง");
        JLabel h3 = new JLabel("รหัสการจอง");
        JLabel h4 = new JLabel("ชื่อผู้จอง");
        JLabel h5 = new JLabel("ห้อง");
        JLabel h6 = new JLabel("วันที่จอง");
        JLabel h7 = new JLabel("เวลา");
        JLabel h8 = new JLabel("สถานะ");
        JLabel h9 = new JLabel("1                                         LAS-20251008-001              นายหมายเลขหนึ่ง คนแรก             ห้อง A101             8 ตุลาคม 2569         10:00 - 12:00          ยืนยันแล้ว ");
        JLabel h10 = new JLabel("2                                         LAS-20251008-002              นายหมายเลขสอง คนสอง             ห้อง A102             8 ตุลาคม 2569         10:00 - 12:00          รอการยืนยัน ");
        JLabel h11 = new JLabel("3                                         LAS-20251008-003              นายหมายเลขสาม คนสาม             ห้อง A103             8 ตุลาคม 2569         10:00 - 12:00          ยกเลิก");
        JLabel h12 = new JLabel("4                                         LAS-20251008-004              นายหมายเลขสี่ คนสุดท้าย             ห้อง A104             8 ตุลาคม 2569         10:00 - 12:00          ยืนยันแล้ว");

        h1.setFont(fontSub);
        h2.setFont(fontSub);
        h3.setFont(fontSub);
        h4.setFont(fontSub);
        h5.setFont(fontSub);
        h6.setFont(fontSub);
        h7.setFont(fontSub);
        h8.setFont(fontSub);
        h9.setFont(fontNormal);
        h10.setFont(fontNormal);
        h11.setFont(fontNormal);
        h12.setFont(fontNormal);

        h1.setBounds(40,30,300,30);
        h2.setBounds(80,80,300,30);
        h3.setBounds(250,80,300,30);
        h4.setBounds(450,80,300,30);
        h5.setBounds(690,80,300,30);
        h6.setBounds(820,80,300,30);
        h7.setBounds(960,80,300,30);
        h8.setBounds(1100,80,300,30);
        h9.setBounds(80,130,1500,30);
        h10.setBounds(80,160,1500,30);
        h11.setBounds(80,190,1500,30);
        h12.setBounds(80,220,1500,30);

        add(h1);
        add(h2);
        add(h3);
        add(h4);
        add(h5);
        add(h6);
        add(h7);
        add(h8);
        add(h9);
        add(h10);
        add(h11);
        add(h12);

    }

    @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawRect(10,10,1231,298);
            
        }

}
