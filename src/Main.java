import ui.AppFrame;   // หน้าต่างหลักอยู่ในแพ็กเกจ ui

/*
 * จุดเริ่มโปรแกรม — รันไฟล์นี้
 */
public class Main {                               // คลาสหลักที่มีเมธอด main
    public static void main(String[] args) {      // Java เริ่มทำงานจากเมธอดนี้เสมอ
        // หน้าต่างโปรแกรมกว้าง 1440 x 900 จุดพอดี (วางทุกอย่างด้วย setBounds)
        // แต่ Windows มักตั้งย่อ/ขยาย (Scale) ไว้ 125% หรือ 150% → Java ขยายหน้าต่างตาม ทำให้ล้นจอ (เห็นไม่ครบ)
        // จึงสั่งให้ Java ไม่ขยายเพิ่ม (1.0 = 1 จุดในโปรแกรม = 1 จุดบนจอ) ต้องสั่งก่อนสร้างหน้าต่าง
        // ถ้าตอนรันกำหนดเองไว้แล้ว (เช่น java -Dsun.java2d.uiScale=1.25 ...) จะใช้ค่าที่กำหนดเอง
        if (System.getProperty("sun.java2d.uiScale") == null) {   // ยังไม่มีใครกำหนดค่านี้
            System.setProperty("sun.java2d.uiScale", "1.0");      // ไม่ขยายตามค่า Scale ของ Windows
        }                                                         // จบ if
        new AppFrame();                           // สร้างหน้าต่างโปรแกรม (ใน AppFrame สั่ง setVisible(true) เองแล้ว)
    }                                             // จบเมธอด main
}                                                 // จบคลาส Main
