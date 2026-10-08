Library Booking System - Login / Register / Home

วิธีรัน (เปิด terminal ในโฟลเดอร์นี้ ต้องมี Member.csv และโฟลเดอร์ images อยู่ด้วย)
  javac -encoding UTF-8 *.java
  java -ea App            เปิดโปรแกรม
  java -ea TestRunner     รันชุดทดสอบ

บัญชีตัวอย่างใน Member.csv
  6821651789 / 12345678   (member)
  admin      / admin123   (admin)

ลำดับหน้าจอ
  Login -> สมัครสมาชิก -> Register -> สมัครสำเร็จ (บันทึกลง Member.csv) -> Login -> Home
  Home -> ออกจากระบบ -> Login

รูปแบบ Member.csv (UTF-8, เปิดด้วย Excel ได้)
  username,password,fullname,email,phone,role
