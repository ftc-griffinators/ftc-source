package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Localizer;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.parts.PIDController;
import org.firstinspires.ftc.teamcode.parts.enums.RobotState;

@TeleOp(name = "TeleOperation", group = "Robot")
public class TeleOperation extends LinearOpMode
{
    private static final double MOVEMENT_DEADZONE = 0.1;
    private static final double CURVE_POWER = 3;
    private static final double MAX_THRESHOLD = 0.9;
    private static final double SLOW_MODE_FACTOR = 0.5;
    private static final double CLAW_OPEN_POS = 0.12;
    private static final double CLAW_CLOSED_POS = 0.32;
    private static final double LAUNCH_ANGLE_LOW = 40 / 180f;
    private static final double LAUNCH_ANGLE_HIGH = 60 / 180f;
    private static final int ARM_EXTENSION_UP = 200;
    private static final int ARM_ROTATION_UP = 170;
    private static final int ARM_EXTENSION_DOWN = 100;
    private static final int ARM_ROTATION_DOWN = 90;
    private static final double DEBOUNCE_THRESHOLD = 0.2;
    private final ElapsedTime debounceTimer = new ElapsedTime();
    private final ElapsedTime launchTimer = new ElapsedTime();
    private DcMotor frontLeft, frontRight, backLeft, backRight;
    private DcMotor armExtendLeft, armExtendRight, armControlLeft, armControlRight;
    private Servo clawLeft, clawRight, launch, launchAngle;
    private PIDController drivePID;
    private PIDController armPID;
    private Localizer localizer;
    private Pose2d robotPose = new Pose2d(0, 0, 0);
    private RobotState currentState = RobotState.IDLE;
    private boolean isLaunching = false;

    @Override
    public void runOpMode()
    {
        initializeHardware();
        drivePID = new PIDController(0.1, 0.01, 0.05, 0.1, 50);
        armPID = new PIDController(0.2, 0.01, 0.05, 0.1, 50);
        localizer = new ThreeDeadWheelLocalizer(hardwareMap, MecanumDrive.PARAMS.inPerTick);

        waitForStart();

        while (opModeIsActive())
        {
            updateRobotPose();

            switch (currentState)
            {
                case IDLE:
                    handleIdle();
                    break;
                case DRIVING:
                    handleDriving(false);
                    break;
                case SLIDING:
                    handleDriving(true);
                    break;
                case ARM_CONTROL:
                    handleArmControl();
                    break;
                case CLAW_CONTROL:
                    handleClawControl();
                    break;
                case LAUNCHING:
                    handleLaunching();
                    break;
            }

            updateTelemetry();
        }
    }

    private void handleIdle()
    {
        stopMotors();

        if (Math.abs(gamepad1.left_stick_x) > MOVEMENT_DEADZONE ||
                Math.abs(gamepad1.left_stick_y) > MOVEMENT_DEADZONE)
        {
            currentState = gamepad1.right_trigger > 0.5 ?
                    RobotState.SLIDING : RobotState.DRIVING;
        } else if (gamepad2.left_bumper || gamepad2.right_bumper)
        {
            currentState = RobotState.CLAW_CONTROL;
        } else if (gamepad2.dpad_up || gamepad2.dpad_down)
        {
            currentState = RobotState.ARM_CONTROL;
        } else if (gamepad1.x && debounceTimer.seconds() > DEBOUNCE_THRESHOLD)
        {
            debounceTimer.reset();
            currentState = RobotState.LAUNCHING;
        }
    }

    private void handleDriving(boolean isSliding)
    {
        double x = responseCurve(gamepad1.left_stick_x);
        double y = -responseCurve(gamepad1.left_stick_y);

        if (Math.abs(x) < MOVEMENT_DEADZONE && Math.abs(y) < MOVEMENT_DEADZONE)
        {
            currentState = RobotState.IDLE;
            return;
        }

        double theta = Math.atan2(y, x);
        double power = Math.hypot(x, y);
        double turn = responseCurve(gamepad1.right_stick_x);

        double headingCompensation = isSliding ? 0 : robotPose.heading.log();
        double sin = Math.sin(theta - Math.PI / 4 - headingCompensation);
        double cos = Math.cos(theta - Math.PI / 4 - headingCompensation);
        double max = Math.max(Math.abs(sin), Math.abs(cos));

        double[] powers = {
                power * cos / max + turn,  // Front Left
                power * sin / max - turn,  // Front Right
                power * sin / max + turn,  // Back Left
                power * cos / max - turn   // Back Right
        };

        // Apply slow mode if active
        if (gamepad1.left_trigger > 0.5)
        {
            for (int i = 0; i < powers.length; i++)
            {
                powers[i] *= SLOW_MODE_FACTOR;
            }
        }

        // Apply PID if not sliding
        if (!isSliding)
        {
            powers[0] = drivePID.calculate(powers[0], frontLeft.getPower());
            powers[1] = drivePID.calculate(powers[1], frontRight.getPower());
            powers[2] = drivePID.calculate(powers[2], backLeft.getPower());
            powers[3] = drivePID.calculate(powers[3], backRight.getPower());
        }

        frontLeft.setPower(powers[0]);
        frontRight.setPower(powers[1]);
        backLeft.setPower(powers[2]);
        backRight.setPower(powers[3]);
    }

    private void handleArmControl()
    {
        if (gamepad2.dpad_up)
        {
            armPIDControl(ARM_EXTENSION_UP, ARM_ROTATION_UP);
        } else if (gamepad2.dpad_down)
        {
            armPIDControl(ARM_EXTENSION_DOWN, ARM_ROTATION_DOWN);
        }
        currentState = RobotState.IDLE;
    }

    private void armPIDControl(int targetExtensionPosition, int targetRotationPosition)
    {
        double currentExtensionPosition = armExtendLeft.getCurrentPosition();
        double currentRotationPosition = armControlLeft.getCurrentPosition();

        double adjustedExtensionPower =
                armPID.calculate(targetExtensionPosition, currentExtensionPosition);
        armExtendLeft.setPower(adjustedExtensionPower);
        armExtendRight.setPower(adjustedExtensionPower);

        double adjustedRotationPower =
                armPID.calculate(targetRotationPosition, currentRotationPosition);
        armControlLeft.setPower(adjustedRotationPower);
        armControlRight.setPower(adjustedRotationPower);
    }

    private void handleClawControl()
    {
        if (gamepad2.left_bumper && debounceTimer.seconds() > DEBOUNCE_THRESHOLD)
        {
            debounceTimer.reset();
            toggleClaw(clawLeft);
        }
        if (gamepad2.right_bumper && debounceTimer.seconds() > DEBOUNCE_THRESHOLD)
        {
            debounceTimer.reset();
            toggleClaw(clawRight);
        }
        currentState = RobotState.IDLE;
    }

    private void toggleClaw(Servo claw)
    {
        claw.setPosition(claw.getPosition() == CLAW_CLOSED_POS ? CLAW_OPEN_POS : CLAW_CLOSED_POS);
    }

    private void handleLaunching()
    {
        if (!isLaunching)
        {
            launchAngle.setPosition(LAUNCH_ANGLE_LOW);
            launchTimer.reset();
            isLaunching = true;
        } else
        {
            if (launchTimer.seconds() >= 1.0 && launchTimer.seconds() < 2.0)
            {
                launch.setPosition(0.5f);
            } else if (launchTimer.seconds() >= 2.0 && launchTimer.seconds() < 2.5)
            {
                launch.setPosition(-0.5f);
            } else if (launchTimer.seconds() >= 2.5)
            {
                launchAngle.setPosition(LAUNCH_ANGLE_HIGH);
                isLaunching = false;
                currentState = RobotState.IDLE;
            }
        }
    }

    private double responseCurve(double input)
    {
        return Math.copySign(
                Math.min(Math.pow(Math.abs(input) / MAX_THRESHOLD, CURVE_POWER), 1.0),
                input
        );
    }

    private void updateRobotPose()
    {
        robotPose = robotPose.plus(localizer.update().value());
    }

    private void stopMotors()
    {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
    }

    private void initializeHardware()
    {
        frontLeft = hardwareMap.get(DcMotor.class, "leftFront");
        frontRight = hardwareMap.get(DcMotor.class, "rightFront");
        backLeft = hardwareMap.get(DcMotor.class, "leftBack");
        backRight = hardwareMap.get(DcMotor.class, "rightBack");

        armExtendLeft = hardwareMap.get(DcMotor.class, "acl");
        armExtendRight = hardwareMap.get(DcMotor.class, "acr");
        armControlLeft = hardwareMap.get(DcMotor.class, "ael");
        armControlRight = hardwareMap.get(DcMotor.class, "aer");

        clawLeft = hardwareMap.servo.get("cl");
        clawRight = hardwareMap.servo.get("cr");
        launch = hardwareMap.servo.get("l");
        launchAngle = hardwareMap.servo.get("lc");

        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        armControlLeft.setDirection(DcMotor.Direction.REVERSE);
        armExtendLeft.setDirection(DcMotor.Direction.REVERSE);

        // Set zero power behavior
        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    private void updateTelemetry()
    {
        telemetry.addData("State", currentState);
        telemetry.addData("Pose", "X: %.2f, Y: %.2f, θ: %.2f°",
                robotPose.position.x,
                robotPose.position.y,
                Math.toDegrees(robotPose.heading.log()));
        telemetry.addData("Arm Extension", "L: %d, R: %d",
                armExtendLeft.getCurrentPosition(),
                armExtendRight.getCurrentPosition());
        telemetry.addData("Arm Rotation", "L: %d, R: %d",
                armControlLeft.getCurrentPosition(),
                armControlRight.getCurrentPosition());
        telemetry.addData("Claw", "L: %.2f, R: %.2f",
                clawLeft.getPosition(),
                clawRight.getPosition());
        telemetry.update();
    }
}