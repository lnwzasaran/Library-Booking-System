package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

/*
 * ข้อมูลผู้ใช้ (นิสิต) 1 คน (1 แถวใน users.csv)
 * คอลัมน์: studentId, fullName, email, phone, password
 * private final = สร้างแล้วแก้ค่าไม่ได้ (immutable — วิชา Software Construction สัปดาห์ที่ 5)
 */
public class User {

    private final String studentId;   // รหัสนิสิต (ใช้เข้าสู่ระบบ)
    private final String fullName;    // ชื่อ-นามสกุล
    private final String email;       // อีเมล
    private final String phone;       // เบอร์โทรศัพท์
    private final String password;    // รหัสผ่าน

    public User(String studentId, String fullName, String email, String phone, String password) {   // คอนสตรักเตอร์
        this.studentId = studentId;   // เก็บรหัสนิสิต
        this.fullName = fullName;     // เก็บชื่อ
        this.email = email;           // เก็บอีเมล
        this.phone = phone;           // เก็บเบอร์โทร
        this.password = password;     // เก็บรหัสผ่าน
    }                                 // จบคอนสตรักเตอร์

    // สร้าง User จาก 1 แถวของ CSV (เรียงคอลัมน์ตามหัวตาราง)
    public static User fromCsv(String[] c) {
        return new User(c[0].trim(), c[1].trim(), c[2].trim(), c[3].trim(), c[4].trim());   // trim() ตัดช่องว่างหัวท้าย
    }                                                                                         // จบ fromCsv

    // แปลงกลับเป็น 1 บรรทัดของ CSV (เรียงคอลัมน์ตามหัวตาราง)
    public String toCsv() {
        return studentId + "," + fullName + "," + email + "," + phone + "," + password;   // ต่อทุกคอลัมน์ด้วย ,
    }                                                                                      // จบ toCsv

    public String getStudentId() { return studentId; }   // getter ทุกคอลัมน์
    public String getFullName()  { return fullName; }
    public String getEmail()     { return email; }
    public String getPhone()     { return phone; }
    public String getPassword()  { return password; }
}                                                        // จบคลาส User
