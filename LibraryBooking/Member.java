/**
 * Member — ADT แบบ immutable แทนสมาชิกหนึ่งคน (หนึ่งแถวในไฟล์ Member.csv)
 *
 * Creators  : new Member(username, password, fullName, email, phone, role)
 * Observers : getUsername(), getFullName(), getEmail(), getPhone(), getRole(),
 *             passwordMatches(...), toCsvLine()
 * ไม่มี Mutator — สร้างแล้วเปลี่ยนไม่ได้ จึงไม่มีปัญหา aliasing
 */
public final class Member {

    // Abstraction Function:
    //   AF(username, password, fullName, email, phone, role) =
    //     นิสิตชื่อ fullName ที่ล็อกอินด้วย username/password ติดต่อได้ทาง email และ phone
    //     และมีสิทธิ์ตาม role
    // Representation Invariant:
    //   username ตรงกับ USERNAME_PATTERN
    //   password, fullName, email, phone ไม่เป็น null ไม่ว่าง และไม่มี ',' (เพราะเก็บใน CSV)
    //   role != null
    // Safety from rep exposure:
    //   ทุก field เป็น private final และเป็นชนิด immutable (String, enum)
    //   ไม่มี getter ของ password — ภายนอกถามได้แค่ว่ารหัสตรงหรือไม่

    public static final String USERNAME_PATTERN = "[A-Za-z0-9_]{3,20}";

    private final String username;
    private final String password;
    private final String fullName;
    private final String email;
    private final String phone;
    private final Role role;

    /**
     * @throws IllegalArgumentException ถ้าค่าใดผิด Representation Invariant
     */
    public Member(String username, String password, String fullName,
                  String email, String phone, Role role) {
        if (username == null || !username.matches(USERNAME_PATTERN)) {
            throw new IllegalArgumentException("invalid username: " + username);
        }
        requireCsvSafe("password", password);
        requireCsvSafe("fullName", fullName);
        requireCsvSafe("email", email);
        requireCsvSafe("phone", phone);
        if (role == null) {
            throw new IllegalArgumentException("role must not be null");
        }
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        checkRep();
    }

    private static void requireCsvSafe(String name, String value) {
        if (value == null || value.isEmpty() || value.contains(",")
                || value.contains("\n") || value.contains("\r")) {
            throw new IllegalArgumentException("invalid " + name);
        }
    }

    /** ตรวจ RI (ทำงานเมื่อรันด้วย java -ea) */
    private void checkRep() {
        assert username.matches(USERNAME_PATTERN);
        assert !password.isEmpty() && !password.contains(",");
        assert !fullName.isEmpty() && !fullName.contains(",");
        assert !email.isEmpty() && !email.contains(",");
        assert !phone.isEmpty() && !phone.contains(",");
        assert role != null;
    }

    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }
    public Role getRole()       { return role; }

    /** @return true ถ้า attempt ตรงกับรหัสผ่านทุกตัวอักษร (null ถือว่าไม่ตรง) */
    public boolean passwordMatches(String attempt) {
        return password.equals(attempt);
    }

    /** หัวตารางของไฟล์ Member.csv */
    public static final String CSV_HEADER = "username,password,fullname,email,phone,role";

    /** @return หนึ่งบรรทัดของ Member.csv ตามลำดับคอลัมน์ใน CSV_HEADER */
    public String toCsvLine() {
        return String.join(",", username, password, fullName, email, phone,
                role.name().toLowerCase());
    }

    /**
     * แปลงบรรทัด CSV กลับเป็น Member
     * @throws IllegalArgumentException ถ้าจำนวนคอลัมน์ไม่ครบหรือค่าผิดรูปแบบ
     */
    public static Member fromCsvLine(String line) {
        String[] f = line.split(",", -1);
        if (f.length != 6) {
            throw new IllegalArgumentException("expected 6 columns but got " + f.length);
        }
        return new Member(f[0].trim(), f[1], f[2].trim(), f[3].trim(), f[4].trim(),
                Role.fromText(f[5]));
    }

    // สมาชิกสองคนเท่ากันเมื่อ username เดียวกัน (username เป็น key ของระบบ)
    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Member)) return false;
        return username.equals(((Member) other).username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }

    @Override
    public String toString() {
        return "Member(" + username + ", " + fullName + ", " + role + ")"; // ไม่แสดงรหัสผ่าน
    }
}
