package com.hoty.handler;

import com.hoty.entity.User;
import javax.validation.constraints.NotNull;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordHandler {
	private static final String PASSWORD_HASH_ALGORITHM = "SHA-256";
	private static final byte[] PASSWORD_PEPPER = "[9a%\\tK/~F!/".getBytes();
	private static final int PASSWORD_SALT_LENGTH = 12;
	private static final String PASSWORD_SALT_ALPHABET =
			"0123456789abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ`~!@#$%^&*()-+[]{}\\\\|,.<>/?";
	private static final SecureRandom saltRandom = new SecureRandom();

	public static String generateSalt() {
		final char[] salt = new char[PASSWORD_SALT_LENGTH];

		for(int i = 0; i < salt.length; i++) {
			salt[i] = PASSWORD_SALT_ALPHABET.charAt(saltRandom.nextInt(PASSWORD_SALT_ALPHABET.length()));
		}

		return new String(salt);
	}

	public static String hashPassword(String password, String salt) {
		final MessageDigest md;
		try {
			md = MessageDigest.getInstance(PASSWORD_HASH_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(PASSWORD_HASH_ALGORITHM + " hash algorithm doesn't exist", e);
		}
		md.update(PASSWORD_PEPPER);
		md.update(password.getBytes());
		md.update(salt.getBytes());
		final byte[] digest = md.digest();

		final StringBuilder hash = new StringBuilder();
		for(byte b : digest) {
			hash.append(String.format("%02x", b));
		}

		return hash.toString();
	}

	public static boolean checkPassword(@NotNull User user, @NotNull String password) {
		return user.getPasswordHash().equals(hashPassword(password, user.getPasswordSalt()));
	}
}
