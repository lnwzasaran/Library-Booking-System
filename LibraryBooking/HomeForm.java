import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * หน้าหลักหลังเข้าสู่ระบบ — แถบเมนูซ้าย, การ์ดทักทาย, ภาพรวมการใช้งาน และทางลัด 4 เมนู
 * ตอนนี้ทำงานได้แค่ "หน้าหลัก" และ "ออกจากระบบ" ส่วนเมนูอื่นจะทำในขั้นต่อไป
 */
public class HomeForm extends BaseForm {

    private static final int SIDEBAR_W = 260;
    private static final int CONTENT_X = SIDEBAR_W + 22;
    private static final int CONTENT_W = WIDTH - CONTENT_X - 20;
    private static final int CARD_W = 205, CARD_GAP = (CONTENT_W - 4 * CARD_W) / 3;

    // ระบบจองห้องยังไม่ได้ทำ จึงใช้ค่าคงที่ไปก่อน — เมื่อมีข้อมูลห้อง/การจองจริงค่อยส่งตัวเลขเข้ามาแทน
    private static final int TOTAL_ROOMS = 10;
    private static final int AVAILABLE_ROOMS = 10;
    private static final int MY_BOOKINGS = 0;
    private static final int TODAY_BOOKINGS = 0;

    private final Member member;
    private Runnable onLogout = () -> { };

    public HomeForm(Member member) {
        super("หน้าหลัก");
        if (member == null) {
            throw new IllegalArgumentException("member must not be null");
        }
        this.member = member;
    }

    public void setOnLogout(Runnable listener) { this.onLogout = listener; }

    @Override
    protected void setComponent() {
        cp.setBackground(new Color(250, 250, 251));
        buildSidebar();
        buildWelcomeCard();
        place(cp, Theme.label("ภาพรวมการใช้งาน", Theme.bold(24), Theme.TEXT), CONTENT_X + 8, 182, 400, 40);
        buildStatCards();
        buildActionCards();
    }

    // ------------------------------------------------------------ แถบเมนูซ้าย
    private void buildSidebar() {
        JPanel sidebar = place(cp, new JPanel(null), 0, 0, SIDEBAR_W, HEIGHT);
        sidebar.setBackground(Theme.NAVY);

        place(sidebar, new JLabel(Icons.of(Icons.Type.BOOK, 44, Theme.WHITE)), 20, 30, 44, 44);
        place(sidebar, Theme.label("Library Booking", Theme.bold(20), Theme.WHITE), 72, 32, SIDEBAR_W - 76, 40);

        Object[][] menu = {
                {"หน้าหลัก", Icons.Type.HOME},
                {"ดูห้องทั้งหมด", Icons.Type.BUILDING},
                {"จองห้อง", Icons.Type.HOUSE_PLUS},
                {"การจองของฉัน", Icons.Type.CALENDAR},
                {"ข้อมูลส่วนตัว", Icons.Type.ACCOUNT},
        };
        for (int i = 0; i < menu.length; i++) {
            String text = (String) menu[i][0];
            boolean selected = (i == 0);
            SideMenuItem item = place(sidebar, new SideMenuItem(text, (Icons.Type) menu[i][1], selected),
                    20, 112 + i * 68, SIDEBAR_W - 40, 54);
            if (!selected) item.addActionListener(e -> comingSoon(text));
        }

        SideMenuItem logout = place(sidebar, new SideMenuItem("ออกจากระบบ", Icons.Type.LOGOUT, false),
                20, HEIGHT - 76, SIDEBAR_W - 40, 54);
        logout.addActionListener(e -> confirmLogout());
    }

    // ------------------------------------------------------------ การ์ดทักทาย
    private void buildWelcomeCard() {
        RoundPanel card = place(cp, new RoundPanel(Theme.CARD_GRAY, 18, null, 0), CONTENT_X, 26, CONTENT_W, 136);
        place(card, new JLabel(Icons.of(Icons.Type.ACCOUNT, 92, Theme.BLUE)), 36, 22, 92, 92);
        place(card, Theme.label("สวัสดี " + member.getFullName(), Theme.bold(28), Theme.TEXT), 150, 26, 700, 44);
        place(card, Theme.label("รหัสนักศึกษา   " + member.getUsername(), Theme.font(18), Theme.TEXT_MUTED),
                152, 76, 600, 28);
    }

    // ------------------------------------------------------------ ภาพรวมการใช้งาน
    private void buildStatCards() {
        statCard(0, "ห้องทั้งหมด", TOTAL_ROOMS + " ห้อง", Icons.Type.DOOR,
                new Color(214, 228, 240), new Color(45, 108, 203));
        statCard(1, "ห้องว่าง", AVAILABLE_ROOMS + " ห้อง", Icons.Type.CALENDAR_CHECK,
                new Color(228, 244, 226), new Color(46, 150, 60));
        statCard(2, "การจองห้องของฉัน", MY_BOOKINGS + " ห้อง", Icons.Type.LIST,
                new Color(243, 229, 245), new Color(126, 50, 160));
        statCard(3, "การจองวันนี้", TODAY_BOOKINGS + " รายการ", Icons.Type.LIST_ADD,
                new Color(253, 236, 236), new Color(220, 70, 70));
    }

    private void statCard(int index, String title, String value, Icons.Type icon, Color bg, Color accent) {
        RoundPanel card = place(cp, new RoundPanel(bg, 14, darker(bg), 4),
                CONTENT_X + index * (CARD_W + CARD_GAP), 228, CARD_W + 4, 160);
        place(card, new JLabel(Icons.of(icon, 40, accent)), 24, 22, 40, 40);
        place(card, Theme.label(title, Theme.font(16), Theme.TEXT), 26, 78, CARD_W - 30, 26);
        place(card, Theme.label(value, Theme.bold(30), Theme.TEXT), 24, 104, CARD_W - 30, 42);
    }

    // ------------------------------------------------------------ ทางลัด
    private void buildActionCards() {
        actionCard(0, "จองห้อง", "เลือกวันที่ เวลา", "ห้องที่ต้องการจอง",
                Icons.Type.HOUSE_PLUS, new Color(11, 140, 245));
        actionCard(1, "ดูห้องทั้งหมด", "ตรวจสอบสถานะ", "รายละเอียดการจอง",
                Icons.Type.BUILDING, new Color(70, 190, 90));
        actionCard(2, "การจองของฉัน", "ดูสถานะการจอง", "",
                Icons.Type.CALENDAR, new Color(242, 178, 60));
        actionCard(3, "โปรไฟล์", "จัดการข้อมูลส่วนตัว", "",
                Icons.Type.ACCOUNT, new Color(172, 88, 245));
    }

    private void actionCard(int index, String title, String line1, String line2, Icons.Type icon, Color color) {
        RoundPanel card = place(cp, new RoundPanel(Theme.WHITE, 14, new Color(200, 204, 210), 4),
                CONTENT_X + index * (CARD_W + CARD_GAP), 408, CARD_W + 4, 246);
        RoundPanel tile = place(card, new RoundPanel(color, 12, null, 3), 10, 10, CARD_W - 17, 140);
        place(tile, new JLabel(Icons.of(icon, 48, Theme.WHITE), SwingConstants.CENTER), 0, 24, CARD_W - 20, 50);
        place(tile, Theme.centeredLabel(title, Theme.bold(22), Theme.WHITE), 0, 80, CARD_W - 20, 36);
        place(card, Theme.centeredLabel(line1, Theme.font(15), Theme.TEXT), 0, 166, CARD_W, 24);
        place(card, Theme.centeredLabel(line2, Theme.font(15), Theme.TEXT), 0, 192, CARD_W, 24);

        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { card.setBorderColor(color); }
            @Override public void mouseExited(MouseEvent e)  { card.setBorderColor(new Color(200, 204, 210)); }
            @Override public void mouseClicked(MouseEvent e) { comingSoon(title); }
        });
    }

    // ------------------------------------------------------------ เหตุการณ์
    private void comingSoon(String feature) {
        JOptionPane.showMessageDialog(this, "เมนู \"" + feature + "\" จะทำในขั้นต่อไป",
                "Library Booking", JOptionPane.INFORMATION_MESSAGE);
    }

    private void confirmLogout() {
        int answer = JOptionPane.showConfirmDialog(this, "ต้องการออกจากระบบหรือไม่?",
                "ออกจากระบบ", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            onLogout.run();
        }
    }

    private static Color darker(Color c) {
        return new Color((int) (c.getRed() * 0.9), (int) (c.getGreen() * 0.9), (int) (c.getBlue() * 0.9));
    }
}
