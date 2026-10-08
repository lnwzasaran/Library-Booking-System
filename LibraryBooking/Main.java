import javax.swing.*;                                 // JOptionPane

public class Main {                                   // จุดเริ่มโปรแกรม (สไลด์ Section 9 หน้า "สร้าง Form")
    public static void main(String[] args) {
        Student s = DataManager.loadStudent("6821651789");    // โหลดข้อมูลผู้ใช้จากไฟล์ (ภายหลังรับรหัสจากหน้า Login แทนได้)
        if (s == null) {                              // ไม่พบผู้ใช้ในไฟล์
            JOptionPane.showMessageDialog(null, "ไม่พบข้อมูลนักศึกษา (ตรวจสอบไฟล์ Data/students.txt)");
        } else {
            new LibraryForm(s);                       // ส่ง object Student เข้าไปให้หน้าต่างแสดงผล
        }
    }
}
