package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;

import static java.lang.Math.abs;

@TeleOp(name="Kiwi", group="DemoBot")
public class Kiwi extends OpMode
{

    DcMotor motorOne;
    DcMotor motorTwo;
    DcMotor motorThree;

    @Override
    public void init()
    {

        motorOne = hardwareMap.dcMotor.get("one");
        motorTwo = hardwareMap.dcMotor.get("two");
        motorThree = hardwareMap.dcMotor.get("three");
        motorOne.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorTwo.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorThree.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

    }

    double x;
    double y;
    double r;

    double m1;
    double m2;
    double m3;
    double speed = 2;
    boolean leftBumper;
    boolean rightBumper;
    boolean pressed;

    @Override
    public void loop()
    {
        x=gamepad1.right_stick_x;
        y=gamepad1.right_stick_y;
        r=gamepad1.left_stick_x;

        m1=-1/2*x - Math.sqrt(3)/2*y + r;
        m2=-1/2*x + Math.sqrt(3)/2*y + r;
        m3= x + r;

        double scale = abs(r) + abs(y) + abs(x);

        //scales the inputs when needed
        if(scale > 1)
        {
            y /= scale;
            x /= scale;
            r /= scale;
        }

        motorOne.setPower(m1/speed );
        motorTwo.setPower(m2/speed);
        motorThree.setPower(m3/speed);

        if(leftBumper && !pressed)
        {
            speed += 0.2;
            pressed = true;
        }
        else if(pressed && !leftBumper)
        {
            pressed = false;
        }
        if(rightBumper && !pressed)
        {
            speed -= 0.2;
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

    }

}
