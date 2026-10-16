package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

import java.util.ArrayList;   // ลิสต์แถวที่อ่านจากไฟล์

/*
 * ตรวจการเข้าสู่ระบบของผู้ดูแลระบบ: อ่านจาก data/admins.csv
 * คอลัมน์: adminId, password, name
 *   adminId  = รหัสที่ใช้กรอกในช่อง "รหัสนิสิต" ตอนเข้าสู่ระบบ
 *   password = รหัสผ่านของ admin
 *   name     = ชื่อที่แสดงมุมขวาบนของหน้า Admin
 */
public class AdminService {

    public static final String FILE = "admins.csv";   // ชื่อไฟล์ข้อมูลผู้ดูแลระบบ

    // รหัสนี้เป็นของ admin ไหม (ใช้แยกว่าจะตรวจแบบ admin หรือเข้าหน้านิสิต)
    public static boolean isAdminId(String id) {
        ArrayList<String[]> rows = CsvFile.read(FILE);      // อ่านทุกแถวของ admins.csv
        for (String[] cols : rows) {                        // วนทีละแถว
            if (cols.length >= 2 && cols[0].trim().equals(id)) {   // คอลัมน์ครบ และรหัสตรงกัน
                return true;                                // เป็นรหัสของ admin
            }                                               // จบ if
        }                                                   // จบ for
        return false;                                       // ไม่ใช่รหัสของ admin
    }                                                       // จบ isAdminId

    // ตรวจรหัส + รหัสผ่าน: ถูกทั้งคู่คืนชื่อ admin, ผิดคืน null
    public static String login(String id, String password) {
        ArrayList<String[]> rows = CsvFile.read(FILE);      // อ่านทุกแถวของ admins.csv
        for (String[] cols : rows) {                        // วนทีละแถว
            if (cols.length >= 2 && cols[0].trim().equals(id)
                    && cols[1].trim().equals(password)) {   // รหัสตรง และรหัสผ่านตรง
                if (cols.length >= 3) {                     // มีคอลัมน์ชื่อ
                    return cols[2].trim();                  // คืนชื่อ admin
                }                                           // จบ if
                return "ผู้ดูแลระบบ";                         // ไม่มีชื่อในไฟล์ → ใช้คำกลาง ๆ
            }                                               // จบ if
        }                                                   // จบ for
        return null;                                        // ไม่ตรงแถวไหนเลย
    }                                                       // จบ login
}                                                           // จบคลาส AdminService
