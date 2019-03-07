package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import java.lang.Math;

import static java.lang.Math.abs;

@TeleOp(name="Kiwi", group="DemoBot")
public class Kiwi extends OpMode
{

    private DcMotor motorOne;
    private DcMotor motorTwo;
    private DcMotor motorThree;
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

    }

    @Override
    public void loop()
    {
        // Controller values
        double x=gamepad1.right_stick_x;
        double y=gamepad1.right_stick_y;
        double r=gamepad1.left_stick_x;
        boolean leftBumper = gamepad1.left_bumper;
        boolean rightBumper = gamepad1.right_bumper;
;

        double scale = abs(r) + abs(y) + abs(x);

        //scales the inputs when needed
        if(scale > 1)
        {
            y /= scale;
            x /= scale;
            r /= scale;
        }

        double motor1Power = -1/2*x - Math.sqrt(3)/2*y + r;
        double motor2Power = -1/2*x + Math.sqrt(3)/2*y + r;
        double motor3Power = x + r;
        motorOne.setPower(motor1Power/speed );
        motorTwo.setPower(motor2Power/speed);
        motorThree.setPower(motor3Power/speed);

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

    public double getOrientedDriveValue() {
        return 0;
    }
}
