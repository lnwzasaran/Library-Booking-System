package Lib;
import java.awt.*;
import javax.swing.*;

public class AdminDashboard{
    public AdminDashboard(){
        JFrame f = new JFrame("Admin Dashboard");
        Container cp = f.getContentPane();
        cp.setLayout(null);
        
        Layout_1 LayOut1 = new Layout_1();
        Layout_2 LayOut2 = new Layout_2();
        Layout_3 LayOut3 = new Layout_3();
        Layout_4 LayOut4 = new Layout_4();
        Layout_5 LayOut5 = new Layout_5();
        Layout_6 LayOut6 = new Layout_6();

        f.setSize(1900,1000);
        f.setVisible(true);
        f.setLocationRelativeTo(null);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        cp.add(LayOut1);
        cp.add(LayOut2);
        cp.add(LayOut3);
        cp.add(LayOut4);
        cp.add(LayOut5);
        cp.add(LayOut6);

        }


        

}