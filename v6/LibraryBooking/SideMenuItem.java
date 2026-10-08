import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/** ปุ่มเมนูด้านซ้าย (ใช้ทั้งหน้า Home และหน้า Admin): ตัวที่เลือกอยู่มีพื้นน้ำเงิน ตัวอื่นเปลี่ยนสีตอนเอาเมาส์ชี้ */
public class SideMenuItem extends JButton {
    private final boolean selected;
    private boolean hover = false;

    public SideMenuItem(String text, Icons.Type icon, boolean selected) {
        super(text, Icons.of(icon, 30, Theme.WHITE));
        this.selected = selected;
        setFont(Theme.font(18));
        setForeground(Theme.WHITE);
        setHorizontalAlignment(SwingConstants.LEFT);
        setIconTextGap(20);
        setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));
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
        if (selected || hover) {
            Graphics2D g2 = Theme.smooth(g);
            g2.setColor(selected ? Theme.BLUE : Theme.NAVY_HOVER);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
            g2.dispose();
        }
        super.paintComponent(g);
    }
}
