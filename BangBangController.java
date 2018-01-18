package ca.mcgill.ecse211.WallFollower;

import lejos.hardware.motor.*;


public class BangBangController implements UltrasonicController {
	private static final int FILTER_OUT = 20;
	
  private final int bandCenter;
  private final int bandwidth;
  private final int motorLow;
  private final int motorHigh;
  private final int minHypotenus;
  private final int maxHypotenus;
  private int distance;
  private int filterControl;

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.forward();
    WallFollowingLab.rightMotor.forward();
    
    this.minHypotenus = (int) ((this.bandCenter - this.bandwidth + 11) * 1.1412);
    this.maxHypotenus = (int) ((this.bandCenter + this.bandwidth + 11) * 1.1412);
   
    
  }

  @Override
  public void processUSData(int distance) {
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
    // TODO: process a movement based on the us distance passed in (BANG-BANG style)
   
    if(this.distance < this.minHypotenus) { // if too close
    	WallFollowingLab.rightMotor.setSpeed(motorLow/10);
    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
    }
   
    else if (this.distance > this.maxHypotenus) {
    	WallFollowingLab.leftMotor.setSpeed(motorLow/10);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    }
    
    else {
    	WallFollowingLab.leftMotor.setSpeed(motorHigh);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    }
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
