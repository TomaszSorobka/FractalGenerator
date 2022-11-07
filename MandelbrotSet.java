import java.awt.*;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


/*
 * MandelbrotSet
 * 
 * @author Tomasz Soróbka
 * @id 1808982
 */

public class MandelbrotSet extends JPanel {

    // Final variables declaration
    private final int MAXITERATIONS = 100;
    private final int WIDTH = 1000;
    private final int HEIGHT = 1000;
    private final int X_SHIFT = 550;
    private final int Y_SHIFT = 350;
    private final int BOUND = 500;

    // Color variables declaration
    Color colorBackground;
    Color colorLine;
    Color colorFill;

    // Public variables declaration 
    public int iterationsCount;
    public int iterationTemp;
    public static double ZOOM = 250;

    // Image and calculation needed variables
    private BufferedImage fractalImage;
    private double zReal;
    private double zImag;
    private double adjustedX;
    private double adjustedY;
    private double tmpValue;
    
    // the constructor of this class
    public MandelbrotSet(Color colorBackground, Color colorLine, Color colorFill,
        int iterations) {

        // Initialize basic instance variables
        this.colorBackground = colorBackground;
        this.colorLine = colorLine;
        this.colorFill = colorFill;
        if (iterations == 0) {
            this.iterationsCount = MAXITERATIONS;
        } else {
            this.iterationsCount = iterations;
        }

        // Set dimensions, opaque and background color
        setBounds(BOUND, BOUND, WIDTH + BOUND, HEIGHT);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(this.colorBackground);

        // Image initialization
        fractalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        // A loop that goes through every value of the height and width of the object
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                // Initialize the values
                zReal = 0;
                zImag = 0;
                adjustedX = (x - X_SHIFT) / ZOOM;
                adjustedY = (y - Y_SHIFT) / ZOOM;
                iterationTemp = iterationsCount;

                // declare the colorInt variable that will store the colors integer value
                int colorInt;

                // Iterate while the conditions are met
                while (zReal * zReal + zImag * zImag < 4.0 && iterationTemp > 0) {
                    // calculate according to the Mandelbrot Set formula
                    tmpValue = zReal * zReal - zImag * zImag + adjustedX;
                    zImag = 2.0 * zReal * zImag + adjustedY;
                    zReal = tmpValue;
                    iterationTemp--;    
                }

                // Assign an appropriate color
                if (iterationTemp == iterationsCount) {
                    colorInt = this.colorBackground.getRGB();
                } else {
                    colorInt = Color.HSBtoRGB((float) this.colorFill.getRGB() / iterationsCount
                        + (float) iterationTemp / iterationsCount, 0.5f, 1);
                }
    
                // set the color of the Image
                fractalImage.setRGB(x, y, colorInt);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(fractalImage, 0, 0, this);
    }
}