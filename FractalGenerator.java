import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.KeyStroke;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*
 * Fractal Generator
 * 
 * The program visualizing 3 different fractals:
 * Pythagoras Tree, Mandelbrot Set and Phoenix Set.
 *
 * @author Tomasz Soróbka
 * @id 1808982
 */

public class FractalGenerator {
    // Below are the main components of the GUI design
    JFrame frame;
    FractalCanvas fractalPainting;
    JPanel sideBar;
    JMenuBar menuBar;
    JSplitPane splitPane;

    // Components of the menu
    JMenu menuFile;
    JMenu menuFractals;
    JMenu menuTools;
    JMenuItem menuItem;
    JCheckBoxMenuItem cbMenuItem;

    // Color sliders and components
    JSlider colorSliderRed;
    JSlider colorSliderGreen;
    JSlider colorSliderBlue;
    JColorChooser colorChooser;
    JLabel colorLabel;
    JPanel colorOptions;

    // Gui elements for spinners and buttons
    JButton applyChanges;
    JLabel iterationsNumber;
    JLabel[] angleLabel = new JLabel[2];
    SpinnerModel anglSpinnerModel;
    JSpinner[] angleSpinner = new JSpinner[2];

    // Static variables used for storing colors, iterations and angles
    static int[] redValues;
    static int[] greenValues;
    static int[] blueValues;
    static int iterations;
    static int[] angleTriangle;
    
    // Final variables
    static final int RGB_MIN = 0;
    static final int RGB_MAX = 255;
    static final int RGB_INIT = 127;
    static final int ITER_MAX = 250;
    final int SPACE_BETWEEN_SLIDERS = 10;

    // Boolean variables
    public static boolean showingSidebar;
    public static boolean showingAngles;

    // a method used for removing the labels and sliders of the angle input when
    // the chosen fractal is not a Pythagoras Fractal
    void removeAngleInput() {
        if (showingAngles) {
            colorOptions.remove(angleLabel[0]);
            colorOptions.remove(angleSpinner[0]);
            colorOptions.remove(angleLabel[1]);
            colorOptions.remove(angleSpinner[1]);
            frame.pack();
        }
        showingAngles = false;    
    }

    // a method used for adding the labels and sliders of the angle input when
    // the chosen fractal is a Pythagoras Fractal
    void addAngleInput() {
        if (!showingAngles) {
            colorOptions.add(angleLabel[0]);
            colorOptions.add(angleSpinner[0]);
            colorOptions.add(angleLabel[1]);
            colorOptions.add(angleSpinner[1]);
            frame.pack();
        }    
        showingAngles = true;   
    }

    // a method used for making the sidebar visible or hidden depending on the current state
    void changeSidebar() {
        // remove the splitPane from the frame
        frame.remove(splitPane);

        // if the side bar is hidden, make it visible or vice versa
        if (showingSidebar) {
            showingSidebar = false;
            JPanel blank = new JPanel();
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                blank, fractalPainting);
        } else {
            showingSidebar = true;
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                sideBar, fractalPainting);
        }

        // add the new splitpane to the frame and repack it
        frame.add(splitPane, BorderLayout.CENTER);
        frame.pack();
    }

    /* A method responsible for adding the color sliders. Three for each of the following:
    * background, line and fill color
    * Each type is given three sliders: red, blue and green.
    *
    * @param name - name of the label for the sliders
    * @param indexOfPanel - index of the sliders, 
    * determines where in the array the colors will be stored
     */ 

    void addColorSliders(String name, int indexOfSlider) {

        // declare and initialize hashtables that will be used to add labels below the sliders
        Hashtable<Integer, JLabel> labelTableRed = new Hashtable<Integer, JLabel>();
        Hashtable<Integer, JLabel> labelTableGreen = new Hashtable<Integer, JLabel>();
        Hashtable<Integer, JLabel> labelTableBlue = new Hashtable<Integer, JLabel>();

        // Put the color names into the Hashtables
        labelTableRed.put(0, new JLabel("Red"));
        labelTableGreen.put(0, new JLabel("Green"));
        labelTableBlue.put(0, new JLabel("Blue"));

        // assign all the necessary labels, sliders, changeListeners for the red color
        colorLabel = new JLabel(name, JLabel.CENTER);
        colorSliderRed = new JSlider(JSlider.HORIZONTAL, RGB_MIN, RGB_MAX, RGB_INIT);
        colorSliderRed.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                redValues[indexOfSlider] = colorSliderRed.getValue();
            }
        });
        colorSliderRed.setLabelTable(labelTableRed);
        colorSliderRed.setPaintLabels(true);

        // assign all the necessary labels, sliders, changeListeners for the green color
        colorSliderGreen = new JSlider(JSlider.HORIZONTAL, RGB_MIN, RGB_MAX, RGB_INIT);
        colorSliderGreen.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                greenValues[indexOfSlider] = colorSliderGreen.getValue();
            }
        });
        colorSliderGreen.setLabelTable(labelTableGreen);
        colorSliderGreen.setPaintLabels(true);

        // assign all the necessary labels, sliders, changeListeners for the blue color
        colorSliderBlue = new JSlider(JSlider.HORIZONTAL, RGB_MIN, RGB_MAX, RGB_INIT);
        colorSliderBlue.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                blueValues[indexOfSlider] = colorSliderBlue.getValue();
            }
        });
        colorSliderBlue.setLabelTable(labelTableBlue);
        colorSliderBlue.setPaintLabels(true);

        // add all of the sliders, labels spacing between them to the colorOptions JPanel
        // that is added to the sidebar
        colorOptions.add(Box.createRigidArea(new Dimension(0, 2 * SPACE_BETWEEN_SLIDERS)));
        colorOptions.add(colorLabel);
        colorOptions.add(colorSliderRed);
        colorOptions.add(colorSliderGreen);
        colorOptions.add(colorSliderBlue);
        colorOptions.add(Box.createRigidArea(new Dimension(0, 3 * SPACE_BETWEEN_SLIDERS)));
    }

    /*
    * A method that adds the spinners for inputting angle when the Pythagoras Fractal is chosen.
    * 
    * @param name - name of the JLabel for the spinner
    * @param indexOfAngle - index of the spinner, 
    * determines where in the array the angle values will be stored
    */ 
    void addAngleSpinners(String name, int indexOfAngle) {
        
        angleLabel[indexOfAngle] = new JLabel(name, JLabel.CENTER);
        anglSpinnerModel = new SpinnerNumberModel(45, 30, 60, 1);     
        angleSpinner[indexOfAngle] = new JSpinner(anglSpinnerModel);
        angleSpinner[indexOfAngle].setMaximumSize(new Dimension(100, 50));
        angleSpinner[indexOfAngle].addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                angleTriangle[indexOfAngle] = (int) angleSpinner[indexOfAngle].getValue();
            }
        });
    }

    /* A method used for zooming in or out depending on whether the click is in the fractalPainint
     * or the menu item "Zoom out"
     * 
     * @param x - x-coordinate of the mouse click
     * @param y - y-coordinate of the mouse click
     * @param fPainting - FractalCanvas object that makes the fractals visible
     * @param zoomValue - depending on the value the fractal is zoomed in or out
     */
    void zoomInOrOut(int x, int y, FractalCanvas fPainting, double zoomValue) {
        if (x >= 0 && y >= 0) {
            if (FractalCanvas.fractalOption == 1) {
                FractalCanvas.zoomPythagoras += zoomValue;
            } else if (FractalCanvas.fractalOption == 2) {
                FractalCanvas.zoomMandel += zoomValue;
            } else if (FractalCanvas.fractalOption == 3) {
                FractalCanvas.zoomPhoenix += zoomValue;
            }
            fPainting.repaint();
        }
        
    }

    // a method that initializes all the menuItems necessary for the "File" Category
    void fileMenuItems() {
        menuFile = new JMenu("File");
        menuItem = new JMenuItem("Save the setting's file",
            new ImageIcon("icons/save.png"));
        menuItem.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_1, ActionEvent.ALT_MASK));
        menuItem.addActionListener(fractalPainting);
        menuFile.add(menuItem);

        menuItem = new JMenuItem("Load a setting's file", new ImageIcon("icons/open.png"));
        menuItem.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_2, ActionEvent.ALT_MASK));
        menuItem.addActionListener(fractalPainting);
        menuFile.add(menuItem);
    }

    // a method that initializes all the menuItems necessary for the "Fractals" Category
    void fractalMenuItems() {
        menuFractals = new JMenu("Fractals");
        menuItem = new JMenuItem("Pythagoras Tree",
            new ImageIcon("icons/Pythagorean.png"));
        menuItem.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_3, ActionEvent.ALT_MASK));
        menuItem.addActionListener(fractalPainting);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addAngleInput();
            }
        });
        
        menuFractals.add(menuItem);
        menuItem = new JMenuItem("Mandelbrot Set",
            new ImageIcon("icons/Mandelbrot.png"));
        menuItem.setAccelerator(KeyStroke  
            .getKeyStroke(KeyEvent.VK_4, ActionEvent.ALT_MASK));
        menuItem.addActionListener(fractalPainting);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeAngleInput();
            }
        });
        
        menuFractals.add(menuItem);
        menuItem = new JMenuItem("Phoenix Set",
            new ImageIcon("icons/phoenix.png"));
        menuItem.setAccelerator(KeyStroke
            .getKeyStroke(KeyEvent.VK_5, ActionEvent.ALT_MASK));
        menuItem.addActionListener(fractalPainting);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removeAngleInput();
            }
        });
        menuFractals.add(menuItem);
    }

    // a method that initializes all the menuItems necessary for the "Tools" Category
    void toolsMenuItems() {
        menuTools = new JMenu("Tools");
        cbMenuItem = new JCheckBoxMenuItem("Do not show sidebar");
        cbMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_6, ActionEvent.ALT_MASK));
        cbMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeSidebar();
            }
        });
        
        menuTools.add(cbMenuItem);
        menuItem = new JMenuItem("Zoom out");
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_7, ActionEvent.ALT_MASK));
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                zoomInOrOut(0, 0, fractalPainting, -50.0);
                zoomInOrOut(0, 0, fractalPainting, -50.0);
            }
        });
        menuTools.add(menuItem);
    }

    // a method that initializes all the components necessary for the iterations spinner
    void addIterationSpinner() {
        iterationsNumber = new JLabel("Number of iterations: ");
        colorOptions.add(iterationsNumber);

        SpinnerModel model = new SpinnerNumberModel(0, 0, ITER_MAX, 1);     
        JSpinner spinner = new JSpinner(model);
        spinner.setMaximumSize(new Dimension(100, 50));
        spinner.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                iterations = (int) spinner.getValue();
            }
        });
        colorOptions.add(spinner);
    }


    // The contructor of this class
    FractalGenerator() {
        // Initialization of basic arrays and boolean values
        redValues = new int[3];
        greenValues = new int[3];
        blueValues = new int[3];
        angleTriangle = new int[2];
        showingSidebar = true;
        showingAngles = false;
        angleLabel = new JLabel[2];
        angleSpinner = new JSpinner[2];
        angleTriangle[0] = 45;
        angleTriangle[1] = 45; 

        // Assembling the whole GUI in invokeLater method
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                
                 
                // Initializing frame, image, fractalCanvas, colorOptions and layout
                frame = new JFrame("Fractal Generator");
                Image img = new ImageIcon(this.getClass()
                    .getResource("icons/FractalsIcon.jpg")).getImage();
                frame.setIconImage(img);
                fractalPainting = new FractalCanvas();
                colorOptions = new JPanel();
                colorOptions.setLayout(new BoxLayout(colorOptions, BoxLayout.Y_AXIS));
                
                // Assembling the menu
                menuBar = new JMenuBar();
                // "File" CATEGORY 
                fileMenuItems();
                // "Fractals" CATEGORY
                fractalMenuItems();
                // "Tools" CATEGORY
                toolsMenuItems();

                // Add all the categories to the main menuBar
                menuBar.add(menuFile);
                menuBar.add(menuFractals);
                menuBar.add(menuTools);

                // Creating sideBar
                sideBar = new JPanel(new BorderLayout());
                sideBar.setPreferredSize(new Dimension(300, 200));

                // Adding the color sliders
                addColorSliders("Background color: ", 0);    
                addColorSliders("Line color: (only for Pythagoras tree): ", 1);  
                addColorSliders("Fill color: ", 2);

                // Adding the iteration spinner
                addIterationSpinner();

                // Adding the angle spinners
                addAngleSpinners("Left Angle (only for Pythagoras tree): ", 0);
                addAngleSpinners("Right Angle (only for Pythagoras tree): ", 1);
                
                // add colorOptions and Apply Changes button to the sidebar
                sideBar.add(colorOptions, BorderLayout.CENTER);
                applyChanges = new JButton("Apply Changes");
                applyChanges.addActionListener(fractalPainting);
                sideBar.add(applyChanges, BorderLayout.SOUTH);

                // Add a mouseListener to the fractalPaining for zooming purposes
                fractalPainting.addMouseListener(new MouseListener() { 
                    public void mouseClicked(java.awt.event.MouseEvent e) {}

                    public void mouseReleased(java.awt.event.MouseEvent e) {}
                    
                    public void mouseEntered(java.awt.event.MouseEvent e) {}

                    public void mouseExited(java.awt.event.MouseEvent e) {} 

                    // When the mouse is pressed, zoom in by the value of 50.0
                    @Override
                    public void mousePressed(java.awt.event.MouseEvent e) {
                        zoomInOrOut(e.getX(), e.getY(), fractalPainting, 50.0);
                        zoomInOrOut(e.getX(), e.getY(), fractalPainting, 50.0);
                    }
                });

                // Assemble the splitPane
                splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                sideBar, fractalPainting);
                splitPane.setOneTouchExpandable(true);
                splitPane.setDividerLocation(250);

                // Assemble the frame
                frame.add(splitPane, BorderLayout.CENTER);
                frame.setJMenuBar(menuBar);
                frame.pack();
                frame.setVisible(true);
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            }
        }); 
    }

    public static void main(String[] arg) {
        new FractalGenerator();
    }
}
