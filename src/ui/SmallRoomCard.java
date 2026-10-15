package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font
import javax.swing.*;   // JLabel

/*
 * การ์ดห้องแบบเล็กในหน้า "ดูห้องทั้งหมด" (ขนาด 288 x 116)
 */
public class SmallRoomCard extends RoundPanel {   // สืบทอด RoundPanel (กล่องสี่เหลี่ยม)

    private String roomName;                      // ชื่อห้อง
    private RoundButton selectButton;             // ปุ่ม "เลือกห้องนี้"

    public SmallRoomCard(String roomName, int capacity, String equipment) {   // รับชื่อห้อง ความจุ อุปกรณ์
        super(Theme.WHITE, Theme.BORDER);     // กล่องพื้นขาว ขอบอ่อน
        this.roomName = roomName;                 // เก็บชื่อห้อง

        JLabel name = new JLabel("ห้อง  " + roomName, JLabel.CENTER);   // ชื่อห้อง จัดกลาง
        name.setFont(Theme.font(Font.PLAIN, 14));                       // ตัวปกติ 14
        name.setForeground(Theme.TEXT);                                 // สีเข้ม
        name.setBounds(0, 6, 288, 20);                                  // แถวบนสุด
        add(name);                                                      // ใส่ลงในการ์ด

        JLabel cap = new JLabel("รองรับ " + capacity + " คน", JLabel.CENTER);   // ความจุ
        cap.setFont(Theme.font(Font.PLAIN, 14));                               // ตัวปกติ 14
        cap.setForeground(Theme.TEXT);                                         // สีเข้ม
        cap.setBounds(0, 29, 288, 20);                                         // แถวที่ 2
        add(cap);                                                              // ใส่ลงในการ์ด

        JLabel equip = new JLabel("อุปกรณ์ : " + equipment, JLabel.CENTER);   // อุปกรณ์
        equip.setFont(Theme.font(Font.PLAIN, 14));                           // ตัวปกติ 14
        equip.setForeground(Theme.TEXT);                                     // สีเข้ม
        equip.setBounds(0, 52, 288, 20);                                     // แถวที่ 3
        add(equip);                                                          // ใส่ลงในการ์ด

        selectButton = new RoundButton("เลือกห้องนี้");      // ปุ่มน้ำเงิน
        selectButton.setFont(Theme.font(Font.PLAIN, 13));   // ฟอนต์เล็กลง (ปุ่มเล็ก)
        selectButton.setBounds(99, 78, 90, 26);             // x = (288-90)/2 = 99 → อยู่กลางการ์ด
        add(selectButton);                                  // ใส่ลงในการ์ด
    }                                                       // จบคอนสตรักเตอร์

    public String getRoomName()            { return roomName; }       // getter ชื่อห้อง
    public RoundButton getSelectButton()   { return selectButton; }   // getter ปุ่ม (AppFrame ใช้ผูก ActionListener)
}                                                                     // จบคลาส SmallRoomCard
