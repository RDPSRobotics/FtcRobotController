package org.firstinspires.ftc.teamcode.Decode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Mechanism.Webcam;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous (name = "Far Red")
public class AutoFarBlue extends OpMode {
    //---------------Driving Variables------------
    DcMotor rFront,rBack,lFront,lBack;

    double rotate;

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
    boolean autoAlign;

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

    //--------------Target Position Variables---------------

    int targetPositionIndex;

    public void init() {
        rFront = hardwareMap.get(DcMotor.class, "rFront");
        rBack = hardwareMap.get(DcMotor.class, "rBack");
        lFront = hardwareMap.get(DcMotor.class, "lFront");
        lBack = hardwareMap.get(DcMotor.class, "lBack");

        lFront.setDirection(DcMotorSimple.Direction.REVERSE);
        lBack.setDirection(DcMotorSimple.Direction.REVERSE);

        //Webcam Init
        webcam.init(hardwareMap,telemetry);

        autoAlign = false;

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


        //Auto init

        targetPositionIndex = 0;
    }

    public void loop() {
        if (targetPositionIndex == 0) {
            moveMotorToPosition(-500,500,500,-500, 0.5);

            resetRuntime();
        }
        else if (targetPositionIndex == 1) {

            if (rFront.isBusy()) {
                return;
            }

            moveMotorToPosition(100,100,-100,-100, 0.5);

        }
        else if (targetPositionIndex == 2) {

            if (rFront.isBusy()) {
                return;
            }

            //Shoot Code
            switch (state) {
                case START:

                    rFlywheel.setVelocity(highVelocity);
                    lFlywheel.setVelocity(highVelocity);

                    rFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    lFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    lBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

                    rFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    rBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    lFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    lBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

                    autoAlign = true;


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
                    intake.setPower(0.5);

                    if (getRuntime()  > 1) {
                        resetRuntime();

                        state = ShootState.END;
                    }
                    break;
                case END:
                    rFlywheel.setVelocity(lowVelocity);
                    lFlywheel.setVelocity(lowVelocity);

                    autoAlign = false;

                    if (getRuntime() > 1) {
                        moveMotorToPosition(-500,-500,-500,-500,0.5);
                    }
                    break;
            }
        }
        else if (targetPositionIndex == 3) {
            if (rFront.isBusy()) {
                return;
            }

            rFront.setPower(0);
            rBack.setPower(0);
            lFront.setPower(0);
            lBack.setPower(0);
        }

        webcam.update();
        AprilTagDetection id24 = webcam.getTagBySpecificID(20);

        if (autoAlign) {
            if (id24 != null) {
                error = goalX - id24.ftcPose.bearing;

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

            rFront.setPower(-rotate);
            lBack.setPower(rotate);
            lFront.setPower(rotate);
            rBack.setPower(-rotate);
        }
        else {
            lastTime = getRuntime();
            lastError = 0;
        }
    }

    public void moveMotorToPosition(int rFTP,int rBTP,int lFTP,int lBTP, double power) {
        // Set the new target position
        rFront.setTargetPosition(rFTP);
        rBack.setTargetPosition(rBTP);
        lFront.setTargetPosition(lFTP);
        lBack.setTargetPosition(lBTP);

        // Ensure the motor is in RUN_TO_POSITION mode (important order)
        rFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lFront.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lBack.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        // Set the power
        rFront.setPower(power);
        lBack.setPower(power);
        lFront.setPower(power);
        rBack.setPower(power);

        // Wait until the motor is no longer busy (movement is complete)
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
