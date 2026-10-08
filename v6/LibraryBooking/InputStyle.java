import java.awt.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.text.JTextComponent;

/**
 * หน้าตาช่องกรอกข้อมูลที่ใช้ร่วมกันระหว่าง InputField และ PasswordInput:
 * พื้นมุมโค้ง, ขอบเปลี่ยนสีตอนโฟกัส/ตอนผิด, ไอคอนด้านซ้าย และข้อความจาง ๆ (placeholder)
 */
final class InputStyle {

    private InputStyle() { }

    static final int ARC = 10;
    static final String ERROR_KEY = "input.error";

    /** ตั้งค่าเริ่มต้นให้ช่องกรอก */
    static void apply(JTextComponent field, Icon icon, Color background) {
        field.setOpaque(false);              // เราวาดพื้นมุมโค้งเอง
        field.setBackground(background);
        field.setFont(Theme.font(15));
        field.setForeground(Theme.TEXT);
        field.setCaretColor(Theme.TEXT);
        int left = (icon == null) ? 14 : 14 + icon.getIconWidth() + 10;
        field.setBorder(new RoundBorder(left));
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusGained(java.awt.event.FocusEvent e) { field.repaint(); }
            @Override public void focusLost(java.awt.event.FocusEvent e)   { field.repaint(); }
        });
    }

    /** วาดพื้นหลัง — เรียกก่อน super.paintComponent */
    static void paintBackground(JTextComponent field, Graphics g) {
        Graphics2D g2 = Theme.smooth(g);
        g2.setColor(field.getBackground());
        g2.fillRoundRect(0, 0, field.getWidth() - 1, field.getHeight() - 1, ARC, ARC);
        g2.dispose();
    }

    /** วาดไอคอนและ placeholder — เรียกหลัง super.paintComponent */
    static void paintDecorations(JTextComponent field, Graphics g, Icon icon, String placeholder) {
        Graphics2D g2 = Theme.smooth(g);
        if (icon != null) {
            icon.paintIcon(field, g2, 14, (field.getHeight() - icon.getIconHeight()) / 2);
        }
        if (field.getDocument().getLength() == 0 && placeholder != null) {
            Insets in = field.getInsets();
            g2.setFont(field.getFont());
            g2.setColor(Theme.PLACEHOLDER);
            FontMetrics fm = g2.getFontMetrics();
            int baseline = (field.getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(placeholder, in.left, baseline);
        }
        g2.dispose();
    }

    static void setError(JTextComponent field, boolean error) {
        field.putClientProperty(ERROR_KEY, error);
        field.repaint();
    }

    /** ขอบมุมโค้ง : แดงเมื่อผิด, น้ำเงินเมื่อโฟกัส, เทาตามปกติ */
    private static final class RoundBorder extends AbstractBorder {
        private final int left;

        RoundBorder(int left) {
            this.left = left;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            boolean error = Boolean.TRUE.equals(((JComponent) c).getClientProperty(ERROR_KEY));
            Graphics2D g2 = Theme.smooth(g);
            if (error) {
                g2.setColor(Theme.ERROR);
                g2.setStroke(new BasicStroke(1.6f));
            } else if (c.hasFocus()) {
                g2.setColor(Theme.BLUE);
                g2.setStroke(new BasicStroke(1.6f));
            } else {
                g2.setColor(Theme.BORDER);
                g2.setStroke(new BasicStroke(1.1f));
            }
            g2.drawRoundRect(x + 1, y + 1, w - 3, h - 3, ARC, ARC);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(4, left, 4, 14);
        }
    }
}
