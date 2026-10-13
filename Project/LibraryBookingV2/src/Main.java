import ui.AppFrame;   // หน้าต่างหลักอยู่ในแพ็กเกจ ui

/*
 * จุดเริ่มโปรแกรม — รันไฟล์นี้
 */
public class Main {                               // คลาสหลักที่มีเมธอด main
    public static void main(String[] args) {      // Java เริ่มทำงานจากเมธอดนี้เสมอ
        new AppFrame();                           // สร้างหน้าต่างโปรแกรม (ใน AppFrame สั่ง setVisible(true) เองแล้ว)
    }                                             // จบเมธอด main
}                                                 // จบคลาส Main
