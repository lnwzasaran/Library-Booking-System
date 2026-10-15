package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

import java.util.ArrayList;   // ลิสต์ของห้อง

/*
 * จัดการข้อมูลห้อง: อ่านจาก data/rooms.csv
 */
public class RoomService {

    public static final String FILE = "rooms.csv";   // ชื่อไฟล์ข้อมูลห้อง

    // อ่านห้องทั้งหมดจากไฟล์ (แถวที่ข้อมูลผิดรูปแบบจะถูกข้าม ไม่ทำให้โปรแกรมพัง)
    public static ArrayList<Room> loadRooms() {
        ArrayList<Room> rooms = new ArrayList<>();          // ลิสต์ว่างไว้เก็บห้อง
        ArrayList<String[]> rows = CsvFile.read(FILE);      // อ่านทุกแถวของ rooms.csv
        for (String[] cols : rows) {                        // วนทีละแถว
            try {                                           // ลองแปลงแถวเป็น Room
                rooms.add(Room.fromCsv(cols));              // แปลงได้ → เก็บไว้
            } catch (Exception e) {                         // แถวผิดรูปแบบ (คอลัมน์ไม่ครบ / ความจุไม่ใช่ตัวเลข)
                System.out.println("[data] skipped bad row in " + FILE + ": " + String.join(",", cols));   // แจ้งใน console แล้วข้าม
            }                                               // จบ try-catch
        }                                                   // จบ for
        return rooms;                                       // คืนลิสต์ห้อง
    }                                                       // จบ loadRooms

    // ห้องนี้ว่างในวันที่ date ไหม = ไม่มีการจอง BOOKED ของห้องนี้ในวันนั้น
    public static boolean isFree(Room room, ArrayList<Booking> bookings, String date) {
        for (Booking b : bookings) {                         // ดูการจองทุกรายการ
            if (b.isActive() && b.getRoomId().equals(room.getId()) && b.getDate().equals(date)) {   // จองห้องนี้ วันนั้น และยังไม่ยกเลิก
                return false;                                // เจอการจอง → ห้องไม่ว่าง
            }                                                // จบ if
        }                                                    // จบ for
        return true;                                         // ไม่เจอการจองเลย → ห้องว่าง
    }                                                        // จบ isFree

    // ลิสต์ห้องที่ "ว่างวันนี้" (ใช้ในหน้าดูห้องทั้งหมด และ dropdown ในหน้าจองห้อง)
    public static ArrayList<Room> freeRooms(ArrayList<Room> rooms, ArrayList<Booking> bookings, String today) {
        ArrayList<Room> free = new ArrayList<>();            // ลิสต์ว่างไว้เก็บห้องว่าง
        for (Room room : rooms) {                            // วนทุกห้อง
            if (isFree(room, bookings, today)) {             // ถ้าห้องนี้ว่างวันนี้
                free.add(room);                              // เก็บไว้
            }                                                // จบ if
        }                                                    // จบ for
        return free;                                         // คืนลิสต์ห้องว่าง
    }                                                        // จบ freeRooms

    // อ่านไฟล์ใหม่ทั้ง rooms.csv และ bookings.csv แล้วคืนห้องที่ว่างวันนี้
    public static ArrayList<Room> loadFreeRooms() {
        return freeRooms(loadRooms(), BookingService.loadBookings(), BookingService.today());   // อ่านห้อง + อ่านการจอง + วันที่วันนี้
    }                                                        // จบ loadFreeRooms

    // นับห้องที่ "ว่างวันนี้" (การ์ดสถิติในหน้าหลัก) — ใช้เกณฑ์เดียวกับ freeRooms
    public static int countFreeRooms(ArrayList<Room> rooms, ArrayList<Booking> bookings, String today) {
        return freeRooms(rooms, bookings, today).size();     // size() = จำนวนห้องในลิสต์
    }                                                        // จบ countFreeRooms

    // หาห้องจากรหัสห้อง (ไม่เจอคืน null)
    public static Room findRoom(ArrayList<Room> rooms, String roomId) {
        for (Room room : rooms) {                     // วนทุกห้อง
            if (room.getId().equals(roomId)) {        // ถ้ารหัสตรงกัน
                return room;                          // คืนห้องนี้
            }                                         // จบ if
        }                                             // จบ for
        return null;                                  // ไม่เจอ
    }                                                 // จบ findRoom

    // ห้องในลิสต์ rooms ที่ว่างในวันที่ date ช่วง start - end (ใช้กับ dropdown หน้าจองห้อง)
    public static ArrayList<Room> freeRoomsAt(ArrayList<Room> rooms, String date, String start, String end) {
        ArrayList<Booking> bookings = BookingService.loadBookings();   // อ่านการจองทั้งหมดจากไฟล์
        ArrayList<Room> result = new ArrayList<>();                    // ลิสต์ผลลัพธ์
        for (Room room : rooms) {                                      // วนทุกห้อง
            if (BookingService.isFreeAt(bookings, room.getId(), date, start, end)) {   // ไม่มีการจองทับ
                result.add(room);                                      // เก็บไว้
            }                                                          // จบ if
        }                                                              // จบ for
        return result;                                                 // คืนลิสต์ห้องว่าง
    }                                                                  // จบ freeRoomsAt
}                                                            // จบคลาส RoomService
