package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;         // AppPath (หาโฟลเดอร์ icons)
import java.awt.*;      // Graphics, Color, Image
import javax.swing.*;   // JPanel, ImageIcon

/*
 * ไอคอน = รูปภาพจากโฟลเดอร์ icons (การใส่รูปด้วย ImageIcon — สไลด์บทที่ 9) ไม่มีการวาดรูปเองด้วยโค้ด
 *   ชื่อไฟล์ = ชื่อไอคอน_แดง_เขียว_น้ำเงิน.png  เช่น  icons/door_25_118_210.png (ประตูสีน้ำเงิน)
 *   รูปต้นฉบับขนาด 64x64 จุด แล้วย่อให้เท่าขนาดที่ใช้ (size)
 *   ถ้าหาไฟล์ไม่เจอ ไอคอนจะว่างไว้ (โปรแกรมไม่พัง) และแจ้งชื่อไฟล์ที่หายใน console
 */
public class IconPanel extends JPanel {   // สืบทอด JPanel แล้วแสดงรูปใน paintComponent

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
    public static final int AVATAR = 13;         // รูปโปรไฟล์
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
    public static final int BOOK_OPEN = 24;      // หนังสือเปิด (หน้า Login ฝั่งขวา)
    public static final int DOOR_FILLED = 25;    // ประตูทึบ (การ์ดสรุปหน้า Admin)
    public static final int DOOR_LOCK = 26;      // ประตูทึบ + แม่กุญแจ (ห้องไม่ว่าง)
    public static final int USERS_PLUS = 27;     // คน 2 คน + เครื่องหมายบวก (ผู้ใช้ทั้งหมด)
    public static final int ROOM_DOOR = 28;      // ประตูในวงเล็บ (รายการสถานะห้อง)
    public static final int MONITOR = 29;        // จอคอมพิวเตอร์
    public static final int ARROW_RIGHT = 30;    // ลูกศรชี้ขวา (ดูทั้งหมด)
    public static final int CHEVRON_LEFT = 31;   // เครื่องหมาย < (ปุ่มหน้าก่อน)
    public static final int CHEVRON_RIGHT = 32;  // เครื่องหมาย > (ปุ่มหน้าถัดไป)

    // ชื่อไฟล์รูปของไอคอนแต่ละชนิด (ลำดับตรงกับตัวเลขชนิดไอคอน 0-32)
    private static final String[] FILE_NAMES = {
        "book",            // 0  BOOK
        "home",            // 1  HOME
        "building",        // 2  BUILDING
        "home_add",        // 3  HOME_ADD
        "calendar",        // 4  CALENDAR
        "user",            // 5  USER
        "bell",            // 6  BELL
        "check_circle",    // 7  CHECK_CIRCLE
        "calendar_info",   // 8  CALENDAR_INFO
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
        "arrow_left",      // 22 ARROW_LEFT
        "login",           // 23 LOGIN
        "book_open",       // 24 BOOK_OPEN
        "door_filled",     // 25 DOOR_FILLED
        "door_lock",       // 26 DOOR_LOCK
        "users_plus",      // 27 USERS_PLUS
        "room_door",       // 28 ROOM_DOOR
        "monitor",         // 29 MONITOR
        "arrow_right",     // 30 ARROW_RIGHT
        "chevron_left",    // 31 CHEVRON_LEFT
        "chevron_right"    // 32 CHEVRON_RIGHT
    };

    private int size;               // ขนาดไอคอนเป็นพิกเซล (กว้าง = สูง)
    private Image picture = null;   // รูปไอคอนที่โหลดแล้ว (null = หาไฟล์ไม่เจอ)

    // type = ชนิดไอคอน (เช่น IconPanel.DOOR), size = ขนาด, color = สีของไอคอน (เลือกไฟล์รูปสีนั้น)
    public IconPanel(int type, int size, Color color) {
        this.size = size;       // เก็บขนาด
        setOpaque(false);       // ไม่ระบายพื้น panel ให้เห็นพื้นด้านหลังไอคอน
        String file = FILE_NAMES[type] + "_" + color.getRed() + "_" + color.getGreen() + "_" + color.getBlue() + ".png";   // เช่น door_25_118_210.png
        picture = loadImage(file, size, size);   // โหลดรูปจากโฟลเดอร์ icons แล้วย่อให้เท่า size
    }                                            // จบคอนสตรักเตอร์

    // โหลดรูปชื่อ file จากโฟลเดอร์ icons แล้วย่อเป็น w x h (คลาสอื่นใช้ได้ด้วย) — หาไม่เจอคืน null
    public static Image loadImage(String file, int w, int h) {
        ImageIcon img = new ImageIcon(AppPath.find("icons/" + file).getPath());   // โหลดรูป (AppPath หาโฟลเดอร์ icons ให้)
        if (img.getIconWidth() <= 0) {                                              // หาไฟล์ไม่เจอ (ความกว้างเป็น -1)
            System.out.println("[icons] not found: icons/" + file);                     // แจ้งใน console ว่าไฟล์ไหนหาย
            return null;                                                            // ไม่มีรูป
        }                                                                           // จบ if
        Image scaled = img.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);  // ย่อรูปให้เท่าขนาดที่ใช้แบบเนียน
        return new ImageIcon(scaled).getImage();                                    // ห่อด้วย ImageIcon เพื่อรอให้ย่อรูปเสร็จก่อนใช้
    }                                                                               // จบ loadImage

    public int getIconSize() {  // ให้คลาสอื่นถามขนาดไอคอนได้ (RoundButton ใช้จัดกึ่งกลาง)
        return size;            // คืนขนาด
    }                           // จบ getIconSize

    public void paintComponent(Graphics g) {   // Java เรียกอัตโนมัติเมื่อต้องแสดง panel นี้
        super.paintComponent(g);               // ให้ JPanel ทำงานพื้นฐานก่อน
        draw(g, 0, 0);                         // แสดงรูปที่มุมซ้ายบนของ panel
    }                                          // จบ paintComponent

    // แสดงรูปไอคอนที่ตำแหน่ง (x, y) — RoundButton เรียกใช้เมธอดนี้ด้วย (ไอคอนบนปุ่ม)
    public void draw(Graphics g, int x, int y) {
        if (picture != null) {                  // มีรูป
            g.drawImage(picture, x, y, null);   // แสดงรูปที่ตำแหน่ง (x, y)
        }                                       // จบ if (ไม่มีรูป = เว้นว่าง)
    }                                           // จบ draw
}                                               // จบคลาส IconPanel
