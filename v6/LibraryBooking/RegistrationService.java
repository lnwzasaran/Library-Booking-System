import java.io.IOException;
import java.util.regex.Pattern;

/**
 * ตรวจข้อมูลสมัครสมาชิกและบันทึกลง MemberRepository
 * แยกจากหน้าจอ (Single Responsibility) จึงทดสอบได้โดยไม่ต้องเปิด Swing
 */
public class RegistrationService {

    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 50;
    public static final int MAX_NAME_LENGTH = 100;

    private static final Pattern EMAIL = Pattern.compile("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+");
    private static final Pattern PHONE_DIGITS = Pattern.compile("0\\d{8,9}"); // 9 หรือ 10 หลัก ขึ้นต้นด้วย 0

    private final MemberRepository repository;

    public RegistrationService(MemberRepository repository) {
        if (repository == null) {
            throw new IllegalArgumentException("repository must not be null");
        }
        this.repository = repository;
    }

    /**
     * ตรวจข้อมูลทีละช่องตามลำดับบนหน้าจอ ถ้าผ่านทั้งหมดจึงบันทึก
     *
     * @return สมาชิกที่สร้างใหม่ (role = MEMBER)
     * @throws RegistrationException ข้อมูลช่องใดไม่ถูกต้อง หรือ username ซ้ำ
     * @throws IOException บันทึกไฟล์ไม่สำเร็จ
     */
    public Member register(RegistrationData data) throws RegistrationException, IOException {
        if (data == null) {
            throw new IllegalArgumentException("data must not be null");
        }
        String username = trim(data.username);
        String fullName = trim(data.fullName).replaceAll("\\s+", " ");
        String email = trim(data.email);
        String phone = trim(data.phone).replaceAll("[\\s-]", "");
        String password = (data.password == null) ? "" : data.password;
        String confirm = (data.confirmPassword == null) ? "" : data.confirmPassword;

        if (username.isEmpty()) {
            throw error(RegistrationException.Field.USERNAME, "กรุณากรอก Username หรือรหัสนิสิต");
        }
        if (!username.matches(Member.USERNAME_PATTERN)) {
            throw error(RegistrationException.Field.USERNAME,
                    "Username ต้องเป็น a-z, 0-9 หรือ _ ยาว 3-20 ตัว");
        }
        if (repository.exists(username)) {
            throw error(RegistrationException.Field.USERNAME, "Username นี้ถูกใช้แล้ว");
        }
        if (fullName.isEmpty()) {
            throw error(RegistrationException.Field.FULL_NAME, "กรุณากรอกชื่อ - นามสกุล");
        }
        if (fullName.length() > MAX_NAME_LENGTH || fullName.contains(",")) {
            throw error(RegistrationException.Field.FULL_NAME,
                    "ชื่อยาวไม่เกิน " + MAX_NAME_LENGTH + " ตัว และห้ามมีเครื่องหมาย ,");
        }
        if (!EMAIL.matcher(email).matches()) {
            throw error(RegistrationException.Field.EMAIL, "รูปแบบอีเมลไม่ถูกต้อง");
        }
        if (!PHONE_DIGITS.matcher(phone).matches()) {
            throw error(RegistrationException.Field.PHONE, "เบอร์โทรศัพท์ต้องเป็นตัวเลข 9-10 หลัก ขึ้นต้นด้วย 0");
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw error(RegistrationException.Field.PASSWORD,
                    "รหัสผ่านต้องมีอย่างน้อย " + MIN_PASSWORD_LENGTH + " ตัวอักษร");
        }
        if (password.length() > MAX_PASSWORD_LENGTH || password.contains(",")
                || !password.equals(password.trim())) {
            throw error(RegistrationException.Field.PASSWORD,
                    "รหัสผ่านยาวไม่เกิน " + MAX_PASSWORD_LENGTH
                            + " ตัว ห้ามมี , และห้ามขึ้นต้นหรือลงท้ายด้วยช่องว่าง");
        }
        if (!password.equals(confirm)) {
            throw error(RegistrationException.Field.CONFIRM_PASSWORD, "ยืนยันรหัสผ่านไม่ตรงกัน");
        }

        Member member = new Member(username, password, fullName, email, phone, Role.MEMBER);
        repository.save(member);
        return member;
    }

    private static String trim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private static RegistrationException error(RegistrationException.Field field, String message) {
        return new RegistrationException(field, message);
    }
}
