package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Font
import javax.swing.*;   // JLabel

/*
 * ป้ายสถานะเล็ก ๆ เช่น "จองสำเร็จ" (เขียว), "ยกเลิกแล้ว" (แดง)
 */
public class StatusBadge extends RoundPanel {   // สืบทอด RoundPanel (กล่องสี่เหลี่ยม)

    private JLabel label;                       // ข้อความในป้าย (เก็บไว้ เผื่อเปลี่ยนสถานะภายหลัง)

    public StatusBadge(String text, Color bg, Color fg, int w, int h) {   // รับข้อความ สีพื้น สีตัวอักษร ขนาด
        super(bg, null);                                  // กล่องสีตามที่ส่งมา ไม่มีขอบ

        label = new JLabel(text, JLabel.CENTER);             // ข้อความจัดกลาง
        label.setFont(Theme.font(Font.BOLD, 13));            // ตัวหนา 13
        label.setForeground(fg);                             // สีตัวอักษร
        label.setBounds(0, 0, w, h);                         // เต็มป้าย → ข้อความอยู่กลางป้ายพอดี
        add(label);                                          // ใส่ลงในป้าย
    }                                                        // จบคอนสตรักเตอร์

    public JLabel getLabel() {   // getter: ใช้เปลี่ยนข้อความ เช่น getLabel().setText("ยกเลิกแล้ว")
        return label;            // คืนป้ายข้อความ
    }                            // จบ getLabel
}                                // จบคลาส StatusBadge
