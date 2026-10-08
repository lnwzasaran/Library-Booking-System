import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

/**
 * สี ฟอนต์ และตัวช่วยสร้าง JLabel ที่ทุกหน้าใช้ร่วมกัน
 * แก้สีหรือฟอนต์ที่นี่ที่เดียว ทุกหน้าเปลี่ยนตาม (Don't Repeat Yourself)
 */
public final class Theme {

    private Theme() { } // utility class — ไม่ให้ new

    // ---------------- สี ----------------
    public static final Color BLUE        = new Color(45, 108, 203);
    public static final Color BLUE_DARK   = new Color(33, 86, 168);
    public static final Color BLUE_LIGHT  = new Color(66, 133, 226);
    public static final Color BLUE_TINT   = new Color(234, 241, 252);
    public static final Color NAVY        = new Color(31, 42, 60);
    public static final Color NAVY_HOVER  = new Color(46, 59, 82);
    public static final Color TEXT        = new Color(31, 36, 48);
    public static final Color TEXT_MUTED  = new Color(96, 104, 117);
    public static final Color PLACEHOLDER = new Color(150, 156, 166);
    public static final Color BORDER      = new Color(160, 166, 176);
    public static final Color ERROR       = new Color(211, 47, 47);
    public static final Color PANEL_GRAY  = new Color(245, 245, 246);
    public static final Color CARD_GRAY   = new Color(235, 235, 237);
    public static final Color WHITE       = Color.WHITE;

    // ---------------- ฟอนต์ ----------------
    /** ลองตามลำดับ ตัวแรกที่มีในเครื่องจะถูกใช้ (ทุกตัวรองรับทั้งไทยและอังกฤษ) */
    private static final String[] PREFERRED_FAMILIES = {
            "IBM Plex Sans Thai Looped", "Noto Sans Thai Looped", "Noto Sans Thai",
            "Leelawadee UI", "Leelawadee", "Tahoma"
    };
    private static final String FAMILY = pickFamily();

    /** ถ้ามีโฟลเดอร์ fonts/ ที่มีไฟล์ .ttf จะโหลดเข้ามาก่อน (เช่นอยากใช้ฟอนต์ Kanit) */
    private static String pickFamily() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        File[] files = new File("fonts").listFiles((dir, name) -> name.toLowerCase().endsWith(".ttf"));
        String bundledFamily = null;
        if (files != null) {
            for (File f : files) {
                try {
                    Font font = Font.createFont(Font.TRUETYPE_FONT, f);
                    ge.registerFont(font);
                    if (bundledFamily == null) bundledFamily = font.getFamily();
                } catch (Exception e) {
                    System.err.println("โหลดฟอนต์ไม่ได้: " + f + " (" + e.getMessage() + ")");
                }
            }
        }
        if (bundledFamily != null) return bundledFamily;
        Set<String> installed = new HashSet<>(Arrays.asList(ge.getAvailableFontFamilyNames()));
        for (String family : PREFERRED_FAMILIES) {
            if (installed.contains(family)) return family;
        }
        return Font.SANS_SERIF;
    }

    public static Font font(int size)  { return new Font(FAMILY, Font.PLAIN, size); }
    public static Font bold(int size)  { return new Font(FAMILY, Font.BOLD, size); }

    // ---------------- ตัวช่วยสร้าง JLabel ----------------
    public static JLabel label(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    public static JLabel centeredLabel(String text, Font font, Color color) {
        JLabel label = label(text, font, color);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /** JLabel ที่มีไอคอนอยู่หน้าข้อความ */
    public static JLabel iconLabel(String text, Icon icon, Font font, Color color) {
        JLabel label = label(text, font, color);
        label.setIcon(icon);
        label.setIconTextGap(10);
        return label;
    }

    /** เปิด antialias ให้รูปทรงและตัวอักษรที่วาดเองเรียบ */
    public static Graphics2D smooth(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        return g2;
    }
}
