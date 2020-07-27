package de.renz.staticdata;

public class RuneSet extends StaticData {

	private final Rune[][] runeSlots = new Rune[4][];

	public Rune[][] getRuneSlots() {
		return runeSlots;
	}

	public int getLengthOfSlotArray(int index){
		return runeSlots[index].length;
	}
}

