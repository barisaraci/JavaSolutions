package monopoly.domains.entities;

import java.util.Random;

import monopoly.domains.Die;

public class RegularDie implements Die {
	
	private int faceValue;
	private Random random = new Random();

	@Override
	public void roll() {
		faceValue = random.nextInt(6) + 1;
	}

	@Override
	public int getFaceValue() {
		return faceValue;
	}

}
