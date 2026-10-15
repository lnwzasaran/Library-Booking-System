package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Font
import javax.swing.*;   // JLabel

/*
 * การ์ดสรุปตัวเลขในหน้า Admin เช่น "ห้องทั้งหมด 10 ห้อง" (ขนาด 142 x 136, จัดทุกอย่างกึ่งกลาง)
 */
public class AdminStatCard extends RoundPanel {   // สืบทอด RoundPanel (กล่องสี่เหลี่ยม)

    private JLabel valueLabel;                    // ป้ายตัวเลข (เก็บไว้ เปลี่ยนค่าได้ภายหลัง)

    public AdminStatCard(int iconType, Color iconColor, Color bg, Color border, String title, String value) {   // รับไอคอน สีต่าง ๆ หัวข้อ ค่า
        super(bg, border);                                   // กล่องพื้นสีอ่อน ขอบตามที่ส่งมา

        IconPanel icon = new IconPanel(iconType, 38, iconColor);   // ไอคอนขนาด 38
        icon.setBounds(52, 14, 38, 38);                            // x = (142-38)/2 = 52 → กลางการ์ด
        add(icon);                                                 // ใส่ลงในการ์ด

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);   // หัวข้อ จัดกลาง
        titleLabel.setFont(Theme.font(Font.BOLD, 15));          // ตัวหนา 15
        titleLabel.setForeground(Theme.TEXT);                   // สีเข้ม
        titleLabel.setBounds(0, 58, 142, 22);                   // กว้างเท่าการ์ด
        add(titleLabel);                                        // ใส่ลงในการ์ด

        valueLabel = new JLabel(value, JLabel.CENTER);          // ตัวเลข จัดกลาง
        valueLabel.setFont(Theme.font(Font.BOLD, 20));          // ตัวหนา 20
        valueLabel.setForeground(Theme.TEXT);                   // สีเข้ม
        valueLabel.setBounds(0, 82, 142, 28);                   // ใต้หัวข้อ
        add(valueLabel);                                        // ใส่ลงในการ์ด
    }                                                           // จบคอนสตรักเตอร์

    public JLabel getValueLabel() {   // getter: เปลี่ยนตัวเลขได้ เช่น getValueLabel().setText("9 ห้อง")
        return valueLabel;            // คืนป้ายตัวเลข
    }                                 // จบ getValueLabel
}                                     // จบคลาส AdminStatCard
