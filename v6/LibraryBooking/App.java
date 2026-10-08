import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

/**
 * จุดเริ่มโปรแกรม และตัวควบคุมการเปลี่ยนหน้า
 *
 *   Login --สมัครสมาชิก--> Register --สมัครสำเร็จ/กลับ--> Login --เข้าสู่ระบบสำเร็จ--> Home
 *   Home --ออกจากระบบ--> Login
 *   ถ้า role = ADMIN จะไปหน้า AdminHome แทน Home
 *
 * วิธีรัน (ในโฟลเดอร์ที่มี Member.csv และโฟลเดอร์ images):
 *   javac -encoding UTF-8 *.java
 *   java -ea App
 */
public class App {

    static final String MEMBER_FILE = "Member.csv";

    private final MemberRepository repository;
    private final AuthService authService;
    private final RegistrationService registrationService;

    App(MemberRepository repository) {
        this.repository = repository;
        this.authService = new AuthService(repository);
        this.registrationService = new RegistrationService(repository);
    }

    public static void main(String[] args) {
        // Swing ไม่ thread-safe : ทุกอย่างที่แตะหน้าจอต้องทำบน Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                new App(new CsvMemberRepository(MEMBER_FILE)).showLogin(null);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "อ่านไฟล์ " + MEMBER_FILE + " ไม่ได้\n" + e.getMessage(),
                        "Library Booking System", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    void showLogin(String prefillUsername) {
        LoginForm login = new LoginForm(authService, prefillUsername);
        login.setOnLoginSuccess(member -> {
            login.dispose();
            if (member.getRole() == Role.ADMIN) {
                showAdminHome(member);     // ผู้ดูแลระบบ
            } else {
                showHome(member);          // สมาชิกทั่วไป
            }
        });
        login.setOnRegisterRequested(() -> {
            login.dispose();
            showRegister();
        });
        login.open();
    }

    void showRegister() {
        RegisterForm register = new RegisterForm(registrationService);
        register.setOnRegistered(member -> {
            register.dispose();
            showLogin(member.getUsername()); // สมัครเสร็จ -> กลับหน้า login พร้อมกรอกชื่อไว้ให้
        });
        register.setOnBack(() -> {
            register.dispose();
            showLogin(null);
        });
        register.open();
    }

    void showHome(Member member) {
        HomeForm home = new HomeForm(member);
        home.setOnLogout(() -> {
            home.dispose();
            showLogin(null);
        });
        home.open();
    }

    void showAdminHome(Member admin) {
        // หน้า Admin ยังกดอะไรไม่ได้ ปิดหน้าต่าง = ปิดโปรแกรม
        new AdminHomeForm(admin, repository.count(), sampleRooms()).open();
    }

    /** ห้อง A101-A105 และ B101-B105 ห้องละ 6 คน — ระบบจองยังไม่ได้ทำ ทุกห้องจึงว่าง */
    static List<Room> sampleRooms() {
        List<Room> rooms = new ArrayList<>();
        for (String zone : new String[]{"A", "B"}) {
            for (int n = 1; n <= 5; n++) {
                rooms.add(new Room(zone + "10" + n, 6, true));
            }
        }
        return rooms;
    }
}
