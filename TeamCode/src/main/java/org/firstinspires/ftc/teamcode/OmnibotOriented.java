package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.RobotLog;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;


import static java.lang.Math.abs;

/**
 * Demo Omnibot code for showing off the omnibot
 */
@TeleOp(name="Omnibot Oriented", group="DemoBot")
public class OmnibotOriented extends OpMode {

    //drive train motors
    private DcMotor frontLeft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;

    public BNO055IMU imu;

    //bumper speed adjustion for master controls
    private double speed = 2;
    private boolean pressed = false;
    private boolean pressed2 = false;
    private boolean pressedA = false;
    private boolean move = true;
    public double adjust = 0;

    @Override
    public void init() {
        //defining motors from config
        //hi spitz
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);

        //setting power to the motors to make sure they are not moving
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

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

    /*@Override
    public void init_loop() {
        telemetry.addData("imu calabration", imu.isGyroCalibrated());
        telemetry.update();
    }*/

    @Override
    public void loop() {
        double leftStickX, leftStickY, rightStickX;
        //movement for robot input kid
        double leftStickY1 = gamepad1.left_stick_y;
        double leftStickX1 = gamepad1.left_stick_x;
        //rotation for robot input kid
        double rightStickX1 = gamepad1.right_stick_x;

        //movement for robot input master
        double leftStickY2 = gamepad2.left_stick_y;
        double leftStickX2 = gamepad2.left_stick_x;
        //rotation for robot input master
        double rightStickX2 = gamepad2.right_stick_x;

        //bumper for speeding up and slowing down the robot.
        boolean leftBumper2 = gamepad2.left_bumper;
        boolean rightBumper2 = gamepad2.right_bumper;

        //a input for stopping the robot working for kid TOGGLE
        boolean a2 = gamepad2.a;

        //toggle to stop robot
        if(a2 && !pressedA) {
            move = !move;
            pressedA = true;
        }
        else if(pressedA && !a2) {
            pressedA = false;
        }

        //slows down robot with master bumper
        if(leftBumper2 && !pressed) {
            speed += 0.2;
            pressed = true;
        }
        // resets pressed for when it isnt pressed
        else if(pressed && !leftBumper2) {
            pressed = false;
        }
        //speeds up robot with master bumper
        if(rightBumper2 && !pressed2) {
            speed -= 0.2;
            pressed2 = true;
        }
        // resets pressed for when it isnt pressed
        else if(pressed2 && !rightBumper2) {
            pressed2 = false;
        }

        //stops divide by 0 error, fastest the robot can go
        if(speed < 1) {
            speed = 1;
        }

        //changing who is driving and move is for stopping kid driving
        if(leftStickX2 == 0 && leftStickY2 == 0 && rightStickX2 == 0 && move) {
            leftStickX = leftStickX1;
            leftStickY = leftStickY1;
            rightStickX = rightStickX1;
        }
        else {
            leftStickX = leftStickX2;
            leftStickY = leftStickY2;
            rightStickX = rightStickX2;
        }

        //movement for robot method being run
        orientedDrive(leftStickY/speed, -leftStickX/speed, rightStickX/speed, gamepad2.x);

        telemetry.addData("Speed", 1/speed);
        telemetry.addData("override", !move);
        telemetry.update();

        RobotLog.ii("5040MSGHW","Motors running");
    }


    public void orientedDrive(double forward, double sideways, double rotation, boolean reset) {

        double P = Math.hypot(sideways, forward);
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);

        double robotAngle = Math.atan2(-forward, -sideways);

        if(reset) {
            adjust = angles.firstAngle;
        }
        double v5 = P * Math.sin(robotAngle - angles.firstAngle + adjust) + P * Math.cos(robotAngle - angles.firstAngle + adjust) - rotation;
        double v6 = P * Math.sin(robotAngle - angles.firstAngle + adjust) - P * Math.cos(robotAngle - angles.firstAngle + adjust) + rotation;
        double v7 = P * Math.sin(robotAngle - angles.firstAngle + adjust) - P * Math.cos(robotAngle - angles.firstAngle + adjust) - rotation;
        double v8 = P * Math.sin(robotAngle - angles.firstAngle + adjust) + P * Math.cos(robotAngle - angles.firstAngle + adjust) + rotation;

        frontLeft.setPower(v5);
        frontRight.setPower(v6);
        backLeft.setPower(v7);
        backRight.setPower(v8);
        //}
    }
}