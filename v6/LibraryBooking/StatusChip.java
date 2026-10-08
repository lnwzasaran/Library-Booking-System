import java.awt.*;
import javax.swing.*;

/** ป้ายสถานะมุมมน : เขียว "ว่าง" หรือแดง "ไม่ว่าง" */
public class StatusChip extends JLabel {

    private static final Color FREE_BG = new Color(220, 247, 213), FREE_LINE = new Color(150, 222, 135),
            FREE_TEXT = new Color(30, 140, 20);
    private static final Color BUSY_BG = new Color(253, 220, 222), BUSY_LINE = new Color(240, 160, 166),
            BUSY_TEXT = new Color(211, 47, 47);

    private final boolean free;

    /**
     * @param free     true = ว่าง (เขียว), false = ไม่ว่าง (แดง)
     * @param fontSize ขนาดตัวอักษร
     */
    public StatusChip(boolean free, int fontSize) {
        super(free ? "ว่าง" : "ไม่ว่าง", SwingConstants.CENTER);
        this.free = free;
        setFont(fontSize >= 15 ? Theme.bold(fontSize) : Theme.font(fontSize));
        setForeground(free ? FREE_TEXT : BUSY_TEXT);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.smooth(g);
        int w = getWidth() - 1, h = getHeight() - 1;
        g2.setColor(free ? FREE_BG : BUSY_BG);
        g2.fillRoundRect(0, 0, w, h, h, h);   // มุมโค้งเท่าความสูง = ปลายมนเป็นครึ่งวงกลม
        g2.setColor(free ? FREE_LINE : BUSY_LINE);
        g2.drawRoundRect(0, 0, w, h, h, h);
        g2.dispose();
        super.paintComponent(g);
    }
}
