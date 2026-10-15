package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font, Graphics
import javax.swing.*;   // JPanel, JLabel

/*
 * เมนู 1 รายการใน sidebar (ไอคอน + ข้อความ)
 * ถ้าถูกเลือก (selected) จะวาดพื้นสีน้ำเงิน
 * การคลิกจัดการที่ AppFrame (addMouseListener)
 */
public class SidebarItem extends JPanel {   // สืบทอด JPanel

    private boolean selected = false;       // เมนูนี้ถูกเลือกอยู่หรือไม่ (เริ่มต้น = ไม่)

    public SidebarItem(int iconType, String text) {   // คอนสตรักเตอร์: รับชนิดไอคอนและชื่อเมนู
        setLayout(null);                              // วางของข้างในด้วย setBounds
        setOpaque(false);                             // ไม่ระบายพื้นเอง (เราวาดพื้นน้ำเงินเองตอนถูกเลือก)

        IconPanel icon = new IconPanel(iconType, 38, Theme.WHITE);   // ไอคอนสีขาว ขนาด 38
        icon.setBounds(10, 12, 38, 38);                              // วางไอคอน (x=10, y=12, กว้าง 38, สูง 38)
        add(icon);                                                   // ใส่ไอคอนลงในเมนู

        JLabel label = new JLabel(text);                 // ป้ายชื่อเมนู
        label.setFont(Theme.font(Font.PLAIN, 18));       // ฟอนต์ขนาด 18
        label.setForeground(Theme.WHITE);                // ตัวอักษรสีขาว
        label.setBounds(68, 12, 150, 38);                // วางทางขวาของไอคอน
        add(label);                                      // ใส่ป้ายชื่อลงในเมนู
    }                                                    // จบคอนสตรักเตอร์

    public void setSelected(boolean selected) {   // ตั้งว่าเมนูนี้ถูกเลือกหรือไม่
        this.selected = selected;                 // เก็บค่าใหม่
        repaint();                                // สั่งวาดใหม่ เพื่อให้พื้นน้ำเงินแสดง/หายไป
    }                                             // จบ setSelected

    public void paintComponent(Graphics g) {      // วาดพื้นของเมนู
        super.paintComponent(g);                  // ให้ JPanel วาดส่วนพื้นฐานก่อน
        if (selected) {                           // ถ้าเมนูนี้ถูกเลือกอยู่
            g.setColor(Theme.SECONDARY);          // สีน้ำเงิน
            g.fillRect(0, 0, getWidth(), getHeight());   // ระบายพื้นสี่เหลี่ยมเต็มขนาดเมนู
        }                                         // จบ if
    }                                             // จบ paintComponent
}                                                 // จบคลาส SidebarItem
