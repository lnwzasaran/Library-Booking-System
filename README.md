# Library-Booking-System
ระบบจองห้องอ่านหนังสือสำหรับนักศึกษาวิทยาการคอมพิวเตอร์ ภาคพิเศษ จำนวน 80 คน พัฒนาด้วยภาษา Java

# สมาชิกและหน้าที่รับผิดชอบ
# สมาชิกคนที่ 1 — User & Authentication

รับผิดชอบระบบผู้ใช้งานและการเข้าสู่ระบบ

ระบบ Login / Logout
ระบบข้อมูลนักศึกษา
ระบบตรวจสอบ Username และ Password
ระบบแสดงข้อมูล Profile
จัดการสิทธิ์การเข้าใช้งานของ User และ Admin

ไฟล์/ส่วนที่รับผิดชอบ

User.java

Login.java

UserService.java

Authentication System

# สมาชิกคนที่ 2 — Room Management

รับผิดชอบระบบจัดการข้อมูลห้องอ่านหนังสือ

แสดงรายการห้องทั้งหมด
แสดงความจุของแต่ละห้อง
ตรวจสอบสถานะห้อง
เพิ่มห้อง
แก้ไขข้อมูลห้อง
ลบห้อง
ตรวจสอบห้องว่างตามวันและเวลา

ไฟล์/ส่วนที่รับผิดชอบ

Room.java
RoomService.java
Room Management UI
# สมาชิกคนที่ 3 — Booking System

รับผิดชอบระบบจองและยกเลิกการจองห้อง

เลือกวันที่และเวลา
เลือกห้องที่ต้องการจอง
ตรวจสอบห้องว่าง
ป้องกันการจองเวลาซ้ำ
ยืนยันการจอง
ยกเลิกการจอง
แสดงประวัติการจองของนักศึกษา

ไฟล์/ส่วนที่รับผิดชอบ

Booking.java
BookingService.java
BookingController.java
Booking System UI
# สมาชิกคนที่ 4 — Admin & Database

รับผิดชอบระบบ Admin และการจัดการฐานข้อมูล

Admin Dashboard
ดูข้อมูลนักศึกษาทั้งหมด
ดูรายการจองทั้งหมด
จัดการข้อมูลห้อง
เชื่อมต่อ Java กับ Database
ออกแบบและจัดการ Database
รวมระบบของสมาชิกทุกคน
ทดสอบระบบโดยรวม

ไฟล์/ส่วนที่รับผิดชอบ

Admin.java
DatabaseConnection.java
AdminController.java
Database
System Integration
# Technologies
Java
JavaFX
MySQL
Git / GitHub
# ระบบหลัก
Login / Authentication
Student Profile
Room Management
Room Booking
Booking History
Cancel Booking
Admin Dashboard
Database Management
# จำนวนผู้ใช้งาน

ระบบรองรับนักศึกษาวิทยาการคอมพิวเตอร์ ภาคพิเศษ จำนวน 80 คน

# Git Branch

สมาชิกแต่ละคนควรทำงานใน Branch ของตัวเองก่อน แล้วจึง commit เข้าสู่ main
