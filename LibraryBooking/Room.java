/**
 * ห้องอ่านหนังสือ 1 ห้อง (immutable)
 * ตอนนี้ยังไม่มีระบบจอง สถานะ "ว่าง/ไม่ว่าง" จึงกำหนดตอนสร้าง
 */
public final class Room {

    // RI: code ไม่ว่าง, capacity > 0
    private final String code;
    private final int capacity;
    private final boolean available;

    public Room(String code, int capacity, boolean available) {
        if (code == null || code.trim().isEmpty()) {
            throw new IllegalArgumentException("room code must not be empty");
        }
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be positive");
        }
        this.code = code;
        this.capacity = capacity;
        this.available = available;
    }

    public String getCode()      { return code; }
    public int getCapacity()     { return capacity; }
    public boolean isAvailable() { return available; }

    /** @return true ถ้ารหัสห้องขึ้นต้นด้วย zone เช่น "A" -> A101..A105 */
    public boolean inZone(String zone) {
        return code.startsWith(zone);
    }
}
