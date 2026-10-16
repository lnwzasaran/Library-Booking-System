package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;            // Font
import java.awt.event.*;      // ItemListener, ItemEvent (บทที่ 11)
import javax.swing.*;         // JPanel, JLabel, JTextField, JComboBox
import java.util.ArrayList;   // ลิสต์ห้องที่อ่านจาก rooms.csv

/*
 * หน้า "จองห้อง" — ฟอร์มเลือกวัน/เวลา/ห้อง + การ์ดของห้องที่เลือก ขนาดหน้า 1172 x 790
 *   - มาจากปุ่ม "เลือกห้องนี้" ในหน้าดูห้องทั้งหมด → ตัวเลือกห้องมีแค่ห้องนั้นห้องเดียว
 *   - มาจากเมนู "จองห้อง" (ยังไม่ได้เลือกห้อง)    → ตัวเลือกห้องคือทุกห้องใน rooms.csv
 *   - dropdown "เลือกห้อง" แสดงเฉพาะห้องที่ "ว่างในวันและเวลาที่เลือก" (ไม่มีการจองทับใน bookings.csv)
 *     เปลี่ยนวันที่ (KeyListener) หรือเวลา (ItemListener) แล้ว dropdown อัปเดตเอง
 *   - การ์ดด้านล่างแสดงเฉพาะห้องที่เลือกใน dropdown (เปลี่ยนตามด้วย ItemListener)
 */
public class BookRoomPage extends JPanel implements ItemListener, KeyListener {   // สืบทอด JPanel + ฟัง dropdown และการพิมพ์วันที่

    private JTextField dateField;                    // ช่องวันที่
    private JComboBox<String> startTimeBox;          // dropdown เวลาเริ่ม (<String> = ตัวเลือกเป็นข้อความ)
    private JComboBox<String> endTimeBox;            // dropdown เวลาสิ้นสุด
    private JComboBox<String> roomBox;               // dropdown เลือกห้อง
    private RoomCard[] roomCards;                    // Array การ์ดห้อง 1 ใบต่อ 1 ห้องใน rooms.csv (แสดงทีละใบ)
    private JLabel noRoomLabel;                      // ข้อความตอนไม่มีห้องให้เลือก
    private ArrayList<Room> baseChoices = new ArrayList<>();   // ห้องตั้งต้น (ห้องที่เลือกมา หรือ ทุกห้อง) ก่อนกรองตามวัน/เวลา

    public BookRoomPage() {                   // คอนสตรักเตอร์
        setLayout(null);                      // วางของด้วย setBounds
        setBackground(Theme.BACKGROUND);      // พื้นสีอ่อน

        JLabel title = createTitle("จองห้อง");   // หัวข้อหน้า (สร้างด้วยเมธอดช่วยด้านล่าง)
        title.setBounds(34, 12, 300, 36);        // มุมซ้ายบน
        add(title);                              // ใส่ลงในหน้า

        // ---------- กล่องฟอร์ม ----------
        RoundPanel form = new RoundPanel(Theme.WHITE, Theme.BORDER);   // กล่องพื้นขาว ขอบอ่อน
        form.setBounds(34, 60, 1102, 310);                                 // กว้างเกือบเต็มหน้า
        add(form);                                                         // ใส่ลงในหน้า

        String[] times = {"09.00", "10.00", "11.00", "12.00", "13.00",                       // Array ตัวเลือกเวลา: เปิดให้จอง 09.00
                          "14.00", "15.00", "16.00", "17.00", "18.00"};                      // ถึง 18.00 (BookingService.OPEN_HOUR / CLOSE_HOUR)

        // แถวที่ 1: วันที่ / เวลาเริ่ม / เวลาสิ้นสุด
        addLabel(form, "เลือกวันที่ (วันนี้ / พรุ่งนี้)", 16, 14);       // ป้ายชื่อช่อง (จองล่วงหน้าได้ 1 วัน)
        RoundPanel dateBox = new RoundPanel(Theme.WHITE, Theme.TEXT); // กรอบสี่เหลี่ยมของช่องวันที่ (ขอบสีเข้ม)
        dateBox.setBounds(16, 40, 196, 32);                              // วางใต้ป้ายชื่อ
        form.add(dateBox);                                               // ใส่ลงในฟอร์ม

        dateField = new JTextField(BookingService.today());   // ช่องวันที่ = วันนี้ (AppFrame ตั้งใหม่ทุกครั้งที่เปิดหน้า)
        dateField.setFont(Theme.font(Font.PLAIN, 14));   // ตัวปกติ 14
        dateField.setBorder(null);                       // ไม่เอากรอบของ JTextField (ใช้กรอบของ RoundPanel แทน)
        dateField.setBounds(10, 2, 150, 28);             // วางในกรอบ
        dateField.addKeyListener(this);                  // พิมพ์วันที่แล้ว → keyReleased กรองห้องใหม่ (บทที่ 11)
        dateBox.add(dateField);                          // ใส่ลงในกรอบ

        IconPanel calendar = new IconPanel(IconPanel.CALENDAR, 16, Theme.TEXT);   // ไอคอนปฏิทินเล็ก
        calendar.setBounds(170, 8, 16, 16);                                       // ชิดขวาในกรอบ
        dateBox.add(calendar);                                                    // ใส่ลงในกรอบ

        addLabel(form, "เวลาที่เริ่ม", 280, 14);      // ป้ายชื่อช่อง
        startTimeBox = createCombo(times);           // สร้าง dropdown จาก Array times
        startTimeBox.setSelectedItem("13.00");       // เลือกค่าเริ่มต้น 13.00
        startTimeBox.setBounds(280, 40, 196, 32);    // วางใต้ป้ายชื่อ
        startTimeBox.addItemListener(this);          // เปลี่ยนเวลาเริ่ม → กรองห้องใหม่
        form.add(startTimeBox);                      // ใส่ลงในฟอร์ม

        addLabel(form, "เวลาสิ้นสุด", 544, 14);       // ป้ายชื่อช่อง
        endTimeBox = createCombo(times);             // สร้าง dropdown จาก Array times
        endTimeBox.setSelectedItem("15.00");         // เลือกค่าเริ่มต้น 15.00
        endTimeBox.setBounds(544, 40, 196, 32);      // วางใต้ป้ายชื่อ
        endTimeBox.addItemListener(this);            // เปลี่ยนเวลาสิ้นสุด → กรองห้องใหม่
        form.add(endTimeBox);                        // ใส่ลงในฟอร์ม

        // แถวที่ 2: ห้อง  (ไม่มีช่องจำนวนผู้ใช้ เพราะทุกห้องจุ 6 คนเท่ากัน — ระบบใช้ความจุห้องจาก rooms.csv แทน)
        addLabel(form, "เลือกห้อง", 16, 146);         // ป้ายชื่อช่อง
        roomBox = createCombo(new String[0]);        // dropdown ห้อง เริ่มแบบว่าง (setRoomChoices จะใส่ห้องให้ตอนเปิดหน้า)
        roomBox.setBounds(16, 172, 196, 32);         // วางใต้ป้ายชื่อ
        roomBox.addItemListener(this);               // เปลี่ยนห้องใน dropdown → itemStateChanged (บทที่ 11)
        form.add(roomBox);                           // ใส่ลงในฟอร์ม


        // ---------- ห้องที่เลือก ----------
        JLabel roomsTitle = createTitle("ห้องที่เลือก");   // หัวข้อส่วนการ์ดห้อง
        roomsTitle.setBounds(34, 394, 300, 36);           // วางใต้กล่องฟอร์ม
        add(roomsTitle);                                  // ใส่ลงในหน้า

        ArrayList<Room> rooms = RoomService.loadRooms();   // อ่านห้องทั้งหมดจาก rooms.csv
        roomCards = new RoomCard[rooms.size()];            // จอง Array ให้พอดีจำนวนห้อง
        for (int i = 0; i < roomCards.length; i++) {       // วนสร้างการ์ดทีละห้อง
            Room room = rooms.get(i);                      // ห้องที่ i ในลิสต์
            roomCards[i] = new RoomCard(room.getId(), room.getCapacity(), room.getEquipmentText());   // สร้างการ์ดจากข้อมูลในไฟล์
            roomCards[i].setBounds(34, 440, 200, 192);     // ทุกใบวางตำแหน่งเดียวกัน (แสดงทีละใบ)
            roomCards[i].setVisible(false);                // ซ่อนไว้ก่อน
            add(roomCards[i]);                             // ใส่การ์ดลงในหน้า
        }                                                  // จบ for

        noRoomLabel = new JLabel("ไม่มีห้องว่างให้เลือก กรุณาเลือกห้องจากหน้าดูห้องทั้งหมด");   // ข้อความตอนไม่มีห้อง
        noRoomLabel.setFont(Theme.font(Font.PLAIN, 16));   // ตัวปกติ 16
        noRoomLabel.setForeground(Theme.TEXT_GRAY);        // สีเทา
        noRoomLabel.setBounds(34, 440, 700, 30);           // ตำแหน่งเดียวกับการ์ด
        noRoomLabel.setVisible(false);                     // ซ่อนไว้ก่อน
        add(noRoomLabel);                                  // ใส่ลงในหน้า
    }                                                 // จบคอนสตรักเตอร์

    private JLabel createTitle(String text) {         // เมธอดช่วย: สร้างหัวข้อตัวหนา 22 (ไม่ต้องเขียนซ้ำหลายที่)
        JLabel l = new JLabel(text);                  // สร้างป้าย
        l.setFont(Theme.font(Font.BOLD, 22));         // ตัวหนา 22
        l.setForeground(Theme.TEXT);                  // สีเข้ม
        return l;                                     // คืนป้ายที่สร้าง
    }                                                 // จบ createTitle

    private void addLabel(JPanel panel, String text, int x, int y) {   // เมธอดช่วย: สร้างป้ายชื่อช่องแล้วใส่ลงใน panel
        JLabel l = new JLabel(text);                  // สร้างป้าย
        l.setFont(Theme.font(Font.BOLD, 14));         // ตัวหนา 14
        l.setForeground(Theme.TEXT);                  // สีเข้ม
        l.setBounds(x, y, 196, 20);                   // วางตามตำแหน่งที่ส่งมา
        panel.add(l);                                 // ใส่ลงใน panel ที่ส่งมา
    }                                                 // จบ addLabel

    private JComboBox<String> createCombo(String[] items) {   // เมธอดช่วย: สร้าง dropdown จาก Array
        JComboBox<String> c = new JComboBox<>(items);         // สร้าง JComboBox ที่มีตัวเลือกตาม Array (บทที่ 8)
        c.setFont(Theme.font(Font.PLAIN, 14));                // ตัวปกติ 14
        c.setBackground(Theme.WHITE);                         // พื้นขาว
        return c;                                             // คืน dropdown ที่สร้าง
    }                                                         // จบ createCombo

    // ใส่ตัวเลือกห้องใหม่ใน dropdown (ส่งมาห้องเดียว = ห้องที่กดเลือกมา, ส่งมาหลายห้อง = ห้องว่างทั้งหมด)
    public void setRoomChoices(ArrayList<Room> choices) {
        baseChoices = choices;                         // จำห้องตั้งต้นไว้ (ใช้กรองใหม่ทุกครั้งที่เปลี่ยนวัน/เวลา)
        filterRooms();                                 // กรองตามวัน/เวลาที่เลือกอยู่ แล้วใส่ลง dropdown
    }                                                  // จบ setRoomChoices

    // ใส่ห้องลง dropdown เฉพาะห้องที่ว่างในวัน/เวลาที่เลือก (อ่าน bookings.csv ใหม่ทุกครั้ง)
    private void filterRooms() {
        String date = dateField.getText().trim();                    // วันที่ที่กรอก
        String start = (String) startTimeBox.getSelectedItem();      // เวลาเริ่มที่เลือก
        String end = (String) endTimeBox.getSelectedItem();          // เวลาสิ้นสุดที่เลือก
        String old = (String) roomBox.getSelectedItem();             // ห้องที่เลือกไว้ก่อน (จะพยายามเลือกห้องเดิมให้)
        ArrayList<Room> free = RoomService.freeRoomsAt(baseChoices, date, start, end);   // logic หาห้องที่ว่างช่วงนี้

        roomBox.removeAllItems();                                    // ลบตัวเลือกเดิมทั้งหมดออกจาก dropdown
        for (Room room : free) {                                     // วนห้องที่ว่าง
            roomBox.addItem(room.getId());                           // เพิ่มเป็นตัวเลือก (บทที่ 8)
        }                                                            // จบ for
        if (old != null) {                                           // ถ้าเคยเลือกห้องไว้
            roomBox.setSelectedItem(old);                            // เลือกห้องเดิม (ถ้าห้องเดิมไม่ว่างแล้ว จะได้ห้องแรกแทน)
        }                                                            // จบ if
        showSelectedCard();                                          // แสดงการ์ดของห้องที่ถูกเลือกอยู่
    }                                                                // จบ filterRooms

    // แสดงเฉพาะการ์ดของห้องที่เลือกใน dropdown
    private void showSelectedCard() {
        String selected = (String) roomBox.getSelectedItem();          // ห้องที่เลือกอยู่ (dropdown ว่าง = null)
        for (int i = 0; i < roomCards.length; i++) {                   // วนทุกการ์ด
            roomCards[i].setVisible(roomCards[i].getRoomName().equals(selected));   // ตรงกับห้องที่เลือก = แสดง, ไม่ตรง = ซ่อน
        }                                                              // จบ for
        noRoomLabel.setVisible(selected == null);                      // ไม่มีห้องให้เลือก → แสดงข้อความ
        if (baseChoices.size() == 1) {                                 // เลือกห้องมาจากหน้าดูห้องทั้งหมด
            noRoomLabel.setText("ห้อง " + baseChoices.get(0).getId() + " ไม่ว่างในวันและเวลานี้ กรุณาเลือกเวลาอื่น");   // บอกว่าห้องที่เลือกมาไม่ว่าง
        } else {                                                       // เข้ามาจากเมนู
            noRoomLabel.setText("ไม่มีห้องว่างในวันและเวลานี้ กรุณาเลือกเวลาอื่น");   // ทุกห้องถูกจองช่วงนี้
        }                                                              // จบ if
        repaint();                                                     // วาดหน้าใหม่ (บทที่ 12)
    }                                                                  // จบ showSelectedCard

    // ================= ItemListener (dropdown) =================
    public void itemStateChanged(ItemEvent e) {        // ทำงานเมื่อตัวเลือกใน dropdown เปลี่ยน
        if (e.getSource() == roomBox) {                // ถ้าเป็น dropdown เลือกห้อง
            showSelectedCard();                        // เปลี่ยนการ์ดให้ตรงกับห้องที่เลือก
        } else if (e.getStateChange() == ItemEvent.SELECTED) {   // dropdown เวลาเริ่ม/สิ้นสุด (ทำตอนเลือกค่าใหม่ ไม่ทำซ้ำตอนยกเลิกค่าเก่า)
            filterRooms();                             // กรองห้องใหม่ตามเวลาที่เปลี่ยน
        }                                              // จบ if
    }                                                  // จบ itemStateChanged

    // ================= KeyListener (ช่องวันที่) =================
    public void keyReleased(KeyEvent e) {              // ทำงานเมื่อปล่อยแป้น (ข้อความในช่องเปลี่ยนแล้ว)
        if (e.getSource() == dateField) {              // ถ้าเป็นช่องวันที่
            filterRooms();                             // กรองห้องใหม่ตามวันที่ที่พิมพ์
        }                                              // จบ if
    }                                                  // จบ keyReleased
    public void keyTyped(KeyEvent e) {}                // ไม่ใช้ (ต้องเขียนให้ครบตาม interface)
    public void keyPressed(KeyEvent e) {}              // ไม่ใช้

    // getter: ให้ AppFrame / logic อ่านค่าที่ผู้ใช้เลือก เช่น getRoomBox().getSelectedItem()
    public JTextField getDateField()            { return dateField; }                       // คืนค่า dateField
    public JComboBox<String> getStartTimeBox()  { return startTimeBox; }                    // คืนค่า startTimeBox
    public JComboBox<String> getEndTimeBox()    { return endTimeBox; }                      // คืนค่า endTimeBox
    public JComboBox<String> getRoomBox()       { return roomBox; }                         // คืนค่า roomBox
    public RoomCard[] getRoomCards()            { return roomCards; }                       // คืนค่า roomCards (AppFrame ผูก ActionListener ให้ทุกใบ)
}                                                                   // จบคลาส BookRoomPage
