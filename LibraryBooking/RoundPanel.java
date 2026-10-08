import javax.swing.*;                                // JPanel
import java.awt.*;                                   // Graphics, Color

// พาเนลมุมโค้ง: สืบทอด JPanel แล้ว override paintComponent (สไลด์ Section 12: Graphic)
public class RoundPanel extends JPanel {
    private int radius;                              // ความโค้งของมุม
    private Color borderColor;                       // สีขอบ (null = ไม่มีขอบ)

    public RoundPanel(int radius, Color bg, Color borderColor) {
        this.radius = radius;
        this.borderColor = borderColor;
        setLayout(null);                             // วาง component ข้างในด้วย setBounds
        setBackground(bg);                           // สีพื้นของการ์ด
        setOpaque(false);                            // ไม่ให้ Swing ทาสี่เหลี่ยมทับมุมโค้ง
    }

    public void paintComponent(Graphics g) {         // Swing เรียกอัตโนมัติเมื่อต้องวาด
        super.paintComponent(g);                     // เรียกของแม่ก่อน (เหมือนสไลด์ Section 12 หน้า Animation)
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);  // ทำให้ขอบโค้งเนียน (เกินจากสไลด์ 1 บรรทัด)
        g.setColor(getBackground());                 // ใช้สีที่ตั้งด้วย setBackground
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius); // สี่เหลี่ยมมุมโค้งแบบทึบ
        if (borderColor != null) {
            g.setColor(borderColor);
            g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius); // เส้นขอบ
        }
    }
}
