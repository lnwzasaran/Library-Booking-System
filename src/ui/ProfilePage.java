package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font
import javax.swing.*;   // JPanel, JLabel

/*
 * หน้า "ข้อมูลส่วนตัว" ขนาดหน้า 1172 x 790
 */
public class ProfilePage extends JPanel {   // สืบทอด JPanel

    private JLabel fullNameValue;           // ค่า ชื่อ-นามสกุล
    private JLabel studentIdValue;          // ค่า รหัสนิสิต
    private JLabel majorValue;              // ค่า สาขา
    private JLabel programValue;            // ค่า ภาค

    public ProfilePage() {                    // คอนสตรักเตอร์
        setLayout(null);                      // วางของด้วย setBounds
        setBackground(Theme.BACKGROUND);      // พื้นสีอ่อน

        JLabel title = new JLabel("ข้อมูลส่วนตัว");   // หัวข้อหน้า
        title.setFont(Theme.font(Font.BOLD, 30));    // ตัวหนา 30
        title.setForeground(Theme.TEXT);             // สีเข้ม
        title.setBounds(26, 14, 400, 44);            // มุมซ้ายบน
        add(title);                                  // ใส่ลงในหน้า

        RoundPanel card = new RoundPanel(Theme.WHITE, Theme.CARD_OUTLINE);   // การ์ดใหญ่ พื้นขาว ขอบเทา
        card.setBounds(26, 82, 1116, 620);                                       // กว้างเกือบเต็มหน้า
        add(card);                                                               // ใส่ลงในหน้า

        // ---------- ฝั่งซ้าย: รูปโปรไฟล์ ----------
        IconPanel avatar = new IconPanel(IconPanel.AVATAR, 184, Theme.SECONDARY);   // รูปโปรไฟล์ขนาด 184 (ด้านในสีขาวเหมือนการ์ด)
        avatar.setBounds(66, 70, 184, 184);                                                    // x = (316-184)/2 = 66 → กลางฝั่งซ้าย
        card.add(avatar);                                                                      // ใส่ลงในการ์ด

        JPanel divider = new JPanel();       // เส้นแนวตั้ง
        divider.setBackground(Theme.BORDER); // สีเส้นอ่อน
        divider.setBounds(316, 14, 1, 592);  // กว้าง 1 พิกเซล สูงเกือบเต็มการ์ด
        card.add(divider);                   // ใส่ลงในการ์ด

        // ---------- ฝั่งขวา: ข้อมูล (ตัวอย่าง) ----------
        fullNameValue  = addRow(card, "ชื่อ-นามสกุล", "นาย วชิรศักดิ์ โยโย่", 46);    // แถวที่ 1
        studentIdValue = addRow(card, "รหัสนิสิต",   "6821651789",          140);   // แถวที่ 2 (ห่างแถวละ 94)
        majorValue     = addRow(card, "สาขา",        "วิทยาการคอมพิวเตอร์",  234);   // แถวที่ 3
        programValue   = addRow(card, "ภาค",         "พิเศษ",                328);   // แถวที่ 4

        JPanel line = new JPanel();          // เส้นแนวนอนด้านล่าง
        line.setBackground(Theme.BORDER);    // สีเส้นอ่อน
        line.setBounds(406, 480, 622, 1);    // สูง 1 พิกเซล
        card.add(line);                      // ใส่ลงในการ์ด
    }                                        // จบคอนสตรักเตอร์

    private JLabel addRow(JPanel card, String label, String value, int y) {   // เมธอดช่วย: สร้างแถว หัวข้อ + ค่า
        JLabel l = new JLabel(label);                  // หัวข้อ เช่น "รหัสนิสิต"
        l.setFont(Theme.font(Font.BOLD, 20));          // ตัวหนา 20
        l.setForeground(Theme.TEXT);                   // สีเข้ม
        l.setBounds(356, y, 184, 30);                  // คอลัมน์หัวข้อ
        card.add(l);                                   // ใส่ลงในการ์ด

        JLabel v = new JLabel(value);                  // ค่า เช่น "6821651789"
        v.setFont(Theme.font(Font.PLAIN, 20));         // ตัวปกติ 20
        v.setForeground(Theme.TEXT);                   // สีเข้ม
        v.setBounds(540, y, 500, 30);                  // คอลัมน์ค่า
        card.add(v);                                   // ใส่ลงในการ์ด
        return v;                                      // คืนป้ายค่า ให้เก็บไว้ในตัวแปร
    }                                                  // จบ addRow

    // getter: ตอนทำ logic ใช้ใส่ข้อมูลจริงของผู้ใช้
    public JLabel getFullNameValue()          { return fullNameValue; }                     // คืนค่า fullNameValue
    public JLabel getStudentIdValue()         { return studentIdValue; }                    // คืนค่า studentIdValue
    public JLabel getMajorValue()             { return majorValue; }                        // คืนค่า majorValue
    public JLabel getProgramValue()           { return programValue; }                      // คืนค่า programValue
}                                                                        // จบคลาส ProfilePage
