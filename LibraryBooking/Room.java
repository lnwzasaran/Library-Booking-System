// ห้องอ่านหนังสือ 1 ห้อง
public class Room {
    private String roomNo;                            // เลขห้อง เช่น R01
    private int capacity;                             // จำนวนที่นั่ง

    public Room(String roomNo, int capacity) {
        this.roomNo = roomNo;
        this.capacity = capacity;
    }

    public String getRoomNo() { return this.roomNo; }
    public int getCapacity()  { return this.capacity; }
}
