import javax.swing.*;                                // JFrame, JPanel, JLabel, ImageIcon, JOptionPane
import java.awt.*;                                   // Container, Color, Font, Dimension, Cursor
import java.awt.event.*;                             // MouseListener, MouseEvent
import java.time.LocalDate;                          // ใช้ดึงวันที่วันนี้ (คลาสสำเร็จรูปของ JVM)
import java.util.ArrayList;                          // ArrayList

public class LibraryForm extends JFrame implements MouseListener {  // หน้าต่าง + รับ event เมาส์ (สไลด์ Section 11)

    private Container cp;                            // พื้นที่วาง component ของ JFrame (สไลด์ Section 8)
    private Student student;                         // ผู้ใช้ที่ล็อกอินอยู่ (ข้อมูลมาจากคลาส ไม่ฮาร์ดโค้ด)

    // ---------- สี ----------
    private Color NAVY  = new Color(31, 41, 64);     // พื้นเมนูซ้าย
    private Color BLUE  = new Color(42, 107, 200);   // เมนูที่ถูกเลือก
    private Color HOVER = new Color(50, 62, 90);     // เมนูตอนเมาส์ชี้
    private Color GRAY  = new Color(236, 236, 236);  // พื้นกล่องต้อนรับ

    // ---------- ข้อมูลเมนู ----------
    private String[] menuName = {"หน้าหลัก", "ดูห้องทั้งหมด", "จองห้อง", "การจองของฉัน", "ข้อมูลส่วนตัว"};
    private String[] menuIcon = {"home", "building", "homeplus", "calendar", "user"};
    private RoundPanel[] menu = new RoundPanel[5];   // อาเรย์เก็บปุ่มเมนู (สไลด์ Section 5: Array)
    private JPanel content;                         // พื้นที่ฝั่งขวา (x=300 ถึง 1440) ใช้สลับหน้า
    private JPanel[] page = new JPanel[5];           // หน้า 5 หน้า ตรงกับเมนู 5 ปุ่ม
    private int[] actGo = {2, 1, 3, 4};              // การ์ดทางลัดแต่ละใบ พาไปหน้าที่เท่าไร
    private int activeMenu = 0;

    // ---------- ตัวแปรของหน้าจองห้อง / การจองของฉัน ----------
    private ArrayList<Room> bookRooms;               // ห้องที่แสดงใน ComboBox
    private JComboBox<String> roomBox;               // เลือกห้อง
    private JTextField dateField;                    // กรอกวันที่
    private JComboBox<String> timeBox;               // เลือกช่วงเวลา
    private RoundPanel bookBtn;                      // ปุ่มยืนยันการจอง
    private ArrayList<RoundPanel> cancelBtn = new ArrayList<RoundPanel>();   // ปุ่มยกเลิกของแต่ละแถว
    private ArrayList<Booking> cancelTarget = new ArrayList<Booking>();      // การจองที่ตรงกับปุ่มยกเลิก                      // เมนูที่ถูกเลือก (0 = หน้าหลัก)

    // ---------- ข้อมูลการ์ดสถิติ ----------
    private String[] statIcon  = {"door", "calcheck", "list", "listplus"};
    private String[] statLabel = {"ห้องทั้งหมด", "ห้องว่าง", "การจองห้องของฉัน", "การจองวันนี้"};
    private String[] statValue = new String[4];      // คำนวณจากไฟล์ใน setStats()
    private Color[] statColor  = {new Color(214, 227, 238), new Color(230, 244, 228),
                                  new Color(243, 232, 246), new Color(253, 236, 236)};

    // ---------- ข้อมูลการ์ดทางลัด ----------
    private String[] actIcon  = {"homeplus", "building", "calendar", "user"};
    private String[] actTitle = {"จองห้อง", "ดูห้องทั้งหมด", "การจองของฉัน", "โปรไฟล์"};
    private String[] actDesc1 = {"เลือกวันที่ เวลา", "ตรวจสอบสถานะ", "ดูสถานะการจอง", "จัดการข้อมูลส่วนตัว"};
    private String[] actDesc2 = {"ห้องที่ต้องการจอง", "รายละเอียดการจอง", "", ""};
    private Color[] actColor  = {new Color(0, 144, 255), new Color(73, 195, 90),
                                 new Color(245, 181, 59), new Color(180, 85, 255)};
    private RoundPanel[] actCard = new RoundPanel[4];

    // ===== constructor: รับ Student เข้ามา แล้วแยกขั้นตอนตามสไลด์ (Initial -> setComponent -> Finally) =====
    public LibraryForm(Student student) {
        super("Library Booking");                    // ชื่อหน้าต่าง
        this.student = student;                      // เก็บผู้ใช้ไว้แสดงในหน้า
        Initial();
        setSidebar();
        setPages();
        setHeader();
        setStats();
        setActions();
        Finally();
    }

    // ===== เตรียม container =====
    public void Initial() {
        cp = getContentPane();
        cp.setLayout(null);                          // ไม่ใช้ Layout Manager
        cp.setPreferredSize(new Dimension(1440, 900)); // พื้นที่ใช้งาน 1440 x 900 (ใช้คู่กับ pack())
        cp.setBackground(Color.WHITE);
    }

    // ===== เมนูด้านซ้าย =====
    public void setSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(null);
        sidebar.setBackground(NAVY);
        sidebar.setBounds(0, 0, 300, 900);
        cp.add(sidebar);

        JLabel logo = new JLabel(new ImageIcon("./Pic/logo.png"));   // ImageIcon (สไลด์ Section 9)
        logo.setBounds(30, 53, 44, 44);
        sidebar.add(logo);

        JLabel title = new JLabel("Library Booking");
        title.setFont(new Font("Tahoma", Font.BOLD, 22));            // สไลด์ Section 9: setFont
        title.setForeground(Color.WHITE);
        title.setBounds(86, 55, 210, 40);
        sidebar.add(title);

        for (int i = 0; i < 5; i++) {                // วนสร้างปุ่มเมนู 5 ปุ่ม
            menu[i] = new RoundPanel(16, NAVY, null);
            menu[i].setBounds(19, 154 + i * 92, 242, 68);

            JLabel ic = new JLabel(new ImageIcon("./Pic/menu_" + menuIcon[i] + ".png")); // ต่อสตริง (สไลด์ Section 2)
            ic.setBounds(17, 15, 38, 38);
            menu[i].add(ic);

            JLabel tx = new JLabel(menuName[i]);
            tx.setFont(new Font("Tahoma", Font.PLAIN, 19));
            tx.setForeground(Color.WHITE);
            tx.setBounds(78, 16, 160, 36);
            menu[i].add(tx);

            menu[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            menu[i].addMouseListener(this);          // ส่ง event เมาส์มาที่เมธอดของคลาสนี้
            sidebar.add(menu[i]);
        }
        menu[0].setBackground(BLUE);                 // เริ่มต้นเลือก "หน้าหลัก"
    }

    // ===== สร้างพื้นที่เนื้อหาด้านขวา และหน้าทั้ง 5 หน้า =====
    public void setPages() {
        content = new JPanel(null);
        content.setBackground(Color.WHITE);
        content.setBounds(300, 0, 1140, 900);
        cp.add(content);

        for (int i = 0; i < 5; i++) {
            page[i] = new JPanel(null);
            page[i].setBackground(Color.WHITE);
            page[i].setBounds(0, 0, 1140, 900);
            page[i].setVisible(i == 0);              // เริ่มต้นแสดงเฉพาะหน้าหลัก
            content.add(page[i]);
        }
    }

    // ===== สลับหน้า + ไฮไลต์เมนูที่เลือก =====
    public void showPage(int index) {
        activeMenu = index;
        page[index].removeAll();                     // สร้างหน้าใหม่ทุกครั้ง เพื่อให้ข้อมูลล่าสุดจากไฟล์
        if (index == 0) { setHeader(); setStats(); setActions(); }
        else if (index == 1) setRoomsPage();
        else if (index == 2) setBookPage();
        else if (index == 3) setMyBookingsPage();
        else if (index == 4) setProfilePage();
        for (int j = 0; j < 5; j++) {
            page[j].setVisible(j == index);
            menu[j].setBackground((j == index) ? BLUE : NAVY);
        }
        page[index].revalidate();
        page[index].repaint();
    }

    // ===== ตัวช่วยสร้างหัวข้อหน้า =====
    private JLabel makeTitle(String text) {
        JLabel t = new JLabel(text);
        t.setFont(new Font("Tahoma", Font.BOLD, 30));
        t.setBounds(37, 55, 800, 50);
        return t;
    }

    private JLabel makeLabel(String text, int size, int style, int x, int y, int w, int h) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Tahoma", style, size));
        l.setBounds(x, y, w, h);
        return l;
    }

    // ===== กล่องต้อนรับ + หัวข้อ =====
    public void setHeader() {
        RoundPanel header = new RoundPanel(20, GRAY, null);
        header.setBounds(9, 55, 1107, 182);
        page[0].add(header);

        JLabel avatar = new JLabel(new ImageIcon("./Pic/avatar.png"));
        avatar.setBounds(47, 48, 88, 88);
        header.add(avatar);

        JLabel name = new JLabel("สวัสดี " + student.getName());      // ชื่อมาจาก object Student (getter)
        name.setFont(new Font("Tahoma", Font.BOLD, 30));
        name.setBounds(161, 46, 800, 50);
        header.add(name);

        JLabel sid = new JLabel("รหัสนักศึกษา  " + student.getStudentId()); // รหัสมาจาก object Student
        sid.setFont(new Font("Tahoma", Font.PLAIN, 19));
        sid.setBounds(161, 102, 600, 30);
        header.add(sid);

        JLabel section = new JLabel("ภาพรวมการใช้งาน");
        section.setFont(new Font("Tahoma", Font.BOLD, 30));
        section.setBounds(37, 251, 500, 44);
        page[0].add(section);
    }

    // ===== การ์ดสถิติ 4 ใบ (ตัวเลขคำนวณจากไฟล์) =====
    public void setStats() {
        ArrayList<Room> rooms = DataManager.loadRooms();             // อ่านห้องทั้งหมดจาก rooms.txt
        ArrayList<Booking> bookings = DataManager.loadBookings();    // อ่านการจองทั้งหมดจาก bookings.txt
        String today = LocalDate.now().toString();                   // วันนี้ รูปแบบ yyyy-MM-dd
        int myCount = 0;                                             // จำนวนการจองของผู้ใช้
        int todayCount = 0;                                          // จำนวนการจองที่เป็นวันนี้
        int freeCount = 0;                                           // จำนวนห้องที่วันนี้ยังว่าง

        for (Booking b : bookings) {                                 // For-each loop (สไลด์ Section 5)
            if (b.getStudentId().equals(student.getStudentId()))     // เทียบสตริงด้วย equals
                myCount++;
            if (b.getDate().equals(today))
                todayCount++;
        }
        for (Room r : rooms) {                                       // เช็คทีละห้องว่าวันนี้ถูกจองหรือยัง
            boolean booked = false;
            for (Booking b : bookings) {
                if (b.getRoomNo().equals(r.getRoomNo()) && b.getDate().equals(today))
                    booked = true;
            }
            if (!booked)
                freeCount++;
        }

        statValue[0] = rooms.size() + " ห้อง";                        // ArrayList.size() (สไลด์ Section 5)
        statValue[1] = freeCount + " ห้อง";                           // ตัวเลข + สตริง (สไลด์ Section 2)
        statValue[2] = myCount + " ห้อง";
        statValue[3] = todayCount + " รายการ";

        for (int i = 0; i < 4; i++) {
            RoundPanel card = new RoundPanel(24, statColor[i], null);
            card.setBounds(42 + i * 285, 302, 225, 216);
            page[0].add(card);

            JLabel ic = new JLabel(new ImageIcon("./Pic/stat_" + statIcon[i] + ".png"));
            ic.setBounds(28, 66, 40, 40);
            card.add(ic);

            JLabel lb = new JLabel(statLabel[i]);
            lb.setFont(new Font("Tahoma", Font.PLAIN, 17));
            lb.setBounds(28, 127, 190, 28);
            card.add(lb);

            JLabel val = new JLabel(statValue[i]);
            val.setFont(new Font("Tahoma", Font.BOLD, 34));
            val.setBounds(28, 156, 190, 48);
            card.add(val);
        }
    }

    // ===== การ์ดทางลัด 4 ใบ =====
    public void setActions() {
        for (int i = 0; i < 4; i++) {
            actCard[i] = new RoundPanel(24, Color.WHITE, new Color(190, 190, 190));
            actCard[i].setBounds(42 + i * 285, 559, 225, 297);
            actCard[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            actCard[i].addMouseListener(this);
            page[0].add(actCard[i]);

            RoundPanel box = new RoundPanel(20, actColor[i], null);
            box.setBounds(14, 12, 198, 156);
            actCard[i].add(box);

            JLabel ic = new JLabel(new ImageIcon("./Pic/act_" + actIcon[i] + ".png"));
            ic.setBounds(77, 40, 44, 44);
            box.add(ic);

            JLabel t = new JLabel(actTitle[i]);
            t.setFont(new Font("Tahoma", Font.BOLD, 24));
            t.setForeground(Color.WHITE);
            t.setHorizontalAlignment(JLabel.CENTER);                 // สไลด์ Section 9: setHorizontalAlignment
            t.setBounds(0, 101, 198, 40);
            box.add(t);

            JLabel d1 = new JLabel(actDesc1[i]);
            d1.setFont(new Font("Tahoma", Font.PLAIN, 15));
            d1.setHorizontalAlignment(JLabel.CENTER);
            d1.setBounds(0, 186, 225, 24);
            actCard[i].add(d1);

            JLabel d2 = new JLabel(actDesc2[i]);
            d2.setFont(new Font("Tahoma", Font.PLAIN, 15));
            d2.setHorizontalAlignment(JLabel.CENTER);
            d2.setBounds(0, 212, 225, 24);
            actCard[i].add(d2);
        }
    }

    // ===== หน้า 1: ดูห้องทั้งหมด =====
    public void setRoomsPage() {
        page[1].add(makeTitle("ดูห้องทั้งหมด"));
        ArrayList<Room> rooms = DataManager.loadRooms();
        ArrayList<Booking> bookings = DataManager.loadBookings();
        String today = LocalDate.now().toString();
        page[1].add(makeLabel("สถานะห้องของวันนี้ " + today, 19, Font.PLAIN, 37, 105, 600, 30));

        for (int i = 0; i < rooms.size(); i++) {
            Room r = rooms.get(i);
            boolean booked = false;
            for (Booking b : bookings)
                if (b.getRoomNo().equals(r.getRoomNo()) && b.getDate().equals(today))
                    booked = true;

            RoundPanel card = new RoundPanel(24, booked ? new Color(253, 236, 236) : new Color(230, 244, 228), null);
            card.setBounds(37 + (i % 5) * 213, 160 + (i / 5) * 230, 190, 200);
            page[1].add(card);

            JLabel no = makeLabel(r.getRoomNo(), 34, Font.BOLD, 0, 30, 190, 50);
            no.setHorizontalAlignment(JLabel.CENTER);
            card.add(no);
            JLabel cap = makeLabel("ที่นั่ง " + r.getCapacity() + " คน", 19, Font.PLAIN, 0, 90, 190, 30);
            cap.setHorizontalAlignment(JLabel.CENTER);
            card.add(cap);
            JLabel st = makeLabel(booked ? "ถูกจองแล้ว" : "ว่าง", 22, Font.BOLD, 0, 135, 190, 36);
            st.setHorizontalAlignment(JLabel.CENTER);
            st.setForeground(booked ? new Color(200, 50, 50) : new Color(40, 150, 60));
            card.add(st);
        }
    }

    // ===== หน้า 2: จองห้อง =====
    public void setBookPage() {
        page[2].add(makeTitle("จองห้อง"));
        bookRooms = DataManager.loadRooms();

        RoundPanel form = new RoundPanel(24, GRAY, null);
        form.setBounds(37, 130, 700, 440);
        page[2].add(form);

        form.add(makeLabel("ห้อง", 21, Font.PLAIN, 40, 50, 150, 40));
        String[] roomItems = new String[bookRooms.size()];
        for (int i = 0; i < bookRooms.size(); i++)
            roomItems[i] = bookRooms.get(i).getRoomNo() + "  (" + bookRooms.get(i).getCapacity() + " ที่นั่ง)";
        roomBox = new JComboBox<String>(roomItems);
        roomBox.setFont(new Font("Tahoma", Font.PLAIN, 19));
        roomBox.setBounds(240, 50, 400, 42);
        form.add(roomBox);

        form.add(makeLabel("วันที่ (yyyy-MM-dd)", 21, Font.PLAIN, 40, 130, 200, 40));
        dateField = new JTextField(LocalDate.now().toString());
        dateField.setFont(new Font("Tahoma", Font.PLAIN, 19));
        dateField.setBounds(240, 130, 400, 42);
        form.add(dateField);

        form.add(makeLabel("ช่วงเวลา", 21, Font.PLAIN, 40, 210, 150, 40));
        String[] times = {"08:00-10:00", "10:00-12:00", "13:00-15:00", "15:00-17:00"};
        timeBox = new JComboBox<String>(times);
        timeBox.setFont(new Font("Tahoma", Font.PLAIN, 19));
        timeBox.setBounds(240, 210, 400, 42);
        form.add(timeBox);

        bookBtn = new RoundPanel(16, BLUE, null);
        bookBtn.setBounds(240, 300, 400, 60);
        bookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        bookBtn.addMouseListener(this);
        JLabel bt = makeLabel("ยืนยันการจอง", 22, Font.BOLD, 0, 10, 400, 40);
        bt.setHorizontalAlignment(JLabel.CENTER);
        bt.setForeground(Color.WHITE);
        bookBtn.add(bt);
        form.add(bookBtn);
    }

    // ตรวจข้อมูลแล้วบันทึกการจองลงไฟล์
    public void doBooking() {
        if (bookRooms.size() == 0) {
            JOptionPane.showMessageDialog(this, "ไม่พบข้อมูลห้อง");
            return;
        }
        String roomNo = bookRooms.get(roomBox.getSelectedIndex()).getRoomNo();
        String time = (String) timeBox.getSelectedItem();
        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());      // แปลงข้อความเป็นวันที่
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "รูปแบบวันที่ไม่ถูกต้อง ต้องเป็น yyyy-MM-dd เช่น 2026-10-15");
            return;
        }
        if (date.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "ไม่สามารถจองย้อนหลังได้");
            return;
        }
        ArrayList<Booking> all = DataManager.loadBookings();
        for (Booking b : all) {
            if (b.getRoomNo().equals(roomNo) && b.getDate().equals(date.toString()) && b.getTime().equals(time)) {
                JOptionPane.showMessageDialog(this, "ห้อง " + roomNo + " ช่วงเวลานี้ถูกจองแล้ว");
                return;
            }
        }
        all.add(new Booking(student.getStudentId(), roomNo, date.toString(), time));
        DataManager.saveBookings(all);
        JOptionPane.showMessageDialog(this, "จองห้อง " + roomNo + " วันที่ " + date + " เวลา " + time + " สำเร็จ");
        showPage(3);                                                 // ไปหน้าการจองของฉัน
    }

    // ===== หน้า 3: การจองของฉัน =====
    public void setMyBookingsPage() {
        page[3].add(makeTitle("การจองของฉัน"));
        cancelBtn.clear();
        cancelTarget.clear();

        ArrayList<Booking> mine = new ArrayList<Booking>();
        for (Booking b : DataManager.loadBookings())
            if (b.getStudentId().equals(student.getStudentId()))
                mine.add(b);
        page[3].add(makeLabel("ทั้งหมด " + mine.size() + " รายการ", 19, Font.PLAIN, 37, 105, 600, 30));

        JPanel list = new JPanel(null);
        list.setBackground(Color.WHITE);
        list.setPreferredSize(new Dimension(1000, Math.max(mine.size() * 90, 10)));
        for (int i = 0; i < mine.size(); i++) {
            Booking b = mine.get(i);
            RoundPanel row = new RoundPanel(20, GRAY, null);
            row.setBounds(0, i * 90, 1000, 76);
            list.add(row);
            row.add(makeLabel("ห้อง " + b.getRoomNo(), 22, Font.BOLD, 30, 18, 200, 40));
            row.add(makeLabel("วันที่ " + b.getDate(), 20, Font.PLAIN, 260, 18, 250, 40));
            row.add(makeLabel("เวลา " + b.getTime(), 20, Font.PLAIN, 530, 18, 250, 40));

            RoundPanel cb = new RoundPanel(14, new Color(214, 69, 65), null);
            cb.setBounds(830, 16, 140, 44);
            cb.setCursor(new Cursor(Cursor.HAND_CURSOR));
            cb.addMouseListener(this);
            JLabel ct = makeLabel("ยกเลิก", 19, Font.BOLD, 0, 4, 140, 36);
            ct.setHorizontalAlignment(JLabel.CENTER);
            ct.setForeground(Color.WHITE);
            cb.add(ct);
            row.add(cb);
            cancelBtn.add(cb);
            cancelTarget.add(b);
        }
        if (mine.size() == 0)
            page[3].add(makeLabel("ยังไม่มีการจอง", 22, Font.PLAIN, 37, 150, 400, 40));

        JScrollPane sp = new JScrollPane(list);
        sp.setBorder(null);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBounds(37, 150, 1050, 700);
        page[3].add(sp);
    }

    // ลบการจอง 1 รายการออกจากไฟล์
    public void doCancel(Booking target) {
        int ok = JOptionPane.showConfirmDialog(this, "ยกเลิกการจองห้อง " + target.getRoomNo()
                + " วันที่ " + target.getDate() + " เวลา " + target.getTime() + " ?", "ยืนยัน", JOptionPane.YES_NO_OPTION);
        if (ok != JOptionPane.YES_OPTION)
            return;
        ArrayList<Booking> all = DataManager.loadBookings();
        for (int i = 0; i < all.size(); i++) {
            Booking b = all.get(i);
            if (b.getStudentId().equals(target.getStudentId()) && b.getRoomNo().equals(target.getRoomNo())
                    && b.getDate().equals(target.getDate()) && b.getTime().equals(target.getTime())) {
                all.remove(i);                                       // ลบรายการแรกที่ตรง
                break;
            }
        }
        DataManager.saveBookings(all);
        showPage(3);                                                 // โหลดหน้าใหม่
    }

    // ===== หน้า 4: ข้อมูลส่วนตัว =====
    public void setProfilePage() {
        page[4].add(makeTitle("ข้อมูลส่วนตัว"));
        int count = 0;
        for (Booking b : DataManager.loadBookings())
            if (b.getStudentId().equals(student.getStudentId()))
                count++;

        RoundPanel card = new RoundPanel(24, GRAY, null);
        card.setBounds(37, 130, 700, 340);
        page[4].add(card);

        JLabel avatar = new JLabel(new ImageIcon("./Pic/avatar.png"));
        avatar.setBounds(47, 48, 88, 88);
        card.add(avatar);
        card.add(makeLabel(student.getName(), 30, Font.BOLD, 161, 46, 500, 50));
        card.add(makeLabel("นักศึกษา", 19, Font.PLAIN, 161, 102, 300, 30));
        card.add(makeLabel("รหัสนักศึกษา", 21, Font.PLAIN, 47, 180, 200, 36));
        card.add(makeLabel(student.getStudentId(), 21, Font.BOLD, 300, 180, 350, 36));
        card.add(makeLabel("จำนวนการจองของฉัน", 21, Font.PLAIN, 47, 235, 250, 36));
        card.add(makeLabel(count + " รายการ", 21, Font.BOLD, 300, 235, 350, 36));
    }

    // ===== ตั้งค่าหน้าต่างและแสดงผล =====
    public void Finally() {
        pack();                                      // ปรับหน้าต่างให้พอดี preferredSize = 1440 x 900
        setLocationRelativeTo(null);                 // กลางจอ
        setResizable(false);                         // ห้ามปรับขนาด
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // ===== Event เมาส์: ต้อง Override ครบ 5 เมธอดของ MouseListener =====
    public void mouseClicked(MouseEvent e) {
        for (int i = 0; i < 5; i++) {
            if (e.getSource() == menu[i])
                showPage(i);                         // สลับไปหน้าที่กด
        }
        for (int i = 0; i < 4; i++) {
            if (e.getSource() == actCard[i])
                showPage(actGo[i]);
        }
        if (e.getSource() == bookBtn)
            doBooking();
        for (int i = 0; i < cancelBtn.size(); i++) {
            if (e.getSource() == cancelBtn.get(i)) {
                Booking target = cancelTarget.get(i);                // เก็บไว้ก่อน เพราะ doCancel จะสร้างหน้าใหม่
                doCancel(target);
                break;
            }
        }
    }
    public void mouseEntered(MouseEvent e) {
        for (int i = 0; i < 5; i++)
            if (e.getSource() == menu[i] && i != activeMenu)
                menu[i].setBackground(HOVER);
    }
    public void mouseExited(MouseEvent e) {
        for (int i = 0; i < 5; i++)
            if (e.getSource() == menu[i] && i != activeMenu)
                menu[i].setBackground(NAVY);
    }
    public void mousePressed(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
}