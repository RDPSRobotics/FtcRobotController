package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanism.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp
public class AprilTagAutoAlignment extends OpMode {

    private final Webcam webcam = new Webcam();
    private final MecanumDrive drive = new MecanumDrive();

    //-------------------PD CONTROLLER----------------------
    double kP = 0.002;
    double error = 0;
    double lastError = 0;
    double goalX = 0;
    double angleTolerance = 0.4;
    double kD = 0.0001;
    double curTime = 0;
    double lastTime = 0;

    //-------------------DRIVING SETUP-----------------------
    double forward, strafe, rotate;

    //------------------CONTROLLER BASED PD TUNING-------------------------

    double[] stepSizes = {0.1, 0.01, 0.001};

    int stepIndex = 1;

    @Override
    public void init() {
        webcam.init(hardwareMap, telemetry);
        drive.init(hardwareMap);

        telemetry.addLine("Initialize");
    }

    public void start() {
        resetRuntime();
        curTime = getRuntime();
    }

    @Override
    public void loop() {
        //Get Controller Inputs

        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        //Get April Tag Info

        webcam.update();
        AprilTagDetection id20 = webcam.getTagBySpecificID(20);

        //Auto Align Rotation Logic

        if (gamepad1.left_trigger > 0.3) {
            if (id20 != null) {
                error = goalX - id20.ftcPose.bearing;

                if (Math.abs(error) < angleTolerance) {
                    double pTerm = error * kP;

                    curTime = getRuntime();
                    double dT = curTime - lastTime;
                    double dTerm = ((error - lastError)/dT) * kD;

                    rotate = Range.clip(pTerm + dTerm, -0.4, 0.4);

                    lastError = error;
                    lastTime = curTime;

                }
            }
            else {
                lastTime = getRuntime();
                lastError = 0;
            }
        }
        else {
            lastTime = getRuntime();
            lastError = 0;
        }

        //Drive
        drive.drive(forward,strafe,rotate);

        //Increasing Step Sizes
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.dpadLeftWasPressed()) {
            kP -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed())  {
            kP += stepSizes[stepIndex];
        }

        if (gamepad1.dpadUpWasPressed()) {
            kD -=  stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            kD += stepSizes[stepIndex];
        }

        //Telemetry
        if (id20 != null) {
            if (gamepad1.left_trigger > 0.3) {
                telemetry.addLine("AUTO ALIGN");
            }
            webcam.displayDetectionTelemetry(id20);
            telemetry.addData("Error", error);

        } else {
            telemetry.addLine("MANUAL DRIVE");
        }

        telemetry.addLine("-----------------------------");
        telemetry.addData("Tuning P", ".4f%,(D Pad L/R)", kP );
        telemetry.addData("Tuning D", ".4f%,(D Pad U/P)", kD);
        telemetry.addData("Step Sizes", ".4f%,(B Button)", stepSizes[stepIndex]);

    }
}
