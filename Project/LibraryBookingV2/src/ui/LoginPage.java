package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Font
import javax.swing.*;   // JPanel, JLabel

/*
 * หน้า "เข้าสู่ระบบ" เต็มหน้าต่าง (1440 x 860)
 * การ์ดกลางจอ: ซ้าย = แผงโลโก้สีเข้ม (ไม่มีรูป), ขวา = ฟอร์ม
 */
public class LoginPage extends JPanel {   // สืบทอด JPanel

    private InputBox studentIdBox;        // ช่องรหัสนิสิต
    private InputBox passwordBox;         // ช่องรหัสผ่าน
    private RoundButton loginButton;      // ปุ่ม "เข้าสู่ระบบ"
    private RoundButton registerButton;   // ปุ่ม "สมัครสมาชิก"

    public LoginPage() {                  // คอนสตรักเตอร์
        setLayout(null);                  // วางของด้วย setBounds
        setBackground(Theme.WHITE);       // พื้นหน้าต่างสีขาว

        // ================= แผงซ้าย =================
        JPanel brand = new JPanel();             // แผงโลโก้
        brand.setLayout(null);                   // วางของในแผงด้วย setBounds
        brand.setBackground(Theme.SIDEBAR);      // พื้นสีน้ำเงินเข้ม
        brand.setBounds(127, 71, 726, 718);      // ครึ่งซ้ายของการ์ดกลางจอ
        add(brand);                              // ใส่ลงในหน้า

        IconPanel logo = new IconPanel(IconPanel.BOOK, 72, Theme.WHITE);   // โลโก้หนังสือ ขนาด 72
        logo.setBounds(58, 56, 72, 72);                                    // มุมซ้ายบนของแผง
        brand.add(logo);                                                   // ใส่ลงในแผง

        // "Library " สีขาว ต่อด้วย "Booking System" สีน้ำเงิน (ใช้ JLabel 2 อันวางต่อกัน)
        JLabel title1 = new JLabel("Library ");             // คำแรก
        title1.setFont(Theme.font(Font.BOLD, 32));          // ตัวหนา 32
        title1.setForeground(Theme.WHITE);                  // สีขาว
        int title1Width = title1.getPreferredSize().width;  // ความกว้างจริงของคำว่า "Library " (พิกเซล)
        title1.setBounds(146, 62, title1Width, 44);         // วางทางขวาของโลโก้
        brand.add(title1);                                  // ใส่ลงในแผง

        JLabel title2 = new JLabel("Booking System");              // คำที่สอง
        title2.setFont(Theme.font(Font.BOLD, 32));                 // ตัวหนา 32
        title2.setForeground(Theme.SECONDARY);                     // สีน้ำเงิน
        title2.setBounds(146 + title1Width, 62, 320, 44);          // วางต่อท้าย "Library " พอดี
        brand.add(title2);                                         // ใส่ลงในแผง

        addWhiteText(brand, "ระบบจองห้องอ่านหนังสือ", 118);                         // ข้อความบรรทัดที่ 1
        addWhiteText(brand, "สำหรับนักศึกษาวิทยาการคอมพิวเตอร์  ภาคพิเศษ", 140);      // บรรทัดที่ 2
        addWhiteText(brand, "“จองห้องง่าย  เรียนรู้ได้อย่างมีประสิทธิภาพ”", 174);     // คำขวัญ

        JLabel footer = new JLabel("© 2026 Department of Computer Science. All rights reserved.");   // ข้อความลิขสิทธิ์
        footer.setFont(Theme.font(Font.PLAIN, 11));                                                 // ตัวเล็ก 11
        footer.setForeground(new Color(0x94A3B8));                                                  // สีเทาอ่อน
        footer.setBounds(12, 690, 500, 18);                                                         // มุมซ้ายล่างของแผง
        brand.add(footer);                                                                          // ใส่ลงในแผง

        // ================= แผงขวา (ฟอร์ม) =================
        JPanel right = new JPanel();             // แผงฟอร์ม
        right.setLayout(null);                   // วางของด้วย setBounds
        right.setBackground(Theme.LOGIN_PANEL);  // พื้นเทาอ่อน
        right.setBounds(853, 71, 460, 718);      // ครึ่งขวาของการ์ด (ต่อจากแผงซ้าย 127+726 = 853)
        add(right);                              // ใส่ลงในหน้า

        IconPanel book = new IconPanel(IconPanel.BOOK_OPEN, 46, Theme.SECONDARY);   // หนังสือเปิดสีน้ำเงิน
        book.setBounds(207, 88, 46, 46);                                       // x = (460-46)/2 = 207 → กลางแผง
        right.add(book);                                                       // ใส่ลงในแผง

        JLabel welcome = new JLabel("ยินดีต้อนรับ", JLabel.CENTER);   // หัวข้อ จัดกลาง
        welcome.setFont(Theme.font(Font.BOLD, 26));                  // ตัวหนา 26
        welcome.setForeground(Theme.TEXT);                           // สีเข้ม
        welcome.setBounds(0, 140, 460, 36);                          // กว้างเต็มแผง
        right.add(welcome);                                          // ใส่ลงในแผง

        JLabel sub = new JLabel("เข้าสู่ระบบจองห้องอ่านหนังสือ", JLabel.CENTER);   // หัวข้อรอง
        sub.setFont(Theme.font(Font.PLAIN, 15));                                  // ตัวปกติ 15
        sub.setForeground(Theme.TEXT);                                            // สีเข้ม
        sub.setBounds(0, 180, 460, 22);                                           // ใต้หัวข้อ
        right.add(sub);                                                           // ใส่ลงในแผง

        addBoldLabel(right, "รหัสนิสิต", 52, 244);                                                  // ป้ายชื่อช่อง
        studentIdBox = new InputBox("กรอกรหัสนิสิต", false, IconPanel.PERSON, 356, 38);            // ช่องกรอก มีไอคอนคน
        studentIdBox.setBounds(52, 266, 356, 38);                                                  // วางใต้ป้าย
        right.add(studentIdBox);                                                                   // ใส่ลงในแผง

        addBoldLabel(right, "Password", 52, 334);                                     // ป้ายชื่อช่อง
        passwordBox = new InputBox("Password", true, IconPanel.LOCK, 356, 38);        // ช่องรหัสผ่าน (true) มีไอคอนกุญแจ
        passwordBox.setBounds(52, 356, 356, 38);                                      // วางใต้ป้าย
        right.add(passwordBox);                                                       // ใส่ลงในแผง

        loginButton = new RoundButton("เข้าสู่ระบบ");                                    // ปุ่มน้ำเงิน
        loginButton.setIconPanel(new IconPanel(IconPanel.LOGIN, 18, Theme.WHITE));     // ไอคอนเข้าสู่ระบบสีขาว
        loginButton.setBounds(52, 438, 356, 36);                                       // กว้างเท่าช่องกรอก
        right.add(loginButton);                                                        // ใส่ลงในแผง

        // เส้นคั่น ---- หรือ ----
        JPanel line1 = new JPanel();                    // เส้นซ้าย
        line1.setBackground(Theme.CARD_OUTLINE);        // สีเทา
        line1.setBounds(52, 520, 144, 1);               // สูง 1 พิกเซล
        right.add(line1);                               // ใส่ลงในแผง

        JLabel or = new JLabel("หรือ", JLabel.CENTER);   // คำว่า "หรือ" ตรงกลาง
        or.setFont(Theme.font(Font.PLAIN, 12));         // ตัวเล็ก 12
        or.setForeground(Theme.TEXT_GRAY);              // สีเทา
        or.setBounds(196, 510, 68, 20);                 // อยู่ระหว่างเส้นซ้ายกับเส้นขวา
        right.add(or);                                  // ใส่ลงในแผง

        JPanel line2 = new JPanel();                    // เส้นขวา
        line2.setBackground(Theme.CARD_OUTLINE);        // สีเทา
        line2.setBounds(264, 520, 144, 1);              // สูง 1 พิกเซล
        right.add(line2);                               // ใส่ลงในแผง

        registerButton = new RoundButton("สมัครสมาชิก");                                     // ปุ่มสมัครสมาชิก
        registerButton.setColors(Theme.WHITE, Theme.SECONDARY, Theme.FIELD_BORDER);         // พื้นขาว ตัวน้ำเงิน ขอบเทา
        registerButton.setIconPanel(new IconPanel(IconPanel.USER_PLUS, 18, Theme.SECONDARY));   // ไอคอนคน + บวก
        registerButton.setBounds(52, 550, 356, 36);                                         // ใต้เส้นคั่น
        right.add(registerButton);                                                          // ใส่ลงในแผง

        JLabel note = new JLabel("สำหรับนักศึกษาวิทยาการคอมพิวเตอร์  ภาคพิเศษ", JLabel.CENTER);   // ข้อความท้ายฟอร์ม
        note.setFont(Theme.font(Font.PLAIN, 12));                                              // ตัวเล็ก 12
        note.setForeground(Theme.TEXT_GRAY);                                                   // สีเทา
        note.setBounds(0, 616, 460, 18);                                                       // กว้างเต็มแผง จัดกลาง
        right.add(note);                                                                       // ใส่ลงในแผง
    }                                                                                          // จบคอนสตรักเตอร์

    private void addWhiteText(JPanel panel, String text, int y) {   // เมธอดช่วย: ข้อความสีขาวในแผงซ้าย
        JLabel l = new JLabel(text);                   // สร้างป้าย
        l.setFont(Theme.font(Font.PLAIN, 15));         // ตัวปกติ 15
        l.setForeground(Theme.WHITE);                  // สีขาว
        l.setBounds(146, y, 520, 22);                  // ชิดแนวเดียวกับชื่อระบบ (x = 146)
        panel.add(l);                                  // ใส่ลงในแผง
    }                                                  // จบ addWhiteText

    private void addBoldLabel(JPanel panel, String text, int x, int y) {   // เมธอดช่วย: ป้ายชื่อช่องตัวหนา
        JLabel l = new JLabel(text);                   // สร้างป้าย
        l.setFont(Theme.font(Font.BOLD, 13));          // ตัวหนา 13
        l.setForeground(Theme.TEXT);                   // สีเข้ม
        l.setBounds(x, y, 200, 20);                    // วางตามตำแหน่งที่ส่งมา
        panel.add(l);                                  // ใส่ลงในแผง
    }                                                  // จบ addBoldLabel

    // getter: AppFrame ผูกปุ่ม, logic อ่านค่าเช่น getStudentIdBox().getText()
    public InputBox getStudentIdBox()       { return studentIdBox; }                        // คืนค่า studentIdBox
    public InputBox getPasswordBox()        { return passwordBox; }                         // คืนค่า passwordBox
    public RoundButton getLoginButton()     { return loginButton; }                         // คืนค่า loginButton
    public RoundButton getRegisterButton()  { return registerButton; }                      // คืนค่า registerButton
}                                                                    // จบคลาส LoginPage
