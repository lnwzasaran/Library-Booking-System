package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

/*
 * ข้อมูลการจอง 1 รายการ (1 แถวใน bookings.csv)
 * คอลัมน์: bookingId, studentId, roomId, date, startTime, endTime, people, status
 *   date   = วันที่แบบ dd/MM/yyyy (ค.ศ.) เช่น 13/10/2026
 *   status = BOOKED (จองอยู่) หรือ CANCELLED (ยกเลิกแล้ว)
 */
public class Booking {

    private final String id;          // รหัสการจอง เช่น LBA-001
    private final String studentId;   // รหัสนิสิตของผู้จอง
    private final String roomId;      // รหัสห้องที่จอง
    private final String date;        // วันที่จอง
    private final String startTime;   // เวลาเริ่ม เช่น 13.00
    private final String endTime;     // เวลาสิ้นสุด เช่น 15.00
    private final int people;         // จำนวนผู้ใช้
    private final String status;      // สถานะการจอง

    public Booking(String id, String studentId, String roomId, String date,
                   String startTime, String endTime, int people, String status) {   // คอนสตรักเตอร์
        this.id = id;                   // เก็บค่าทุกคอลัมน์
        this.studentId = studentId;
        this.roomId = roomId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.people = people;
        this.status = status;
    }                                   // จบคอนสตรักเตอร์

    // สร้าง Booking จาก 1 แถวของ CSV (เรียงคอลัมน์ตามหัวตาราง)
    public static Booking fromCsv(String[] c) {
        return new Booking(c[0].trim(), c[1].trim(), c[2].trim(), toAD(c[3].trim()),
                c[4].trim(), c[5].trim(), Integer.parseInt(c[6].trim()), c[7].trim());   // trim() ตัดช่องว่าง, parseInt แปลงเป็นตัวเลข
    }                                                                                     // จบ fromCsv

    // วันที่ที่ถูกบันทึกเป็น พ.ศ. (เช่น 13/10/2569 จากเครื่องที่ตั้งปฏิทินไทย) → แปลงกลับเป็น ค.ศ. (13/10/2026)
    private static String toAD(String date) {
        String[] p = date.split("/");                        // แยก วัน / เดือน / ปี
        if (p.length == 3) {                                 // รูปแบบถูกต้อง
            int year = Integer.parseInt(p[2]);               // ปี
            if (year > 2400) {                               // ปีเกิน 2400 = เป็น พ.ศ. แน่นอน
                return p[0] + "/" + p[1] + "/" + (year - 543);   // ลบ 543 ให้เป็น ค.ศ.
            }                                                // จบ if
        }                                                    // จบ if
        return date;                                         // เป็น ค.ศ. อยู่แล้ว ใช้ค่าเดิม
    }                                                        // จบ toAD

    // แปลงกลับเป็น 1 บรรทัดของ CSV (เรียงคอลัมน์ตามหัวตาราง)
    public String toCsv() {
        return id + "," + studentId + "," + roomId + "," + date + ","
                + startTime + "," + endTime + "," + people + "," + status;   // ต่อทุกคอลัมน์ด้วย ,
    }                                                                         // จบ toCsv

    // การจองนี้ยังใช้งานอยู่ไหม (ยังไม่ถูกยกเลิก)
    public boolean isActive() {
        return status.equals("BOOKED");   // true ถ้าสถานะเป็น BOOKED
    }                                     // จบ isActive

    public String getId()         { return id; }          // getter ทุกคอลัมน์
    public String getStudentId()  { return studentId; }
    public String getRoomId()     { return roomId; }
    public String getDate()       { return date; }
    public String getStartTime()  { return startTime; }
    public String getEndTime()    { return endTime; }
    public int getPeople()        { return people; }
    public String getStatus()     { return status; }
}                                                         // จบคลาส Booking
