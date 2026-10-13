package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

import java.text.SimpleDateFormat;   // จัดรูปแบบวันที่เป็นข้อความ
import java.util.ArrayList;          // ลิสต์ของการจอง
import java.util.Date;
import java.util.Locale;             // Locale.US = ใช้ปฏิทิน ค.ศ. เสมอ (เครื่องที่ตั้งภาษาไทยจะได้ พ.ศ. ถ้าไม่ระบุ)               // วันที่และเวลาปัจจุบันของเครื่อง

/*
 * จัดการข้อมูลการจอง: อ่านจาก data/bookings.csv
 */
public class BookingService {

    public static final String FILE = "bookings.csv";   // ชื่อไฟล์ข้อมูลการจอง
    public static final String HEADER = "bookingId,studentId,roomId,date,startTime,endTime,people,status";   // หัวตาราง (ใช้ตอนสร้างไฟล์ใหม่)

    // ---------- กฎการจอง ----------
    public static final int MAX_HOURS = 2;          // จองได้ไม่เกิน 2 ชั่วโมงต่อครั้ง
    public static final int MAX_PER_DAY = 1;        // 1 คนจองได้วันละ 1 ครั้ง
    public static final int MAX_DAYS_AHEAD = 1;     // จองล่วงหน้าได้ 1 วัน (วันนี้ หรือ พรุ่งนี้)
    public static final int OPEN_HOUR = 9;          // เปิดให้จองตั้งแต่ 09.00
    public static final int CLOSE_HOUR = 18;        // จองได้ถึง 18.00 (เวลาสิ้นสุดห้ามเกิน 18.00)

    // อ่านการจองทั้งหมดจากไฟล์ (แถวที่ข้อมูลผิดรูปแบบจะถูกข้าม ไม่ทำให้โปรแกรมพัง)
    public static ArrayList<Booking> loadBookings() {
        ArrayList<Booking> bookings = new ArrayList<>();    // ลิสต์ว่างไว้เก็บการจอง
        ArrayList<String[]> rows = CsvFile.read(FILE);      // อ่านทุกแถวของ bookings.csv
        for (String[] cols : rows) {                        // วนทีละแถว
            try {                                           // ลองแปลงแถวเป็น Booking
                bookings.add(Booking.fromCsv(cols));        // แปลงได้ → เก็บไว้
            } catch (Exception e) {                         // แถวผิดรูปแบบ
                System.out.println("ข้ามแถวที่ผิดใน " + FILE + ": " + String.join(",", cols));   // แจ้งใน console แล้วข้าม
            }                                               // จบ try-catch
        }                                                   // จบ for
        return bookings;                                    // คืนลิสต์การจอง
    }                                                       // จบ loadBookings

    // วันที่วันนี้ในรูปแบบเดียวกับไฟล์ (dd/MM/yyyy ค.ศ.) เช่น 13/10/2026
    public static String today() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);   // กำหนดรูปแบบ วัน/เดือน/ปี
        return format.format(new Date());                               // new Date() = ตอนนี้ → แปลงเป็นข้อความ
    }                                                                   // จบ today

    // วันที่พรุ่งนี้ในรูปแบบ dd/MM/yyyy
    public static String tomorrow() {
        long oneDay = 24L * 60 * 60 * 1000;                              // 1 วัน = 86,400,000 มิลลิวินาที
        Date next = new Date(System.currentTimeMillis() + oneDay);       // เวลาตอนนี้ + 1 วัน
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.US);    // รูปแบบ วัน/เดือน/ปี
        return format.format(next);                                      // แปลงเป็นข้อความ
    }                                                                    // จบ tomorrow

    // ดึงชั่วโมงจากเวลา เช่น "13.00" → 13  (รับแบบ "13:00" ได้ด้วย)
    public static int hourOf(String time) {
        String t = time.trim().replace(":", ".");             // เปลี่ยน : เป็น . ให้เหมือนกันหมด
        if (t.indexOf(".") >= 0) {                            // ถ้ามีจุด
            t = t.substring(0, t.indexOf("."));               // ตัดข้อความหน้าจุด เช่น "13"
        }                                                     // จบ if
        return Integer.parseInt(t);                           // แปลงเป็นตัวเลข
    }                                                         // จบ hourOf

    // ตรวจกฎการจองทั้งหมด — ผ่านคืน null, ไม่ผ่านคืนข้อความบอกเหตุผล (เอาไปแสดงในกล่องข้อความ)
    public static String validate(ArrayList<Booking> bookings, String studentId, Room room,
                                  String date, String start, String end) {
        // 1) จองล่วงหน้าได้ 1 วัน → วันที่ต้องเป็นวันนี้ หรือ พรุ่งนี้
        if (!date.equals(today()) && !date.equals(tomorrow())) {                            // ไม่ใช่ทั้งสองวัน
            return "จองได้เฉพาะวันนี้ (" + today() + ") หรือพรุ่งนี้ (" + tomorrow() + ") เท่านั้น";   // แจ้งวันที่จองได้
        }                                                                                   // จบ if

        // 2) เวลา: อยู่ในช่วง 09.00 - 18.00, สิ้นสุดต้องหลังเริ่ม และไม่เกิน 2 ชั่วโมง
        if (hourOf(start) < OPEN_HOUR || hourOf(end) > CLOSE_HOUR) {   // เริ่มก่อน 09.00 หรือ จบหลัง 18.00
            return "จองได้เฉพาะช่วงเวลา " + String.format("%02d.00", OPEN_HOUR)
                    + " - " + CLOSE_HOUR + ".00 เท่านั้น";              // แจ้งช่วงเวลาที่จองได้ (%02d → 09)
        }                                                            // จบ if
        int hours = hourOf(end) - hourOf(start);                     // จำนวนชั่วโมงที่จอง
        if (hours <= 0) {                                            // สิ้นสุดก่อนหรือเท่ากับเวลาเริ่ม
            return "เวลาสิ้นสุดต้องอยู่หลังเวลาที่เริ่ม";                  // แจ้งเหตุผล
        }                                                            // จบ if
        if (hours > MAX_HOURS) {                                     // เกิน 2 ชั่วโมง
            return "จองได้ไม่เกิน " + MAX_HOURS + " ชั่วโมงต่อครั้ง (เลือกไว้ " + hours + " ชั่วโมง)";   // แจ้งเหตุผล
        }                                                            // จบ if

        // 3) 1 คนจองได้วันละ 1 ครั้ง
        int sameDay = 0;                                             // จำนวนการจองของคนนี้ในวันนั้น
        for (Booking b : bookings) {                                 // วนทุกการจอง
            if (b.isActive() && b.getStudentId().equals(studentId) && b.getDate().equals(date)) {   // ของคนนี้ วันเดียวกัน ยังไม่ยกเลิก
                sameDay++;                                           // นับเพิ่ม
            }                                                        // จบ if
        }                                                            // จบ for
        if (sameDay >= MAX_PER_DAY) {                                // ครบโควตาแล้ว
            return "คุณจองไปแล้วในวันที่ " + date + " (จองได้วันละ " + MAX_PER_DAY + " ครั้ง)";   // แจ้งเหตุผล
        }                                                            // จบ if

        // 4) ห้องเดียวกันห้ามจองเวลาทับกัน (README ข้อ 7)
        int s = hourOf(start);                                       // ชั่วโมงเริ่มที่ขอจอง
        int e = hourOf(end);                                         // ชั่วโมงสิ้นสุดที่ขอจอง
        for (Booking b : bookings) {                                 // วนทุกการจอง
            if (b.isActive() && b.getRoomId().equals(room.getId()) && b.getDate().equals(date)) {   // ห้องเดียวกัน วันเดียวกัน ยังไม่ยกเลิก
                int bs = hourOf(b.getStartTime());                   // ชั่วโมงเริ่มของการจองเดิม
                int be = hourOf(b.getEndTime());                     // ชั่วโมงสิ้นสุดของการจองเดิม
                if (s < be && bs < e) {                              // ช่วงเวลาทับกัน (เริ่มก่อนอีกอันจบ ทั้งสองฝั่ง)
                    return "ห้อง " + room.getId() + " ถูกจองแล้วช่วง " + b.getStartTime() + " - " + b.getEndTime();   // แจ้งช่วงที่ถูกจอง
                }                                                    // จบ if
            }                                                        // จบ if
        }                                                            // จบ for

        return null;                                                 // ผ่านทุกข้อ
    }                                                                // จบ validate

    // สร้างรหัสการจองใหม่ของวันที่ date
    //   - จองของวันนี้      → LBA-001, LBA-002, ... (เริ่มที่ 1)
    //   - จองของวันอื่น (พรุ่งนี้) → LBA-101, LBA-102, ... (เริ่มที่ 101)
    //   - นับแยกตามวันที่จอง จึง "รีเซ็ต" เองเมื่อขึ้นวันใหม่ (วันใหม่ยังไม่มีการจอง → เริ่มเลขแรกอีกครั้ง)
    //   - นับรวมรายการที่ยกเลิกแล้วด้วย เลขจะได้ไม่ซ้ำกับรายการเดิมในวันเดียวกัน
    public static String nextId(ArrayList<Booking> bookings, String date) {
        int start;                                                   // เลขแรกของชุดนี้
        if (date.equals(today())) {                                  // ถ้าจองของวันนี้
            start = 1;                                               // ชุด 001, 002, ...
        } else {                                                     // ถ้าจองของวันอื่น (ล่วงหน้า)
            start = 101;                                             // ชุด 101, 102, ...
        }                                                            // จบ if
        int max = start - 1;                                         // เลขมากสุดที่เจอ (ยังไม่เจอ = เลขแรก - 1)
        for (Booking b : bookings) {                                 // วนทุกการจอง
            if (b.getDate().equals(date)) {                          // ดูเฉพาะการจองของวันเดียวกัน
                try {                                                // ลองแปลงเลขหลัง "LBA-"
                    int n = Integer.parseInt(b.getId().substring(4));   // ตัด "LBA-" ออก เหลือ "101" → 101
                    if (n >= start && n < start + 100 && n > max) {  // อยู่ในชุดเดียวกัน และมากกว่าที่เคยเจอ
                        max = n;                                     // จำไว้
                    }                                                // จบ if
                } catch (Exception e) {                              // รหัสไม่ได้อยู่ในรูปแบบ LBA-ตัวเลข
                    // ข้ามไป
                }                                                    // จบ try-catch
            }                                                        // จบ if
        }                                                            // จบ for
        return String.format("LBA-%03d", max + 1);                   // %03d = ตัวเลข 3 หลัก เติม 0 ข้างหน้า เช่น 7 → 007
    }                                                                // จบ nextId

    // บันทึกการจองใหม่ต่อท้าย bookings.csv (true = สำเร็จ)
    public static boolean save(Booking booking) {
        return CsvFile.appendLine(FILE, HEADER, booking.toCsv());    // แปลงเป็นบรรทัด CSV แล้วเพิ่มท้ายไฟล์
    }                                                                // จบ save

    // แปลงวันที่ 13/10/2026 → "13 ตุลาคม 2569" (ใช้แสดงในหน้าจอ)
    public static String thaiDate(String date) {
        String[] months = {"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน",
                           "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};   // ชื่อเดือน 12 เดือน
        String[] parts = date.split("/");                            // แยก วัน / เดือน / ปี
        int day = Integer.parseInt(parts[0]);                        // วัน (13)
        int month = Integer.parseInt(parts[1]);                      // เดือน (10)
        int year = Integer.parseInt(parts[2]);                       // ปีที่อยู่ในข้อมูล
        if (year < 2400) {                                           // เป็นปี ค.ศ. (เช่น 2026)
            year = year + 543;                                       // ค.ศ. → พ.ศ. (2026 + 543 = 2569)
        }                                                            // จบ if (ถ้าเป็น พ.ศ. อยู่แล้ว เช่น 2569 ใช้ค่าเดิม ไม่บวกซ้ำ)
        return day + " " + months[month - 1] + " " + year;           // months[0] = มกราคม จึงต้อง -1
    }                                                                // จบ thaiDate

    // นับการจองที่ยังใช้งานอยู่ (BOOKED) ของนิสิตคนนี้
    public static int countMyBookings(ArrayList<Booking> bookings, String studentId) {
        int count = 0;                                                       // ตัวนับ
        for (Booking b : bookings) {                                         // วนทุกการจอง
            if (b.isActive() && b.getStudentId().equals(studentId)) {        // ยังไม่ยกเลิก และเป็นของคนนี้
                count++;                                                     // นับเพิ่ม 1
            }                                                                // จบ if
        }                                                                    // จบ for
        return count;                                                        // คืนจำนวน
    }                                                                        // จบ countMyBookings

    // นับการจองทั้งหมดของวันนี้ (BOOKED เท่านั้น)
    public static int countToday(ArrayList<Booking> bookings, String today) {
        int count = 0;                                                  // ตัวนับ
        for (Booking b : bookings) {                                    // วนทุกการจอง
            if (b.isActive() && b.getDate().equals(today)) {            // ยังไม่ยกเลิก และเป็นวันนี้
                count++;                                                // นับเพิ่ม 1
            }                                                           // จบ if
        }                                                               // จบ for
        return count;                                                   // คืนจำนวน
    }                                                                   // จบ countToday

    // การจองนี้ "หมดเวลาแล้ว" หรือยัง = วันที่ + เวลาสิ้นสุด อยู่ก่อนเวลาตอนนี้
    public static boolean isFinished(Booking b) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH.mm", Locale.US);   // รูปแบบ วัน/เดือน/ปี ชั่วโมง.นาที
        try {                                                                 // parse อาจผิดรูปแบบ จึงต้องอยู่ใน try-catch
            String text = b.getDate() + " " + b.getEndTime().replace(":", ".");   // เช่น "13/10/2026 15.00"
            Date end = format.parse(text);                                    // แปลงข้อความเป็นวันเวลา
            return end.before(new Date());                                    // before = เวลาสิ้นสุดผ่านไปแล้ว
        } catch (Exception e) {                                               // วันที่/เวลาในไฟล์ผิดรูปแบบ
            return false;                                                     // ถือว่ายังไม่หมดเวลา
        }                                                                     // จบ try-catch
    }                                                                         // จบ isFinished

    // ยกเลิกการจอง: เปลี่ยนสถานะเป็น CANCELLED แล้วเขียน bookings.csv ใหม่ทั้งไฟล์
    // หาด้วย รหัส + วันที่ + รหัสนิสิต (รหัสการจองซ้ำได้ข้ามวัน) คืนค่า true = สำเร็จ
    public static boolean cancel(String bookingId, String date, String studentId) {
        ArrayList<Booking> bookings = loadBookings();               // อ่านการจองทั้งหมดจากไฟล์
        ArrayList<String> lines = new ArrayList<>();                // บรรทัดที่จะเขียนกลับ
        boolean found = false;                                      // เจอรายการที่จะยกเลิกไหม
        for (Booking b : bookings) {                                // วนทุกการจอง
            if (b.getId().equals(bookingId) && b.getDate().equals(date)
                    && b.getStudentId().equals(studentId) && b.isActive()) {   // ตรงทั้งรหัส วันที่ เจ้าของ และยังไม่ยกเลิก
                Booking cancelled = new Booking(b.getId(), b.getStudentId(), b.getRoomId(), b.getDate(),
                        b.getStartTime(), b.getEndTime(), b.getPeople(), "CANCELLED");   // สร้างใหม่ สถานะ CANCELLED (Booking แก้ค่าไม่ได้)
                lines.add(cancelled.toCsv());                       // เก็บบรรทัดที่แก้แล้ว
                found = true;                                       // เจอแล้ว
            } else {                                                // รายการอื่น
                lines.add(b.toCsv());                               // เก็บไว้เหมือนเดิม
            }                                                       // จบ if
        }                                                           // จบ for
        if (!found) {                                               // ไม่เจอรายการ
            return false;                                           // ยกเลิกไม่สำเร็จ
        }                                                           // จบ if
        return CsvFile.writeAll(FILE, HEADER, lines);               // เขียนทับทั้งไฟล์
    }                                                               // จบ cancel

    // นาทีของเวลาตอนนี้นับจากเที่ยงคืน เช่น 13:05 → 13*60 + 5 = 785
    public static int nowMinutes() {
        SimpleDateFormat format = new SimpleDateFormat("HH.mm", Locale.US);      // รูปแบบ ชั่วโมง.นาที (24 ชม.)
        String now = format.format(new Date());                       // เช่น "13.05"
        int hour = Integer.parseInt(now.substring(0, 2));             // "13" → 13
        int minute = Integer.parseInt(now.substring(3, 5));           // "05" → 5
        return hour * 60 + minute;                                    // แปลงเป็นนาที
    }                                                                 // จบ nowMinutes

    // หาการจองที่กำลังใช้ห้องนี้อยู่ ณ วันที่ date เวลา minutes (ไม่มี = null → ห้องว่าง)
    // กำลังใช้ = BOOKED, วันเดียวกัน, เวลาเริ่ม <= ตอนนี้ < เวลาสิ้นสุด
    public static Booking findCurrent(ArrayList<Booking> bookings, String roomId, String date, int minutes) {
        for (Booking b : bookings) {                                  // วนทุกการจอง
            if (b.isActive() && b.getRoomId().equals(roomId) && b.getDate().equals(date)) {   // ห้องนี้ วันนี้ ยังไม่ยกเลิก
                int start = hourOf(b.getStartTime()) * 60;            // เวลาเริ่มเป็นนาที (13.00 → 780)
                int end = hourOf(b.getEndTime()) * 60;                // เวลาสิ้นสุดเป็นนาที (15.00 → 900)
                if (start <= minutes && minutes < end) {              // ตอนนี้อยู่ในช่วงที่จอง (ถึง 15:00 พอดีถือว่าหมดแล้ว)
                    return b;                                         // เจอ → ห้องไม่ว่าง
                }                                                     // จบ if
            }                                                         // จบ if
        }                                                             // จบ for
        return null;                                                  // ไม่มีการจองในช่วงนี้ → ห้องว่าง
    }                                                                 // จบ findCurrent

    // ห้องนี้ว่างในวันที่ date ช่วง start - end ไหม (ไม่มีการจอง BOOKED ที่เวลาทับกัน) — ใช้กรองห้องใน dropdown หน้าจองห้อง
    public static boolean isFreeAt(ArrayList<Booking> bookings, String roomId, String date, String start, String end) {
        int s = hourOf(start);                                       // ชั่วโมงเริ่มที่เลือก
        int e = hourOf(end);                                         // ชั่วโมงสิ้นสุดที่เลือก
        for (Booking b : bookings) {                                 // วนทุกการจอง
            if (b.isActive() && b.getRoomId().equals(roomId) && b.getDate().equals(date)) {   // ห้องนี้ วันนั้น ยังไม่ยกเลิก
                int bs = hourOf(b.getStartTime());                   // ชั่วโมงเริ่มของการจองเดิม
                int be = hourOf(b.getEndTime());                     // ชั่วโมงสิ้นสุดของการจองเดิม
                if (s < be && bs < e) {                              // เวลาทับกัน (กฎเดียวกับ validate ข้อ 4)
                    return false;                                    // ห้องไม่ว่างช่วงนี้
                }                                                    // จบ if
            }                                                        // จบ if
        }                                                            // จบ for
        return true;                                                 // ไม่มีการจองทับ → ว่าง
    }                                                                // จบ isFreeAt

    // ================= เมธอดที่ย้ายมาจากหน้า UI (หน้า UI แค่เรียกใช้แล้วแสดงผล) =================

    // จองห้อง: ตรวจกฎ → สร้างรหัส → บันทึกลง bookings.csv แล้วคืนการจองที่สร้าง
    // ไม่ผ่านกฎ / บันทึกไม่ได้ → throw IllegalArgumentException พร้อมข้อความบอกเหตุผล (วิชา SC สัปดาห์ที่ 2)
    public static Booking book(String studentId, String roomId, String date, String start, String end) {
        Room room = RoomService.findRoom(RoomService.loadRooms(), roomId);      // หาห้องจาก rooms.csv
        if (room == null) {                                                     // ไม่มีห้องนี้ในไฟล์
            throw new IllegalArgumentException("ไม่พบห้อง " + roomId + " ในไฟล์ rooms.csv");   // แจ้งเหตุผล
        }                                                                       // จบ if
        ArrayList<Booking> bookings = loadBookings();                           // อ่านการจองทั้งหมดจากไฟล์
        String error = validate(bookings, studentId, room, date, start, end);   // ตรวจกฎทั้งหมด
        if (error != null) {                                                    // ผิดกฎข้อใดข้อหนึ่ง
            throw new IllegalArgumentException(error);                          // แจ้งเหตุผล
        }                                                                       // จบ if
        Booking booking = new Booking(nextId(bookings, date), studentId, room.getId(),
                date, start, end, room.getCapacity(), "BOOKED");                // การจองใหม่ (จำนวนผู้ใช้ = ความจุห้อง)
        if (!save(booking)) {                                                   // บันทึกลงไฟล์ไม่ได้
            throw new IllegalArgumentException("บันทึกการจองไม่สำเร็จ กรุณาลองใหม่");   // แจ้งเหตุผล
        }                                                                       // จบ if
        return booking;                                                         // คืนการจองที่บันทึกแล้ว
    }                                                                           // จบ book

    // เรียงจากใหม่ไปเก่า (บรรทัดท้ายไฟล์ = จองล่าสุด → อยู่บนสุด)
    public static ArrayList<Booking> newestFirst(ArrayList<Booking> list) {
        ArrayList<Booking> result = new ArrayList<>();          // ลิสต์ผลลัพธ์
        for (int i = list.size() - 1; i >= 0; i--) {            // วนจากตัวสุดท้ายขึ้นมา
            result.add(list.get(i));                            // เก็บเรียงกลับด้าน
        }                                                       // จบ for
        return result;                                          // คืนลิสต์ใหม่
    }                                                           // จบ newestFirst

    // การจองปัจจุบันของนิสิตคนนี้ = BOOKED และยังไม่หมดเวลา (เรียงใหม่ไปเก่า)
    public static ArrayList<Booking> currentOf(ArrayList<Booking> all, String studentId) {
        ArrayList<Booking> result = new ArrayList<>();          // ลิสต์ผลลัพธ์
        for (Booking b : newestFirst(all)) {                    // วนจากใหม่ไปเก่า
            if (b.getStudentId().equals(studentId) && b.isActive() && !isFinished(b)) {   // ของคนนี้ ยังไม่ยกเลิก ยังไม่หมดเวลา
                result.add(b);                                  // เก็บไว้
            }                                                   // จบ if
        }                                                       // จบ for
        return result;                                          // คืนลิสต์
    }                                                           // จบ currentOf

    // ประวัติการจองของนิสิตคนนี้ = ทุกการจองของคนนี้ (รวมการจองปัจจุบันด้วย เรียงใหม่ไปเก่า)
    public static ArrayList<Booking> historyOf(ArrayList<Booking> all, String studentId) {
        ArrayList<Booking> result = new ArrayList<>();          // ลิสต์ผลลัพธ์
        for (Booking b : newestFirst(all)) {                    // วนจากใหม่ไปเก่า
            if (b.getStudentId().equals(studentId)) {           // ของคนนี้
                result.add(b);                                  // เก็บไว้
            }                                                   // จบ if
        }                                                       // จบ for
        return result;                                          // คืนลิสต์
    }                                                           // จบ historyOf

    // กรองตามสถานะ: status = "BOOKED" หรือ "CANCELLED" (null = ทั้งหมด)
    public static ArrayList<Booking> filterByStatus(ArrayList<Booking> list, String status) {
        ArrayList<Booking> result = new ArrayList<>();          // ลิสต์ผลลัพธ์
        for (Booking b : list) {                                // วนทุกการจอง
            if (status == null || b.getStatus().equals(status)) {   // ไม่กรอง หรือ สถานะตรงกัน
                result.add(b);                                  // เก็บไว้
            }                                                   // จบ if
        }                                                       // จบ for
        return result;                                          // คืนลิสต์
    }                                                           // จบ filterByStatus

    // นับการจองที่ยังไม่ยกเลิก (BOOKED) ในลิสต์
    public static int countActive(ArrayList<Booking> list) {
        int count = 0;                                          // ตัวนับ
        for (Booking b : list) {                                // วนทุกการจอง
            if (b.isActive()) {                                 // ยังไม่ยกเลิก
                count++;                                        // นับเพิ่ม
            }                                                   // จบ if
        }                                                       // จบ for
        return count;                                           // คืนจำนวน
    }                                                           // จบ countActive

    // ช่วงเวลาสำหรับแสดงผล เช่น "13:00 - 15:00"
    public static String timeRange(Booking b) {
        return b.getStartTime().replace(".", ":") + " - " + b.getEndTime().replace(".", ":");   // เปลี่ยน . เป็น :
    }                                                           // จบ timeRange
}                                                                    // จบคลาส BookingService
