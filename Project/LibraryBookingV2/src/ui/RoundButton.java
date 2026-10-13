package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Graphics, Font, FontMetrics
import javax.swing.*;   // JButton

/*
 * ปุ่มสี่เหลี่ยม วาดเองใน paintComponent (พื้น + เส้นขอบ + ไอคอน + ข้อความ)
 * ยังเป็น JButton อยู่ จึงใช้ addActionListener ได้ตามปกติ
 */
public class RoundButton extends JButton {   // สืบทอด JButton: ได้ความสามารถกดได้ + ActionListener มาเลย

    private String label;                    // ข้อความบนปุ่ม (เราวาดเอง)
    private Color bg = Theme.PRIMARY;        // สีพื้นปุ่ม (ค่าเริ่มต้น = น้ำเงินเข้ม)
    private Color fg = Theme.WHITE;          // สีตัวอักษร (ค่าเริ่มต้น = ขาว)
    private Color border = null;             // สีเส้นขอบ (ค่าเริ่มต้น = ไม่มี)
    private IconPanel icon = null;           // ไอคอนหน้าข้อความ (ค่าเริ่มต้น = ไม่มี)

    public RoundButton(String label) {       // คอนสตรักเตอร์: รับข้อความบนปุ่ม
        this.label = label;                  // เก็บข้อความไว้วาดใน paintComponent
        setFont(Theme.font(Font.BOLD, 14));  // ฟอนต์เริ่มต้นของปุ่ม (ตัวหนา 14)
        setContentAreaFilled(false);         // ไม่ให้ Swing วาดพื้นปุ่มเอง
        setBorderPainted(false);             // ไม่ให้ Swing วาดกรอบปุ่มเอง
        setFocusPainted(false);              // ไม่ให้วาดกรอบเส้นประตอนปุ่มได้โฟกัส
        setOpaque(false);                    // ไม่ให้ Swing ระบายพื้นเอง (เราระบายเองใน paintComponent)
    }                                        // จบคอนสตรักเตอร์

    // พื้น / ตัวอักษร / เส้นขอบ (null = ไม่มีขอบ)
    public void setColors(Color bg, Color fg, Color border) {   // ใช้เปลี่ยนสีปุ่ม เช่น ปุ่มพื้นขาวขอบน้ำเงิน
        this.bg = bg;                        // สีพื้นใหม่
        this.fg = fg;                        // สีตัวอักษรใหม่
        this.border = border;                // สีขอบใหม่
    }                                        // จบ setColors

    public void setIconPanel(IconPanel icon) {   // ใส่ไอคอนหน้าข้อความ
        this.icon = icon;                        // เก็บไอคอนไว้วาดใน paintComponent
    }                                            // จบ setIconPanel

    public String getButtonText() {              // ข้อความบนปุ่ม (เช่น เลขหน้า "2")
        return label;                            // คืนข้อความที่เก็บไว้
    }                                            // จบ getButtonText

    public void paintComponent(Graphics g) {     // override: วาดปุ่มเองทั้งหมด (ไม่เรียก super เพราะไม่อยากให้ Swing วาดซ้ำ)
        // พื้นและเส้นขอบ
        g.setColor(bg);                                              // ตั้งสีพื้น
        g.fillRect(0, 0, getWidth(), getHeight());                   // ระบายพื้นปุ่มสี่เหลี่ยม
        if (border != null) {                                        // ถ้ามีเส้นขอบ
            g.setColor(border);                                      // ตั้งสีขอบ
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);       // วาดเส้นขอบ
        }                                                            // จบ if

        // หาความกว้างของข้อความ + ไอคอน เพื่อจัดให้อยู่กึ่งกลางปุ่ม
        g.setFont(getFont());                     // ใช้ฟอนต์ของปุ่ม
        FontMetrics fm = g.getFontMetrics();      // ตัววัดขนาดตัวอักษรของฟอนต์นี้
        int textWidth = fm.stringWidth(label);    // ความกว้างของข้อความ (พิกเซล)
        int iconWidth = 0;                        // ความกว้างไอคอน + ช่องว่าง (เริ่มที่ 0 = ไม่มีไอคอน)
        if (icon != null) {                       // ถ้ามีไอคอน
            iconWidth = icon.getIconSize() + 8;   // ขนาดไอคอน + เว้นห่างจากข้อความ 8 พิกเซล
        }                                         // จบ if
        int x = (getWidth() - textWidth - iconWidth) / 2;   // จุดเริ่มด้านซ้าย ที่ทำให้ (ไอคอน+ข้อความ) อยู่กลางปุ่ม

        if (icon != null) {                                              // ถ้ามีไอคอน
            icon.draw(g, x, (getHeight() - icon.getIconSize()) / 2);     // วาดไอคอนที่ x, กึ่งกลางแนวตั้ง
            x = x + iconWidth;                                           // เลื่อนจุดเริ่มข้อความไปทางขวา
        }                                                                // จบ if

        g.setColor(fg);                           // ตั้งสีตัวอักษร
        g.setFont(getFont());                     // ตั้งฟอนต์อีกครั้ง (ไอคอนบางตัวเปลี่ยนฟอนต์ระหว่างวาด)
        int y = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;   // ตำแหน่งเส้นฐานตัวอักษรที่ทำให้อยู่กลางแนวตั้ง
        g.drawString(label, x, y);                // วาดข้อความ (บทที่ 12: drawString)
    }                                             // จบ paintComponent
}                                                 // จบคลาส RoundButton
