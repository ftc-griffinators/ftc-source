package org.firstinspires.ftc.teamcode.griffinators;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;

@TeleOp(name="TeleOperation", group="Robot")

public class TeleOperation extends LinearOpMode {
	
	private final ElapsedTime runtime = new ElapsedTime();
	DcMotor frontLeft, frontRight, backLeft, backRight, armExtendLeft, armExtendRight, armControlLeft, armControlRight;
	Servo clawExtension, clawGrab, clawRightRot, clawLeftRot;
	double i=0.4;

	private Pose2d pose = new Pose2d(0,0,0);
	@Override
	public void runOpMode() throws InterruptedException{
		
		telemetry.addData("Status", "Initialized");
		telemetry.update();


		//Expansion Hub 0
		frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");



		//Control Hub 0
		frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");



		//Expansion Hub 1
		backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");

		// Control Hub Port 1
		backRight = hardwareMap.get(DcMotorEx.class,"rightRear");








//		//"ce" is port 5 on control hub
//		clawExtension = hardwareMap.servo.get("ce");
//		//"c" is port 4
//		clawGrab = hardwareMap.servo.get("c");
//		//"cr" ia port 2
//		clawRightRot = hardwareMap.servo.get("cr");
//		// "cl" is port 3
//		clawLeftRot = hardwareMap.servo.get("cl");
//

		backLeft.setDirection(DcMotor.Direction.REVERSE);
		frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);

		waitForStart();
		runtime.reset();
		runtime.startTime();

		Localizer localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);


		while (opModeIsActive()) {


		////////	MOVEMENT	///////		

		pose = pose.plus(localizer.update().value());
		if (gamepad1.right_bumper) {
			pose = new Pose2d(0,0,-Math.PI/2);
		}
		if (gamepad1.left_bumper) {
			pose = new Pose2d(0,0,Math.PI/2);
		}

		double x = responseCurve(gamepad1.left_stick_x,	3,0.9);
		double y = responseCurve(-gamepad1.left_stick_y, 3, 0.9);
//		double turn = responseCurve(gamepad1.right_stick_x, 5, 0.95);
//		if (Math.hypot(-gamepad1.left_stick_y,-gamepad1.left_stick_x) >= 0.95) {
//			x = -gamepad1.left_stick_y - gamepad2.left_stick_y / 5;
//			y = -gamepad1.left_stick_x - gamepad2.left_stick_x / 5;
//		} else {
//			x = -gamepad1.left_stick_y/2 - gamepad2.left_stick_y / 5;
//			y = -gamepad1.left_stick_x/2 - gamepad2.left_stick_x / 5;
//		}
		double theta = Math.atan2(y, x);
		double power = Math.hypot(x, y);
		double turn;
		if (Math.abs(gamepad1.right_stick_x) >= 0.95) {
			turn = gamepad1.right_stick_x/2;
		} else {
			turn = gamepad1.right_stick_x/4 ;
		}
		double sin = Math.sin(theta - Math.PI/4 - pose.heading.log());
		double cos = Math.cos(theta - Math.PI/4 - pose.heading.log());
		double max = Math.max(Math.abs(sin), Math.abs(cos));

		/*
		if (gamepad1.dpad_left) {
			time1.reset();
		} else if (gamepad1.dpad_right) {
			time1.reset();
		}

		if (time1.seconds() < 0.6 && runtime.seconds() > 2) {
			turn = 1;
		}
*/
		double FLpower = power * cos/max + turn;
		double FRpower = power * sin/max - turn;
		double BLpower = power * sin/max + turn;
		double BRpower = power * cos/max - turn;

		if((power + Math.abs(turn)) > 1) {
			FLpower /= power + turn;
			FRpower /= power + turn;
			BLpower /= power + turn;
			BRpower /= power + turn;
		}

		frontLeft.setPower(FLpower);
		frontRight.setPower(FRpower);
		backLeft.setPower(BLpower);
		backRight.setPower(BRpower);








		}
	}
	
	
	
	
/*
	
	private void setExtension(int amount){
		armExtendLeft.setTargetPosition(amount);
		armExtendRight.setTargetPosition(amount);
	}
	
	private void setRotation(int amount){
		armControlLeft.setTargetPosition(amount);
		armControlRight.setTargetPosition(amount);
	}
*/
	private double responseCurve (double raw, int curvePower, double maxThreshold) {
		return Math.copySign(Math.min(Math.pow(Math.abs(raw)/maxThreshold, curvePower), 1), raw);
	}
}