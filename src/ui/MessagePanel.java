package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import java.awt.*;            // Color, Font, Graphics, Image
import java.awt.event.*;      // ActionListener, MouseListener, KeyListener
import javax.swing.*;         // JPanel, JLabel, ImageIcon, SwingUtilities

/*
 * กล่องข้อความที่ทำจาก JPanel (ใช้แทน JOptionPane) — ลอยอยู่กลางหน้าต่างโปรแกรม ไม่ได้เปิดหน้าต่างใหม่
 *   - หน้าตาเหมือนกล่องของ JOptionPane: แถบหัว (ชื่อกล่อง + ปุ่ม ✕), ไอคอน, ข้อความ, ปุ่ม OK / ใช่ / ไม่
 *   - ไอคอนเป็นรูปจากโฟลเดอร์ icons (ImageIcon บทที่ 9): msg_error.png = ผิดพลาด, msg_info.png = แจ้งข้อมูล,
 *     msg_question.png = คำถาม, msg_close_*.png = ปุ่ม ✕
 *   - ตอนกล่องเปิดอยู่ พื้นหลังจะมืดลง และกดส่วนอื่นของโปรแกรมไม่ได้ (เหมือนกล่องที่ต้องตอบก่อน)
 *
 * วิธีทำงาน: AppFrame ตั้ง MessagePanel เป็น "glass pane" (แผ่นใสที่วางทับทั้งหน้าต่าง)
 *   แสดงกล่อง = setVisible(true) · ปิดกล่อง = setVisible(false)
 *
 * วิธีใช้ (จากหน้าไหนก็ได้ที่อยู่ในหน้าต่างโปรแกรม):
 *   MessagePanel.showMessage(this, "สมัครสำเร็จ", "สมัครสมาชิก", MessagePanel.INFO);       // ปุ่ม OK
 *   MessagePanel.showConfirm(this, "คุณแน่ใจว่าจะกดยกเลิก?", "ยกเลิกการจอง", this);         // ปุ่ม ใช่ / ไม่
 *     → กด "ใช่" แล้ว actionPerformed ของ this จะถูกเรียก (เช็กด้วย MessagePanel.isYesButton(e.getSource()))
 */
public class MessagePanel extends JPanel implements ActionListener, MouseListener, KeyListener {   // แผ่นทับหน้าต่าง + ฟังปุ่ม เมาส์ และแป้นพิมพ์เอง

    // ชนิดของกล่อง (ใช้เลือกไอคอน)
    public static final int ERROR = 0;      // ผิดพลาด  → ไอคอนแดงกากบาท
    public static final int INFO = 1;       // แจ้งข้อมูล → ไอคอนฟ้าตัว i
    public static final int QUESTION = 2;   // คำถาม    → ไอคอนเขียว ?

    private static final int BOX_H = 150;           // ความสูงกล่อง
    private static final int TITLE_H = 30;          // ความสูงแถบหัว
    private static final Color BODY = new Color(0xF0F0F0);       // สีพื้นกล่อง (เทาอ่อนแบบกล่องข้อความ)
    private static final Color FRAME = new Color(0x8C8C8C);      // สีเส้นกรอบกล่อง
    private static final Color DIM = new Color(0, 0, 0, 70);     // สีดำโปร่งแสง ทำให้พื้นหลังมืดลง (ค่าที่ 4 = ความทึบ 0-255)

    private RoundPanel box;            // กล่องข้อความ (อยู่กลางหน้าต่าง)
    private JLabel titleText;          // ชื่อกล่องบนแถบหัว
    private JLabel messageText;        // ข้อความในกล่อง
    private JLabel icon;               // ไอคอนหน้าข้อความ (รูปจากโฟลเดอร์ icons)
    private JLabel closeX;             // ปุ่ม ✕ มุมขวาบน (รูปจากโฟลเดอร์ icons)
    private ImageIcon[] typeIcons;     // รูปไอคอนของแต่ละชนิด: [0] ผิดพลาด, [1] แจ้งข้อมูล, [2] คำถาม
    private ImageIcon closeBlack;      // รูป ✕ สีดำ (ปกติ)
    private ImageIcon closeWhite;      // รูป ✕ สีขาว (ตอนเอาเมาส์วาง พื้นแดง)
    private RoundButton okButton;      // ปุ่ม OK (กล่องแจ้งข้อความ)
    private RoundButton yesButton;     // ปุ่ม ใช่ (กล่องยืนยัน)
    private RoundButton noButton;      // ปุ่ม ไม่ (กล่องยืนยัน)
    private ActionListener yesListener = null;   // ใครรอฟังการกด "ใช่" ของกล่องยืนยันที่เปิดอยู่

    public MessagePanel() {                       // คอนสตรักเตอร์ (AppFrame สร้างครั้งเดียว)
        setLayout(null);                          // วางของด้วย setBounds
        setOpaque(false);                         // ไม่ระบายพื้นเอง (วาดพื้นมืดโปร่งแสงใน paintComponent)
        addMouseListener(this);                   // ดักการคลิกพื้นหลัง ไม่ให้ทะลุไปกดปุ่มในหน้าข้างใต้

        box = new RoundPanel(BODY, FRAME);        // กล่องสี่เหลี่ยม พื้นเทาอ่อน ขอบเทา
        add(box);                                 // ใส่ลงในแผ่นทับ

        RoundPanel titleBar = new RoundPanel(Theme.WHITE, null);   // แถบหัวพื้นขาว
        titleBar.setBounds(1, 1, 10, TITLE_H);                     // ความกว้างจริงตั้งตอนแสดงกล่อง (layoutBox)
        box.add(titleBar);                                         // ใส่ลงในกล่อง

        titleText = new JLabel("");                       // ชื่อกล่อง
        titleText.setFont(Theme.font(Font.PLAIN, 13));   // ตัวปกติ 13
        titleText.setForeground(Color.BLACK);             // สีดำ
        titleBar.add(titleText);                          // ใส่ลงในแถบหัว

        // รูปจากโฟลเดอร์ icons (ImageIcon — บทที่ 9) ใช้เมธอดโหลดรูปตัวเดียวกับ IconPanel
        typeIcons = new ImageIcon[3];                                                   // รูป 3 ชนิด
        typeIcons[ERROR] = toIcon(IconPanel.loadImage("msg_error.png", 36, 36));        // แดงกากบาท
        typeIcons[INFO] = toIcon(IconPanel.loadImage("msg_info.png", 36, 36));          // ฟ้าตัว i
        typeIcons[QUESTION] = toIcon(IconPanel.loadImage("msg_question.png", 36, 36));  // เขียว ?
        closeBlack = toIcon(IconPanel.loadImage("msg_close_0_0_0.png", 16, 16));        // ✕ สีดำ
        closeWhite = toIcon(IconPanel.loadImage("msg_close_255_255_255.png", 16, 16));  // ✕ สีขาว

        closeX = new JLabel(closeBlack, JLabel.CENTER);   // ปุ่ม ✕ = ป้ายที่มีรูป จัดกลาง
        closeX.setBackground(new Color(0xC42B1C));        // สีพื้นตอนเอาเมาส์วาง (แดงแบบ Windows)
        closeX.setOpaque(false);                          // ปกติไม่ระบายพื้น
        closeX.addMouseListener(this);                    // คลิก / เอาเมาส์วาง → MouseListener
        titleBar.add(closeX);                             // ใส่ลงในแถบหัว

        icon = new JLabel();                       // ไอคอนหน้าข้อความ (ใส่รูปตอนเปิดกล่อง)
        box.add(icon);                             // ใส่ลงในกล่อง

        messageText = new JLabel("");                       // ข้อความ
        messageText.setFont(Theme.font(Font.PLAIN, 15));    // ตัวปกติ 15
        messageText.setForeground(Color.BLACK);             // สีดำ
        box.add(messageText);                               // ใส่ลงในกล่อง

        okButton = createButton("OK");             // ปุ่ม OK
        yesButton = createButton("ใช่");            // ปุ่ม ใช่
        noButton = createButton("ไม่");             // ปุ่ม ไม่
        box.add(okButton);                         // ใส่ลงในกล่อง
        box.add(yesButton);                        // ใส่ลงในกล่อง
        box.add(noButton);                         // ใส่ลงในกล่อง

        setVisible(false);                         // เริ่มต้นซ่อนไว้
    }                                              // จบคอนสตรักเตอร์

    // ================= วิธีเรียกใช้ (static: เรียกจากหน้าไหนก็ได้) =================

    // กล่องแจ้งข้อความ ปุ่ม OK — type = MessagePanel.ERROR หรือ MessagePanel.INFO
    public static void showMessage(Component from, String message, String title, int type) {
        MessagePanel p = of(from);                 // หากล่องของหน้าต่างนี้
        p.open(message, title, type, false, null); // เปิดแบบปุ่ม OK ปุ่มเดียว
    }                                              // จบ showMessage

    // กล่องยืนยัน ปุ่ม ใช่ / ไม่ — กด "ใช่" แล้วเรียก actionPerformed ของ onYes, กด "ไม่" / ✕ = ปิดเฉย ๆ
    public static void showConfirm(Component from, String message, String title, ActionListener onYes) {
        MessagePanel p = of(from);                       // หากล่องของหน้าต่างนี้
        p.open(message, title, QUESTION, true, onYes);   // เปิดแบบปุ่ม ใช่ / ไม่
    }                                                    // จบ showConfirm

    // ใช้ใน actionPerformed ของหน้าที่เรียก showConfirm: เช็กว่าเป็นการกด "ใช่" ของกล่องหรือไม่
    public static boolean isYesButton(Object source) {
        return source instanceof RoundButton && ((RoundButton) source).getButtonText().equals("ใช่")
                && SwingUtilities.getAncestorOfClass(MessagePanel.class, (Component) source) != null;   // ปุ่ม "ใช่" ที่อยู่ใน MessagePanel
    }                                                    // จบ isYesButton

    // หา MessagePanel ของหน้าต่างที่ from อยู่ (AppFrame ตั้งไว้เป็น glass pane)
    private static MessagePanel of(Component from) {
        return (MessagePanel) SwingUtilities.getRootPane(from).getGlassPane();   // glass pane ของหน้าต่าง = กล่องข้อความนี้
    }                                                                            // จบ of

    // ================= เปิด / ปิดกล่อง =================

    private void open(String message, String title, int type, boolean confirm, ActionListener onYes) {
        titleText.setText(title);                      // ชื่อกล่อง
        messageText.setText(message);                  // ข้อความ
        icon.setIcon(typeIcons[type]);                 // รูปไอคอนตามชนิด (ผิดพลาด / แจ้งข้อมูล / คำถาม)
        yesListener = onYes;                           // จำไว้ว่ากด "ใช่" แล้วต้องแจ้งใคร
        okButton.setVisible(!confirm);                 // กล่องแจ้งข้อความ → ปุ่ม OK
        yesButton.setVisible(confirm);                 // กล่องยืนยัน → ปุ่ม ใช่
        noButton.setVisible(confirm);                  // กล่องยืนยัน → ปุ่ม ไม่
        layoutBox();                                   // จัดขนาดกล่องตามความยาวข้อความ แล้ววางไว้กลางหน้าต่าง
        setVisible(true);                              // แสดงกล่อง (ทับทั้งหน้าต่าง)
        if (confirm) {                                 // กล่องยืนยัน
            noButton.requestFocusInWindow();           // ให้ปุ่ม "ไม่" ได้โฟกัส (กด Enter = ไม่ กันยกเลิกพลาด)
        } else {                                       // กล่องแจ้งข้อความ
            okButton.requestFocusInWindow();           // ให้ปุ่ม OK ได้โฟกัส (กด Enter = OK)
        }                                              // จบ if
    }                                                  // จบ open

    private void close() {                             // ปิดกล่อง
        setVisible(false);                             // ซ่อนแผ่นทับ → กดหน้าข้างใต้ได้ตามปกติ
    }                                                  // จบ close

    // คำนวณขนาดกล่องจากความยาวข้อความ แล้ววางทุกอย่างใหม่
    private void layoutBox() {
        int textW = messageText.getPreferredSize().width;      // ความกว้างข้อความ (พิกเซล)
        int titleW = titleText.getPreferredSize().width;       // ความกว้างชื่อกล่อง
        int w = Math.max(textW + 110, titleW + 90);            // กว้างพอทั้งข้อความ (+ไอคอน) และชื่อกล่อง (+ปุ่ม ✕)
        w = Math.max(w, 300);                                  // อย่างน้อย 300

        box.setBounds((getWidth() - w) / 2, (getHeight() - BOX_H) / 2, w, BOX_H);   // กลางหน้าต่าง
        Component titleBar = titleText.getParent();            // แถบหัว
        titleBar.setBounds(1, 1, w - 2, TITLE_H);              // เต็มความกว้างกล่อง
        titleText.setBounds(10, 0, w - 70, TITLE_H);           // ชื่อกล่องชิดซ้าย
        closeX.setBounds(w - 2 - 46, 0, 46, TITLE_H);          // ปุ่ม ✕ ชิดขวา
        icon.setBounds(20, TITLE_H + 18, 36, 36);              // ไอคอนด้านซ้าย
        messageText.setBounds(72, TITLE_H + 18, w - 90, 36);   // ข้อความทางขวาของไอคอน

        int y = BOX_H - 46;                                    // แถวปุ่มด้านล่าง
        okButton.setBounds(w / 2 - 34, y, 68, 28);             // ปุ่ม OK กลางกล่อง
        yesButton.setBounds(w / 2 - 74, y, 68, 28);            // ปุ่ม ใช่ ซ้ายของกึ่งกลาง
        noButton.setBounds(w / 2 + 6, y, 68, 28);              // ปุ่ม ไม่ ขวาของกึ่งกลาง
    }                                                          // จบ layoutBox

    // ปุ่มแบบกล่องข้อความ: พื้นขาว ขอบเทาเข้ม ตัวดำ
    private RoundButton createButton(String label) {
        RoundButton b = new RoundButton(label);                       // สร้างปุ่ม
        b.setFont(Theme.font(Font.PLAIN, 14));                        // ตัวปกติ 14
        b.setColors(Theme.WHITE, Color.BLACK, new Color(0x5A6B85));   // พื้นขาว ตัวดำ ขอบเทาน้ำเงิน
        b.addActionListener(this);                                    // กด → actionPerformed ของกล่องนี้
        b.addKeyListener(this);                                       // กด Enter บนปุ่ม → keyPressed
        return b;                                                     // คืนปุ่ม
    }                                                                 // จบ createButton

    // พื้นหลังมืดโปร่งแสงทับทั้งหน้าต่าง
    public void paintComponent(Graphics g) {
        g.setColor(DIM);                                   // ดำโปร่งแสง
        g.fillRect(0, 0, getWidth(), getHeight());         // ระบายเต็มหน้าต่าง
    }                                                      // จบ paintComponent

    // ================= ActionListener (ปุ่ม OK / ใช่ / ไม่) =================
    public void actionPerformed(ActionEvent e) {
        close();                                           // ปุ่มไหนก็ตาม → ปิดกล่องก่อน
        if (e.getSource() == yesButton && yesListener != null) {   // กด "ใช่" และมีคนรอฟัง
            ActionListener l = yesListener;                // คนที่รอฟัง
            yesListener = null;                            // ล้างทิ้ง (ใช้ครั้งเดียว)
            l.actionPerformed(e);                          // แจ้งหน้านั้น (e.getSource() = ปุ่ม "ใช่")
        }                                                  // จบ if
    }                                                      // จบ actionPerformed

    // ================= KeyListener (Enter บนปุ่ม) =================
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {          // กด Enter
            ((RoundButton) e.getSource()).doClick();        // เหมือนคลิกปุ่มที่มีโฟกัสอยู่
        }                                                   // จบ if
    }                                                       // จบ keyPressed
    public void keyTyped(KeyEvent e) {}                     // ไม่ใช้ (ต้องเขียนให้ครบตาม interface)
    public void keyReleased(KeyEvent e) {}                  // ไม่ใช้

    // ================= MouseListener (ปุ่ม ✕ และพื้นหลัง) =================
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == closeX) {                      // คลิกปุ่ม ✕
            close();                                        // ปิดกล่อง (กล่องยืนยัน = เหมือนกด "ไม่")
        }                                                   // จบ if (คลิกพื้นหลัง = ไม่ทำอะไร)
    }                                                       // จบ mouseClicked
    public void mouseEntered(MouseEvent e) {
        if (e.getSource() == closeX) {                      // เอาเมาส์วางบนปุ่ม ✕
            closeX.setOpaque(true);                         // ระบายพื้นแดง
            closeX.setIcon(closeWhite);                     // ✕ สีขาว
            closeX.repaint();                               // แสดงใหม่
        }                                                   // จบ if
    }                                                       // จบ mouseEntered
    public void mouseExited(MouseEvent e) {
        if (e.getSource() == closeX) {                      // เอาเมาส์ออกจากปุ่ม ✕
            closeX.setOpaque(false);                        // ไม่ระบายพื้น
            closeX.setIcon(closeBlack);                     // ✕ สีดำ
            closeX.repaint();                               // แสดงใหม่
        }                                                   // จบ if
    }                                                       // จบ mouseExited
    public void mousePressed(MouseEvent e) {}               // ไม่ใช้
    public void mouseReleased(MouseEvent e) {}              // ไม่ใช้

    // แปลง Image เป็น ImageIcon สำหรับใส่ใน JLabel (รูปหายไม่เจอ → null = ป้ายว่าง)
    private static ImageIcon toIcon(Image img) {
        if (img == null) {                 // หารูปไม่เจอ
            return null;                   // ไม่มีรูป
        }                                  // จบ if
        return new ImageIcon(img);         // ห่อเป็น ImageIcon
    }                                      // จบ toIcon
}                                                          // จบคลาส MessagePanel
