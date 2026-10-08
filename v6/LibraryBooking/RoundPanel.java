import java.awt.*;
import javax.swing.*;

/**
 * JPanel มุมโค้ง มีเงาจาง ๆ และขอบ (ถ้าต้องการ) ใช้ทำการ์ดต่าง ๆ
 * layout เป็น null เพื่อวางของข้างในด้วย setBounds เหมือนหน้าอื่น
 */
public class RoundPanel extends JPanel {

    private final int arc;
    private Color borderColor;
    private final int shadow;

    /**
     * @param background  สีพื้น
     * @param arc         ความโค้งของมุม
     * @param borderColor สีขอบ (null = ไม่มีขอบ)
     * @param shadow      ความหนาของเงาด้านล่าง/ขวาเป็นพิกเซล (0 = ไม่มีเงา)
     */
    public RoundPanel(Color background, int arc, Color borderColor, int shadow) {
        super(null);
        this.arc = arc;
        this.borderColor = borderColor;
        this.shadow = shadow;
        setBackground(background);
        setOpaque(false);
    }

    /** เปลี่ยนสีขอบ (ใช้ทำ hover) */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.smooth(g);
        int w = getWidth() - shadow - 1;
        int h = getHeight() - shadow - 1;
        for (int i = shadow; i > 0; i--) {   // เงา: วาดสี่เหลี่ยมโปร่งใสซ้อนกันทีละชั้น
            g2.setColor(new Color(0, 0, 0, 10 + (shadow - i) * 4));
            g2.fillRoundRect(i, i + 1, w, h, arc + i, arc + i);
        }
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, w, h, arc, arc);
        if (borderColor != null) {
            g2.setColor(borderColor);
            g2.drawRoundRect(0, 0, w, h, arc, arc);
        }
        g2.dispose();
    }
}
