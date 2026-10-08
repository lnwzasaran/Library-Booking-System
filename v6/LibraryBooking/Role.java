/**
 * บทบาทของผู้ใช้ในระบบ (ใช้ enum แทน String เพื่อให้คอมไพเลอร์ช่วยตรวจ — static checking)
 */
public enum Role {
    MEMBER, ADMIN;

    /**
     * แปลงข้อความจากไฟล์ CSV เป็น Role
     * @param text เช่น "admin", "member" (ไม่สนตัวพิมพ์เล็กใหญ่ ตัดช่องว่างหัวท้าย)
     * @return Role ที่ตรงกัน
     * @throws IllegalArgumentException ถ้า text เป็น null หรือไม่ตรงกับ Role ใดเลย
     */
    public static Role fromText(String text) {
        if (text == null) {
            throw new IllegalArgumentException("role must not be null");
        }
        return Role.valueOf(text.trim().toUpperCase());
    }
}
