/**
 * เกิดเมื่อข้อมูลสมัครสมาชิกไม่ผ่าน บอกได้ว่าผิดที่ช่องไหน (getField)
 * หน้าจอจึงไฮไลต์ช่องนั้นได้ และ getMessage() พร้อมแสดงให้ผู้ใช้เห็น
 */
public class RegistrationException extends Exception {

    /** ช่องในฟอร์มสมัครสมาชิก */
    public enum Field { USERNAME, FULL_NAME, EMAIL, PHONE, PASSWORD, CONFIRM_PASSWORD, NONE }

    private final Field field;

    public RegistrationException(Field field, String message) {
        super(message);
        this.field = field;
    }

    public Field getField() {
        return field;
    }
}
