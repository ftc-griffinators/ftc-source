package org.firstinspires.ftc.teamcode.griffinators.Parts;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;


public class PIDController {

    @Config
    public static class PIDKValues{
        public static double TunableKP =0;
        public static double TunableKI=0;
        public static  double TunableKD =0;
    }


    // kp: Proportional
    // ki: Integral
    // kd: Derivative
    private final double kp;
    private final double ki;
    private final double kd;

    private final double tolerance;
    private final double IntegralSumLimit=0.25;
    private  double lastReference=0;
    private double previousError = 0;
    private double integralSum = 0;

    /*
    \\ Low pass filter implementation
    private double currentFilterEstimate = 0;
    private double previousFilterEstimate = 0;
    private double filterConstant=0.4;

*/



    public PIDController(double kp, double ki, double kd, double tolerance)
    {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.tolerance = tolerance;

    }

    public double calculate(double target, double current) {


        double error = target - current;
        if (Math.abs(error) < tolerance)
        {
            return 0;
        }

        double proportional = kp * error;

        integralSum += error;
        double min =-IntegralSumLimit;
        integralSum = Math.max(min, Math.min(integralSum, IntegralSumLimit));
        double integralTerm = ki * integralSum;

        double derivative = kd * (error - previousError);
        previousError = error;

        return proportional + integralTerm + derivative;
    }




    public  double testCorrection(double reference, double current){

        if (Math.abs(reference-lastReference)>tolerance){
            reset();
        }

        if (reference==0.0){
            return 0;
        }
        lastReference=reference;

        double error = reference - current;
        ElapsedTime time = new ElapsedTime();
        time.startTime();

        double proportional = kp * error;

        integralSum =integralSum+ error * time.seconds();

        double min = -IntegralSumLimit;
        integralSum = Math.max(min, Math.min(integralSum, IntegralSumLimit));

        double derivative = kd * (error - previousError)/time.seconds();

        double output= proportional + ki*integralSum +derivative;


        time.reset();

        previousError=error;


        return output;
}

    public void reset()
    {
        integralSum = 0;
        previousError = 0;

    }

}
