import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 * แผงพื้นหลังรูปภาพ ย่อ/ขยายรูปให้เต็มพื้นที่แบบไม่บิดสัดส่วน (cover)
 * แล้วทับด้วยสีโปร่งแสงเพื่อให้ตัวหนังสือสีขาวอ่านง่าย
 * ถ้าไม่พบไฟล์รูป จะใช้สีพื้น (fallback) แทน โปรแกรมไม่พัง
 */
public class ImagePanel extends JPanel {

    private final BufferedImage image;  // อาจเป็น null ถ้าโหลดไม่ได้
    private final Color fallback;
    private final Color overlay;
    private final int imageTop;         // เริ่มวาดรูปที่ y เท่าไร (ด้านบนเป็นสีพื้นไว้วางหัวข้อ)

    /**
     * @param imagePath พาธไฟล์รูป เช่น "images/login_bg.jpg"
     * @param fallback  สีพื้น (ใช้เมื่อไม่มีรูป และใช้ไล่สีส่วนบนเมื่อ imageTop > 0)
     * @param overlay   สีโปร่งแสงที่ทับรูป (null = ไม่ทับ)
     * @param imageTop  ระยะจากขอบบนที่เริ่มวาดรูป
     */
    public ImagePanel(String imagePath, Color fallback, Color overlay, int imageTop) {
        super(null);
        this.image = loadImage(imagePath);
        this.fallback = fallback;
        this.overlay = overlay;
        this.imageTop = imageTop;
        setBackground(fallback);
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (Exception e) {
            System.err.println("โหลดรูปไม่ได้: " + path + " — ใช้สีพื้นแทน");
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = Theme.smooth(g);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int w = getWidth(), h = getHeight();
        g2.setColor(fallback);
        g2.fillRect(0, 0, w, h);

        if (image != null) {
            int areaH = h - imageTop;
            double scale = Math.max((double) w / image.getWidth(), (double) areaH / image.getHeight());
            int drawW = (int) Math.ceil(image.getWidth() * scale);
            int drawH = (int) Math.ceil(image.getHeight() * scale);
            int x = (w - drawW) / 2;
            int y = imageTop + (areaH - drawH) / 2;
            g2.setClip(0, imageTop, w, areaH);
            g2.drawImage(image, x, y, drawW, drawH, null);
            g2.setClip(null);
            if (imageTop > 0) { // ไล่สีจากสีพื้นลงมาทับขอบบนของรูปให้กลืนกัน
                Color clear = new Color(fallback.getRed(), fallback.getGreen(), fallback.getBlue(), 0);
                g2.setPaint(new GradientPaint(0, imageTop, fallback, 0, imageTop + 160, clear));
                g2.fillRect(0, imageTop, w, 160);
            }
        }
        if (overlay != null) {
            g2.setColor(overlay);
            g2.fillRect(0, 0, w, h);
        }
        g2.dispose();
    }
}
