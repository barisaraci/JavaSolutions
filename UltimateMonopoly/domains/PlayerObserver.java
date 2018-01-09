package monopoly.domains;

import monopoly.domains.entities.Player;
import monopoly.ui.BotUI;

public class PlayerObserver extends Thread implements Observer {
	
	private Player player;
	private BotUI botUI;
	private String state;
	
	public PlayerObserver(Player player, BotUI botUI) {
		this.player = player;
		this.botUI = botUI;
		player.attach(this);
	}
	
	public void update(String state) {
		this.state = state;
		updateView();
	}

	@Override
	public void updateView() {
		botUI.updateView(state, player.getId());
	}

}
