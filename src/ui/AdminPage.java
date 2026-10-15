package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;            // Color, Font
import java.awt.event.*;      // ActionListener, ActionEvent (ใช้กับ Timer)
import javax.swing.*;         // JPanel, JLabel, Timer
import java.util.ArrayList;   // ลิสต์ห้อง / การจองที่อ่านจากไฟล์ csv
import java.text.SimpleDateFormat;   // จัดรูปแบบเวลา
import java.util.Date;
import java.util.Locale;             // Locale.US = ใช้ปฏิทิน ค.ศ. / เลขอารบิกเสมอ               // เวลาปัจจุบันของเครื่อง

/*
 * หน้าหลักของผู้ดูแลระบบ (Admin Dashboard) — เต็มหน้าต่าง 1440 x 860
 * ซ้าย = AdminSidebar, บน = แถบชื่อผู้ดูแล, กลาง = การ์ดต่าง ๆ, ขวา = สถานะห้อง
 * Timer (บทที่ 12) ทำงานทุก 1 วินาที: อัปเดตวันที่/เวลา และอ่าน bookings.csv ใหม่ทุกต้นนาที เพื่อเปลี่ยนสถานะห้องตามเวลา
 */
public class AdminPage extends JPanel implements ActionListener {   // สืบทอด JPanel + ฟัง event ของ Timer

    private static final Color TABLE_HEAD = new Color(0xE3EDF8);   // สีฟ้าอ่อนของหัวตาราง

    private AdminSidebar sidebar;              // แถบเมนูซ้าย
    private JLabel adminName;                  // ชื่อผู้ดูแลมุมขวาบน
    private JLabel dateLabel;                  // วันที่ (เปลี่ยนเป็นวันจริงตอนทำ logic)
    private JLabel timeLabel;                  // เวลา
    private AdminStatCard totalRoomsCard;      // การ์ด "ห้องทั้งหมด"
    private AdminStatCard freeRoomsCard;       // การ์ด "ห้องว่าง"
    private AdminStatCard busyRoomsCard;       // การ์ด "ห้องไม่ว่าง"
    private AdminStatCard usersCard;           // การ์ด "ผู้ใช้ทั้งหมด"
    private RoomStatusPanel roomStatus;        // กล่องสถานะห้องด้านขวา
    private JLabel seeAll;                     // ลิงก์ "ดูทั้งหมด" (กดแล้วไปหน้ารายการจองทั้งหมด)
    private IconPanel seeAllArrow;             // ลูกศรต่อท้าย "ดูทั้งหมด" (กดได้เหมือนกัน)
    private Timer timer;                       // นาฬิกาจับเวลา ทำงานทุก 1 วินาที (บทที่ 12)
    private JPanel latestRows;                 // กล่องใส่แถวข้อมูลของตาราง "การจองล่าสุด" (ล้างแล้วสร้างใหม่ทุกครั้งที่ refreshLatest)

    private static final int[] LATEST_CENTERS = {16, 70, 182, 312, 444, 560};   // กลางคอลัมน์ของตารางการจองล่าสุด (วัดจากซ้ายของตาราง)
    private static final int LATEST_ROW_H = 38;      // ความสูงแถวข้อมูล
    private static final int LATEST_MAX_ROWS = 5;    // แสดงไม่เกิน 5 รายการล่าสุด

    public AdminPage() {                       // คอนสตรักเตอร์
        setLayout(null);                       // วางของด้วย setBounds
        setBackground(Theme.BACKGROUND);       // พื้นสีอ่อน

        // ================= แถบเมนูซ้าย =================
        sidebar = new AdminSidebar();          // สร้าง sidebar ของ Admin
        sidebar.setBounds(0, 0, 352, 860);     // ชิดซ้าย กว้าง 352 สูงเต็มหน้าต่าง
        add(sidebar);                          // ใส่ลงในหน้า

        // ================= แถบด้านบน =================
        JPanel header = new JPanel();                  // แถบบน
        header.setLayout(null);                        // วางของด้วย setBounds
        header.setBackground(new Color(0xF7F8FA));     // สีเทาอ่อนมาก
        header.setBounds(352, 0, 1088, 72);            // ทางขวาของ sidebar สูง 72
        add(header);                                   // ใส่ลงในหน้า

        IconPanel adminIcon = new IconPanel(IconPanel.USER, 34, Theme.TEXT);   // ไอคอนผู้ใช้ ขนาด 34
        adminIcon.setBounds(836, 18, 34, 34);                                  // ด้านขวาของแถบ
        header.add(adminIcon);                                                 // ใส่ลงในแถบ

        adminName = new JLabel("");                                // ชื่อผู้ดูแล (AppFrame ใส่ชื่อจาก admins.csv ตอนเข้าสู่ระบบ)
        adminName.setFont(Theme.font(Font.BOLD, 15));              // ตัวหนา 15
        adminName.setForeground(Theme.TEXT);                       // สีเข้ม
        adminName.setBounds(880, 14, 210, 22);                     // ทางขวาของไอคอน
        header.add(adminName);                                     // ใส่ลงในแถบ

        JLabel adminRole = new JLabel("ผู้ดูแลระบบ");                // ตำแหน่ง
        adminRole.setFont(Theme.font(Font.PLAIN, 13));             // ตัวปกติ 13
        adminRole.setForeground(Theme.TEXT);                       // สีเข้ม
        adminRole.setBounds(880, 36, 210, 20);                     // ใต้ชื่อ
        header.add(adminRole);                                     // ใส่ลงในแถบ

        // ================= การ์ดต้อนรับ =================
        RoundPanel welcome = new RoundPanel(Theme.WHITE, Theme.BORDER);   // กล่องพื้นขาว
        welcome.setBounds(356, 80, 698, 100);                                // ใต้แถบบน
        add(welcome);                                                        // ใส่ลงในหน้า

        RoundPanel photo = new RoundPanel(new Color(0xC9CED6), null);     // กรอบรูปสีเทา (รูปตัวอย่าง)
        photo.setBounds(18, 22, 52, 52);                                     // ซ้ายของการ์ด
        welcome.add(photo);                                                  // ใส่ลงในการ์ด

        IconPanel person = new IconPanel(IconPanel.PERSON, 36, Theme.WHITE); // คนสีขาวในกรอบรูป
        person.setBounds(8, 10, 36, 36);                                     // กลางกรอบรูป
        photo.add(person);                                                   // ใส่ลงในกรอบรูป

        addLabel(welcome, "ยินดีต้อนรับ, ผู้ดูแลระบบ", Font.BOLD, 19, 84, 14, 400, 28);                 // หัวข้อต้อนรับ
        addLabel(welcome, "ระบบจัดการห้องสมุด มหาวิทยาลัยเกษตรศาสตร์", Font.PLAIN, 13, 84, 46, 320, 18);   // บรรทัดที่ 1
        addLabel(welcome, "วิทยาเขตกำแพงแสน", Font.PLAIN, 13, 84, 64, 320, 18);                         // บรรทัดที่ 2

        JPanel divider = new JPanel();          // เส้นแนวตั้งคั่นวันที่
        divider.setBackground(Theme.BORDER);    // สีเส้นอ่อน
        divider.setBounds(398, 10, 1, 80);      // กว้าง 1 พิกเซล
        welcome.add(divider);                   // ใส่ลงในการ์ด

        dateLabel = new JLabel("", JLabel.CENTER);                // วันที่ (updateClock ใส่วันที่จริง)
        dateLabel.setFont(Theme.font(Font.PLAIN, 15));            // ตัวปกติ 15
        dateLabel.setForeground(Theme.TEXT);                      // สีเข้ม
        dateLabel.setBounds(400, 20, 160, 22);                    // ทางขวาของเส้นคั่น
        welcome.add(dateLabel);                                   // ใส่ลงในการ์ด

        timeLabel = new JLabel("", JLabel.CENTER);                // เวลา (updateClock ใส่เวลาจริง)
        timeLabel.setFont(Theme.font(Font.PLAIN, 15));            // ตัวปกติ 15
        timeLabel.setForeground(Theme.TEXT);                      // สีเข้ม
        timeLabel.setBounds(400, 48, 160, 22);                    // ใต้วันที่
        welcome.add(timeLabel);                                   // ใส่ลงในการ์ด

        // ================= การ์ดสรุป 4 ใบ =================
        RoundPanel stats = new RoundPanel(Theme.WHITE, Theme.BORDER);   // กล่องพื้นขาวรองการ์ด
        stats.setBounds(356, 186, 698, 158);                               // ใต้การ์ดต้อนรับ
        add(stats);                                                        // ใส่ลงในหน้า

        totalRoomsCard = new AdminStatCard(IconPanel.DOOR_FILLED, Theme.PRIMARY,                      // ประตูน้ำเงิน
                new Color(0xEEF4FC), new Color(0xC9D8EE), "ห้องทั้งหมด", "10 ห้อง");                    // พื้นฟ้าอ่อน
        freeRoomsCard = new AdminStatCard(IconPanel.DOOR_FILLED, new Color(0x0B7A0B),                 // ประตูเขียว
                new Color(0xEAFBEA), new Color(0xC6E9C6), "ห้องว่าง", "1 ห้อง");                        // พื้นเขียวอ่อน
        busyRoomsCard = new AdminStatCard(IconPanel.DOOR_LOCK, new Color(0xC81E1E),                   // ประตูแดง + กุญแจ
                new Color(0xFDF0F0), new Color(0xF0CACA), "ห้องไม่ว่าง", "1 ห้อง");                      // พื้นชมพูอ่อน
        usersCard = new AdminStatCard(IconPanel.USERS_PLUS, new Color(0x9333EA),                      // คนสีม่วง
                new Color(0xF3ECFD), new Color(0xDCCBF5), "ผู้ใช้ทั้งหมด", "1 คน");                       // พื้นม่วงอ่อน

        totalRoomsCard.setBounds(10, 10, 142, 136);    // การ์ดที่ 1
        freeRoomsCard.setBounds(186, 10, 142, 136);    // การ์ดที่ 2 (ห่างกัน 176)
        busyRoomsCard.setBounds(364, 10, 142, 136);    // การ์ดที่ 3
        usersCard.setBounds(542, 10, 142, 136);        // การ์ดที่ 4
        stats.add(totalRoomsCard);                     // ใส่การ์ดทั้ง 4 ลงในกล่อง
        stats.add(freeRoomsCard);
        stats.add(busyRoomsCard);
        stats.add(usersCard);

        // ================= การจองล่าสุด =================
        RoundPanel latest = new RoundPanel(Theme.WHITE, Theme.BORDER);   // กล่องพื้นขาว
        latest.setBounds(356, 352, 698, 80 + LATEST_ROW_H * LATEST_MAX_ROWS + 16);   // ใต้การ์ดสรุป สูงพอดี 5 แถว (หัวตาราง 80 + แถวละ 38 + ขอบล่าง 16)
        add(latest);                                                        // ใส่ลงในหน้า

        addLabel(latest, "การจองล่าสุด", Font.BOLD, 15, 28, 14, 300, 26);    // หัวข้อ

        seeAll = new JLabel("ดูทั้งหมด");                       // ลิงก์ "ดูทั้งหมด" (AppFrame ผูก MouseListener)
        seeAll.setFont(Theme.font(Font.PLAIN, 11));             // ตัวเล็ก 11
        seeAll.setForeground(Theme.PRIMARY);                    // สีน้ำเงินเข้ม
        seeAll.setBounds(612, 10, 50, 18);                      // มุมขวาบนของกล่อง
        latest.add(seeAll);                                     // ใส่ลงในกล่อง

        seeAllArrow = new IconPanel(IconPanel.ARROW_RIGHT, 14, Theme.PRIMARY);       // ลูกศรชี้ขวา
        seeAllArrow.setBounds(664, 12, 14, 14);                                      // ต่อท้ายคำว่าดูทั้งหมด
        latest.add(seeAllArrow);                                                     // ใส่ลงในกล่อง

        JPanel head = new JPanel();             // แถบหัวตาราง
        head.setLayout(null);                   // วางของด้วย setBounds
        head.setBackground(TABLE_HEAD);         // สีฟ้าอ่อน
        head.setBounds(28, 46, 638, 34);        // กว้างเกือบเต็มกล่อง
        latest.add(head);                       // ใส่ลงในกล่อง

        String[] columns = {"ลำดับ", "รหัสการจอง", "ห้อง", "วันที่จอง", "เวลา", "สถานะการจอง"};   // ชื่อคอลัมน์
        int[] centers = LATEST_CENTERS;                                                          // กลางคอลัมน์ (วัดจากซ้ายของตาราง)
        for (int i = 0; i < columns.length; i++) {                                               // วนใส่ชื่อคอลัมน์
            JLabel c = new JLabel(columns[i], JLabel.CENTER);                                    // ชื่อคอลัมน์ จัดกลาง
            c.setFont(Theme.font(Font.PLAIN, 11));                                               // ตัวเล็ก 11
            c.setForeground(Theme.PRIMARY);                                                      // สีน้ำเงินเข้ม
            c.setBounds(centers[i] - 50, 0, 100, 34);                                            // กว้าง 100 ให้ center อยู่ตรงกลาง
            head.add(c);                                                                         // ใส่ลงในแถบหัวตาราง
        }                                                                                        // จบ for

        // แถวข้อมูล: อ่านจาก bookings.csv (สร้างใน refreshLatest)
        latestRows = new JPanel();                              // กล่องใส่แถวข้อมูล
        latestRows.setLayout(null);                             // วางของด้วย setBounds
        latestRows.setBackground(Theme.WHITE);                  // พื้นขาวเท่ากล่อง
        latestRows.setBounds(28, 80, 638, LATEST_ROW_H * LATEST_MAX_ROWS);   // ใต้หัวตาราง สูงพอ 5 แถว
        latest.add(latestRows);                                 // ใส่ลงในกล่อง
        refreshLatest();                                        // ใส่ข้อมูลครั้งแรก

        // ================= สถานะห้อง (ด้านขวา) =================
        roomStatus = new RoomStatusPanel(RoomService.loadRooms());   // สร้างกล่องสถานะห้องจาก rooms.csv
        roomStatus.setBounds(1074, 78, 354, 776);                    // ชิดขวา สูงเกือบเต็มหน้าต่าง
        add(roomStatus);                                             // ใส่ลงในหน้า

        updateClock();                                  // ใส่วันที่/เวลาจริงครั้งแรก
        refreshRoomStatus();                            // ใส่สถานะห้องครั้งแรก
        timer = new Timer(1000, this);                  // ทุก 1000 มิลลิวินาที (1 วินาที) เรียก actionPerformed ของคลาสนี้
        timer.start();                                  // เริ่มจับเวลา
    }                                                         // จบคอนสตรักเตอร์

    // ================= Timer (ทุก 1 วินาที) =================
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {                               // ถ้าเป็น event จาก Timer
            updateClock();                                          // อัปเดตวันที่/เวลาบนการ์ดต้อนรับ
            SimpleDateFormat sec = new SimpleDateFormat("ss", Locale.US);      // รูปแบบ "วินาที"
            if (sec.format(new Date()).equals("00")) {              // ทุกต้นนาที (วินาที = 00)
                refreshRoomStatus();                                // อ่านไฟล์ใหม่ แล้วเปลี่ยนสถานะห้องตามเวลา
            }                                                       // จบ if
        }                                                           // จบ if
    }                                                               // จบ actionPerformed

    // วันที่/เวลาปัจจุบัน เช่น "13 ตุลาคม 2569" และ "22:37 น."
    private void updateClock() {
        dateLabel.setText(BookingService.thaiDate(BookingService.today()));   // วันที่แบบไทย (พ.ศ.)
        SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.US);              // รูปแบบ ชั่วโมง:นาที (24 ชม.)
        timeLabel.setText(format.format(new Date()) + " น.");                 // เวลาตอนนี้
    }                                                                         // จบ updateClock

    // อ่าน bookings.csv ใหม่ แล้วตั้งสถานะทุกห้อง: มีการจองที่ครอบคลุมเวลาตอนนี้ = ไม่ว่าง, ไม่มี = ว่าง
    // AppFrame เรียกตอนเปิดหน้า Admin และ Timer เรียกทุกต้นนาที
    public void refreshRoomStatus() {
        ArrayList<Booking> bookings = BookingService.loadBookings();   // อ่านการจองทั้งหมดจากไฟล์
        String today = BookingService.today();                         // วันนี้ (dd/MM/yyyy)
        int now = BookingService.nowMinutes();                         // เวลาตอนนี้เป็นนาที
        String[] ids = roomStatus.getRoomIds();                        // รหัสห้องแต่ละแถวในกล่อง
        for (int i = 0; i < ids.length; i++) {                         // วนทุกห้อง
            Booking b = BookingService.findCurrent(bookings, ids[i], today, now);   // การจองที่กำลังใช้ห้องนี้อยู่
            if (b != null) {                                           // มีการจองในช่วงเวลานี้
                String time = BookingService.timeRange(b);                       // เช่น "13:00 - 15:00"
                roomStatus.setStatus(i, true, time);                   // → ไม่ว่าง + ช่วงเวลา
            } else {                                                   // ไม่มีการจองช่วงนี้
                roomStatus.setStatus(i, false, "");                    // → ว่าง
            }                                                          // จบ if
        }                                                              // จบ for
        roomStatus.repaint();                                          // วาดกล่องใหม่ (บทที่ 12)
    }                                                                  // จบ refreshRoomStatus

    // อ่าน bookings.csv ใหม่ แล้วแสดงการจองล่าสุด (บรรทัดท้ายไฟล์ = จองล่าสุด) ไม่เกิน 5 รายการ
    // AppFrame เรียกทุกครั้งที่เปิดหน้า Admin
    public void refreshLatest() {
        latestRows.removeAll();                                       // ลบแถวเดิมทั้งหมดก่อน
        ArrayList<Booking> bookings = BookingService.newestFirst(BookingService.loadBookings());   // อ่านไฟล์ แล้วเรียงใหม่ไปเก่า
        int[] c = LATEST_CENTERS;                                     // กลางคอลัมน์ (ชื่อสั้นจะได้อ่านง่าย)
        int row = 0;                                                  // แถวที่กำลังสร้าง (0 = แถวบนสุด)
        for (int i = 0; i < bookings.size() && row < LATEST_MAX_ROWS; i++) {       // วนจากรายการล่าสุด จนครบ 5 แถว
            Booking b = bookings.get(i);                              // การจองที่ i
            int y = row * LATEST_ROW_H;                               // y ของแถวนี้
            String time = BookingService.timeRange(b);                       // เช่น "13:00 - 15:00"
            String[] data = {"" + (row + 1), b.getId(), "ห้อง " + b.getRoomId(),
                             BookingService.thaiDate(b.getDate()), time};   // ข้อความ 5 คอลัมน์แรก
            for (int k = 0; k < data.length; k++) {                   // วนใส่ข้อมูลทีละคอลัมน์
                addCenterLabel(latestRows, data[k], c[k], y, LATEST_ROW_H);   // ข้อความกลางคอลัมน์
            }                                                         // จบ for

            StatusBadge status;                                       // ป้ายสถานะการจอง
            if (b.isActive()) {                                       // ยังไม่ยกเลิก
                status = new StatusBadge("จองสำเร็จ", new Color(0xDFF7DA), Theme.STAT_GREEN, 72, 20);   // ป้ายเขียว
            } else {                                                  // ยกเลิกแล้ว
                status = new StatusBadge("ยกเลิกแล้ว", new Color(0xFDE0E0), Theme.DANGER, 72, 20);     // ป้ายแดง
            }                                                         // จบ if
            status.setBounds(c[5] - 36, y + 9, 72, 20);               // กลางคอลัมน์สถานะ
            latestRows.add(status);                                   // ใส่ลงในตาราง

            JPanel line = new JPanel();                               // เส้นใต้แถว = JPanel สูง 1 พิกเซล
            line.setBackground(Theme.BORDER);                         // สีเส้นอ่อน
            line.setBounds(0, y + LATEST_ROW_H - 1, 638, 1);          // ชิดล่างของแถว
            latestRows.add(line);                                     // ใส่ลงในตาราง
            row++;                                                    // แถวถัดไป
        }                                                             // จบ for
        if (row == 0) {                                               // ไฟล์ยังไม่มีการจองเลย
            JLabel empty = new JLabel("ยังไม่มีการจอง", JLabel.CENTER);   // ข้อความแทนตาราง
            empty.setFont(Theme.font(Font.PLAIN, 12));                // ตัวปกติ 12
            empty.setForeground(Theme.TEXT_GRAY);                     // สีเทา
            empty.setBounds(0, 0, 638, LATEST_ROW_H);                 // แถวแรก
            latestRows.add(empty);                                    // ใส่ลงในตาราง
        }                                                             // จบ if
        latestRows.repaint();                                         // วาดตารางใหม่ (บทที่ 12)
    }                                                                 // จบ refreshLatest

    // เมธอดช่วย: สร้างป้ายข้อความชิดซ้ายแล้วใส่ลงใน panel
    private void addLabel(JPanel panel, String text, int style, int size, int x, int y, int w, int h) {
        JLabel l = new JLabel(text);                 // สร้างป้าย
        l.setFont(Theme.font(style, size));          // ฟอนต์ตามที่ส่งมา
        l.setForeground(Theme.TEXT);                 // สีเข้ม
        l.setBounds(x, y, w, h);                     // ตำแหน่งตามที่ส่งมา
        panel.add(l);                                // ใส่ลงใน panel
    }                                                // จบ addLabel

    // เมธอดช่วย: ข้อความจัดกลางที่ตำแหน่ง center (ใช้กับแถวตาราง)
    private void addCenterLabel(JPanel panel, String text, int center, int y, int h) {
        JLabel l = new JLabel(text, JLabel.CENTER);  // ข้อความจัดกลาง
        l.setFont(Theme.font(Font.PLAIN, 11));       // ตัวเล็ก 11
        l.setForeground(Theme.TEXT);                 // สีเข้ม
        l.setBounds(center - 60, y, 120, h);         // กว้าง 120 ให้ center อยู่ตรงกลาง
        panel.add(l);                                // ใส่ลงใน panel
    }                                                // จบ addCenterLabel

    // getter: ให้ AppFrame / logic เข้าถึงส่วนต่าง ๆ
    public AdminSidebar getSidebar()          { return sidebar; }
    public JLabel getAdminName()              { return adminName; }
    public JLabel getDateLabel()              { return dateLabel; }
    public JLabel getTimeLabel()              { return timeLabel; }
    public AdminStatCard getTotalRoomsCard()  { return totalRoomsCard; }
    public AdminStatCard getFreeRoomsCard()   { return freeRoomsCard; }
    public AdminStatCard getBusyRoomsCard()   { return busyRoomsCard; }
    public AdminStatCard getUsersCard()       { return usersCard; }
    public RoomStatusPanel getRoomStatus()    { return roomStatus; }
    public JLabel getSeeAll()                 { return seeAll; }
    public IconPanel getSeeAllArrow()         { return seeAllArrow; }
}                                                                       // จบคลาส AdminPage
