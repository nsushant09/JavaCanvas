package com.assignment;

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

    private final Graphics g = getGraphicsContext();
    private JMenuItem saveImage;
    private JMenuItem loadImage;
    private JMenuItem saveCommand;
    private JMenuItem loadCommand;
    private JMenuItem colorPicker;
    private boolean drawFilledShape = false;
    private boolean isCommandFileSaved = false;

    ArrayList<String> commandToFile = new ArrayList<>();

    Boolean shapePositionReset = false;
    ArrayList<CircleDetails> circleDetailsArrayList = new ArrayList<CircleDetails>();
    ArrayList<TriangleDetails> triangleDetailsArrayList = new ArrayList<TriangleDetails>();

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
        frame.setVisible(true);

        reset();
    }


    public static void main(String[] args) {
        new GraphicsSystem();
    }

    public void setupMenuBar() {

        JMenuBar menuBar = new JMenuBar();
        JMenu options = new JMenu("Options");
        loadCommand = new JMenuItem("Load Command");
        saveCommand = new JMenuItem("Save Command");
        loadImage = new JMenuItem("Load Image");
        saveImage = new JMenuItem("Save Image");
        colorPicker = new JMenuItem("Color Picker");

        menuBar.add(options);
        options.add(colorPicker);
        options.add(loadImage);
        options.add(saveImage);
        options.add(loadCommand);
        options.add(saveCommand);

        super.add(menuBar);

        colorPicker.addActionListener(this);
        loadCommand.addActionListener(this);
        saveCommand.addActionListener(this);
        loadImage.addActionListener(this);
        saveImage.addActionListener(this);

    }

    /*
     * Action Listener
     * Acts according to the button clicked
     * i.e known by .getSource();
     * */
    public void actionPerformed(ActionEvent arg0) {

        if (arg0.getSource() == colorPicker) {
            Color color = JColorChooser.showDialog(null, "Pick a color...", Color.RED);
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


    private boolean firstProcessCommand = true;

    @Override
    public void processCommand(String s) {

        int commandStringCount = 0;
        int parameterValue;
        Scanner scanString = new Scanner(s);
        ArrayList<String> commandStringsList = new ArrayList<String>(2);
        String[] CommandWithParameterList = {"turnleft", "turnright", "forward", "backward", "pen", "stroke", "circle", "triangle"};
        Colors[] colorsList = {new Colors("black", Color.BLACK), new Colors("red", Color.RED), new Colors("green", Color.GREEN), new Colors("white", Color.WHITE), new Colors("yellow", Color.YELLOW)};


        if (firstProcessCommand) {
            reset();
            clear();
            firstProcessCommand = false;
        }

        storeCommandToFile(s);
        commandToFile.add(s);
        isCommandFileSaved = false;

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

                        }
                    }

                }
                // to break the function when parameter matches with one.
                return;
            }
        }


        for (Colors colorElement : colorsList) {
            if (colorElement.colorName.equalsIgnoreCase(s)) {
                setPenColour(colorElement.color);
                g.setColor(colorElement.color);
                displayMessage("Pen Colour Set to " + colorElement.colorName);
                return;
            }
        }


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
                if (circleDetailsArrayList.size() == 0) {
                    displayMessage("No circles to be filled");
                } else {

                    for (CircleDetails circle : circleDetailsArrayList) {
                        if (circle.isCircleFilled()) {
                            g.clearRect(circle.getCircleXpos(), circle.getCircleYpos(), circle.getWidth(), circle.getHeight());
                            g.drawOval(circle.getCircleXpos(), circle.getCircleYpos(), circle.getWidth(), circle.getHeight());
                            circle.changeFilling();
                        } else {
                            g.fillOval(circle.getCircleXpos(), circle.getCircleYpos(), circle.getWidth(), circle.getHeight());
                            circle.changeFilling();
                        }
                    }
                    displayMessage("Circle Filling Reversed.");
                }
            }

            default -> displayMessage("Invalid Command");
        }
    }

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


    @Override
    public void clear() {
        newClear();
        reset();
    }

    public void newClear() {
        super.clear();
        circleDetailsArrayList.clear();
    }

    @Override
    public void about() {
        super.about();
        this.direction = 90;
        displayMessage("Sushant Neupane");
        g.drawString("Sushant Neupane", 200, 200);
    }

    public void backward(int amount) {
        turnLeft(180);
        penDown();
        forward(amount);
        turnLeft(180);
    }


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
     * List that stores recently drawn circle details
     * Details used later to change fillings
     * shapePositionReset used to set the position of turtle for first circle
     * Draws the circle using drawOval
     * Set's X and Y position for upcoming circle
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
        circleDetailsArrayList.add(new CircleDetails(getxPos(), getyPos(), radius, drawFilledShape));
        setxPos(getxPos() + radius * 3);

        if (getxPos() >= 900) {
            setxPos(100);
            setyPos(210);
        }
    }

    public void triangle(ArrayList<String> arrayList) {
        int lengthA;
        int lengthB;
        int lengthC;
        if (arrayList.size() == 1) {
            displayMessage("Valid command with missing parameter.");
        } else if (arrayList.size() == 2) {
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
        triangleDetailsArrayList.add(new TriangleDetails(xPoints, yPoints, drawFilledShape));
        setxPos(getxPos() + lengthB + 10);
        displayMessage("Traingle drawn.");
        if (getxPos() >= 900) {
            setxPos(100);
            setyPos(210);
        }

    }

    /*
     * Additional Pen Command
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
            displayMessage("Pen Colour Changed.");
        }

    }

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

    BufferedImage resize(BufferedImage image, int width, int height) {
        Image temp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = resized.createGraphics();
        graphics2D.drawImage(temp, 0, 0, null);
        graphics2D.dispose();
        return resized;
    }

    void saveImage() {
        int response;
        JFileChooser fileChooser = new JFileChooser();
        response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            try {
                ImageIO.write(getBufferedImage(), "png", fileChooser.getSelectedFile());
                displayMessage("Image Saved Successfully");
            } catch (IOException e) {
                displayMessage("Failed to Save Image");
            }
        } else {
            displayMessage("No file chosen");
        }
    }

    public void loadCommand() {
        int response;
        JFileChooser fileChooser = new JFileChooser();
        response = fileChooser.showOpenDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            loadCommandFromFile(fileChooser.getSelectedFile().getAbsolutePath());
        } else {
            displayMessage("No file chosen");
        }
    }

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


    public void saveCommand() {
        int response;
        JFileChooser fileChooser = new JFileChooser();
        response = fileChooser.showSaveDialog(null);
        if (response == JFileChooser.APPROVE_OPTION) {
            storeCommandToFile(commandToFile, fileChooser.getSelectedFile());
        }
        isCommandFileSaved = true;
    }

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

    public void warningClosing(Frame frame) {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (isCommandFileSaved) {
                    e.getWindow().dispose();
                } else {
                    int response = JOptionPane.showConfirmDialog(null, "Do you want to save the command before exiting ?", "Close", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        saveCommand();
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
