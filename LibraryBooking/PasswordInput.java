import java.awt.*;
import javax.swing.*;

/** ช่องกรอกรหัสผ่าน หน้าตาเหมือน InputField */
public class PasswordInput extends JPasswordField {

    private final Icon icon;
    private final String placeholder;

    public PasswordInput(String placeholder, Icon icon, Color background) {
        this.placeholder = placeholder;
        this.icon = icon;
        InputStyle.apply(this, icon, background);
        setEchoChar('•');
    }

    public void setError(boolean error) {
        InputStyle.setError(this, error);
    }

    /** @return รหัสที่กรอกเป็น String (สะดวกสำหรับส่งให้ service) */
    public String getPasswordText() {
        return new String(getPassword());
    }

    @Override
    protected void paintComponent(Graphics g) {
        InputStyle.paintBackground(this, g);
        super.paintComponent(g);
        InputStyle.paintDecorations(this, g, icon, placeholder);
    }
}
