package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;            // Font
import javax.swing.*;         // JPanel, JLabel
import java.util.ArrayList;   // ลิสต์ห้องที่อ่านจาก rooms.csv

/*
 * หน้า "ดูห้องทั้งหมด" — แสดงเฉพาะห้องที่ยังไม่ถูกจองในวันนี้ ขนาดหน้า 1172 x 790
 *   - ตอนสร้างหน้า: สร้างการ์ดให้ทุกห้องใน rooms.csv ไว้ก่อน (ซ่อนไว้ทั้งหมด)
 *   - ทุกครั้งที่เปิดหน้า: AppFrame เรียก refresh(ห้องว่าง) → แสดงเฉพาะการ์ดของห้องว่าง แล้วจัดตำแหน่งใหม่
 *   - ตึก A อยู่คอลัมน์ซ้าย, ตึกอื่น (B) อยู่คอลัมน์ขวา ตามดีไซน์
 */
public class AllRoomsPage extends JPanel {   // สืบทอด JPanel

    private SmallRoomCard[] cards;    // Array เก็บการ์ดห้อง 1 ใบต่อ 1 ห้องใน rooms.csv (บทที่ 5)
    private JLabel emptyLabel;        // ข้อความตอนไม่มีห้องว่างเลย

    public AllRoomsPage() {                   // คอนสตรักเตอร์
        setLayout(null);                      // วางของด้วย setBounds
        setBackground(Theme.BACKGROUND);      // พื้นสีอ่อน

        JLabel title = new JLabel("ดูห้องทั้งหมด");   // หัวข้อหน้า
        title.setFont(Theme.font(Font.BOLD, 22));     // ตัวหนา 22
        title.setForeground(Theme.TEXT);              // สีเข้ม
        title.setBounds(24, 12, 300, 36);             // มุมซ้ายบน
        add(title);                                   // ใส่ลงในหน้า

        ArrayList<Room> rooms = RoomService.loadRooms();   // อ่านห้องทั้งหมดจาก rooms.csv
        cards = new SmallRoomCard[rooms.size()];           // จอง Array ให้พอดีจำนวนห้อง
        for (int i = 0; i < cards.length; i++) {           // วนสร้างการ์ดทีละห้อง
            Room room = rooms.get(i);                      // ห้องที่ i ในลิสต์
            cards[i] = new SmallRoomCard(room.getId(), room.getCapacity(), room.getEquipmentText());   // สร้างการ์ดจากข้อมูลในไฟล์
            cards[i].setVisible(false);                    // ซ่อนไว้ก่อน (refresh จะเลือกแสดงเฉพาะห้องว่าง)
            add(cards[i]);                                 // ใส่การ์ดลงในหน้า
        }                                                  // จบ for

        emptyLabel = new JLabel("วันนี้ไม่มีห้องว่างแล้ว", JLabel.CENTER);   // ข้อความตอนห้องเต็มทุกห้อง
        emptyLabel.setFont(Theme.font(Font.PLAIN, 18));                    // ตัวปกติ 18
        emptyLabel.setForeground(Theme.TEXT_GRAY);                         // สีเทา
        emptyLabel.setBounds(0, 300, 1172, 30);                            // กลางหน้า
        emptyLabel.setVisible(false);                                      // ซ่อนไว้ก่อน
        add(emptyLabel);                                                   // ใส่ลงในหน้า
    }                                                                      // จบคอนสตรักเตอร์

    // แสดงเฉพาะการ์ดของห้องที่อยู่ในลิสต์ freeRooms แล้วจัดเรียงใหม่ไม่ให้มีช่องว่าง
    public void refresh(ArrayList<Room> freeRooms) {
        int leftRow = 0;     // แถวถัดไปของคอลัมน์ซ้าย (ตึก A)
        int rightRow = 0;    // แถวถัดไปของคอลัมน์ขวา (ตึก B)
        for (int i = 0; i < cards.length; i++) {                                        // วนทุกการ์ด
            boolean free = RoomService.findRoom(freeRooms, cards[i].getRoomName()) != null;   // ห้องนี้อยู่ในลิสต์ห้องว่างไหม (findRoom ไม่เจอคืน null)
            cards[i].setVisible(free);                                                  // ว่าง = แสดง, ถูกจองแล้ว = ซ่อน
            if (free) {                                                                 // จัดตำแหน่งเฉพาะการ์ดที่แสดง
                if (cards[i].getRoomName().startsWith("A")) {                           // ตึก A → คอลัมน์ซ้าย
                    cards[i].setBounds(276, 56 + leftRow * (116 + 9), 288, 116);        // x ซ้าย, y เลื่อนแถวละ 125
                    leftRow++;                                                          // แถวถัดไปของคอลัมน์ซ้าย
                } else {                                                                // ตึกอื่น → คอลัมน์ขวา
                    cards[i].setBounds(276 + 288 + 44, 56 + rightRow * (116 + 9), 288, 116);   // x ขวา (เลื่อนไป 332)
                    rightRow++;                                                         // แถวถัดไปของคอลัมน์ขวา
                }                                                                       // จบ if
            }                                                                           // จบ if
        }                                                                               // จบ for
        emptyLabel.setVisible(freeRooms.size() == 0);   // ไม่มีห้องว่างเลย → แสดงข้อความ
        repaint();                                      // วาดหน้าใหม่ (บทที่ 12)
    }                                                   // จบ refresh

    public SmallRoomCard[] getCards() {   // getter: AppFrame วนผูก ActionListener ให้ปุ่มทุกใบ
        return cards;                     // คืน Array ของการ์ด
    }                                     // จบ getCards
}                                         // จบคลาส AllRoomsPage
