package com.starwars.planets.utils;

import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class TestUtils {
	public static Integer getRandomInteger() {
		return Math.abs(new Random().nextInt());
	}

	public static String getRandomString() {
		return String.valueOf(getRandomInteger());
	}

	public static LocalDate getRandomLocalDate() {
		int hundredYears = 100 * 365;
		return LocalDate.ofEpochDay(ThreadLocalRandom.current()
				.nextInt(- hundredYears, hundredYears));
	}
}
