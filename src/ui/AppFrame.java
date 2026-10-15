package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;         // Container
import java.awt.event.*;   // ActionListener, ActionEvent, MouseListener, MouseEvent
import javax.swing.*;      // JFrame, JPanel, UIManager
import java.util.ArrayList;   // ลิสต์ห้อง / ลิสต์การจอง ที่อ่านจากไฟล์ csv

/*
 * หน้าต่างหลักของโปรแกรม (1440 x 900)
 * วางทุกหน้าไว้ใน content pane ด้วย setBounds แล้วสลับหน้าด้วย setVisible(true/false)
 *
 * Event (สไลด์บทที่ 10-11):
 *   - ปุ่ม (JButton)         -> ActionListener  แยกด้วย e.getSource()
 *   - เมนู / กล่องเมนูลัด    -> MouseListener   แยกด้วย e.getSource()
 * ข้อมูลทั้งหมดอ่าน/บันทึกด้วยไฟล์ csv ในโฟลเดอร์ data (users / admins / rooms / bookings)
 */
public class AppFrame extends JFrame implements ActionListener, MouseListener {   // เป็นหน้าต่าง + ฟัง event ปุ่มและเมาส์เอง

    private Container cp;                    // content pane ของหน้าต่าง (ที่วาง component ทั้งหมด)
    private String currentStudentId = "";             // รหัสนิสิตของผู้ที่ล็อกอินอยู่ (setCurrentUser ใส่ให้ตอนเข้าสู่ระบบสำเร็จ)
    private String selectedRoomId = null;             // ห้องที่กด "เลือกห้องนี้" มาจากหน้าดูห้องทั้งหมด (null = ยังไม่ได้เลือก)

    // หน้าก่อนเข้าระบบ
    private LoginPage loginPage;             // หน้าเข้าสู่ระบบ
    private RegisterPage registerPage;       // หน้าสมัครสมาชิก

    // ส่วนประกอบหลังเข้าระบบ
    private SidebarPanel sidebar;            // แถบเมนูซ้าย
    private HeaderPanel header;              // แถบด้านบน
    private HomePage homePage;               // หน้าหลัก
    private AllRoomsPage allRoomsPage;       // หน้าดูห้องทั้งหมด
    private BookRoomPage bookRoomPage;       // หน้าจองห้อง
    private BookingSuccessPage successPage;  // หน้าจองสำเร็จ
    private ProfilePage profilePage;         // หน้าข้อมูลส่วนตัว
    private MyBookingsPage myBookingsPage;   // หน้าการจองของฉัน
    private AdminPage adminPage;             // หน้าหลักของผู้ดูแลระบบ (เต็มหน้าต่าง)
    private AdminBookingsPage adminBookingsPage;   // หน้ารายการจองทั้งหมดของผู้ดูแลระบบ

    // โครงสร้างฟอร์มแบบเดียวกับสไลด์บทที่ 9: Initial() -> setComponent() -> Finally()
    public AppFrame() {                      // คอนสตรักเตอร์
        super("Library Booking System");     // เรียกคอนสตรักเตอร์ของ JFrame พร้อมตั้งชื่อหน้าต่าง
        setThaiFont();                       // ตั้งฟอนต์ภาษาไทยให้กล่องข้อความ (ต้องทำก่อนสร้าง component)
        Initial();                           // ขั้นที่ 1: เตรียม content pane
        setComponent();                      // ขั้นที่ 2: สร้างและวาง component
        Finally();                           // ขั้นที่ 3: ตั้งค่าหน้าต่างแล้วแสดงผล
    }                                        // จบคอนสตรักเตอร์

    // ตั้งฟอนต์ Tahoma (มีตัวอักษรไทย) ให้ส่วนที่เราไม่ได้ setFont เอง โดยเฉพาะกล่อง JOptionPane
    // ถ้าไม่ตั้ง บน Windows ข้อความไทยในกล่องข้อความจะขึ้นเป็นสี่เหลี่ยม □□□
    private void setThaiFont() {
        Font thai = Theme.font(Font.PLAIN, 14);              // ฟอนต์ Tahoma ขนาด 14
        UIManager.put("OptionPane.messageFont", thai);       // ข้อความในกล่อง JOptionPane
        UIManager.put("OptionPane.buttonFont", thai);        // ปุ่มในกล่อง (OK / ใช่ / ไม่)
        UIManager.put("Label.font", thai);                   // JLabel ที่ไม่ได้ตั้งฟอนต์เอง
        UIManager.put("Button.font", thai);                  // JButton ที่ไม่ได้ตั้งฟอนต์เอง
        UIManager.put("ComboBox.font", thai);                // dropdown (รวมรายการที่กางออก)
        UIManager.put("TextField.font", thai);               // ช่องกรอกข้อความ
        UIManager.put("PasswordField.font", thai);           // ช่องรหัสผ่าน
    }                                                        // จบ setThaiFont

    // 1) เตรียม content pane
    public void Initial() {                                                                 // เริ่มเมธอด Initial
        cp = getContentPane();               // ขอ content pane ของ JFrame (บทที่ 8)
        cp.setLayout(null);                  // ไม่ใช้ Layout Manager — วางด้วย setBounds
        setGlassPane(new MessagePanel());    // กล่องข้อความแบบ JPanel: แผ่นใสวางทับทั้งหน้าต่าง (ซ่อนไว้จนกว่าจะเรียก MessagePanel.showMessage)
    }                                        // จบ Initial

    // 2) สร้างทุกหน้า + วางตำแหน่ง + ผูก Event
    public void setComponent() {                                                            // เริ่มเมธอด setComponent
        loginPage = new LoginPage();                         // สร้างหน้าเข้าสู่ระบบ
        registerPage = new RegisterPage();                   // สร้างหน้าสมัครสมาชิก
        sidebar = new SidebarPanel();                        // สร้างแถบเมนู
        header = new HeaderPanel("นาย วชิรศักดิ์ โยโย่");      // สร้างแถบบน พร้อมชื่อผู้ใช้ (ข้อมูลตัวอย่าง)
        homePage = new HomePage();                           // สร้างหน้าหลัก
        allRoomsPage = new AllRoomsPage();                   // สร้างหน้าดูห้องทั้งหมด
        bookRoomPage = new BookRoomPage();                   // สร้างหน้าจองห้อง
        successPage = new BookingSuccessPage();              // สร้างหน้าจองสำเร็จ
        profilePage = new ProfilePage();                     // สร้างหน้าข้อมูลส่วนตัว
        myBookingsPage = new MyBookingsPage();               // สร้างหน้าการจองของฉัน
        adminPage = new AdminPage();                         // สร้างหน้าผู้ดูแลระบบ
        adminBookingsPage = new AdminBookingsPage();         // สร้างหน้ารายการจองทั้งหมด

        loginPage.setBounds(0, 0, 1440, 860);        // เต็มหน้าต่าง
        registerPage.setBounds(0, 0, 1440, 860);     // เต็มหน้าต่าง
        sidebar.setBounds(0, 0, 268, 860);           // ชิดซ้าย กว้าง 268
        header.setBounds(268, 0, 1172, 70);          // ด้านบน ทางขวาของ sidebar
        homePage.setBounds(268, 0, 1172, 860);       // หน้าหลักไม่มี header
        allRoomsPage.setBounds(268, 70, 1172, 790);  // ใต้ header ทางขวาของ sidebar
        bookRoomPage.setBounds(268, 70, 1172, 790);  // ตำแหน่งเดียวกัน (แสดงทีละหน้า)
        successPage.setBounds(268, 70, 1172, 790);                                          // วางหน้าจองสำเร็จ ที่ x=268 y=70 กว้าง 1172 สูง 790
        profilePage.setBounds(268, 70, 1172, 790);                                          // วางหน้าข้อมูลส่วนตัว ที่ x=268 y=70 กว้าง 1172 สูง 790
        myBookingsPage.setBounds(268, 70, 1172, 790);                                       // วางหน้าการจองของฉัน ที่ตำแหน่งเดียวกับหน้าอื่น
        adminPage.setBounds(0, 0, 1440, 860);                                               // หน้า Admin เต็มหน้าต่าง (มี sidebar ของตัวเอง)
        adminBookingsPage.setBounds(0, 0, 1440, 860);                                       // เต็มหน้าต่างเหมือนหน้า Admin

        cp.add(loginPage);          // ใส่ทุกหน้าลงใน content pane ตั้งแต่แรก
        cp.add(registerPage);       // (ซ้อนกันอยู่ แต่จะแสดงแค่บางหน้าด้วย setVisible)
        cp.add(sidebar);                                                                    // ใส่แถบเมนูซ้ายลงใน content pane
        cp.add(header);                                                                     // ใส่แถบด้านบนลงใน content pane
        cp.add(homePage);                                                                   // ใส่หน้าหลักลงใน content pane
        cp.add(allRoomsPage);                                                               // ใส่หน้าดูห้องทั้งหมดลงใน content pane
        cp.add(bookRoomPage);                                                               // ใส่หน้าจองห้องลงใน content pane
        cp.add(successPage);                                                                // ใส่หน้าจองสำเร็จลงใน content pane
        cp.add(profilePage);                                                                // ใส่หน้าข้อมูลส่วนตัวลงใน content pane
        cp.add(myBookingsPage);                                                             // ใส่หน้าการจองของฉันลงใน content pane
        cp.add(adminPage);                                                                  // ใส่หน้าผู้ดูแลระบบลงใน content pane
        cp.add(adminBookingsPage);                                                          // ใส่หน้ารายการจองทั้งหมดลงใน content pane

        // ---------- ผูก Event ----------
        loginPage.getLoginButton().addActionListener(this);         // ปุ่มเข้าสู่ระบบ → actionPerformed ของคลาสนี้ (this)
        loginPage.getRegisterButton().addActionListener(this);      // ปุ่มสมัครสมาชิก (หน้า Login)
        registerPage.getRegisterButton().addActionListener(this);   // ปุ่มสมัครสมาชิก (หน้าสมัคร)
        registerPage.getBackButton().addActionListener(this);       // ปุ่มกลับไปหน้าเข้าสู่ระบบ

        for (int i = 0; i < allRoomsPage.getCards().length; i++) {               // วนทุกการ์ดในหน้าดูห้องทั้งหมด
            allRoomsPage.getCards()[i].getSelectButton().addActionListener(this); // ผูกปุ่ม "เลือกห้องนี้" ของการ์ดที่ i
        }                                                                         // จบ for
        for (int i = 0; i < bookRoomPage.getRoomCards().length; i++) {               // วนทุกการ์ดในหน้าจองห้อง
            bookRoomPage.getRoomCards()[i].getSelectButton().addActionListener(this); // ผูกปุ่ม "เลือกห้องนี้" ของการ์ดที่ i
        }                                                                             // จบ for

        sidebar.getHomeItem().addMouseListener(this);       // คลิกเมนูหน้าหลัก → mouseClicked
        sidebar.getAllRoomsItem().addMouseListener(this);   // คลิกเมนูดูห้องทั้งหมด
        sidebar.getBookRoomItem().addMouseListener(this);   // คลิกเมนูจองห้อง
        sidebar.getProfileItem().addMouseListener(this);    // คลิกเมนูข้อมูลส่วนตัว
        sidebar.getMyBookingsItem().addMouseListener(this); // คลิกเมนูการจองของฉัน

        homePage.getBookRoomMenu().getTile().addMouseListener(this);   // คลิกกล่อง "จองห้อง" ในหน้าหลัก
        homePage.getAllRoomsMenu().getTile().addMouseListener(this);   // คลิกกล่อง "ดูห้องทั้งหมด"
        homePage.getProfileMenu().getTile().addMouseListener(this);    // คลิกกล่อง "โปรไฟล์"
        homePage.getMyBookingsMenu().getTile().addMouseListener(this); // คลิกกล่อง "การจองของฉัน"
        adminPage.getSeeAll().addMouseListener(this);                  // คลิก "ดูทั้งหมด" ในหน้า Admin
        adminPage.getSeeAllArrow().addMouseListener(this);             // คลิกลูกศรต่อท้าย "ดูทั้งหมด"
        adminBookingsPage.getSidebar().getHomeItem().addMouseListener(this);   // คลิกเมนู "หน้าหลัก" ในหน้ารายการจองทั้งหมด

        showLogin();   // เริ่มที่หน้า Login
    }                  // จบ setComponent

    // 3) ตั้งค่าหน้าต่างแล้วแสดงผล
    public void Finally() {                                                                 // เริ่มเมธอด Finally
        setSize(1440, 900);                              // ขนาดหน้าต่าง
        setResizable(false);                             // ห้ามปรับขนาด (เพราะวางตำแหน่งตายตัวด้วย setBounds)
        setLocationRelativeTo(null);                     // ให้หน้าต่างอยู่กลางจอ
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // กด X แล้วปิดโปรแกรม
        setVisible(true);                                // แสดงหน้าต่าง

        // ให้ปุ่มเข้าสู่ระบบได้โฟกัสก่อน ช่องกรอกจะยังแสดงข้อความจางอยู่
        loginPage.getLoginButton().requestFocusInWindow();                                  // ให้ปุ่มได้โฟกัส ช่องกรอกจึงยังแสดงข้อความจาง
    }                                                    // จบ Finally

    // ซ่อนทุกหน้า
    private void hideAll() {                                                                // เมธอดซ่อนทุกหน้า
        loginPage.setVisible(false);       // ซ่อนทีละหน้า
        registerPage.setVisible(false);                                                     // ซ่อนหน้าสมัครสมาชิก
        sidebar.setVisible(false);                                                          // ซ่อนแถบเมนูซ้าย
        header.setVisible(false);                                                           // ซ่อนแถบด้านบน
        homePage.setVisible(false);                                                         // ซ่อนหน้าหลัก
        allRoomsPage.setVisible(false);                                                     // ซ่อนหน้าดูห้องทั้งหมด
        bookRoomPage.setVisible(false);                                                     // ซ่อนหน้าจองห้อง
        successPage.setVisible(false);                                                      // ซ่อนหน้าจองสำเร็จ
        profilePage.setVisible(false);                                                      // ซ่อนหน้าข้อมูลส่วนตัว
        myBookingsPage.setVisible(false);                                                   // ซ่อนหน้าการจองของฉัน
        adminPage.setVisible(false);                                                        // ซ่อนหน้าผู้ดูแลระบบ
        adminBookingsPage.setVisible(false);                                                // ซ่อนหน้ารายการจองทั้งหมด
    }                                      // จบ hideAll

    public void showLogin() {              // แสดงหน้าเข้าสู่ระบบ
        hideAll();                         // ซ่อนทุกหน้าก่อน
        loginPage.setVisible(true);        // แสดงหน้า Login หน้าเดียว
        setTitle("Login");                 // เปลี่ยนชื่อหน้าต่าง
        loginPage.getLoginButton().requestFocusInWindow();         // ให้ปุ่มได้โฟกัส ช่องกรอกจะยังแสดงข้อความจาง
    }                                      // จบ showLogin

    public void showRegister() {           // แสดงหน้าสมัครสมาชิก
        hideAll();                         // ซ่อนทุกหน้าก่อน
        registerPage.setVisible(true);     // แสดงหน้าสมัครสมาชิก
        setTitle("Sign Up");               // เปลี่ยนชื่อหน้าต่าง
        registerPage.getRegisterButton().requestFocusInWindow();   // ให้ปุ่มได้โฟกัส ช่องกรอกจะยังแสดงข้อความจาง
    }                                      // จบ showRegister

    public void showAdmin() {              // แสดงหน้าผู้ดูแลระบบ
        hideAll();                         // ซ่อนทุกหน้าก่อน
        adminPage.refreshLatest();         // อ่าน bookings.csv ใหม่ แล้วอัปเดตตาราง "การจองล่าสุด"
        adminPage.refreshRoomStatus();     // อ่าน bookings.csv ใหม่ แล้วตั้งสถานะห้องตามเวลาตอนนี้
        adminPage.refreshStats();          // อ่านไฟล์ csv ใหม่ แล้วอัปเดตการ์ดสรุป (ห้องทั้งหมด / ว่าง / ไม่ว่าง / ผู้ใช้)
        adminPage.setVisible(true);        // แสดงหน้า Admin (มี sidebar และแถบบนของตัวเองอยู่แล้ว)
        setTitle("Admin Dashboard");       // เปลี่ยนชื่อหน้าต่าง
    }                                      // จบ showAdmin

    public void showAdminBookings() {      // แสดงหน้ารายการจองทั้งหมดของผู้ดูแลระบบ
        hideAll();                         // ซ่อนทุกหน้าก่อน
        adminBookingsPage.refresh();       // อ่าน bookings.csv ใหม่ แล้วแสดงหน้า 1 ของรายการทั้งหมด
        adminBookingsPage.setVisible(true); // แสดงหน้ารายการจองทั้งหมด
        setTitle("All Bookings");          // เปลี่ยนชื่อหน้าต่าง
    }                                      // จบ showAdminBookings

    // แสดงหน้าหลังเข้าระบบ + ไฮไลต์เมนูที่ตรงกัน
    public void showPage(JPanel page, SidebarItem menu, String title) {   // page = หน้าที่จะแสดง, menu = เมนูที่จะไฮไลต์, title = ชื่อหน้าต่าง
        hideAll();                         // ซ่อนทุกหน้าก่อน
        sidebar.setVisible(true);          // แสดงแถบเมนู
        sidebar.setActive(menu);           // ไฮไลต์เมนูที่ตรงกับหน้า
        if (page != homePage) {            // ทุกหน้ายกเว้นหน้าหลัก
            header.setVisible(true);       // แสดงแถบบน
        }                                  // จบ if
        if (page == allRoomsPage) {                                       // ถ้าจะแสดงหน้าดูห้องทั้งหมด
            allRoomsPage.refresh(RoomService.loadFreeRooms());            // อ่านไฟล์ใหม่ แล้วแสดงเฉพาะห้องที่ยังไม่ถูกจองวันนี้
        }                                                                 // จบ if
        if (page == bookRoomPage) {                                       // ถ้าจะแสดงหน้าจองห้อง
            bookRoomPage.getDateField().setText(BookingService.today());  // ตั้งช่องวันที่เป็นวันนี้ (dd/MM/yyyy)
            ArrayList<Room> choices = new ArrayList<>();                  // ห้องที่จะใส่ใน dropdown "เลือกห้อง"
            if (selectedRoomId != null) {                                 // ถ้ากดเลือกห้องมาจากหน้าดูห้องทั้งหมด
                Room room = RoomService.findRoom(RoomService.loadRooms(), selectedRoomId);   // หาข้อมูลห้องนั้นใน rooms.csv
                if (room != null) {                                       // ถ้าเจอ
                    choices.add(room);                                    // dropdown มีแค่ห้องนี้ห้องเดียว
                }                                                         // จบ if
            } else {                                                      // ถ้าเข้ามาจากเมนู (ยังไม่ได้เลือกห้อง)
                choices = RoomService.loadRooms();                        // ตั้งต้นด้วยทุกห้อง (หน้าจองจะกรองเหลือห้องที่ว่างในวัน/เวลาที่เลือก)
            }                                                             // จบ if
            bookRoomPage.setRoomChoices(choices);                         // ใส่ตัวเลือกลง dropdown + แสดงการ์ดห้องที่เลือก
            selectedRoomId = null;                                        // ใช้แล้วล้างทิ้ง (ครั้งหน้าที่เข้าจากเมนูจะได้ไม่ค้าง)
        }                                                                 // จบ if
        if (page == myBookingsPage) {                    // ถ้าจะแสดงหน้าการจองของฉัน
            myBookingsPage.refresh(currentStudentId);    // อ่าน bookings.csv ใหม่ แล้วสร้างตารางของนิสิตคนนี้
        }                                                // จบ if
        if (page == homePage) {                          // ถ้าจะแสดงหน้าหลัก
            homePage.refreshStats(currentStudentId);     // อ่านไฟล์ csv ใหม่ แล้วอัปเดตตัวเลขในการ์ด
        }                                                // จบ if
        page.setVisible(true);             // แสดงหน้าที่ต้องการ
        setTitle(title);                   // เปลี่ยนชื่อหน้าต่าง
    }                                      // จบ showPage

    // จองห้อง: อ่านค่าจากฟอร์ม → ตรวจกฎ → บันทึกลง bookings.csv → แสดงหน้าจองสำเร็จ
    private void bookRoom(String roomId) {
        String date = bookRoomPage.getDateField().getText().trim();               // วันที่ที่กรอก
        String start = (String) bookRoomPage.getStartTimeBox().getSelectedItem(); // เวลาเริ่มที่เลือก (getSelectedItem คืน Object จึงต้องแปลงเป็น String)
        String end = (String) bookRoomPage.getEndTimeBox().getSelectedItem();     // เวลาสิ้นสุดที่เลือก

        try {                                                                   // ให้ logic จอง (ตรวจกฎ + บันทึกไฟล์)
            Booking booking = BookingService.book(currentStudentId, roomId, date, start, end);   // จองสำเร็จ → ได้การจองที่บันทึกแล้ว
            successPage.showBooking(booking);                                   // ใส่ข้อมูลจริงในหน้าจองสำเร็จ
            showPage(successPage, sidebar.getBookRoomItem(), "Booking Success");   // → ไปหน้าจองสำเร็จ
        } catch (IllegalArgumentException ex) {                                 // ผิดกฎ / บันทึกไม่ได้
            showError(ex.getMessage());                                         // แจ้งเหตุผลที่ logic ส่งมา
        }                                                                       // จบ try-catch
    }                                                                           // จบ bookRoom

    // สมัครสมาชิก: อ่านค่าจากฟอร์ม → ตรวจ → บันทึกลง users.csv → กล่อง "สมัครสำเร็จ" → กลับหน้า Login
    private void register() {
        String id = registerPage.getUsernameBox().getText().trim();          // รหัสนิสิต
        String name = registerPage.getFullNameBox().getText().trim();        // ชื่อ-นามสกุล
        String email = registerPage.getEmailBox().getText().trim();          // อีเมล
        String phone = registerPage.getPhoneBox().getText().trim();          // เบอร์โทร
        String password = registerPage.getPasswordBox().getText();           // รหัสผ่าน
        String confirm = registerPage.getConfirmPasswordBox().getText();     // ยืนยันรหัสผ่าน

        String error = UserService.register(id, name, email, phone, password, confirm);   // logic ตรวจข้อมูล + บันทึกลง users.csv
        if (error != null) {                                                 // ไม่สำเร็จ
            MessagePanel.showMessage(this, error, "สมัครไม่สำเร็จ", MessagePanel.ERROR);   // กล่องแจ้งข้อผิดพลาด (ไอคอนแดงกากบาท)
            return;                                                          // หยุด
        }                                                                    // จบ if

        registerPage.getUsernameBox().clear();                               // ล้างฟอร์มทุกช่อง (กลับเป็นข้อความจาง)
        registerPage.getFullNameBox().clear();
        registerPage.getEmailBox().clear();
        registerPage.getPhoneBox().clear();
        registerPage.getPasswordBox().clear();
        registerPage.getConfirmPasswordBox().clear();
        showLogin();                                                         // กลับไปหน้าเข้าสู่ระบบ
        MessagePanel.showMessage(this, "สมัครสำเร็จ", "สมัครสมาชิก", MessagePanel.INFO);   // กล่องแจ้งข้อมูล (ไอคอนฟ้าตัว i) ทับหน้าเข้าสู่ระบบ
    }                                                                        // จบ register

    // ใส่ข้อมูลผู้ใช้ที่เข้าสู่ระบบลงในหน้าต่าง ๆ (แถบบน, หน้าหลัก, ข้อมูลส่วนตัว)
    private void setCurrentUser(User user) {
        currentStudentId = user.getStudentId();                                      // จำรหัสไว้ (ใช้จอง / ดูการจองของฉัน)
        header.getNameLabel().setText(user.getFullName());                           // ชื่อมุมขวาบน
        homePage.getNameLabel().setText("สวัสดี " + user.getFullName());              // ข้อความต้อนรับในหน้าหลัก
        homePage.getStudentIdLabel().setText("รหัสนักศึกษา   " + user.getStudentId());  // รหัสนักศึกษาในหน้าหลัก
        profilePage.getFullNameValue().setText(user.getFullName());                  // ชื่อในหน้าข้อมูลส่วนตัว
        profilePage.getStudentIdValue().setText(user.getStudentId());                // รหัสในหน้าข้อมูลส่วนตัว
    }                                                                                // จบ setCurrentUser

    // กล่องข้อความแจ้งเหตุผลที่จองไม่สำเร็จ
    private void showError(String message) {
        MessagePanel.showMessage(this, message, "จองไม่สำเร็จ", MessagePanel.ERROR);   // กล่องแจ้งข้อผิดพลาด (ไอคอนแดงกากบาท)
    }                                                                                              // จบ showError

    // ================= ActionListener (ปุ่ม) =================
    public void actionPerformed(ActionEvent e) {                        // ทำงานเมื่อกดปุ่มใดก็ได้ที่ผูกไว้
        if (e.getSource() == loginPage.getLoginButton()) {              // ถ้ากดปุ่ม "เข้าสู่ระบบ"
            String id = loginPage.getStudentIdBox().getText().trim();         // รหัสนิสิตที่กรอก
            String password = loginPage.getPasswordBox().getText();           // รหัสผ่านที่กรอก
            if (AdminService.isAdminId(id)) {                                 // ถ้าเป็นรหัสของ admin (มีใน admins.csv)
                String name = AdminService.login(id, password);               // ตรวจรหัสผ่านกับไฟล์ (ถูก = ได้ชื่อ, ผิด = null)
                if (name == null) {                                           // รหัสผ่านไม่ถูกต้อง
                    MessagePanel.showMessage(this, "รหัสผ่านของผู้ดูแลระบบไม่ถูกต้อง", "เข้าสู่ระบบไม่สำเร็จ", MessagePanel.ERROR);   // กล่องแจ้งข้อผิดพลาด (ไอคอนแดงกากบาท)
                } else {                                                      // รหัส + รหัสผ่านถูกต้อง
                    adminPage.getAdminName().setText(name);                   // ชื่อ admin มุมขวาบน
                    showAdmin();                                              // → ไปหน้าผู้ดูแลระบบ
                }                                                             // จบ if
            } else {                                                          // ไม่ใช่รหัส admin → ตรวจกับ users.csv
                ArrayList<User> users = UserService.loadUsers();              // อ่านผู้ใช้ทั้งหมดจากไฟล์
                String error = UserService.validateLogin(users, id, password);   // ตรวจรหัสนิสิต + รหัสผ่าน
                if (error != null) {                                          // ไม่ผ่าน
                    MessagePanel.showMessage(this, error, "เข้าสู่ระบบไม่สำเร็จ", MessagePanel.ERROR);   // กล่องแจ้งข้อผิดพลาด (ไอคอนแดงกากบาท)
                } else {                                                      // ผ่าน
                    setCurrentUser(UserService.findUser(users, id));          // ใส่ชื่อ/รหัสของผู้ใช้คนนี้ในทุกหน้า
                    loginPage.getPasswordBox().clear();                       // ล้างช่องรหัสผ่าน
                    showPage(homePage, sidebar.getHomeItem(), "Home");        // → ไปหน้าหลักของนักศึกษา
                }                                                             // จบ if
            }                                                                 // จบ if
        } else if (e.getSource() == loginPage.getRegisterButton()) {    // ถ้ากด "สมัครสมาชิก" ในหน้า Login
            showRegister();                                             // → ไปหน้าสมัครสมาชิก
        } else if (e.getSource() == registerPage.getBackButton()) {     // ถ้ากด "กลับไปหน้าเข้าสู่ระบบ"
            showLogin();                                                // → กลับหน้า Login
        } else if (e.getSource() == registerPage.getRegisterButton()) { // ถ้ากด "สมัครสมาชิก" ในหน้าสมัคร
            register();                                                 // ตรวจข้อมูล → บันทึกลง users.csv
        } else {                                                        // ถ้าไม่ใช่ปุ่มข้างบน ให้เช็กปุ่มในการ์ดห้อง
            // ปุ่ม "เลือกห้องนี้" ในหน้าดูห้องทั้งหมด -> หน้าจองห้อง
            for (int i = 0; i < allRoomsPage.getCards().length; i++) {                  // วนทุกการ์ด
                if (e.getSource() == allRoomsPage.getCards()[i].getSelectButton()) {    // ถ้าเป็นปุ่มของการ์ดที่ i
                    selectedRoomId = allRoomsPage.getCards()[i].getRoomName();          // จำห้องที่เลือกไว้
                    showPage(bookRoomPage, sidebar.getBookRoomItem(), "Booking");       // → ไปหน้าจองห้อง (dropdown จะมีแค่ห้องนี้)
                }                                                                       // จบ if
            }                                                                           // จบ for
            // ปุ่ม "เลือกห้องนี้" ในหน้าจองห้อง -> ตรวจกฎ + บันทึก -> หน้าจองสำเร็จ
            for (int i = 0; i < bookRoomPage.getRoomCards().length; i++) {                      // วนทุกการ์ด
                if (e.getSource() == bookRoomPage.getRoomCards()[i].getSelectButton()) {        // ถ้าเป็นปุ่มของการ์ดที่ i
                    bookRoom(bookRoomPage.getRoomCards()[i].getRoomName());                     // จองห้องของการ์ดนี้
                }                                                                               // จบ if
            }                                                                                   // จบ for
        }                                                                                       // จบ if-else
    }                                                                                           // จบ actionPerformed

    // ================= MouseListener (เมนู / กล่องเมนูลัด) =================
    public void mouseClicked(MouseEvent e) {                                    // ทำงานเมื่อคลิก (กดแล้วปล่อยที่เดิม)
        if (e.getSource() == adminPage.getSeeAll()                              // ถ้าคลิก "ดูทั้งหมด" ในหน้า Admin
                || e.getSource() == adminPage.getSeeAllArrow()) {               // หรือ ลูกศรต่อท้าย
            showAdminBookings();                                                // → ไปหน้ารายการจองทั้งหมด
            return;                                                             // จบเมธอด ไม่ต้องเช็กต่อ
        }                                                                       // จบ if
        if (e.getSource() == adminBookingsPage.getSidebar().getHomeItem()) {    // ถ้าคลิกเมนูหน้าหลักของ Admin
            showAdmin();                                                        // → กลับหน้า Admin
            return;                                                             // จบเมธอด ไม่ต้องเช็กต่อ
        }                                                                       // จบ if
        if (e.getSource() == sidebar.getHomeItem()) {                           // ถ้าคลิกเมนูหน้าหลัก
            showPage(homePage, sidebar.getHomeItem(), "Home");                  // → ไปหน้าหลัก
        } else if (e.getSource() == sidebar.getAllRoomsItem()                   // ถ้าคลิกเมนูดูห้องทั้งหมด
                || e.getSource() == homePage.getAllRoomsMenu().getTile()) {     // หรือ กล่องดูห้องทั้งหมดในหน้าหลัก (|| = หรือ)
            showPage(allRoomsPage, sidebar.getAllRoomsItem(), "All Rooms");     // → ไปหน้าดูห้องทั้งหมด
        } else if (e.getSource() == sidebar.getBookRoomItem()                   // ถ้าคลิกเมนูจองห้อง
                || e.getSource() == homePage.getBookRoomMenu().getTile()) {     // หรือ กล่องจองห้องในหน้าหลัก
            showPage(bookRoomPage, sidebar.getBookRoomItem(), "Booking");       // → ไปหน้าจองห้อง
        } else if (e.getSource() == sidebar.getMyBookingsItem()                 // ถ้าคลิกเมนูการจองของฉัน
                || e.getSource() == homePage.getMyBookingsMenu().getTile()) {   // หรือ กล่องการจองของฉันในหน้าหลัก
            showPage(myBookingsPage, sidebar.getMyBookingsItem(), "My Bookings");   // → ไปหน้าการจองของฉัน
        } else if (e.getSource() == sidebar.getProfileItem()                    // ถ้าคลิกเมนูข้อมูลส่วนตัว
                || e.getSource() == homePage.getProfileMenu().getTile()) {      // หรือ กล่องโปรไฟล์ในหน้าหลัก
            showPage(profilePage, sidebar.getProfileItem(), "Profile");         // → ไปหน้าข้อมูลส่วนตัว
        }                                                                       // จบ if-else
    }                                                                           // จบ mouseClicked

    // MouseListener ต้อง override ครบทุกเมธอด (ไม่ได้ใช้ ปล่อยว่าง)
    public void mousePressed(MouseEvent e) { }    // กดปุ่มเมาส์ลง
    public void mouseReleased(MouseEvent e) { }   // ปล่อยปุ่มเมาส์
    public void mouseEntered(MouseEvent e) { }    // เมาส์เข้ามาใน component
    public void mouseExited(MouseEvent e) { }     // เมาส์ออกจาก component

    // getter: เผื่อคลาสอื่น (เช่นตอนทำ logic หรือทดสอบ) ต้องเข้าถึงแต่ละหน้า
    public LoginPage getLoginPage()             { return loginPage; }                       // คืนค่า loginPage
    public RegisterPage getRegisterPage()       { return registerPage; }                    // คืนค่า registerPage
    public SidebarPanel getSidebar()            { return sidebar; }                         // คืนค่า sidebar
    public HomePage getHomePage()               { return homePage; }                        // คืนค่า homePage
    public AllRoomsPage getAllRoomsPage()       { return allRoomsPage; }                    // คืนค่า allRoomsPage
    public BookRoomPage getBookRoomPage()       { return bookRoomPage; }                    // คืนค่า bookRoomPage
    public BookingSuccessPage getSuccessPage()  { return successPage; }                     // คืนค่า successPage
    public ProfilePage getProfilePage()         { return profilePage; }                     // คืนค่า profilePage
    public MyBookingsPage getMyBookingsPage()   { return myBookingsPage; }                  // คืนค่า myBookingsPage
    public AdminPage getAdminPage()             { return adminPage; }                       // คืนค่า adminPage
    public AdminBookingsPage getAdminBookingsPage() { return adminBookingsPage; }           // คืนค่า adminBookingsPage
}                                                                       // จบคลาส AppFrame
