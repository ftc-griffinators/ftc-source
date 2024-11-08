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

		// Control Hub Port 1
		frontLeft =  hardwareMap.get(DcMotorEx.class,"leftFront");

		//Expantion Hub 0
		frontRight =hardwareMap.get(DcMotorEx.class,"rightFront");

		//Control Hub 0
		backLeft = hardwareMap.get(DcMotorEx.class,"leftRear");

		//Expantion Hub 1
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





		///////		CLAW	///////

			//telemetry.addData("extension position",clawExtension.getPosition());
			telemetry.addData("None usb test","works");
			telemetry.update();

			//Around 0.71 is the fully retracted state
			//Around 0.27 is the fully extended state

			if (gamepad1.a){
				//telemetry.addData("Servo added, current position",clawExtension.getPosition());
				i=i+0.01;
				telemetry.update();
			}
			if (gamepad1.b){
				//telemetry.addData("Servo subtracted, current position",clawExtension.getPosition());
				i=i-0.01;
				telemetry.update();
			}








/*		double openPos = 0.12;
		double closedPos = 0.32;
		// double closedPos = 0.36; // too tight
		double leftMax = 0.89;
		
		if (gamepad2.left_bumper) {
			if (LclawChangeable) {
				LclawChangeable = false;
				Lopen = !Lopen;
			}
		} else {
			LclawChangeable = true;
		}
		if (gamepad2.right_bumper) {
			if (RclawChangeable) {
				RclawChangeable = false;
				Ropen = !Ropen;
			}
		} else {
			RclawChangeable = true;
		}
		
		if (Ropen) {
			clawRight.setPosition(closedPos);
		} else {
			clawRight.setPosition(openPos);
		}
		if (Lopen) {
			clawLeft.setPosition(leftMax - closedPos);
		} else {
			clawLeft.setPosition(leftMax - openPos);
		}
		
		if (Aground) {	
			if (gamepad2.a){
				if(CgroundChangeable){
					Cground = !Cground;
					CgroundChangeable = false;
				}
			} else {
				CgroundChangeable = true;
			}
	   
			if (Cground) {
				clawControl.setPosition(0.472);
			} else {
				clawControl.setPosition(0.472 + 0.04);
			}	
		} else if (gamepad2.a) {
			clawControl.setPosition(0.465);
		}





		///////		ARM		///////
		
		armControlLeft.setPower(basicPower);
		armControlRight.setPower(basicPower);
		armExtendLeft.setPower(basicPower);
		armExtendRight.setPower(basicPower);
		
		if (gamepad2.dpad_left) {
			time2.reset();
			setExtension(0);
			sleep(200);
			setRotation(50);
			sleep(200);
			setRotation(0);
			Aground = true;
			Cground = false;
		} else if (gamepad2.dpad_up) {
			setRotation(170);
			setExtension(200);
			clawControl.setPosition(0.54);
			Aground = false;
			Cground = false;
		} else if (gamepad2.dpad_right) {
			setRotation(230);
			setExtension(400);
			clawControl.setPosition(0.53);
			Aground = false;
			Cground = false;
		} else if (gamepad2.dpad_down) {
			setRotation(280);
			setExtension(500);
			clawControl.setPosition(0.52);
			Aground = false;
			Cground = false;
		} else if (gamepad2.x) {
		 	setRotation(320);
		 	setExtension(600);
		 	clawControl.setPosition(0.52);
		 	Aground = false;
		 	Cground = false;
		} else if (gamepad2.y) {
			setRotation(360);
			setExtension(700);
			clawControl.setPosition(0.52);
			Aground = false;
			Cground = false;
		} else if (gamepad2.b) {
			setRotation(390);
			setExtension(800);
			clawControl.setPosition(0.52);
			Aground = false;
			Cground = false;
		} else if (gamepad1.b) {
			setRotation(800);
			setExtension(400);
			Aground = false;
			Cground = false;
		} else if (gamepad1.y) {
			setExtension(0);
			setRotation(400);
			Aground = false;
			Cground = false;
		}



		
		
		///////		LAUNCH		///////

		if (gamepad1.x) {
		launchAngle.setPosition(40/180f);
		sleep(1000);
		launch.setPosition(0.5f);
		sleep(1000);
		launch.setPosition(-0.5f);
		sleep(500);
		launchAngle.setPosition(60/180f);
		}

*/



		///////		DATA		///////
/*
		telemetry.addData("RunTime", "time:" + runtime);
		//telemetry.addData("Booleans", "ground: " + ground);	 
		telemetry.addData("Position", "Extension: " + armExtendLeft.getCurrentPosition() + " " + armExtendRight.getCurrentPosition()
		+ "; Rotation: " + armControlLeft.getCurrentPosition() + " " + armControlRight.getCurrentPosition() + "; rot: " + " " + pose.heading.log());
		telemetry.addData("Gamepad 1", "gamepad1_l_x: " + gamepad1.left_stick_x + "; gamepad1_l_y: " + gamepad1.left_stick_y + "gamepad1_r_x" + gamepad1.right_stick_x + "gamepad1_Y: " + gamepad1.y);
		telemetry.addData("Gamepad 2", "gamepad2_l_x: " + gamepad2.left_stick_x + "; gamepad2_l_y: " + gamepad2.left_stick_y + "gamepad2_r_x" + gamepad2.right_stick_x + "gamepad2_Y: " + gamepad2.y);
		telemetry.update();

		*/
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