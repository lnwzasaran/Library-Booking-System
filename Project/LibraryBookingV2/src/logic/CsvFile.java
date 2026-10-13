package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

import java.io.*;                         // File, FileReader, BufferedReader (สไลด์บทที่ 7)
import java.nio.charset.StandardCharsets;  // UTF_8 (ให้อ่านภาษาไทยในไฟล์ได้ถูกต้อง)
import java.util.ArrayList;                // ลิสต์ที่ยาวขึ้นได้เรื่อย ๆ (Java Collection — วิชา Software Construction สัปดาห์ที่ 8)

/*
 * อ่านไฟล์ .csv ทั้งไฟล์ แล้วคืนค่าเป็นลิสต์ของแถว (แต่ละแถว = Array ของคอลัมน์)
 *   - บรรทัดแรกของไฟล์เป็นหัวตาราง (ชื่อคอลัมน์) จะถูกข้ามไป
 *   - คอลัมน์คั่นด้วย , (จุลภาค) ห้ามมี , อยู่ในข้อมูล (ถ้าต้องมีหลายค่าในช่องเดียว ให้คั่นด้วย | แทน)
 *   - ไฟล์อยู่ในโฟลเดอร์ data ข้าง src (หรือใน src/data ก็ได้)
 */
public class CsvFile {   // คลาสรวมเมธอด static ไม่ต้องสร้าง object

    // หาไฟล์ในโฟลเดอร์ data (ข้าง src) ก่อน ถ้าไม่เจอค่อยหาใน src/data
    public static File find(String fileName) {
        File f = new File("data/" + fileName);         // ที่แรก: data/ชื่อไฟล์
        if (!f.exists()) {                              // ถ้าไม่มีไฟล์นี้
            f = new File("src/data/" + fileName);       // ลองที่ src/data/ชื่อไฟล์
        }                                               // จบ if
        return f;                                       // คืนไฟล์ที่หา (อาจยังไม่มีอยู่จริง)
    }                                                   // จบ find

    // อ่านทุกแถวของไฟล์ (ไม่รวมหัวตาราง) ถ้าอ่านไม่ได้จะคืนลิสต์ว่าง โปรแกรมไม่พัง
    public static ArrayList<String[]> read(String fileName) {
        ArrayList<String[]> rows = new ArrayList<>();   // ลิสต์ว่างไว้เก็บผลลัพธ์
        File f = find(fileName);                        // หาไฟล์
        try {                                           // การอ่านไฟล์ต้องอยู่ใน try-catch (สไลด์บทที่ 7)
            FileReader fr = new FileReader(f, StandardCharsets.UTF_8);   // เปิดช่องทางอ่านไฟล์ แบบ UTF-8
            BufferedReader br = new BufferedReader(fr);                  // ตัวอ่านทีละบรรทัด
            String line = br.readLine();                // อ่านบรรทัดแรก (หัวตาราง) ทิ้งไป
            while ((line = br.readLine()) != null) {    // อ่านบรรทัดถัดไปเรื่อย ๆ จนหมดไฟล์ (null = หมด)
                if (!line.trim().equals("")) {          // ข้ามบรรทัดว่าง
                    rows.add(line.split(","));          // แยกคอลัมน์ด้วย , แล้วเก็บเป็น 1 แถว
                }                                       // จบ if
            }                                           // จบ while
            br.close();                                 // ปิดตัวอ่าน
            fr.close();                                 // ปิดไฟล์
        } catch (Exception e) {                         // ถ้าอ่านไม่ได้ (เช่น ไม่มีไฟล์)
            System.out.println("อ่านไฟล์ " + f.getPath() + " ไม่ได้: " + e);   // แจ้งใน console
        }                                               // จบ try-catch
        return rows;                                    // คืนลิสต์ของแถว
    }                                                   // จบ read

    // เพิ่ม 1 บรรทัดต่อท้ายไฟล์ (ถ้ายังไม่มีไฟล์ จะสร้างใหม่พร้อมหัวตาราง header)
    // วิธี: อ่านทุกบรรทัดเดิมเก็บไว้ → เพิ่มบรรทัดใหม่ → เขียนกลับทั้งไฟล์
    // (ป้องกันกรณีบรรทัดสุดท้ายของไฟล์ไม่มีการขึ้นบรรทัดใหม่ แล้วข้อมูลไปต่อท้ายบรรทัดเดิม)
    // คืนค่า true = บันทึกสำเร็จ, false = บันทึกไม่ได้
    public static boolean appendLine(String fileName, String header, String newLine) {
        File f = find(fileName);                        // หาไฟล์ (data/ หรือ src/data/)
        ArrayList<String> lines = new ArrayList<>();    // เก็บบรรทัดเดิมทั้งหมด
        try {                                           // อ่าน/เขียนไฟล์ต้องอยู่ใน try-catch
            if (f.exists()) {                           // ถ้ามีไฟล์อยู่แล้ว
                BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8));   // เปิดอ่านแบบ UTF-8
                String line;                            // บรรทัดที่อ่านได้
                while ((line = br.readLine()) != null) {   // อ่านจนหมดไฟล์
                    if (!line.trim().equals("")) {      // ข้ามบรรทัดว่าง
                        lines.add(line);                // เก็บบรรทัดเดิม
                    }                                   // จบ if
                }                                       // จบ while
                br.close();                             // ปิดตัวอ่าน
            } else {                                    // ถ้ายังไม่มีไฟล์
                if (f.getParentFile() != null) {        // ถ้ามีโฟลเดอร์แม่ (เช่น data)
                    f.getParentFile().mkdirs();         // สร้างโฟลเดอร์ให้ (ถ้ายังไม่มี)
                }                                       // จบ if
                lines.add(header);                      // บรรทัดแรก = หัวตาราง
            }                                           // จบ if
            lines.add(newLine);                         // เพิ่มบรรทัดใหม่ต่อท้าย

            BufferedWriter bw = new BufferedWriter(new FileWriter(f, StandardCharsets.UTF_8));   // เปิดเขียนทับทั้งไฟล์ แบบ UTF-8 (บทที่ 7)
            for (String line : lines) {                 // วนทุกบรรทัด
                bw.write(line);                         // เขียนข้อมูล
                bw.newLine();                           // ขึ้นบรรทัดใหม่
            }                                           // จบ for
            bw.close();                                 // ปิดตัวเขียน (ข้อมูลถูกบันทึกลงไฟล์ตอนนี้)
            return true;                                // บันทึกสำเร็จ
        } catch (Exception e) {                         // ถ้าเขียนไม่ได้
            System.out.println("บันทึกไฟล์ " + f.getPath() + " ไม่ได้: " + e);   // แจ้งใน console
            return false;                               // บันทึกไม่สำเร็จ
        }                                               // จบ try-catch
    }                                                   // จบ appendLine

    // เขียนทับทั้งไฟล์: บรรทัดแรก = header แล้วตามด้วยทุกบรรทัดใน lines (ใช้ตอนแก้ข้อมูลแถวเดิม เช่น ยกเลิกการจอง)
    // คืนค่า true = บันทึกสำเร็จ, false = บันทึกไม่ได้
    public static boolean writeAll(String fileName, String header, ArrayList<String> lines) {
        File f = find(fileName);                        // หาไฟล์ (data/ หรือ src/data/)
        try {                                           // เขียนไฟล์ต้องอยู่ใน try-catch (บทที่ 7)
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, StandardCharsets.UTF_8));   // เปิดเขียนทับทั้งไฟล์ แบบ UTF-8
            bw.write(header);                           // บรรทัดแรก = หัวตาราง
            bw.newLine();                               // ขึ้นบรรทัดใหม่
            for (String line : lines) {                 // วนทุกบรรทัดข้อมูล
                bw.write(line);                         // เขียนข้อมูล
                bw.newLine();                           // ขึ้นบรรทัดใหม่
            }                                           // จบ for
            bw.close();                                 // ปิดตัวเขียน (ข้อมูลถูกบันทึกลงไฟล์ตอนนี้)
            return true;                                // บันทึกสำเร็จ
        } catch (Exception e) {                         // ถ้าเขียนไม่ได้
            System.out.println("บันทึกไฟล์ " + f.getPath() + " ไม่ได้: " + e);   // แจ้งใน console
            return false;                               // บันทึกไม่สำเร็จ
        }                                               // จบ try-catch
    }                                                   // จบ writeAll
}                                                       // จบคลาส CsvFile
