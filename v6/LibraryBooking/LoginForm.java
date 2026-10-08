import java.awt.*;
import java.util.function.Consumer;
import javax.swing.*;

/**
 * หน้าเข้าสู่ระบบ — ซ้ายเป็นรูปห้องสมุด ขวาเป็นฟอร์ม
 * ตรวจรหัสผ่านด้วย AuthService แล้วแจ้งผลผ่าน callback (Observer)
 * หน้านี้จึงไม่ต้องรู้ว่าหน้าถัดไปคืออะไร
 */
public class LoginForm extends BaseForm {

    private static final int CARD_X = 90, CARD_Y = 45, CARD_W = 1020, CARD_H = 590;
    private static final int IMAGE_W = 600;
    private static final int FORM_W = CARD_W - IMAGE_W;
    private static final int FIELD_X = 40, FIELD_W = FORM_W - 2 * FIELD_X, FIELD_H = 44;

    private final AuthService authService;
    private final String prefillUsername;

    private Consumer<Member> onLoginSuccess = member -> { };
    private Runnable onRegisterRequested = () -> { };

    private InputField tfUsername;
    private PasswordInput pfPassword;
    private JLabel lbStatus;

    /**
     * @param authService     ตัวตรวจสอบการเข้าสู่ระบบ
     * @param prefillUsername ชื่อผู้ใช้ที่ใส่ไว้ให้ล่วงหน้า (เช่นหลังสมัครเสร็จ) หรือ null
     */
    public LoginForm(AuthService authService, String prefillUsername) {
        super("เข้าสู่ระบบ");
        if (authService == null) {
            throw new IllegalArgumentException("authService must not be null");
        }
        this.authService = authService;
        this.prefillUsername = prefillUsername;
    }

    public void setOnLoginSuccess(Consumer<Member> listener) { this.onLoginSuccess = listener; }
    public void setOnRegisterRequested(Runnable listener)    { this.onRegisterRequested = listener; }

    @Override
    protected void setComponent() {
        RoundPanel card = place(cp, new RoundPanel(Theme.PANEL_GRAY, 16, null, 8),
                CARD_X, CARD_Y, CARD_W + 8, CARD_H + 8);
        buildImageSide(card);
        buildFormSide(card);
    }

    // ------------------------------------------------------------ ฝั่งซ้าย
    private void buildImageSide(Container card) {
        ImagePanel side = place(card, new ImagePanel("images/login_bg.jpg",
                new Color(60, 52, 48), new Color(0, 0, 0, 55), 0), 0, 0, IMAGE_W, CARD_H);

        place(side, new JLabel(Icons.of(Icons.Type.BOOK, 78, Theme.WHITE)), 50, 48, 80, 80);
        JLabel title = new JLabel("<html><span style='color:#FFFFFF'>Library</span> "
                + "<span style='color:#4A8BEA'>Booking System</span></html>");
        title.setFont(Theme.bold(34));
        place(side, title, 145, 52, 440, 48);
        place(side, Theme.label("ระบบจองห้องอ่านหนังสือ", Theme.font(16), Theme.WHITE), 148, 102, 420, 24);
        place(side, Theme.label("สำหรับนักศึกษาวิทยาการคอมพิวเตอร์ ภาคพิเศษ", Theme.font(16), Theme.WHITE),
                148, 126, 420, 24);
        place(side, Theme.label("“จองห้องง่าย  เรียนรู้ได้อย่างมีประสิทธิภาพ”", Theme.font(16), Theme.WHITE),
                148, 162, 420, 26);
        place(side, Theme.label("© 2026 Department of Computer Science. All rights reserved.",
                new Font(Font.MONOSPACED, Font.PLAIN, 12), new Color(225, 225, 225)), 24, CARD_H - 40, 500, 20);
    }

    // ------------------------------------------------------------ ฝั่งขวา
    private void buildFormSide(Container card) {
        JPanel side = place(card, new JPanel(null), IMAGE_W, 0, FORM_W, CARD_H);
        side.setOpaque(false);

        place(side, new JLabel(Icons.of(Icons.Type.BOOK, 50, Theme.BLUE), SwingConstants.CENTER), 0, 30, FORM_W, 52);
        place(side, Theme.centeredLabel("ยินดีต้อนรับ", Theme.bold(30), Theme.TEXT), 0, 84, FORM_W, 44);
        place(side, Theme.centeredLabel("เข้าสู่ระบบจองห้องอ่านหนังสือ", Theme.font(15), Theme.TEXT_MUTED),
                0, 126, FORM_W, 24);

        place(side, Theme.label("รหัสนิสิต", Theme.bold(14), Theme.TEXT), FIELD_X, 176, FIELD_W, 22);
        tfUsername = place(side, new InputField("กรอกรหัสนิสิต",
                Icons.of(Icons.Type.USER, 18, Theme.TEXT_MUTED), Theme.WHITE), FIELD_X, 200, FIELD_W, FIELD_H);
        if (prefillUsername != null) tfUsername.setText(prefillUsername);

        place(side, Theme.label("Password", Theme.bold(14), Theme.TEXT), FIELD_X, 258, FIELD_W, 22);
        pfPassword = place(side, new PasswordInput("Password",
                Icons.of(Icons.Type.LOCK, 18, Theme.TEXT_MUTED), Theme.WHITE), FIELD_X, 282, FIELD_W, FIELD_H);

        lbStatus = place(side, Theme.centeredLabel(" ", Theme.font(14), Theme.ERROR), FIELD_X, 334, FIELD_W, 22);

        RoundButton btLogin = place(side, new RoundButton("เข้าสู่ระบบ",
                Icons.of(Icons.Type.LOGIN, 20, Theme.WHITE), RoundButton.Kind.PRIMARY), FIELD_X, 362, FIELD_W, 46);
        btLogin.addActionListener(e -> attemptLogin());
        tfUsername.addActionListener(e -> pfPassword.requestFocusInWindow()); // Enter -> ไปช่องรหัส
        pfPassword.addActionListener(e -> attemptLogin());                     // Enter -> เข้าสู่ระบบ

        place(side, new OrDivider(), FIELD_X, 430, FIELD_W, 24);

        RoundButton btRegister = place(side, new RoundButton("สมัครสมาชิก",
                Icons.of(Icons.Type.PERSON_ADD, 20, Theme.BLUE), RoundButton.Kind.OUTLINE), FIELD_X, 470, FIELD_W, 46);
        btRegister.addActionListener(e -> onRegisterRequested.run());

        place(side, Theme.centeredLabel("สำหรับนักศึกษาวิทยาการคอมพิวเตอร์ ภาคพิเศษ", Theme.font(13), Theme.TEXT_MUTED),
                0, 540, FORM_W, 22);

        SwingUtilities.invokeLater(() ->
                (prefillUsername == null ? tfUsername : pfPassword).requestFocusInWindow());
    }

    // ------------------------------------------------------------ เหตุการณ์
    private void attemptLogin() {
        tfUsername.setError(false);
        pfPassword.setError(false);
        try {
            Member member = authService.login(tfUsername.getText(), pfPassword.getPasswordText());
            lbStatus.setText(" ");
            onLoginSuccess.accept(member);
        } catch (LoginException ex) {
            lbStatus.setText(ex.getMessage());
            if (tfUsername.getText().trim().isEmpty()) {
                tfUsername.setError(true);
                tfUsername.requestFocusInWindow();
            } else {
                pfPassword.setError(true);
                pfPassword.setText("");
                pfPassword.requestFocusInWindow();
            }
        }
    }

    /** เส้นคั่น ——— หรือ ——— */
    private static final class OrDivider extends JComponent {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = Theme.smooth(g);
            String text = "หรือ";
            g2.setFont(Theme.font(13));
            FontMetrics fm = g2.getFontMetrics();
            int textW = fm.stringWidth(text);
            int midY = getHeight() / 2;
            int gap = 22;
            g2.setColor(new Color(190, 194, 200));
            g2.drawLine(0, midY, (getWidth() - textW) / 2 - gap, midY);
            g2.drawLine((getWidth() + textW) / 2 + gap, midY, getWidth(), midY);
            g2.setColor(Theme.TEXT_MUTED);
            g2.drawString(text, (getWidth() - textW) / 2, midY + fm.getAscent() / 2 - 2);
            g2.dispose();
        }
    }
}
