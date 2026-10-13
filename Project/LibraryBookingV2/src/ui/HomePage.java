package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;          // Font
import javax.swing.*;       // JPanel, JLabel
import java.util.ArrayList; // ลิสต์ห้อง / ลิสต์การจอง ที่อ่านจากไฟล์ csv

/*
 * หน้าหลัก (Dashboard) — ขนาด 1172 x 860 (หน้านี้ไม่มี header ตามดีไซน์)
 */
public class HomePage extends JPanel {   // สืบทอด JPanel

    private JLabel nameLabel;            // ป้าย "สวัสดี ..." (เปลี่ยนเป็นชื่อจริงตอน login ได้)
    private JLabel studentIdLabel;       // ป้ายรหัสนักศึกษา

    private StatCard totalRoomsCard;     // การ์ด "ห้องทั้งหมด"
    private StatCard availableRoomsCard; // การ์ด "ห้องว่าง"
    private StatCard myBookingsCard;     // การ์ด "การจองห้องของฉัน"
    private StatCard todayBookingsCard;  // การ์ด "การจองวันนี้"

    private MenuCard bookRoomMenu;       // เมนูลัด "จองห้อง"
    private MenuCard allRoomsMenu;       // เมนูลัด "ดูห้องทั้งหมด"
    private MenuCard myBookingsMenu;     // เมนูลัด "การจองของฉัน"
    private MenuCard profileMenu;        // เมนูลัด "โปรไฟล์"

    public HomePage() {                       // คอนสตรักเตอร์
        setLayout(null);                      // วางของด้วย setBounds
        setBackground(Theme.BACKGROUND);      // พื้นสีอ่อน

        // ---------- การ์ดต้อนรับ ----------
        RoundPanel welcome = new RoundPanel(Theme.WELCOME_BG, null);   // กล่องเทาอ่อน ไม่มีขอบ
        welcome.setBounds(10, 46, 1138, 162);                              // กว้างเกือบเต็มหน้า
        add(welcome);                                                      // ใส่ลงในหน้า

        IconPanel avatar = new IconPanel(IconPanel.AVATAR, 80, Theme.SECONDARY, Theme.WELCOME_BG);   // รูปโปรไฟล์ ขนาด 80 (ด้านในสีเดียวกับพื้นการ์ด)
        avatar.setBounds(40, 41, 80, 80);                                                          // วางด้านซ้ายของการ์ด
        welcome.add(avatar);                                                                       // ใส่ลงในการ์ดต้อนรับ

        nameLabel = new JLabel("สวัสดี นาย วชิรศักดิ์ โยโย่");   // ข้อความต้อนรับ (ข้อมูลตัวอย่าง)
        nameLabel.setFont(Theme.font(Font.BOLD, 26));           // ตัวหนา 26
        nameLabel.setForeground(Theme.TEXT);                    // สีเข้ม
        nameLabel.setBounds(142, 44, 700, 40);                  // วางทางขวาของรูป
        welcome.add(nameLabel);                                 // ใส่ลงในการ์ดต้อนรับ

        studentIdLabel = new JLabel("รหัสนักศึกษา   6821651789");   // รหัสนักศึกษา (ข้อมูลตัวอย่าง)
        studentIdLabel.setFont(Theme.font(Font.PLAIN, 16));         // ตัวปกติ 16
        studentIdLabel.setForeground(Theme.TEXT);                   // สีเข้ม
        studentIdLabel.setBounds(142, 92, 400, 26);                 // วางใต้ชื่อ
        welcome.add(studentIdLabel);                                // ใส่ลงในการ์ดต้อนรับ

        // ---------- หัวข้อ ----------
        JLabel title = new JLabel("ภาพรวมการใช้งาน");    // หัวข้อส่วนการ์ดสรุป
        title.setFont(Theme.font(Font.BOLD, 24));        // ตัวหนา 24
        title.setForeground(Theme.TEXT);                 // สีเข้ม
        title.setBounds(36, 222, 400, 36);               // วางใต้การ์ดต้อนรับ
        add(title);                                      // ใส่ลงในหน้า

        // ---------- การ์ดสรุป (ข้อมูลตัวอย่าง) ----------
        totalRoomsCard     = new StatCard(IconPanel.DOOR, Theme.SECONDARY, Theme.STAT_BLUE_BG, "ห้องทั้งหมด", "10 ห้อง");                  // การ์ดสีฟ้า
        availableRoomsCard = new StatCard(IconPanel.CALENDAR_CHECK, Theme.STAT_GREEN, Theme.STAT_GREEN_BG, "ห้องว่าง", "10 ห้อง");          // การ์ดสีเขียว
        myBookingsCard     = new StatCard(IconPanel.LIST, Theme.STAT_PURPLE, Theme.STAT_PURPLE_BG, "การจองห้องของฉัน", "1 ห้อง");          // การ์ดสีม่วง
        todayBookingsCard  = new StatCard(IconPanel.LIST_ADD, Theme.DANGER, Theme.STAT_RED_BG, "การจองวันนี้", "0 รายการ");                // การ์ดสีแดง

        totalRoomsCard.setBounds(38, 268, 200, 192);       // การ์ดที่ 1 (ห่างกันทีละ 303 พิกเซล)
        availableRoomsCard.setBounds(341, 268, 200, 192);  // การ์ดที่ 2
        myBookingsCard.setBounds(644, 268, 200, 192);      // การ์ดที่ 3
        todayBookingsCard.setBounds(947, 268, 200, 192);   // การ์ดที่ 4
        add(totalRoomsCard);                               // ใส่การ์ดทั้ง 4 ลงในหน้า
        add(availableRoomsCard);                                                            // ใส่การ์ดห้องว่างลงใน panel
        add(myBookingsCard);                                                                // ใส่การ์ดการจองของฉันลงใน panel
        add(todayBookingsCard);                                                             // ใส่การ์ดการจองวันนี้ลงใน panel

        // ---------- เมนูลัด ----------
        bookRoomMenu   = new MenuCard(IconPanel.HOME_ADD, Theme.TILE_BLUE, "จองห้อง", "เลือกวันที่  เวลา", "ห้องที่ต้องการจอง");          // กล่องน้ำเงิน
        allRoomsMenu   = new MenuCard(IconPanel.BUILDING, Theme.TILE_GREEN, "ดูห้องทั้งหมด", "ตรวจสอบสถานะ", "รายละเอียดการจอง");       // กล่องเขียว
        myBookingsMenu = new MenuCard(IconPanel.CALENDAR, Theme.TILE_ORANGE, "การจองของฉัน", "ดูสถานะการจอง", "");                     // กล่องส้ม (คำอธิบายบรรทัดเดียว)
        profileMenu    = new MenuCard(IconPanel.USER, Theme.TILE_PURPLE, "โปรไฟล์", "จัดการข้อมูลส่วนตัว", "");                           // กล่องม่วง (คำอธิบายบรรทัดเดียว)

        bookRoomMenu.setBounds(38, 496, 200, 264);      // แถวล่าง ตำแหน่ง x เดียวกับการ์ดสรุป
        allRoomsMenu.setBounds(341, 496, 200, 264);                                         // วางเมนูลัดดูห้องทั้งหมด ที่ x=341 y=496 กว้าง 200 สูง 264
        myBookingsMenu.setBounds(644, 496, 200, 264);                                       // วางเมนูลัดการจองของฉัน ที่ x=644 y=496 กว้าง 200 สูง 264
        profileMenu.setBounds(947, 496, 200, 264);                                          // วางเมนูลัดโปรไฟล์ ที่ x=947 y=496 กว้าง 200 สูง 264
        add(bookRoomMenu);                              // ใส่เมนูลัดทั้ง 4 ลงในหน้า
        add(allRoomsMenu);                                                                  // ใส่เมนูลัดดูห้องทั้งหมดลงใน panel
        add(myBookingsMenu);                                                                // ใส่เมนูลัดการจองของฉันลงใน panel
        add(profileMenu);                                                                   // ใส่เมนูลัดโปรไฟล์ลงใน panel
    }                                                   // จบคอนสตรักเตอร์

    // อ่านข้อมูลจากไฟล์ csv แล้วอัปเดตตัวเลขในการ์ดสรุปทั้ง 4 ใบ (AppFrame เรียกทุกครั้งที่เปิดหน้าหลัก)
    public void refreshStats(String studentId) {
        ArrayList<Room> rooms = RoomService.loadRooms();              // อ่านห้องทั้งหมดจาก data/rooms.csv
        ArrayList<Booking> bookings = BookingService.loadBookings();  // อ่านการจองทั้งหมดจาก data/bookings.csv
        String today = BookingService.today();                       // วันที่วันนี้ เช่น 13/10/2026

        int total = rooms.size();                                                  // ห้องทั้งหมด = จำนวนแถวใน rooms.csv
        int free = RoomService.countFreeRooms(rooms, bookings, today);             // ห้องที่ไม่มีการจองวันนี้
        int mine = BookingService.countMyBookings(bookings, studentId);            // การจองที่ยังไม่ยกเลิกของนิสิตคนนี้
        int todayCount = BookingService.countToday(bookings, today);               // การจองทั้งหมดของวันนี้

        totalRoomsCard.getValueLabel().setText(total + " ห้อง");          // เปลี่ยนตัวเลขบนการ์ด "ห้องทั้งหมด"
        availableRoomsCard.getValueLabel().setText(free + " ห้อง");       // การ์ด "ห้องว่าง"
        myBookingsCard.getValueLabel().setText(mine + " ห้อง");           // การ์ด "การจองห้องของฉัน"
        todayBookingsCard.getValueLabel().setText(todayCount + " รายการ"); // การ์ด "การจองวันนี้"
    }                                                                     // จบ refreshStats

    // getter: ให้ AppFrame / logic เข้าถึงส่วนต่าง ๆ ของหน้า
    public JLabel getNameLabel()               { return nameLabel; }                        // คืนค่า nameLabel
    public JLabel getStudentIdLabel()          { return studentIdLabel; }                   // คืนค่า studentIdLabel
    public StatCard getTotalRoomsCard()        { return totalRoomsCard; }                   // คืนค่า totalRoomsCard
    public StatCard getAvailableRoomsCard()    { return availableRoomsCard; }               // คืนค่า availableRoomsCard
    public StatCard getMyBookingsCard()        { return myBookingsCard; }                   // คืนค่า myBookingsCard
    public StatCard getTodayBookingsCard()     { return todayBookingsCard; }                // คืนค่า todayBookingsCard
    public MenuCard getBookRoomMenu()          { return bookRoomMenu; }                     // คืนค่า bookRoomMenu
    public MenuCard getAllRoomsMenu()          { return allRoomsMenu; }                     // คืนค่า allRoomsMenu
    public MenuCard getMyBookingsMenu()        { return myBookingsMenu; }                   // คืนค่า myBookingsMenu
    public MenuCard getProfileMenu()           { return profileMenu; }                      // คืนค่า profileMenu
}                                                                                           // จบคลาส HomePage
