package Bar;

import  java.awt.*;
import javax.swing.*;

public class BookingTab extends JPanel{
    public BookingTab(){

        setLayout(null);
        setBounds(220, 0, 780, 650); 
        setBackground(new Color(245, 246, 250));

        JLabel j1 = new JLabel("MY BOOKING");
        JLabel j2 = new JLabel("Now Booking");
        JLabel j3 = new JLabel("Old Booking");
        JLabel j4 = new JLabel("นายวชิรศักดิ์ โยโย่");

        //tt is top tap
        JLabel tt1 = new JLabel("QUEUE");
        JLabel tt2 = new JLabel("BOOKING ID");
        JLabel tt3 = new JLabel("ROOM");
        JLabel tt4 = new JLabel("DATE");
        JLabel tt5 = new JLabel("TIME");
        JLabel tt6 = new JLabel("STATUS");
        JLabel tt7 = new JLabel("MANAGE");

        //ut is under tap
        JLabel ut1 = new JLabel("QUEUE");
        JLabel ut2 = new JLabel("BOOKING ID");
        JLabel ut3 = new JLabel("ROOM");
        JLabel ut4 = new JLabel("DATE");
        JLabel ut5 = new JLabel("TIME");
        JLabel ut6 = new JLabel("STATUS");

        //w = write
        JLabel w1 = new JLabel("1                            LBA-001               B102                     13 Oct 2025        09.00 - 11.00       BOOKING             CANCEL");
        JLabel w2 = new JLabel("1                                 LBA-001                    B102                          13 Oct 2025             09.00 - 11.00           CANCEL");
        
        
        j1.setFont(new Font("Tahoma", Font.BOLD, 20));
        j2.setFont(new Font("Tahoma", Font.BOLD, 16));
        j3.setFont(new Font("Tahoma", Font.BOLD, 16));
        
        j1.setBounds(30, 30, 300, 40);
        j2.setBounds(30, 80, 300, 40);
        j3.setBounds(30, 310, 300, 40);
        j4.setBounds(620, 5, 300, 30);

        tt1.setBounds(63, 113, 105, 40);
        tt2.setBounds(155, 113, 105, 40);
        tt3.setBounds(247, 113, 105, 40);
        tt4.setBounds(339, 113, 105, 40);
        tt5.setBounds(431, 113, 105, 40);
        tt6.setBounds(523, 113, 105, 40);
        tt7.setBounds(615, 113, 105, 40);

        ut1.setBounds(63, 343, 300, 40);
        ut2.setBounds(170, 343, 300, 40);
        ut3.setBounds(277, 343, 300, 40);
        ut4.setBounds(384, 343, 300, 40);
        ut5.setBounds(491, 343, 300, 40);
        ut6.setBounds(598, 343, 300, 40);

        w1.setBounds(63, 145, 600, 40);
        w2.setBounds(63, 375, 600, 40);

        add(j1);
        add(j2);
        add(j3);
        add(j4);
        add(tt1);
        add(tt2);
        add(tt3);
        add(tt4);
        add(tt5);
        add(tt6);
        add(tt7);
        add(ut1);
        add(ut2);
        add(ut3);
        add(ut4);
        add(ut5);
        add(ut6);

        add(w1);
        add(w2);
        
    }

    @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            //top tap
            g.drawRect(55,120,644,180);
            g.drawRect(55,120,92,27);
            g.drawRect(147,120,92,27);
            g.drawRect(239,120,92,27);
            g.drawRect(331,120,92,27);
            g.drawRect(423,120,92,27);
            g.drawRect(515,120,92,27);
            g.drawRect(607,120,92,27);
            
            //under tap
            g.drawRect(55,350,644,180);
            g.drawRect(55,350,107,27);
            g.drawRect(162,350,107,27);
            g.drawRect(269,350,107,27);
            g.drawRect(376,350,107,27);
            g.drawRect(483,350,107,27);
            g.drawRect(590,350,109,27);
            
        }
    
}