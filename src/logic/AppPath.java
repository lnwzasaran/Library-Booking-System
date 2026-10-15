package logic;   // ไฟล์นี้อยู่ในโฟลเดอร์ logic (ข้อมูล + กฎ + อ่าน/เขียนไฟล์ csv — ไม่มีโค้ดหน้าจอ)

import java.io.*;                          // File, BufferedWriter, FileWriter
import java.nio.charset.StandardCharsets;   // UTF_8 (ภาษาไทยในไฟล์ไม่เพี้ยน)
import java.util.ArrayList;                 // คิวของโฟลเดอร์ที่ต้องค้น

/*
 * หาโฟลเดอร์ของโปรแกรม (data, icons) ให้เจอไม่ว่าจะเปิดโปรแกรมจากที่ไหน / เครื่องไหน
 * เพราะแต่ละเครื่อง / แต่ละ IDE เริ่มโปรแกรมจากโฟลเดอร์ไม่เหมือนกัน และแต่ละคนอาจวางโฟลเดอร์ data ไว้คนละที่
 *
 * ลำดับการหา (เจอที่ไหนใช้ที่นั่น และจำไว้ ไม่ต้องหาซ้ำ):
 *   1) โฟลเดอร์ที่เปิดโปรแกรม และ src ข้างใน
 *   2) โฟลเดอร์ที่ไฟล์ .class / .jar ของโปรแกรมอยู่ และโฟลเดอร์แม่ขึ้นไป 4 ชั้น (และ src ของแต่ละชั้น)
 *   3) ค้นลงไปในโฟลเดอร์ย่อยของโฟลเดอร์ที่เปิดโปรแกรม ลึกสุด 4 ชั้น
 * ถ้าหาโฟลเดอร์ data ไม่เจอเลย → สร้างโฟลเดอร์ data ใหม่ข้าง ๆ ที่เปิดโปรแกรม พร้อมไฟล์เริ่มต้นครบ 4 ไฟล์
 * ถ้าเจอโฟลเดอร์ data แต่ไฟล์ไหนหายไป → สร้างไฟล์นั้นใหม่ (ไฟล์ที่มีอยู่แล้วไม่ถูกแตะ)
 * ตอนเริ่มโปรแกรมจะพิมพ์ใน console ว่าใช้โฟลเดอร์ data ที่ไหน (ใช้ตรวจเวลาเครื่องอื่นหาไฟล์ไม่เจอ)
 */
public class AppPath {

    private static File dataFolder = null;    // โฟลเดอร์ data ที่หาเจอแล้ว (จำไว้)
    private static File iconsFolder = null;   // โฟลเดอร์ icons ที่หาเจอแล้ว (จำไว้)

    // ไฟล์เริ่มต้นในโฟลเดอร์ data: {ชื่อไฟล์, เนื้อหา} — ใช้ตอนไฟล์ยังไม่มี
    private static final String[][] DEFAULT_FILES = {
        {"admins.csv",   "adminId,password,name\nadmin,admin1234,นางสาวอัจฉราพร สุขศรี\n"},
        {"rooms.csv",    "roomId,capacity,equipment\n"
                       + "A101,6,โต๊ะ|เก้าอี้|TV\nA102,6,โต๊ะ|เก้าอี้|TV\nA103,6,โต๊ะ|เก้าอี้|TV\nA104,6,โต๊ะ|เก้าอี้|TV\nA105,6,โต๊ะ|เก้าอี้|TV\n"
                       + "B101,6,โต๊ะ|เก้าอี้|TV\nB102,6,โต๊ะ|เก้าอี้|TV\nB103,6,โต๊ะ|เก้าอี้|TV\nB104,6,โต๊ะ|เก้าอี้|TV\nB105,6,โต๊ะ|เก้าอี้|TV\n"},
        {"users.csv",    "studentId,fullName,email,phone,password\n"},
        {"bookings.csv", "bookingId,studentId,roomId,date,startTime,endTime,people,status\n"}
    };

    // โฟลเดอร์ data (หาครั้งแรกครั้งเดียว ไม่เจอ → สร้างใหม่พร้อมไฟล์เริ่มต้น)
    public static File dataFolder() {
        if (dataFolder == null) {                                    // ยังไม่เคยหา
            dataFolder = search("data", "rooms.csv");                // หาโฟลเดอร์ data ที่มีไฟล์ rooms.csv
            if (dataFolder == null) {                                // ไม่มีไฟล์ rooms.csv ที่ไหนเลย
                dataFolder = search("data", null);                   // หาโฟลเดอร์ชื่อ data เฉย ๆ
            }                                                        // จบ if
            if (dataFolder == null) {                                // ไม่เจอเลยจริง ๆ
                dataFolder = new File("data").getAbsoluteFile();     // ใช้ data ข้าง ๆ ที่เปิดโปรแกรม
                System.out.println("[data] folder not found -> created new: " + dataFolder);   // แจ้งใน console
            }                                                        // จบ if
            createMissingFiles(dataFolder);                          // ไฟล์ไหนไม่มี สร้างให้ (ไฟล์เดิมไม่ถูกแตะ)
            System.out.println("[data] using: " + dataFolder.getAbsolutePath());   // บอกว่าใช้ไฟล์ csv จากที่ไหน
        }                                                            // จบ if
        return dataFolder;                                           // คืนโฟลเดอร์ data
    }                                                                // จบ dataFolder

    // โฟลเดอร์ icons (ไม่เจอ → คืน null แล้ว IconPanel จะวาดไอคอนเองแทน)
    public static File iconsFolder() {
        if (iconsFolder == null) {                                   // ยังไม่เคยหา
            iconsFolder = search("icons", null);                     // หาโฟลเดอร์ชื่อ icons
            if (iconsFolder == null) {                               // ไม่เจอ
                iconsFolder = new File("icons");                     // ใช้ชื่อเดิม (โหลดรูปไม่ได้ → วาดเอง)
                System.out.println("[icons] folder not found -> icons will be blank");   // แจ้งใน console
            }                                                        // จบ if
        }                                                            // จบ if
        return iconsFolder;                                          // คืนโฟลเดอร์ icons
    }                                                                // จบ iconsFolder

    // หาโฟลเดอร์ชื่อ folderName (ถ้า mustHave ไม่เป็น null ต้องมีไฟล์ชื่อนั้นอยู่ข้างในด้วย) — ไม่เจอคืน null
    private static File search(String folderName, String mustHave) {
        ArrayList<File> places = new ArrayList<>();                  // ที่ที่จะลองดูตามลำดับ
        File here = new File("").getAbsoluteFile();                  // โฟลเดอร์ที่เปิดโปรแกรม
        places.add(here);                                            // 1) โฟลเดอร์ที่เปิดโปรแกรม
        places.add(new File(here, "src"));                           //    และ src ข้างใน
        File dir = programFolder();                                  // 2) โฟลเดอร์ที่ไฟล์ .class / .jar อยู่
        for (int i = 0; i < 5 && dir != null; i++) {                 //    และขึ้นไปทีละชั้น (สูงสุด 4 ชั้น)
            places.add(dir);                                         //    โฟลเดอร์ชั้นนี้
            places.add(new File(dir, "src"));                        //    src ของชั้นนี้
            dir = dir.getParentFile();                               //    ขึ้นไปโฟลเดอร์แม่
        }                                                            // จบ for
        for (File place : places) {                                  // วนลองทีละที่
            File f = new File(place, folderName);                    // เช่น .../data
            if (isGood(f, mustHave)) {                               // เป็นโฟลเดอร์ที่ต้องการ
                return f;                                            // ใช้ที่นี่
            }                                                        // จบ if
        }                                                            // จบ for
        return searchDown(here, folderName, mustHave, 4);            // 3) ค้นลงไปในโฟลเดอร์ย่อย ลึกสุด 4 ชั้น (เลือกอันที่เป็นของโปรเจกต์นี้ก่อน)
    }                                                                // จบ search

    // ค้นลงไปในโฟลเดอร์ย่อยของ dir ลึกสุด depth ชั้น — ถ้าเจอหลายที่ (เช่น workspace มีหลายโปรเจกต์)
    // เลือกโฟลเดอร์ที่อยู่คู่กับ src ของโปรเจกต์นี้ก่อน (src ที่มีไฟล์ logic/AppPath.java)
    private static File searchDown(File dir, String folderName, String mustHave, int depth) {
        ArrayList<File> found = new ArrayList<>();                   // ทุกโฟลเดอร์ที่ชื่อตรงและใช้ได้
        collect(dir, folderName, mustHave, depth, found);            // ค้นเก็บไว้ทั้งหมด
        for (File f : found) {                                       // รอบแรก: หาโฟลเดอร์ที่เป็นของโปรเจกต์นี้
            if (belongsToThisProject(f)) {                           // อยู่คู่กับ src ของโปรเจกต์นี้
                return f;                                            // ใช้อันนี้
            }                                                        // จบ if
        }                                                            // จบ for
        if (found.size() > 0) {                                      // ไม่มีอันไหนชัดเจน
            return found.get(0);                                     // ใช้อันแรกที่เจอ
        }                                                            // จบ if
        return null;                                                 // ไม่เจอเลย
    }                                                                // จบ searchDown

    // เก็บทุกโฟลเดอร์ชื่อ folderName ที่อยู่ใต้ dir (ลึกสุด depth ชั้น ข้ามโฟลเดอร์ซ่อน เช่น .git) ลงใน found
    private static void collect(File dir, String folderName, String mustHave, int depth, ArrayList<File> found) {
        File[] children = dir.listFiles();                           // ของทั้งหมดในโฟลเดอร์นี้
        if (children == null || depth == 0) {                        // อ่านไม่ได้ หรือ ลึกเกินแล้ว
            return;                                                  // หยุด
        }                                                            // จบ if
        for (File c : children) {                                    // ดูทีละอัน
            if (!c.isDirectory() || c.getName().startsWith(".")) {   // ไม่ใช่โฟลเดอร์ หรือเป็นโฟลเดอร์ซ่อน
                continue;                                            // ข้าม
            }                                                        // จบ if
            if (c.getName().equals(folderName) && isGood(c, mustHave)) {   // ชื่อตรงและใช้ได้
                found.add(c);                                        // เก็บไว้
            } else {                                                 // โฟลเดอร์อื่น
                collect(c, folderName, mustHave, depth - 1, found);  // ค้นต่อในโฟลเดอร์ย่อย (เรียกตัวเองซ้ำ)
            }                                                        // จบ if
        }                                                            // จบ for
    }                                                                // จบ collect

    // โฟลเดอร์ f (เช่น data) เป็นของโปรเจกต์นี้ไหม = ข้าง ๆ มี src/logic/AppPath.java หรือ f อยู่ใน src ที่มี logic/AppPath.java
    private static boolean belongsToThisProject(File f) {
        File parent = f.getParentFile();                                         // โฟลเดอร์ที่ f อยู่
        if (parent == null) {                                                    // ไม่มีโฟลเดอร์แม่
            return false;                                                        // ไม่ใช่
        }                                                                        // จบ if
        return new File(parent, "src/logic/AppPath.java").isFile()               // data อยู่ข้าง src
            || new File(parent, "logic/AppPath.java").isFile();                  // data อยู่ใน src
    }                                                                            // จบ belongsToThisProject

    // f เป็นโฟลเดอร์ที่ใช้ได้ไหม (มีอยู่จริง และมีไฟล์ mustHave ข้างใน ถ้ากำหนดมา)
    private static boolean isGood(File f, String mustHave) {
        if (!f.isDirectory()) {                                      // ไม่มีโฟลเดอร์นี้
            return false;                                            // ใช้ไม่ได้
        }                                                            // จบ if
        return mustHave == null || new File(f, mustHave).isFile();   // ไม่ต้องเช็กไฟล์ หรือ มีไฟล์นั้นอยู่
    }                                                                // จบ isGood

    // สร้างไฟล์เริ่มต้นที่ยังไม่มี (ไฟล์ที่มีอยู่แล้วไม่ถูกเขียนทับ)
    private static void createMissingFiles(File folder) {
        folder.mkdirs();                                             // สร้างโฟลเดอร์ (ถ้ายังไม่มี)
        for (String[] file : DEFAULT_FILES) {                        // วนทุกไฟล์เริ่มต้น
            File f = new File(folder, file[0]);                      // เช่น data/rooms.csv
            if (!f.exists()) {                                       // ยังไม่มีไฟล์นี้
                try {                                                // เขียนไฟล์ต้องอยู่ใน try-catch (บทที่ 7)
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f, StandardCharsets.UTF_8));   // เปิดเขียนแบบ UTF-8
                    bw.write(file[1]);                               // เขียนเนื้อหาเริ่มต้น
                    bw.close();                                      // ปิดไฟล์
                    System.out.println("[data] created default file: " + f.getAbsolutePath());   // แจ้งใน console
                } catch (Exception e) {                              // เขียนไม่ได้ (เช่น โฟลเดอร์ห้ามเขียน)
                    System.out.println("[data] cannot create " + f + ": " + e);   // แจ้งใน console
                }                                                    // จบ try-catch
            }                                                        // จบ if
        }                                                            // จบ for
    }                                                                // จบ createMissingFiles

    // ใช้กับไฟล์ไอคอน เช่น find("icons/door_25_118_210.png") (คงไว้ให้โค้ดเดิมเรียกได้)
    public static File find(String name) {
        if (name.startsWith("icons/")) {                             // ไฟล์ในโฟลเดอร์ icons
            return new File(iconsFolder(), name.substring(6));       // ตัด "icons/" ออก แล้วต่อกับโฟลเดอร์ icons ที่หาเจอ
        }                                                            // จบ if
        if (name.equals("data")) {                                   // ขอโฟลเดอร์ data
            return dataFolder();                                     // ใช้โฟลเดอร์ data ที่หาเจอ / สร้างใหม่
        }                                                            // จบ if
        return new File(name);                                       // อย่างอื่น: ต่อจากโฟลเดอร์ที่เปิดโปรแกรม
    }                                                                // จบ find

    // โฟลเดอร์ที่โปรแกรมถูกโหลดมา (โฟลเดอร์ของไฟล์ .class หรือโฟลเดอร์ที่มีไฟล์ .jar) — หาไม่ได้คืน null
    private static File programFolder() {
        try {                                                                        // บางระบบอาจไม่ให้อ่านตำแหน่งนี้
            File where = new File(AppPath.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());                         // ตำแหน่งที่โหลดคลาสนี้มา
            if (where.isFile()) {                                                    // ถ้าเป็นไฟล์ .jar
                return where.getParentFile();                                        // ใช้โฟลเดอร์ที่ไฟล์ .jar อยู่
            }                                                                        // จบ if
            return where;                                                            // เป็นโฟลเดอร์ของไฟล์ .class
        } catch (Exception e) {                                                      // อ่านไม่ได้
            return null;                                                             // ไม่ใช้วิธีนี้
        }                                                                            // จบ try-catch
    }                                                                                // จบ programFolder
}                                                                                    // จบคลาส AppPath
