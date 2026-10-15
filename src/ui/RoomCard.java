package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font
import javax.swing.*;   // JLabel

/*
 * การ์ดห้องที่เลือกในหน้า "จองห้อง" (ขนาด 200 x 192)
 */
public class RoomCard extends RoundPanel {   // สืบทอด RoundPanel (กล่องสี่เหลี่ยม)

    private String roomName;                 // ชื่อห้อง เช่น "A101" (เก็บไว้ ตอนทำ logic จะรู้ว่ากดห้องไหน)
    private RoundButton selectButton;        // ปุ่ม "เลือกห้องนี้"

    public RoomCard(String roomName, int capacity, String equipment) {   // รับชื่อห้อง ความจุ อุปกรณ์
        super(Theme.WHITE, Theme.CARD_OUTLINE);   // กล่องพื้นขาว ขอบเทา
        this.roomName = roomName;                     // เก็บชื่อห้อง

        JLabel name = new JLabel("ห้อง " + roomName, JLabel.CENTER);   // ชื่อห้อง (JLabel.CENTER = จัดกลาง)
        name.setFont(Theme.font(Font.BOLD, 14));                       // ตัวหนา 14
        name.setForeground(Theme.TEXT);                                // สีเข้ม
        name.setBounds(0, 12, 200, 22);                                // กว้างเท่าการ์ด จึงอยู่กลางการ์ดพอดี
        add(name);                                                     // ใส่ลงในการ์ด

        JLabel cap = new JLabel("รองรับ: " + capacity + " คน", JLabel.CENTER);   // ความจุ (ต่อสตริงกับตัวเลข)
        cap.setFont(Theme.font(Font.PLAIN, 14));                                // ตัวปกติ 14
        cap.setForeground(Theme.TEXT_GRAY);                                     // สีเทา
        cap.setBounds(0, 46, 200, 22);                                          // วางใต้ชื่อห้อง
        add(cap);                                                               // ใส่ลงในการ์ด

        JLabel equip = new JLabel("อุปกรณ์: " + equipment, JLabel.CENTER);   // อุปกรณ์ในห้อง
        equip.setFont(Theme.font(Font.PLAIN, 14));                          // ตัวปกติ 14
        equip.setForeground(Theme.TEXT_GRAY);                               // สีเทา
        equip.setBounds(0, 78, 200, 22);                                    // วางใต้ความจุ
        add(equip);                                                         // ใส่ลงในการ์ด

        selectButton = new RoundButton("เลือกห้องนี้");   // ปุ่มน้ำเงิน
        selectButton.setBounds(51, 126, 98, 40);         // x = (200-98)/2 = 51 → อยู่กลางการ์ด
        add(selectButton);                               // ใส่ลงในการ์ด
    }                                                    // จบคอนสตรักเตอร์

    public String getRoomName()            { return roomName; }       // getter ชื่อห้อง
    public RoundButton getSelectButton()   { return selectButton; }   // getter ปุ่ม (AppFrame ใช้ผูก ActionListener)
}                                                                     // จบคลาส RoomCard
