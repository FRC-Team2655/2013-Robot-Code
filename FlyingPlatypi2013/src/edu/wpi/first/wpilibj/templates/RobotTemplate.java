/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.*;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
/**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     *  
     * 1: A
    2: B
    3: X
    4: Y
    5: Left Bumper
    6: Right Bumper
    7: Back
    8: Start
    9: Left Joystick
    10: Right Joystick

Axis Mappings #

The axis on the controller return values between -1 and 1, and follow the following mapping :

    1: Left Stick X Axis
        Left:Negative
        Right: Positive
    2: Left Stick Y Axis
        Up: Negative
        Down: Positive
    3: Triggers
        Left: Positive
        Right: Negative
    4: Right Stick X Axis
        Left: Negative
        Right: Positive
    5: Right Stick Y Axis
        Up: Negative
        Down: Positive
    6: Directional Pad (Not recommended, buggy)
     */
public class RobotTemplate extends IterativeRobot {
    
    //TODO All references are created but not instantiatecd here

    Victor shooter;
    Victor loader;
    SendableChooser teleopChooser;
    Relay defense;
    Object dumpMode, shootMode, shootingMode;
    private final int shootPWM = 9;
    private final int loaderPWM = 10;
    private final int defenseChannel = 3;
    
    
    Talon leftMotor; //drive motors
    Talon rightMotor;
    RobotDrive driveMotors; //drive motor controller
    Jaguar climb1; //climbers
    Jaguar climb2; 
    Victor lifter; //lifter
    Victor pusher; //pusher
    Victor latch; //latch
    Relay dump;
    Encoder driveEncoder1;  //encoders
    Encoder driveEncoder2; 
    Joystick joystick1; //controls
    Joystick joystick2;
    Joystick xbox;
    Gyro gyro; //Gyro
    DigitalInput lifter1max;
    DigitalInput lifter1min;
    DigitalInput lifter2max;
    DigitalInput lifter2min;
    AnalogChannel lifter2minA;
    AnalogChannel lifter1minA;
    Relay lights;
    //Smartdashboard stuff
    SendableChooser autonomousChooser;
    SendableChooser values;
    
    
    //TODO Set these to the actual slot numbers
    private final int slotNumber = 2;
    private final int analogSlotNumber = 1;
    //Motor PWM Channels
    private final int driveMotor1PWM = 2;
    private final int driveMotor2PWM = 1;
    private final int climb1PWM = 3;
    private final int climb2PWM = 4;
    private final int lifterPWM = 5;
    private final int pusherPWM = 6;
    private final int latchPWM = 7;
    
    //DIO Channels
    private final int lifter1maxPWM = 1; 
    private final int lifter2maxPWM = 2;
    private final int lifter2minPWM = 3;   
    private final int lifter1minPWM = 4;
    private final int lifter1minAnalog = 6;   
    private final int lifter2minAnalog = 7;
    private final int encoder1Channel1 = 5;
    private final int encoder1Channel2 = 6;
    private final int encoder2Channel1 = 7;
    private final int encoder2Channel2 = 8;
    
    //Analog Channels
    private final int gyroChannel = 1;
    
    //Relay
    private final int lightsChannel = 8;
    private final int dumpRelay = 2;
    
    //Intergers for driving autonomously
    private final double heading = 0.03;
    private final double autoSpeed = 0.4;
    private double postCenterDistance = 1000;
    private final double postLeftDistance = 10000;
    private final double postRightDistance = 10000;
    boolean autoInPosition , atGoal;
    double dumpTime = 0;
    double dumpTime2 = 0.4;
    int state = 0;
    double backUpDistance =  400;
    double backUpAngle = -45;
    double backUpMidField = 1000;
    
    //teleop 
    double currentSpeed1 = 0;
    double currentSpeed2 = 0;
    double increment = .1;
    double incrementReverse = .05;
    int lightVar = 0;
    private boolean lifter1minProc = false, lifter2minProc = false, shooterOn = true;


    

    
    //Object Choosers for Autnomous
    
    Object closePost;
    Object backPost;
    Object sidePost;
    Object noPost;
    Object customPost;
    Object selectedMode;
       
    Timer autonomousTimer;
    Timer latchTimer;
    Timer lightTimer;
    
    
    
    
    
   
    

    
    //Example:  RobotDrive drive; 
    
    
    
    public void robotInit() {
        
//TODO Instantiate objects here
//Example: driveTrain = new RobotDrive(1,2);
        
    leftMotor = new Talon(driveMotor1PWM);
    rightMotor = new Talon(driveMotor2PWM);
    lifter = new Victor(lifterPWM); //lifter
    pusher = new Victor(pusherPWM);
    driveMotors = new RobotDrive(leftMotor, rightMotor);
    latch = new Victor(latchPWM);
    dump = new Relay(dumpRelay);
    shooter = new Victor(shootPWM);
    loader = new Victor(loaderPWM);
    defense = new Relay(defenseChannel);
    climb1 = new Jaguar(climb1PWM);
    climb2 = new Jaguar(climb2PWM);
    
    lifter1max = new DigitalInput(lifter1maxPWM);
    lifter1min = new DigitalInput(lifter1minPWM);
    lifter2max = new DigitalInput(lifter2maxPWM);
    lifter2min = new DigitalInput(lifter2minPWM);

    lights = new Relay(lightsChannel);
    driveEncoder1 = new Encoder(encoder1Channel1,encoder1Channel2); 
    driveEncoder2 = new Encoder(encoder2Channel1,encoder2Channel2);
    driveEncoder1.start();
    driveEncoder2.start();
    gyro = new Gyro(analogSlotNumber, gyroChannel);

    joystick1 = new Joystick(1);
    joystick2= new Joystick(2);
    xbox = new Joystick(3);
    autonomousChooser = new SendableChooser();
    values = new SendableChooser();
    teleopChooser = new SendableChooser();
    //speedMulti = new SendableChooser();
    closePost = new Object();
    backPost = new Object();
    sidePost = new Object();
    noPost = new Object();
    customPost = new Object();
   
   
shootingMode = new Object();
    shootMode = new Object();
    dumpMode = new Object();


    
    autonomousChooser.addObject("Closest post", closePost);
    autonomousChooser.addObject("Back post", backPost);
    autonomousChooser.addObject("Side post", sidePost);
    autonomousChooser.addObject("Custom", customPost);
    autonomousChooser.addDefault("DO NOTHING", noPost);
    autonomousChooser.addObject("SHOOT", shootingMode);
    SmartDashboard.putData("Autonomouse Program Chooser", autonomousChooser);



teleopChooser.addDefault("Dumping stuff", dumpMode);
    teleopChooser.addObject("Shooting time", shootMode);
    SmartDashboard.putData("Teleop Mode Chooser", teleopChooser);

    
    
    
    //SmartDashboard.putData("Speed Multiplier", speedMulti);
    autonomousTimer = new Timer();
    latchTimer = new Timer();
    lightTimer = new Timer();
    
    lifter1minA = new AnalogChannel(lifter1minAnalog);
    lifter2minA = new AnalogChannel(lifter2minAnalog);
   
    
    
    LiveWindow.addActuator("Drive", "Left Motor", leftMotor);
    LiveWindow.addActuator("Drive", "Right Motor", rightMotor);
    LiveWindow.addActuator("Climb", "Climb 1", climb1);
    LiveWindow.addActuator("Climb", "Climb 2", climb2);
    LiveWindow.addActuator("Lifter", "Lifter", lifter);
    LiveWindow.addActuator("pusher", "Pusher", pusher);
    LiveWindow.addActuator("Dump", "Dump", dump);
    LiveWindow.addActuator("Latch", "Latch", latch);
    LiveWindow.addSensor("Drive", "Encoder 1", driveEncoder1);
    LiveWindow.addSensor("Drive", "Encoder 2", driveEncoder2);
    LiveWindow.addSensor("Drive", "Gyro", gyro);
    LiveWindow.addSensor("Lifter", "Lifter1Max", lifter1max);
    LiveWindow.addSensor("Lifter", "Lifter2Max", lifter2max);
    LiveWindow.addSensor("Lifter", "Lifter1Min", lifter1min);
    LiveWindow.addSensor("Lifter", "Lifter2Min", lifter2min);
    LiveWindow.addSensor("Lifter", "Lifter1MinA", lifter1minA);
    LiveWindow.addSensor("Lifter", "Lifter2MinA", lifter2minA);
    SmartDashboard.putNumber("back up?", 1);
    SmartDashboard.putNumber("Autonomous Delay", 0);
    SmartDashboard.putNumber("Distance", 1000);
    
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousInit(){
    selectedMode = autonomousChooser.getSelected();
    gyro.reset();
    autonomousTimer.reset();
    autonomousTimer.start();
    driveEncoder1.reset();
    driveEncoder2.reset();
    dumpTime = 0;
    state = -1;

     autoInPosition = false;
     atGoal = false;
    }
    public void autonomousPeriodic() {
        SmartDashboard.putNumber("gyro", gyro.getAngle());
        selectedMode = autonomousChooser.getSelected();
    
        double encoder2 = (driveEncoder2.getDistance());
        double encoder1 = -(driveEncoder1.getDistance());
        double targetDistance;
        double correctAngle=0;
        
       if(selectedMode.equals(closePost)){
            targetDistance = postCenterDistance;
            correctAngle = 0;
       }
       else if(selectedMode.equals(backPost)){
           targetDistance = postRightDistance;
           correctAngle = 30;
       }
       else  if(selectedMode.equals(sidePost)){
           targetDistance = postLeftDistance;
           correctAngle = -20;
       }
       else if (selectedMode.equals(customPost)){
//            SmartDashboard.putNumber("test", 5.00);
           targetDistance = SmartDashboard.getNumber("Distance");
           correctAngle = 0;
          
         
       }
       else if (selectedMode.equals(shootingMode)){
           //TODO shooting autonomous code
           driveMotors.drive(0,0);
           targetDistance = 0;
           shooter.set(1.0);
            
           if(DriverStation.getInstance().getMatchTime() >2&& autonomousTimer.get()> .5 && autonomousTimer.get() < 1){
               dump.set(Relay.Value.kOn);
               dump.set(Relay.Value.kForward);
       }
           else if(autonomousTimer.get() > 1.75){
               autonomousTimer.reset();
           }
           else{
               dump.set(Relay.Value.kOff);
           }
               
           }

       else {
         targetDistance = 0;
         correctAngle = 0;
       }
       
        
    
        if((autonomousTimer.get() >= SmartDashboard.getNumber("Autonomous Delay")) && targetDistance > 0 ){
            if((encoder1 < targetDistance || encoder2 < targetDistance)&& atGoal == false){        

                driveMotors.drive(autoSpeed, gyro.getAngle()*-heading);
                
            }
            /*else if (Math.abs(gyro.getAngle()) < Math.abs(correctAngle) ){
                if(correctAngle > 0){
                driveMotors.tankDrive(0.3,0);
                }
                else{
                driveMotors.tankDrive(0,0.3);
                }
           }*/
            else if(autoInPosition == false){
                atGoal = true;
                if (encoder1 > targetDistance - 100 || encoder2 > targetDistance - 100){
                    driveMotors.tankDrive(-.5, -.5);
                }
                else{
                    autoInPosition = true;
                }
            }
            else{
                driveMotors.drive(0, 0);
                if(dumpTime == 0){
                    dumpTime = autonomousTimer.get();
                }
                else if ((autonomousTimer.get() - dumpTime) < dumpTime2){
                    dump.set(Relay.Value.kOn);
                    dump.set(Relay.Value.kReverse);
                }
                else if ((autonomousTimer.get() - dumpTime) < (dumpTime2 + 1)){
                    dump.set(Relay.Value.kOff);
                }
                else if ((autonomousTimer.get() - dumpTime) < (2*dumpTime2+1)){
                    dump.set(Relay.Value.kOn);
                    dump.set(Relay.Value.kForward);
                }
                else{
                    dump.set(Relay.Value.kOff);
                    driveMotors.tankDrive(0, 0);
                    state = 0;
                }
            }
         if (SmartDashboard.getNumber("back up?") > 0 && state >= 0){
            if(state == 0){ // int back up
                autonomousTimer.reset();
                autonomousTimer.start();
                gyro.reset();
                driveEncoder1.reset();
                driveEncoder2.reset();
                state = 1;
            }
            else if (state == 1){
                if (encoder1 > -backUpDistance){
                driveMotors.drive(-.5, .5);
                }
                else{
                    driveMotors.drive(0, 0);
                    state = 2;
                }
            }
            else if (state == 2){
                if (gyro.getAngle() > backUpAngle){
                    driveMotors.tankDrive(-0.5, 0.5);
                }
                else{
                    driveMotors.drive(0,0);
                    driveEncoder1.reset();
                    driveEncoder2.reset();
                    gyro.reset();
                    state = 3;
                }
            }
            else if(state == 3){
             if( encoder1 > -backUpMidField || encoder2 > -backUpMidField){        

                driveMotors.drive(-autoSpeed, gyro.getAngle()*heading);    
                }
             else{
                 driveMotors.drive(0,0);
                 state = 4;
             }
            }
            else{
                driveMotors.tankDrive(0,0);
            }
        }
        
        }
        
        
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopInit(){
//        lightTimer.reset();
//        lightTimer.start();
        selectedMode=teleopChooser.getSelected();
        shooterOn = false;
        lights.set(Relay.Value.kOn);
        lights.setDirection(Relay.Direction.kForward);
    
    }
    public void teleopPeriodic() {
//  TODO Teleop Code
       // increment = speedMulti;
        SmartDashboard.putNumber("Encoder 1", driveEncoder1.get());
        SmartDashboard.putNumber("Encoder 2", driveEncoder2.get());
        SmartDashboard.putNumber("current speed left", currentSpeed1);
        SmartDashboard.putNumber("current speed right", currentSpeed2);
        SmartDashboard.putNumber("joy 1", joystick1.getRawAxis(1));
        SmartDashboard.putNumber("joy 2", joystick2.getRawAxis(1));
        double joystickValue1 = joystick1.getRawAxis(2);// Changed from 2 -- left stick
        double joystickValue2 = joystick2.getRawAxis(2);// Changed from 2 -- right stick
        
        if(lifter1minA.getVoltage() > 5){
            lifter1minProc = false;
        }
        else{
            lifter1minProc = true;
        }
        
        if(lifter2minA.getVoltage() > 5){
            lifter2minProc = false;
        }
        else{
            lifter2minProc = true;
        }
        
        
     //Drive--------------------------------------------------------------------Drive   
        //left side drive
        if (currentSpeed1 < -joystickValue1){
            currentSpeed1 = currentSpeed1 + increment;
        }
        else if (currentSpeed1 > -joystickValue1){
            currentSpeed1 = currentSpeed1 - incrementReverse;
        }
        //right side drive
        if (currentSpeed2 < -joystickValue2){
            currentSpeed2 = currentSpeed2 + increment;
        }
        else if (currentSpeed2 > -joystickValue2){
            currentSpeed2 = currentSpeed2 - incrementReverse;
        }
    driveMotors.tankDrive(currentSpeed1, currentSpeed2);   
    
    
    //latch---------------------------------------------------------------------Latch
    if(xbox.getRawButton(5)){ //Close Latch
       
       latch.set(0.45);

    }
    
     else if (xbox.getRawButton(6)){ 
        latch.set(-0.45);
       
    }
    else{
        latch.set(0);
    }
  
    //if(latchTimer.get() == 5)
    //{
    //    latch.set(Relay.Value.kOff);
    //    latchTimer.reset();
    //}
    //pusher--------------------------------------------------------------------Pusher
 
    if (xbox.getRawButton(8)){ 
           pusher.set(1.00);
           
     }
    
   else if(xbox.getRawButton(7)){
            pusher.set(-1.00);
    }
   else {
        pusher.set(0.0);
    }
       
     
   
    //lifter--------------------------------------------------------------------Lifter
   
 
    if(xbox.getRawButton(4)){
        lifter.set(0.3);
    }
    else if(xbox.getRawButton(1)){
        lifter.set(-0.8);
    }
     else{
        lifter.set(0.0);
    }
   
   
    //climb---------------------------------------------------------------------Climb
   // boolean overRide = joystick1.getRawAxis(3) > .6;
    boolean overRide = joystick1.getRawButton(10);
    if((((lifter1max.get() == false && xbox.getRawAxis(2) < 0)|| (lifter1min.get() && xbox.getRawAxis(2) > 0))&&(Math.abs(xbox.getRawAxis(2))> 0.2))||(overRide)) {
       climb1.set(-xbox.getRawAxis(2));
   }
   else {
       climb1.set(0);
   }
   if((((lifter2max.get() == false && xbox.getRawAxis(5) < 0)||(lifter2min.get() && xbox.getRawAxis(5) > 0))&&(Math.abs(xbox.getRawAxis(5))> 0.2)||(overRide))){
       climb2.set(xbox.getRawAxis(5));
   }
   else {
       climb2.set(0);
   }
  

   if(selectedMode.equals(dumpMode)){
   if (joystick1.getRawButton(1)){
       dump.set(Relay.Value.kOn);
       dump.set(Relay.Value.kReverse);
   }
   else if(joystick2.getRawButton(1)){
       dump.set(Relay.Value.kOn);
       dump.set(Relay.Value.kForward);
   }
   else{
       dump.set(Relay.Value.kOff);
   }
   }
   else if(selectedMode.equals(shootMode)){
      
       if(joystick2.getRawButton(2)){
           shooterOn = false;
       }
       if(joystick2.getRawButton(1)){
           shooterOn = true;
       }
       if(shooterOn){
           shooter.set(1.0);
           
       }
       else {
           shooter.set(0);
       }
       if(joystick1.getRawButton(1)){
           dump.set(Relay.Value.kOn);
           dump.set(Relay.Value.kForward);
       }
       else{
           dump.set(Relay.Value.kOff);
       }
       
   }
    if (joystick1.getRawButton(4) == true){
         defense.set(Relay.Value.kOn);
         defense.set(Relay.Value.kForward);
    
       
    }
    else if(joystick2.getRawButton(6) == true){
        defense.set(Relay.Value.kOn);
        defense.set(Relay.Value.kReverse);
    }
    else{
        defense.set(Relay.Value.kOff);
    }
   
   
   //lights
//    if (lightTimer.get() > 90){
      
        lights.set(Relay.Value.kOn);
        lights.setDirection(Relay.Direction.kForward);
//    }
//    else{
//        lights.set(Relay.Value.kOff);
//    
//    }   
    // if(xbox.getRawAxis(2) == 0){
   //     climb1.set(0);
   //     climb2.set(0);
   // }
   /* if((xbox.getRawAxis(3) > 0)){
        if((lifter1min.get() == false)){
        climb1.set(xbox.getRawAxis(3));    
        }
        else if((lifter1min.get() == true )){
        climb1.set(0);    
        }
        
    }
    if((xbox.getRawAxis(3) < 0)){
        if((lifter2min.get() == false)){
            climb2.set(xbox.getRawAxis(3));
        }
       else if((lifter2min.get() == true)){
            climb2.set(0);
        }
        
    }
    if((xbox.getRawButton(5))&&((xbox.getRawAxis(3) > 0))){
        if((lifter1max.get() == false)){
            climb1.set(-(xbox.getRawAxis(3)));
        }
        else if(lifter1max.get()){
            climb1.set(0);
            
        }
    }
    if((xbox.getRawButton(6))&&((xbox.getRawAxis(3) < 0))){
        if(lifter2max.get() == false){
         climb2.set(-(xbox.getRawAxis(3)));
        }
        else if(lifter2max.get()){
            climb2.set(0);
            
        }
        
    }
    */ 
    
// }       
    }
    public void testInit(){
   
        
    }
    public void testPeriodic(){
        LiveWindow.run();
        
    
    }
    
    public void end() {
        lights.set(Relay.Value.kOn);
        lights.set(Relay.Value.kForward);
    
}
    
}
