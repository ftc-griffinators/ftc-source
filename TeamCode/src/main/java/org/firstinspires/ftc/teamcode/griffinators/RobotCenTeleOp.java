package org.firstinspires.ftc.teamcode.griffinators;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;


public class RobotCenTeleOp extends LinearOpMode {

    private DcMotor frontLeft, frontRight, backLeft, backRight, armExtendLeft, armExtendRight, armControlLeft, armControlRight;

    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        frontLeft = hardwareMap.dcMotor.get("leftFront");
        frontRight = hardwareMap.dcMotor.get("rightFront");
        backLeft = hardwareMap.dcMotor.get("leftBack");
        backRight = hardwareMap.dcMotor.get("rightBack");
        Localizer localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);


        waitForStart();
        telemetry.addData("Status", "Begin");
        telemetry.update();

        while (opModeIsActive()) {
            double movementY = -gamepad1.left_stick_y;
            double movementX = gamepad1.left_stick_x;
            double movementR = gamepad1.right_stick_x;

            double clawRotate = gamepad2.right_stick_x;

            double fLPower = movementY + movementX + movementR;
            double bLPower = movementY - movementX + movementR;
            double fRPower = movementY - movementX - movementR;
            double bRPower = movementY + movementX - movementR;

            double max = Math.max(Math.abs(fLPower), Math.abs(bLPower));
            max = Math.max(Math.abs(max), Math.abs(fRPower));
            max = Math.max(Math.abs(max), Math.abs(bRPower));

            if (max > 1.0) {
                fLPower /= max;
                bLPower /= max;
                fRPower /= max;
                bRPower /= max;
            }


            frontLeft.setPower(fLPower);
            backLeft.setPower(bLPower);
            frontRight.setPower(fRPower);
            backRight.setPower(bRPower);


        }
    }
}
