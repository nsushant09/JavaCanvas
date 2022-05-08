package com.assignment.shapesdetails;


import java.awt.*;

/*
* Used as Data Class to store Circle Details to use when it's filling is to be changed.
* */
public class CircleDetails {
    private final int circleXpos;
    private final int circleYpos;
    private final int width;
    private final int height;
    private boolean isFilled;
    Color color;

    public CircleDetails(int xpos, int ypos, int radius, boolean isFilled, Color color){
        this.circleXpos = xpos;
        this.circleYpos = ypos;
        this.width = radius * 2;
        this.height = radius * 2;
        this.isFilled = isFilled;
        this.color = color;
    }
    public int getCircleXpos(){
        return circleXpos;
    }
    public int getCircleYpos(){
        return circleYpos;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public Color getShapeColor(){return color;}
    public boolean isCircleFilled(){
        return isFilled;
    }
    public void changeFilling(){
        isFilled = !isFilled;
    }
}
