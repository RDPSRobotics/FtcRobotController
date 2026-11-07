package org.firstinspires.ftc.teamcode.Auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Mechanism.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Disabled
@Autonomous (name = "Close Red")
public class SimpleAuto extends OpMode {
    //---------------Driving Variables------------
    DcMotor rFront, rBack, lFront, lBack;

    int targetPositionIndex;

    public void init() {
        rFront = hardwareMap.get(DcMotor.class, "rFront");
        rBack = hardwareMap.get(DcMotor.class, "rBack");
        lFront = hardwareMap.get(DcMotor.class, "lFront");
        lBack = hardwareMap.get(DcMotor.class, "lBack");

        lFront.setDirection(DcMotorSimple.Direction.REVERSE);
        lBack.setDirection(DcMotorSimple.Direction.REVERSE);


        //Auto init

        targetPositionIndex = 0;
    }

    public void loop() {
        if (targetPositionIndex == 0) {
            moveMotorToPosition(1600, 1600, 1600, 1600, 0.5);

            resetRuntime();
        } else if (targetPositionIndex == 2) {
            if (rFront.isBusy()) {
                return;
            }

            rFront.setPower(0);
            rBack.setPower(0);
            lFront.setPower(0);
            lBack.setPower(0);

        }

    }

    public void moveMotorToPosition(int rFTP, int rBTP, int lFTP, int lBTP, double power) {
        rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rFront.setTargetPosition(rFTP);
        rBack.setTargetPosition(rBTP);
        lFront.setTargetPosition(lFTP);
        lBack.setTargetPosition(lBTP);

        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set the power
        rFront.setPower(power);
        lBack.setPower(power);
        lFront.setPower(power);
        rBack.setPower(power);

        while (rFront.isBusy()) {
            telemetry.addData("Current Position", rFront.getCurrentPosition());
            telemetry.addData("Target Position", rFTP);
            telemetry.update();
        }

        targetPositionIndex++;
        // Optionally, you might want to switch back to a different mode here if needed
        // e.g., myMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}


