package org.firstinspires.ftc.teamcode.parts;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config
public class Hanger  {
    public DcMotorEx rightHanger, leftHanger;
    public Encoder leftHangerEncoder,rightHangerEncoder;
    public static int INIT_HANGER=0;
    public static int HANG=2400;




    public Hanger(HardwareMap hardwareMap){
        leftHanger =hardwareMap.get(DcMotorEx.class,"leftHanger");
        rightHanger=hardwareMap.get(DcMotorEx.class,"rightHanger");


        leftHanger.setDirection(DcMotorSimple.Direction.REVERSE);


        leftHanger.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightHanger.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightHanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftHanger.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }
    public Hanger getHangerEncoder(HardwareMap hardwareMap){
        rightHangerEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"rightHanger")));
        leftHangerEncoder=new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class,"leftHanger")));
        leftHangerEncoder.setDirection(DcMotorSimple.Direction.REVERSE);


        return this;
    }
public void initHanger(){
    leftHanger.setTargetPosition(INIT_HANGER);
    rightHanger.setTargetPosition(INIT_HANGER);

    leftHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rightHanger.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    leftHanger.setPower(0.1);
    rightHanger.setPower(0.1);

}
public void hang(){
    leftHanger.setTargetPosition(HANG);
    rightHanger.setTargetPosition(HANG);
    leftHanger.setPower(1);
    rightHanger.setPower(1);
}
public void resetHanger(){
    leftHanger.setTargetPosition(INIT_HANGER);
    rightHanger.setTargetPosition(INIT_HANGER);
    leftHanger.setPower(1);
    rightHanger.setPower(1);
}

}
