// คลาสแม่ (Super Class) ตามตัวอย่าง People ในสไลด์ Section 3
public class Person {
    private String name;                              // private = ซ่อนข้อมูล (Encapsulation, สไลด์ Section 3)

    public Person(String name) {                      // constructor กำหนดค่าเริ่มต้นตอน new (สไลด์ Section 4)
        this.name = name;                             // this.name = attribute ของ object, name = พารามิเตอร์
    }

    public String getName() {                         // Accessor Method: อ่านค่า
        return this.name;
    }

    public void setName(String name) {                // Mutator Method: แก้ค่า
        this.name = name;
    }
}
