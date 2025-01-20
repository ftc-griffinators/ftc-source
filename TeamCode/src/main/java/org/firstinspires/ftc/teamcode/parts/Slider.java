package org.firstinspires.ftc.teamcode.parts;

import static org.firstinspires.ftc.teamcode.parts.Utility.sliderSmoothMovement;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slider {
    public static double startingSlider=50;
    public static double sliderTop=4200;
    public static double sliderMid=2100;

    DcMotorEx sliderLeft,sliderRight;


    public Slider( HardwareMap hardwareMap)  {
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");
        //Control Hub port 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");



        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sliderRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        sliderLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    public void sliderInit(){
        sliderLeft.setTargetPosition(50);
        sliderRight.setTargetPosition(50);

        sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    private void smoothing(int initial){
        sliderRight.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderRight.getCurrentPosition())));
        sliderLeft.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderLeft.getCurrentPosition())));
    }

    public void sliderExtensionTopBox(){
        sliderRight.setTargetPosition(4200);
        sliderLeft.setTargetPosition(4200);
        sliderRight.setPower(1);
        sliderLeft.setPower(1);
    }

    public void sliderRetractionBox(){
        sliderRight.setTargetPosition(50);
        sliderLeft.setTargetPosition(50);
        smoothing(sliderLeft.getCurrentPosition());
    }
    public void sliderExtensionMidBox(){
        sliderRight.setTargetPosition(2100);
        sliderLeft.setTargetPosition(2100);
        sliderRight.setPower(1);
        sliderLeft.setPower(1);
    }


}
