package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font, Graphics
import javax.swing.*;   // JPanel, JLabel

/*
 * แถบด้านบน: กระดิ่ง + ไอคอนผู้ใช้ + ชื่อผู้ใช้ (กว้าง 1172 สูง 70)
 */
public class HeaderPanel extends JPanel {   // สืบทอด JPanel

    private JLabel nameLabel;               // ป้ายชื่อผู้ใช้ (เก็บไว้ เผื่อเปลี่ยนชื่อตอน login)

    public HeaderPanel(String userName) {   // คอนสตรักเตอร์: รับชื่อผู้ใช้
        setLayout(null);                    // วางของด้วย setBounds
        setBackground(Theme.WHITE);         // พื้นสีขาว

        IconPanel bell = new IconPanel(IconPanel.BELL, 24, Theme.TEXT);   // ไอคอนกระดิ่ง ขนาด 24
        bell.setBounds(942, 23, 24, 24);                                  // วางด้านขวา
        add(bell);                                                        // ใส่ลงใน header

        IconPanel user = new IconPanel(IconPanel.USER, 26, Theme.TEXT);   // ไอคอนผู้ใช้ ขนาด 26
        user.setBounds(994, 22, 26, 26);                                  // วางถัดจากกระดิ่ง
        add(user);                                                        // ใส่ลงใน header

        nameLabel = new JLabel(userName);                  // ป้ายชื่อผู้ใช้
        nameLabel.setFont(Theme.font(Font.PLAIN, 13));     // ฟอนต์ขนาด 13
        nameLabel.setForeground(Theme.TEXT);               // สีตัวอักษรเข้ม
        nameLabel.setBounds(1032, 22, 140, 26);            // วางถัดจากไอคอนผู้ใช้
        add(nameLabel);                                    // ใส่ลงใน header
    }                                                      // จบคอนสตรักเตอร์

    public void paintComponent(Graphics g) {   // วาด header
        super.paintComponent(g);               // วาดพื้นสีขาวก่อน
        g.setColor(Theme.BORDER);              // สีเส้นอ่อน
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);   // เส้นใต้ header
    }                                          // จบ paintComponent

    public JLabel getNameLabel() {   // getter: ใช้เปลี่ยนชื่อผู้ใช้ภายหลัง เช่น getNameLabel().setText(...)
        return nameLabel;            // คืนป้ายชื่อ
    }                                // จบ getNameLabel
}                                    // จบคลาส HeaderPanel
