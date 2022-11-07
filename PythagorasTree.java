import java.awt.*;
import java.awt.geom.Path2D;
import javax.swing.*;

/*
 * PythagorasTree
 * 
 * @author Tomasz Soróbka
 * @id 1808982
 */

public class PythagorasTree extends JPanel {

    // Final variables declaration
    private final int MAXITERATIONS = 15;
    private final int WIDTH = 1200;
    private final int HEIGHT = 700;
    private final int X1_INIT = 450;
    private final int Y1_INIT = 625;
    private final int X2_INIT = 600;
    private final int Y2_INIT = 625;

    // Color variables declaration
    Color colorBackground;
    Color colorLine;
    Color colorFill;
  
    // Basic variables declaration
    public int iterationCount;
    public int angleLeft;
    public int angleRight;
    public int angleTop;
    public static double zoom = 0.0;

    // the costructor of this class
    public PythagorasTree(Color colorBackground, Color colorLine, Color colorFill,
        int iterations, int angleLeft, int angleRight) {
        
        // Initialize basic instance variables
        this.colorBackground = colorBackground;
        this.colorLine = colorLine;
        this.colorFill = colorFill;
        if (iterations == 0) {
            iterationCount = MAXITERATIONS;
        } else {
            iterationCount = iterations;
        }
        this.angleLeft = angleLeft;
        this.angleRight = angleRight;
        this.angleTop = 180 - angleLeft - angleRight;

        // Set dimensions, opaque and background color
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setOpaque(true);
        setBackground(colorBackground);
        
    }

    /* A function used to calculate the distance between two points.
    *  @param x1 - x coordinate of the first point
    *  @param y1 - y coordinate of the first point
    *  @param x2 - x coordinate of the second point
    *  @param y2 - y coordinate of the second point
    */ 
    float calculateDistance(float x1, float y1, float x2, float y2) {
        float distance = (float) Math.sqrt(Math.pow((double) (x2 - x1), 2.0)
            + Math.pow((double) (y2 - y1), 2.0));
        return distance;
    }

    /* A method that draws the subsequent squares of the Pythagoras Fractal.

    *  @param x1 - x coordinate of the first point
    *  @param y1 - y coordinate of the first point
    *  @param x2 - x coordinate of the second point
    *  @param y2 - y coordinate of the second point
    */ 
    public void drawPythagorasTree(float x1, float y1, float x2, float y2, int iterations, Graphics2D g) {

        if (iterations == 0) {
            return;
        }

        // calculate the differences between the x and y coordinates and based on that calculate 
        // the coordinates of x3, y3, x4, y4, which are the 2 remaining vertices of the square
        float differenceX = x2 - x1;
        float differenceY = y1 - y2;
        float x3 = x2 - differenceY;
        float y3 = y2 - differenceX;
        float x4 = x1 - differenceY;
        float y4 = y1 - differenceX;

        // calculate triangle sides length
        float distanceC = calculateDistance(x3, y3, x4, y4);
        float distanceB = distanceC * (float) Math.sin(Math.toRadians(angleRight))
            / (float) Math.sin(Math.toRadians(angleTop));
        float distanceA = distanceC * (float) Math.sin(Math.toRadians(angleLeft))
            / (float) Math.sin(Math.toRadians(angleTop));
        float a = (float) ((Math.pow(distanceB, 2)
            - Math.pow(distanceA, 2) + Math.pow(distanceC, 2)) / (2 * distanceC));
        
        // calculate height, the scale factors (rateX and rateY)
        float height = (float) Math.sqrt((Math.pow(distanceB, 2) - Math.pow(a, 2)));
        float xDistance = (float) Math.sqrt((Math.pow(distanceB, 2) - Math.pow(height, 2)));
        float rateX = xDistance / (distanceC / 2);
        float rateY = height / (0.5F * distanceC);

        // create the shape of the square by connecting the given and calculated points
        Path2D squareShape = new Path2D.Float();
        squareShape.moveTo(x1, y1);
        squareShape.lineTo(x2, y2);
        squareShape.lineTo(x3, y3);
        squareShape.lineTo(x4, y4);
        squareShape.closePath();

        // set the color of the square to be the colorFill
        g.setColor(colorFill);
        g.fill(squareShape);
        // set the color of the sides of the square to be the colorLine
        g.setColor(colorLine);
        g.draw(squareShape);

        // calculate the coordinates of the x5 and y5 which are the coordinates of
        // the third vertex of the triangle
        float x5 = x4 + 0.5F * rateX * (differenceX - differenceY);
        float y5 = y4 - 0.5F * rateY * (differenceX + differenceY);
        Path2D triangle = new Path2D.Float();

        // create the shape of the triangle by connecting the calculated points
        triangle.moveTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.lineTo(x5, y5);
        triangle.closePath();

        // set the color of the square to be the colorFill
        g.setColor(Color.WHITE);
        g.fill(triangle);
        // set the color of the sides of the square to be the colorLine
        g.setColor(colorLine);
        g.draw(triangle);

        // Recursively call this function for the creating the fractal on the
        // left side of the triangle and on the right sight of the triangle
        drawPythagorasTree(x4, y4, x5, y5, iterations - 1, g);
        drawPythagorasTree(x5, y5, x3, y3, iterations - 1, g);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // If the iterationCount exceeds the MAXITERATIONS value, set it to the max value
        if (iterationCount > MAXITERATIONS) {
            iterationCount = MAXITERATIONS;
        }
        drawPythagorasTree(X1_INIT, Y1_INIT, X2_INIT, Y2_INIT, iterationCount, (Graphics2D) g);
    }
}