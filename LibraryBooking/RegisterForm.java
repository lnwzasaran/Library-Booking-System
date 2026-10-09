import java.awt.*;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.*;
import javax.swing.text.JTextComponent;

/**
 * หน้าสมัครสมาชิก — กรอก 6 ช่อง แล้วบันทึกลง Member.csv ผ่าน RegistrationService
 * สมัครสำเร็จจะแจ้งผ่าน callback onRegistered เพื่อให้กลับไปหน้าเข้าสู่ระบบ
 */
public class RegisterForm extends BaseForm {

    private static final int SIDE_W = 480;
    private static final int COL1_X = 530, COL2_X = 870, FIELD_W = 300, FIELD_H = 44;
    private static final int[] ROW_Y = {128, 224, 320};
    private static final int BUTTON_W = 420;
    private static final int BUTTON_X = SIDE_W + (WIDTH - SIDE_W - BUTTON_W) / 2;

    private final RegistrationService registrationService;

    private Consumer<Member> onRegistered = member -> { };
    private Runnable onBack = () -> { };

    private InputField tfUsername, tfFullName, tfEmail, tfPhone;
    private PasswordInput pfPassword, pfConfirm;
    private JLabel lbStatus;
    private final Map<RegistrationException.Field, JTextComponent> fieldOf =
            new EnumMap<>(RegistrationException.Field.class);

    public RegisterForm(RegistrationService registrationService) {
        super("สมัครสมาชิก");
        if (registrationService == null) {
            throw new IllegalArgumentException("registrationService must not be null");
        }
        this.registrationService = registrationService;
    }

    public void setOnRegistered(Consumer<Member> listener) { this.onRegistered = listener; }
    public void setOnBack(Runnable listener)                { this.onBack = listener; }

    @Override
    protected void setComponent() {
        buildImageSide();
        buildHeader();
        buildFields();
        buildButtons();
        SwingUtilities.invokeLater(() -> tfUsername.requestFocusInWindow());
    }

    // ------------------------------------------------------------ ฝั่งซ้าย
    private void buildImageSide() {
        ImagePanel side = place(cp, new ImagePanel("images/register_bg.jpg",
                new Color(30, 43, 64), null, 150), 0, 0, SIDE_W, HEIGHT);
        place(side, new JLabel(Icons.of(Icons.Type.BOOK, 70, Theme.WHITE)), 40, 30, 72, 72);
        place(side, Theme.label("Library Booking System", Theme.bold(30), Theme.WHITE), 122, 34, 350, 44);
        place(side, Theme.label("ระบบจองห้องอ่านหนังสือ", Theme.font(15), Theme.WHITE), 124, 80, 350, 22);
        place(side, Theme.label("สำหรับนักศึกษาวิทยาการคอมพิวเตอร์ ภาคพิเศษ", Theme.font(15), Theme.WHITE),
                124, 102, 350, 22);
        place(side, Theme.label("“จองห้องง่าย  เรียนรู้ได้อย่างมีประสิทธิภาพ”", Theme.font(15), Theme.WHITE),
                124, 132, 350, 24);
        place(side, Theme.label("© 2026 Department of Computer Science. All rights reserved.",
                Theme.font(11), new Color(205, 210, 220)), 14, HEIGHT - 28, 400, 18);
    }

    // ------------------------------------------------------------ หัวข้อ
    private void buildHeader() {
        place(cp, new JLabel(Icons.of(Icons.Type.ACCOUNT, 62, Theme.BLUE)), COL1_X - 8, 26, 64, 64);
        place(cp, Theme.label("สมัครสมาชิก", Theme.bold(32), Theme.TEXT), COL1_X + 68, 20, 400, 44);
        place(cp, Theme.label("กรอกข้อมูลของคุณเพื่อสร้างบัญชีผู้ใช้", Theme.font(16), Theme.TEXT_MUTED),
                COL1_X + 70, 62, 450, 26);
    }

    // ------------------------------------------------------------ ช่องกรอก 2 คอลัมน์
    private void buildFields() {
        tfUsername = textField(COL1_X, ROW_Y[0], "Username / รหัสนิสิต", Icons.Type.USER,
                "กรอกรหัสนิสิต หรือ Username");
        tfFullName = textField(COL2_X, ROW_Y[0], "ชื่อ - นามสกุล", Icons.Type.USER, "เช่น สมชาย ใจดี");
        tfEmail = textField(COL1_X, ROW_Y[1], "อีเมล", Icons.Type.MAIL, "example@gmail.com");
        tfPhone = textField(COL2_X, ROW_Y[1], "เบอร์โทรศัพท์", Icons.Type.PHONE, "เช่น 081-234-5678");
        pfPassword = passwordField(COL1_X, ROW_Y[2], "รหัสผ่าน",
                "อย่างน้อย " + RegistrationService.MIN_PASSWORD_LENGTH + " ตัวอักษร");
        pfConfirm = passwordField(COL2_X, ROW_Y[2], "ยืนยันรหัสผ่าน", "กรอกรหัสผ่านอีกครั้ง");

        fieldOf.put(RegistrationException.Field.USERNAME, tfUsername);
        fieldOf.put(RegistrationException.Field.FULL_NAME, tfFullName);
        fieldOf.put(RegistrationException.Field.EMAIL, tfEmail);
        fieldOf.put(RegistrationException.Field.PHONE, tfPhone);
        fieldOf.put(RegistrationException.Field.PASSWORD, pfPassword);
        fieldOf.put(RegistrationException.Field.CONFIRM_PASSWORD, pfConfirm);

        // กด Enter แล้วเลื่อนไปช่องถัดไป ช่องสุดท้ายกด Enter = สมัคร
        JTextComponent[] order = {tfUsername, tfFullName, tfEmail, tfPhone, pfPassword, pfConfirm};
        for (int i = 0; i < order.length - 1; i++) {
            JTextComponent next = order[i + 1];
            ((JTextField) order[i]).addActionListener(e -> next.requestFocusInWindow());
        }
        pfConfirm.addActionListener(e -> attemptRegister());
    }

    private void fieldLabel(int x, int y, String text, Icons.Type icon) {
        place(cp, Theme.iconLabel(text, Icons.of(icon, 24, Theme.TEXT), Theme.font(16), Theme.TEXT),
                x, y, FIELD_W, 28);
    }

    private InputField textField(int x, int y, String label, Icons.Type icon, String placeholder) {
        fieldLabel(x, y, label, icon);
        return place(cp, new InputField(placeholder, null, Theme.WHITE), x, y + 34, FIELD_W, FIELD_H);
    }

    private PasswordInput passwordField(int x, int y, String label, String placeholder) {
        fieldLabel(x, y, label, Icons.Type.LOCK);
        return place(cp, new PasswordInput(placeholder, null, Theme.WHITE), x, y + 34, FIELD_W, FIELD_H);
    }

    // ------------------------------------------------------------ ปุ่ม
    private void buildButtons() {
        lbStatus = place(cp, Theme.centeredLabel(" ", Theme.font(15), Theme.ERROR),
                SIDE_W, 448, WIDTH - SIDE_W, 24);

        RoundButton btRegister = place(cp, new RoundButton("สมัครสมาชิก",
                Icons.of(Icons.Type.PERSON_ADD, 22, Theme.WHITE), RoundButton.Kind.PRIMARY),
                BUTTON_X, 488, BUTTON_W, 48);
        btRegister.addActionListener(e -> attemptRegister());

        RoundButton btBack = place(cp, new RoundButton("กลับไปหน้าเข้าสู่ระบบ",
                Icons.of(Icons.Type.ARROW_BACK, 22, Theme.BLUE), RoundButton.Kind.OUTLINE),
                BUTTON_X, 550, BUTTON_W, 48);
        btBack.setFont(Theme.font(16));
        btBack.addActionListener(e -> onBack.run());
    }

    // ------------------------------------------------------------ เหตุการณ์
    private void attemptRegister() {
        clearErrors();
        RegistrationData data = new RegistrationData(
                tfUsername.getText(), tfFullName.getText(), tfEmail.getText(), tfPhone.getText(),
                pfPassword.getPasswordText(), pfConfirm.getPasswordText());
        try {
            Member member = registrationService.register(data);
            JOptionPane.showMessageDialog(this,
                    "สมัครสมาชิกสำเร็จ!\nกรุณาเข้าสู่ระบบด้วย " + member.getUsername(),
                    "สมัครสมาชิก", JOptionPane.INFORMATION_MESSAGE);
            onRegistered.accept(member);
        } catch (RegistrationException ex) {
            showError(ex.getField(), ex.getMessage());
        } catch (IOException ex) {
            showError(RegistrationException.Field.NONE, "บันทึกไฟล์ไม่สำเร็จ: " + ex.getMessage());
        }
    }

    private void clearErrors() {
        lbStatus.setText(" ");
        for (JTextComponent field : fieldOf.values()) {
            InputStyle.setError(field, false);
        }
    }

    private void showError(RegistrationException.Field field, String message) {
        lbStatus.setText(message);
        JTextComponent target = fieldOf.get(field);
        if (target != null) {
            InputStyle.setError(target, true);
            target.requestFocusInWindow();
            target.selectAll();
        }
    }
}
