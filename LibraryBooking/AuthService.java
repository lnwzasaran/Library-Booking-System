/**
 * ตรรกะการเข้าสู่ระบบ — แยกออกจากหน้าจอ (Single Responsibility)
 * จึงทดสอบได้โดยไม่ต้องเปิดหน้าต่าง Swing
 */
public class AuthService {

    public static final String MSG_EMPTY = "กรุณากรอกรหัสนิสิตและรหัสผ่าน";
    public static final String MSG_WRONG = "รหัสนิสิตหรือรหัสผ่านไม่ถูกต้อง";

    private final MemberRepository repository;

    /** @param repository แหล่งข้อมูลสมาชิก ห้ามเป็น null */
    public AuthService(MemberRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }
        this.repository = repository;
    }

    /**
     * ตรวจชื่อผู้ใช้และรหัสผ่าน
     *
     * @param username ชื่อผู้ใช้ (ตัดช่องว่างหัวท้ายให้)
     * @param password รหัสผ่าน (ไม่ตัดช่องว่าง — ถือว่าเป็นส่วนหนึ่งของรหัส)
     * @return สมาชิกที่ล็อกอินสำเร็จ
     * @throws LoginException ถ้าช่องใดว่าง หรือชื่อ/รหัสไม่ถูกต้อง
     *         (ใช้ข้อความเดียวกันทั้งกรณีไม่มีผู้ใช้และรหัสผิด เพื่อไม่บอกใบ้ว่ามีบัญชีนี้อยู่)
     */
    public Member login(String username, String password) throws LoginException {
        String name = (username == null) ? "" : username.trim();
        if (name.isEmpty() || password == null || password.isEmpty()) {
            throw new LoginException(MSG_EMPTY);
        }
        Member member = repository.findByUsername(name)
                .orElseThrow(() -> new LoginException(MSG_WRONG));
        if (!member.passwordMatches(password)) {
            throw new LoginException(MSG_WRONG);
        }
        return member;
    }
}
