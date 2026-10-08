import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

/**
 * หน้าหลักของผู้ดูแลระบบ (role = ADMIN) — ตอนนี้แสดงผลอย่างเดียว ยังกดอะไรไม่ได้
 *
 *  ┌ แถบเมนู ┐┌──────────── แถบบน (แจ้งเตือน + ชื่อผู้ดูแล) ────────────┐
 *  │         ││ การ์ดต้อนรับ + วันเวลา                  │ สถานะห้อง     │
 *  │         ││ การ์ดตัวเลข 4 ใบ                         │ (10 ห้อง)     │
 *  │         ││ ปฏิทินการจอง                             │               │
 *  │         ││ ตารางการจองล่าสุด                         │               │
 */
public class AdminHomeForm extends BaseForm {

    private static final int SIDEBAR_W = 270;
    private static final int MAIN_X = SIDEBAR_W + 12, MAIN_W = 640;   // คอลัมน์กลาง
    private static final int SIDE_X = MAIN_X + MAIN_W + 12, SIDE_W = WIDTH - SIDE_X - 10; // คอลัมน์สถานะห้อง

    private static final Color PAGE_BG    = new Color(238, 246, 248);
    private static final Color HEADER_BG  = new Color(223, 234, 247);
    private static final Color HEADER_TXT = new Color(30, 90, 180);
    private static final Color LINE       = new Color(226, 230, 235);
    private static final Color ROOM_GREEN = new Color(30, 140, 20);

    private static final String[] THAI_MONTHS = {"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม",
            "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};

    /** ข้อมูลตัวอย่างของตาราง "การจองล่าสุด" — ระบบจองยังไม่ได้ทำ จึงยังไม่มีข้อมูลจริง */
    private static final String[][] SAMPLE_BOOKINGS = {
            {"LBA-001", "ห้อง B102", "2 ตุลาคม 2569",  "18:00 - 20:00", "free"},
            {"LBA-002", "ห้อง A102", "23 ตุลาคม 2569", "16:00 - 18:00", "busy"},
            {"LBA-003", "ห้อง A101", "13 ตุลาคม 2569", "13:00 - 15:00", "busy"},
            {"LBA-004", "ห้อง B105", "21 ตุลาคม 2569", "10:00 - 12:00", "free"},
    };

    private final Member admin;
    private final int memberCount;
    private final List<Room> rooms;

    private JLabel lbDate, lbTime;
    private Timer clock;

    /**
     * @param admin       ผู้ดูแลที่ล็อกอินอยู่
     * @param memberCount จำนวนผู้ใช้ทั้งหมดใน Member.csv
     * @param rooms       รายการห้องทั้งหมด
     */
    public AdminHomeForm(Member admin, int memberCount, List<Room> rooms) {
        super("ผู้ดูแลระบบ");
        if (admin == null || rooms == null) {
            throw new IllegalArgumentException("admin and rooms must not be null");
        }
        this.admin = admin;
        this.memberCount = memberCount;
        this.rooms = List.copyOf(rooms); // defensive copy: ภายนอกแก้ list เดิมแล้วไม่กระทบหน้านี้
    }

    @Override
    protected void setComponent() {
        cp.setBackground(PAGE_BG);
        buildSidebar();
        buildTopBar();
        buildWelcomeCard();
        buildStatCards();
        buildCalendarCard();
        buildLatestBookings();
        buildRoomStatus();
        startClock();
    }

    // ------------------------------------------------------------ แถบเมนูซ้าย
    private void buildSidebar() {
        JPanel sidebar = place(cp, new JPanel(null), 0, 0, SIDEBAR_W, HEIGHT);
        sidebar.setBackground(Theme.NAVY);
        place(sidebar, new JLabel(Icons.of(Icons.Type.BOOK, 56, Theme.WHITE)), 16, 18, 56, 56);
        place(sidebar, Theme.label("Library Booking", Theme.bold(21), Theme.WHITE), 82, 16, 184, 32);
        place(sidebar, Theme.label("Admin", Theme.font(16), new Color(220, 226, 236)), 84, 48, 120, 22);
        SideMenuItem home = place(sidebar, new SideMenuItem("หน้าหลัก", Icons.Type.HOME, true), 14, 92, SIDEBAR_W - 28, 52);
        home.setFont(Theme.bold(20));
        home.setCursor(Cursor.getDefaultCursor()); // ยังกดไม่ได้
    }

    // ------------------------------------------------------------ แถบบน
    private void buildTopBar() {
        JPanel bar = place(cp, new JPanel(null), SIDEBAR_W, 0, WIDTH - SIDEBAR_W, 56);
        bar.setBackground(new Color(250, 251, 252));
        int x = bar.getWidth() - 296;
        place(bar, new JLabel(Icons.of(Icons.Type.BELL, 22, new Color(229, 57, 53))), x, 17, 22, 22);
        place(bar, new JLabel(Icons.of(Icons.Type.ACCOUNT, 36, Theme.TEXT)), x + 38, 10, 36, 36);
        place(bar, Theme.label(admin.getFullName(), Theme.bold(15), Theme.TEXT), x + 82, 8, 210, 22);
        place(bar, Theme.label("ผู้ดูแลระบบ", Theme.font(13), Theme.TEXT_MUTED), x + 82, 29, 210, 18);
    }

    // ------------------------------------------------------------ การ์ดต้อนรับ + วันเวลา
    private void buildWelcomeCard() {
        RoundPanel card = place(cp, new RoundPanel(Theme.WHITE, 10, null, 3), MAIN_X, 64, MAIN_W, 86);
        RoundPanel avatar = place(card, new RoundPanel(new Color(200, 204, 211), 8, null, 0), 14, 16, 52, 52);
        place(avatar, new JLabel(Icons.of(Icons.Type.USER, 40, Theme.WHITE)), 6, 6, 40, 40);

        place(card, Theme.label("ยินดีต้อนรับ, ผู้ดูแลระบบ", Theme.bold(18), Theme.TEXT), 78, 8, 350, 28);
        place(card, Theme.label("ระบบจัดการห้องสมุด มหาวิทยาลัยเกษตรศาสตร์", Theme.font(13), Theme.TEXT), 78, 38, 350, 18);
        place(card, Theme.label("วิทยาเขตกำแพงแสน", Theme.font(13), Theme.TEXT), 78, 56, 350, 18);

        JPanel divider = place(card, new JPanel(), 440, 12, 1, 60);
        divider.setBackground(LINE);
        lbDate = place(card, Theme.centeredLabel(" ", Theme.font(15), Theme.TEXT), 444, 18, 190, 22);
        lbTime = place(card, Theme.centeredLabel(" ", Theme.font(15), Theme.TEXT), 444, 46, 190, 22);
    }

    // ------------------------------------------------------------ การ์ดตัวเลข 4 ใบ
    private void buildStatCards() {
        int total = rooms.size();
        int free = (int) rooms.stream().filter(Room::isAvailable).count();
        RoundPanel box = place(cp, new RoundPanel(Theme.WHITE, 10, null, 3), MAIN_X, 158, MAIN_W, 122);
        statCard(box, 0, Icons.Type.DOOR_FILLED, new Color(21, 50, 200), "ห้องทั้งหมด", total + " ห้อง",
                new Color(238, 243, 251), new Color(196, 210, 236));
        statCard(box, 1, Icons.Type.DOOR_FILLED, new Color(20, 140, 20), "ห้องว่าง", free + " ห้อง",
                new Color(230, 248, 230), new Color(184, 230, 184));
        statCard(box, 2, Icons.Type.DOOR_LOCK, new Color(214, 40, 40), "ห้องไม่ว่าง", (total - free) + " ห้อง",
                new Color(253, 237, 238), new Color(240, 200, 204));
        statCard(box, 3, Icons.Type.GROUP_ADD, new Color(150, 60, 220), "ผู้ใช้ทั้งหมด", memberCount + " คน",
                new Color(243, 236, 252), new Color(214, 196, 240));
    }

    private void statCard(Container box, int index, Icons.Type icon, Color iconColor, String title, String value,
                          Color bg, Color border) {
        int w = 146;
        RoundPanel card = place(box, new RoundPanel(bg, 10, border, 0), 10 + index * (w + 11), 10, w, 100);
        place(card, new JLabel(Icons.of(icon, 36, iconColor), SwingConstants.CENTER), 0, 8, w, 36);
        place(card, Theme.centeredLabel(title, Theme.bold(15), Theme.TEXT), 0, 46, w, 22);
        place(card, Theme.centeredLabel(value, Theme.bold(22), Theme.TEXT), 0, 68, w, 28);
    }

    // ------------------------------------------------------------ ปฏิทินการจอง
    private void buildCalendarCard() {
        RoundPanel card = place(cp, new RoundPanel(Theme.WHITE, 10, null, 3), MAIN_X, 288, MAIN_W, 182);
        place(card, Theme.label("ปฏิทินการจอง", Theme.bold(17), Theme.TEXT), 16, 8, 200, 26);
        zoneSummary(card, "A", 42);
        zoneSummary(card, "B", 104);

        YearMonth month = YearMonth.now();
        place(card, new JLabel(Icons.of(Icons.Type.CALENDAR, 20, HEADER_TXT)), 304, 8, 20, 20);
        place(card, Theme.centeredLabel(thaiMonthYear(month), Theme.bold(13), Theme.TEXT), 300, 8, 326, 20);
        place(card, new MonthCalendar(month, LocalDate.now()), 300, 32, 326, 144);
    }

    /** สรุปห้องในโซน เช่น "ห้อง A101 - A105" ว่างทั้งโซนหรือไม่ */
    private void zoneSummary(Container card, String zone, int y) {
        List<Room> inZone = rooms.stream().filter(r -> r.inZone(zone)).collect(Collectors.toList());
        if (inZone.isEmpty()) return;
        String range = "ห้อง " + inZone.get(0).getCode() + " - " + inZone.get(inZone.size() - 1).getCode();
        boolean anyFree = inZone.stream().anyMatch(Room::isAvailable);
        place(card, Theme.label(range, Theme.bold(13), Theme.TEXT), 16, y, 170, 20);
        place(card, Theme.label(inZone.get(0).getCapacity() + " คน", Theme.font(11), Theme.TEXT_MUTED), 16, y + 20, 170, 16);
        place(card, Theme.label("(TV , โต๊ะ , เก้าอี้)", Theme.font(11), Theme.TEXT_MUTED), 16, y + 35, 170, 16);
        place(card, new StatusChip(anyFree, 16), 176, y + 10, 100, 28);
    }

    // ------------------------------------------------------------ ตารางการจองล่าสุด
    private void buildLatestBookings() {
        RoundPanel card = place(cp, new RoundPanel(Theme.WHITE, 10, null, 3), MAIN_X, 478, MAIN_W, 194);
        place(card, Theme.label("การจองล่าสุด", Theme.bold(17), Theme.TEXT), 16, 8, 200, 26);
        JLabel viewAll = place(card, Theme.label("ดูทั้งหมด  →", Theme.font(12), HEADER_TXT), 500, 12, 120, 18);
        viewAll.setHorizontalAlignment(SwingConstants.RIGHT);

        // คอลัมน์: {x, กว้าง, จัดกลางไหม}
        int[][] cols = {{18, 44, 0}, {64, 100, 0}, {170, 90, 1}, {262, 130, 1}, {394, 110, 1}, {506, 110, 1}};
        String[] headers = {"ลำดับ", "รหัสการจอง", "ห้อง", "วันที่จอง", "เวลา", "สถานะการจอง"};

        JPanel headerRow = place(card, new JPanel(null), 12, 38, MAIN_W - 27, 26);
        headerRow.setBackground(HEADER_BG);
        for (int c = 0; c < headers.length; c++) {
            JLabel h = Theme.label(headers[c], Theme.font(12), HEADER_TXT);
            if (cols[c][2] == 1) h.setHorizontalAlignment(SwingConstants.CENTER);
            place(headerRow, h, cols[c][0] - 12, 0, cols[c][1], 26);
        }

        int rowH = 31;
        for (int r = 0; r < SAMPLE_BOOKINGS.length; r++) {
            String[] b = SAMPLE_BOOKINGS[r];
            int y = 64 + r * rowH;
            String[] cells = {String.valueOf(r + 1), b[0], b[1], b[2], b[3]};
            for (int c = 0; c < cells.length; c++) {
                JLabel cell = Theme.label(cells[c], Theme.font(12), Theme.TEXT);
                if (cols[c][2] == 1) cell.setHorizontalAlignment(SwingConstants.CENTER);
                place(card, cell, cols[c][0], y, cols[c][1], rowH);
            }
            place(card, new StatusChip(b[4].equals("free"), 12), cols[5][0] + 12, y + 5, 86, 21);
            if (r < SAMPLE_BOOKINGS.length - 1) {
                JPanel line = place(card, new JPanel(), 12, y + rowH, MAIN_W - 27, 1);
                line.setBackground(LINE);
            }
        }
    }

    // ------------------------------------------------------------ สถานะห้อง (คอลัมน์ขวา)
    private void buildRoomStatus() {
        RoundPanel card = place(cp, new RoundPanel(Theme.WHITE, 10, null, 3), SIDE_X, 64, SIDE_W, 608);
        place(card, Theme.label("สถานะห้อง", Theme.bold(19), Theme.TEXT), 20, 10, 200, 28);
        int rowH = 55;
        for (int i = 0; i < rooms.size(); i++) {
            Room room = rooms.get(i);
            int y = 46 + i * rowH;
            JPanel line = place(card, new JPanel(), 14, y, SIDE_W - 31, 1);
            line.setBackground(LINE);
            place(card, new JLabel(Icons.of(Icons.Type.DOOR_OPEN, 30, ROOM_GREEN)), 18, y + 12, 30, 30);
            place(card, Theme.label("ห้อง " + room.getCode(), Theme.font(16), Theme.TEXT), 58, y + 7, 110, 24);
            place(card, Theme.label(room.getCapacity() + " คน", Theme.font(11), Theme.TEXT_MUTED), 58, y + 30, 110, 16);
            place(card, new StatusChip(room.isAvailable(), 12), SIDE_W - 98, y + 17, 68, 22);
        }
    }

    // ------------------------------------------------------------ นาฬิกา
    /** อัปเดตวันที่และเวลาทุก 1 วินาทีด้วย javax.swing.Timer (ทำงานบน Event Dispatch Thread จึงแก้ JLabel ได้) */
    private void startClock() {
        updateClock();
        clock = new Timer(1000, e -> updateClock());
        clock.start();
    }

    private void updateClock() {
        LocalDateTime now = LocalDateTime.now();
        lbDate.setText(thaiDate(now.toLocalDate()));
        lbTime.setText(String.format("%02d:%02d น.", now.getHour(), now.getMinute()));
    }

    @Override
    public void dispose() {
        if (clock != null) clock.stop(); // หยุด Timer ก่อนปิดหน้าต่าง ไม่ให้ทำงานค้างอยู่เบื้องหลัง
        super.dispose();
    }

    // ------------------------------------------------------------ วันที่แบบไทย
    /** เช่น 9 ตุลาคม 2569 (พ.ศ. = ค.ศ. + 543) */
    static String thaiDate(LocalDate d) {
        return d.getDayOfMonth() + " " + THAI_MONTHS[d.getMonthValue() - 1] + " " + (d.getYear() + 543);
    }

    static String thaiMonthYear(YearMonth m) {
        return THAI_MONTHS[m.getMonthValue() - 1] + " " + (m.getYear() + 543);
    }

    /** ปฏิทิน 1 เดือน วาดเองทั้งหมด: แถวชื่อวัน + ตารางวันที่ วงกลมวันนี้ */
    private static final class MonthCalendar extends JComponent {
        private static final String[] DAY_NAMES = {"อา", "จ", "อ", "พ", "พฤ", "ศ", "ส"};
        private final YearMonth month;
        private final LocalDate today;

        MonthCalendar(YearMonth month, LocalDate today) {
            this.month = month;
            this.today = today;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.smooth(g);
            int w = getWidth(), headerH = 20;
            double colW = w / 7.0;
            int firstCol = month.atDay(1).getDayOfWeek().getValue() % 7;   // อาทิตย์ = 0
            int days = month.lengthOfMonth();
            int rows = (firstCol + days + 6) / 7;
            double rowH = (getHeight() - headerH - 2) / (double) rows;

            g2.setColor(HEADER_BG);
            g2.fillRect(0, 0, w, headerH);
            g2.setFont(Theme.font(11));
            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(HEADER_TXT);
            for (int c = 0; c < 7; c++) {
                int tx = (int) (c * colW + (colW - fm.stringWidth(DAY_NAMES[c])) / 2);
                g2.drawString(DAY_NAMES[c], tx, 14);
            }

            g2.setColor(LINE);                               // เส้นแบ่งช่อง
            for (int r = 1; r <= rows; r++) {
                int y = (int) (headerH + r * rowH);
                g2.drawLine(0, y, w, y);
            }
            for (int c = 1; c < 7; c++) {
                int x = (int) (c * colW);
                g2.drawLine(x, headerH, x, (int) (headerH + rows * rowH));
            }

            g2.setFont(Theme.font(12));
            fm = g2.getFontMetrics();
            for (int day = 1; day <= days; day++) {
                int cell = firstCol + day - 1;
                double cx = (cell % 7) * colW + colW / 2;
                double cy = headerH + (cell / 7) * rowH + rowH / 2;
                String text = String.valueOf(day);
                boolean isToday = month.equals(YearMonth.from(today)) && today.getDayOfMonth() == day;
                if (isToday) {
                    double r = Math.min(rowH, colW) / 2 - 1;
                    g2.setColor(Theme.BLUE);
                    g2.fill(new java.awt.geom.Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r));
                }
                g2.setColor(isToday ? Theme.WHITE : Theme.TEXT);
                g2.drawString(text, (int) (cx - fm.stringWidth(text) / 2.0),
                        (int) (cy + fm.getAscent() / 2.0 - 2));
            }
            g2.dispose();
        }
    }
}
