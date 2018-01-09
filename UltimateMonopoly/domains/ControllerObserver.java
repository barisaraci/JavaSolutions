package monopoly.domains;

import monopoly.ui.BotUI;

public class ControllerObserver extends Thread implements Observer {

	private BotUI botUI;
	
	private String state;
	private int id;
	
	public ControllerObserver(Controller controller, BotUI botUI) {
		this.botUI = botUI;
		controller.attach(this);
	}
	
	public void update(String state, int id) {
		this.state = state;
		this.id = id;
		updateView();
	}

	@Override
	public void updateView() {
		botUI.updateView(state, id);
	}

}
