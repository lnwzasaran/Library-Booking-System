import java.awt.*;
import javax.swing.*;

/** ช่องกรอกข้อความ มุมโค้ง มีไอคอนและ placeholder (หน้าตาจาก InputStyle) */
public class InputField extends JTextField {

    private final Icon icon;
    private final String placeholder;

    public InputField(String placeholder, Icon icon, Color background) {
        this.placeholder = placeholder;
        this.icon = icon;
        InputStyle.apply(this, icon, background);
    }

    public void setError(boolean error) {
        InputStyle.setError(this, error);
    }

    @Override
    protected void paintComponent(Graphics g) {
        InputStyle.paintBackground(this, g);
        super.paintComponent(g);
        InputStyle.paintDecorations(this, g, icon, placeholder);
    }
}
