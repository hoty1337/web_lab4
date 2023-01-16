package com.hoty.handler;

public class Validator {
	private static final int USERNAME_LENGTH_MIN = 3;
	private static final int PASSWORD_LENGTH_MIN = 3;

	public static boolean isValidPoint(double x, double y, double r) {
		return x >= -3 && x <= 5
				&& y >= -5 && y <= 3
				&& r >= -3 && r <= 5;
	}

	public static boolean isHit(double x, double y, double r) {
		return ((x >= 0 && y >= 0 && x <= r / 2 && y <= r) ||
				(x <= 0 && y >= 0 && x * x + y * y <= r * r / 4) ||
				(x >= 0 && y <= 0 && 2 * x - y <= r));
	}

	public static boolean isInvalidUser(String username, String password) {
		String regex = "[A-Za-z0-9]+$";
		if(!username.matches(regex) || !password.matches(regex)) {
			return true;
		}
		return username.length() < USERNAME_LENGTH_MIN || password.length() < PASSWORD_LENGTH_MIN;
	}
}
