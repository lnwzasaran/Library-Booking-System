package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

/*
 * ข้อมูลห้อง 1 ห้อง (1 แถวใน rooms.csv)
 * คอลัมน์: roomId, capacity, equipment
 * private final = สร้างแล้วแก้ค่าไม่ได้ (immutable — วิชา Software Construction สัปดาห์ที่ 5)
 */
public class Room {

    private final String id;          // รหัสห้อง เช่น A101
    private final int capacity;       // ความจุ (คน)
    private final String equipment;   // อุปกรณ์ เช่น "โต๊ะ|เก้าอี้|TV"

    public Room(String id, int capacity, String equipment) {   // คอนสตรักเตอร์
        this.id = id;                                          // เก็บรหัสห้อง
        this.capacity = capacity;                              // เก็บความจุ
        this.equipment = equipment;                            // เก็บอุปกรณ์
    }                                                          // จบคอนสตรักเตอร์

    // สร้าง Room จาก 1 แถวของ CSV (cols[0] = roomId, cols[1] = capacity, cols[2] = equipment)
    public static Room fromCsv(String[] cols) {
        int capacity = Integer.parseInt(cols[1].trim());       // แปลงข้อความ "6" เป็นตัวเลข 6
        return new Room(cols[0].trim(), capacity, cols[2].trim());   // trim() = ตัดช่องว่างหัวท้าย
    }                                                          // จบ fromCsv

    public String getId()        { return id; }          // คืนรหัสห้อง
    public int getCapacity()     { return capacity; }    // คืนความจุ
    public String getEquipment() { return equipment; }   // คืนอุปกรณ์

    // อุปกรณ์แบบอ่านง่ายสำหรับแสดงบนการ์ด: "โต๊ะ|เก้าอี้|TV" → "โต๊ะ , เก้าอี้ , TV"
    public String getEquipmentText() {
        return equipment.replace("|", " , ");            // เปลี่ยนตัวคั่น | เป็น " , "
    }                                                    // จบ getEquipmentText
}                                                        // จบคลาส Room
