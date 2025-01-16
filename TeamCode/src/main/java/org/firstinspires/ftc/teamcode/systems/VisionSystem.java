package org.firstinspires.ftc.teamcode.systems;

import android.annotation.SuppressLint;
import android.util.Size;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.RotatedRect;

import java.util.List;

public class VisionSystem
{
    private static final double CLAW_OPEN = 0.7;
    private static final double CLAW_CLOSED = 0.2;
    private static final int MIN_BLOB_AREA = 50;
    private static final int MAX_BLOB_AREA = 20000;
    private static final double CENTER_THRESHOLD = 30; // pixels

    private final HardwareMap hardwareMap;
    private final String cameraName;
    private final Servo clawServo;
    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor colorLocator;
    private ColorRange currentColor = ColorRange.BLUE;
    private ImageRegion currentROI = ImageRegion.entireFrame();

    public VisionSystem(HardwareMap hardwareMap, String cameraName, String servoName)
    {
        this.hardwareMap = hardwareMap;
        this.cameraName = cameraName;
        this.clawServo = hardwareMap.get(Servo.class, servoName);
        initVision();
    }

    private void initVision()
    {
        if (visionPortal != null)
        {
            visionPortal.close();
        }

        colorLocator = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(currentColor)
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setRoi(currentROI)
                .setDrawContours(true)
                .setBlurSize(5)
                .build();

        visionPortal = new VisionPortal.Builder()
                .addProcessor(colorLocator)
                .setCameraResolution(new Size(320, 240))
                .setCamera(hardwareMap.get(WebcamName.class, cameraName))
                .build();
    }

    public void setTargetColor(ColorRange color)
    {
        this.currentColor = color;
        initVision();
    }

    public void setROI(double centerX, double centerY, double width, double height)
    {
        this.currentROI = ImageRegion.asUnityCenterCoordinates(
                centerX - width / 2,
                centerY + height / 2,
                centerX + width / 2,
                centerY - height / 2
        );
        initVision();
    }

    public boolean isTargetCentered()
    {
        List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();
        ColorBlobLocatorProcessor.Util.filterByArea(MIN_BLOB_AREA, MAX_BLOB_AREA, blobs);

        if (!blobs.isEmpty())
        {
            ColorBlobLocatorProcessor.Blob largest = blobs.get(0);
            RotatedRect boxFit = largest.getBoxFit();
            return Math.abs(boxFit.center.x - 160) < CENTER_THRESHOLD;  // 160 is center for 320 width
        }
        return false;
    }

    public String getAlignmentHint()
    {
        List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();
        ColorBlobLocatorProcessor.Util.filterByArea(MIN_BLOB_AREA, MAX_BLOB_AREA, blobs);

        if (!blobs.isEmpty())
        {
            ColorBlobLocatorProcessor.Blob largest = blobs.get(0);
            RotatedRect boxFit = largest.getBoxFit();
            double offset = boxFit.center.x - 160;  // Distance from center

            if (Math.abs(offset) < CENTER_THRESHOLD)
            {
                return "Centered";
            } else
            {
                return offset > 0 ? "Move Right" : "Move Left";
            }
        }
        return "No Target";
    }

    @SuppressLint("DefaultLocale")
    public void updateTelemetry(Telemetry telemetry)
    {
        List<ColorBlobLocatorProcessor.Blob> blobs = colorLocator.getBlobs();
        ColorBlobLocatorProcessor.Util.filterByArea(MIN_BLOB_AREA, MAX_BLOB_AREA, blobs);

        telemetry.addData("Alignment", getAlignmentHint());

        if (!blobs.isEmpty())
        {
            ColorBlobLocatorProcessor.Blob largest = blobs.get(0);
            RotatedRect boxFit = largest.getBoxFit();

            telemetry.addData("Area", largest.getContourArea());
            telemetry.addData("Center", String.format("(%.0f, %.0f)",
                    boxFit.center.x, boxFit.center.y));
            telemetry.addData("Density", String.format("%.2f", largest.getDensity()));
        }

        telemetry.update();
    }

    public void openClaw()
    {
        clawServo.setPosition(CLAW_OPEN);
    }

    public void closeClaw()
    {
        clawServo.setPosition(CLAW_CLOSED);
    }

    public void shutdown()
    {
        if (visionPortal != null)
        {
            visionPortal.close();
        }
    }
}