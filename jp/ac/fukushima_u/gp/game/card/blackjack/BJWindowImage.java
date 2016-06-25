package jp.ac.fukushima_u.gp.game.card.blackjack;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import jp.ac.fukushima_u.gp.game.card.Card;
import jp.ac.fukushima_u.gp.template.Template;

public class BJWindowImage extends BJWindow {
	void setDealerCard(int c) {
		//shdc-j
		char s;
		s = Card.getSuit(c);
		int card = c % 13 + 1;

		String t = Template.getCLASSPATH() + "jp/ac/fukushima_u/gp/materials\\cards_image\\cards_" + s;
		//		String t = "jp/ac/fukushima_u/gp/materials\\cards_image\\cards_" + s;
		//		String t = "materials\\cards_image\\cards_" + s;

		if (card < 10)
			t += "0";

		t += card + ".png";

		pd.add(new JLabel(new ImageIcon(t)));
	}

	void setUserCard(int c) {
		char s = Card.getSuit(c);
		int card = c % 13 + 1;

		String t = Template.getCLASSPATH() + "jp/ac/fukushima_u/gp/materials\\cards_image\\cards_" + s;
		//		String t = "jp/ac/fukushima_u/gp/materials\\cards_image\\cards_" + s;
		//		String t = "materials\\cards_image\\cards_" + s;

		if (card < 10)
			t += "0";

		t += card + ".png";

		pu.add(new JLabel(new ImageIcon(t)));
	}
}
