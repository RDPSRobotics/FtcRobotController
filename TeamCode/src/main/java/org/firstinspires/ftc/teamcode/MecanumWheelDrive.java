package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp
public class MecanumWheelDrive extends OpMode {

    public DcMotorEx rFront;
    public DcMotorEx rBack;
    public DcMotorEx lFront;
    public DcMotorEx lBack;

    double forward;
    double turn;
    double strafe;

    int speed = 1000;

    @Override
    public void init() {
        rFront = hardwareMap.get(DcMotorEx.class, "rFront");
        rBack = hardwareMap.get(DcMotorEx.class, "rBack");
        lFront = hardwareMap.get(DcMotorEx.class, "lFront");
        lBack = hardwareMap.get(DcMotorEx.class, "lBack");

        lFront.setDirection(DcMotorSimple.Direction.REVERSE);
        lBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void loop() {

        forward = -gamepad1.left_stick_y;
        turn = gamepad1.right_stick_x;
        strafe = gamepad1.left_stick_x;

        rFront.setVelocity((forward - turn - strafe) * speed);
        rBack.setVelocity((forward - turn + strafe) * speed);
        lFront.setVelocity((forward + turn + strafe) * speed);
        lBack.setVelocity((forward + turn - strafe) * speed);

        telemetry.addData("Forward",forward);
        telemetry.addData("Turn", turn);
        telemetry.addData("Strafe", strafe);

        telemetry.update();
    }

}
