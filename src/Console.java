import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

public class Console {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final char LEFT = KeyEvent.VK_LEFT;
    public static final char RIGHT = KeyEvent.VK_RIGHT;
    public static final char UP = KeyEvent.VK_UP;
    public static final char DOWN = KeyEvent.VK_DOWN;

    private static volatile ConsoleFrame frame = null;

    public static void locate(int x, int y) {
        init();

        frame.setX(x);
        frame.setY(y);
    }

    public static void print(char c) {
        init();

        frame.print(c);
    }

    public static void print(String str) {
        init();

        for (int index = 0; index < str.length(); index++) {
            frame.print(str.charAt(index));
        }
    }

    public static void clear() {
        init();

        frame.clear();
    }

    public static boolean pressed(char keyCode) {
        init();

        return frame.pressed(keyCode);
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
        }
    }

    private static void init() {
        if (frame == null) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        frame = new ConsoleFrame();
                    }
                });
            }
            catch (InterruptedException e) {
            }
            catch (InvocationTargetException e) {
            }
        }
    }

    private static class ConsoleFrame extends JFrame {

        private static final int FONT_SIZE = 12;
        private static final int KEY_BUFFER_SIZE = 256;

        private char[][] screenBuffer;
        private int x;
        private int y;
        private boolean[] keyBuffer;

        public ConsoleFrame() {
            super("Screen");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setVisible(true);

            this.screenBuffer = new char[Console.HEIGHT][Console.WIDTH];
            this.keyBuffer = new boolean[KEY_BUFFER_SIZE];

            Insets insets = getInsets();
            setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));
            FontMetrics metrics = getGraphics().getFontMetrics();
            int width = insets.left + Console.WIDTH * metrics.charWidth('W') + insets.right;
            int height = insets.top + Console.HEIGHT * metrics.getHeight() + insets.bottom;
            setSize(width, height);

            addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    if (0 <= keyCode && keyCode < KEY_BUFFER_SIZE) {
                        ConsoleFrame.this.keyBuffer[keyCode] = true;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    if (0 <= keyCode && keyCode < KEY_BUFFER_SIZE) {
                        ConsoleFrame.this.keyBuffer[keyCode] = false;
                    }
                }
            });
        }

        public void setX(int x) {
            if (x < 0 || Console.WIDTH <= x) {
                throw new IndexOutOfBoundsException("");
            }
            this.x = x;
        }

        public void setY(int y) {
            if (y < 0 || Console.HEIGHT <= y) {
                throw new IndexOutOfBoundsException("");
            }
            this.y = y;
        }

        public void print(char c) {
            this.screenBuffer[this.y][this.x] = c;

            this.x++;
            if (this.x == Console.WIDTH) {
                this.x = 0;
                this.y++;
                if (this.y == Console.HEIGHT) {
                    this.y = 0;
                }
            }

            super.repaint();
        }

        public void clear() {
            for (int y = 0; y < Console.HEIGHT; y++) {
                for (int x = 0; x < Console.WIDTH; x++) {
                    this.screenBuffer[y][x] = 0;
                }
            }

            super.repaint();
        }

        public boolean pressed(int keyCode) {
            if (0 <= keyCode && keyCode < KEY_BUFFER_SIZE) {
                return this.keyBuffer[keyCode];
            }
            else {
                return false;
            }
        }

        @Override
        public void paint(Graphics g) {
            ((Graphics2D)g).setBackground(Color.black);
            g.clearRect(0, 0, getWidth(), getHeight());

            Insets insets = getInsets();
            setFont(new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE));
            FontMetrics metrics = g.getFontMetrics();
            int width = metrics.charWidth('W');
            int height = metrics.getHeight();
            int ascent = metrics.getAscent();
            g.setColor(Color.white);

            for (int y = 0; y < Console.HEIGHT; y++) {
                for (int x = 0; x < Console.WIDTH; x++) {
                    g.drawChars(this.screenBuffer[y], x, 1, insets.left + x * width, insets.top + y * height + ascent);
                }
            }
        }
    }
}
