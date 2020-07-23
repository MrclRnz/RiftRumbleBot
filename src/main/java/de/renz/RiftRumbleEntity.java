package de.renz;

import de.renz.staticdata.Champion;
import de.renz.staticdata.Item;
import de.renz.staticdata.RuneSet;
import net.dv8tion.jda.api.entities.User;

public class RiftRumbleEntity {

	private static final int MAX_ITEM_SLOTS = 6;

	private final User discordUser;

	private Champion champion;
	private Item[] items = new Item[MAX_ITEM_SLOTS];
	private RuneSet runeSet;

	public RiftRumbleEntity(User user){
		this.discordUser = user;
	}

	public Champion getChampion() {
		return champion;
	}

	public void setChampion(Champion champion) {
		this.champion = champion;
	}

	public Item[] getItems() {
		return items;
	}

	public RuneSet getRuneSet() {
		return runeSet;
	}

	public void setRuneSet(RuneSet runeSet) {
		this.runeSet = runeSet;
	}


}
