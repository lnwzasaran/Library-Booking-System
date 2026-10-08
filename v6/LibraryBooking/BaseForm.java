import java.awt.*;
import javax.swing.*;

/**
 * แม่แบบของทุกหน้าจอ (Login / Register / Home)
 * ทำสิ่งที่ทุกหน้าต้องทำเหมือนกัน: ขนาดหน้าต่าง, null layout, จัดกลางจอ
 * หน้าลูกแค่ override setComponent() เพื่อวางของของตัวเอง (Template Method)
 */
public abstract class BaseForm extends JFrame {

    public static final int WIDTH = 1200;
    public static final int HEIGHT = 680;

    protected final Container cp;

    protected BaseForm(String title) {
        super("Library Booking System - " + title);
        cp = getContentPane();
        cp.setLayout(null);
        cp.setBackground(Theme.WHITE);
        ((JComponent) cp).setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    /** วางคอมโพเนนต์ทั้งหมดของหน้า */
    protected abstract void setComponent();

    /** สร้างหน้าจอแล้วแสดง — เรียกครั้งเดียวหลัง constructor ของหน้าลูก */
    public void open() {
        setComponent();
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /** ตัวช่วย: วาง c ลงใน parent ที่ตำแหน่ง (x, y) ขนาด w x h แล้วคืน c กลับไป */
    protected static <T extends Component> T place(Container parent, T c, int x, int y, int w, int h) {
        c.setBounds(x, y, w, h);
        parent.add(c);
        return c;
    }
}
