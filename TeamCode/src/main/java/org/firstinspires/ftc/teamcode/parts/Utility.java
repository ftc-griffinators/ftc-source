package org.firstinspires.ftc.teamcode.parts;

public class Utility
{


    //scales a range to be between 0 to 1
    public static double sliderSmoothMovement(int lowerBound, int upperBound, int current)
    {
        double x = current / (upperBound - lowerBound);
        // return  -0.2*Math.pow(x,2)-0.2*x+1;

        return -(1 / (-63 + Math.pow(Math.E, -x + 5.2))) + 1;

    }
}
