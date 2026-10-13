package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Font
import javax.swing.*;   // JPanel, JLabel

/*
 * หน้า "สมัครสมาชิก" เต็มหน้าต่าง (1440 x 860)
 * ซ้าย = แผงโลโก้สีเข้ม (ไม่มีรูป), ขวา = ฟอร์ม 2 คอลัมน์
 */
public class RegisterPage extends JPanel {   // สืบทอด JPanel

    private InputBox usernameBox;          // ช่อง Username/รหัสนิสิต
    private InputBox fullNameBox;          // ช่องชื่อ-นามสกุล
    private InputBox emailBox;             // ช่องอีเมล
    private InputBox phoneBox;             // ช่องเบอร์โทร
    private InputBox passwordBox;          // ช่องรหัสผ่าน
    private InputBox confirmPasswordBox;   // ช่องยืนยันรหัสผ่าน
    private RoundButton registerButton;    // ปุ่ม "สมัครสมาชิก"
    private RoundButton backButton;        // ปุ่ม "กลับไปหน้าเข้าสู่ระบบ"

    public RegisterPage() {                // คอนสตรักเตอร์
        setLayout(null);                   // วางของด้วย setBounds
        setBackground(Theme.WHITE);        // พื้นสีขาว (ฝั่งฟอร์ม)

        // ================= แผงซ้าย =================
        JPanel brand = new JPanel();             // แผงโลโก้
        brand.setLayout(null);                   // วางของด้วย setBounds
        brand.setBackground(Theme.SIDEBAR);      // พื้นสีน้ำเงินเข้ม
        brand.setBounds(0, 0, 560, 860);         // ชิดซ้าย สูงเต็มหน้าต่าง
        add(brand);                              // ใส่ลงในหน้า

        IconPanel logo = new IconPanel(IconPanel.BOOK, 72, Theme.WHITE);   // โลโก้หนังสือ ขนาด 72
        logo.setBounds(56, 40, 72, 72);                                    // มุมซ้ายบน
        brand.add(logo);                                                   // ใส่ลงในแผง

        JLabel title = new JLabel("Library Booking System");   // ชื่อระบบ
        title.setFont(Theme.font(Font.BOLD, 32));              // ตัวหนา 32
        title.setForeground(Theme.WHITE);                      // สีขาว
        title.setBounds(146, 50, 420, 44);                     // ทางขวาของโลโก้
        brand.add(title);                                      // ใส่ลงในแผง

        addWhiteText(brand, "ระบบจองห้องอ่านหนังสือ", 112);                         // ข้อความบรรทัดที่ 1
        addWhiteText(brand, "สำหรับนักศึกษาวิทยาการคอมพิวเตอร์  ภาคพิเศษ", 134);      // บรรทัดที่ 2
        addWhiteText(brand, "“จองห้องง่าย  เรียนรู้ได้อย่างมีประสิทธิภาพ”", 168);     // คำขวัญ

        JLabel footer = new JLabel("© 2026 Department of Computer Science. All rights reserved.");   // ข้อความลิขสิทธิ์
        footer.setFont(Theme.font(Font.PLAIN, 11));                                                 // ตัวเล็ก 11
        footer.setForeground(new Color(0x94A3B8));                                                  // สีเทาอ่อน
        footer.setBounds(12, 830, 500, 18);                                                         // มุมซ้ายล่าง
        brand.add(footer);                                                                          // ใส่ลงในแผง

        // ================= ฟอร์มด้านขวา =================
        IconPanel avatar = new IconPanel(IconPanel.AVATAR, 56, Theme.SECONDARY, Theme.WHITE);   // รูปโปรไฟล์ ขนาด 56
        avatar.setBounds(604, 36, 56, 56);                                                    // หน้าหัวข้อ
        add(avatar);                                                                          // ใส่ลงในหน้า

        JLabel heading = new JLabel("สมัครสมาชิก");      // หัวข้อ
        heading.setFont(Theme.font(Font.BOLD, 32));     // ตัวหนา 32
        heading.setForeground(Theme.TEXT);              // สีเข้ม
        heading.setBounds(676, 28, 400, 44);            // ทางขวาของรูปโปรไฟล์
        add(heading);                                   // ใส่ลงในหน้า

        JLabel sub = new JLabel("กรอกข้อมูลของคุณเพื่อสร้างบัญชีผู้ใช้");   // หัวข้อรอง
        sub.setFont(Theme.font(Font.PLAIN, 16));                          // ตัวปกติ 16
        sub.setForeground(Theme.TEXT);                                    // สีเข้ม
        sub.setBounds(676, 70, 400, 24);                                  // ใต้หัวข้อ
        add(sub);                                                         // ใส่ลงในหน้า

        // คอลัมน์ซ้าย x = 610, คอลัมน์ขวา x = 952  (แถวห่างกันทีละ 104)
        usernameBox        = addField(IconPanel.PERSON, "Username\\รหัสนิสิต", "กรอกรหัสนิสิต (ตัวเลขเท่านั้น)", false, 610, 136);   // \\ = เขียนตัว \ หนึ่งตัวในสตริง
        fullNameBox        = addField(IconPanel.PERSON, "ชื่อ - นามสกุล",      "เช่น สมชาย ใจกราม",          false, 952, 136);  // ช่อง "ชื่อ - นามสกุล" (ไอคอน PERSON)
        emailBox           = addField(IconPanel.MAIL,   "อีเมล",              "example@gmail.com",          false, 610, 240);  // ช่อง "อีเมล" (ไอคอน MAIL)
        phoneBox           = addField(IconPanel.PHONE,  "เบอร์โทรศัพท์",        "เช่น 081-234-5678",          false, 952, 240);  // ช่อง "เบอร์โทรศัพท์" (ไอคอน PHONE)
        passwordBox        = addField(IconPanel.LOCK,   "รหัสผ่าน",            "อย่างน้อย 8 ตัวอักษร",         true,  610, 344);   // true = ช่องรหัสผ่าน
        confirmPasswordBox = addField(IconPanel.LOCK,   "ยืนยันรหัสผ่าน",        "กรอกรหัสผ่านอีกครั้ง",         true,  952, 344);  // ช่อง "ยืนยันรหัสผ่าน" (ไอคอน LOCK)
        usernameBox.setDigitsOnly(true);   // ช่องรหัสนิสิต: พิมพ์ได้แค่ตัวเลข

        // ---------- ปุ่ม ----------
        registerButton = new RoundButton("สมัครสมาชิก");                                     // ปุ่มน้ำเงิน
        registerButton.setFont(Theme.font(Font.PLAIN, 14));                                 // ตัวปกติ 14
        registerButton.setIconPanel(new IconPanel(IconPanel.USER_PLUS, 20, Theme.WHITE));   // ไอคอนคน + บวก สีขาว
        registerButton.setBounds(706, 730, 422, 36);                                        // กลางใต้ฟอร์ม 2 คอลัมน์
        add(registerButton);                                                                // ใส่ลงในหน้า

        backButton = new RoundButton("กลับไปหน้าเข้าสู่ระบบ");                                // ปุ่มกลับ
        backButton.setFont(Theme.font(Font.PLAIN, 14));                                     // ตัวปกติ 14
        backButton.setColors(Theme.WHITE, Theme.SECONDARY, Theme.SECONDARY);                // พื้นขาว ตัวและขอบน้ำเงิน
        backButton.setIconPanel(new IconPanel(IconPanel.ARROW_LEFT, 18, Theme.SECONDARY));  // ลูกศรชี้ซ้าย
        backButton.setBounds(706, 792, 422, 36);                                            // ใต้ปุ่มสมัคร
        add(backButton);                                                                    // ใส่ลงในหน้า
    }                                                                                       // จบคอนสตรักเตอร์

    // ไอคอน + ชื่อช่อง แล้วตามด้วยช่องกรอกด้านล่าง
    private InputBox addField(int iconType, String label, String hint, boolean password, int x, int y) {  // คืนค่าเป็นช่องกรอกที่สร้าง
        IconPanel icon = new IconPanel(iconType, 20, Theme.TEXT);   // ไอคอนหน้าชื่อช่อง
        icon.setBounds(x, y, 20, 20);                               // วางที่ (x, y)
        add(icon);                                                  // ใส่ลงในหน้า

        JLabel l = new JLabel(label);                  // ชื่อช่อง
        l.setFont(Theme.font(Font.PLAIN, 14));         // ตัวปกติ 14
        l.setForeground(Theme.TEXT);                   // สีเข้ม
        l.setBounds(x + 30, y, 240, 20);               // ทางขวาของไอคอน
        add(l);                                        // ใส่ลงในหน้า

        InputBox box = new InputBox(hint, password, -1, 272, 36);   // ช่องกรอก (-1 = ไม่มีไอคอนในช่อง)
        box.setBounds(x, y + 32, 272, 36);                          // วางใต้ชื่อช่อง 32 พิกเซล
        add(box);                                                   // ใส่ลงในหน้า
        return box;                                                 // คืนช่องกรอก ให้เก็บไว้ในตัวแปร
    }                                                               // จบ addField

    private void addWhiteText(JPanel panel, String text, int y) {   // เมธอดช่วย: ข้อความสีขาวในแผงซ้าย
        JLabel l = new JLabel(text);                   // สร้างป้าย
        l.setFont(Theme.font(Font.PLAIN, 15));         // ตัวปกติ 15
        l.setForeground(Theme.WHITE);                  // สีขาว
        l.setBounds(146, y, 400, 22);                  // ชิดแนวเดียวกับชื่อระบบ
        panel.add(l);                                  // ใส่ลงในแผง
    }                                                  // จบ addWhiteText

    // getter: AppFrame ผูกปุ่ม, logic อ่านค่าเช่น getEmailBox().getText()
    public InputBox getUsernameBox()          { return usernameBox; }                       // คืนค่า usernameBox
    public InputBox getFullNameBox()          { return fullNameBox; }                       // คืนค่า fullNameBox
    public InputBox getEmailBox()             { return emailBox; }                          // คืนค่า emailBox
    public InputBox getPhoneBox()             { return phoneBox; }                          // คืนค่า phoneBox
    public InputBox getPasswordBox()          { return passwordBox; }                       // คืนค่า passwordBox
    public InputBox getConfirmPasswordBox()   { return confirmPasswordBox; }                // คืนค่า confirmPasswordBox
    public RoundButton getRegisterButton()    { return registerButton; }                    // คืนค่า registerButton
    public RoundButton getBackButton()        { return backButton; }                        // คืนค่า backButton
}                                                                      // จบคลาส RegisterPage
