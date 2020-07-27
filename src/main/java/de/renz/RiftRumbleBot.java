package de.renz;

import de.renz.staticdata.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RiftRumbleBot extends ListenerAdapter {

	static RxWebSocketClient client;
	static Map<User, RiftRumbleEntity> participants = new HashMap<>();
	static User riftRumbleCreator;

	private final static int MAX_PLAYERS = 10;


	public static void main(String[] args) throws LoginException {

		// args[0] should be the token
		// We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
		// All other events will be disabled.
		JDA jda = JDABuilder.createDefault("NzM2MTMxMzQ3OTg2MzgyOTEw.XxqVvg.PtxCkduk4lCg6PTtS9T0uLsu_9w").build();
		jda.addEventListener(new RiftRumbleBot());


		/*
		client = new RxWebSocketClient("ws://localhost:8080/rx/ws");
		*/
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message msg = event.getMessage();
		MessageChannel channel = event.getChannel();
		switch (msg.getContentRaw()) {
			case "#openRiftRumble":
				participants.clear();
				riftRumbleCreator = msg.getAuthor();
				channel.sendMessage("Rift Rumble initialized!") /* => RestAction<Message> */
						.queue();
				break;
			case "#signup":
				if (!participants.containsKey(msg.getAuthor()) && participants.size() < MAX_PLAYERS)
					participants.put(msg.getAuthor(), null);

				msg.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You have been added to the participants!").queue());
				break;
			case "#remove":
				if (participants.containsKey(msg.getAuthor()))
					participants.remove(msg.getAuthor());

				msg.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You have been removed from the participants!").queue());
				break;
			case "#startGame":
				createEntities();
				sendEntityMessage();
				break;
			default:
				break;
		}
	}

	private void sendEntityMessage(){
		for(Map.Entry<User, RiftRumbleEntity> entry : participants.entrySet()){
			if(entry.getValue() != null){
				EmbedBuilder builder = new EmbedBuilder();

				builder.setTitle("Rift Rumble Build");
				builder.addField("Champion", entry.getValue().getChampion().getName(), false);
				builder.addField("Runes", entry.getValue().getRunesString(), false);
				builder.addField("Item Build", entry.getValue().getItemsString(), false);
				builder.addField("Summoner Spells", entry.getValue().getSummonersString(), false);
				builder.addField("Skill Order", entry.getValue().getSkillOrderString(), false);
				entry.getKey().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage(builder.build()).queue());
			}
		}
	}

	private void createEntities() {
		for (Map.Entry<User, RiftRumbleEntity> entry : participants.entrySet()){
			RiftRumbleEntity entity = new RiftRumbleEntity(entry.getKey());
			entity.setChampion((Champion) StaticDataCatalog.getInstance().getRandomStaticData(Champion.class));

			entity.getItems()[0] = (Item) StaticDataCatalog.getInstance().getBoots();

			for(int i = 1; i < entity.getItems().length; i++){
				entity.getItems()[i] = (Item) StaticDataCatalog.getInstance().getRandomStaticData(Item.class,
						Arrays.asList(entity.getItems()));
			}

			for (int i = 0; i < entity.getRuneSet().length; i++){
				entity.getRuneSet()[i] = (RuneSet) StaticDataCatalog.getInstance().getRandomStaticData(RuneSet.class,
						Arrays.asList(entity.getRuneSet()));
			}

			for (int i = 0; i < entity.getSpells().length; i++){
				if(entity.containsJungleItem() && i == 0){
					entity.getSpells()[0] = StaticDataCatalog.getInstance().getSmite();
				}
				entity.getSpells()[i] = (SummonerSpell) StaticDataCatalog.getInstance().getRandomStaticData(SummonerSpell.class,
						Arrays.asList(entity.getSpells()));
			}

			participants.put(entry.getKey(), entity);
		}
	}

}
