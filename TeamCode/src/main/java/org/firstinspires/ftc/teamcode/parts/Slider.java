package org.firstinspires.ftc.teamcode.parts;



import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Slider {
    public static int INIT_SLIDER =50;
    public static int SLIDER_TOP =3100;
    public static int SLIDER_MID =200;
    public static int SLIDER_TOP_BAR;
    public static int SLIDER_HANGER;
    public static int RESET_CORRECTION=0;

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
        sliderLeft.setTargetPosition(INIT_SLIDER);
        sliderRight.setTargetPosition(INIT_SLIDER);

        sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);


    }
    public static double sliderSmoothMovement(int lowerBound, int upperBound,int current){
        double x= current/(upperBound-lowerBound);

        return -(1/(-63+Math.pow(Math.E,-x+5.2)))+1;

    }


    private void smoothing(int initial){
        sliderRight.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderRight.getCurrentPosition())));
        sliderLeft.setPower(sliderSmoothMovement(0,4000,Math.abs(initial-sliderLeft.getCurrentPosition())));
    }
    public void  sliderReset(){
        sliderRight.setTargetPosition(RESET_CORRECTION);
        sliderRight.setTargetPosition(RESET_CORRECTION);
        sliderRight.setPower(0.2);
        sliderLeft.setPower(0.2);

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        sliderInit();



    }
    public void sliderExtensionTopBar(){
        sliderRight.setTargetPosition(SLIDER_TOP_BAR);
        sliderRight.setTargetPosition(SLIDER_TOP_BAR);
        sliderRight.setPower(1);
        sliderLeft.setPower(1);
    }

    public void sliderExtensionTopBox(){
        sliderRight.setTargetPosition(SLIDER_TOP);
        sliderLeft.setTargetPosition(SLIDER_TOP);
        sliderRight.setPower(1);
        sliderLeft.setPower(1);
    }
    public void sliderRetraction(){
        sliderRight.setTargetPosition(INIT_SLIDER);
        sliderLeft.setTargetPosition(INIT_SLIDER);
        smoothing(sliderLeft.getCurrentPosition());

    }
    public void sliderExtensionHanger(){
        sliderRight.setTargetPosition(SLIDER_HANGER);
        sliderLeft.setTargetPosition(SLIDER_HANGER);
        sliderRight.setPower(1);
        sliderLeft.setPower(1);
    }
    public void sliderExtensionMidBox(){
        sliderRight.setTargetPosition(SLIDER_MID);
        sliderLeft.setTargetPosition(SLIDER_MID);
        sliderRight.setPower(1);
        sliderLeft.setPower(1);
    }


}
