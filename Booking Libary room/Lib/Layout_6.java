package Lib;
import java.awt.*;
import javax.swing.*;

public class Layout_6 extends JPanel{
    public Layout_6(){

        setLayout(null);
        setBounds(0,90,1900,990);

        Font fontTille = new Font("SansSerif",Font.BOLD,25);
        Font fontSub = new Font("SansSerif",Font.BOLD,18);
        Font fontNormal = new Font("SansSerif",Font.BOLD,16);

        JLabel d1 = new JLabel("สถานะห้อง");
        JLabel d2 = new JLabel("A101");
        JLabel d3 = new JLabel("A102");
        JLabel d4 = new JLabel("A103");
        JLabel d5 = new JLabel("A104");
        JLabel d6 = new JLabel("A105");
        JLabel d7 = new JLabel("B101");
        JLabel d8 = new JLabel("B102");
        JLabel d9 = new JLabel("B103");
        JLabel d10 = new JLabel("B104");
        JLabel d11 = new JLabel("B105");
        JLabel d12 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d13 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d14 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d15 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d16 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d17 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d18 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d19 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d20 = new JLabel("1 TV, 2 Table, 6 chairs");
        JLabel d21 = new JLabel("1 TV, 2 Table, 6 chairs");
       
        d1.setFont(fontSub);
        d2.setFont(fontSub);
        d3.setFont(fontSub);
        d4.setFont(fontSub);
        d5.setFont(fontSub);
        d6.setFont(fontSub);
        d7.setFont(fontSub);
        d8.setFont(fontSub);
        d9.setFont(fontSub);
        d10.setFont(fontSub);
        d11.setFont(fontSub);

        d1.setBounds(1570,50,300,30);
        d2.setBounds(1570,110,300,30);
        d3.setBounds(1570,185,300,30);
        d4.setBounds(1570,260,300,30);
        d5.setBounds(1570,335,300,30);
        d6.setBounds(1570,410,300,30);
        d7.setBounds(1570,485,300,30);
        d8.setBounds(1570,560,300,30);
        d9.setBounds(1570,635,300,30);
        d10.setBounds(1570,710,300,30);
        d11.setBounds(1570,785,300,30);

        d12.setBounds(1570,130,300,30);
        d13.setBounds(1570,205,300,30);
        d14.setBounds(1570,280,300,30);
        d15.setBounds(1570,355,300,30);
        d16.setBounds(1570,430,300,30);
        d17.setBounds(1570,505,300,30);
        d18.setBounds(1570,580,300,30);
        d19.setBounds(1570,655,300,30);
        d20.setBounds(1570,730,300,30);
        d21.setBounds(1570,805,300,30);

        add(d1);
        add(d2);
        add(d3);
        add(d4);
        add(d5);
        add(d6);
        add(d7);
        add(d8);
        add(d9);
        add(d10);
        add(d11);
        add(d12);
        add(d13);
        add(d14);
        add(d15);
        add(d16);
        add(d17);
        add(d18);
        add(d19);
        add(d20);
        add(d21);

        String[] options = {"Available","Waiting","Not Available"};
        JComboBox<String>[] boxchoice = new JComboBox[10];
        int[] boxPosition ={120,195,270,345,420,495,570,645,720,795};
        
        for(int i=0; i<10; i++){
            boxchoice[i] = new JComboBox<>(options);
            boxchoice[i].setBounds(1740, boxPosition[i], 105, 30);
            add(boxchoice[i]);
        
        }
       
    }

    @Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.drawRect(1534,31,335,819);     

        }
}
