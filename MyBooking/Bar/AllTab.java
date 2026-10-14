package Bar;

import java.awt.*;
import javax.swing.*;

public class AllTab{
    public AllTab(){
        JFrame f = new JFrame("Library Booking System");
        Container cp = f.getContentPane();
        cp.setLayout(null);
        
        SideBar Bar1 = new SideBar();
        BookingTab Bar2 = new BookingTab();

        f.setSize(1000, 650);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLocationRelativeTo(null);

        cp.add(Bar1);
        cp.add(Bar2);
        
        
    
    }

}