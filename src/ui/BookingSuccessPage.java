package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font
import javax.swing.*;   // JPanel, JLabel

/*
 * หน้า "จองห้องสำเร็จ" ขนาดหน้า 1172 x 790
 */
public class BookingSuccessPage extends JPanel {   // สืบทอด JPanel

    private JLabel roomValue;        // ค่า "ห้องที่จอง"
    private JLabel peopleValue;      // ค่า "จำนวนผู้ใช้"
    private JLabel dateValue;        // ค่า "วันที่จอง"
    private JLabel timeValue;        // ค่า "เวลาที่จอง"
    private JLabel bookingIdValue;   // ค่า "รหัสการจอง"

    public BookingSuccessPage() {             // คอนสตรักเตอร์
        setLayout(null);                      // วางของด้วย setBounds
        setBackground(Theme.BACKGROUND);      // พื้นสีอ่อน

        IconPanel check = new IconPanel(IconPanel.CHECK_CIRCLE, 58, Theme.SUCCESS);   // วงกลมเขียว + เครื่องหมายถูก
        check.setBounds(557, 40, 58, 58);                                             // x = (1172-58)/2 = 557 → กลางหน้า
        add(check);                                                                   // ใส่ลงในหน้า

        JLabel title = new JLabel("จองห้องสำเร็จ", JLabel.CENTER);   // หัวข้อ จัดกลาง
        title.setFont(Theme.font(Font.BOLD, 22));                    // ตัวหนา 22
        title.setForeground(Theme.TEXT);                             // สีเข้ม
        title.setBounds(0, 112, 1172, 34);                           // กว้างเต็มหน้า → อยู่กลางหน้าพอดี
        add(title);                                                  // ใส่ลงในหน้า

        JLabel subtitle = new JLabel("การจองห้องของคุณสำเร็จแล้ว", JLabel.CENTER);   // หัวข้อรอง
        subtitle.setFont(Theme.font(Font.PLAIN, 18));                               // ตัวปกติ 18
        subtitle.setForeground(Theme.TEXT_GRAY);                                    // สีเทา
        subtitle.setBounds(0, 150, 1172, 28);                                       // ใต้หัวข้อ
        add(subtitle);                                                              // ใส่ลงในหน้า

        JLabel note = new JLabel("กรุณาบันทึกรหัสการจองเพื่อนำมาแสดงให้เจ้าหน้าที่และติดต่อรับกุญแจห้อง", JLabel.CENTER);   // ข้อความแนะนำ
        note.setFont(Theme.font(Font.PLAIN, 13));                                                                   // ตัวเล็ก 13
        note.setForeground(Theme.TEXT_GRAY);                                                                        // สีเทา
        note.setBounds(0, 184, 1172, 20);                                                                           // ใต้หัวข้อรอง
        add(note);                                                                                                  // ใส่ลงในหน้า

        // ---------- การ์ดรายละเอียด ----------
        RoundPanel card = new RoundPanel(Theme.WHITE, Theme.BORDER);   // การ์ดพื้นขาว ขอบอ่อน
        card.setBounds(270, 236, 632, 348);                                // x = (1172-632)/2 = 270 → กลางหน้า
        add(card);                                                         // ใส่ลงในหน้า

        IconPanel cardIcon = new IconPanel(IconPanel.CALENDAR_INFO, 32, Theme.SECONDARY);   // ไอคอนหัวการ์ด
        cardIcon.setBounds(26, 14, 32, 32);                                                 // มุมซ้ายบนของการ์ด
        card.add(cardIcon);                                                                 // ใส่ลงในการ์ด

        JLabel cardTitle = new JLabel("รายละเอียดการจอง");   // หัวการ์ด
        cardTitle.setFont(Theme.font(Font.BOLD, 17));       // ตัวหนา 17
        cardTitle.setForeground(Theme.TEXT);                // สีเข้ม
        cardTitle.setBounds(66, 15, 300, 30);               // ทางขวาของไอคอน
        card.add(cardTitle);                                // ใส่ลงในการ์ด

        JPanel line = new JPanel();          // เส้นคั่น = JPanel สูง 1 พิกเซล
        line.setBackground(Theme.BORDER);    // สีเส้นอ่อน
        line.setBounds(26, 58, 580, 1);      // ยาวเกือบเต็มการ์ด สูง 1 พิกเซล
        card.add(line);                      // ใส่ลงในการ์ด

        // ข้อมูลตัวอย่างตามดีไซน์ (ภายหลังส่งค่าจริงมาแทน)
        roomValue      = addRow(card, IconPanel.DOOR,     "ห้องที่จอง",  "ห้อง A101",      82);    // แถวที่ 1
        peopleValue    = addRow(card, IconPanel.USERS,    "จำนวนผู้ใช้", "6 คน",           128);   // แถวที่ 2 (ห่างแถวละ 46)
        dateValue      = addRow(card, IconPanel.CALENDAR, "วันที่จอง",   "13 ตุลาคม 2569", 174);   // แถวที่ 3
        timeValue      = addRow(card, IconPanel.CLOCK,    "เวลาที่จอง",  "13.00-15.00",    220);   // แถวที่ 4
        bookingIdValue = addRow(card, IconPanel.ID_BADGE, "รหัสการจอง", "LBA-003",        266);   // แถวที่ 5
    }                                                                                          // จบคอนสตรักเตอร์

    // แถวข้อมูล: ไอคอน + หัวข้อสีเทา + ค่าตัวหนา  (คืนค่า JLabel ของค่า ไว้เปลี่ยนข้อความทีหลัง)
    private JLabel addRow(JPanel card, int iconType, String title, String value, int y) {   // คืนค่าเป็น JLabel ของค่า
        IconPanel icon = new IconPanel(iconType, 22, Theme.SECONDARY);   // ไอคอนน้ำเงิน ขนาด 22
        icon.setBounds(36, y, 22, 22);                                   // ซ้ายสุดของแถว
        card.add(icon);                                                  // ใส่ลงในการ์ด

        JLabel t = new JLabel(title);                 // หัวข้อของแถว
        t.setFont(Theme.font(Font.PLAIN, 15));        // ตัวปกติ 15
        t.setForeground(Theme.TEXT_GRAY);             // สีเทา
        t.setBounds(66, y, 122, 24);                  // ทางขวาของไอคอน
        card.add(t);                                  // ใส่ลงในการ์ด

        JLabel v = new JLabel(value);                 // ค่าของแถว
        v.setFont(Theme.font(Font.BOLD, 15));         // ตัวหนา 15
        v.setForeground(Theme.TEXT);                  // สีเข้ม
        v.setBounds(188, y, 400, 24);                 // ทางขวาของหัวข้อ
        card.add(v);                                  // ใส่ลงในการ์ด
        return v;                                     // คืนป้ายค่า ให้เก็บไว้ในตัวแปร
    }                                                 // จบ addRow

    // ใส่ข้อมูลการจองจริงลงในการ์ด (AppFrame เรียกหลังบันทึกการจองสำเร็จ)
    public void showBooking(Booking b) {
        roomValue.setText("ห้อง " + b.getRoomId());                              // ห้องที่จอง
        peopleValue.setText(b.getPeople() + " คน");                              // จำนวนผู้ใช้
        dateValue.setText(BookingService.thaiDate(b.getDate()));                // วันที่แบบไทย เช่น 14 ตุลาคม 2569
        timeValue.setText(b.getStartTime() + "-" + b.getEndTime());             // เวลา เช่น 13.00-15.00
        bookingIdValue.setText(b.getId());                                      // รหัสการจอง
    }                                                                           // จบ showBooking

    // getter: ตอนทำ logic ใช้ใส่ค่าจริง เช่น getBookingIdValue().setText("LBA-010")
    public JLabel getRoomValue()       { return roomValue; }                                // คืนค่า roomValue
    public JLabel getPeopleValue()     { return peopleValue; }                              // คืนค่า peopleValue
    public JLabel getDateValue()       { return dateValue; }                                // คืนค่า dateValue
    public JLabel getTimeValue()       { return timeValue; }                                // คืนค่า timeValue
    public JLabel getBookingIdValue()  { return bookingIdValue; }                           // คืนค่า bookingIdValue
}                                                               // จบคลาส BookingSuccessPage
