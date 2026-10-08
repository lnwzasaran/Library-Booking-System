import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * เก็บสมาชิกในไฟล์ CSV (ค่าเริ่มต้นคือ Member.csv)
 * รูปแบบ : username,password,fullname,email,phone,role  (บรรทัดแรกเป็นหัวตาราง)
 *
 * - อ่าน/เขียนเป็น UTF-8 เสมอ ชื่อภาษาไทยจึงไม่เพี้ยนไม่ว่าเครื่องจะตั้งภาษาอะไร
 * - ไฟล์ใหม่ขึ้นต้นด้วย BOM เพื่อให้เปิดด้วย Excel แล้วเห็นภาษาไทยถูกต้อง
 * - ถ้ายังไม่มีไฟล์ จะเริ่มจากว่าง แล้วสร้างไฟล์ให้ตอนสมัครคนแรก
 */
public class CsvMemberRepository implements MemberRepository {

    private static final char BOM = '﻿';

    private final Path file;
    private final Map<String, Member> membersByUsername = new LinkedHashMap<>();
    private int skippedLines = 0;

    /**
     * @param filePath พาธไฟล์ CSV
     * @throws IOException ถ้ามีไฟล์อยู่แต่อ่านไม่สำเร็จ
     */
    public CsvMemberRepository(String filePath) throws IOException {
        this.file = Paths.get(filePath);
        if (Files.exists(file)) {
            load();
        }
    }

    private void load() throws IOException {
        try (BufferedReader br = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1 && !line.isEmpty() && line.charAt(0) == BOM) {
                    line = line.substring(1);
                }
                if (line.trim().isEmpty() || line.equalsIgnoreCase(Member.CSV_HEADER)) {
                    continue;
                }
                try {
                    Member member = Member.fromCsvLine(line);
                    membersByUsername.put(member.getUsername(), member);
                } catch (IllegalArgumentException e) {
                    skippedLines++;
                    System.err.println("ข้ามบรรทัด " + lineNumber + " ของ " + file + ": " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Optional<Member> findByUsername(String username) {
        return Optional.ofNullable(membersByUsername.get(username));
    }

    @Override
    public void save(Member member) throws IOException {
        if (member == null) {
            throw new IllegalArgumentException("member must not be null");
        }
        if (exists(member.getUsername())) {
            throw new IllegalStateException("username already exists: " + member.getUsername());
        }
        boolean newFile = !Files.exists(file) || Files.size(file) == 0;
        // เขียนลงไฟล์ให้สำเร็จก่อน แล้วค่อยใส่ใน Map — ข้อมูลในหน่วยความจำกับไฟล์จะไม่ขัดกัน
        try (BufferedWriter bw = Files.newBufferedWriter(file, StandardCharsets.UTF_8,
                StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            if (newFile) {
                bw.write(BOM + Member.CSV_HEADER);
                bw.newLine();
            } else if (!endsWithNewline()) {
                bw.newLine();
            }
            bw.write(member.toCsvLine());
            bw.newLine();
        }
        membersByUsername.put(member.getUsername(), member);
    }

    private boolean endsWithNewline() throws IOException {
        byte[] bytes = Files.readAllBytes(file);
        return bytes.length == 0 || bytes[bytes.length - 1] == '\n';
    }

    @Override
    public int count() {
        return membersByUsername.size();
    }

    /** @return จำนวนสมาชิกที่มีอยู่ (ชื่อเดิม ใช้ใน TestRunner) */
    public int size() {
        return count();
    }

    /** @return จำนวนบรรทัดที่ข้ามไปเพราะรูปแบบผิด */
    public int getSkippedLines() {
        return skippedLines;
    }
}
