
import com.teamten.image.ImageUtils;
import com.teamten.image.Typeface;
import com.teamten.util.Files;
import java.awt.Color;
import java.awt.font.TextLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Make a poster for Hack Night like the Graphics Engine cover.
 */
public class HackNightGears {
    private static final int DPI = 72;          // Screen
    //private static final int DPI = 300;         // Print
    private static final double WIDTH_IN = 11;
    private static final double HEIGHT_IN = 14;
    private static final Color BACKGROUND_COLOR = new Color(204, 108, 5);
    private static final double MARGIN_IN = 0.5;
    private static final double RIBBON_TOP_IN = 3;
    private static final double RIBBON_BOTTOM_IN = 6;
    private static final double RIBBON_SHADOW_IN = 0.25;
    private static final Color RIBBON_COLOR = new Color(3, 79, 128);
    private static final Color RIBBON_SHADOW_COLOR =
        ImageUtils.interpolateColor(RIBBON_COLOR, Color.BLACK, 0.4);
    private static final double DIAGRAM_X_IN = 3.8;
    private static final double DIAGRAM_Y_IN = -2.6;
    private static final Color DIAGRAM_COLOR = new Color(169, 58, 0);
    private static final double DIAGRAM_SCALE = 1.3;
    private static final double TITLE_PT = 240/300.;
    private static final double TITLE_POS_IN = 4;
    private static final Color TITLE_TEXT_COLOR = Color.WHITE; // new Color(253, 187, 36);
    private static final double AUTHOR_PT = 120/300.;
    private static final double AUTHOR_LEADING = 0.5;
    private static final double AUTHOR_POS_IN = 5.05;
    private static final Color AUTHOR_TEXT_COLOR = TITLE_TEXT_COLOR;
    private static final double CODE_PT = 100/300.;
    private static final double CODE_X_IN = MARGIN_IN*2;
    private static final double CODE_Y_IN = -0.20;
    private static final double CODE_LEADING_IN = 0.35;
    /// private static final Color CODE_TEXT_COLOR = new Color(255, 255, 0, 20);
//    private static final Color CODE_TEXT_COLOR = new Color(0, 0, 0, 20);
    private static final Color CODE_TEXT_COLOR = new Color(169, 58, 0, 100);
    /// private static final Color CODE_TEXT_COLOR = new Color(214, 130, 20);
    private static final double GEARS_SCALE = 0.03;
    private static final double GEARS_OFFSET_X = 16;
    private static final double GEARS_OFFSET_Y = 4;
    private static final double GEARS_ROTATE_DEG = 30;
    private static final Color[] GEAR_COLORS = new Color[] {
        new Color(173, 59, 0),
        new Color(183, 69, 2),
        new Color(170, 65, 0),
        new Color(181, 74, 1),
        new Color(200, 96, 6),
        new Color(194, 91, 4),
        new Color(222, 152, 12),
        new Color(214, 130, 20),
        new Color(201, 105, 5)
    };
    private static final int TEXTURE_STRENGTH = 16;

    private static final int WIDTH = toPixels(WIDTH_IN);
    private static final int HEIGHT = toPixels(HEIGHT_IN);
    private static final int MARGIN = toPixels(MARGIN_IN);

    public static void main(String[] args) throws Exception {
        BufferedImage image = ImageUtils.make(WIDTH, HEIGHT, BACKGROUND_COLOR);

        // Front.
        Graphics2D g = ImageUtils.createGraphics(image);

        // Gears.
        drawGears(g, "data/gears.raw");

        // Assembly language.
        drawAssembly(g);

        // Diagram.
        drawDragram(g, "data/picboard.png");

        // Texture.
        drawTexture(g);

        // Ribbon.
        g.setColor(RIBBON_COLOR);
        int ribbonTop = toPixels(RIBBON_TOP_IN); 
        int ribbonBottom = toPixels(RIBBON_BOTTOM_IN);
        g.fillRect(0, ribbonTop, WIDTH, ribbonBottom - ribbonTop);
        int shadowSize = toPixels(RIBBON_SHADOW_IN);
        for (int i = 0; i < shadowSize; i++) {
            double shadow = (double) i / shadowSize;
            shadow = Math.pow(shadow, 0.5);
            g.setColor(ImageUtils.interpolateColor(RIBBON_SHADOW_COLOR, RIBBON_COLOR, shadow));
            g.drawLine(0, ribbonTop + i, WIDTH, ribbonTop + i);
            g.drawLine(0, ribbonBottom - i, WIDTH, ribbonBottom - i);
        }

        // Title.
        Font font = ImageUtils.getFont(Typeface.HELVETICA, true, false, false, TITLE_PT*DPI);
        g.setColor(TITLE_TEXT_COLOR);
        g.setFont(font);
        TextLayout textLayout = new TextLayout("Hack Night", font, g.getFontRenderContext());
        int width = (int) textLayout.getBounds().getWidth();
        textLayout.draw(g, WIDTH - MARGIN - width, toPixels(TITLE_POS_IN));

        // Sub-titles.
        font = ImageUtils.getFont(Typeface.HELVETICA, false, false, false, AUTHOR_PT*DPI);
        g.setColor(AUTHOR_TEXT_COLOR);
        g.setFont(font);
        textLayout = new TextLayout("1452 Hartford Avenue", font, g.getFontRenderContext());
        width = (int) textLayout.getBounds().getWidth();
        textLayout.draw(g, WIDTH - MARGIN - width, toPixels(AUTHOR_POS_IN));
        textLayout = new TextLayout("June 5, 2019", font, g.getFontRenderContext());
        width = (int) textLayout.getBounds().getWidth();
        textLayout.draw(g, WIDTH - MARGIN - width, toPixels(AUTHOR_POS_IN + AUTHOR_LEADING));

        g.dispose();

        ImageUtils.save(image, "poster.png");

        if (DPI > 72) {
            image = ImageUtils.scale(image, 72.0 / DPI);
            ImageUtils.save(image, "poster_small.png");
        }
    }

    private static void drawAssembly(Graphics2D g) throws Exception {
        String[] code = new String[] {
                "PUSH IX",
                "PUSH HL",
                "PUSH BC",
                "",
                "LD IX, OUTPUT_BUF",
                "CALL PHEXBYTE",
                "",
                "LD HL, KEYBUF",
                "LD A, (NUMKEY)",
                "LD C, A",
                "LD B, 0",
                "ADD HL, BC",
                "INC A",
                "LD (NUMKEY), A",
                "",
                "LD A, (OUTPUT_BUF)",
                "LD (HL), A",
                "",
                "LD HL, KEYBUF",
                "LD A, (NUMKEY)",
                "LD C, A",
                "LD B, 0",
                "ADD HL, BC",
                "INC A",
                "LD (NUMKEY), A",
                "",
                "LD A, (OUTPUT_BUF+1)",
                "LD (HL), A",
                "",
                "LD A, (PIC_COMMAND)",
                "CP PIC_NON_CMD",
                "JP Z, ISR0_IS_COMMAND",
                "",
                "CP PIC_SER_CMD",
                "JP Z, ISR0_IS_SERIAL",
                "",
                "CP PIC_KBD_CMD",
                "JP Z, ISR0_IS_KEYBOARD",
                "",
                "POP BC",
                "POP HL",
                "POP IX",
                "",
                "JP ISR0_RESET",
        };

        Font font = ImageUtils.getFont(Typeface.COURIER, true, false, false, CODE_PT*DPI);
        g.setColor(CODE_TEXT_COLOR);
        g.setFont(font);
        for (int i = 0; i < code.length; i++) {
            if (!code[i].isEmpty()) {
                TextLayout textLayout = new TextLayout(code[i], font, g.getFontRenderContext());
                textLayout.draw(g, toPixels(CODE_X_IN), toPixels(CODE_Y_IN + i*CODE_LEADING_IN));
            }
        }
    }

    private static void drawGears(Graphics2D g, String filename) throws IOException {
        int colorIndex = 0;

        double sin = Math.sin(GEARS_ROTATE_DEG*Math.PI/180);
        double cos = Math.cos(GEARS_ROTATE_DEG*Math.PI/180);

        for (String line : Files.readLines(new File(filename))) {
            String[] fields = line.split(" ");

            if (fields[0].equals("circle")) {
                double ox = Double.parseDouble(fields[1])*GEARS_SCALE;
                double oy = Double.parseDouble(fields[2])*GEARS_SCALE;
                double r = Double.parseDouble(fields[3])*GEARS_SCALE;
                double x = ox*cos + oy*sin + GEARS_OFFSET_X;
                double y = ox*sin - oy*cos + GEARS_OFFSET_Y;
                g.setColor(BACKGROUND_COLOR);
                g.fillArc(toPixels(x - r), toPixels(y - r), toPixels(r*2), toPixels(r*2), 0, 360);
            } else if (fields[0].equals("polyline")) {
                int numPoints = (fields.length - 1)/2;
                int[] x = new int[numPoints];
                int[] y = new int[numPoints];

                for (int i = 0; i < numPoints; i++) {
                    double ox = Double.parseDouble(fields[i*2 + 1])*GEARS_SCALE;
                    double oy = Double.parseDouble(fields[i*2 + 2])*GEARS_SCALE;
                    x[i] = toPixels(ox*cos + oy*sin + GEARS_OFFSET_X);
                    y[i] = toPixels(ox*sin - oy*cos + GEARS_OFFSET_Y);
                }

                g.setColor(GEAR_COLORS[colorIndex++ % GEAR_COLORS.length]);
                g.fillPolygon(x, y, numPoints);
            } else {
                System.err.printf("Unknown command \"%s\" in raw file %s%n", fields[0], filename);
            }
        }
    }

    private static void drawDragram(Graphics2D g, String filename) throws IOException {
        BufferedImage diagram = ImageUtils.load(filename);
        diagram = ImageUtils.grayToTransparent(diagram, 255, -500);
        diagram = ImageUtils.scale(diagram, DPI/300.0*DIAGRAM_SCALE);
        BufferedImage solid = ImageUtils.make(diagram.getWidth(), diagram.getHeight(),
                DIAGRAM_COLOR);
        diagram = ImageUtils.clipToMask(solid, diagram);
        g.drawImage(diagram, toPixels(DIAGRAM_X_IN), toPixels(DIAGRAM_Y_IN), null);
    }

    /**
     * Draw a texture patten on top of the whole image.
     */
    private static void drawTexture(Graphics2D g) {
        Random random = new Random();

        for (int x = 0; x < WIDTH; x++) {
            g.setColor(new Color(0, 0, 0, random.nextInt(TEXTURE_STRENGTH)));
            g.drawLine(x, 0, x, HEIGHT);
        }
        for (int y = 0; y < HEIGHT; y++) {
            g.setColor(new Color(0, 0, 0, random.nextInt(TEXTURE_STRENGTH)));
            g.drawLine(0, y, WIDTH, y);
        }
    }

    private static int toPixels(double inches) {
        return (int) (inches*DPI + 0.5);
    }
}
