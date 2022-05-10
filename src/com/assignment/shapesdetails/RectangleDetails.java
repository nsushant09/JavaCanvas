package com.assignment.shapesdetails;

import java.awt.*;

/*
 * Used as Data Class to store Circle Details to use when it's filling is to be changed.
 * */
public class RectangleDetails {
    private final int rectXpos;
    private final int rectYpos;
    private final int length;
    private final int breadth;
    private boolean isFilled;
    Color color;

    public RectangleDetails(int xpos, int ypos, int length, int breadth, boolean isFilled, Color color) {
        this.rectXpos = xpos;
        this.rectYpos = ypos;
        this.length = length;
        this.breadth = breadth;
        this.isFilled = isFilled;
        this.color = color;
    }

    public int getRectXpos() {
        return rectXpos;
    }

    public int getRectYpos() {
        return rectYpos;
    }

    public int getLength() {
        return length;
    }

    public int getBreadth() {
        return breadth;
    }

    public Color getShapeColor() {
        return color;
    }

    public boolean isRectangleFilled() {
        return isFilled;
    }

    public void changeFilling() {
        isFilled = !isFilled;
    }
}

