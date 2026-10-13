package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Graphics, Color, Font, Polygon, Image
import javax.swing.*;   // JPanel, ImageIcon

/*
 * ไอคอนที่วาดเองด้วย Graphics (ไม่ต้องใช้ไฟล์รูป)
 * ออกแบบบนตาราง 24x24 แล้วย่อ/ขยายตาม size
 *   เช่น size = 48 → ทุกพิกัดคูณ 2,  size = 12 → ทุกพิกัดหาร 2
 * ใช้แค่ drawLine, drawOval, fillOval, drawArc, fillArc, fillRect, Polygon (สไลด์บทที่ 12)
 *
 * ไอคอน 20 ตัวใช้ "รูปภาพ" จากโฟลเดอร์ icons แทนการวาด (การใส่รูปด้วย ImageIcon — สไลด์บทที่ 9)
 *   ชื่อไฟล์ = ชื่อไอคอน_แดง_เขียว_น้ำเงิน.png  เช่น  icons/door_25_118_210.png (ประตูสีน้ำเงิน)
 *   ถ้าหาไฟล์ไม่เจอ จะกลับไปวาดเองเหมือนเดิม โปรแกรมจึงไม่พัง
 */
public class IconPanel extends JPanel {   // สืบทอด JPanel เพื่อวาดใน paintComponent

    // ชนิดของไอคอน (ใช้ตัวเลขแทนชื่อ อ่านง่ายกว่าเขียนเลขตรง ๆ)
    public static final int BOOK = 0;            // หนังสือเปิด (โลโก้)
    public static final int HOME = 1;            // บ้าน (เมนูหน้าหลัก)
    public static final int BUILDING = 2;        // อาคาร (เมนูดูห้องทั้งหมด)
    public static final int HOME_ADD = 3;        // บ้าน + เครื่องหมายบวก (เมนูจองห้อง)
    public static final int CALENDAR = 4;        // ปฏิทิน (เมนูการจองของฉัน)
    public static final int USER = 5;            // คนในวงกลม แบบเส้น (เมนูข้อมูลส่วนตัว)
    public static final int BELL = 6;            // กระดิ่งแจ้งเตือน
    public static final int CHECK_CIRCLE = 7;    // วงกลมมีเครื่องหมายถูก (จองสำเร็จ)
    public static final int CALENDAR_INFO = 8;   // ปฏิทิน + ตัว i (หัวการ์ดรายละเอียดการจอง)
    public static final int DOOR = 9;            // ประตู
    public static final int USERS = 10;          // คน 2 คน
    public static final int CLOCK = 11;          // นาฬิกา
    public static final int ID_BADGE = 12;       // กรอบมีคำว่า ID
    public static final int AVATAR = 13;         // รูปโปรไฟล์ (วงแหวน + คนทึบ)
    public static final int CALENDAR_CHECK = 14; // ปฏิทินมีเครื่องหมายถูก
    public static final int LIST = 15;           // กล่องรายการ
    public static final int LIST_ADD = 16;       // รายการ + เครื่องหมายบวก
    public static final int PERSON = 17;         // คนแบบทึบ
    public static final int MAIL = 18;           // ซองจดหมาย
    public static final int PHONE = 19;          // หูโทรศัพท์
    public static final int LOCK = 20;           // แม่กุญแจ
    public static final int USER_PLUS = 21;      // คน + เครื่องหมายบวก (สมัครสมาชิก)
    public static final int ARROW_LEFT = 22;     // ลูกศรชี้ซ้าย (กลับ)
    public static final int LOGIN = 23;          // ลูกศรเข้าประตู (เข้าสู่ระบบ)
    public static final int BOOK_OPEN = 24;      // หนังสือเปิด หน้าซ้ายทึบ หน้าขวาโปร่ง (หน้า Login ฝั่งขวา)
    public static final int DOOR_FILLED = 25;    // ประตูทึบ (การ์ดสรุปหน้า Admin)
    public static final int DOOR_LOCK = 26;      // ประตูทึบ + แม่กุญแจ (ห้องไม่ว่าง)
    public static final int USERS_PLUS = 27;     // คน 2 คน + เครื่องหมายบวก (ผู้ใช้ทั้งหมด)
    public static final int ROOM_DOOR = 28;      // ประตูในวงเล็บ (รายการสถานะห้อง)
    public static final int MONITOR = 29;        // จอคอมพิวเตอร์ (หัวปฏิทิน)
    public static final int ARROW_RIGHT = 30;    // ลูกศรชี้ขวา (ดูทั้งหมด)
    public static final int CHEVRON_LEFT = 31;   // เครื่องหมาย < (ปุ่มหน้าก่อน)
    public static final int CHEVRON_RIGHT = 32;  // เครื่องหมาย > (ปุ่มหน้าถัดไป)

    private int type;       // ชนิดไอคอน (หนึ่งในค่าด้านบน)
    private int size;       // ขนาดไอคอนเป็นพิกเซล (กว้าง = สูง)
    private Color color;    // สีหลักของไอคอน
    private Color accent;   // สีรอง: เครื่องหมาย + ของ HOME_ADD / สีพื้นด้านในของ AVATAR

    // ตัวแปรที่ใช้ระหว่างวาด
    private Graphics pen;   // ตัววาดที่ส่งมาจาก paintComponent / RoundButton
    private int ox, oy;     // ตำแหน่งมุมซ้ายบน
    private int t;          // ความหนาเส้น

    // ชื่อไฟล์รูปของไอคอนแต่ละชนิด (ลำดับตรงกับตัวเลขชนิดไอคอน 0-32)  null = ไม่มีรูป ให้วาดเอง
    private static final String[] FILE_NAMES = {
        "book",            // 0  BOOK
        null,              // 1  HOME
        "building",        // 2  BUILDING
        "home_add",        // 3  HOME_ADD
        "calendar",        // 4  CALENDAR
        "user",            // 5  USER
        null,              // 6  BELL
        "check_circle",    // 7  CHECK_CIRCLE
        null,              // 8  CALENDAR_INFO
        "door",            // 9  DOOR
        "users",           // 10 USERS
        "clock",           // 11 CLOCK
        "id_badge",        // 12 ID_BADGE
        "avatar",          // 13 AVATAR
        "calendar_check",  // 14 CALENDAR_CHECK
        "list",            // 15 LIST
        "list_add",        // 16 LIST_ADD
        "person",          // 17 PERSON
        "mail",            // 18 MAIL
        "phone",           // 19 PHONE
        "lock",            // 20 LOCK
        "user_plus",       // 21 USER_PLUS
        null,              // 22 ARROW_LEFT
        null,              // 23 LOGIN
        "book_open",       // 24 BOOK_OPEN
        null,              // 25 DOOR_FILLED
        null,              // 26 DOOR_LOCK
        null,              // 27 USERS_PLUS
        null,              // 28 ROOM_DOOR
        null,              // 29 MONITOR
        null,              // 30 ARROW_RIGHT
        null,              // 31 CHEVRON_LEFT
        null               // 32 CHEVRON_RIGHT
    };

    private Image picture = null;   // รูปไอคอนที่โหลดแล้ว (null = ไม่มีรูป ใช้การวาดแทน)

    public IconPanel(int type, int size, Color color) {   // คอนสตรักเตอร์แบบไม่ระบุสีรอง
        this(type, size, color, Theme.SECONDARY);         // เรียกคอนสตรักเตอร์ตัวล่าง โดยใช้สีรอง = น้ำเงิน
    }                                                     // จบคอนสตรักเตอร์

    public IconPanel(int type, int size, Color color, Color accent) {   // คอนสตรักเตอร์แบบระบุสีรอง
        this.type = type;       // เก็บชนิดไอคอน
        this.size = size;       // เก็บขนาด
        this.color = color;     // เก็บสีหลัก
        this.accent = accent;   // เก็บสีรอง
        setOpaque(false);       // ไม่ระบายพื้น panel ให้เห็นพื้นด้านหลังไอคอน
        loadPicture();          // ลองโหลดรูปไอคอน (ถ้าชนิดนี้มีรูป)
    }                           // จบคอนสตรักเตอร์

    // โหลดรูปจากโฟลเดอร์ icons ตามชนิดและสีของไอคอน
    private void loadPicture() {
        String name = FILE_NAMES[type];                     // ชื่อไฟล์ของไอคอนชนิดนี้
        if (name == null) {                                 // ชนิดนี้ไม่มีรูป
            return;                                         // ไม่ต้องโหลด ใช้การวาดแทน
        }                                                   // จบ if
        String file = name + "_" + color.getRed() + "_" + color.getGreen() + "_" + color.getBlue() + ".png";   // เช่น door_25_118_210.png
        ImageIcon img = new ImageIcon("icons/" + file);     // โหลดรูป (โฟลเดอร์ icons อยู่ข้าง src)
        if (img.getIconWidth() <= 0) {                      // ถ้าหาไม่เจอ (ความกว้างเป็น -1)
            img = new ImageIcon("src/icons/" + file);       // ลองหาในโฟลเดอร์ src/icons
        }                                                   // จบ if
        if (img.getIconWidth() > 0) {                       // ถ้าโหลดรูปได้
            Image scaled = img.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);   // ย่อรูปจาก 64x64 ให้เท่าขนาดไอคอนแบบเนียน
            picture = new ImageIcon(scaled).getImage();     // ห่อด้วย ImageIcon เพื่อรอให้ย่อรูปเสร็จก่อนใช้
        }                                                   // จบ if
    }                                                       // จบ loadPicture

    public int getIconSize() {  // ให้คลาสอื่นถามขนาดไอคอนได้ (RoundButton ใช้จัดกึ่งกลาง)
        return size;            // คืนขนาด
    }                           // จบ getIconSize

    public void paintComponent(Graphics g) {   // Java เรียกอัตโนมัติเมื่อต้องวาด panel นี้
        super.paintComponent(g);               // ให้ JPanel วาดส่วนพื้นฐานก่อน
        draw(g, 0, 0);                         // วาดไอคอนที่มุมซ้ายบนของ panel
    }                                          // จบ paintComponent

    // วาดไอคอนที่ตำแหน่ง (x, y) — RoundButton เรียกใช้เมธอดนี้ด้วย
    public void draw(Graphics g, int x, int y) {                                            // เริ่มเมธอด draw
        pen = g;                        // เก็บตัววาดไว้ให้เมธอดย่อย (line, oval ...) ใช้
        ox = x;                         // เก็บตำแหน่งแกน x
        oy = y;                         // เก็บตำแหน่งแกน y
        t = Math.max(1, size / 12);     // ความหนาเส้นตามขนาดไอคอน (อย่างน้อย 1 พิกเซล)
        g.setColor(color);              // ตั้งสีหลัก

        if (picture != null) {                  // ถ้าไอคอนนี้มีรูป
            g.drawImage(picture, x, y, null);   // วาดรูปที่ตำแหน่ง (x, y)
            return;                             // จบเลย ไม่ต้องวาดเส้นด้านล่าง
        }                                       // จบ if

        if (type == BOOK || type == BOOK_OPEN) {                         // ----- หนังสือเปิด (ถ้าไม่มีรูป BOOK_OPEN ก็วาดแบบเดียวกัน) -----
            fillPoly(new double[]{1, 5, 11, 6.5, 11, 20, 1, 18.5});      // หน้าซ้าย (สี่เหลี่ยมเอียง)
            fillPoly(new double[]{13, 6.5, 23, 5, 23, 18.5, 13, 20});    // หน้าขวา
            fillRect(1, 20, 22, 1.5);                                    // ฐานหนังสือ
        } else if (type == HOME) {                                       // ----- บ้าน -----
            line(2.5, 11.5, 12, 3);                                      // หลังคาด้านซ้าย
            line(12, 3, 21.5, 11.5);                                     // หลังคาด้านขวา
            line(5, 10, 5, 21);                                          // ผนังซ้าย
            line(5, 21, 19, 21);                                         // พื้น
            line(19, 21, 19, 10);                                        // ผนังขวา
            line(10, 21, 10, 15);                                        // ประตู ด้านซ้าย
            line(10, 15, 14, 15);                                        // ประตู ด้านบน
            line(14, 15, 14, 21);                                        // ประตู ด้านขวา
        } else if (type == BUILDING) {                                   // ----- อาคาร -----
            line(3, 21, 3, 11);                                          // ผนังซ้ายของบ้านหลังเล็ก
            line(3, 11, 9, 6);                                           // หลังคาซ้าย
            line(9, 6, 15, 11);                                          // หลังคาขวา
            line(15, 3, 15, 21);                                         // ผนังซ้ายของตึกสูง
            line(15, 3, 21, 3);                                          // หลังคาตึกสูง
            line(21, 3, 21, 21);                                         // ผนังขวาของตึกสูง
            line(1.5, 21, 22.5, 21);                                     // พื้น
            line(7, 21, 7, 15.5);                                        // ประตู ด้านซ้าย
            line(7, 15.5, 11, 15.5);                                     // ประตู ด้านบน
            line(11, 15.5, 11, 21);                                      // ประตู ด้านขวา
            fillRect(17.3, 7, 1.8, 1.8);                                 // หน้าต่างชั้นบน
            fillRect(17.3, 11, 1.8, 1.8);                                // หน้าต่างชั้นกลาง
            fillRect(17.3, 15, 1.8, 1.8);                                // หน้าต่างชั้นล่าง
        } else if (type == HOME_ADD) {                                   // ----- บ้าน + เครื่องหมายบวก -----
            line(1.5, 10.5, 10, 3);                                      // หลังคาซ้าย
            line(10, 3, 16, 8.3);                                        // หลังคาขวา (สั้น เว้นที่ให้วงกลม)
            line(3.5, 9, 3.5, 19.5);                                     // ผนังซ้าย
            line(3.5, 19.5, 12, 19.5);                                   // พื้น
            fillOval(12.5, 12.5, 10.5, 10.5);                            // วงกลมทึบมุมขวาล่าง
            g.setColor(accent);                                          // เปลี่ยนเป็นสีรอง
            line(17.5, 15, 17.5, 20.5);                                  // เครื่องหมายบวก เส้นตั้ง
            line(15, 17.5, 20.5, 17.5);                                  // เครื่องหมายบวก เส้นนอน
        } else if (type == CALENDAR) {                                   // ----- ปฏิทิน -----
            rect(3, 5, 18, 16);                                          // กรอบปฏิทิน
            line(3, 10, 21, 10);                                         // เส้นแบ่งหัวปฏิทิน
            line(8, 3, 8, 7);                                            // ห่วงซ้าย
            line(16, 3, 16, 7);                                          // ห่วงขวา
            fillRect(7, 13, 2, 2);                                       // จุดวันที่ แถว 1 ช่อง 1
            fillRect(11, 13, 2, 2);                                      // แถว 1 ช่อง 2
            fillRect(15, 13, 2, 2);                                      // แถว 1 ช่อง 3
            fillRect(7, 16.5, 2, 2);                                     // แถว 2 ช่อง 1
            fillRect(11, 16.5, 2, 2);                                    // แถว 2 ช่อง 2
        } else if (type == USER) {                                       // ----- คนในวงกลม (เส้น) -----
            oval(2, 2, 20, 20);                                          // วงกลมนอก
            oval(8.5, 6, 7, 7);                                          // หัว
            arc(5, 15, 14, 12, 0, 180);                                  // ไหล่ (ครึ่งวงรีด้านบน)
        } else if (type == BELL) {                                       // ----- กระดิ่ง -----
            line(6, 17, 6, 11);                                          // ด้านซ้ายของกระดิ่ง
            arc(6, 5, 12, 12, 0, 180);                                   // ส่วนโค้งด้านบน
            line(18, 11, 18, 17);                                        // ด้านขวาของกระดิ่ง
            line(4, 17, 20, 17);                                         // ขอบล่าง
            line(12, 2.5, 12, 5);                                        // ก้านด้านบน
            arc(10, 17.5, 4, 4, 180, 180);                               // ลูกกระดิ่งด้านล่าง (ครึ่งวงกลมล่าง)
        } else if (type == CHECK_CIRCLE) {                               // ----- วงกลม + เครื่องหมายถูก -----
            fillOval(0, 0, 24, 24);                                      // วงกลมทึบเต็มไอคอน
            g.setColor(Color.WHITE);                                     // เปลี่ยนเป็นสีขาว
            t = Math.max(2, size / 10);                                  // เส้นเครื่องหมายถูกหนากว่าปกติ
            line(6.5, 12.5, 10.5, 16.5);                                 // ขีดสั้นด้านซ้าย
            line(10.5, 16.5, 17.5, 8.5);                                 // ขีดยาวด้านขวา
        } else if (type == CALENDAR_INFO) {                              // ----- ปฏิทิน + ตัว i -----
            fillRect(1, 2, 17, 16);                                      // ปฏิทินทึบ
            g.setColor(Color.WHITE);                                     // สีขาว
            fillRect(1, 8, 17, 1.6);                                     // เส้นแบ่งหัวปฏิทิน (สีขาว)
            fillOval(10.5, 10.5, 13, 13);                                // วงกลมขาวรองพื้นตัว i
            g.setColor(color);                                           // กลับเป็นสีหลัก
            oval(11.5, 11.5, 11, 11);                                    // วงกลมรอบตัว i
            fillOval(16.2, 13.6, 1.8, 1.8);                              // จุดบนตัว i
            line(17, 16.5, 17, 20);                                      // ขาตัว i
        } else if (type == DOOR) {                                       // ----- ประตู -----
            line(3, 21, 21, 21);                                         // พื้น
            line(6, 21, 6, 3);                                           // วงกบซ้าย
            line(6, 3, 18, 3);                                           // วงกบบน
            line(18, 3, 18, 21);                                         // วงกบขวา
            line(6, 3, 13, 5);                                           // บานประตูที่เปิดออก (ด้านบน)
            line(13, 5, 13, 21);                                         // บานประตู (ด้านขวา)
            fillOval(10, 11.5, 1.8, 1.8);                                // ลูกบิด
        } else if (type == USERS) {                                      // ----- คน 2 คน -----
            fillOval(4, 4, 7.5, 7.5);                                    // หัวคนหน้า
            fillArc(1, 13, 13.5, 12, 0, 180);                            // ตัวคนหน้า (ครึ่งวงรีทึบ)
            fillOval(14, 5, 6.5, 6.5);                                   // หัวคนหลัง
            fillArc(13.5, 13, 10.5, 11, 0, 180);                         // ตัวคนหลัง
        } else if (type == CLOCK) {                                      // ----- นาฬิกา -----
            oval(3, 3, 18, 18);                                          // หน้าปัด
            line(12, 7, 12, 12);                                         // เข็มยาว
            line(12, 12, 15.5, 14);                                      // เข็มสั้น
        } else if (type == ID_BADGE) {                                   // ----- กรอบ ID -----
            rect(2.5, 3, 19, 18);                                        // กรอบสี่เหลี่ยม
            g.setFont(new Font("Tahoma", Font.BOLD, len(9)));            // ฟอนต์ตัวหนา ขนาดตามไอคอน
            g.drawString("ID", px(6.5), py(16));                         // เขียนคำว่า ID ในกรอบ
        } else if (type == AVATAR) {                                     // ----- รูปโปรไฟล์ -----
            // วงแหวน: วงกลมใหญ่สีน้ำเงิน แล้ววางวงกลมเล็กสีพื้นหลัง (accent) ทับตรงกลาง
            fillOval(1.5, 1.5, 21, 21);                                  // วงกลมใหญ่สีหลัก
            g.setColor(accent);                                          // เปลี่ยนเป็นสีพื้นหลัง
            fillOval(3.1, 3.1, 17.8, 17.8);                              // วงกลมเล็กทับ → เหลือเป็นวงแหวน
            g.setColor(color);                                           // กลับเป็นสีหลัก
            fillOval(8.3, 5.5, 7.4, 7.4);                                // หัว
            fillArc(7, 14.5, 10, 10, 0, 180);                            // ไหล่ (ครึ่งวงกลมทึบ)
        } else if (type == CALENDAR_CHECK) {                             // ----- ปฏิทิน + เครื่องหมายถูก -----
            rect(3.5, 5, 17, 16);                                        // กรอบปฏิทิน
            line(3.5, 9.5, 20.5, 9.5);                                   // เส้นแบ่งหัวปฏิทิน
            line(8, 3, 8, 6.5);                                          // ห่วงซ้าย
            line(16, 3, 16, 6.5);                                        // ห่วงขวา
            line(9, 15, 11.2, 17.2);                                     // เครื่องหมายถูก ขีดสั้น
            line(11.2, 17.2, 15.5, 12.8);                                // เครื่องหมายถูก ขีดยาว
        } else if (type == LIST) {                                       // ----- กล่องรายการ -----
            fillRect(2.5, 2.5, 19, 19);                                  // กล่องทึบ
            g.setColor(Color.WHITE);                                     // สีขาว
            for (int i = 0; i < 4; i++) {                                // วนวาด 4 แถว
                double y2 = 5.2 + i * 4.1;                               // ตำแหน่งแนวตั้งของแถวที่ i
                fillRect(5, y2, 2.2, 2.2);                               // จุดหน้าแถว
                fillRect(9.2, y2 + 0.6, 9.8, 1.1);                       // เส้นข้อความของแถว
            }                                                            // จบ for
        } else if (type == LIST_ADD) {                                   // ----- รายการ + เครื่องหมายบวก -----
            line(3, 6, 19, 6);                                           // บรรทัดที่ 1
            line(3, 11, 19, 11);                                         // บรรทัดที่ 2
            line(3, 16, 12, 16);                                         // บรรทัดที่ 3 (สั้น)
            line(17.5, 13.5, 17.5, 20.5);                                // เครื่องหมายบวก เส้นตั้ง
            line(14, 17, 21, 17);                                        // เครื่องหมายบวก เส้นนอน
        } else if (type == PERSON) {                                     // ----- คนแบบทึบ -----
            fillOval(7.5, 3, 9, 9);                                      // หัว
            fillArc(3, 14, 18, 16, 0, 180);                              // ตัว (ครึ่งวงรีทึบ)
        } else if (type == MAIL) {                                       // ----- ซองจดหมาย -----
            fillRect(2, 5, 20, 14);                                      // ตัวซองทึบ
            g.setColor(Color.WHITE);                                     // สีขาว
            line(4, 7.5, 12, 13);                                        // ฝาซอง ด้านซ้าย
            line(12, 13, 20, 7.5);                                       // ฝาซอง ด้านขวา
        } else if (type == PHONE) {                                      // ----- หูโทรศัพท์ -----
            fillPoly(new double[]{6.5, 2.5, 9.5, 6.5, 8, 9, 10, 12.5, 12.5, 15, 15, 16, 17.5, 14.5,  // จุดมุมของหูโทรศัพท์ (บรรทัดแรก)
                                  21.5, 17.5, 19.5, 21, 14, 20, 8.5, 16.5, 5, 12, 3, 7, 3, 4.5});   // รูปหลายเหลี่ยมเป็นรูปหูโทรศัพท์
        } else if (type == LOCK) {                                       // ----- แม่กุญแจ -----
            arc(7, 2.5, 10, 11, 0, 180);                                 // ห่วงด้านบน (ครึ่งวงรี)
            line(7, 8, 7, 11);                                           // ขาห่วงซ้าย
            line(17, 8, 17, 11);                                         // ขาห่วงขวา
            fillRect(4, 10, 16, 12);                                     // ตัวกุญแจทึบ
            g.setColor(Color.WHITE);                                     // สีขาว
            fillOval(10.5, 14.5, 3, 3);                                  // รูกุญแจ
        } else if (type == USER_PLUS) {                                  // ----- คน + เครื่องหมายบวก -----
            fillOval(10, 4, 8, 8);                                       // หัว
            fillArc(6.5, 14, 15, 12, 0, 180);                            // ตัว
            line(5, 6, 5, 12);                                           // เครื่องหมายบวก เส้นตั้ง
            line(2, 9, 8, 9);                                            // เครื่องหมายบวก เส้นนอน
        } else if (type == ARROW_LEFT) {                                 // ----- ลูกศรชี้ซ้าย -----
            line(4, 12, 20, 12);                                         // ก้านลูกศร
            line(11, 5, 4, 12);                                          // หัวลูกศร ขีดบน
            line(4, 12, 11, 19);                                         // หัวลูกศร ขีดล่าง
        } else if (type == LOGIN) {                                      // ----- เข้าสู่ระบบ -----
            line(13, 3, 20, 3);                                          // กรอบประตู ด้านบน
            line(20, 3, 20, 21);                                         // กรอบประตู ด้านขวา
            line(20, 21, 13, 21);                                        // กรอบประตู ด้านล่าง
            line(3, 12, 14, 12);                                         // ก้านลูกศรชี้เข้าประตู
            line(10, 8, 14, 12);                                         // หัวลูกศร ขีดบน
            line(14, 12, 10, 16);                                        // หัวลูกศร ขีดล่าง
        } else if (type == DOOR_FILLED) {                                // ----- ประตูทึบ -----
            fillRect(5, 2, 14, 20);                                      // บานประตูทึบ
            fillRect(3, 21, 18, 1.5);                                    // พื้น
            g.setColor(Color.WHITE);                                     // สีขาว
            fillOval(14.5, 11, 2.2, 2.2);                                // ลูกบิด
        } else if (type == DOOR_LOCK) {                                  // ----- ประตูทึบ + แม่กุญแจ -----
            fillRect(3, 2, 13, 20);                                      // บานประตูทึบ
            fillRect(1, 21, 16, 1.5);                                    // พื้น
            g.setColor(Color.WHITE);                                     // สีขาว
            fillOval(5, 11, 2, 2);                                       // ลูกบิด
            fillRect(11, 10, 12, 11);                                    // พื้นขาวรองแม่กุญแจ
            g.setColor(color);                                           // กลับเป็นสีหลัก
            fillRect(12.5, 14, 9, 6);                                    // ตัวแม่กุญแจ
            arc(14, 9.5, 6, 8, 0, 180);                                  // ห่วงแม่กุญแจ
        } else if (type == USERS_PLUS) {                                 // ----- คน 2 คน + เครื่องหมายบวก -----
            fillOval(9, 5, 7, 7);                                        // หัวคนหน้า
            fillArc(6.5, 13.5, 12, 11, 0, 180);                          // ตัวคนหน้า
            fillOval(16, 4, 6, 6);                                       // หัวคนหลัง
            fillArc(15.5, 11.5, 8.5, 9, 0, 180);                         // ตัวคนหลัง
            line(4, 7, 4, 13);                                           // เครื่องหมายบวก เส้นตั้ง
            line(1, 10, 7, 10);                                          // เครื่องหมายบวก เส้นนอน
        } else if (type == ROOM_DOOR) {                                  // ----- ประตูในวงเล็บ -----
            line(3, 3, 3, 21);                                           // วงเล็บ เส้นตั้ง
            line(3, 3, 7, 3);                                            // วงเล็บ ขีดบน
            line(3, 21, 7, 21);                                          // วงเล็บ ขีดล่าง
            fillPoly(new double[]{9, 2, 21, 5, 21, 19, 9, 22});          // บานประตูทึบ (เอียงเล็กน้อย)
            g.setColor(Color.WHITE);                                     // สีขาว
            fillOval(11, 11, 2.2, 2.2);                                  // ลูกบิด
        } else if (type == MONITOR) {                                    // ----- จอคอมพิวเตอร์ -----
            rect(2, 4, 20, 13);                                          // กรอบจอ
            line(12, 17, 12, 20);                                        // ขาตั้ง
            line(8, 20, 16, 20);                                         // ฐาน
        } else if (type == ARROW_RIGHT) {                                // ----- ลูกศรชี้ขวา -----
            line(4, 12, 20, 12);                                         // ก้านลูกศร
            line(13, 5, 20, 12);                                         // หัวลูกศร ขีดบน
            line(20, 12, 13, 19);                                        // หัวลูกศร ขีดล่าง
        } else if (type == CHEVRON_LEFT) {                               // ----- เครื่องหมาย < -----
            line(15, 5, 8, 12);                                          // ขีดบน
            line(8, 12, 15, 19);                                         // ขีดล่าง
        } else if (type == CHEVRON_RIGHT) {                              // ----- เครื่องหมาย > -----
            line(9, 5, 16, 12);                                          // ขีดบน
            line(16, 12, 9, 19);                                         // ขีดล่าง
        }                                                                // จบ if-else ของชนิดไอคอน
    }                                                                    // จบ draw

    // ---------- แปลงพิกัดจากตาราง 24x24 เป็นพิกเซลจริง ----------
    private int px(double v) {                              // แปลงพิกัด x
        return ox + (int) Math.round(v * size / 24.0);      // ย่อ/ขยายตาม size แล้วบวกตำแหน่งเริ่ม
    }                                                       // จบ px

    private int py(double v) {                              // แปลงพิกัด y
        return oy + (int) Math.round(v * size / 24.0);      // ย่อ/ขยายตาม size แล้วบวกตำแหน่งเริ่ม
    }                                                       // จบ py

    private int len(double v) {                             // แปลงความยาว (กว้าง/สูง) ไม่ต้องบวกตำแหน่ง
        return (int) Math.round(v * size / 24.0);           // ย่อ/ขยายตาม size
    }                                                       // จบ len

    // ---------- เมธอดวาดพื้นฐาน (เส้นหนา t พิกเซล) ----------
    private void line(double x1, double y1, double x2, double y2) {   // วาดเส้นตรงหนา t พิกเซล
        for (int i = 0; i < t; i++) {                                  // วาดซ้ำ t รอบ
            pen.drawLine(px(x1) + i, py(y1), px(x2) + i, py(y2));      // เลื่อนไปทางขวาทีละ 1 พิกเซล
            pen.drawLine(px(x1), py(y1) + i, px(x2), py(y2) + i);      // เลื่อนลงทีละ 1 พิกเซล → เส้นดูหนาขึ้น
        }                                                              // จบ for
    }                                                                  // จบ line

    private void rect(double x, double y, double w, double h) {   // วาดกรอบสี่เหลี่ยมด้วยเส้นหนา 4 เส้น
        line(x, y, x + w, y);                                     // ด้านบน
        line(x + w, y, x + w, y + h);                             // ด้านขวา
        line(x, y + h, x + w, y + h);                             // ด้านล่าง
        line(x, y, x, y + h);                                     // ด้านซ้าย
    }                                                             // จบ rect

    private void oval(double x, double y, double w, double h) {   // วาดวงกลม/วงรีเส้นหนา t พิกเซล
        for (int i = 0; i < t; i++) {                             // วาดซ้อนกัน t วง ให้เล็กลงทีละนิด
            pen.drawOval(px(x) + i, py(y) + i, len(w) - 2 * i, len(h) - 2 * i);             // วาดวงที่ i (เล็กลงทีละ 1 พิกเซลทุกด้าน)
            pen.drawOval(px(x) + i, py(y) + i, len(w) - 2 * i - 1, len(h) - 2 * i - 1);   // อุดช่องว่างระหว่างวง
        }                                                         // จบ for
    }                                                             // จบ oval

    private void arc(double x, double y, double w, double h, int start, int angle) {   // วาดส่วนโค้งเส้นหนา t พิกเซล
        for (int i = 0; i < t; i++) {                                                  // วาดซ้อนกัน t เส้น
            pen.drawArc(px(x) + i, py(y) + i, len(w) - 2 * i, len(h) - 2 * i, start, angle);   // start = มุมเริ่ม, angle = มุมที่วาดต่อ
        }                                                                              // จบ for
    }                                                                                  // จบ arc

    private void fillOval(double x, double y, double w, double h) {   // วงกลม/วงรีทึบ
        pen.fillOval(px(x), py(y), len(w), len(h));                   // แปลงพิกัดแล้วระบาย
    }                                                                 // จบ fillOval

    private void fillArc(double x, double y, double w, double h, int start, int angle) {   // ส่วนโค้งทึบ (รูปพัด)
        pen.fillArc(px(x), py(y), len(w), len(h), start, angle);                           // แปลงพิกัดแล้วระบาย
    }                                                                                      // จบ fillArc

    private void fillRect(double x, double y, double w, double h) {           // สี่เหลี่ยมทึบ
        pen.fillRect(px(x), py(y), Math.max(1, len(w)), Math.max(1, len(h)));  // อย่างน้อย 1 พิกเซล ไม่ให้หายตอนไอคอนเล็ก
    }                                                                         // จบ fillRect

    // จุดเรียงกันเป็นคู่ x,y เช่น {x1,y1, x2,y2, ...}
    private void fillPoly(double[] points) {                // รูปหลายเหลี่ยมทึบ (บทที่ 12: Polygon)
        Polygon p = new Polygon();                          // สร้าง Polygon ว่าง
        for (int i = 0; i < points.length; i += 2) {        // อ่าน Array ทีละ 2 ช่อง (x, y)
            p.addPoint(px(points[i]), py(points[i + 1]));   // เพิ่มจุดมุมของรูปหลายเหลี่ยม
        }                                                   // จบ for
        pen.fillPolygon(p);                                 // ระบายรูปหลายเหลี่ยม
    }                                                       // จบ fillPoly
}                                                           // จบคลาส IconPanel
