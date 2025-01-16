package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.ftc.Encoder;
import com.acmerobotics.roadrunner.ftc.OverflowEncoder;
import com.acmerobotics.roadrunner.ftc.RawEncoder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PwmControl;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoImplEx;

@TeleOp(name = "Testing", group = "Robot")

public class TestOp extends LinearOpMode
{

    private final double CLAW_EXTENDED = 0;
    private final double CLAW_RETRACTED = 0.26;
    private final double CLAW_ROT_MID = 0.54;
    private final double CLAW_ROT_GROUND = 0.19;
    private final double CLAW_ROT_FRONT = 0.25;
    private final double CLAW_GRAB = 0.83;
    private final double CLAW_RELEASE = 0.71;
    private final double CLAW_ROT_BACK = 0.80;
    DcMotor frontLeft, frontRight, backLeft, backRight, sliderLeft, sliderRight;
    ServoImplEx clawExtend, clawGrab, clawRightRot, clawLeftRot;
    Encoder leftSliderEncoder, rightSliderEncoder, perp, par0, par1;
    double leftRot, rightRot, extend = 0;

    @Override
    public void runOpMode() throws InterruptedException
    {


        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Expansion Hub 0
        frontLeft = hardwareMap.get(DcMotorEx.class, "leftFront");
        //Control Hub 0
        frontRight = hardwareMap.get(DcMotorEx.class, "rightRear");
        //Expansion Hub 1
        backLeft = hardwareMap.get(DcMotorEx.class, "leftRear");
        // Control Hub Port 1
        backRight = hardwareMap.get(DcMotorEx.class, "rightFront");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        rightSliderEncoder = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "rightSlider")));
        leftSliderEncoder = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "leftSlider")));


        par0 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "rightFront")));

        //Expansion hub 0, Left encoder
        par1 = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "leftFront")));
        par1.setDirection(DcMotorSimple.Direction.REVERSE);

        //Control Hub Port 1, Back encoder
        perp = new OverflowEncoder(new RawEncoder(hardwareMap.get(DcMotorEx.class, "rightRear")));
        // Expansion Hub 2
        sliderLeft = hardwareMap.get(DcMotorEx.class, "leftSlider");


        //Control Hub port 2
        sliderRight = hardwareMap.get(DcMotorEx.class, "rightSlider");

        sliderLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        sliderRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rightSliderEncoder.setDirection(DcMotorSimple.Direction.REVERSE);
        ;


//"clawExtend" is port 2 on control hub
        clawRightRot = hardwareMap.get(ServoImplEx.class, "clawRightRot");
        //"clawGrab" is port 3 on control hub
        clawGrab = hardwareMap.get(ServoImplEx.class, "clawGrab");
        //"clawRightRot" is port 0 on control hub
        clawExtend = hardwareMap.get(ServoImplEx.class, "clawExtend");

        // "clawLeftRot" is port 1 on control hub
        clawLeftRot = hardwareMap.get(ServoImplEx.class, "clawLeftRot");

        clawExtend.setPwmRange(new PwmControl.PwmRange(500, 2500));

        clawLeftRot.setPwmRange(new PwmControl.PwmRange(500, 2500));

        clawRightRot.setPwmRange(new PwmControl.PwmRange(500, 2500));

        clawGrab.setPwmRange(new PwmControl.PwmRange(500, 2500));

        clawLeftRot.setDirection(Servo.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
        telemetry.update();
        waitForStart();
        while (opModeIsActive())
        {
            if (gamepad1.y)
            {
                frontLeft.setPower(0.5);
            } else
            {
                frontLeft.setPower(0);
            }
            if (gamepad1.b)
            {
                frontRight.setPower(0.5);
            } else
            {
                frontRight.setPower(0);
            }
            if (gamepad1.x)
            {
                backLeft.setPower(0.5);
            } else
            {
                backLeft.setPower(0);
            }

            if (gamepad1.a)
            {
                backRight.setPower(0.5);
            } else
            {
                backRight.setPower(0);
            }


            telemetry.addData("par0", par0.getPositionAndVelocity().position);
            telemetry.addData("par1", par1.getPositionAndVelocity().position);
            telemetry.addData("perp", perp.getPositionAndVelocity().position);


            telemetry.update();


        }


    }

}
