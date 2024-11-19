package org.firstinspires.ftc.teamcode.griffinators.Parts;

public class Utility {



    //scales a range to be between 0 to 1
    public static double sliderSmoothMovement(int lowerBound, int upperBound,int current){
        double x= current/(upperBound-lowerBound);
        return  0.4*Math.pow(x,2)-x+1;

    }
}
