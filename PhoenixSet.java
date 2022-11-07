import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/*
 * Phoenix set
 * 
 * @author Tomasz Soróbka
 * @id 1808982
 */

/*
 * Phoenix fractal class 
 * fractal formula z(n+1) = z(n)^2 + c * z(n) + p*z(n-1) 
 */
public class PhoenixSet  extends JPanel {
    
    // Final variables declaration
    private final int MAXITERATIONS = 100;
    private final int WIDTH = 1000;
    private final int HEIGHT = 640;
    private final int SCREEN_SIZE = 1500;
    private final int BOUND = 500;
    private final int X_INIT = 0;
    private final int Y_INIT = 0;

    // Color variables declaration
    Color colorBackground;
    Color colorLine;
    Color colorFill;

    // Basic variable declaration
    int iterationCount;

    // Image declaration
    private BufferedImage fractalImage;

    // initial fractal constants
    private double C = 0.56666;
    private double P = -0.5;
    public static double ZOOM = 350.0;
    
    // the constructor of this class
    public PhoenixSet(Color colorBackground, Color colorLine, Color colorFill, int iterations) {
        // Initialize basic instance variables
        this.colorBackground = colorBackground;
        this.colorLine = colorLine;
        this.colorFill = colorFill;
        if (iterations == 0) {
            this.iterationCount = MAXITERATIONS;
        } else {
            this.iterationCount = iterations;
        }

        // Set dimensions, opaque and background color
        setBounds(BOUND, BOUND, SCREEN_SIZE, SCREEN_SIZE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(colorBackground);

        // Image initialization
        fractalImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        // A loop that goes through every value of the height and width of the object
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                // Get the color for every coordinate
                int colorInt = iterateColor((x - WIDTH / 2f) / ZOOM,
                    (y - HEIGHT / 2f) / ZOOM, iterationCount);
                // set the color of the image
                fractalImage.setRGB(x, y, colorInt);
            }
        }       
        
    }

     /* A method that returns a color to be assigned to a coordinate
    *   based on the definition of Phoenix set
    *  @param x - x coordinate of the point
    *  @param y - y coordinate of the point
    *  @param iterations - a number of iterations to be performed
    */ 
    int iterateColor(double x, double y, int iterations) {
        /*
         * Complex parts of z point gets the pixel (x,y)
         * Stimulate current z point: z(n)
         */
        double zReal = y;
        double zImag = x;

        /*
         * To save the previous z point: z(n-1)
         */
        double zPrevRe = 0;
        double zPrevIm = 0;

        /* 
         * Keep iterating till the boundary condition is reached
         */
        int n = 0;
        while (n < iterations && zReal * zReal + zImag * zImag < 4.0) {
            double tmpReal = zReal;
            double tmpImag = zImag;

            zReal = zReal * zReal - zImag * zImag + C + P * zPrevRe;
            zImag = 2 * tmpReal * tmpImag + P * zPrevIm;

            zPrevRe = tmpReal;
            zPrevIm = tmpImag;
            n++;
        }

        // Return either a basic fill color
        if (n == iterations) {
            return colorFill.getRGB();
        }

        // Or return a color based on the iterations number and chosen background color
        return Color.HSBtoRGB((float) colorBackground.getRGB() / iterations
            + (float) n / iterations, 0.5f, 1);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(fractalImage, X_INIT, Y_INIT, this);
    }

}
