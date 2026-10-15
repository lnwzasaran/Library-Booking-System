package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;            // Color, Font
import javax.swing.*;         // JPanel, JLabel
import java.util.ArrayList;   // ลิสต์ห้องที่อ่านจาก rooms.csv

/*
 * กล่อง "สถานะห้อง" ด้านขวาของหน้า Admin — รายการห้องจาก rooms.csv พร้อมป้าย ว่าง / ไม่ว่าง
 *   - แต่ละห้องสร้างป้ายไว้ 2 อัน (ว่าง สีเขียว, ไม่ว่าง สีแดง) แล้วสลับด้วย setVisible
 *   - ใต้ป้าย "ไม่ว่าง" มีข้อความบอกช่วงเวลาที่ถูกจอง เช่น "13:00 - 15:00"
 *   - AdminPage เรียก setStatus(...) ตามการจองในเวลาปัจจุบัน
 */
public class RoomStatusPanel extends RoundPanel {   // สืบทอด RoundPanel (กล่องสี่เหลี่ยม)

    private String[] roomIds;          // รหัสห้องแต่ละแถว (เรียงตาม rooms.csv)
    private StatusBadge[] freeBadges;  // ป้าย "ว่าง" ของแต่ละห้อง
    private StatusBadge[] busyBadges;  // ป้าย "ไม่ว่าง" ของแต่ละห้อง
    private JLabel[] timeLabels;       // ช่วงเวลาที่ถูกจอง (แสดงเฉพาะตอนไม่ว่าง)

    // rooms = ห้องทั้งหมดที่อ่านจาก rooms.csv
    public RoomStatusPanel(ArrayList<Room> rooms) {
        super(Theme.WHITE, Theme.BORDER);       // กล่องพื้นขาว ขอบอ่อน

        JLabel title = new JLabel("สถานะห้อง");      // หัวข้อ
        title.setFont(Theme.font(Font.BOLD, 20));   // ตัวหนา 20
        title.setForeground(Theme.TEXT);            // สีเข้ม
        title.setBounds(38, 16, 300, 32);           // มุมซ้ายบน
        add(title);                                 // ใส่ลงในกล่อง

        addLine(60);                                // เส้นคั่นใต้หัวข้อ

        int n = rooms.size();                       // จำนวนห้อง
        roomIds = new String[n];                    // สร้าง Array เท่าจำนวนห้อง
        freeBadges = new StatusBadge[n];            // ป้ายว่าง
        busyBadges = new StatusBadge[n];            // ป้ายไม่ว่าง
        timeLabels = new JLabel[n];                 // ข้อความช่วงเวลา
        for (int i = 0; i < n; i++) {               // วนสร้างแถวห้องทีละห้อง
            Room room = rooms.get(i);               // ห้องที่ i
            roomIds[i] = room.getId();              // จำรหัสห้อง
            int y = 62 + i * 70;                    // แถวถัดไปเลื่อนลง 70 พิกเซล

            IconPanel door = new IconPanel(IconPanel.ROOM_DOOR, 28, Theme.STAT_GREEN);   // ไอคอนประตูสีเขียว
            door.setBounds(36, y + 20, 28, 28);                                          // ซ้ายของแถว
            add(door);                                                                   // ใส่ลงในกล่อง

            JLabel name = new JLabel("ห้อง " + room.getId());   // ชื่อห้องจากไฟล์
            name.setFont(Theme.font(Font.PLAIN, 15));            // ตัวปกติ 15
            name.setForeground(Theme.TEXT);                      // สีเข้ม
            name.setBounds(84, y + 14, 150, 22);                 // ทางขวาของไอคอน
            add(name);                                           // ใส่ลงในกล่อง

            JLabel cap = new JLabel(room.getCapacity() + " คน");   // ความจุจากไฟล์
            cap.setFont(Theme.font(Font.PLAIN, 10));               // ตัวเล็ก 10
            cap.setForeground(Theme.TEXT);                         // สีเข้ม
            cap.setBounds(84, y + 36, 100, 14);                    // ใต้ชื่อห้อง
            add(cap);                                              // ใส่ลงในกล่อง

            freeBadges[i] = new StatusBadge("ว่าง", new Color(0xDFF7DA), Theme.STAT_GREEN, 72, 20);   // ป้ายเขียวอ่อน ตัวเขียว
            freeBadges[i].setBounds(240, y + 24, 72, 20);        // ชิดขวาของแถว
            add(freeBadges[i]);                                  // ใส่ลงในกล่อง

            busyBadges[i] = new StatusBadge("ไม่ว่าง", new Color(0xFDE0E0), Theme.DANGER, 72, 20);   // ป้ายชมพู ตัวแดง
            busyBadges[i].setBounds(240, y + 16, 72, 20);        // ตำแหน่งเดียวกัน (ขยับขึ้นนิดให้มีที่ใส่เวลาด้านล่าง)
            busyBadges[i].setVisible(false);                     // ซ่อนไว้ก่อน (เริ่มต้น = ว่าง)
            add(busyBadges[i]);                                  // ใส่ลงในกล่อง

            timeLabels[i] = new JLabel("", JLabel.CENTER);       // ช่วงเวลาที่ถูกจอง
            timeLabels[i].setFont(Theme.font(Font.PLAIN, 10));   // ตัวเล็ก 10
            timeLabels[i].setForeground(Theme.DANGER);           // สีแดงเข้าชุดกับป้าย
            timeLabels[i].setBounds(226, y + 38, 100, 14);       // ใต้ป้ายไม่ว่าง
            add(timeLabels[i]);                                  // ใส่ลงในกล่อง

            addLine(y + 70);                                     // เส้นคั่นใต้แถว
        }                                                        // จบ for
    }                                                            // จบคอนสตรักเตอร์

    // เปลี่ยนสถานะห้องแถวที่ i: busy = true → ไม่ว่าง (แสดงช่วงเวลา time), false → ว่าง
    public void setStatus(int i, boolean busy, String time) {
        freeBadges[i].setVisible(!busy);             // ว่าง → แสดงป้ายเขียว
        busyBadges[i].setVisible(busy);              // ไม่ว่าง → แสดงป้ายแดง
        if (busy) {                                  // ถ้าไม่ว่าง
            timeLabels[i].setText(time);             // แสดงช่วงเวลาที่ถูกจอง
        } else {                                     // ถ้าว่าง
            timeLabels[i].setText("");               // ไม่ต้องมีข้อความเวลา
        }                                            // จบ if
    }                                                // จบ setStatus

    private void addLine(int y) {                    // เมธอดช่วย: เส้นคั่นแนวนอน
        JPanel line = new JPanel();                  // เส้น = JPanel สูง 1 พิกเซล
        line.setBackground(Theme.BORDER);            // สีเส้นอ่อน
        line.setBounds(28, y, 300, 1);               // ยาว 300
        add(line);                                   // ใส่ลงในกล่อง
    }                                                // จบ addLine

    public String[] getRoomIds()           { return roomIds; }      // getter รหัสห้องแต่ละแถว
    public StatusBadge[] getBusyBadges()   { return busyBadges; }   // getter ป้ายไม่ว่าง (ใช้ทดสอบ)
}                                                                   // จบคลาส RoomStatusPanel
