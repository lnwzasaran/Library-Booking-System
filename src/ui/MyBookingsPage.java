package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;            // Font
import java.awt.event.*;      // ActionListener, ActionEvent (บทที่ 10)
import javax.swing.*;         // JPanel, JLabel
import java.util.ArrayList;   // ลิสต์การจองที่อ่านจาก bookings.csv

/*
 * หน้า "การจองของฉัน" ขนาดหน้า 1172 x 790 — อ่านข้อมูลจาก bookings.csv ทุกครั้งที่เปิดหน้า
 *   ตารางที่ 1 การจองปัจจุบัน : สถานะ BOOKED และยังไม่หมดเวลา → ป้าย "จองสำเร็จ" + ปุ่ม "ยกเลิก"
 *   ตารางที่ 2 ประวัติการจอง  : ทุกการจองของนิสิตคนนี้ (รวมการจองปัจจุบันด้วย)
 *                               - BOOKED    → ป้าย "จองสำเร็จ" สีเขียว
 *                               - CANCELLED → ป้าย "ยกเลิกแล้ว" สีแดง
 *   กด "ยกเลิก" → กล่องถาม "คุณแน่ใจว่าจะกดยกเลิก?" ปุ่ม ใช่ / ไม่
 *                 ใช่ = เปลี่ยนสถานะในไฟล์เป็น CANCELLED แล้วย้ายไปอยู่ในประวัติ, ไม่ = ไม่ทำอะไร
 * ตารางทำจาก RoundPanel: แถวหัวตาราง 1 กล่อง + แถวข้อมูลแถวละ 1 กล่อง
 */
public class MyBookingsPage extends JPanel implements ActionListener {   // สืบทอด JPanel + ฟังปุ่มยกเลิกเอง

    // ตำแหน่งกึ่งกลางของแต่ละคอลัมน์ (วัดจากขอบซ้ายของตาราง)
    // ลำดับ, รหัสการจอง, ห้องที่จอง, วันที่จอง, เวลา, สถานะ, จัดการ
    private static final int[] COLUMN_CENTERS = {24, 120, 262, 442, 594, 764, 937};

    private static final int TABLE_X = 43;       // ตำแหน่งซ้ายของตาราง
    private static final int TABLE_W = 1086;     // ความกว้างของตาราง
    private static final int BODY_Y = 100;       // ส่วนตารางเริ่มที่ y = 100 ของหน้า (ใต้หัวข้อ)
    private static final int PAGE_BOTTOM = 780;  // ห้ามวางแถวเลยบรรทัดนี้ (หน้าสูง 790)

    private JPanel body;                          // กล่องใส่ตารางทั้งสอง (ล้างแล้วสร้างใหม่ทุกครั้งที่ refresh)
    private String studentId = "";                // รหัสนิสิตของผู้ที่ดูหน้านี้อยู่
    private RoundButton[] cancelButtons = new RoundButton[0];   // ปุ่ม "ยกเลิก" ของการจองปัจจุบันแต่ละแถว (Array)
    private Booking pendingCancel = null;                     // การจองที่กำลังถามยืนยันยกเลิก (รอกด "ใช่")
    private ArrayList<Booking> currentList = new ArrayList<>(); // การจองปัจจุบัน เรียงตรงกับปุ่ม (ปุ่มที่ i = การจองที่ i)

    public MyBookingsPage() {                     // คอนสตรักเตอร์
        setLayout(null);                          // วางของด้วย setBounds
        setBackground(Theme.BACKGROUND);          // พื้นสีอ่อน

        JLabel title = new JLabel("การจองของฉัน");   // หัวข้อหน้า
        title.setFont(Theme.font(Font.BOLD, 24));    // ตัวหนา 24
        title.setForeground(Theme.TEXT);             // สีเข้ม
        title.setBounds(27, 22, 400, 40);            // มุมซ้ายบน
        add(title);                                  // ใส่ลงในหน้า

        body = new JPanel();                         // กล่องใส่ตาราง
        body.setLayout(null);                        // วางของด้วย setBounds
        body.setBackground(Theme.BACKGROUND);        // พื้นสีเดียวกับหน้า
        body.setBounds(0, BODY_Y, 1172, 790 - BODY_Y);   // ใต้หัวข้อ จนสุดหน้า
        add(body);                                   // ใส่ลงในหน้า
    }                                                // จบคอนสตรักเตอร์

    // อ่าน bookings.csv ใหม่ แล้วสร้างตารางทั้งสองของนิสิตคนนี้ (AppFrame เรียกทุกครั้งที่เปิดหน้า)
    public void refresh(String studentId) {
        this.studentId = studentId;                                 // จำรหัสนิสิตไว้ (ใช้ตอนยกเลิก)
        body.removeAll();                                           // ลบตารางเดิมทั้งหมดก่อนสร้างใหม่

        // ---------- แยกการจองของคนนี้เป็น 2 กลุ่ม ----------
        ArrayList<Booking> all = BookingService.loadBookings();     // อ่านการจองทั้งหมดจากไฟล์
        currentList = BookingService.currentOf(all, studentId);     // การจองปัจจุบัน (logic แยกให้: ยังไม่ยกเลิก + ยังไม่หมดเวลา)
        ArrayList<Booking> historyList = BookingService.historyOf(all, studentId);   // ประวัติ = ทุกการจองของคนนี้ (ใหม่ไปเก่า)

        // ================= ตารางที่ 1: การจองปัจจุบัน =================
        addSectionTitle("การจองปัจจุบัน", 110);                                     // หัวข้อตาราง
        String[] currentHeaders = {"ลำดับ", "รหัสการจอง", "ห้องที่จอง", "วันที่จอง", "เวลา", "สถานะ", "จัดการ"};   // หัวคอลัมน์ 7 ช่อง
        addHeaderRow(currentHeaders, 171);                                         // แถวหัวตาราง ที่ y = 171

        cancelButtons = new RoundButton[currentList.size()];                       // สร้าง Array ปุ่มยกเลิก เท่าจำนวนแถว
        int y = 231;                                                               // y ของแถวแรก
        for (int i = 0; i < currentList.size(); i++) {                             // วนสร้างแถวข้อมูลทีละแถว
            RoundPanel row = addDataRow(toRow(i + 1, currentList.get(i)), y, 63);  // กล่องแถว + ข้อความ 5 คอลัมน์แรก

            StatusBadge status = new StatusBadge("จองสำเร็จ", Theme.SUCCESS, Theme.TEXT, 80, 20);   // ป้ายสถานะสีเขียว
            status.setBounds(COLUMN_CENTERS[5] - 40, 22, 80, 20);                  // กลางคอลัมน์ "สถานะ" (กว้าง 80 → เลื่อนซ้าย 40)
            row.add(status);                                                       // ใส่ลงในแถว

            cancelButtons[i] = new RoundButton("ยกเลิก");                           // ปุ่มยกเลิก
            cancelButtons[i].setColors(Theme.DANGER, Theme.WHITE, null);           // พื้นแดง ตัวขาว ไม่มีขอบ
            cancelButtons[i].setFont(Theme.font(Font.BOLD, 13));                   // ตัวหนา 13
            cancelButtons[i].setBounds(COLUMN_CENTERS[6] - 40, 22, 80, 20);        // กลางคอลัมน์ "จัดการ"
            cancelButtons[i].addActionListener(this);                              // กดแล้ว → actionPerformed ของหน้านี้
            row.add(cancelButtons[i]);                                             // ใส่ลงในแถว
            y += 70;                                                               // แถวถัดไปเลื่อนลง 70 พิกเซล
        }                                                                          // จบ for
        if (currentList.size() == 0) {                                             // ไม่มีการจองปัจจุบัน
            addEmptyRow("ยังไม่มีการจองปัจจุบัน", y, 63);                             // แถวข้อความแทน
            y += 70;                                                               // เลื่อนลงเท่าแถวปกติ
        }                                                                          // จบ if

        // ================= ตารางที่ 2: ประวัติการจอง =================
        int historyTop = y + 67;                                                   // หัวข้อประวัติอยู่ใต้ตารางแรก (ระยะเท่าดีไซน์เดิม)
        addSectionTitle("ประวัติการจอง", historyTop);                               // หัวข้อตาราง
        String[] historyHeaders = {"ลำดับ", "รหัสการจอง", "ห้องที่จอง", "วันที่จอง", "เวลา", "สถานะ"};   // หัวคอลัมน์ 6 ช่อง (ไม่มี "จัดการ")
        addHeaderRow(historyHeaders, historyTop + 51);                             // แถวหัวตาราง

        y = historyTop + 110;                                                      // y ของแถวแรกในประวัติ
        int shown = 0;                                                             // จำนวนแถวที่แสดงแล้ว
        for (int i = 0; i < historyList.size(); i++) {                             // วนสร้างแถวข้อมูลทีละแถว
            if (y + 48 > PAGE_BOTTOM) {                                            // แถวนี้จะล้นหน้าจอ
                break;                                                             // หยุด (แสดงเฉพาะรายการล่าสุดที่พอดีหน้า)
            }                                                                      // จบ if
            Booking b = historyList.get(i);                                        // การจองที่ i
            RoundPanel row = addDataRow(toRow(i + 1, b), y, 48);                   // กล่องแถว + ข้อความ 5 คอลัมน์แรก

            StatusBadge status;                                                    // ป้ายสถานะ
            if (b.isActive()) {                                                    // ไม่ได้ยกเลิก (กำลังจองอยู่ หรือ หมดเวลาแล้ว)
                status = new StatusBadge("จองสำเร็จ", Theme.SUCCESS, Theme.TEXT, 80, 20);    // ป้ายสีเขียว
            } else {                                                               // ยกเลิกแล้ว
                status = new StatusBadge("ยกเลิกแล้ว", Theme.DANGER, Theme.WHITE, 80, 20);   // ป้ายสีแดง
            }                                                                      // จบ if
            status.setBounds(820 - 40, 14, 80, 20);                                // ตารางนี้คอลัมน์ "สถานะ" อยู่กลาง x = 820
            row.add(status);                                                       // ใส่ลงในแถว
            y += 55;                                                               // แถวถัดไปเลื่อนลง 55 พิกเซล
            shown++;                                                               // นับแถวที่แสดง
        }                                                                          // จบ for
        if (historyList.size() == 0) {                                             // ยังไม่มีประวัติ
            addEmptyRow("ยังไม่มีประวัติการจอง", y, 48);                              // แถวข้อความแทน
        } else if (shown < historyList.size()) {                                   // มีบางรายการที่ไม่ได้แสดง (ล้นหน้า)
            JLabel note = new JLabel("(แสดง " + shown + " รายการล่าสุด จากทั้งหมด " + historyList.size() + " รายการ)");   // บอกว่าแสดงไม่ครบ
            note.setFont(Theme.font(Font.PLAIN, 14));                              // ตัวปกติ 14
            note.setForeground(Theme.TEXT_GRAY);                                   // สีเทา
            note.setBounds(230, historyTop - BODY_Y + 4, 500, 24);                 // ข้างหัวข้อ "ประวัติการจอง"
            body.add(note);                                                        // ใส่ลงในกล่องตาราง
        }                                                                          // จบ if

        body.repaint();                                                            // วาดตารางใหม่ (บทที่ 12)
    }                                                                              // จบ refresh

    // แปลงการจอง 1 รายการเป็นข้อความ 5 คอลัมน์ {ลำดับ, รหัส, ห้อง, วันที่, เวลา}
    private String[] toRow(int no, Booking b) {
        String time = BookingService.timeRange(b);                                     // "13:00 - 15:00"
        String[] row = {"" + no, b.getId(), "ห้อง " + b.getRoomId(), BookingService.thaiDate(b.getDate()), time};   // Array 5 ช่อง
        return row;                                                    // คืนข้อความของแถว
    }                                                                  // จบ toRow

    // ================= ActionListener (ปุ่มยกเลิก) =================
    public void actionPerformed(ActionEvent e) {                       // ทำงานเมื่อกดปุ่มยกเลิกแถวใดก็ได้ หรือกด "ใช่" ในกล่องยืนยัน
        if (MessagePanel.isYesButton(e.getSource())) {                 // กด "ใช่" ในกล่องยืนยัน
            Booking b = pendingCancel;                                 // การจองที่ถามไว้
            pendingCancel = null;                                      // ล้างทิ้ง
            if (b != null && BookingService.cancel(b.getId(), b.getDate(), studentId)) {   // เปลี่ยนสถานะในไฟล์เป็น CANCELLED
                refresh(studentId);                                    // สร้างตารางใหม่ → แถวนี้ย้ายไปประวัติ ป้าย "ยกเลิกแล้ว"
            } else {                                                   // บันทึกไฟล์ไม่ได้
                MessagePanel.showMessage(this, "ยกเลิกการจองไม่สำเร็จ กรุณาลองใหม่", "ยกเลิกไม่สำเร็จ", MessagePanel.ERROR);   // แจ้งผู้ใช้
            }                                                          // จบ if
            return;                                                    // จบ
        }                                                              // จบ if
        for (int i = 0; i < cancelButtons.length; i++) {               // วนหาว่ากดปุ่มของแถวไหน
            if (e.getSource() == cancelButtons[i]) {                   // ถ้าเป็นปุ่มของแถวที่ i
                confirmCancel(currentList.get(i));                     // ถามยืนยันก่อนยกเลิก
                return;                                                // เจอแล้ว ไม่ต้องวนต่อ
            }                                                          // จบ if
        }                                                              // จบ for
    }                                                                  // จบ actionPerformed

    // กล่องถามยืนยัน ปุ่ม "ใช่" / "ไม่" (MessagePanel — กล่องข้อความแบบ JPanel)
    // กล่อง JPanel ไม่หยุดรอคำตอบ จึงจำการจองไว้ก่อน แล้วรอ actionPerformed ตอนกด "ใช่"
    private void confirmCancel(Booking b) {
        pendingCancel = b;                                             // จำว่ากำลังถามเรื่องการจองไหน
        MessagePanel.showConfirm(this, "คุณแน่ใจว่าจะกดยกเลิก?",          // ข้อความคำถาม
                "ยกเลิกการจอง " + b.getId(), this);                      // ชื่อกล่อง, กด "ใช่" → actionPerformed ของหน้านี้
    }                                                                  // จบ confirmCancel

    // หัวข้อของแต่ละตาราง เช่น "การจองปัจจุบัน" (y = ตำแหน่งในหน้า)
    private void addSectionTitle(String text, int y) {
        JLabel l = new JLabel(text);                  // สร้างป้าย
        l.setFont(Theme.font(Font.PLAIN, 19));        // ตัวปกติ 19
        l.setForeground(Theme.TEXT);                  // สีเข้ม
        l.setBounds(84, y - BODY_Y, 400, 28);         // เยื้องเข้ามาจากตารางเล็กน้อยตามดีไซน์ (ลบ BODY_Y เพราะวางใน body)
        body.add(l);                                  // ใส่ลงในกล่องตาราง
    }                                                 // จบ addSectionTitle

    // แถวหัวตาราง: กล่องสี่เหลี่ยม + ชื่อคอลัมน์ (จำนวนคอลัมน์ = ความยาว Array ที่ส่งมา)
    private void addHeaderRow(String[] headers, int y) {
        RoundPanel row = new RoundPanel(Theme.WHITE, Theme.CARD_OUTLINE);   // กล่องพื้นขาว ขอบเทา
        row.setBounds(TABLE_X, y - BODY_Y, TABLE_W, 54);                       // สูง 54
        body.add(row);                                                         // ใส่ลงในกล่องตาราง

        for (int i = 0; i < headers.length; i++) {                             // วนใส่ชื่อคอลัมน์ทีละช่อง
            int center = COLUMN_CENTERS[i];                                    // กลางคอลัมน์ที่ i
            if (headers.length == 6 && i == 5) {                               // ตารางประวัติ (6 คอลัมน์) คอลัมน์ "สถานะ"
                center = 820;                                                  // ย้ายไปกลาง x = 820 ตามดีไซน์
            }                                                                  // จบ if
            addCell(row, headers[i], center, 0, 54, Font.PLAIN, i == 0);       // ใส่ชื่อคอลัมน์ (คอลัมน์แรกชิดซ้าย)
        }                                                                      // จบ for
    }                                                                          // จบ addHeaderRow

    // แถวข้อมูล: กล่องสี่เหลี่ยม + ข้อความ 5 คอลัมน์แรก (ลำดับ ถึง เวลา) คืนกล่องแถวกลับไปให้ใส่ป้าย/ปุ่มต่อ
    private RoundPanel addDataRow(String[] data, int y, int h) {
        RoundPanel row = new RoundPanel(Theme.WHITE, Theme.CARD_OUTLINE);   // กล่องพื้นขาว ขอบเทา
        row.setBounds(TABLE_X, y - BODY_Y, TABLE_W, h);                        // สูงตามที่ส่งมา
        body.add(row);                                                         // ใส่ลงในกล่องตาราง

        for (int i = 0; i < data.length; i++) {                                // วนใส่ข้อมูลทีละคอลัมน์
            addCell(row, data[i], COLUMN_CENTERS[i], 0, h, Font.BOLD, i == 0); // ใส่ข้อมูล (คอลัมน์แรกชิดซ้าย)
        }                                                                      // จบ for
        return row;                                                            // คืนกล่องแถว
    }                                                                          // จบ addDataRow

    // แถวว่าง: กล่องสี่เหลี่ยม + ข้อความสีเทาตรงกลาง (ตอนไม่มีข้อมูล)
    private void addEmptyRow(String text, int y, int h) {
        RoundPanel row = new RoundPanel(Theme.WHITE, Theme.CARD_OUTLINE);   // กล่องพื้นขาว ขอบเทา
        row.setBounds(TABLE_X, y - BODY_Y, TABLE_W, h);                        // ตำแหน่งเดียวกับแถวข้อมูล
        body.add(row);                                                         // ใส่ลงในกล่องตาราง
        JLabel l = new JLabel(text, JLabel.CENTER);                            // ข้อความจัดกลาง
        l.setFont(Theme.font(Font.PLAIN, 14));                                 // ตัวปกติ 14
        l.setForeground(Theme.TEXT_GRAY);                                      // สีเทา
        l.setBounds(0, 0, TABLE_W, h);                                         // เต็มแถว
        row.add(l);                                                            // ใส่ลงในแถว
    }                                                                          // จบ addEmptyRow

    // ข้อความ 1 ช่องในตาราง: จัดกึ่งกลางที่ center (หรือชิดซ้ายถ้า leftAlign เป็น true)
    private void addCell(JPanel row, String text, int center, int y, int h, int style, boolean leftAlign) {
        JLabel l;                                                  // ประกาศตัวแปรป้าย
        if (leftAlign) {                                           // ถ้าเป็นคอลัมน์แรก (ลำดับ)
            l = new JLabel(text);                                  // ข้อความชิดซ้าย
            l.setBounds(10, y, 60, h);                             // ห่างขอบซ้ายของแถว 10 พิกเซล
        } else {                                                   // คอลัมน์อื่น
            l = new JLabel(text, JLabel.CENTER);                   // ข้อความจัดกลาง
            l.setBounds(center - 80, y, 160, h);                   // กว้าง 160 โดยให้ center อยู่ตรงกลาง
        }                                                          // จบ if
        l.setFont(Theme.font(style, 14));                          // ขนาด 14 (หัวตาราง = ปกติ, ข้อมูล = ตัวหนา)
        l.setForeground(Theme.TEXT);                               // สีเข้ม
        row.add(l);                                                // ใส่ลงในแถว
    }                                                              // จบ addCell

    public RoundButton[] getCancelButtons() {   // getter: ปุ่มยกเลิกของแต่ละแถว (ใช้ทดสอบ / ต่อยอด)
        return cancelButtons;                   // คืน Array ปุ่มยกเลิก
    }                                           // จบ getCancelButtons
}                                               // จบคลาส MyBookingsPage
