package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;         // Font
import java.awt.event.*;   // FocusListener, FocusEvent, KeyListener, KeyEvent
import javax.swing.*;      // JTextField, JPasswordField

/*
 * ช่องกรอกข้อความมีกรอบ + ข้อความจาง (placeholder)
 * ข้อความจางทำแบบเดียวกับตัวอย่าง FocusListener ในสไลด์บทที่ 11:
 *   focusGained -> ถ้ายังเป็นข้อความจาง ให้ลบออก
 *   focusLost   -> ถ้าช่องว่าง ให้ใส่ข้อความจางกลับ
 */
public class InputBox extends RoundPanel implements FocusListener, KeyListener {   // เป็นกล่องสี่เหลี่ยม + ฟังเหตุการณ์โฟกัสและการกดแป้นเอง

    private JTextField field;        // ช่องพิมพ์จริง (ถ้าเป็นรหัสผ่านจะเป็น JPasswordField ซึ่งเป็นลูกของ JTextField)
    private String hint;             // ข้อความจาง เช่น "กรอกรหัสนิสิต"
    private boolean password;        // true = ช่องรหัสผ่าน
    private boolean showingHint;     // true = ตอนนี้ในช่องเป็นข้อความจางอยู่ (ผู้ใช้ยังไม่ได้พิมพ์)
    private boolean digitsOnly = false;   // true = ให้พิมพ์ได้แค่ตัวเลข 0-9 (เช่น ช่องรหัสนิสิต)

    // iconType = -1 คือไม่มีไอคอนในช่อง
    public InputBox(String hint, boolean password, int iconType, int w, int h) {   // w, h = ขนาดกล่อง (ใช้คำนวณตำแหน่งของข้างใน)
        super(Theme.INPUT_BG, Theme.FIELD_BORDER);   // เรียกคอนสตรักเตอร์ RoundPanel: พื้นอ่อน, ขอบเทา
        this.hint = hint;                               // เก็บข้อความจาง
        this.password = password;                       // เก็บว่าเป็นช่องรหัสผ่านหรือไม่

        if (password) {                         // ถ้าเป็นช่องรหัสผ่าน
            field = new JPasswordField();       // ใช้ JPasswordField (พิมพ์แล้วขึ้นเป็นจุด)
        } else {                                // ถ้าเป็นช่องทั่วไป
            field = new JTextField();           // ใช้ JTextField
        }                                       // จบ if

        int left = 12;                          // ระยะจากขอบซ้ายถึงช่องพิมพ์ (กรณีไม่มีไอคอน)
        if (iconType != -1) {                   // ถ้ามีไอคอนในช่อง
            IconPanel icon = new IconPanel(iconType, 14, Theme.FIELD_BORDER);   // สร้างไอคอนขนาด 14 สีเทา
            icon.setBounds(12, (h - 14) / 2, 14, 14);   // วางไอคอนชิดซ้าย กึ่งกลางแนวตั้ง
            add(icon);                                  // ใส่ไอคอนลงในกล่อง
            left = 34;                                  // ขยับช่องพิมพ์ไปทางขวา ให้พ้นไอคอน
        }                                               // จบ if

        field.setBounds(left, 2, w - left - 10, h - 4); // วางช่องพิมพ์ในกล่อง (เว้นขอบรอบ ๆ)
        field.setFont(Theme.font(Font.PLAIN, 14));      // ฟอนต์ในช่อง
        field.setBorder(null);     // ไม่เอากรอบสี่เหลี่ยมของ JTextField (ใช้ขอบของ RoundPanel แทน)
        field.setOpaque(false);    // ไม่ระบายพื้นช่องพิมพ์ ให้เห็นพื้นของกล่อง
        field.addFocusListener(this);   // ให้ object นี้ (InputBox) ฟังเหตุการณ์โฟกัสของช่องพิมพ์
        field.addKeyListener(this);     // ให้ object นี้ฟังการกดแป้นพิมพ์ด้วย (ใช้กรองตัวเลข — สไลด์บทที่ 11)
        add(field);                     // ใส่ช่องพิมพ์ลงในกล่อง

        showHint();                     // เริ่มต้นแสดงข้อความจาง
    }                                   // จบคอนสตรักเตอร์

    private void showHint() {           // ใส่ข้อความจางลงในช่อง
        showingHint = true;             // จำไว้ว่าตอนนี้เป็นข้อความจาง
        field.setText(hint);            // ใส่ข้อความจาง
        field.setForeground(Theme.HINT);    // เปลี่ยนตัวอักษรเป็นสีเทาจาง
        if (password) {                     // ถ้าเป็นช่องรหัสผ่าน
            ((JPasswordField) field).setEchoChar((char) 0);   // แสดงข้อความจางเป็นตัวอักษรปกติ
        }                                   // จบ if
    }                                       // จบ showHint

    public void focusGained(FocusEvent e) { // ทำงานเมื่อผู้ใช้คลิกเข้าช่อง (ได้โฟกัส)
        if (showingHint) {                  // ถ้าในช่องยังเป็นข้อความจาง
            showingHint = false;            // ไม่ใช่ข้อความจางแล้ว
            field.setText("");              // ล้างช่องให้ว่าง พร้อมให้พิมพ์
            field.setForeground(Theme.TEXT);    // เปลี่ยนตัวอักษรเป็นสีเข้มปกติ
            if (password) {                     // ถ้าเป็นช่องรหัสผ่าน
                ((JPasswordField) field).setEchoChar('•');    // กลับมาแสดงเป็นจุด
            }                                   // จบ if
        }                                       // จบ if
    }                                           // จบ focusGained

    public void focusLost(FocusEvent e) {       // ทำงานเมื่อผู้ใช้คลิกออกจากช่อง (เสียโฟกัส)
        if (field.getText().equals("")) {       // ถ้าผู้ใช้ไม่ได้พิมพ์อะไร
            showHint();                         // ใส่ข้อความจางกลับเข้าไป
        }                                       // จบ if
    }                                           // จบ focusLost

    // ข้อความที่ผู้ใช้พิมพ์ (ถ้ายังเป็นข้อความจางจะได้ "")
    public String getText() {                                                               // เริ่มเมธอด getText
        if (showingHint) {          // ถ้ายังเป็นข้อความจาง แปลว่าผู้ใช้ยังไม่ได้พิมพ์
            return "";              // คืนค่าว่าง (ไม่ใช่คืนข้อความจาง)
        }                           // จบ if
        return field.getText();     // คืนข้อความที่ผู้ใช้พิมพ์
    }                               // จบ getText

    // ล้างช่องกลับเป็นข้อความจาง (ใช้หลังสมัครสำเร็จ / ออกจากระบบ)
    public void clear() {
        showHint();                 // ใส่ข้อความจางกลับเข้าไป (getText จะคืน "")
    }                               // จบ clear

    // เปิด/ปิดโหมดพิมพ์ได้แค่ตัวเลข
    public void setDigitsOnly(boolean digitsOnly) {
        this.digitsOnly = digitsOnly;   // เก็บค่าไว้ให้ keyTyped ใช้ตัดสินใจ
    }                                   // จบ setDigitsOnly

    // ================= KeyListener (สไลด์บทที่ 11) =================
    public void keyTyped(KeyEvent e) {                       // ทำงานทุกครั้งที่พิมพ์ตัวอักษร 1 ตัว (ก่อนตัวอักษรจะเข้าไปในช่อง)
        char c = e.getKeyChar();                             // ตัวอักษรที่ผู้ใช้กด (แบบเดียวกับ e.getKeyChar() ในสไลด์)
        if (digitsOnly && (c < '0' || c > '9')) {            // ถ้าเปิดโหมดตัวเลข และตัวที่กดไม่ใช่ 0-9
            e.consume();                                     // ทิ้งตัวอักษรนี้ ไม่ให้เข้าไปในช่อง (Backspace/Delete ยังใช้ได้ตามปกติ)
        }                                                    // จบ if
    }                                                        // จบ keyTyped

    public void keyPressed(KeyEvent e) { }    // KeyListener ต้อง override ครบทุกเมธอด (ไม่ได้ใช้ ปล่อยว่าง)
    public void keyReleased(KeyEvent e) { }   // ปล่อยแป้น (ไม่ได้ใช้)

    public JTextField getField() {  // คืนช่องพิมพ์จริง (เผื่อใช้ addActionListener / addKeyListener)
        return field;               // คืนค่า field
    }                               // จบ getField
}                                   // จบคลาส InputBox
