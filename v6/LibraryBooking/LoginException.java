/**
 * เกิดเมื่อเข้าสู่ระบบไม่สำเร็จ ข้อความ (getMessage) พร้อมแสดงให้ผู้ใช้เห็นได้ทันที
 */
public class LoginException extends Exception {
    public LoginException(String message) {
        super(message);
    }
}
