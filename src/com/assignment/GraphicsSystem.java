package com.assignment;

import com.assignment.shapesdetails.CircleDetails;
import com.assignment.shapesdetails.RectangleDetails;
import com.assignment.shapesdetails.TriangleDetails;
import uk.ac.leedsbeckett.oop.LBUGraphics;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class GraphicsSystem extends LBUGraphics {

    private JMenuItem saveImage;
    private JMenuItem loadImage;
    private JMenuItem saveCommand;
    private JMenuItem loadCommand;
    private JButton colorPicker;
    private boolean drawFilledShape = false;
    private boolean isCommandFileSaved = false;
    private boolean isImageFileSaved = false;
    private final Graphics g = getGraphicsContext();


    private boolean firstProcessCommand = true;
    boolean shapePositionReset = false;
    ArrayList<String> commandToFile = new ArrayList<>();
    ArrayList<CircleDetails> circleDetailsArrayList = new ArrayList<CircleDetails>();
    ArrayList<TriangleDetails> triangleDetailsArrayList = new ArrayList<TriangleDetails>();
    ArrayList<RectangleDetails> rectangleDetailsArrayList = new ArrayList<RectangleDetails>();

    /*
     * Constructor
     * Calls Parent class constructor
     * Adds Actions Listener to Color Picker Button
     * Add Color Picker Button to the panel
     * */

    GraphicsSystem() {

        super();

        JFrame frame = new JFrame();
        frame.setTitle("Turtle Canvas");
        frame.setLayout(new FlowLayout());
        frame.pack();
        frame.setSize(1050, 450);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ImageIcon imageIcon = new ImageIcon("turtle90.png");
        frame.setIconImage(imageIcon.getImage());
        frame.add(this);
        warningClosing(frame);
        setupMenuBar();
        setupBtnColorPicker();
        frame.setVisible(true);

        about();

        reset();
    }


    public static void main(String[] args) {
        new GraphicsSystem();
    }

    /*
    * Create instance of Menu bar
    * Create instances of MenuItems
    * Adds menu to frame.
    * Adds listener to each of the menuItems
    * */
    public void setupMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        loadCommand = new JMenuItem("Load Command");
        saveCommand = new JMenuItem("Save Command");
        loadImage = new JMenuItem("Load Image");
        saveImage = new JMenuItem("Save Image");

        menuBar.add(menu);
        menu.add(loadImage);
        menu.add(saveImage);
        menu.add(loadCommand);
        menu.add(saveCommand);

        super.add(menuBar);

        loadCommand.addActionListener(this);
        saveCommand.addActionListener(this);
        loadImage.addActionListener(this);
        saveImage.addActionListener(this);

    }


    /*
    * Instance of JButton
    * Added to frame
    * Adds instance to the actionListener
    * */
    public void setupBtnColorPicker() {
        colorPicker = new JButton("Color Picker");
        super.add(colorPicker);
        colorPicker.addActionListener(this);
    }

    /*
     * Action Listener
     * Acts according to the button clicked
     * i.e known by .getSource();
     * */
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == colorPicker) {
            Color color = JColorChooser.showDialog(null, "Pick a color...", Color.RED);
            commandToFile.add("pen "+color.getRed()+" "+color.getGreen()+" "+color.getBlue());
            setPenColour(color);
            g.setColor(color);
        } else if (arg0.getSource() == loadCommand) {
            loadCommand();
        } else if (arg0.getSource() == saveCommand) {
            saveCommand();
        } else if (arg0.getSource() == loadImage) {
            loadImage();
        } else if (arg0.getSource() == saveImage) {
            saveImage();
        } else {
            super.actionPerformed(arg0);
        }

    }


    /*
    * Method named processCommand that takes a string parameter
    * If the function is called for very first time reset and clear is called
    * The string parameter is saved to a textfile using storeCommandToFile()
    * String parameter added to list to save on a specific file later on
    * The total words in string is stored in commandStringCount
    * All the words are added to commandStringList
    * Since first element of command list should be the command
    * It is then
    * checked with commandWithParameterList, colorList and other command and acts accordingly
    * */
    @Override
    public void processCommand(String s) {

        int commandStringCount = 0;
        int parameterValue;
        Scanner scanString = new Scanner(s);
        ArrayList<String> commandStringsList = new ArrayList<String>(2);
        String[] CommandWithParameterList = {"turnleft", "turnright", "forward", "backward", "pen", "stroke", "circle", "triangle", "rectangle"};
        Colors[] colorsList = {new Colors("black", Color.BLACK), new Colors("red", Color.RED), new Colors("green", Color.GREEN), new Colors("white", Color.WHITE), new Colors("yellow", Color.YELLOW)};


        if (firstProcessCommand) {
            reset();
            clear();
            firstProcessCommand = false;
        }

        storeCommandToFile(s);
        commandToFile.add(s);
        isCommandFileSaved = false;
        isImageFileSaved = false;

        //returning the total number of commands string passed in text field
        while (scanString.hasNext()) {
            commandStringsList.add(scanString.next());
            commandStringCount++;
        }

        //checking with commands that require parameters
        for (String command : CommandWithParameterList) {

            if (command.equalsIgnoreCase(commandStringsList.get(0))) {
                if (commandStringCount == 1) {
                    displayMessage("Valid Command with Missing parameter.");
                } else {
                    try {
                        parameterValue = Integer.parseInt(commandStringsList.get(1));
                    } catch (Exception e) {
                        displayMessage("Non numeric data for parameter.");
                        return;
                    }

                    if (parameterValue < 0) {
                        displayMessage("Negative value detected.");
                    } else if (parameterValue > 360) {
                        displayMessage("Non sensible value detected.");
                    } else {
                        switch (command) {
                            case "turnleft" -> {
                                turnLeft(parameterValue);
                                displayMessage("Pen turned left " + parameterValue + " degree");
                            }
                            case "turnright" -> {
                                turnRight(parameterValue);
                                displayMessage("Pen turned right " + parameterValue + " degree");
                            }
                            case "forward" -> {
                                forward(parameterValue);
                                displayMessage("Pen moved forward " + parameterValue + " distance");
                            }

                            case "backward" -> {
                                backward(parameterValue);
                                displayMessage("Pen moved backward " + parameterValue + " distance");
                            }

                            case "pen" -> {
                                setPenColorRGB(commandStringsList);
                            }
                            case "stroke" -> {
                                setPenStroke(commandStringsList);
                            }
                            case "circle" -> {
                                if (parameterValue > 50) {
                                    displayMessage("Cannot create a circle of raidus greater than 100.");
                                } else {
                                    circle(parameterValue);
                                    displayMessage("Circle of radius " + parameterValue + " drawn");
                                }
                            }
                            case "triangle" -> {
                                triangle(commandStringsList);
                            }
                            case "rectangle" -> {
                                rectangle(commandStringsList);
                            }

                        }
                    }

                }
                // to break the function when parameter matches with one.
                return;
            }
        }


        // checking string with colors
        for (Colors colorElement : colorsList) {
            if (colorElement.colorName.equalsIgnoreCase(s)) {
                setPenColour(colorElement.color);
                g.setColor(colorElement.color);
                displayMessage("Pen Colour Set to " + colorElement.colorName);
                return;
            }
        }


        // checking string with command that require no parameters
        switch (s.toLowerCase()) {
            case "about" -> {
                about();
            }

            case "penup" -> {
                penUp();
                displayMessage("Pen is lifted. Movement is not shown");
            }

            case "pendown" -> {
                penDown();
                displayMessage("Pen is placed down. Movement will be shown.");
            }

            case "reset" -> {
                reset();
                displayMessage("Turtle position reset / centered.");
            }

            case "clear" -> {
                clear();
                displayMessage("Canvas is cleared & turtle position is reset.");
            }
            case "new" -> {
                newClear();
                displayMessage("Canvas is cleared with turtle in latest position.");
            }

            case "fill" -> {
                if (drawFilledShape) {
                    drawFilledShape = false;
                    displayMessage("Non filled shape will be drawn.");
                } else {
                    drawFilledShape = true;
                    displayMessage("Filled shape will be drawn.");
                }
            }

            case "changefill" -> {
                if (circleDetailsArrayList.size() == 0 && rectangleDetailsArrayList.size() == 0 && triangleDetailsArrayList.size() == 0) {
                    displayMessage("No shapes to be reversed");
                } else {

                    for(TriangleDetails triangle : triangleDetailsArrayList){
                        g.setColor(triangle.getShapeColor());
                       if(triangle.isTriangleFilled()){
                           g.clearRect(triangle.getxPoints()[0] - triangle.getLengthA(), triangle.getyPoints()[0] + 1, triangle.getLengthB() * 2, Math.max(triangle.getLengthA(), triangle.getLengthC()));
                           g.drawPolygon(triangle.getxPoints(), triangle.getyPoints(), 3);
                           triangle.changeFilling();
                       }else{
                           g.fillPolygon(triangle.getxPoints(), triangle.getyPoints(), 3);
                           triangle.changeFilling();
                       }
                    }
                    for (CircleDetails circle : circleDetailsArrayList) {
                        g.setColor(circle.getShapeColor());
                        if (circle.isCircleFilled()) {
                            g.clearRect(circle.getCircleXpos(), circle.getCircleYpos(), circle.getWidth(), circle.getHeight());
                            g.drawOval(circle.getCircleXpos(), circle.getCircleYpos(), circle.getWidth(), circle.getHeight());
                            circle.changeFilling();
                        } else {
                            g.fillOval(circle.getCircleXpos(), circle.getCircleYpos(), circle.getWidth(), circle.getHeight());
                            circle.changeFilling();
                        }
                    }
                    for (RectangleDetails rectangle : rectangleDetailsArrayList) {
                        g.setColor(rectangle.getShapeColor());
                        if (rectangle.isRectangleFilled()) {
                            g.clearRect(rectangle.getRectXpos(), rectangle.getRectYpos(), rectangle.getLength(), rectangle.getBreadth());
                            g.drawRect(rectangle.getRectXpos(), rectangle.getRectYpos(), rectangle.getLength(), rectangle.getBreadth());
                            rectangle.changeFilling();
                        } else {
                            g.fillRect(rectangle.getRectXpos(), rectangle.getRectYpos(), rectangle.getLength(), rectangle.getBreadth());
                            rectangle.changeFilling();
                        }
                    }

                    displayMessage("Filling Reversed.");
                }
            }

            default -> displayMessage("Invalid Command");
        }
    }

    /*
    * Overriding the reset() Method
    * Sets turtle to center
    * pen is set to down
    * turtle faces downwards
    * background color and stroke is reset
    * */
    @Override
    public void reset() {
        this.xPos = 500;
        this.yPos = 200;
        penDown();
        this.direction = 90;
        this.repaint();
        this.setBackground_Col(Color.BLACK);
        this.setStroke(1, false);
        shapePositionReset = false;
    }


    /*
    * Overriding the clear() Method
    * Calls newClear() to clear screen
    * Calls reset to reset the turtle
    * */
    @Override
    public void clear() {
        newClear();
        reset();
    }

    /*
    * Method named newClear();
    * Calls parent clear() Method
    * Clears all element from specified lists.
    * */
    public void newClear() {
        super.clear();
        circleDetailsArrayList.clear();
        triangleDetailsArrayList.clear();
        rectangleDetailsArrayList.clear();
    }

    /*
    * Overriding about() Method
    * Calls parent about
    * Set's turtle direction facing downwards
    * Appends name when the method is called.
    * */
    @Override
    public void about() {
        super.about();
        this.direction = 90;
        displayMessage("Sushant Neupane");
        g.drawString("Sushant Neupane", 200, 200);
    }

    /*
    * Method named backward which takes integer type amount as parameter
    * It's makes the turtle move backwards by amount distance
    * Could also use forward(- amount) for same purpose
    * */
    public void backward(int amount) {
        turnLeft(180);
        penDown();
        forward(amount);
        turnLeft(180);
    }


    /*
    * Method name setPenStroke that takes ArrayList of String as parameter
    * This is because we need to specify the pen size as well as if it is dashed or not
    * The first element will be stroke followed by pensize followed by dashed or null
    * */
    public void setPenStroke(ArrayList<String> arrayList) {
        boolean dashed = false;
        int parameterValue;
        if (arrayList.size() == 3) {
            if (arrayList.get(2).equalsIgnoreCase("dashed")) {
                dashed = true;
            }
        }
        if (arrayList.size() > 3) {
            displayMessage("Invalid amount of parameter.");
        }
        try {
            parameterValue = Integer.parseInt(arrayList.get(1));
        } catch (NumberFormatException numberFormatException) {
            displayMessage("Non numeric data for parameter.");
            return;
        }
        if (parameterValue > 10 || parameterValue < 1) {
            displayMessage("Cannot set " + parameterValue + " as a stroke size.");
        } else {
            setStroke(parameterValue, dashed);
            displayMessage("Stroke size set to " + parameterValue);
        }
    }


    /*
    * Overriddien circle() method which takes a integer radius as parameter
    * If any of the shapeMethod is called for the first time the shape will be drawn in the specified position
    * The circle will be either filled or non filled depending on the command previously given
    * The value of circle is added to a list so that it's fillings can be reversed later on
    * The position of turtle is set in such a way that non of the shapes will overlap
    * */
    @Override
    public void circle(int radius) {

        if (!shapePositionReset) {
            setxPos(100);
            setyPos(100);
            shapePositionReset = true;
        }

        if (drawFilledShape) {
            g.fillOval(getxPos(), getyPos(), radius * 2, radius * 2);
        } else {
            g.drawOval(getxPos(), getyPos(), radius * 2, radius * 2);
        }
        circleDetailsArrayList.add(new CircleDetails(getxPos(), getyPos(), radius, drawFilledShape, getPenColour()));
        setxPos(getxPos() + radius * 3);

        if (getxPos() >= 900) {
            setxPos(100);
            setyPos(210);
        }
    }



    /*
    * Method named triangle() that takes a list of string as parameter
    * Validates the command accordingly
    * Calls drawTriangle() method for actual drawing.
    * */
    public void triangle(ArrayList<String> arrayList) {
        int lengthA;
        int lengthB;
        int lengthC;
        if (arrayList.size() == 2) {
            try {
                lengthA = Integer.parseInt(arrayList.get(1));
            } catch (NumberFormatException numberFormatException) {
                displayMessage("Non numeric data for parameter.");
                return;
            }
            drawTriangle(lengthA, lengthA, lengthA);

        } else if (arrayList.size() == 4) {
            try {
                lengthA = Integer.parseInt(arrayList.get(1));
                lengthB = Integer.parseInt(arrayList.get(2));
                lengthC = Integer.parseInt(arrayList.get(3));
            } catch (NumberFormatException numberFormatException) {
                displayMessage("Non numeric data for parameter.");
                return;
            }
            if (lengthA > 100 || lengthB > 100 || lengthC > 100) {
                displayMessage("Cannot create triangle greater than length 100");
            } else {
                drawTriangle(lengthA, lengthB, lengthC);
            }
        } else {
            displayMessage("Invalid amount of parameters.");
        }

    }

    /*
     * Method named drawTriangle() that takes length of three sides as parameter
     * If any of the shapeMethod is called for the first time the shape will be drawn in the specified position
     * The triangle will be either filled or non filled depending on the command previously given
     * The value of triangle is added to a list so that it's fillings can be reversed later on
     * The position of turtle is set in such a way that non of the shapes will overlap
     * */
    void drawTriangle(int lengthA, int lengthB, int lengthC) {

        if (!shapePositionReset) {
            setxPos(100);
            setyPos(100);
            shapePositionReset = true;
        }
        final int A_X_POS = getxPos();
        final int A_Y_POS = getyPos();
        final int B_X_POS = getxPos() - (lengthB / 2);
        final int B_Y_POS = getyPos() + lengthA;
        final int C_X_POS = getxPos() + (lengthB / 2);
        final int C_Y_POS = getyPos() + lengthC;
        final int[] xPoints = new int[]{A_X_POS, B_X_POS, C_X_POS};
        final int[] yPoints = new int[]{A_Y_POS, B_Y_POS, C_Y_POS};

        if (drawFilledShape) {
            g.fillPolygon(xPoints, yPoints, 3);
        } else {
            g.drawPolygon(xPoints, yPoints, 3);
        }
        triangleDetailsArrayList.add(new TriangleDetails(xPoints, yPoints,lengthA, lengthB, lengthC, drawFilledShape, getPenColour()));
        setxPos(getxPos() + lengthB + 10);
        displayMessage("Traingle drawn.");
        if (getxPos() >= 900) {
            setxPos(100);
            setyPos(210);
        }

    }

    /*
     * Method named rectangle() that takes a list of string as parameter
     * Validates the command accordingly
     * Calls drawRectangle() method for actual drawing.
     * */
    public void rectangle(ArrayList<String> arrayList) {
        int length;
        int breadth;

        if (arrayList.size() == 3) {
            try {
                length = Integer.parseInt(arrayList.get(1));
                breadth = Integer.parseInt(arrayList.get(2));
            } catch (NumberFormatException numberFormatException) {
                displayMessage("Non numeric data for parameter.");
                return;
            }
            if (length > 200 || breadth > 200 ) {
                displayMessage("Cannot create rectangle greater than length 200");
            } else {
                drawRectangle(length, breadth);
            }
        } else {
            displayMessage("Invalid amount of parameters.");
        }

    }

    /*
     * Method named drawRectangle() that takes length of three sides as parameter
     * If any of the shapeMethod is called for the first time the shape will be drawn in the specified position
     * The rectangle will be either filled or non filled depending on the command previously given
     * The value of rectangle is added to a list so that it's fillings can be reversed later on
     * The position of turtle is set in such a way that non of the shapes will overlap
     * */
    void drawRectangle(int length, int breadth){
        if (!shapePositionReset) {
            setxPos(100);
            setyPos(100);
            shapePositionReset = true;
        }
        if (drawFilledShape) {
            g.fillRect(getxPos(), getyPos(), length, breadth);
        } else {
            g.drawRect(getxPos(), getyPos(), length, breadth);
        }

        rectangleDetailsArrayList.add(new RectangleDetails(getxPos(), getyPos(), length, breadth, drawFilledShape, getPenColour()));
        setxPos((int) (getxPos() + length * 1.5));
        displayMessage("Rectangle drawn drawn.");
        if (getxPos() >= 900) {
            setxPos(100);
            setyPos(210);
        }
    }

    /*
    * Method names setPenColorRGB() that takes a list as parameter
    * Validates the command
    * Set's the RGB color to the pen as well as Graphics instance
    * */
    void setPenColorRGB(ArrayList<String> arrayList) {
        int red;
        int green;
        int blue;
        if (arrayList.size() < 4) {
            displayMessage("Valid command with missing parameter.");
            return;
        }
        if (arrayList.size() > 4) {
            displayMessage("Valid command with excess parameter");
            return;
        }
        try {
            red = Integer.parseInt(arrayList.get(1));
            green = Integer.parseInt(arrayList.get(2));
            blue = Integer.parseInt(arrayList.get(3));
        } catch (NumberFormatException numberFormatException) {
            displayMessage("Non Numeric data for parameter.");
            return;
        }
        if (red < 0 || green < 0 || blue < 0) {
            displayMessage("Negative value detected.");
        } else if (red > 255 || green > 255 || blue > 255) {
            displayMessage("Non sensible value detected");
        } else {
            setPenColour(new Color(red, green, blue));
            g.setColor(new Color(red, green, blue));
            displayMessage("Pen Colour Changed.");
        }

    }

    /*
    * Method named loadImage()
    * Creates a instance of JFileChooser (GUI) to choose files
    * Calls the .showOpenDialog() method and saves the received response to a variable named response
    * Reads the image choosen from fileChooser and saves it' to loadedImage
    * Resizes the Image to frame's dimensions
    * Set's the Image to the frame
    * */
    void loadImage() {
        int response;
        JFileChooser fileChooser = new JFileChooser();
        response = fileChooser.showOpenDialog(null); //select file to open
        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedImage loadedImage = ImageIO.read(fileChooser.getSelectedFile());
                BufferedImage resizedImage = resize(loadedImage, 1000, 400);
                setBufferedImage(resizedImage);
                displayMessage("Image Loaded Sucessfully.");
            } catch (IOException e) {
                displayMessage("Failed to Load Image");
            }
        } else {
            displayMessage("No file chosen");
        }

    }


    /*
    * Method named resize() that takes image, width and height as parameter
    * Creates a temporary image of desired size
    * Creates a reference variable named resized of desired size to use for graphics
    * Create a instance of Graphics2D named graphics2d and initialized it with resized.createGraphics()
    * Draws the saved temporary image to graphics2d which saved the image to resized
    * Disposed the graphics2d
    * return the resized Image
    * */
    BufferedImage resize(BufferedImage image, int width, int height) {
        Image temp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resized.createGraphics();
        graphics2D.drawImage(temp, 0, 0, null);
        graphics2D.dispose();
        return resized;
    }

    /*
     * Method named saveImage()
     * Creates a instance of JFileChooser (GUI) to choose files
     * Calls the .showOpenDialog() method and saves the received reponse to a variable named response
     * Writes the image to the file choosen from fileChooser in png Format
     * */
    void saveImage() {
        int response;
        JFileChooser fileChooser = new JFileChooser();
        response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(getBufferedImage(), "png", fileChooser.getSelectedFile());
                isImageFileSaved = true;
                displayMessage("Image Saved Successfully");
            } catch (IOException e) {
                displayMessage("Failed to Save Image");
            }
        } else {
            displayMessage("No file chosen");
        }
    }

    /*
    * Method named loadCommand()
    * Creates a instance of JFileChooser (GUI) to choose files
    * Calls the .showOpenDialog() method and saves the received response to a variable named response
    * Calls newClear() and loadCommandFromFile() Method
    * */
    public void loadCommand() {
        int response;
        JFileChooser fileChooser = new JFileChooser();
        response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            newClear();
            loadCommandFromFile(fileChooser.getSelectedFile().getAbsolutePath());
        } else {
            displayMessage("No file chosen");
        }
    }

    /*
    * Method named loadCommandFromFile() that takes the fileName as parameter
    * Creates and instance of File with the fileName received as parameter
    * Creates an instance of Scanner class to read through the file
    * Read the file using while loop and .hasNextLine() method
    * Passes each command i.e each line from file to processCommand(eachline) method
    * */
    public void loadCommandFromFile(String fileName) {

        File file = new File(fileName);
        try {
            Scanner readFile = new Scanner(file);
            while (readFile.hasNextLine()) {
                String currentLine = readFile.nextLine();
                processCommand(currentLine);
            }
            displayMessage("Command Loaded from file");

        } catch (FileNotFoundException e) {
            displayMessage("Failed to laod file");
        }

    }


    /*
     * Method named saveCommand()
     * Creates a instance of JFileChooser (GUI) to choose files
     * Calls the .showOpenDialog() method and saves the received response to a variable named response
     * Calls storeCommandToFile() that takes list of string and fileName (String) as parameter
     * */
    public void saveCommand() {
        int response;
        JFileChooser fileChooser = new JFileChooser();
        response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            storeCommandToFile(commandToFile, fileChooser.getSelectedFile());
            isCommandFileSaved = true;
        }
    }

    /*
    * Method names storeCommandToFile() that takes a String command as parameter
    * The given string is always stored in CommandHistory.txt automatically
    * It is called in processCommand() method
    * */
    public void storeCommandToFile(String commandStr) {

        File file = new File("CommandHistory.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            fileWriter.write(commandStr + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /*
     * Method named storeCommandToFile() that takes the fileName and list of command stored as String as parameter
     * Creates and instance of File with the fileName received as parameter and extends with .txt
     * Creates an instance of FileWriter with file and set's true to append
     * Uses a forEach loop to write each element of list to the file
     * */
    public void storeCommandToFile(ArrayList<String> commandList, File fileName) {
        File file = new File(String.valueOf(fileName) + ".txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            for (String commandStr : commandList) {
                fileWriter.write(commandStr + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    * Method named warningClosing that takes a Frame as a parameter
    * calls the .addWindowListener() method of Frame class and passes a WindowAdapter()
    * Overrides the WindowClosing() method
    * If file is saved then the frame is closed
    * Otherwise a .showConfirmDialog() is used from JOptionPane class
    * It's response is saved to response
    * If Yes is pressed the program will proceed to save the command
    * If No is and if the image is saved  pressed the window is disposed
    * If Cancle is pressed the user is taken back to the screen
    * Similar processed is also applied for closing the window but checks if the image is saved or not.
    * */
    public void warningClosing(Frame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isCommandFileSaved) {
                    if(isImageFileSaved){
                        e.getWindow().dispose();
                    }else{
                        e.getWindow();
                    }
                } else {
                    int response = JOptionPane.showConfirmDialog(null, "Do you want to save the command before exiting ?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        saveCommand();
                    } else if (response == JOptionPane.NO_OPTION) {
                        if(isImageFileSaved){
                            e.getWindow().dispose();
                        }else{
                            e.getWindow();
                        }
                    } else {
                        e.getWindow();
                    }
                }

                if (isImageFileSaved) {
                    e.getWindow().dispose();
                } else {
                    int response = JOptionPane.showConfirmDialog(null, "Do you want to save the Image before exiting ?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        saveImage();
                    } else if (response == JOptionPane.NO_OPTION) {
                        e.getWindow().dispose();
                    } else {
                        e.getWindow();
                    }
                }
            }
        });
    }


}
