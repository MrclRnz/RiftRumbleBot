package de.renz.staticdata;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.*;

public class StaticDataCatalog {

	private static final StaticDataCatalog INSTANCE = new StaticDataCatalog();
	private static final String DATA_DRAGON_VERSION = "9.3.1";
	private static final String SUMMONERS_RIFT_ID = "11";

	private final List<StaticData> staticDataList;

	private final Gson gson = new Gson();

	private StaticDataCatalog() {
		staticDataList = new ArrayList<>();
		initializeStaticData();
	}

	public static StaticDataCatalog getInstance() {
		return INSTANCE;
	}

	private void initializeStaticData() {
		initializeChampions();
		initializeItems();
		initializeRunes();
		initializeSummoners();
	}

	private void initializeChampions() {
		String championResourcePath = DATA_DRAGON_VERSION + "/data/en_US/champion/";
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(championResourcePath);
			 BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String resource;

			while ((resource = br.readLine()) != null) {
				JsonElement root = JsonParser.parseReader(new InputStreamReader(
						getClass().getClassLoader().getResourceAsStream(championResourcePath + resource)));
				JsonObject object = root.getAsJsonObject();
				JsonObject data = object.getAsJsonObject("data");
				JsonObject champion = data.getAsJsonObject(data.keySet().iterator().next());
				Champion champ = new Champion();
				champ.setId(champion.getAsJsonPrimitive("id").getAsString());
				champ.setName(champion.getAsJsonPrimitive("name").getAsString());
				champ.setImage(champion.getAsJsonObject("image").getAsJsonPrimitive("full").getAsString());

				staticDataList.add(champ);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initializeItems() {
		String itemResourcePath = DATA_DRAGON_VERSION + "/data/en_US/item.json";

		JsonElement root = JsonParser.parseReader(new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream(itemResourcePath)));
		JsonObject object = root.getAsJsonObject();
		JsonObject data = object.getAsJsonObject("data");
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			String id = it.next();
			JsonObject item = data.getAsJsonObject(id);
			if (isEligibleItem(item)) {
				Item itm = new Item();
				itm.setId(id);
				itm.setName(item.getAsJsonPrimitive("name").getAsString());
				itm.setImage(item.getAsJsonObject("image").getAsJsonPrimitive("full").getAsString());

				staticDataList.add(itm);
			}
		}
	}


	private void initializeRunes() {
		String runesResourcePath = DATA_DRAGON_VERSION + "/data/en_US/runesReforged.json";

		JsonElement root = JsonParser.parseReader(new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream(runesResourcePath)));
		Iterator<JsonElement> rootIterator = root.getAsJsonArray().iterator();

		while (rootIterator.hasNext()) {
			JsonObject runeSet = rootIterator.next().getAsJsonObject();
			RuneSet rnSet = new RuneSet();
			rnSet.setId(runeSet.getAsJsonPrimitive("id").getAsString());
			rnSet.setName(runeSet.getAsJsonPrimitive("name").getAsString());
			rnSet.setImage(runeSet.getAsJsonPrimitive("icon").getAsString());
			int slotIndex = 0;
			Iterator<JsonElement> runeSlotIterator = runeSet.getAsJsonArray("slots").iterator();
			while (runeSlotIterator.hasNext()) {
				JsonObject runeSlot = runeSlotIterator.next().getAsJsonObject();
				Iterator<JsonElement> runeIterator = runeSlot.getAsJsonArray("runes").iterator();
				int runeIndex = 0;
				while (runeIterator.hasNext()) {
					JsonObject rune = runeIterator.next().getAsJsonObject();
					Rune rn = new Rune();
					rn.setId(rune.getAsJsonPrimitive("id").getAsString());
					rn.setName(rune.getAsJsonPrimitive("name").getAsString());
					rn.setImage(rune.getAsJsonPrimitive("icon").getAsString());

					if (rnSet.getRuneSlots()[slotIndex] == null) {
						rnSet.getRuneSlots()[slotIndex] = new Rune[runeSlot.getAsJsonArray("runes").size()];
					}
					rnSet.getRuneSlots()[slotIndex][runeIndex] = rn;

					runeIndex++;
				}

				slotIndex++;
			}
			staticDataList.add(rnSet);
		}
	}

	private void initializeSummoners() {
		String summonersResourcePath = DATA_DRAGON_VERSION + "/data/en_US/summoner.json";

		JsonElement root = JsonParser.parseReader(new InputStreamReader(
				getClass().getClassLoader().getResourceAsStream(summonersResourcePath)));
		JsonObject object = root.getAsJsonObject();
		JsonObject data = object.getAsJsonObject("data");
		Iterator<String> it = data.keySet().iterator();
		while (it.hasNext()) {
			JsonObject summonerSpell = data.getAsJsonObject(it.next());

			if (summonerSpell.getAsJsonArray("modes").toString().contains("CLASSIC")) {
				SummonerSpell summoner = new SummonerSpell();
				summoner.setId(summonerSpell.getAsJsonPrimitive("id").getAsString());
				summoner.setName(summonerSpell.getAsJsonPrimitive("name").getAsString());
				summoner.setImage(summonerSpell.getAsJsonObject("image").getAsJsonPrimitive("full").getAsString());

				staticDataList.add(summoner);
			}
		}
	}

	private boolean isEligibleItem(JsonObject item) {
		if (item.getAsJsonArray("into") == null || item.getAsJsonArray("into").size() == 0) {
			if (item.getAsJsonObject("gold").getAsJsonPrimitive("purchasable").getAsBoolean()) {
				if (item.getAsJsonObject("gold").getAsJsonPrimitive("total").getAsInt() > 1000) {
					if (item.getAsJsonObject("maps").getAsJsonPrimitive(SUMMONERS_RIFT_ID).getAsBoolean()) {
						if (!item.getAsJsonArray("tags").toString().contains("Consumable")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public StaticData getRandomStaticData(Class<StaticData> clazz, List<StaticData> notAllowedStaticData){
		return staticDataList.stream()
				.filter(staticData -> clazz.isInstance(staticData) && !notAllowedStaticData.contains(staticData))
				.skip(staticDataList.stream()
						.filter(staticData -> clazz.isInstance(staticData) && !notAllowedStaticData.contains(staticData))
						.count())
				.findFirst()
				.get();
	}

	public StaticData getRandomStaticData(Class<StaticData> clazz){
		return staticDataList.stream()
				.filter(staticData -> clazz.isInstance(staticData))
				.skip(staticDataList.stream()
						.filter(staticData -> clazz.isInstance(staticData))
						.count())
				.findFirst()
				.get();
	}

}
