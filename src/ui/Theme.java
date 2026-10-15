package ui;      // ไฟล์นี้อยู่ในโฟลเดอร์ ui (หน้าจอ / ส่วนประกอบหน้าจอ)

import logic.*;  // ใช้คลาสฝั่ง logic (Room, Booking, BookingService ฯลฯ)
import java.awt.*;   // นำเข้า Color และ Font (อยู่ในแพ็กเกจ java.awt)

/*
 * เก็บสีและฟอนต์ที่ใช้ร่วมกันทั้งโปรแกรม (สีตาม README ข้อ 5)
 * ทุกคลาสเรียกใช้ได้เลย เช่น Theme.PRIMARY หรือ Theme.font(Font.BOLD, 14)
 * ข้อดี: อยากเปลี่ยนสีทั้งโปรแกรม แก้ที่ไฟล์นี้ไฟล์เดียว
 */
public class Theme {   // คลาสเก็บค่าคงที่ ไม่ต้องสร้าง object

    // ---------- สีหลักตาม README ----------
    // static = เรียกผ่านชื่อคลาสได้เลย, final = ค่าคงที่ เปลี่ยนไม่ได้
    // new Color(0x0D47A1) = สร้างสีจากรหัส HEX แบบเดียวกับใน Figma (#0D47A1)
    public static final Color PRIMARY    = new Color(0x0D47A1);   // น้ำเงินเข้ม: ปุ่มหลัก
    public static final Color SECONDARY  = new Color(0x1976D2);   // น้ำเงินสว่าง: เมนูที่เลือก, ไอคอน
    public static final Color BACKGROUND = new Color(0xF5F8FC);   // พื้นหลังหน้าต่าง ๆ (ฟ้าอ่อนมาก)
    public static final Color WHITE      = new Color(0xFFFFFF);   // สีขาว
    public static final Color TEXT       = new Color(0x0F172A);   // สีตัวอักษรหลัก (เกือบดำ)
    public static final Color TEXT_GRAY  = new Color(0x64748B);   // สีตัวอักษรรอง (เทา)
    public static final Color SUCCESS    = new Color(0x22C55E);   // เขียว: สำเร็จ
    public static final Color DANGER     = new Color(0xEF4444);   // แดง: อันตราย/ผิดพลาด
    public static final Color BORDER     = new Color(0xE2E8F0);   // เส้นขอบสีอ่อน

    // ---------- สีที่ใช้ในหน้าต่าง ๆ ----------
    public static final Color SIDEBAR      = new Color(0x0F172A);   // พื้นแถบเมนูซ้าย (น้ำเงินเข้มเกือบดำ)
    public static final Color CARD_OUTLINE = new Color(0xB8BFC8);   // เส้นขอบการ์ด (เทากลาง)
    public static final Color FIELD_BORDER = new Color(0x9CA3AF);   // เส้นขอบช่องกรอกข้อความ
    public static final Color INPUT_BG     = new Color(0xF8FAFC);   // พื้นช่องกรอกข้อความ
    public static final Color HINT         = new Color(0xA0A7B1);   // สีข้อความจาง (placeholder)
    public static final Color LOGIN_PANEL  = new Color(0xF4F5F7);   // พื้นฟอร์มด้านขวาของหน้า Login
    public static final Color WELCOME_BG   = new Color(0xEBEEF2);   // พื้นการ์ดต้อนรับในหน้าหลัก

    // การ์ดสรุปในหน้าหลัก (พื้นอ่อน / สีไอคอน)
    public static final Color STAT_BLUE_BG   = new Color(0xDCE6F0);   // พื้นการ์ด "ห้องทั้งหมด"
    public static final Color STAT_GREEN_BG  = new Color(0xE4F3E6);   // พื้นการ์ด "ห้องว่าง"
    public static final Color STAT_GREEN     = new Color(0x16A34A);   // ไอคอนการ์ด "ห้องว่าง"
    public static final Color STAT_PURPLE_BG = new Color(0xF3E8F6);   // พื้นการ์ด "การจองห้องของฉัน"
    public static final Color STAT_PURPLE    = new Color(0x8E3BA6);   // ไอคอนการ์ด "การจองห้องของฉัน"
    public static final Color STAT_RED_BG    = new Color(0xFDECEC);   // พื้นการ์ด "การจองวันนี้"

    // ปุ่มเมนูลัดในหน้าหลัก
    public static final Color TILE_BLUE   = new Color(0x0A8CFF);   // กล่อง "จองห้อง"
    public static final Color TILE_GREEN  = new Color(0x45C15A);   // กล่อง "ดูห้องทั้งหมด"
    public static final Color TILE_ORANGE = new Color(0xF4B33F);   // กล่อง "การจองของฉัน"
    public static final Color TILE_PURPLE = new Color(0xB356F7);   // กล่อง "โปรไฟล์"

    // ฟอนต์ Tahoma แสดงภาษาไทยได้ และมีในทุกเครื่อง Windows (แบบเดียวกับตัวอย่างในสไลด์บทที่ 12)
    public static Font font(int style, int size) {       // style = Font.PLAIN (ปกติ) หรือ Font.BOLD (ตัวหนา), size = ขนาดตัวอักษร
        return new Font("Tahoma", style, size);          // สร้างฟอนต์ Tahoma ตามแบบและขนาดที่ส่งเข้ามา แล้วคืนค่ากลับไป
    }                                                     // จบเมธอด font
}                                                         // จบคลาส Theme
