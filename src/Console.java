import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Console {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 25;

    public static final char LEFT = KeyEvent.VK_LEFT;
    public static final char RIGHT = KeyEvent.VK_RIGHT;
    public static final char UP = KeyEvent.VK_UP;
    public static final char DOWN = KeyEvent.VK_DOWN;

    public static final byte BLACK = 0;
    public static final byte BLUE = 1;
    public static final byte RED = 2;
    public static final byte MAGENTA = 3;
    public static final byte GREEN = 4;
    public static final byte CYAN = 5;
    public static final byte YELLOW = 6;
    public static final byte WHITE = 7;
      
    private static volatile Screen screen = null;

    public static void locate(int x, int y) {
        init();

        screen.setX(x);
        screen.setY(y);
    }

    public static void color(byte color) {
        init();

        screen.setColor(color);
    }

    public static void print(char c) {
        init();

        screen.print(c);
    }

    public static void print(String str) {
        init();

        for (int index = 0; index < str.length(); index++) {
        	screen.print(str.charAt(index));
        }
    }

    public static void print(int i) {
        init();

        Console.print(String.valueOf(i));
    }

    public static void clear() {
        init();

        screen.clear();
    }

    public static char scan(int x, int y) {
        init();

        return screen.scan(x, y);
    }

    public static boolean pressed(char keyCode) {
        init();

        return screen.pressed(keyCode);
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch (InterruptedException e) {
        }
    }

    private static void init() {
        if (screen == null) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                    	screen = new Screen();
                    }
                });
            }
            catch (InterruptedException e) {
            }
            catch (InvocationTargetException e) {
            }
        }
    }

    private static class Screen {

        private static final int FONT_SIZE = 12;
        private static final int KEY_BUFFER_SIZE = 256;

        private char[][] screenBuffer;
        private byte[][] colorBuffer;
        private int x;
        private int y;
        private byte color;
        private boolean[] keyBuffer;
        private JFrame frame;
        private Content content;

        public class Content extends JComponent {

        	private final Color[] colorMap = {Color.BLACK, Color.BLUE, Color.RED, Color.MAGENTA, Color.GREEN, Color.CYAN, Color.YELLOW, Color.WHITE};

        	private Font font;
        	private int width;
        	private int height;
        	private int ascent;

        	public Content() {
        		super();

        		this.font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
        		BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        		Graphics graphics = image.getGraphics();
        		graphics.setFont(font);
        		FontMetrics metrics = graphics.getFontMetrics();

                this.width = metrics.charWidth('W');
                this.height = metrics.getHeight();
                this.ascent = metrics.getAscent();
                this.setPreferredSize(new Dimension(Console.WIDTH * this.width, Console.HEIGHT * this.height));
        	}

        	@Override
        	  public void paintComponent(Graphics g) {
                ((Graphics2D)g).setBackground(Color.black);
                g.clearRect(0, 0, getWidth(), getHeight());

                g.setFont(this.font);

                for (int y = 0; y < Console.HEIGHT; y++) {
                    for (int x = 0; x < Console.WIDTH; x++) {
                    	g.setColor(this.colorMap[Screen.this.colorBuffer[y][x]]);
                        g.drawChars(Screen.this.screenBuffer[y], x, 1, x * this.width, y * this.height + this.ascent);
                    }
                }
        	}

        }

        public Screen() {
            this.screenBuffer = new char[Console.HEIGHT][Console.WIDTH];
            for (int y = 0; y < Console.HEIGHT; y++) {
                for (int x = 0; x < Console.WIDTH; x++) {
                    this.screenBuffer[y][x] = ' ';
                }
            }
            this.colorBuffer = new byte[Console.HEIGHT][Console.WIDTH];
            this.color = Console.WHITE;
            this.keyBuffer = new boolean[KEY_BUFFER_SIZE];

        	this.frame = new JFrame("Screen");
        	this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	this.content = new Content();
        	this.frame.add(this.content);
        	this.frame.pack();
        	this.frame.setVisible(true);

        	this.frame.addKeyListener(new KeyAdapter() {

                @Override
                public void keyPressed(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    if (0 <= keyCode && keyCode < KEY_BUFFER_SIZE) {
                        Screen.this.keyBuffer[keyCode] = true;
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    int keyCode = e.getKeyCode();
                    if (0 <= keyCode && keyCode < KEY_BUFFER_SIZE) {
                    	Screen.this.keyBuffer[keyCode] = false;
                    }
                }
            });
        }

        public void setX(int x) {
            if (x < 0 || Console.WIDTH <= x) {
                throw new IndexOutOfBoundsException();
            }
            this.x = x;
        }

        public void setY(int y) {
            if (y < 0 || Console.HEIGHT <= y) {
                throw new IndexOutOfBoundsException();
            }
            this.y = y;
        }

        public void setColor(byte color) {
            if (color <= Console.BLACK || Console.WHITE <= color) {
                throw new IndexOutOfBoundsException();
            }
            this.color = color;
        }

        public void print(char c) {
            this.screenBuffer[this.y][this.x] = c;
            this.colorBuffer[this.y][this.x] = this.color;

            this.x++;
            if (this.x == Console.WIDTH) {
                this.x = 0;
                this.y++;
                if (this.y == Console.HEIGHT) {
                    this.y = 0;
                }
            }

            this.content.repaint();
        }

        public void clear() {
            for (int y = 0; y < Console.HEIGHT; y++) {
                for (int x = 0; x < Console.WIDTH; x++) {
                    this.screenBuffer[y][x] = ' ';
                }
            }

            this.content.repaint();
        }

        public char scan(int x, int y) {
            if (x < 0 || Console.WIDTH <= x || y < 0 || Console.HEIGHT <= y) {
                throw new IndexOutOfBoundsException();
            }
            return this.screenBuffer[y][x];
        }

        public boolean pressed(int keyCode) {
            if (0 <= keyCode && keyCode < KEY_BUFFER_SIZE) {
                return this.keyBuffer[keyCode];
            }
            else {
                return false;
            }
        }

    }

}
