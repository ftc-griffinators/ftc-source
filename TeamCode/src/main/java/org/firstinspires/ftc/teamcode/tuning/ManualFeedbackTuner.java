package org.firstinspires.ftc.teamcode.tuning;

import androidx.annotation.NonNull;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.systems.MecanumDrive;
import org.firstinspires.ftc.teamcode.systems.TankDrive;
import org.firstinspires.ftc.teamcode.systems.ThreeDeadWheelLocalizer;
import org.firstinspires.ftc.teamcode.systems.TwoDeadWheelLocalizer;

public final class ManualFeedbackTuner extends LinearOpMode
{
    public static double DISTANCE = 64;

    @Override
    public void runOpMode() throws InterruptedException
    {
        if (TuningOpModes.DRIVE_CLASS.equals(MecanumDrive.class))
        {
            MecanumDrive drive = getMecanumDrive();
            waitForStart();

            while (opModeIsActive())
            {
                for (int i = 0; i < 3; i++)
                {
                    Actions.runBlocking(
                            drive.actionBuilder(new Pose2d(0, 0, 0))
                                    .lineToX(DISTANCE)
                                    .lineToX(0)
                                    .build());
                }
                sleep(3000);
            }
        }
        else if (TuningOpModes.DRIVE_CLASS.equals(TankDrive.class))
        {
            TankDrive drive = getTankDrive();
            waitForStart();

            while (opModeIsActive())
            {
                Actions.runBlocking(
                        drive.actionBuilder(new Pose2d(0, 0, 0))
                                .lineToX(DISTANCE)
                                .lineToX(0)
                                .build());
            }
        }
        else
        {
            throw new RuntimeException();
        }
    }

    private @NonNull TankDrive getTankDrive()
    {
        TankDrive drive = new TankDrive(hardwareMap, new Pose2d(0, 0, 0));

        if (drive.localizer instanceof TwoDeadWheelLocalizer)
        {
            if (TwoDeadWheelLocalizer.PARAMS.perpXTicks == 0
                && TwoDeadWheelLocalizer.PARAMS.parYTicks == 0)
            {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        }
        else if (drive.localizer instanceof ThreeDeadWheelLocalizer)
        {
            if (ThreeDeadWheelLocalizer.PARAMS.perpXTicks == 0
                && ThreeDeadWheelLocalizer.PARAMS.par0YTicks == 0
                && ThreeDeadWheelLocalizer.PARAMS.par1YTicks == 1)
            {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        }
        return drive;
    }

    private @NonNull MecanumDrive getMecanumDrive()
    {
        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(0, 0, 0));

        if (drive.localizer instanceof TwoDeadWheelLocalizer)
        {
            if (TwoDeadWheelLocalizer.PARAMS.perpXTicks == 0
                && TwoDeadWheelLocalizer.PARAMS.parYTicks == 0)
            {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        }
        else if (drive.localizer instanceof ThreeDeadWheelLocalizer)
        {
            if (ThreeDeadWheelLocalizer.PARAMS.perpXTicks == 0
                && ThreeDeadWheelLocalizer.PARAMS.par0YTicks == 0
                && ThreeDeadWheelLocalizer.PARAMS.par1YTicks == 1)
            {
                throw new RuntimeException("Odometry wheel locations not set! Run AngularRampLogger to tune them.");
            }
        }
        return drive;
    }
}
