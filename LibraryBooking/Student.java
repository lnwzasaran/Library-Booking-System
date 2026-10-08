// คลาสลูก สืบทอดจาก Person (Inheritance, สไลด์ Section 3) ได้ name / getName() / setName() มาใช้เลย
public class Student extends Person {
    private String studentId;                         // รหัสนักศึกษา (เพิ่มเฉพาะของ Student)

    public Student(String name, String studentId) {
        super(name);                                  // เรียก constructor ของแม่เพื่อเก็บ name (สไลด์ Section 4: super)
        this.studentId = studentId;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
}
