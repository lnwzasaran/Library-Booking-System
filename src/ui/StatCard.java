package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Font
import javax.swing.*;   // JLabel

/*
 * การ์ดสรุปตัวเลขในหน้าหลัก เช่น "ห้องทั้งหมด 10 ห้อง" (ขนาด 200 x 192)
 */
public class StatCard extends RoundPanel {   // สืบทอด RoundPanel (กล่องสี่เหลี่ยม)

    private JLabel valueLabel;               // ป้ายตัวเลข (เก็บไว้ เปลี่ยนค่าได้ภายหลัง)

    public StatCard(int iconType, Color iconColor, Color background, String title, String value) {   // รับไอคอน สีไอคอน สีพื้น หัวข้อ ค่า
        super(background, darker(background));   // พื้นตามที่ส่งมา ขอบเข้มกว่าพื้นนิดหน่อย

        IconPanel icon = new IconPanel(iconType, 38, iconColor);   // ไอคอนขนาด 38
        icon.setBounds(26, 52, 38, 38);                            // วางด้านบนซ้าย
        add(icon);                                                 // ใส่ลงในการ์ด

        JLabel titleLabel = new JLabel(title);              // หัวข้อ เช่น "ห้องทั้งหมด"
        titleLabel.setFont(Theme.font(Font.PLAIN, 16));     // ตัวปกติ 16
        titleLabel.setForeground(Theme.TEXT);               // สีเข้ม
        titleLabel.setBounds(26, 102, 170, 22);             // วางใต้ไอคอน
        add(titleLabel);                                    // ใส่ลงในการ์ด

        valueLabel = new JLabel(value);                     // ตัวเลข เช่น "10 ห้อง"
        valueLabel.setFont(Theme.font(Font.BOLD, 26));      // ตัวหนาใหญ่ 26
        valueLabel.setForeground(Theme.TEXT);               // สีเข้ม
        valueLabel.setBounds(26, 128, 170, 36);             // วางใต้หัวข้อ
        add(valueLabel);                                    // ใส่ลงในการ์ด
    }                                                       // จบคอนสตรักเตอร์

    // สีเส้นขอบ: เข้มกว่าสีพื้นเล็กน้อย
    private static Color darker(Color c) {   // static เพราะเรียกใน super(...) ซึ่งทำงานก่อน object สร้างเสร็จ
        return new Color(c.getRed() * 92 / 100, c.getGreen() * 92 / 100, c.getBlue() * 92 / 100);   // ลดค่าแดง/เขียว/น้ำเงินลงเหลือ 92%
    }                                        // จบ darker

    public JLabel getValueLabel() {   // getter: เปลี่ยนตัวเลขได้ เช่น getValueLabel().setText("8 ห้อง")
        return valueLabel;            // คืนป้ายตัวเลข
    }                                 // จบ getValueLabel
}                                     // จบคลาส StatCard
