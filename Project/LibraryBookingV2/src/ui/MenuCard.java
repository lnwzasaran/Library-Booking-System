package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Font
import javax.swing.*;   // JLabel

/*
 * การ์ดเมนูลัดในหน้าหลัก: กล่องสี (ไอคอน + ชื่อ) + คำอธิบาย 2 บรรทัด (ขนาด 200 x 264)
 * กล่องสี (tile) กดได้ — จัดการที่ AppFrame ด้วย MouseListener
 */
public class MenuCard extends RoundPanel {   // สืบทอด RoundPanel (กล่องสี่เหลี่ยม)

    private RoundPanel tile;                 // กล่องสีด้านบน (ส่วนที่กดได้)

    public MenuCard(int iconType, Color tileColor, String title, String line1, String line2) {   // รับไอคอน สีกล่อง ชื่อ คำอธิบาย 2 บรรทัด
        super(Theme.WHITE, Theme.CARD_OUTLINE);   // การ์ดพื้นขาว ขอบเทา

        // ---------- กล่องสี ----------
        tile = new RoundPanel(tileColor, null);   // กล่องสีตามที่ส่งมา ไม่มีขอบ
        tile.setBounds(12, 12, 176, 138);             // วางในการ์ด เว้นขอบ 12
        add(tile);                                    // ใส่กล่องสีลงในการ์ด

        IconPanel icon = new IconPanel(iconType, 40, Theme.WHITE, tileColor);   // ไอคอนสีขาว (สีรอง = สีกล่อง)
        icon.setBounds(68, 30, 40, 40);                                        // x = (176-40)/2 = 68 → กลางกล่อง
        tile.add(icon);                                                        // ใส่ไอคอนลงในกล่องสี

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);   // ชื่อเมนู จัดกลาง
        titleLabel.setFont(Theme.font(Font.BOLD, 20));          // ตัวหนา 20
        titleLabel.setForeground(Theme.WHITE);                  // สีขาว
        titleLabel.setBounds(0, 82, 176, 30);                   // วางใต้ไอคอน กว้างเท่ากล่อง
        tile.add(titleLabel);                                   // ใส่ลงในกล่องสี

        // ---------- คำอธิบาย ----------
        JLabel desc1 = new JLabel(line1, JLabel.CENTER);   // คำอธิบายบรรทัดที่ 1
        desc1.setFont(Theme.font(Font.PLAIN, 14));         // ตัวปกติ 14
        desc1.setForeground(Theme.TEXT);                   // สีเข้ม
        desc1.setBounds(0, 168, 200, 20);                  // วางใต้กล่องสี
        add(desc1);                                        // ใส่ลงในการ์ด

        JLabel desc2 = new JLabel(line2, JLabel.CENTER);   // คำอธิบายบรรทัดที่ 2 (ส่ง "" ได้ถ้าไม่มี)
        desc2.setFont(Theme.font(Font.PLAIN, 14));         // ตัวปกติ 14
        desc2.setForeground(Theme.TEXT);                   // สีเข้ม
        desc2.setBounds(0, 192, 200, 20);                  // วางใต้บรรทัดที่ 1
        add(desc2);                                        // ใส่ลงในการ์ด
    }                                                      // จบคอนสตรักเตอร์

    public RoundPanel getTile() {   // getter: AppFrame ใช้ผูก MouseListener กับกล่องสี
        return tile;                // คืนกล่องสี
    }                               // จบ getTile
}                                   // จบคลาส MenuCard
