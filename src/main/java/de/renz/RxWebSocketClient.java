package de.renz;

import javax.websocket.*;
import java.net.URI;

public class RxWebSocketClient {
	private Session session;

	public RxWebSocketClient(final String uri) {
		try {
			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			container.connectToServer(this, new URI(uri));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@OnOpen
	public void onOpen(Session userSession) {
		System.out.println("opening websocket");
		this.session = userSession;
	}

	@OnClose
	public void onClose(Session userSession, CloseReason reason) {
		System.out.println("closing websocket");
		this.session = null;
	}

	@OnMessage
	public void onMessage(String message) {
		System.out.println(message);
	}

	public Session getSession() {
		return session;
	}

}
