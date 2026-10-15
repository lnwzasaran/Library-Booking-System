package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;            // Color, Font
import java.awt.event.*;      // ActionListener, MouseListener (บทที่ 10-11)
import javax.swing.*;         // JPanel, JLabel
import java.util.ArrayList;   // ลิสต์การจองที่อ่านจาก bookings.csv

/*
 * หน้า "การจองล่าสุด" ของผู้ดูแลระบบ (รายการจองทั้งหมด) — เต็มหน้าต่าง 1440 x 860
 * มาจากการกด "ดูทั้งหมด" ในหน้า Admin — ข้อมูลทั้งหมดอ่านจาก bookings.csv (AppFrame เรียก refresh() ทุกครั้งที่เปิดหน้า)
 * ส่วนประกอบ:
 *   - แถบตัวกรอง: ทั้งหมด / จองสำเร็จ (BOOKED) / ยกเลิกแล้ว (CANCELLED) พร้อมจำนวนจากไฟล์ — คลิกเพื่อกรอง (MouseListener)
 *   - ตาราง: เรียงจากการจองล่าสุด (บรรทัดท้ายไฟล์) หน้าละ 5 รายการ
 *   - ปุ่มเปลี่ยนหน้า: < เลขหน้า > (ActionListener)
 */
public class AdminBookingsPage extends JPanel implements ActionListener, MouseListener {   // สืบทอด JPanel + ฟังปุ่มและการคลิกตัวกรองเอง

    // ตำแหน่งกึ่งกลางของแต่ละคอลัมน์ (วัดจากขอบซ้ายของตาราง)
    // ลำดับ, รหัสการจอง, ห้องที่จอง, วันที่จอง, เวลา, สถานะ
    private static final int[] CENTERS = {28, 112, 238, 356, 474, 588};

    private static final int TABLE_X = 572;      // ตำแหน่งซ้ายของตาราง (อยู่กลางพื้นที่เนื้อหา)
    private static final int TABLE_W = 648;      // ความกว้างของตาราง
    private static final int ROW_H = 80;         // ความสูงแถวข้อมูล
    private static final int PER_PAGE = 5;       // จำนวนรายการต่อหน้า
    private static final int MAX_PAGE_BUTTONS = 5;   // แสดงปุ่มเลขหน้าไม่เกิน 5 ปุ่ม

    // ตัวกรอง: 0 = ทั้งหมด, 1 = จองสำเร็จ, 2 = ยกเลิกแล้ว
    private static final int FILTER_ALL = 0;
    private static final int FILTER_BOOKED = 1;
    private static final int FILTER_CANCELLED = 2;

    private AdminSidebar sidebar;                // แถบเมนูซ้าย (ใช้คลาสเดียวกับหน้า Admin)
    private RoundPanel allFilter;                // ตัวกรอง "ทั้งหมด"
    private RoundPanel freeFilter;               // ตัวกรอง "จองสำเร็จ" (ชื่อตัวแปรเดิม)
    private RoundPanel busyFilter;               // ตัวกรอง "ยกเลิกแล้ว" (ชื่อตัวแปรเดิม)
    private JLabel allText;                      // ข้อความในตัวกรอง "ทั้งหมด"
    private JLabel bookedText;                   // ข้อความในตัวกรอง "จองสำเร็จ"
    private JLabel cancelledText;                // ข้อความในตัวกรอง "ยกเลิกแล้ว"
    private JPanel rowsPanel;                    // กล่องใส่แถวข้อมูล (ล้างแล้วสร้างใหม่ทุกครั้ง)
    private JPanel pagePanel;                    // กล่องใส่ปุ่มเลขหน้า (ล้างแล้วสร้างใหม่ทุกครั้ง)
    private RoundButton prevButton;              // ปุ่ม < (หน้าก่อน)
    private RoundButton[] pageButtons = new RoundButton[0];   // ปุ่มเลขหน้า (Array สร้างใหม่ตามจำนวนหน้า)
    private RoundButton nextButton;              // ปุ่ม > (หน้าถัดไป)

    private ArrayList<Booking> bookings = new ArrayList<>();   // การจองทั้งหมดจากไฟล์ (เรียงล่าสุดก่อน)
    private int filter = FILTER_ALL;             // ตัวกรองที่เลือกอยู่
    private int page = 1;                        // หน้าที่แสดงอยู่ (เริ่มที่ 1)

    public AdminBookingsPage() {                 // คอนสตรักเตอร์
        setLayout(null);                         // วางของด้วย setBounds
        setBackground(Theme.WHITE);              // พื้นสีขาว

        // ================= แถบเมนูซ้าย =================
        sidebar = new AdminSidebar();            // สร้าง sidebar ของ Admin
        sidebar.setBounds(0, 0, 352, 860);       // ชิดซ้าย สูงเต็มหน้าต่าง
        add(sidebar);                            // ใส่ลงในหน้า

        // ================= แถบด้านบน (ว่างตามดีไซน์) =================
        JPanel headerLine = new JPanel();        // เส้นใต้แถบบน
        headerLine.setBackground(Theme.BORDER);  // สีเส้นอ่อน
        headerLine.setBounds(352, 78, 1088, 1);  // สูง 1 พิกเซล ที่ y = 78
        add(headerLine);                         // ใส่ลงในหน้า

        JLabel title = new JLabel("การจองล่าสุด");    // หัวข้อหน้า
        title.setFont(Theme.font(Font.PLAIN, 24));   // ตัวปกติ 24
        title.setForeground(Theme.TEXT);             // สีเข้ม
        title.setBounds(390, 92, 400, 36);           // ใต้แถบบน
        add(title);                                  // ใส่ลงในหน้า

        // ================= แถบตัวกรอง =================
        RoundPanel filterBar = new RoundPanel(new Color(0xE9F0F8), new Color(0xD5DEE9));   // กล่องฟ้าอ่อน
        filterBar.setBounds(396, 150, 1002, 80);                                               // กว้างเกือบเต็มพื้นที่เนื้อหา
        add(filterBar);                                                                        // ใส่ลงในหน้า

        allFilter = new RoundPanel(Theme.SECONDARY, null);    // กล่องตัวกรอง "ทั้งหมด"
        allFilter.setBounds(12, 20, 140, 40);                    // ซ้ายสุดของแถบ
        allText = createFilterText(allFilter, 140);              // ข้อความในกล่อง
        filterBar.add(allFilter);                                // ใส่ลงในแถบ

        freeFilter = new RoundPanel(new Color(0xB9F5B0), null);   // กล่องตัวกรอง "จองสำเร็จ"
        freeFilter.setBounds(164, 20, 150, 40);                      // ถัดจาก "ทั้งหมด"
        bookedText = createFilterText(freeFilter, 150);              // ข้อความในกล่อง
        filterBar.add(freeFilter);                                   // ใส่ลงในแถบ

        busyFilter = new RoundPanel(new Color(0xF5A8AE), null);   // กล่องตัวกรอง "ยกเลิกแล้ว"
        busyFilter.setBounds(326, 20, 150, 40);                      // ถัดจาก "จองสำเร็จ"
        cancelledText = createFilterText(busyFilter, 150);           // ข้อความในกล่อง
        filterBar.add(busyFilter);                                   // ใส่ลงในแถบ

        allFilter.addMouseListener(this);        // คลิกตัวกรอง → mouseClicked
        freeFilter.addMouseListener(this);       // คลิกตัวกรอง → mouseClicked
        busyFilter.addMouseListener(this);       // คลิกตัวกรอง → mouseClicked

        // ================= ตาราง =================
        RoundPanel head = new RoundPanel(new Color(0xE5E7EB), Theme.CARD_OUTLINE);   // แถวหัวตาราง สีเทาอ่อน
        head.setBounds(TABLE_X, 258, TABLE_W, 40);                                      // สูง 40
        add(head);                                                                      // ใส่ลงในหน้า

        String[] columns = {"ลำดับ", "รหัสการจอง", "ห้องที่จอง", "วันที่จอง", "เวลา", "สถานะ"};   // ชื่อคอลัมน์ 6 ช่อง
        for (int i = 0; i < columns.length; i++) {                                            // วนใส่ชื่อคอลัมน์
            addCell(head, columns[i], CENTERS[i], 40, Font.PLAIN);                            // ข้อความกลางคอลัมน์
        }                                                                                     // จบ for

        rowsPanel = new JPanel();                                     // กล่องใส่แถวข้อมูล
        rowsPanel.setLayout(null);                                    // วางของด้วย setBounds
        rowsPanel.setBackground(Theme.WHITE);                         // พื้นขาว
        rowsPanel.setBounds(TABLE_X, 298, TABLE_W, ROW_H * PER_PAGE); // ใต้หัวตาราง สูงพอ 5 แถว
        add(rowsPanel);                                               // ใส่ลงในหน้า

        // ================= ปุ่มเปลี่ยนหน้า =================
        prevButton = createPageButton("", false);                                       // ปุ่ม < (ไม่มีข้อความ ใช้ไอคอน)
        prevButton.setIconPanel(new IconPanel(IconPanel.CHEVRON_LEFT, 18, Theme.TEXT)); // ไอคอน <
        prevButton.addActionListener(this);                                             // กด → actionPerformed
        add(prevButton);                                                                // ใส่ลงในหน้า

        pagePanel = new JPanel();                       // กล่องใส่ปุ่มเลขหน้า
        pagePanel.setLayout(null);                      // วางของด้วย setBounds
        pagePanel.setBackground(Theme.WHITE);           // พื้นขาว
        add(pagePanel);                                 // ใส่ลงในหน้า

        nextButton = createPageButton("", false);                                         // ปุ่ม >
        nextButton.setIconPanel(new IconPanel(IconPanel.CHEVRON_RIGHT, 18, Theme.TEXT));  // ไอคอน >
        nextButton.addActionListener(this);                                               // กด → actionPerformed
        add(nextButton);                                                                  // ใส่ลงในหน้า

        refresh();                                      // ใส่ข้อมูลครั้งแรก
    }                                                   // จบคอนสตรักเตอร์

    // อ่าน bookings.csv ใหม่ แล้วกลับไปหน้า 1 ของตัวกรอง "ทั้งหมด" (AppFrame เรียกทุกครั้งที่เปิดหน้า)
    public void refresh() {
        bookings = BookingService.newestFirst(BookingService.loadBookings());   // อ่านไฟล์ แล้วเรียงการจองล่าสุดขึ้นก่อน
        filter = FILTER_ALL;                                      // กลับไปตัวกรอง "ทั้งหมด"
        page = 1;                                                 // กลับไปหน้า 1
        showTable();                                              // สร้างตาราง
    }                                                             // จบ refresh

    // ลิสต์การจองตามตัวกรองที่เลือกอยู่ (logic กรองให้ตามสถานะ)
    private ArrayList<Booking> filtered() {
        if (filter == FILTER_BOOKED) {                                       // ตัวกรอง "จองสำเร็จ"
            return BookingService.filterByStatus(bookings, "BOOKED");       // เฉพาะ BOOKED
        } else if (filter == FILTER_CANCELLED) {                             // ตัวกรอง "ยกเลิกแล้ว"
            return BookingService.filterByStatus(bookings, "CANCELLED");    // เฉพาะ CANCELLED
        }                                                                    // จบ if
        return bookings;                                                     // ตัวกรอง "ทั้งหมด"
    }                                                                        // จบ filtered

    // สร้างตัวกรอง (ตัวเลข + สี) ตาราง และปุ่มเลขหน้า ใหม่ทั้งหมด
    private void showTable() {
        // ---------- ตัวเลขบนตัวกรอง ----------
        int booked = BookingService.countActive(bookings);        // จำนวน BOOKED (logic นับให้)
        int cancelled = bookings.size() - booked;                 // ที่เหลือคือยกเลิกแล้ว
        allText.setText("ทั้งหมด " + bookings.size());             // ข้อความ + จำนวน
        bookedText.setText("จองสำเร็จ " + booked);                 // ข้อความ + จำนวน
        cancelledText.setText("ยกเลิกแล้ว " + cancelled);          // ข้อความ + จำนวน

        // ---------- สีตัวกรอง: อันที่เลือก = น้ำเงิน ตัวขาว ----------
        setFilterColor(allFilter, allText, filter == FILTER_ALL, new Color(0xD6E4F5), Theme.SECONDARY);
        setFilterColor(freeFilter, bookedText, filter == FILTER_BOOKED, new Color(0xB9F5B0), new Color(0x15803D));
        setFilterColor(busyFilter, cancelledText, filter == FILTER_CANCELLED, new Color(0xF5A8AE), new Color(0xE11D2A));

        // ---------- แถวข้อมูลของหน้านี้ ----------
        ArrayList<Booking> list = filtered();                     // การจองตามตัวกรอง
        int totalPages = (list.size() + PER_PAGE - 1) / PER_PAGE; // จำนวนหน้า (ปัดขึ้น เช่น 6 รายการ = 2 หน้า)
        if (totalPages < 1) {                                     // ไม่มีข้อมูลเลย
            totalPages = 1;                                       // ยังถือว่ามี 1 หน้า (ว่าง)
        }                                                         // จบ if
        if (page > totalPages) {                                  // หน้าเกินจำนวนที่มี
            page = totalPages;                                    // ไปหน้าสุดท้ายแทน
        }                                                         // จบ if

        rowsPanel.removeAll();                                    // ลบแถวเดิมทั้งหมด
        int first = (page - 1) * PER_PAGE;                        // ตำแหน่งรายการแรกของหน้านี้ (หน้า 2 → 5)
        for (int i = first; i < list.size() && i < first + PER_PAGE; i++) {   // วนเฉพาะรายการของหน้านี้
            Booking b = list.get(i);                              // การจองที่ i
            RoundPanel row = new RoundPanel(Theme.WHITE, Theme.BORDER);    // กล่องแถวพื้นขาว
            row.setBounds(0, (i - first) * ROW_H, TABLE_W, ROW_H);            // แถวต่อกันลงมา
            rowsPanel.add(row);                                               // ใส่ลงในกล่องแถว

            String time = BookingService.timeRange(b);                         // "13:00 - 15:00"
            String[] data = {"" + (i + 1), b.getId(), "ห้อง " + b.getRoomId(),
                             BookingService.thaiDate(b.getDate()), time};    // ข้อความ 5 คอลัมน์แรก (ลำดับนับต่อเนื่องข้ามหน้า)
            for (int c = 0; c < data.length; c++) {                          // วนใส่ข้อมูลทีละคอลัมน์
                addCell(row, data[c], CENTERS[c], ROW_H, Font.BOLD);         // ข้อความกลางคอลัมน์
            }                                                                // จบ for

            StatusBadge status;                                              // ป้ายสถานะของแถวนี้
            if (b.isActive()) {                                              // ยังไม่ยกเลิก
                status = new StatusBadge("จองสำเร็จ", new Color(0xBBF7B5), new Color(0x15803D), 86, 34);   // ป้ายเขียว
            } else {                                                         // ยกเลิกแล้ว
                status = new StatusBadge("ยกเลิกแล้ว", new Color(0xFF4D50), Theme.WHITE, 86, 34);          // ป้ายแดง
            }                                                                // จบ if
            status.setBounds(CENTERS[5] - 43, (ROW_H - 34) / 2, 86, 34);     // กลางคอลัมน์สถานะ กลางแถวแนวตั้ง
            row.add(status);                                                 // ใส่ลงในแถว
        }                                                                    // จบ for
        if (list.size() == 0) {                                              // ไม่มีรายการตามตัวกรองนี้
            JLabel empty = new JLabel("ไม่มีรายการจอง", JLabel.CENTER);       // ข้อความแทนตาราง
            empty.setFont(Theme.font(Font.PLAIN, 15));                       // ตัวปกติ 15
            empty.setForeground(Theme.TEXT_GRAY);                            // สีเทา
            empty.setBounds(0, 0, TABLE_W, ROW_H);                           // แถวแรก
            rowsPanel.add(empty);                                            // ใส่ลงในกล่องแถว
        }                                                                    // จบ if
        rowsPanel.repaint();                                                 // วาดใหม่ (บทที่ 12)

        // ---------- ปุ่มเลขหน้า (ไม่เกิน 5 ปุ่ม รอบ ๆ หน้าปัจจุบัน) ----------
        int count = totalPages;                                   // จำนวนปุ่มที่จะแสดง
        if (count > MAX_PAGE_BUTTONS) {                           // หน้าเยอะเกิน 5
            count = MAX_PAGE_BUTTONS;                             // แสดงแค่ 5 ปุ่ม
        }                                                         // จบ if
        int startPage = page - count / 2;                         // ให้หน้าปัจจุบันอยู่ตรงกลาง
        if (startPage < 1) {                                      // เลยหน้าแรก
            startPage = 1;                                        // เริ่มที่หน้า 1
        }                                                         // จบ if
        if (startPage + count - 1 > totalPages) {                 // เลยหน้าสุดท้าย
            startPage = totalPages - count + 1;                   // เลื่อนกลับให้ปุ่มสุดท้าย = หน้าสุดท้าย
        }                                                         // จบ if

        pagePanel.removeAll();                                    // ลบปุ่มเลขหน้าเดิม
        pageButtons = new RoundButton[count];                     // Array ปุ่มใหม่
        for (int i = 0; i < count; i++) {                         // วนสร้างปุ่มทีละหน้า
            int number = startPage + i;                           // เลขหน้าของปุ่มนี้
            pageButtons[i] = createPageButton("" + number, number == page);   // หน้าปัจจุบัน = สีน้ำเงิน
            pageButtons[i].setBounds(i * 73, 0, 50, 50);          // ปุ่มห่างกันทีละ 73 (เท่าดีไซน์เดิม)
            pageButtons[i].addActionListener(this);               // กด → actionPerformed
            pagePanel.add(pageButtons[i]);                        // ใส่ลงในกล่องปุ่ม
        }                                                         // จบ for

        // จัดปุ่ม < [เลขหน้า] > ให้อยู่กลางตาราง
        int width = 50 + 23 + (count * 73 - 23) + 23 + 50;        // ความกว้างรวม: ปุ่ม< + ช่องว่าง + ปุ่มเลขหน้า + ช่องว่าง + ปุ่ม>
        int left = TABLE_X + (TABLE_W - width) / 2;               // x ซ้ายสุด ให้อยู่กลางตาราง
        prevButton.setBounds(left, 730, 50, 50);                  // ปุ่ม <
        pagePanel.setBounds(left + 73, 730, count * 73 - 23, 50); // กล่องปุ่มเลขหน้า
        nextButton.setBounds(left + 73 + count * 73, 730, 50, 50);   // ปุ่ม >
        prevButton.setEnabled(page > 1);                          // หน้าแรกแล้ว กด < ไม่ได้
        nextButton.setEnabled(page < totalPages);                 // หน้าสุดท้ายแล้ว กด > ไม่ได้
        pagePanel.repaint();                                      // วาดใหม่
        repaint();                                                // วาดทั้งหน้าใหม่
    }                                                             // จบ showTable

    // ================= ActionListener (ปุ่มเปลี่ยนหน้า) =================
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == prevButton && prevButton.isEnabled()) {        // กด <
            page--;                                                         // ถอยไป 1 หน้า
        } else if (e.getSource() == nextButton && nextButton.isEnabled()) { // กด >
            page++;                                                         // ไปหน้าถัดไป
        } else {                                                            // อาจเป็นปุ่มเลขหน้า
            for (int i = 0; i < pageButtons.length; i++) {                  // วนหาว่ากดปุ่มไหน
                if (e.getSource() == pageButtons[i]) {                      // เจอปุ่มที่กด
                    page = Integer.parseInt(pageButtons[i].getButtonText());      // ไปหน้าตามเลขบนปุ่ม
                }                                                           // จบ if
            }                                                               // จบ for
        }                                                                   // จบ if
        showTable();                                                        // สร้างตารางของหน้าใหม่
    }                                                                       // จบ actionPerformed

    // ================= MouseListener (ตัวกรอง) =================
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == allFilter) {             // คลิก "ทั้งหมด"
            filter = FILTER_ALL;
        } else if (e.getSource() == freeFilter) {     // คลิก "จองสำเร็จ"
            filter = FILTER_BOOKED;
        } else if (e.getSource() == busyFilter) {     // คลิก "ยกเลิกแล้ว"
            filter = FILTER_CANCELLED;
        }                                             // จบ if
        page = 1;                                     // เปลี่ยนตัวกรองแล้วกลับไปหน้า 1
        showTable();                                  // สร้างตารางใหม่ตามตัวกรอง
    }                                                 // จบ mouseClicked
    public void mousePressed(MouseEvent e) {}         // ไม่ใช้ (ต้องเขียนให้ครบ 5 เมธอดตาม interface)
    public void mouseReleased(MouseEvent e) {}        // ไม่ใช้
    public void mouseEntered(MouseEvent e) {}         // ไม่ใช้
    public void mouseExited(MouseEvent e) {}          // ไม่ใช้

    // เมธอดช่วย: ข้อความจัดกลางเต็มกล่องตัวกรอง
    private JLabel createFilterText(RoundPanel chip, int w) {
        JLabel l = new JLabel("", JLabel.CENTER);         // ข้อความจัดกลาง (ใส่ข้อความใน showTable)
        l.setFont(Theme.font(Font.BOLD, 14));             // ตัวหนา 14
        l.setBounds(0, 0, w, 40);                         // เต็มกล่อง
        chip.add(l);                                      // ใส่ลงในกล่อง
        return l;                                         // คืนป้าย
    }                                                     // จบ createFilterText

    // เมธอดช่วย: ตัวกรองที่เลือก = พื้นน้ำเงิน ตัวขาว, ไม่ได้เลือก = สีของตัวเอง
    private void setFilterColor(RoundPanel chip, JLabel text, boolean selected, Color bg, Color fg) {
        if (selected) {                                   // ตัวกรองที่เลือกอยู่
            chip.setColors(Theme.SECONDARY, null);        // พื้นน้ำเงิน
            text.setForeground(Theme.WHITE);              // ตัวขาว
        } else {                                          // ตัวกรองอื่น
            chip.setColors(bg, null);                     // สีพื้นของตัวเอง
            text.setForeground(fg);                       // สีตัวอักษรของตัวเอง
        }                                                 // จบ if
    }                                                     // จบ setFilterColor

    // เมธอดช่วย: ข้อความ 1 ช่องในตาราง จัดกึ่งกลางที่ center
    private void addCell(JPanel row, String text, int center, int h, int style) {
        JLabel l = new JLabel(text, JLabel.CENTER);       // ข้อความจัดกลาง
        l.setFont(Theme.font(style, 14));                 // ขนาด 14
        l.setForeground(Theme.TEXT);                      // สีเข้ม
        l.setBounds(center - 60, 0, 120, h);              // กว้าง 120 ให้ center อยู่ตรงกลาง สูงเต็มแถว
        row.add(l);                                       // ใส่ลงในแถว
    }                                                     // จบ addCell

    // เมธอดช่วย: ปุ่มเปลี่ยนหน้าขนาด 50x50 (selected = หน้าปัจจุบัน สีน้ำเงิน)
    private RoundButton createPageButton(String text, boolean selected) {
        RoundButton b = new RoundButton(text);                            // สร้างปุ่ม
        if (selected) {                                                   // ถ้าเป็นหน้าปัจจุบัน
            b.setColors(Theme.SECONDARY, Theme.WHITE, null);              // พื้นน้ำเงิน ตัวขาว
        } else {                                                          // หน้าอื่น / ปุ่มลูกศร
            b.setColors(Theme.WHITE, Theme.TEXT, Theme.CARD_OUTLINE);     // พื้นขาว ตัวเข้ม ขอบเทา
        }                                                                 // จบ if
        return b;                                                         // คืนปุ่ม
    }                                                                     // จบ createPageButton

    // getter: ให้ AppFrame / การทดสอบ เข้าถึงส่วนต่าง ๆ
    public AdminSidebar getSidebar()        { return sidebar; }
    public RoundPanel getAllFilter()        { return allFilter; }
    public RoundPanel getFreeFilter()       { return freeFilter; }
    public RoundPanel getBusyFilter()       { return busyFilter; }
    public RoundButton getPrevButton()      { return prevButton; }
    public RoundButton[] getPageButtons()   { return pageButtons; }
    public RoundButton getNextButton()      { return nextButton; }
}                                                                   // จบคลาส AdminBookingsPage
