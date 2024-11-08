package org.firstinspires.ftc.teamcode.griffinators;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Testing",group = "Robot")

public class TestOp extends LinearOpMode {

    DcMotor frontLeft, frontRight, backLeft, backRight, sliderLeft, sliderRight;


    @Override
    public void runOpMode() throws InterruptedException {


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Control Hub Port 1
        frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");

        //Expantion Hub 0
        frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");

        //Control Hub 0
        backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");

        //Expantion Hub 1
        backRight = hardwareMap.get(DcMotorEx.class,"rightRear");


        //Control Hub port 2
        sliderLeft=hardwareMap.get(DcMotorEx.class,"leftSlider");

        //     Expantion Hub 2
        sliderRight=hardwareMap.get(DcMotorEx.class,"rightSlider");

        sliderRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        sliderLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        sliderLeft.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();

        if (gamepad1.dpad_up){
            sliderRight.setPower(0.1);
            sliderLeft.setPower(0.1);
        }
        if (gamepad1.dpad_down){
            sliderRight.setPower(-0.1);
            sliderLeft.setPower(-0.1);
        }







    }

}
