package monopoly.ui;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import monopoly.MonopolyGame;

public class BotUI {
	
	private ArrayList<EmojiPanel> panels = new ArrayList<>();
	
	public BotUI() {
		
	}
	
	public void updateView(String state, int id) {
		panels.get(id).setAnimActive();
		if (state.equals(MonopolyGame.EMOJI_DEFAULT)) {
			panels.get(id).getLabel().setText("<html><div style='text-align: center; font-size: 16; color: #A7A735'>" + state + "</div></html>");
		} else if (state.equals(MonopolyGame.EMOJI_BANKRUPT)) {
			panels.get(id).getLabel().setText("<html><div style='text-align: center; font-size: 16; color: #FD0404'>" + state + "</div></html>");
		} else if (state.equals(MonopolyGame.EMOJI_AFK)) {
			panels.get(id).getLabel().setText("<html><div style='text-align: center; font-size: 16; color: #F87118'>" + state + "</div></html>");
		} else if (state.equals(MonopolyGame.EMOJI_GAIN)) {
			panels.get(id).getLabel().setText("<html><div style='text-align: center; font-size: 16; color: #2FC30F'>" + state + "</div></html>");
		} else if (state.equals(MonopolyGame.EMOJI_RENT)) {
			panels.get(id).getLabel().setText("<html><div style='text-align: center; font-size: 16; color: #E72A2A'>" + state + "</div></html>");
		} else if (state.equals(MonopolyGame.EMOJI_WIN)) {
			panels.get(id).getLabel().setText("<html><div style='text-align: center; font-size: 16; color: #2FED05'>" + state + "</div></html>");
		} else if (state.equals(MonopolyGame.EMOJI_JAIL)) {
			panels.get(id).getLabel().setText("<html><div style='text-align: center; font-size: 16; color: #DD1414'>" + state + "</div></html>");
		}
	}
	
	public void attachPanel(EmojiPanel panel) {
		panels.add(panel);
	}

}
