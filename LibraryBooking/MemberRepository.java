import java.io.IOException;
import java.util.Optional;

/**
 * แหล่งเก็บข้อมูลสมาชิก (Dependency Inversion Principle)
 * AuthService / RegistrationService พึ่งพาแค่ interface นี้ — ตอนทดสอบจึงใช้ข้อมูลปลอมได้
 * และวันหลังจะเปลี่ยนไปเก็บที่อื่นก็ไม่ต้องแก้ส่วนอื่น (Open/Closed)
 */
public interface MemberRepository {

    /** @return สมาชิกที่ username ตรงกัน หรือ Optional.empty() ถ้าไม่พบ */
    Optional<Member> findByUsername(String username);

    /** @return จำนวนสมาชิกทั้งหมด */
    int count();

    /** @return true ถ้ามี username นี้อยู่แล้ว */
    default boolean exists(String username) {
        return findByUsername(username).isPresent();
    }

    /**
     * บันทึกสมาชิกใหม่
     * @throws IllegalStateException ถ้ามี username นี้อยู่แล้ว
     * @throws IOException ถ้าบันทึกลงที่เก็บไม่สำเร็จ
     */
    void save(Member member) throws IOException;
}
