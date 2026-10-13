package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font
import javax.swing.*;   // JPanel, JLabel

/*
 * แถบเมนูด้านซ้าย (กว้าง 268)
 */
public class SidebarPanel extends JPanel {   // สืบทอด JPanel

    private SidebarItem homeItem;         // เมนู "หน้าหลัก"
    private SidebarItem allRoomsItem;     // เมนู "ดูห้องทั้งหมด"
    private SidebarItem bookRoomItem;     // เมนู "จองห้อง"
    private SidebarItem myBookingsItem;   // เมนู "การจองของฉัน"
    private SidebarItem profileItem;      // เมนู "ข้อมูลส่วนตัว"

    public SidebarPanel() {                   // คอนสตรักเตอร์
        setLayout(null);                      // วางของด้วย setBounds
        setBackground(Theme.SIDEBAR);         // พื้นสีน้ำเงินเข้ม

        // ---------- โลโก้ ----------
        IconPanel logo = new IconPanel(IconPanel.BOOK, 46, Theme.WHITE);   // ไอคอนหนังสือสีขาว ขนาด 46
        logo.setBounds(26, 40, 46, 46);                                    // วางมุมซ้ายบน
        add(logo);                                                         // ใส่ลงใน sidebar

        JLabel logoText = new JLabel("Library Booking");   // ชื่อระบบข้างโลโก้
        logoText.setFont(Theme.font(Font.BOLD, 20));       // ตัวหนา 20
        logoText.setForeground(Theme.WHITE);               // สีขาว
        logoText.setBounds(84, 46, 180, 34);               // วางทางขวาของโลโก้
        add(logoText);                                     // ใส่ลงใน sidebar

        // ---------- เมนู ----------
        homeItem       = new SidebarItem(IconPanel.HOME,     "หน้าหลัก");        // สร้างเมนูหน้าหลัก
        allRoomsItem   = new SidebarItem(IconPanel.BUILDING, "ดูห้องทั้งหมด");    // สร้างเมนูดูห้องทั้งหมด
        bookRoomItem   = new SidebarItem(IconPanel.HOME_ADD, "จองห้อง");         // สร้างเมนูจองห้อง
        myBookingsItem = new SidebarItem(IconPanel.CALENDAR, "การจองของฉัน");    // สร้างเมนูการจองของฉัน
        profileItem    = new SidebarItem(IconPanel.USER,     "ข้อมูลส่วนตัว");    // สร้างเมนูข้อมูลส่วนตัว

        homeItem.setBounds(24, 128, 216, 62);         // วางเมนูที่ 1 (กว้าง 216 สูง 62)
        allRoomsItem.setBounds(24, 210, 216, 62);     // วางเมนูที่ 2
        bookRoomItem.setBounds(24, 292, 216, 62);     // วางเมนูที่ 3
        myBookingsItem.setBounds(24, 396, 216, 62);   // วางเมนูที่ 4 (เว้นช่องห่างมากขึ้นตามดีไซน์)
        profileItem.setBounds(24, 482, 216, 62);      // วางเมนูที่ 5

        add(homeItem);         // ใส่เมนูทั้ง 5 ลงใน sidebar
        add(allRoomsItem);                                                                  // ใส่เมนูดูห้องทั้งหมดลงใน panel
        add(bookRoomItem);                                                                  // ใส่เมนูจองห้องลงใน panel
        add(myBookingsItem);                                                                // ใส่เมนูการจองของฉันลงใน panel
        add(profileItem);                                                                   // ใส่เมนูข้อมูลส่วนตัวลงใน panel
    }                          // จบคอนสตรักเตอร์

    // ไฮไลต์เมนูที่ส่งเข้ามา และยกเลิกไฮไลต์เมนูอื่น
    public void setActive(SidebarItem item) {                                               // item = เมนูที่จะไฮไลต์
        homeItem.setSelected(item == homeItem);               // true เฉพาะเมนูที่ตรงกับ item
        allRoomsItem.setSelected(item == allRoomsItem);       // เมนูอื่นจะได้ false (ไม่ไฮไลต์)
        bookRoomItem.setSelected(item == bookRoomItem);                                     // ไฮไลต์เมนูจองห้อง ถ้าตรงกับ item
        myBookingsItem.setSelected(item == myBookingsItem);                                 // ไฮไลต์เมนูการจองของฉัน ถ้าตรงกับ item
        profileItem.setSelected(item == profileItem);                                       // ไฮไลต์เมนูข้อมูลส่วนตัว ถ้าตรงกับ item
    }                                                         // จบ setActive

    // getter: ให้ AppFrame เอาเมนูไปผูก MouseListener
    public SidebarItem getHomeItem()       { return homeItem; }                             // คืนค่า homeItem
    public SidebarItem getAllRoomsItem()   { return allRoomsItem; }                         // คืนค่า allRoomsItem
    public SidebarItem getBookRoomItem()   { return bookRoomItem; }                         // คืนค่า bookRoomItem
    public SidebarItem getMyBookingsItem() { return myBookingsItem; }                       // คืนค่า myBookingsItem
    public SidebarItem getProfileItem()    { return profileItem; }                          // คืนค่า profileItem
}                                                             // จบคลาส SidebarPanel
