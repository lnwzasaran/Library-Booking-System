package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;      // Color, Graphics
import javax.swing.*;   // JPanel

/*
 * JPanel พื้นหลังสี่เหลี่ยม (มุมเหลี่ยม ไม่มีความโค้ง) + เส้นขอบ — ใช้กับกล่องฟอร์ม การ์ด ป้าย ช่องกรอก ฯลฯ
 * ข้างในใช้ setLayout(null) วางของด้วย setBounds
 * (ชื่อคลาสยังเป็น RoundPanel เหมือนเดิม เพื่อไม่ให้โค้ดส่วนอื่นที่เรียกใช้ต้องแก้ตาม)
 */
public class RoundPanel extends JPanel {   // สืบทอด JPanel (Inheritance บทที่ 3) แล้วเปลี่ยนวิธีวาดพื้น

    private Color fill;     // สีพื้น
    private Color border;   // สีเส้นขอบ (null = ไม่มีเส้นขอบ)

    public RoundPanel(Color fill, Color border) {   // คอนสตรักเตอร์: รับสีพื้น และสีขอบ
        this.fill = fill;           // this.fill = ตัวแปรของ object, fill = ค่าที่ส่งเข้ามา (บทที่ 4)
        this.border = border;       // เก็บสีขอบ
        setLayout(null);            // ไม่ใช้ Layout Manager — ของที่ใส่ข้างในวางด้วย setBounds
        setOpaque(false);           // ไม่ให้ JPanel ระบายพื้นเอง (เราระบายเองใน paintComponent)
    }                               // จบคอนสตรักเตอร์

    // เปลี่ยนสีพื้นและสีขอบภายหลัง (เช่น ตัวกรองที่ถูกเลือกเปลี่ยนเป็นสีน้ำเงิน)
    public void setColors(Color fill, Color border) {
        this.fill = fill;           // สีพื้นใหม่
        this.border = border;       // สีขอบใหม่ (null = ไม่มีขอบ)
        repaint();                  // วาดใหม่ด้วยสีใหม่ (บทที่ 12)
    }                               // จบ setColors

    public void paintComponent(Graphics g) {     // override: Java เรียกเมธอดนี้อัตโนมัติทุกครั้งที่ต้องวาด panel (บทที่ 12)
        super.paintComponent(g);                 // ให้ JPanel ทำงานวาดพื้นฐานของมันก่อน
        g.setColor(fill);                        // ตั้งสีเป็นสีพื้น
        g.fillRect(0, 0, getWidth(), getHeight());   // ระบายสี่เหลี่ยมเต็มขนาด panel
        if (border != null) {                    // ถ้ามีกำหนดสีเส้นขอบ
            g.setColor(border);                  // ตั้งสีเป็นสีขอบ
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);   // วาดเส้นขอบ (-1 เพื่อให้เส้นขวา/ล่างไม่ล้นขอบ)
        }                                        // จบ if
    }                                            // จบ paintComponent
}                                                // จบคลาส RoundPanel
