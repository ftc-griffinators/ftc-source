package org.firstinspires.ftc.teamcode.math;

import android.annotation.SuppressLint;
import androidx.annotation.NonNull;

public class Transform
{
    public static final Transform IDENTITY = new Transform(new Position(0, 0, 0),
            new Orientation(0, 0, 0));
    public static final Transform INVALID = new Transform(new Position(Double.NaN, Double.NaN,
            Double.NaN), new Orientation(Double.NaN, Double.NaN, Double.NaN));
    public final Position position;
    public final Orientation orientation;

    public Transform(double x, double y, double yaw)
    {
        this(new Position(x, y), new Orientation(yaw));
    }

    public Transform(double x, double y, double z, double roll, double pitch, double yaw)
    {
        this(new Position(x, y, z), new Orientation(roll, pitch, yaw));
    }

    public Transform(Position position, Orientation orientation)
    {

        this.position = position;
        this.orientation = orientation;
    }

    public static Transform from2D(double x, double y, double yaw)
    {
        return new Transform(x, y, yaw);
    }

    public static Transform from3D(
            double x, double y, double z, double roll, double pitch, double yaw)
    {
        return new Transform(x, y, z, roll, pitch, yaw);
    }

    // @Note: For proper 3D, you'd want to use rotation matrices or quaternions
    public Transform multiply(Transform other)
    {
        return new Transform(position.add(other.position), orientation.add(other.orientation));
    }

    public Transform inverse()
    {
        return new Transform(position.scale(-1), new Orientation(-orientation.roll,
                -orientation.pitch, -orientation.yaw));
    }

    public Transform delta(Transform target)
    {
        return this.inverse().multiply(target);
    }

    public Position transformPoint(Position point)
    {
        // Simple 2D transform for now
        // For proper 3D, use rotation matrices
        double cos = Math.cos(Math.toRadians(orientation.yaw));
        double sin = Math.sin(Math.toRadians(orientation.yaw));

        double x = point.x * cos - point.y * sin + position.x;
        double y = point.x * sin + point.y * cos + position.y;
        double z = point.z + position.z;

        return new Position(x, y, z);
    }

    public Transform lerp(Transform target, double t)
    {
        // Linear interpolation between transforms
        return new Transform(new Position(
                position.x + (target.position.x - position.x) * t,
                position.y + (target.position.y - position.y) * t,
                position.z + (target.position.z - position.z) * t), new Orientation(
                orientation.roll + (target.orientation.roll - orientation.roll) * t,
                orientation.pitch + (target.orientation.pitch - orientation.pitch) * t,
                orientation.yaw + (target.orientation.yaw - orientation.yaw) * t));
    }

    public boolean is2D()
    {return position.is2D() && orientation.is2D();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        Transform cframe = (Transform) o;
        return position.equals(cframe.position) && orientation.equals(cframe.orientation);
    }

    @NonNull
    @Override
    public String toString()
    {
        return String.format("CFrame(%s, %s)", position, orientation);
    }

    public static class Position
    {
        public final double x;
        public final double y;
        public final double z;

        // 2d constructor
        public Position(double x, double y)
        {
            this(x, y, 0.0);
        }

        // 3d constructor
        public Position(double x, double y, double z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Position add(Position other)
        {
            return new Position(x + other.x, y + other.y, z + other.z);
        }

        public Position subtract(Position other)
        {
            return new Position(x - other.x, y - other.y, z - other.z);
        }

        public Position scale(double factor)
        {
            return new Position(x * factor, y * factor, z * factor);
        }

        public double distanceTo(Position other)
        {
            double dx = x - other.x;
            double dy = y - other.y;
            double dz = z - other.z;
            return Math.sqrt(dx * dx + dy * dy + dz * dz);
        }

        public boolean is2D()
        {
            return Math.abs(z) < 1e-6;  // small epsilon for floating point comparison
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            Position position = (Position) o;
            return Math.abs(position.x - x) < 1e-6 && Math.abs(position.y - y) < 1e-6 &&
                    Math.abs(position.z - z) < 1e-6;
        }

        @SuppressLint("DefaultLocale")
        @NonNull
        @Override
        public String toString()
        {
            return String.format("Position(%.2f, %.2f, %.2f)", x, y, z);
        }
    }

    public static class Orientation
    {
        public final double roll;  // X rotation
        public final double pitch; // Y rotation
        public final double yaw;   // Z rotation

        public Orientation(double yaw)
        {
            this(0.0, 0.0, yaw);
        }

        public Orientation(double roll, double pitch, double yaw)
        {
            this.roll = normalizeAngle(roll);
            this.pitch = normalizeAngle(pitch);
            this.yaw = normalizeAngle(yaw);
        }

        private static double normalizeAngle(double angle)
        {
            // normalize angle to [-180, 180]
            angle = angle % 360.0;
            if (angle > 180.0)
            {
                angle -= 360.0;
            }
            if (angle < -180.0)
            {
                angle += 360.0;
            }
            return angle;
        }

        public Orientation add(Orientation other)
        {
            return new Orientation(roll + other.roll, pitch + other.pitch, yaw + other.yaw);
        }

        public Orientation subtract(Orientation other)
        {
            return new Orientation(roll - other.roll, pitch - other.pitch, yaw - other.yaw);
        }

        public Orientation scale(double factor)
        {
            return new Orientation(roll * factor, pitch * factor, yaw * factor);
        }

        public boolean is2D()
        {
            return Math.abs(roll) < 1e-6 && Math.abs(pitch) < 1e-6;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o)
            {
                return true;
            }
            if (o == null || getClass() != o.getClass())
            {
                return false;
            }
            Orientation that = (Orientation) o;
            return Math.abs(that.roll - roll) < 1e-6 && Math.abs(that.pitch - pitch) < 1e-6 &&
                    Math.abs(that.yaw - yaw) < 1e-6;
        }

        @SuppressLint("DefaultLocale")
        @NonNull
        @Override
        public String toString()
        {
            return String.format("Orientation(%.2f, %.2f, %.2f)", roll, pitch, yaw);
        }
    }
}