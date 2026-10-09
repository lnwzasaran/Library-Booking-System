/**
 * ข้อมูลที่ผู้ใช้กรอกในหน้าสมัครสมาชิก (ยังไม่ผ่านการตรวจสอบ)
 * รวมเป็นก้อนเดียว แทนการส่งพารามิเตอร์ String 6 ตัวเรียงกัน ซึ่งสลับลำดับผิดได้ง่าย
 */
public final class RegistrationData {
    public final String username;
    public final String fullName;
    public final String email;
    public final String phone;
    public final String password;
    public final String confirmPassword;

    public RegistrationData(String username, String fullName, String email,
                            String phone, String password, String confirmPassword) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
