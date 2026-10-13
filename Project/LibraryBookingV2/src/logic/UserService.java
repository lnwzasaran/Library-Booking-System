package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

import java.util.ArrayList;   // ลิสต์ผู้ใช้

/*
 * จัดการข้อมูลผู้ใช้ (นิสิต): อ่าน/เขียน data/users.csv
 *   - สมัครสมาชิก: ตรวจข้อมูลด้วย validateRegister แล้วบันทึกด้วย save
 *   - เข้าสู่ระบบ: หาผู้ใช้จากรหัสนิสิต แล้วเทียบรหัสผ่าน
 */
public class UserService {

    public static final String FILE = "users.csv";                                    // ชื่อไฟล์ข้อมูลผู้ใช้
    public static final String HEADER = "studentId,fullName,email,phone,password";    // หัวตาราง (ใช้ตอนสร้างไฟล์ใหม่)
    public static final int STUDENT_ID_LENGTH = 10;   // รหัสนิสิตต้องมี 10 หลัก
    public static final int MIN_PASSWORD = 8;         // รหัสผ่านอย่างน้อย 8 ตัวอักษร (ตามข้อความในช่องกรอก)

    // อ่านผู้ใช้ทั้งหมดจากไฟล์ (แถวที่ข้อมูลผิดรูปแบบจะถูกข้าม)
    public static ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();          // ลิสต์ว่างไว้เก็บผู้ใช้
        ArrayList<String[]> rows = CsvFile.read(FILE);      // อ่านทุกแถวของ users.csv
        for (String[] cols : rows) {                        // วนทีละแถว
            try {                                           // ลองแปลงแถวเป็น User
                users.add(User.fromCsv(cols));              // แปลงได้ → เก็บไว้
            } catch (Exception e) {                         // แถวผิดรูปแบบ (คอลัมน์ไม่ครบ)
                System.out.println("ข้ามแถวที่ผิดใน " + FILE);   // แจ้งใน console แล้วข้าม (ไม่พิมพ์ข้อมูลออกมาเพราะมีรหัสผ่าน)
            }                                               // จบ try-catch
        }                                                   // จบ for
        return users;                                       // คืนลิสต์ผู้ใช้
    }                                                       // จบ loadUsers

    // หาผู้ใช้จากรหัสนิสิต (ไม่เจอคืน null)
    public static User findUser(ArrayList<User> users, String studentId) {
        for (User u : users) {                              // วนทุกผู้ใช้
            if (u.getStudentId().equals(studentId)) {       // รหัสตรงกัน
                return u;                                   // คืนผู้ใช้คนนี้
            }                                               // จบ if
        }                                                   // จบ for
        return null;                                        // ไม่เจอ
    }                                                       // จบ findUser

    // ตรวจการเข้าสู่ระบบ — ผ่านคืน null, ไม่ผ่านคืนข้อความบอกเหตุผล
    public static String validateLogin(ArrayList<User> users, String studentId, String password) {
        if (studentId.equals("") || password.equals("")) {          // กรอกไม่ครบ
            return "กรุณากรอกรหัสนิสิตและรหัสผ่าน";                     // แจ้งเหตุผล
        }                                                           // จบ if
        User u = findUser(users, studentId);                        // หาผู้ใช้จากรหัสนิสิต
        if (u == null) {                                            // ไม่มีรหัสนี้ในไฟล์
            return "ไม่พบรหัสนิสิตนี้ในระบบ กรุณาสมัครสมาชิกก่อน";          // แจ้งเหตุผล
        }                                                           // จบ if
        if (!u.getPassword().equals(password)) {                    // รหัสผ่านไม่ตรง
            return "รหัสผ่านไม่ถูกต้อง";                                // แจ้งเหตุผล
        }                                                           // จบ if
        return null;                                                // ผ่าน
    }                                                               // จบ validateLogin

    // ตรวจข้อมูลสมัครสมาชิก — ผ่านคืน null, ไม่ผ่านคืนข้อความบอกเหตุผล
    public static String validateRegister(ArrayList<User> users, String studentId, String fullName,
                                          String email, String phone, String password, String confirm) {
        if (studentId.equals("") || fullName.equals("") || email.equals("")
                || phone.equals("") || password.equals("") || confirm.equals("")) {   // มีช่องว่าง
            return "กรุณากรอกข้อมูลให้ครบทุกช่อง";                                       // แจ้งเหตุผล
        }                                                                           // จบ if
        if (studentId.length() != STUDENT_ID_LENGTH) {                              // รหัสนิสิตไม่ครบ 10 หลัก
            return "รหัสนิสิตต้องเป็นตัวเลข " + STUDENT_ID_LENGTH + " หลัก";              // แจ้งเหตุผล
        }                                                                           // จบ if
        if (findUser(users, studentId) != null || AdminService.isAdminId(studentId)) {   // มีรหัสนี้อยู่แล้ว (ผู้ใช้ หรือ admin)
            return "รหัสนิสิตนี้สมัครสมาชิกไปแล้ว";                                       // แจ้งเหตุผล
        }                                                                           // จบ if
        if (email.indexOf("@") < 1 || email.indexOf(".") < 0) {                     // ไม่มี @ (หรือ @ อยู่ตัวแรก) หรือไม่มีจุด
            return "รูปแบบอีเมลไม่ถูกต้อง เช่น example@gmail.com";                       // แจ้งเหตุผล
        }                                                                           // จบ if
        String digits = phone.replace("-", "").replace(" ", "");                    // ตัดขีดและช่องว่างออก เช่น 081-234-5678 → 0812345678
        if (digits.length() != 10 || !isDigits(digits)) {                           // ต้องเป็นตัวเลข 10 หลัก
            return "เบอร์โทรศัพท์ต้องเป็นตัวเลข 10 หลัก เช่น 081-234-5678";              // แจ้งเหตุผล
        }                                                                           // จบ if
        if (password.length() < MIN_PASSWORD) {                                     // รหัสผ่านสั้นเกิน
            return "รหัสผ่านต้องมีอย่างน้อย " + MIN_PASSWORD + " ตัวอักษร";               // แจ้งเหตุผล
        }                                                                           // จบ if
        if (!password.equals(confirm)) {                                            // ยืนยันรหัสผ่านไม่ตรง
            return "ยืนยันรหัสผ่านไม่ตรงกับรหัสผ่าน";                                    // แจ้งเหตุผล
        }                                                                           // จบ if
        String all = studentId + fullName + email + phone + password;               // ข้อมูลทุกช่องรวมกัน
        if (all.indexOf(",") >= 0) {                                                // มี , (จะทำให้คอลัมน์ใน csv เพี้ยน)
            return "ห้ามใช้เครื่องหมาย , ในข้อมูล";                                       // แจ้งเหตุผล
        }                                                                           // จบ if
        return null;                                                                // ผ่านทุกข้อ
    }                                                                               // จบ validateRegister

    // ข้อความนี้เป็นตัวเลข 0-9 ทั้งหมดไหม
    private static boolean isDigits(String text) {
        for (int i = 0; i < text.length(); i++) {          // วนทีละตัวอักษร
            char c = text.charAt(i);                       // ตัวอักษรที่ i
            if (c < '0' || c > '9') {                      // ไม่ใช่ตัวเลข
                return false;                              // ไม่ผ่าน
            }                                              // จบ if
        }                                                  // จบ for
        return true;                                       // เป็นตัวเลขทั้งหมด
    }                                                      // จบ isDigits

    // บันทึกผู้ใช้ใหม่ต่อท้าย users.csv (true = สำเร็จ)
    public static boolean save(User user) {
        return CsvFile.appendLine(FILE, HEADER, user.toCsv());   // แปลงเป็นบรรทัด CSV แล้วเพิ่มท้ายไฟล์
    }                                                            // จบ save

    // สมัครสมาชิก: ตรวจข้อมูล แล้วบันทึกลง users.csv — สำเร็จคืน null, ไม่สำเร็จคืนข้อความบอกเหตุผล
    public static String register(String studentId, String fullName, String email,
                                  String phone, String password, String confirm) {
        String error = validateRegister(loadUsers(), studentId, fullName, email, phone, password, confirm);   // ตรวจทุกข้อ
        if (error != null) {                                         // ไม่ผ่าน
            return error;                                            // คืนเหตุผล
        }                                                            // จบ if
        if (!save(new User(studentId, fullName, email, phone, password))) {   // บันทึกลงไฟล์ไม่ได้
            return "บันทึกข้อมูลไม่สำเร็จ กรุณาลองใหม่";                       // คืนเหตุผล
        }                                                            // จบ if
        return null;                                                 // สำเร็จ
    }                                                                // จบ register
}                                                                // จบคลาส UserService
