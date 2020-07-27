package de.renz;

import de.renz.staticdata.*;
import net.dv8tion.jda.api.entities.User;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class RiftRumbleEntity {

	private static final int MAX_ITEM_SLOTS = 6;

	private final User discordUser;

	private Champion champion;
	private Item[] items = new Item[MAX_ITEM_SLOTS];
	private RuneSet[] runeSet = new RuneSet[2];
	private SummonerSpell[] spells = new SummonerSpell[2];
	private List<String> skillOrder = new ArrayList<String>();

	public RiftRumbleEntity(User user) {
		this.discordUser = user;
		skillOrder.add("Q");
		skillOrder.add("W");
		skillOrder.add("E");
		Collections.shuffle(skillOrder);
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

	public RuneSet[] getRuneSet() {
		return runeSet;
	}

	public SummonerSpell[] getSpells() {
		return spells;
	}

	public boolean containsJungleItem(){
		for (Item item : items){
			if(item.getName().contains("Enchantment"))
				return true;
		}

		return false;
	}

	public String getItemsString(){
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < items.length; i++){
			if (i != 0)
				builder.append(" / ");
			builder.append(items[i].getName());
		}

		return builder.toString();
	}

	public String getRunesString(){
		StringBuilder builder = new StringBuilder();

		builder.append("Main Tree: ");
		builder.append(runeSet[0].getName());
		builder.append(System.lineSeparator());

		for(int i = 0; i < 4; i++){
			if(i != 0)
				builder.append(" / ");
			builder.append(runeSet[0].getRuneSlots()[i][new Random().nextInt(runeSet[0].getLengthOfSlotArray(i))].getName());
		}

		builder.append(System.lineSeparator());
		builder.append("Secondary Tree: ");
		builder.append(runeSet[1].getName());
		builder.append(System.lineSeparator());

		int firstRow = ThreadLocalRandom.current().nextInt(1, 3);
		int secondRow = ThreadLocalRandom.current().nextInt(1, 3);
		while(firstRow == secondRow){
			secondRow = ThreadLocalRandom.current().nextInt(1, 3);
		}

		builder.append(runeSet[1].getRuneSlots()[firstRow][new Random().nextInt(runeSet[0].getLengthOfSlotArray(firstRow))].getName());
		builder.append(" / ");
		builder.append(runeSet[1].getRuneSlots()[secondRow][new Random().nextInt(runeSet[0].getLengthOfSlotArray(secondRow))].getName());
		return builder.toString();
	}

	public String getSummonersString(){
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < spells.length; i++){
			if (i != 0)
				builder.append(" / ");
			builder.append(spells[i].getName());
		}

		return builder.toString();
	}

	public String getSkillOrderString(){
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < skillOrder.size(); i++){
			if (i != 0)
				builder.append(" -> ");
			builder.append(skillOrder.get(i));
		}

		return builder.toString();
	}
}
