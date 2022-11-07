import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

/*
 * Fractal Canvas
 * 
 * @author Tomasz Soróbka
 * @id 1808982
 */

public class FractalCanvas extends JPanel implements ActionListener {
    
    private final int WIDTH = 1000;
    private final int HEIGHT = 700;

    // Color variables declaration
    Color colorBackground;
    Color colorLine;
    Color colorFill;

    // Static variables declaration
    public static int fractalOption;
    public static double zoomPythagoras;
    public static double zoomMandel;
    public static double zoomPhoenix;

    // The declaration of Fractal Objects
    PythagorasTree pythTree;
    MandelbrotSet mandelSet;
    PhoenixSet phoeSet;

    // The constructor of this class
    public FractalCanvas() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        zoomPythagoras = 0.0;
        zoomMandel = 0.0;
        zoomPhoenix = 0.0;
        FractalCanvas.fractalOption = 0;
        setOpaque(true);
        getColors();
    }

    // The method that handles the events (as this class is the extension of the
    // Action Listener)
    @Override
    public void actionPerformed(ActionEvent e) {
        // depending on what action was performed, execute apprioprate code and methods
        if ("Apply Changes".equals(e.getActionCommand())) {
            repaint();
        } else if ("Pythagoras Tree".equals(e.getActionCommand())) {
            fractalOption = 1;
            repaint();
        } else if ("Mandelbrot Set".equals(e.getActionCommand())) {
            fractalOption = 2;
            repaint();
        } else if ("Phoenix Set".equals(e.getActionCommand())) {
            fractalOption = 3;
            repaint();
        } else if ("Save the setting's file".equals(e.getActionCommand())){
            try {
                writeOutput(getSaveLocation(1));
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        } else if ("Load a setting's file".equals(e.getActionCommand())){
            try {
                loadFile(getSaveLocation(2));
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
            }
        }
    }

    // a method used to fetch colors to instance variables
    void getColors() {
        colorBackground = new Color(FractalGenerator.redValues[0], 
            FractalGenerator.greenValues[0], FractalGenerator.blueValues[0]);
        colorLine = new Color(FractalGenerator.redValues[1], 
            FractalGenerator.greenValues[1], FractalGenerator.blueValues[1]);
        colorFill = new Color(FractalGenerator.redValues[2], 
            FractalGenerator.greenValues[2], FractalGenerator.blueValues[2]);
    }

    // a method that paints the Fractals
    @Override
    protected void paintComponent(Graphics g) {
        // Clear the panel
        super.paintComponent(g); 
        getColors();

        // Assign the background color
        if (colorBackground.getRGB() == -16777216) {
            colorBackground = Color.WHITE;
        }
        setBackground(colorBackground);
        
        // Depending on which fractal was chosen, paint appropriate fractal
        // Pythagoras Tree
        if (fractalOption == 1) {
            int degreesLeft = FractalGenerator.angleTriangle[0];
            int degreesRight = FractalGenerator.angleTriangle[1];
            PythagorasTree.zoom += zoomPythagoras;
            pythTree = new PythagorasTree(colorBackground, colorLine, colorFill,
                FractalGenerator.iterations, degreesLeft, degreesRight);
            
            pythTree.paintComponent(g);

            // Mandelbrot Set
        } else if (fractalOption == 2) {
            MandelbrotSet.ZOOM += zoomMandel;
            mandelSet = new MandelbrotSet(colorBackground, colorLine, colorFill,
                FractalGenerator.iterations);
            mandelSet.paintComponent(g);

            // Phoenix Set
        } else if (fractalOption == 3) {
            PhoenixSet.ZOOM += zoomPhoenix;
            phoeSet = new PhoenixSet(colorBackground, colorLine, colorFill,
                FractalGenerator.iterations);
            phoeSet.paintComponent(g);
        }
    }

    // A method used to choose the saving and opening location of a file
    // @param option - option number whether a file is being saved or opened
    private File getSaveLocation(int option) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); 
        int result = 0;

        // depending on whether a file is saved or opened,
        // show appropriate window
        if (option == 1) {
            result = chooser.showSaveDialog(this);
        } else if (option == 2) {
            result = chooser.showOpenDialog(this);
        }
        
        if (result == chooser.APPROVE_OPTION) { 
            return chooser.getSelectedFile();
        } else {
            return null;
        }
    }

    // A method used to load the file and extract the data
    // @param saveLocation - location of the file to be opened
    public void loadFile(File saveLocation) throws FileNotFoundException {
        Scanner sc = new Scanner(saveLocation);
        fractalOption = sc.nextInt();
        colorBackground = new Color(sc.nextInt());
        colorFill = new Color(sc.nextInt());
        colorLine = new Color(sc.nextInt());
        FractalGenerator.iterations = sc.nextInt();
        FractalGenerator.angleTriangle[0] = sc.nextInt();
        FractalGenerator.angleTriangle[1] = sc.nextInt();
        repaint();
        sc.close();
    }

    // A method used to load the file and extract the data
    // @param saveLocation - location of the file to be saved
    public void writeOutput(File saveLocation) throws FileNotFoundException {

        String path = saveLocation.toString();
        int index = path.length() - 1;
        char letter = path.charAt(index);
        while (letter != '\\') {
            index--;
            letter = path.charAt(index);
        }
        String filename = path.substring(index, path.length());
        path = path.substring(0, index);
        File newSaveLocation = new File(path);
        PrintWriter file = new PrintWriter(new File(newSaveLocation, filename + ".txt"));
        String settingString = Integer.toString(fractalOption);
        settingString += " " + Integer.toString(colorBackground.getRGB());
        settingString += " " + Integer.toString(colorLine.getRGB());
        settingString += " " + Integer.toString(colorFill.getRGB());
        settingString += " " + Integer.toString(FractalGenerator.iterations);
        settingString += " " + Integer.toString(FractalGenerator.angleTriangle[0]);
        settingString += " " + Integer.toString(FractalGenerator.angleTriangle[1]);
        file.write(settingString);
        file.close();
    }   
}
