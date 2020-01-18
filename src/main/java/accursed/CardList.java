package accursed;

import accursed.cards.AccursedDefend;
import accursed.cards.AccursedStrike;
import accursed.cards.Adaptability;
import accursed.cards.Jealousy;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class CardList {
    public static final ArrayList<AbstractCard> allCards;

    static {
        allCards = new ArrayList<>();
        // Starting Cards
        allCards.add(new AccursedDefend());
        allCards.add(new AccursedStrike());
        allCards.add(new Adaptability());
        allCards.add(new Jealousy());

        // Special Cards

        // Common Attacks

        //Common Skills

        //Uncommon Attacks

        //Uncommon Skills

        //Uncommon Powers

        //Rare Attacks

        //Rare Skills

        //Rare Powers
    }
}
