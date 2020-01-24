package dregsmod;

import com.megacrit.cardcrawl.cards.AbstractCard;
import dregsmod.cards.Adaptability;
import dregsmod.cards.DregsDefend;
import dregsmod.cards.DregsStrike;
import dregsmod.cards.LashOut;
import dregsmod.cards.common.*;
import dregsmod.cards.curses.Catastrophe;
import dregsmod.cards.curses.Doom;
import dregsmod.cards.curses.Gloom;
import dregsmod.cards.curses.Jealousy;
import dregsmod.cards.rare.*;
import dregsmod.cards.uncommon.*;

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

        // Curses
        allCards.add(new Catastrophe());
        allCards.add(new Doom());
        allCards.add(new Gloom());

        // Special Cards

        // Common Attacks
        allCards.add(new CursedNeedle());
        allCards.add(new Gash());
        allCards.add(new IcyTouch());
        allCards.add(new Improvise());
        allCards.add(new Ruin());
        allCards.add(new SharpShadows());

        //Common Skills
        allCards.add(new Curate());
        allCards.add(new Plague());
        allCards.add(new Quarantine());
        allCards.add(new RitualJar());
        allCards.add(new ShallowGrave());

        //Uncommon Attacks
        allCards.add(new Fear());

        //Uncommon Skills
        allCards.add(new CursedScroll());
        allCards.add(new Glare());
        allCards.add(new Sarcophagus());

        //Uncommon Powers
        allCards.add(new Asylum());

        //Rare Attacks
        allCards.add(new LastHope());

        //Rare Skills
        allCards.add(new Entropy());
        allCards.add(new Indestructible());
        allCards.add(new MeanLook());
        allCards.add(new Overcome());

        //Rare Powers
        allCards.add(new ChaosTheory());
        allCards.add(new Incantation());
    }
}
