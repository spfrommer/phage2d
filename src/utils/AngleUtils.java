package utils;

public class AngleUtils {
	private AngleUtils() {

	}

	// function in degrees
	public static double absAngle(double angle) {
		if (Math.abs(angle) > 180) {
			angle += 360 * -(angle / Math.abs(angle));
		}
		return angle;
	}
}
