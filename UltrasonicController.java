package ca.mcgill.ecse211.WallFollower;

public interface UltrasonicController {

  public void processUSData(int distance);

  public int readUSDistance();
}
