package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;            // Font, Window, Component
import java.awt.event.*;      // ActionListener, ActionEvent
import javax.swing.*;         // JDialog, JLabel, JPanel

/*
 * กล่องข้อความของโปรแกรม (ใช้แทน JOptionPane)
 *   - สี่เหลี่ยมมุมเหลี่ยม: ปิดกรอบหน้าต่างของ Windows (setUndecorated) เพราะ Windows 11 จะทำมุมโค้งให้หน้าต่างที่มีกรอบเสมอ
 *     แล้ววาดแถบหัวเองให้หน้าตาเหมือนของ Windows แทน
 *   - ไม่มีไอคอน (ไม่มีวงกลมแดงกากบาท / วงกลมเขียวเครื่องหมายถูก)
 *   - แถบหัวหน้าตาแบบแถบหัวหน้าต่างของ Windows: พื้นสีอ่อน + ชื่อกล่องตัวดำ + ปุ่ม ✕ (เอาเมาส์วางแล้วเป็นสีแดง)
 *   - ตรงกลางเป็นข้อความ, ล่างเป็นปุ่มสี่เหลี่ยม (RoundButton)
 *   - เป็น modal: โปรแกรมรอจนผู้ใช้กดปุ่ม แล้วค่อยทำบรรทัดถัดไป (เหมือน JOptionPane)
 *
 * วิธีใช้:
 *   MessageBox.show(this, "สมัครสมาชิก", "สมัครสำเร็จ");                       // ปุ่ม ตกลง ปุ่มเดียว
 *   boolean yes = MessageBox.confirm(this, "ยกเลิกการจอง", "คุณแน่ใจว่าจะกดยกเลิก?");   // ปุ่ม ใช่ / ไม่ → ใช่ = true
 */
public class MessageBox extends JDialog implements ActionListener, MouseListener {   // สืบทอด JDialog (หน้าต่างย่อย) + ฟังการกดปุ่ม และเมาส์บนปุ่ม ✕

    private static final int MIN_WIDTH = 360;    // ความกว้างขั้นต่ำของกล่อง
    private static final int HEIGHT = 170;       // ความสูงของกล่อง
    private static final int TITLE_H = 32;       // ความสูงแถบหัวกล่อง (เท่าแถบหัวหน้าต่างของ Windows)
    private static final Color TITLE_BG = new Color(0xEEF2F8);     // สีพื้นแถบหัว (สีอ่อนแบบแถบหัวของ Windows 11)
    private static final Color FRAME_LINE = new Color(0x9AA3AF);   // สีเส้นกรอบรอบกล่อง
    private static final Color CLOSE_HOVER = new Color(0xC42B1C);  // สีแดงตอนเอาเมาส์วางบนปุ่ม ✕ (แบบ Windows)

    private RoundButton yesButton;               // ปุ่ม "ตกลง" หรือ "ใช่"
    private RoundButton noButton;                // ปุ่ม "ไม่" (มีเฉพาะกล่องยืนยัน)
    private CloseButton closeButton;             // ปุ่ม ✕ มุมขวาบน (ปิดกล่อง = เหมือนกด "ไม่")
    private boolean answer = false;              // ผลลัพธ์: true = กด ตกลง/ใช่

    // parent = หน้าที่เรียก (ใช้หาหน้าต่างหลักเพื่อวางกล่องไว้ตรงกลาง), confirm = true → มีปุ่ม ใช่ / ไม่
    private MessageBox(Component parent, String title, String message, boolean confirm) {
        super(ownerOf(parent), ModalityType.APPLICATION_MODAL);   // หน้าต่างย่อยของหน้าต่างหลัก แบบรอจนปิด
        setUndecorated(true);                    // ไม่ใช้กรอบหน้าต่างของระบบ (ไม่มีมุมโค้ง / ปุ่ม X ของ Windows)

        JLabel text = new JLabel(message, JLabel.CENTER);    // ข้อความในกล่อง จัดกลาง
        text.setFont(Theme.font(Font.PLAIN, 15));            // Tahoma 15 (แสดงภาษาไทยได้)
        text.setForeground(Theme.TEXT);                      // สีเข้ม
        int width = text.getPreferredSize().width + 80;      // กว้างตามความยาวข้อความ + ขอบซ้ายขวา
        if (width < MIN_WIDTH) {                             // ข้อความสั้น
            width = MIN_WIDTH;                               // ใช้ความกว้างขั้นต่ำ
        }                                                    // จบ if

        RoundPanel box = new RoundPanel(Theme.WHITE, FRAME_LINE);      // พื้นขาว เส้นกรอบสีเทา (สี่เหลี่ยม)
        setContentPane(box);                                           // ใช้กล่องนี้เป็นพื้นของหน้าต่างทั้งหมด

        RoundPanel titleBar = new RoundPanel(TITLE_BG, null);          // แถบหัวกล่องสีอ่อน (หน้าตาแบบแถบหัวหน้าต่างของ Windows)
        titleBar.setBounds(1, 1, width - 2, TITLE_H);                  // เต็มความกว้าง ด้านบน (เว้นเส้นกรอบ 1 พิกเซล)
        box.add(titleBar);                                             // ใส่ลงในกล่อง

        JLabel titleText = new JLabel(title);                          // ชื่อกล่อง
        titleText.setFont(Theme.font(Font.PLAIN, 12));                 // ตัวปกติ 12 (แบบชื่อหน้าต่าง)
        titleText.setForeground(Color.BLACK);                          // สีดำ
        titleText.setBounds(12, 0, width - 70, TITLE_H);               // ชิดซ้ายในแถบ
        titleBar.add(titleText);                                       // ใส่ลงในแถบ

        closeButton = new CloseButton();                               // ปุ่ม ✕ (วาดเส้นไขว้เอง)
        closeButton.setBounds(width - 2 - 46, 0, 46, TITLE_H);         // ชิดขวาสุดของแถบ กว้าง 46 แบบ Windows
        closeButton.addMouseListener(this);                            // คลิก / เอาเมาส์วาง → MouseListener
        titleBar.add(closeButton);                                     // ใส่ลงในแถบ

        text.setBounds(20, TITLE_H + 18, width - 40, 40);              // ข้อความอยู่กลางกล่อง
        box.add(text);                                                 // ใส่ลงในกล่อง

        if (confirm) {                                                 // กล่องยืนยัน: ปุ่ม ใช่ / ไม่
            yesButton = createButton("ใช่", true);                      // ปุ่มน้ำเงิน
            noButton = createButton("ไม่", false);                      // ปุ่มขาวขอบน้ำเงิน
            yesButton.setBounds(width / 2 - 110, HEIGHT - 52, 100, 34);   // ซ้ายของกึ่งกลาง
            noButton.setBounds(width / 2 + 10, HEIGHT - 52, 100, 34);     // ขวาของกึ่งกลาง
            box.add(yesButton);                                        // ใส่ลงในกล่อง
            box.add(noButton);                                         // ใส่ลงในกล่อง
            getRootPane().setDefaultButton(noButton);                  // กด Enter = "ไม่" (กันยกเลิกพลาด)
        } else {                                                       // กล่องแจ้งข้อความ: ปุ่ม ตกลง ปุ่มเดียว
            yesButton = createButton("ตกลง", true);                     // ปุ่มน้ำเงิน
            yesButton.setBounds(width / 2 - 50, HEIGHT - 52, 100, 34);  // กลางกล่อง
            box.add(yesButton);                                        // ใส่ลงในกล่อง
            getRootPane().setDefaultButton(yesButton);                 // กด Enter = ตกลง
        }                                                              // จบ if

        setSize(width, HEIGHT);                          // ขนาดกล่อง
        setLocationRelativeTo(getOwner());               // วางไว้กลางหน้าต่างหลัก
    }                                                    // จบคอนสตรักเตอร์

    // หาหน้าต่างหลักของ parent: ถ้า parent เป็นหน้าต่างอยู่แล้ว (เช่น AppFrame) ใช้ตัวมันเลย ไม่งั้นหาหน้าต่างที่มันอยู่ข้างใน
    private static Window ownerOf(Component parent) {
        if (parent instanceof Window) {                          // parent เป็นหน้าต่าง (JFrame)
            return (Window) parent;                              // ใช้ตัวมันเอง
        }                                                        // จบ if
        return SwingUtilities.getWindowAncestor(parent);         // parent เป็น panel → หาหน้าต่างที่ครอบอยู่
    }                                                            // จบ ownerOf

    // เมธอดช่วย: ปุ่มสี่เหลี่ยม (primary = ปุ่มหลักสีน้ำเงิน, ไม่ใช่ = พื้นขาวขอบน้ำเงิน)
    private RoundButton createButton(String label, boolean primary) {
        RoundButton b = new RoundButton(label);                          // สร้างปุ่ม
        b.setFont(Theme.font(Font.BOLD, 14));                            // ตัวหนา 14
        if (primary) {                                                   // ปุ่มหลัก
            b.setColors(Theme.PRIMARY, Theme.WHITE, null);               // พื้นน้ำเงิน ตัวขาว
        } else {                                                         // ปุ่มรอง
            b.setColors(Theme.WHITE, Theme.PRIMARY, Theme.PRIMARY);      // พื้นขาว ตัวและขอบน้ำเงิน
        }                                                                // จบ if
        b.addActionListener(this);                                       // กด → actionPerformed
        return b;                                                        // คืนปุ่ม
    }                                                                    // จบ createButton

    // ================= MouseListener (ปุ่ม ✕) =================
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == closeButton) {       // คลิกปุ่ม ✕
            answer = false;                       // ปิดกล่อง = ไม่ตกลง (กล่องยืนยันนับเป็น "ไม่")
            dispose();                            // ปิดกล่อง
        }                                         // จบ if
    }                                             // จบ mouseClicked
    public void mouseEntered(MouseEvent e) {      // เอาเมาส์วางบนปุ่ม ✕
        closeButton.setHover(true);               // พื้นแดง เส้นขาว
    }                                             // จบ mouseEntered
    public void mouseExited(MouseEvent e) {       // เอาเมาส์ออกจากปุ่ม ✕
        closeButton.setHover(false);              // กลับเป็นปกติ
    }                                             // จบ mouseExited
    public void mousePressed(MouseEvent e) {}     // ไม่ใช้ (ต้องเขียนให้ครบ 5 เมธอดตาม interface)
    public void mouseReleased(MouseEvent e) {}    // ไม่ใช้

    // ปุ่ม ✕ มุมขวาบน: JPanel ที่วาดเส้นไขว้ 2 เส้นเอง (บทที่ 12) — ไม่ใช้ตัวอักษร เพราะบางฟอนต์ไม่มีเครื่องหมาย ✕
    private static class CloseButton extends JPanel {
        private boolean hover = false;            // เมาส์อยู่บนปุ่มไหม

        CloseButton() {                           // คอนสตรักเตอร์
            setOpaque(false);                     // พื้นวาดเองใน paintComponent
        }                                         // จบคอนสตรักเตอร์

        void setHover(boolean hover) {            // เปลี่ยนสถานะเมาส์
            this.hover = hover;                   // เก็บค่า
            repaint();                            // วาดใหม่
        }                                         // จบ setHover

        public void paintComponent(Graphics g) {  // วาดปุ่ม
            if (hover) {                          // เมาส์อยู่บนปุ่ม
                g.setColor(CLOSE_HOVER);          // พื้นแดง
                g.fillRect(0, 0, getWidth(), getHeight());   // ระบายเต็มปุ่ม
                g.setColor(Color.WHITE);          // เส้นขาว
            } else {                              // ปกติ
                g.setColor(Color.BLACK);          // เส้นดำ
            }                                     // จบ if
            int cx = getWidth() / 2;              // กึ่งกลางแนวนอน
            int cy = getHeight() / 2;             // กึ่งกลางแนวตั้ง
            g.drawLine(cx - 5, cy - 5, cx + 5, cy + 5);   // เส้นทแยง \
            g.drawLine(cx - 5, cy + 5, cx + 5, cy - 5);   // เส้นทแยง /
        }                                         // จบ paintComponent
    }                                             // จบคลาส CloseButton

    // ================= ActionListener (ปุ่มในกล่อง) =================
    public void actionPerformed(ActionEvent e) {
        answer = (e.getSource() == yesButton);    // กด ตกลง/ใช่ → true, กด ไม่ → false
        dispose();                                // ปิดกล่อง (โปรแกรมจะทำงานต่อจากบรรทัดที่เรียก show/confirm)
    }                                             // จบ actionPerformed

    // แสดงกล่องแจ้งข้อความ ปุ่ม "ตกลง" แล้วรอจนผู้ใช้กด
    public static void show(Component parent, String title, String message) {
        MessageBox m = new MessageBox(parent, title, message, false);   // สร้างกล่องแบบปุ่มเดียว
        m.setVisible(true);                                             // แสดงแล้วรอ (modal)
    }                                                                   // จบ show

    // แสดงกล่องยืนยัน ปุ่ม "ใช่" / "ไม่" แล้วรอจนผู้ใช้กด — คืนค่า true ถ้ากด "ใช่"
    public static boolean confirm(Component parent, String title, String message) {
        MessageBox m = new MessageBox(parent, title, message, true);    // สร้างกล่องแบบ 2 ปุ่ม
        m.setVisible(true);                                             // แสดงแล้วรอ (modal)
        return m.answer;                                                // ผลลัพธ์ที่ผู้ใช้เลือก
    }                                                                   // จบ confirm
}                                                                       // จบคลาส MessageBox
