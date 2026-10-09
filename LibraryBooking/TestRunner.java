import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Test Runner เขียนเอง (ไม่พึ่ง JUnit) ตามแบบ Week 3 — Arrange · Act · Assert
 * ทดสอบเฉพาะตรรกะ (ไม่เปิดหน้าจอ) ใช้ไฟล์ชั่วคราว ไม่แตะ Member.csv จริง
 *
 * รัน:  javac -encoding UTF-8 *.java  แล้ว  java -ea TestRunner
 */
public class TestRunner {

    static int pass = 0, fail = 0;

    static void check(String name, boolean ok) {
        if (ok) { pass++; System.out.println("[PASS] " + name); }
        else    { fail++; System.out.println("[FAIL] " + name); }
    }

    interface ThrowingAction { void run() throws Exception; }

    static boolean throwsException(Class<? extends Throwable> expected, ThrowingAction action) {
        try {
            action.run();
            return false;
        } catch (Throwable t) {
            return expected.isInstance(t);
        }
    }

    static String repeat(char c, int n) {
        return new String(new char[n]).replace('\0', c);
    }

    public static void main(String[] args) throws Exception {
        testMember();
        testCsvRepository();
        testRegistration();
        testAuth();
        testRegisterThenLogin();
        System.out.println("\nรวม: PASS " + pass + " / FAIL " + fail);
        if (fail > 0) System.exit(1);
    }

    static Member sample(String username) {
        return new Member(username, "password1", "สมชาย ใจดี", "a@b.com", "0812345678", Role.MEMBER);
    }

    // ------------------------------------------------------------------ Member
    static void testMember() {
        System.out.println("--- Member");
        Class<IllegalArgumentException> IAE = IllegalArgumentException.class;
        check("username ยาว 2 -> throws", throwsException(IAE, () -> sample("ab")));
        check("username ยาว 3 -> ok", !throwsException(IAE, () -> sample("abc")));
        check("username ยาว 20 -> ok", !throwsException(IAE, () -> sample(repeat('1', 20))));
        check("username ยาว 21 -> throws", throwsException(IAE, () -> sample(repeat('1', 21))));
        check("ชื่อมี ',' -> throws", throwsException(IAE,
                () -> new Member("abc", "pw", "a,b", "a@b.com", "0812345678", Role.MEMBER)));
        check("อีเมลว่าง -> throws", throwsException(IAE,
                () -> new Member("abc", "pw", "ชื่อ", "", "0812345678", Role.MEMBER)));
        check("role null -> throws", throwsException(IAE,
                () -> new Member("abc", "pw", "ชื่อ", "a@b.com", "0812345678", null)));

        Member m = sample("6821651789");
        check("toCsvLine -> fromCsvLine ได้ค่าเดิม (ชื่อไทย)",
                Member.fromCsvLine(m.toCsvLine()).getFullName().equals("สมชาย ใจดี"));
        check("fromCsvLine คอลัมน์ไม่ครบ -> throws", throwsException(IAE, () -> Member.fromCsvLine("a,b,c")));
        check("passwordMatches ถูก", m.passwordMatches("password1"));
        check("passwordMatches ต่างตัวพิมพ์ -> false", !m.passwordMatches("Password1"));
        check("toString ไม่เผยรหัสผ่าน", !m.toString().contains("password1"));
        check("equals ใช้ username", m.equals(sample("6821651789")) && !m.equals(sample("6821651788")));
        check("hashCode สอดคล้องกับ equals", m.hashCode() == sample("6821651789").hashCode());
    }

    // -------------------------------------------------------- CsvMemberRepository
    static void testCsvRepository() throws IOException {
        System.out.println("--- CsvMemberRepository");
        Path dir = Files.createTempDirectory("lbs");
        Path file = dir.resolve("Member.csv");

        CsvMemberRepository empty = new CsvMemberRepository(file.toString());
        check("ไม่มีไฟล์ -> เริ่มจากว่าง ไม่ error", empty.size() == 0);

        empty.save(sample("6821651789"));
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        check("save ครั้งแรกสร้างไฟล์ + หัวตาราง (มี BOM)",
                lines.size() == 2 && lines.get(0).equals('﻿' + Member.CSV_HEADER));
        check("save ครั้งแรกเขียนข้อมูลบรรทัดที่ 2", lines.get(1).startsWith("6821651789,password1,สมชาย ใจดี"));

        empty.save(sample("6821651790"));
        check("save ครั้งที่สองต่อท้าย ไม่เขียนหัวซ้ำ",
                Files.readAllLines(file, StandardCharsets.UTF_8).size() == 3);
        check("save username ซ้ำ -> IllegalStateException",
                throwsException(IllegalStateException.class, () -> empty.save(sample("6821651789"))));
        check("save ซ้ำไม่ทำให้ไฟล์เพิ่มบรรทัด", Files.readAllLines(file, StandardCharsets.UTF_8).size() == 3);

        CsvMemberRepository reloaded = new CsvMemberRepository(file.toString());
        check("เปิดไฟล์ใหม่ อ่าน BOM/หัวตารางถูก ได้ 2 คน", reloaded.size() == 2);
        check("ชื่อภาษาไทยอ่านกลับมาไม่เพี้ยน",
                reloaded.findByUsername("6821651790").get().getFullName().equals("สมชาย ใจดี"));

        Files.write(file, ("\n" + "broken,line\n" + "x,pw,ชื่อ,a@b.com,0812345678,member\n"
                + "abc,pw,ชื่อ,a@b.com,0812345678,guest").getBytes(StandardCharsets.UTF_8),
                java.nio.file.StandardOpenOption.APPEND);
        CsvMemberRepository withBad = new CsvMemberRepository(file.toString());
        check("บรรทัดผิดรูปแบบ 3 บรรทัดถูกข้าม", withBad.getSkippedLines() == 3 && withBad.size() == 2);
        withBad.save(sample("6821651791"));
        check("ไฟล์ไม่มี newline ท้ายไฟล์ -> save แล้วยังแยกบรรทัดถูก",
                new CsvMemberRepository(file.toString()).findByUsername("6821651791").isPresent());
    }

    // ------------------------------------------------------- RegistrationService
    static RegistrationData data(String user, String name, String email, String phone, String pw, String confirm) {
        return new RegistrationData(user, name, email, phone, pw, confirm);
    }

    static RegistrationData valid() {
        return data("6821650001", "สมชาย ใจดี", "somchai@gmail.com", "081-234-5678", "12345678", "12345678");
    }

    /** คืนชื่อช่องที่ผิด หรือ "OK" ถ้าสมัครผ่าน */
    static String registerResult(RegistrationService service, RegistrationData d) {
        try {
            service.register(d);
            return "OK";
        } catch (RegistrationException e) {
            return e.getField().name();
        } catch (IOException e) {
            return "IO";
        }
    }

    /** repository ในหน่วยความจำ — ทดสอบได้โดยไม่แตะไฟล์ (ได้ประโยชน์จาก DIP) */
    static MemberRepository memoryRepo() {
        Map<String, Member> map = new HashMap<>();
        return new MemberRepository() {
            public Optional<Member> findByUsername(String u) { return Optional.ofNullable(map.get(u)); }
            public void save(Member m) { map.put(m.getUsername(), m); }
            public int count() { return map.size(); }
        };
    }

    static void testRegistration() {
        System.out.println("--- RegistrationService (partition + boundary)");
        RegistrationService s = new RegistrationService(memoryRepo());
        RegistrationData v = valid();

        check("username ว่าง -> USERNAME",
                registerResult(s, data(" ", v.fullName, v.email, v.phone, v.password, v.confirmPassword)).equals("USERNAME"));
        check("username มีภาษาไทย -> USERNAME",
                registerResult(s, data("สมชาย", v.fullName, v.email, v.phone, v.password, v.confirmPassword)).equals("USERNAME"));
        check("ชื่อว่าง -> FULL_NAME",
                registerResult(s, data(v.username, "", v.email, v.phone, v.password, v.confirmPassword)).equals("FULL_NAME"));
        check("ชื่อมี ',' -> FULL_NAME",
                registerResult(s, data(v.username, "ก,ข", v.email, v.phone, v.password, v.confirmPassword)).equals("FULL_NAME"));
        check("อีเมลไม่มี @ -> EMAIL",
                registerResult(s, data(v.username, v.fullName, "somchai.gmail.com", v.phone, v.password, v.confirmPassword)).equals("EMAIL"));
        check("อีเมลไม่มีโดเมน -> EMAIL",
                registerResult(s, data(v.username, v.fullName, "somchai@", v.phone, v.password, v.confirmPassword)).equals("EMAIL"));
        check("เบอร์ 8 หลัก -> PHONE",
                registerResult(s, data(v.username, v.fullName, v.email, "08123456", v.password, v.confirmPassword)).equals("PHONE"));
        check("เบอร์มีตัวอักษร -> PHONE",
                registerResult(s, data(v.username, v.fullName, v.email, "08l2345678", v.password, v.confirmPassword)).equals("PHONE"));
        check("เบอร์ไม่ขึ้นต้นด้วย 0 -> PHONE",
                registerResult(s, data(v.username, v.fullName, v.email, "8123456789", v.password, v.confirmPassword)).equals("PHONE"));
        check("รหัส 7 ตัว -> PASSWORD",
                registerResult(s, data(v.username, v.fullName, v.email, v.phone, "1234567", "1234567")).equals("PASSWORD"));
        check("รหัส 51 ตัว -> PASSWORD",
                registerResult(s, data(v.username, v.fullName, v.email, v.phone, repeat('a', 51), repeat('a', 51))).equals("PASSWORD"));
        check("ยืนยันรหัสไม่ตรง -> CONFIRM_PASSWORD",
                registerResult(s, data(v.username, v.fullName, v.email, v.phone, "12345678", "12345679")).equals("CONFIRM_PASSWORD"));

        check("ข้อมูลถูกทั้งหมด (รหัส 8 ตัว) -> OK", registerResult(s, v).equals("OK"));
        check("สมัคร username เดิมซ้ำ -> USERNAME", registerResult(s, v).equals("USERNAME"));
        check("เบอร์ 9 หลักแบบมีขีด -> OK",
                registerResult(s, data("6821650002", v.fullName, v.email, "02-123-4567", v.password, v.confirmPassword)).equals("OK"));
    }

    // --------------------------------------------------------------- AuthService
    static void testAuth() throws IOException {
        System.out.println("--- AuthService");
        MemberRepository repo = memoryRepo();
        repo.save(sample("6821651789"));
        AuthService auth = new AuthService(repo);
        check("รหัสถูก -> ได้ Member", loginResult(auth, "6821651789", "password1").equals("OK"));
        check("มีช่องว่างหน้า-หลังรหัสนิสิต -> trim แล้วผ่าน", loginResult(auth, " 6821651789 ", "password1").equals("OK"));
        check("รหัสผิด -> MSG_WRONG", loginResult(auth, "6821651789", "password2").equals(AuthService.MSG_WRONG));
        check("ไม่มีผู้ใช้ -> MSG_WRONG เหมือนกัน", loginResult(auth, "0000000000", "password1").equals(AuthService.MSG_WRONG));
        check("ช่องว่าง -> MSG_EMPTY", loginResult(auth, "", "").equals(AuthService.MSG_EMPTY));
        check("null -> MSG_EMPTY", loginResult(auth, null, null).equals(AuthService.MSG_EMPTY));
    }

    static String loginResult(AuthService auth, String user, String pass) {
        try {
            auth.login(user, pass);
            return "OK";
        } catch (LoginException e) {
            return e.getMessage();
        }
    }

    // ------------------------------------------- สมัครแล้วล็อกอินได้ (ผ่านไฟล์จริง)
    static void testRegisterThenLogin() throws Exception {
        System.out.println("--- สมัคร -> บันทึกลง Member.csv -> เปิดโปรแกรมใหม่ -> ล็อกอิน");
        Path file = Files.createTempDirectory("lbs").resolve("Member.csv");
        new RegistrationService(new CsvMemberRepository(file.toString())).register(valid());

        AuthService auth = new AuthService(new CsvMemberRepository(file.toString())); // จำลองเปิดโปรแกรมใหม่
        Member m = auth.login("6821650001", "12345678");
        check("ล็อกอินด้วยบัญชีที่เพิ่งสมัครได้", m.getFullName().equals("สมชาย ใจดี"));
        check("เบอร์ถูกบันทึกเป็นตัวเลขล้วน", m.getPhone().equals("0812345678"));
        check("สมาชิกใหม่มี role = MEMBER", m.getRole() == Role.MEMBER);
    }
}
