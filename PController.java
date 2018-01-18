package ca.mcgill.ecse211.WallFollower;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;

public class PController implements UltrasonicController {

  /* Constants */
  private static final int MOTOR_SPEED = 200;
  private static final int FILTER_OUT = 20;

  private final int bandCenter;
  private final int bandWidth;
  private int distance;
  private int filterControl;
  private final int minHypotenus;
  private final int maxHypotenus;

  public PController(int bandCenter, int bandwidth) {
    this.bandCenter = bandCenter;
    this.bandWidth = bandwidth;
    this.filterControl = 0;

    WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED); // Initalize motor rolling forward
    WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
    
    this.minHypotenus = (int) ((this.bandCenter - this.bandWidth + 11) * 1.1412);
    this.maxHypotenus = (int) ((this.bandCenter + this.bandWidth + 11) * 1.1412);
  }

  @Override
  public void processUSData(int distance) {

    // rudimentary filter - toss out invalid samples corresponding to null
    // signal.
    // (n.b. this was not included in the Bang-bang controller, but easily
    // could have).
    //
    if (distance >= 255 && filterControl < FILTER_OUT) {
      // bad value, do not set the distance var, however do increment the
      // filter value
      filterControl++;
    } else if (distance >= 255) {
      // We have repeated large values, so there must actually be nothing
      // there: leave the distance alone
      this.distance = distance;
    } else {
      // distance went below 255: reset filter and leave
      // distance alone.
      filterControl = 0;
      this.distance = distance;
    }

    // TODO: process a movement based on the us distance passed in (P style)
    
    if(this.distance < this.minHypotenus) { // if too close
    	WallFollowingLab.rightMotor.setSpeed(((float)this.distance/(float)this.minHypotenus)*MOTOR_SPEED);
    	WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED);
    	WallFollowingLab.leftMotor.forward();
      WallFollowingLab.rightMotor.forward();
    }
   
    else if (this.distance > this.maxHypotenus & this.distance<= 2*this.maxHypotenus) {
    	WallFollowingLab.leftMotor.setSpeed(-MOTOR_SPEED*((float)this.distance/((float)this.maxHypotenus)) + 2*MOTOR_SPEED);
    	WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    	WallFollowingLab.leftMotor.forward();
      WallFollowingLab.rightMotor.forward();
    }
    else if(this.distance>2*this.maxHypotenus) {
    	WallFollowingLab.leftMotor.setSpeed(0);
    	WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    	WallFollowingLab.leftMotor.forward();
      WallFollowingLab.rightMotor.forward();
    }
    
    else {
    	WallFollowingLab.leftMotor.setSpeed(MOTOR_SPEED);
    	WallFollowingLab.rightMotor.setSpeed(MOTOR_SPEED);
    	WallFollowingLab.leftMotor.forward();
      WallFollowingLab.rightMotor.forward();
    }
  }

    
 

  @Override
  public int readUSDistance() {
    return this.distance;
  }

}
