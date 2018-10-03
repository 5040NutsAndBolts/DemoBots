package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Demo Omnibot code for showing off the omnibot
 */
@TeleOp(name="Omnibot", group="DemoBot")
public class Omnibot extends OpMode {

    //drive train motors
    private DcMotor frontleft = null;
    private DcMotor backLeft = null;
    private DcMotor frontRight = null;
    private DcMotor backRight = null;

    //bumper speed adjustion for master controls
    private double speed = 2;
    private boolean pressed = false;
    private boolean pressed2 = false;
    private boolean pressedA = false;
    private boolean move = true;

    @Override
    public void init() {
        //defining motors from config
        frontleft = hardwareMap.dcMotor.get("frontleft");
        backLeft = hardwareMap.dcMotor.get("backLeft");
        frontRight = hardwareMap.dcMotor.get("frontRight");
        backRight = hardwareMap.dcMotor.get("backRight");

        //seting power to the motors to make sure they are not moving
        frontleft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);

        // Send telemetry message to signify robot is ready to start;
        telemetry.addLine("Robot ready to go");
    }

    @Override
    public void loop() {
        double leftStickX, leftStickY, rightStickX;
        //movement for robot input kid
        double leftStick1Y = gamepad1.left_stick_y;
        double leftStick1X = gamepad1.left_stick_x;
        //rotation for robot input kid
        double rightStick1X = gamepad1.right_stick_x;

        //movement for robot input master
        double leftStick2Y = gamepad2.left_stick_y;
        double leftStick2X = gamepad2.left_stick_x;
        //rotation for robot input master
        double rightStick2X = gamepad2.right_stick_x;

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
        if(leftStick2X == 0 && leftStick2Y == 0 && rightStick2X == 0 && move) {
            leftStickX = leftStick1X;
            leftStickY = leftStick1Y;
            rightStickX = rightStick1X;
        }
        else {
            leftStickX = leftStick2X;
            leftStickY = leftStick2Y;
            rightStickX = rightStick2X;
        }

        //movement for robot method being run
        omniDrive(leftStickX, leftStickY, rightStickX);

        RobotLog.ii("5040MSGHW","Motors running");
    }


    //method to move the robot
    private void omniDrive (double sideways, double forward, double rotation)
    {
        //try {
        frontleft.setPower(((forward - sideways)/speed) + (-.3 * rotation));
        backLeft.setPower(((forward + sideways)/speed) + (-.3 * rotation));
        frontRight.setPower(((-forward - sideways)/speed) + (-.3* rotation));
        backRight.setPower(((-forward + sideways)/speed) + (-.3 * rotation));
        //} catch (Exception e) {
        //}
    }
}