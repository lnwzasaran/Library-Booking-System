package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Font
import javax.swing.*;   // JPanel, JLabel

/*
 * แถบเมนูด้านซ้ายของผู้ดูแลระบบ (กว้าง 352) — มีเมนูเดียวคือ "หน้าหลัก"
 */
public class AdminSidebar extends JPanel {   // สืบทอด JPanel

    private SidebarItem homeItem;            // เมนู "หน้าหลัก" (ใช้คลาส SidebarItem ตัวเดียวกับฝั่งนักศึกษา)

    public AdminSidebar() {                  // คอนสตรักเตอร์
        setLayout(null);                     // วางของด้วย setBounds
        setBackground(Theme.SIDEBAR);        // พื้นสีน้ำเงินเข้ม

        IconPanel logo = new IconPanel(IconPanel.BOOK, 54, Theme.WHITE);   // โลโก้หนังสือสีขาว ขนาด 54
        logo.setBounds(18, 18, 54, 54);                                    // มุมซ้ายบน
        add(logo);                                                         // ใส่ลงใน sidebar

        JLabel title = new JLabel("Library Booking");      // ชื่อระบบ
        title.setFont(Theme.font(Font.BOLD, 20));          // ตัวหนา 20
        title.setForeground(Theme.WHITE);                  // สีขาว
        title.setBounds(89, 16, 250, 30);                  // ทางขวาของโลโก้
        add(title);                                        // ใส่ลงใน sidebar

        JLabel role = new JLabel("Admin");                 // บอกว่าเป็นฝั่งผู้ดูแลระบบ
        role.setFont(Theme.font(Font.PLAIN, 16));          // ตัวปกติ 16
        role.setForeground(Theme.WHITE);                   // สีขาว
        role.setBounds(89, 46, 200, 24);                   // ใต้ชื่อระบบ
        add(role);                                         // ใส่ลงใน sidebar

        homeItem = new SidebarItem(IconPanel.HOME, "หน้าหลัก");   // เมนูหน้าหลัก
        homeItem.setBounds(21, 106, 308, 60);                     // กว้างกว่าฝั่งนักศึกษา ตามดีไซน์
        homeItem.setSelected(true);                               // ไฮไลต์ไว้ตลอด (มีเมนูเดียว)
        add(homeItem);                                            // ใส่ลงใน sidebar
    }                                                             // จบคอนสตรักเตอร์

    public SidebarItem getHomeItem() {   // getter: เผื่อผูก MouseListener ภายหลัง
        return homeItem;                 // คืนเมนูหน้าหลัก
    }                                    // จบ getHomeItem
}                                        // จบคลาส AdminSidebar
