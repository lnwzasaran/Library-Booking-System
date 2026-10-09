import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * ปุ่มมุมโค้ง 2 แบบ : PRIMARY (พื้นน้ำเงิน ตัวขาว) และ OUTLINE (พื้นขาว ขอบน้ำเงิน)
 * มีสีตอนเอาเมาส์ชี้ (hover) และตอนกด
 */
public class RoundButton extends JButton {

    public enum Kind { PRIMARY, OUTLINE }

    private static final int ARC = 10;

    private final Kind kind;
    private boolean hover = false;

    public RoundButton(String text, Icon icon, Kind kind) {
        super(text, icon);
        this.kind = kind;
        setFont(Theme.bold(16));
        setForeground(kind == Kind.PRIMARY ? Theme.WHITE : Theme.BLUE);
        setIconTextGap(10);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
            @Override public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.smooth(g);
        boolean pressed = getModel().isArmed() && getModel().isPressed();
        int w = getWidth(), h = getHeight();
        if (kind == Kind.PRIMARY) {
            g2.setColor(pressed ? Theme.BLUE_DARK : hover ? Theme.BLUE_LIGHT : Theme.BLUE);
            g2.fillRoundRect(0, 0, w, h, ARC, ARC);
        } else {
            g2.setColor(pressed ? new Color(214, 228, 248) : hover ? Theme.BLUE_TINT : Theme.WHITE);
            g2.fillRoundRect(0, 0, w - 1, h - 1, ARC, ARC);
            g2.setColor(Theme.BLUE);
            g2.setStroke(new BasicStroke(1.2f));
            g2.drawRoundRect(0, 0, w - 1, h - 1, ARC, ARC);
        }
        g2.dispose();
        super.paintComponent(g); // วาดไอคอนและข้อความทับ
    }
}
