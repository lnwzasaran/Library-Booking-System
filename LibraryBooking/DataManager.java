import java.io.*;                                     // BufferedReader, FileReader (สไลด์ Section 7)
import java.nio.charset.StandardCharsets;             // ระบุ UTF-8 เพื่อให้ภาษาไทยไม่เพี้ยน
import java.util.ArrayList;                           // ArrayList (สไลด์ Section 5)

// คลาสจัดการอ่านข้อมูลจากไฟล์ .txt ทุกเมธอดเป็น static จึงเรียกใช้ได้โดยไม่ต้อง new (สไลด์ Section 4: static)
public class DataManager {

    // ---------- อ่านรายชื่อห้องทั้งหมดจาก rooms.txt (บรรทัดละ 1 ห้อง: เลขห้อง,จำนวนที่นั่ง) ----------
    public static ArrayList<Room> loadRooms() {
        ArrayList<Room> list = new ArrayList<Room>();                 // ArrayList ขนาดไม่ตายตัวเก็บห้องทั้งหมด
        // try-with-resources: ไฟล์จะถูกปิดให้อัตโนมัติ ไม่ว่าจะเกิด exception หรือไม่ (สไลด์ Section 7)
        try (BufferedReader br = new BufferedReader(new FileReader("./Data/rooms.txt", StandardCharsets.UTF_8))) {
            String s;
            while ((s = br.readLine()) != null) {                     // อ่านทีละบรรทัดจนหมดไฟล์
                if (s.length() > 0) {                                 // ข้ามบรรทัดว่าง
                    String[] p = s.split(",");                        // ตัดข้อความที่เครื่องหมาย , ได้เป็นอาเรย์
                    int cap;
                    try {
                        cap = Integer.parseInt(p[1].trim());          // แปลงสตริงเป็นตัวเลข (สไลด์ Section 2)
                    } catch (NumberFormatException e) {               // ถ้าในไฟล์ไม่ใช่ตัวเลข (สไลด์ Section 5)
                        cap = 4;                                      // ใช้ค่าเริ่มต้นแทน แล้วทำงานต่อได้
                    }
                    list.add(new Room(p[0].trim(), cap));             // add เข้า ArrayList
                }
            }
        } catch (Exception e) {                                       // ไฟล์หาย/อ่านไม่ได้ จะมาที่นี่
            System.out.println(e);
        }
        return list;
    }

    // ---------- อ่านการจองทั้งหมดจาก bookings.txt (รหัสนศ.,เลขห้อง,วันที่,เวลา) ----------
    public static ArrayList<Booking> loadBookings() {
        ArrayList<Booking> list = new ArrayList<Booking>();
        try (BufferedReader br = new BufferedReader(new FileReader("./Data/bookings.txt", StandardCharsets.UTF_8))) {
            String s;
            while ((s = br.readLine()) != null) {
                if (s.length() > 0) {
                    String[] p = s.split(",");
                    list.add(new Booking(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim()));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    // ---------- หานักศึกษาจากรหัสใน students.txt (รหัส,ชื่อ) ถ้าไม่เจอส่ง null ----------
    public static Student loadStudent(String id) {
        Student result = null;                                        // เริ่มต้นว่ายังไม่เจอ
        try (BufferedReader br = new BufferedReader(new FileReader("./Data/students.txt", StandardCharsets.UTF_8))) {
            String s;
            while ((s = br.readLine()) != null) {
                String[] p = s.split(",");
                if (p.length >= 2 && p[0].trim().equals(id))          // เทียบสตริงด้วย equals ไม่ใช้ == (สไลด์ Section 2)
                    result = new Student(p[1].trim(), p[0].trim());
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return result;
    }

    // ---------- เขียนการจองทั้งหมดกลับลง bookings.txt (ใช้ทั้งตอนจองและตอนยกเลิก) ----------
    public static void saveBookings(ArrayList<Booking> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("./Data/bookings.txt", StandardCharsets.UTF_8))) {
            for (Booking b : list) {                                  // เขียนทีละรายการ บรรทัดละ 1 การจอง
                bw.write(b.getStudentId() + "," + b.getRoomNo() + "," + b.getDate() + "," + b.getTime());
                bw.newLine();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}