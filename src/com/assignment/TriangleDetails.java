package com.assignment;

public class TriangleDetails {
    private final int [] xPoints;
    private final int [] yPoints;
    private boolean isFilled;

    TriangleDetails(int [] xPoints, int [] yPoints, boolean isFilled){
        this.xPoints = xPoints;
        this.yPoints = yPoints;
        this.isFilled = isFilled;
    }
    public int [] getxPoints(){return xPoints;}
    public int [] getyPoints(){return yPoints;};
    public boolean isCircleFilled(){
        return isFilled;
    }
    public void changeFilling(){
        isFilled = !isFilled;
    }
}
