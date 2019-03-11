package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import sun.nio.cs.ext.MacHebrew;

import static java.lang.Math.abs;

@TeleOp(name="Kiwi", group="DemoBot")
public class OrientedKiwi extends OpMode
{

    private DcMotor motorOne;
    private DcMotor motorTwo;
    private DcMotor motorThree;
    public BNO055IMU imu;
    private boolean pressed = false;
    private double speed = 2;

    @Override
    public void init()
    {
        motorOne = hardwareMap.dcMotor.get("one");
        motorTwo = hardwareMap.dcMotor.get("two");
        motorThree = hardwareMap.dcMotor.get("three");
        motorOne.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorTwo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorThree.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
    }

    @Override
    public void loop()
    {

        //
        double gyroFirstAngle = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS).firstAngle;

        // Controller values
        double leftStickX1 = gamepad1.left_stick_x;
        double leftStickY1 = gamepad1.left_stick_y;
        double r = gamepad1.right_stick_x/3;
        boolean leftBumper = gamepad1.left_bumper;
        boolean rightBumper = gamepad1.right_bumper;
;

        double scale = abs(r) + abs(leftStickX1) + abs(leftStickY1);

        //scales the inputs when needed
        if(scale > 1)
        {
            leftStickX1 /= scale;
            leftStickY1 /= scale;
            r /= scale;
        }

        double x = leftStickX1*Math.cos(gyroFirstAngle) + leftStickY1*Math.sin(gyroFirstAngle);
        double y = -leftStickX1*Math.sin(gyroFirstAngle) + leftStickY1*Math.cos(gyroFirstAngle);
        double motor1Power = -x/2 - 0.866*y + r;
        double motor2Power = -x/2 + 0.866*y + r;
        double motor3Power = x + r;
        if(gamepad1.x)
        {

            motorOne.setPower(1);
            motorTwo.setPower(1);
            motorThree.setPower(1);

        }
        else {
            motorOne.setPower(motor1Power / speed);
            motorTwo.setPower(motor2Power / speed);
            motorThree.setPower(motor3Power / speed);
        }

        if(leftBumper && !pressed)
        {
            speed += 0.1;
            pressed = true;
        }
        else if(pressed && !leftBumper)
        {
            pressed = false;
        }
        if(rightBumper && !pressed)
        {
            speed -= 0.1;
            pressed = true;
        }
        else if(pressed && !rightBumper)
        {
            pressed = false;
        }
        if(speed < 1)
        {
            speed = 1;
        }

        telemetry.addData("speed",1/speed);
        telemetry.addData("motor1",motor1Power);
        telemetry.addData("motor2",motor2Power);
        telemetry.addData("motore3",motor3Power);

    }

    public double getOrientedDriveValue() {
        return 0;
    }
}
