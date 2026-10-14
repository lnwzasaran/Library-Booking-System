package Bar;

import  java.awt.*;
import javax.swing.*;



public class SideBar extends JPanel {
    public SideBar() {

        Color sidebarColor = new Color(26,37,48);
        Color textbaColor = Color.WHITE;

        JLabel title = new JLabel("Library Booking");
        JButton b1 = new JButton("HOME");
        JButton b2 = new JButton("ROOM FOR BOOKING");
        JButton b3 = new JButton("BOOKING CHANEL");
        JButton b4 = new JButton("MY BOOKING");
        JButton b5 = new JButton("IMFORMATION");

        b1.setBackground(new Color(26,37,48));
        b2.setBackground(new Color(26,37,48));
        b3.setBackground(new Color(26,37,48));
        b4.setBackground(new Color(41,128,185));
        b5.setBackground(new Color(26,37,48));

        title.setForeground(textbaColor);
        b1.setForeground(textbaColor);
        b2.setForeground(textbaColor);
        b3.setForeground(textbaColor);
        b4.setForeground(textbaColor);
        b5.setForeground(textbaColor);

        title.setFont(new Font("Tahoma", Font.BOLD, 20));
        b1.setFont(new Font("Tahoma", Font.BOLD, 16));
        b2.setFont(new Font("Tahoma", Font.BOLD, 16));
        b3.setFont(new Font("Tahoma", Font.BOLD, 16));
        b4.setFont(new Font("Tahoma", Font.BOLD, 16));
        b5.setFont(new Font("Tahoma", Font.BOLD, 16));

        title.setBounds(20,30,200,40);
        b1.setBounds(0,100,220,40);
        b2.setBounds(0,140,220,40);
        b3.setBounds(0,180,220,40);
        b4.setBounds(0,220,220,40);
        b5.setBounds(0,260,220,40);

        add(title);
        add(b1);
        add(b2);
        add(b3);
        add(b4);
        add(b5);

        setLayout(null);
        setBackground(sidebarColor);
        setBounds(0, 0, 220, 650); 
    }
}
/*
        JLabel j1 = new JLabel("  Library Booking");

        j1.setColor(text);

        j1.setBounds(20,30,200,30);

        add(j1);

        
        // โลโก้ด้านบน
        JLabel logoLabel = new JLabel("  Library Booking");
        logoLabel.setForeground(textColor);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        logoLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 30, 10));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(logoLabel);

        // สร้างปุ่มเมนูต่างๆ พร้อมกำหนด Action เมื่อคลิก
        add(createMenuButton("🏠 หน้าหลัก", "HOME", listener));
        add(createMenuButton("🏢 ดูห้องทั้งหมด", "ALL_ROOMS", listener));
        add(createMenuButton("➕ จองห้อง", "BOOK_ROOM", listener));
        add(createMenuButton("📅 การจองของฉัน", "MY_BOOKINGS", listener));
        add(createMenuButton("👤 ข้อมูลส่วนตัว", "PROFILE", listener));
    }

    private JButton createMenuButton(String text, String cardName, MenuClickListener listener) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(220, 45));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(26, 37, 48));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(new Font("Tahoma", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // เมื่อกดปุ่ม ให้เรียกฟังก์ชันผ่าน listener ไปเปลี่ยนหน้าจอตรงกลาง
        btn.addActionListener(e -> {
            if (listener != null) {
                listener.onMenuClick(cardName);
            }
        });

        return btn; */

    

    /*@Override 
        protected void paintComponent(Graphics g){
            super.paintComponent(g);

            g.setColor(new Color(41, 128, 185));
            g.fillRect(30, 30, 200, 100);
            g.drawRect(10,10,270,930);
            
        }*/
    
    
        
       

