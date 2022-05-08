package com.assignment.shapesdetails;

import java.awt.*;

public class TriangleDetails {
    private final int [] xPoints;
    private final int [] yPoints;
    private final int lengthA;
    private final int lengthB;
    private final int lengthC;
    private boolean isFilled;
    Color color;

    public TriangleDetails(int[] xPoints, int[] yPoints,int lengthA, int lengthB, int lengthC, boolean isFilled, Color color){
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.lengthA = lengthA;
        this.lengthB = lengthB;
        this.lengthC = lengthC;
        this.isFilled = isFilled;
        this.color = color;
    }
    public int [] getxPoints(){return xPoints;}
    public int [] getyPoints(){return yPoints;}
    public int getLengthA() {return lengthA;}
    public int getLengthB(){return lengthB;}
    public int getLengthC(){return lengthC;}
    public Color getShapeColor(){return color;}
    public boolean isTriangleFilled(){
        return isFilled;
    }
    public void changeFilling(){
        isFilled = !isFilled;
    }
}
