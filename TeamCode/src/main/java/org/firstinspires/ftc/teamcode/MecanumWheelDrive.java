package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class MecanumWheelDrive extends OpMode {

    public DcMotorEx rFront;
    public DcMotorEx rBack;
    public DcMotorEx lFront;
    public DcMotorEx lBack;

    double forward;
    double turn;
    double strafe;

    int speed;

    @Override
    public void init() {
        rFront = hardwareMap.get(DcMotorEx.class, "rFront");
        rBack = hardwareMap.get(DcMotorEx.class, "rBack");
        lFront = hardwareMap.get(DcMotorEx.class, "lFront");
        lBack = hardwareMap.get(DcMotorEx.class, "lBack");
    }

    @Override
    public void loop() {

        forward = gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;
        strafe = gamepad1.left_stick_x;

        rFront.setVelocity((forward - turn - strafe) * 300);
        rBack.setVelocity((forward - turn + strafe) * 300);
        lFront.setVelocity((forward + turn + strafe) * 300);
        lBack.setVelocity((forward + turn - strafe) * 300);

    }

}
