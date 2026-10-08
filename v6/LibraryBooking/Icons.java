import java.awt.*;
import java.awt.geom.*;
import javax.swing.Icon;

/**
 * ไอคอนทั้งหมดของโปรแกรม วาดเองด้วย Java2D (ไม่ต้องมีไฟล์รูป ขยายเท่าไรก็คม)
 * ทุกไอคอนวาดบนพื้นที่สมมติขนาด 24 x 24 แล้วย่อ/ขยายตาม size ที่ขอ
 *
 * ใช้งาน:  Icons.of(Icons.Type.USER, 20, Theme.TEXT)
 */
public final class Icons {

    private Icons() { }

    public enum Type {
        BOOK, USER, LOCK, MAIL, PHONE, LOGIN, PERSON_ADD, ARROW_BACK, LOGOUT,
        HOME, BUILDING, HOUSE_PLUS, CALENDAR, CALENDAR_CHECK, DOOR, LIST, LIST_ADD, ACCOUNT,
        BELL, GROUP_ADD, DOOR_FILLED, DOOR_LOCK, DOOR_OPEN
    }

    /** @return Icon ขนาด size x size พิกเซล สี color */
    public static Icon of(Type type, int size, Color color) {
        return new VectorIcon(type, size, color);
    }

    private static final class VectorIcon implements Icon {
        private final Type type;
        private final int size;
        private final Color color;

        VectorIcon(Type type, int size, Color color) {
            this.type = type;
            this.size = size;
            this.color = color;
        }

        @Override public int getIconWidth()  { return size; }
        @Override public int getIconHeight() { return size; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = Theme.smooth(g);
            g2.translate(x, y);
            g2.scale(size / 24.0, size / 24.0);
            g2.setColor(color);
            g2.setStroke(stroke(1.8f));
            draw(type, g2);
            g2.dispose();
        }
    }

    private static BasicStroke stroke(float width) {
        return new BasicStroke(width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    }

    private static Path2D polyline(double... xy) {
        Path2D p = new Path2D.Double();
        p.moveTo(xy[0], xy[1]);
        for (int i = 2; i < xy.length; i += 2) p.lineTo(xy[i], xy[i + 1]);
        return p;
    }

    private static Shape circle(double cx, double cy, double r) {
        return new Ellipse2D.Double(cx - r, cy - r, 2 * r, 2 * r);
    }

    private static void draw(Type type, Graphics2D g) {
        switch (type) {
            case BOOK:           book(g); break;
            case USER:           person(g, 12, 0); break;
            case LOCK:           lock(g); break;
            case MAIL:           mail(g); break;
            case PHONE:          phone(g); break;
            case LOGIN:          login(g); break;
            case PERSON_ADD:     personAdd(g); break;
            case ARROW_BACK:     arrowBack(g); break;
            case LOGOUT:         logout(g); break;
            case HOME:           home(g); break;
            case BUILDING:       building(g); break;
            case HOUSE_PLUS:     housePlus(g); break;
            case CALENDAR:       calendar(g, true); break;
            case CALENDAR_CHECK: calendarCheck(g); break;
            case DOOR:           door(g); break;
            case LIST:           list(g); break;
            case LIST_ADD:       listAdd(g); break;
            case ACCOUNT:        account(g); break;
            case BELL:           bell(g); break;
            case GROUP_ADD:      groupAdd(g); break;
            case DOOR_FILLED:    doorFilled(g); break;
            case DOOR_LOCK:      doorLock(g); break;
            case DOOR_OPEN:      doorOpen(g); break;
        }
    }

    // ---------------------------------------------------------------- shapes
    /** หนังสือเปิด (โลโก้) */
    private static void book(Graphics2D g) {
        Path2D left = new Path2D.Double();
        left.moveTo(11.3, 6.2);
        left.curveTo(8.6, 4.6, 5.0, 4.1, 1.5, 4.6);
        left.lineTo(1.5, 17.8);
        left.curveTo(5.0, 17.3, 8.6, 17.8, 11.3, 19.2);
        left.closePath();
        g.fill(left);
        g.fill(AffineTransform.getTranslateInstance(24, 0).createTransformedShape(
                AffineTransform.getScaleInstance(-1, 1).createTransformedShape(left)));
        g.setStroke(stroke(1.3f));
        Path2D base = new Path2D.Double();
        base.moveTo(1.2, 19.6);
        base.curveTo(5.5, 19.0, 9.5, 19.6, 12, 21.2);
        base.curveTo(14.5, 19.6, 18.5, 19.0, 22.8, 19.6);
        g.draw(base);
    }

    /** คน (หัวกลม + ไหล่) ใช้ตำแหน่ง cx เป็นกึ่งกลาง */
    private static void person(Graphics2D g, double cx, double dy) {
        g.fill(circle(cx, 7.5 + dy, 4.2));
        g.fill(new RoundRectangle2D.Double(cx - 8, 13.6 + dy, 16, 7.6, 7.6, 7.6));
    }

    private static void lock(Graphics2D g) {
        Area body = new Area(new RoundRectangle2D.Double(4.5, 10, 15, 11.5, 3, 3));
        body.subtract(new Area(circle(12, 15.2, 1.7)));
        g.fill(body);
        g.setStroke(stroke(2.2f));
        Path2D shackle = new Path2D.Double();
        shackle.moveTo(8, 10.5);
        shackle.lineTo(8, 7.5);
        shackle.curveTo(8, 2.2, 16, 2.2, 16, 7.5);
        shackle.lineTo(16, 10.5);
        g.draw(shackle);
    }

    private static void mail(Graphics2D g) {
        Area env = new Area(new RoundRectangle2D.Double(2, 4.5, 20, 15, 3, 3));
        Shape flap = stroke(1.8f).createStrokedShape(polyline(3.5, 7.5, 12, 13.2, 20.5, 7.5));
        env.subtract(new Area(flap));
        g.fill(env);
    }

    private static void phone(Graphics2D g) {
        g.setStroke(new BasicStroke(3.4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        Path2D handset = new Path2D.Double();
        handset.moveTo(6.2, 3.8);
        handset.curveTo(2.8, 6.2, 4.2, 12.0, 8.6, 15.8);
        handset.curveTo(12.6, 19.6, 17.6, 21.2, 20.2, 17.8);
        g.draw(handset);
        g.fill(new RoundRectangle2D.Double(4.0, 2.2, 5.2, 5.4, 2.2, 2.2));
        g.fill(new RoundRectangle2D.Double(16.4, 14.8, 5.4, 5.2, 2.2, 2.2));
    }

    private static void login(Graphics2D g) {
        g.draw(polyline(13, 3.5, 19.5, 3.5, 19.5, 20.5, 13, 20.5));
        g.draw(polyline(3.5, 12, 14.5, 12));
        g.draw(polyline(10.5, 8, 14.5, 12, 10.5, 16));
    }

    private static void logout(Graphics2D g) {
        g.draw(polyline(10.5, 3.5, 4.5, 3.5, 4.5, 20.5, 10.5, 20.5));
        g.draw(polyline(9.5, 12, 20.5, 12));
        g.draw(polyline(16.5, 8, 20.5, 12, 16.5, 16));
    }

    private static void personAdd(Graphics2D g) {
        g.fill(circle(15, 7.8, 3.9));
        g.fill(new RoundRectangle2D.Double(7.5, 13.6, 15, 7.4, 7.4, 7.4));
        g.setStroke(stroke(2.1f));
        g.draw(new Line2D.Double(1.8, 10.5, 8.2, 10.5));
        g.draw(new Line2D.Double(5, 7.3, 5, 13.7));
    }

    private static void arrowBack(Graphics2D g) {
        g.setStroke(stroke(2.1f));
        g.draw(polyline(20, 12, 4.5, 12));
        g.draw(polyline(10.5, 6, 4.5, 12, 10.5, 18));
    }

    private static void home(Graphics2D g) {
        g.draw(polyline(2.5, 11.5, 12, 3.5, 21.5, 11.5));
        g.draw(polyline(5, 9.5, 5, 20.5, 19, 20.5, 19, 9.5));
        g.draw(polyline(10, 20.5, 10, 14.5, 14, 14.5, 14, 20.5));
    }

    private static void building(Graphics2D g) {
        g.draw(new Rectangle2D.Double(12.5, 3.5, 8.5, 17));
        g.draw(polyline(2.5, 20.5, 2.5, 12.5, 7.5, 8.5, 12.5, 12.5));
        g.draw(polyline(1.5, 20.5, 22.5, 20.5));
        g.draw(polyline(6, 20.5, 6, 15.5, 9, 15.5, 9, 20.5));
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 2; col++) {
                g.fill(new Rectangle2D.Double(14.6 + col * 2.9, 6.2 + row * 4, 1.6, 1.8));
            }
        }
    }

    private static void housePlus(Graphics2D g) {
        g.draw(polyline(2.5, 11.5, 12, 3.5, 19.5, 9.8));
        g.draw(polyline(5, 9.5, 5, 20.5, 11.5, 20.5));
        g.draw(polyline(19, 9.5, 19, 11));
        g.draw(polyline(17.5, 4, 17.5, 7.5));
        g.draw(circle(17.5, 17, 4.7));
        g.setStroke(stroke(1.6f));
        g.draw(polyline(17.5, 14.6, 17.5, 19.4));
        g.draw(polyline(15.1, 17, 19.9, 17));
    }

    private static void calendarFrame(Graphics2D g) {
        g.draw(new RoundRectangle2D.Double(3, 5, 18, 16, 3, 3));
        g.draw(polyline(3, 9.8, 21, 9.8));
        g.draw(polyline(8, 2.8, 8, 6.5));
        g.draw(polyline(16, 2.8, 16, 6.5));
    }

    private static void calendar(Graphics2D g, boolean dots) {
        calendarFrame(g);
        if (dots) {
            g.fill(new Rectangle2D.Double(6.2, 15.2, 2.2, 2.0));
            g.fill(new Rectangle2D.Double(10.2, 15.2, 2.2, 2.0));
            g.fill(new Rectangle2D.Double(13.0, 12.6, 2.2, 2.0));
            g.fill(new Rectangle2D.Double(16.2, 12.6, 2.2, 2.0));
        }
    }

    private static void calendarCheck(Graphics2D g) {
        calendarFrame(g);
        g.draw(polyline(8.5, 15.2, 11, 17.6, 15.8, 12.8));
    }

    private static void door(Graphics2D g) {
        g.draw(polyline(6, 20.5, 6, 3, 17.5, 3, 17.5, 20.5));
        g.draw(new Path2D.Double(polyline(6, 3, 13.5, 5, 13.5, 21.5, 6, 20.5)));
        g.draw(polyline(3, 20.5, 20.5, 20.5));
        g.fill(circle(11.6, 13, 0.9));
    }

    private static void list(Graphics2D g) {
        Area box = new Area(new RoundRectangle2D.Double(3, 3, 18, 18, 2.5, 2.5));
        for (int row = 0; row < 4; row++) {
            double y = 6.2 + row * 3.4;
            box.subtract(new Area(new Rectangle2D.Double(6, y, 2.2, 1.9)));
            box.subtract(new Area(new Rectangle2D.Double(9.8, y, 8.2, 1.9)));
        }
        g.fill(box);
    }

    private static void listAdd(Graphics2D g) {
        g.setStroke(stroke(1.4f));
        g.draw(polyline(3, 6, 17, 6));
        g.draw(polyline(3, 10.5, 17, 10.5));
        g.draw(polyline(3, 15, 11.5, 15));
        g.draw(polyline(17.5, 13, 17.5, 21));
        g.draw(polyline(13.5, 17, 21.5, 17));
    }

    /** วงกลมโปรไฟล์ (ขอบวง + คนอยู่ข้างใน) */
    private static void account(Graphics2D g) {
        g.setStroke(stroke(1.9f));
        g.draw(circle(12, 12, 10.2));
        g.fill(circle(12, 9.3, 3.9));
        Area body = new Area(new Ellipse2D.Double(5.2, 14.6, 13.6, 12));
        body.intersect(new Area(circle(12, 12, 9.0)));
        g.fill(body);
    }

    private static void bell(Graphics2D g) {
        Path2D body = new Path2D.Double();
        body.moveTo(4.5, 17.5);
        body.lineTo(19.5, 17.5);
        body.lineTo(17.6, 15.2);
        body.lineTo(17.6, 10.8);
        body.curveTo(17.6, 7.4, 15.2, 5.0, 12, 5.0);
        body.curveTo(8.8, 5.0, 6.4, 7.4, 6.4, 10.8);
        body.lineTo(6.4, 15.2);
        body.closePath();
        g.fill(body);
        g.fill(circle(12, 3.9, 1.4));
        g.fill(circle(12, 19.6, 2.0));
    }

    /** สองคน + เครื่องหมายบวก (ผู้ใช้ทั้งหมด) */
    private static void groupAdd(Graphics2D g) {
        g.fill(circle(19.2, 7.6, 2.7));
        g.fill(new RoundRectangle2D.Double(15.6, 12.8, 8.4, 7.2, 6, 6));
        g.fill(circle(13.2, 8.0, 3.4));
        g.fill(new RoundRectangle2D.Double(6.8, 13.4, 12.8, 7.6, 7.6, 7.6));
        g.setStroke(stroke(2.1f));
        g.draw(new Line2D.Double(1.6, 10.6, 7.4, 10.6));
        g.draw(new Line2D.Double(4.5, 7.7, 4.5, 13.5));
    }

    /** ประตูทึบ */
    private static void doorFilled(Graphics2D g) {
        Area door = new Area(new RoundRectangle2D.Double(6, 2.5, 12, 19, 1.5, 1.5));
        door.subtract(new Area(circle(15, 12.5, 1.2)));
        g.fill(door);
        g.draw(polyline(3.5, 21.5, 20.5, 21.5));
    }

    /** ประตูทึบ + แม่กุญแจ (ห้องไม่ว่าง) */
    private static void doorLock(Graphics2D g) {
        Area door = new Area(new RoundRectangle2D.Double(4, 2.5, 12, 19, 1.5, 1.5));
        door.subtract(new Area(circle(7.5, 12.5, 1.2)));
        door.subtract(new Area(new RoundRectangle2D.Double(10.5, 10.2, 13, 13, 3, 3)));
        g.fill(door);
        Area lock = new Area(new RoundRectangle2D.Double(12.2, 15.2, 9.6, 7, 1.8, 1.8));
        lock.subtract(new Area(circle(17, 18.7, 1.1)));
        g.fill(lock);
        g.setStroke(stroke(1.6f));
        Path2D shackle = new Path2D.Double();
        shackle.moveTo(14.4, 15.4);
        shackle.lineTo(14.4, 14.0);
        shackle.curveTo(14.4, 10.6, 19.6, 10.6, 19.6, 14.0);
        shackle.lineTo(19.6, 15.4);
        g.draw(shackle);
    }

    /** กรอบประตู + บานประตูเปิด (รายการห้อง) */
    private static void doorOpen(Graphics2D g) {
        g.setStroke(stroke(2.2f));
        g.draw(polyline(7.5, 3, 4, 3, 4, 21, 7.5, 21));
        Path2D leaf = new Path2D.Double();
        leaf.moveTo(9, 2.2);
        leaf.lineTo(19.5, 4.2);
        leaf.lineTo(19.5, 19.8);
        leaf.lineTo(9, 21.8);
        leaf.closePath();
        Area a = new Area(leaf);
        a.subtract(new Area(circle(16.4, 12, 1.2)));
        g.fill(a);
    }
}
