package dregsmod;

import com.megacrit.cardcrawl.cards.AbstractCard;
import dregsmod.cards.*;
import dregsmod.cards.common.Curate;
import dregsmod.cards.common.CursedNeedle;
import dregsmod.cards.common.Gash;
import dregsmod.cards.rare.ChaosTheory;
import dregsmod.cards.rare.Entropy;
import dregsmod.cards.rare.LastHope;
import dregsmod.cards.uncommon.Asylum;
import dregsmod.cards.uncommon.CursedScroll;
import dregsmod.cards.uncommon.Fear;

import java.util.ArrayList;

public class CardList {
    public static final ArrayList<AbstractCard> allCards;

    static {
        allCards = new ArrayList<>();
        // Starting Cards
        allCards.add(new DregsDefend());
        allCards.add(new DregsStrike());
        allCards.add(new Adaptability());
        allCards.add(new Jealousy());
        allCards.add(new LashOut());

        // Special Cards

        // Common Attacks
        allCards.add(new CursedNeedle());
        allCards.add(new Gash());

        //Common Skills
        allCards.add(new Curate());

        //Uncommon Attacks
        allCards.add(new Fear());

        //Uncommon Skills
        allCards.add(new CursedScroll());

        //Uncommon Powers
        allCards.add(new Asylum());

        //Rare Attacks
        allCards.add(new LastHope());

        //Rare Skills
        allCards.add(new Entropy());

        //Rare Powers
        allCards.add(new ChaosTheory());
    }
}
