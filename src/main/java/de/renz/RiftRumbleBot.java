package de.renz;

import com.google.gson.Gson;
import de.renz.staticdata.StaticDataCatalog;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RiftRumbleBot extends ListenerAdapter {

	static RxWebSocketClient client;
	static List<User> participants = new ArrayList<>();

	private final static int MAX_PLAYERS = 10;


	public static void main(String[] args) throws LoginException {
		/*
		if (args.length < 1) {
			System.out.println("You have to provide a token as first argument!");
			System.exit(1);
		}
		// args[0] should be the token
		// We only need 2 intents in this bot. We only respond to messages in guilds and private channels.
		// All other events will be disabled.
		JDA jda = JDABuilder.createDefault(args[0]).build();
		jda.addEventListener(new RiftRumbleBot());

		client = new RxWebSocketClient("ws://localhost:8080/rx/ws");
		*/

		StaticDataCatalog.getInstance();
	}

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message msg = event.getMessage();
		MessageChannel channel = event.getChannel();
		switch(msg.getContentRaw()){
			case "#openRiftRumble":
				channel.sendMessage("Rift Rumble initialized!") /* => RestAction<Message> */
						.queue(response /* => Message */ -> {
							response.editMessageFormat("Pong: %d ms", System.currentTimeMillis()).queue();
						});
				break;
			case "#signup":
				if (!participants.contains(msg.getAuthor()) && participants.size() < MAX_PLAYERS)
					participants.add(msg.getAuthor());

				msg.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You have been added to the participants!"));
				break;
			case "#remove":
				if (participants.contains(msg.getAuthor()))
					participants.remove(msg.getAuthor());

				msg.getAuthor().openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("You have been removed from the participants!"));
				break;
			case "#startGame":
				break;
			default:
				break;
		}
	}

	private void startGame(){

	}

	private void initializeStaticData(){

	}
}
