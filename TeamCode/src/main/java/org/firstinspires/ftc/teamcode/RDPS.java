package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Mechanism.MecanumDrive;
import org.firstinspires.ftc.teamcode.Mechanism.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


@TeleOp
public class RDPS extends OpMode {

    //---------------Driving Variables------------
    MecanumDrive drive = new MecanumDrive();
    public double forward,strafe,rotate;

    //----------------Webcam/Auto Alignment Variables---------------
    Webcam webcam = new Webcam();

    double kP = 0.019;
    double error = 0;
    double lastError = 0;
    double goalX = 0;
    double angleTolerance = 0.4;
    double kD = 0;
    double curTime = 0;
    double lastTime = 0;

    //--------------Shooter Variables-------------
    public DcMotorEx rFlywheel;
    public DcMotorEx lFlywheel;
    public Servo kicker;
    PIDFCoefficients rpidfCoefficients = new PIDFCoefficients(88,0,0,13.534);
    PIDFCoefficients lpidfCoefficients = new PIDFCoefficients(97.6,0,0,15.3);
    double highVelocity = 856;
    double lowVelocity = 0;
    ShootState state = ShootState.START;

    //--------------Intake Variables--------------
    public DcMotorEx intake;

    public void init() {

        //Drive Init
        drive.init(hardwareMap);

        //Webcam Init
        webcam.init(hardwareMap,telemetry);

        //Shooting Init
        rFlywheel = hardwareMap.get(DcMotorEx.class, "rFlywheel");
        lFlywheel = hardwareMap.get(DcMotorEx.class, "lFlywheel");

        kicker = hardwareMap.get(Servo.class, "kicker");

        rFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        lFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        lFlywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        rFlywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, rpidfCoefficients);
        lFlywheel.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, lpidfCoefficients);
        telemetry.addLine("Init Complete");

        //Intake Init
        intake = hardwareMap.get(DcMotorEx.class, "intake");

        intake.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void start() {
        resetRuntime();
        curTime = getRuntime();
    }

    public void loop() {
        //Get controller Inputs
        forward = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        rotate = gamepad1.right_stick_x;

        //Get April Tag Info
        webcam.update();
        AprilTagDetection id20 = webcam.getTagBySpecificID(20);

        if (gamepad1.right_trigger > 0.3) {
            if (id20 != null) {
                error = goalX - id20.ftcPose.bearing;

                if (Math.abs(error) < angleTolerance) {
                    rotate = 0;
                }
                else {
                    double pTerm = -error * kP;

                    curTime = getRuntime();
                    double dT = curTime - lastTime;
                    double dTerm = ((-error - lastError)/dT) * kD;

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

        drive.drive(forward,strafe,rotate);

        //Shoot Code
        switch (state) {
            case START:
                if (gamepad1.right_trigger > 0.3) {
                    rFlywheel.setVelocity(highVelocity);
                    lFlywheel.setVelocity(highVelocity);

                    //Auto Align
                }

                if (rFlywheel.getVelocity() + lFlywheel.getVelocity() >= (highVelocity * 2) - 20) {
                    resetRuntime();
                    state = ShootState.SHOOT;
                }
                break;
            case SHOOT:
                kicker.setPosition(0);

                if (getRuntime() > 0.25) {
                    kicker.setPosition(1);
                }

                if (getRuntime() > 0.5) {

                    resetRuntime();

                    state = ShootState.INTAKE;
                }
                break;
            case INTAKE:
                intake.setPower(0.75);

                if (getRuntime()  > 2) {
                    resetRuntime();

                    state = ShootState.END;
                }
                break;
            case END:
                rFlywheel.setVelocity(lowVelocity);
                lFlywheel.setVelocity(lowVelocity);

                //Turn off auto alignment

                if (getRuntime() > 1) {

                    state = ShootState.START;
                }
                break;
        }

        if (gamepad1.left_trigger > 0.3) {
            intake.setPower(0.75);
        }
        else if (gamepad1.leftBumperWasPressed()) {
            intake.setPower(-0.2);
        }
        else if (state == ShootState.START) {
            intake.setPower(0);
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
    }
}
