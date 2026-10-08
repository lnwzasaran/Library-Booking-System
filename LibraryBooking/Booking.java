// การจอง 1 รายการ
public class Booking {
    private String studentId;                         // ใครเป็นคนจอง
    private String roomNo;                            // จองห้องไหน
    private String date;                              // วันที่ รูปแบบ yyyy-MM-dd
    private String time;                              // ช่วงเวลา เช่น 10:00-12:00

    public Booking(String studentId, String roomNo, String date, String time) {
        this.studentId = studentId;
        this.roomNo = roomNo;
        this.date = date;
        this.time = time;
    }

    public String getStudentId() { return this.studentId; }
    public String getRoomNo()    { return this.roomNo; }
    public String getDate()      { return this.date; }
    public String getTime()      { return this.time; }
}
