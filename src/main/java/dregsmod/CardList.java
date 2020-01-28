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
        allCards.add(new Misery());
        allCards.add(new Plague());
        allCards.add(new Quarantine());
        allCards.add(new RitualJar());
        allCards.add(new ShallowGrave());

        //Uncommon Attacks
        allCards.add(new Fear());
        allCards.add(new Malediction());
        allCards.add(new Misfortune());
        allCards.add(new RiteOfPurification());
        allCards.add(new Unlucky());

        //Uncommon Skills
        allCards.add(new Ascetic());
        allCards.add(new CursedScroll());
        allCards.add(new EmbraceDarkness());
        allCards.add(new Glare());
        allCards.add(new Guardian());
        allCards.add(new HardLuck());
        allCards.add(new Jinx());
        allCards.add(new MidnightManacles());
        allCards.add(new Obstruct());
        allCards.add(new Pinprick());
        allCards.add(new Sarcophagus());

        //Uncommon Powers
        allCards.add(new Asylum());
        allCards.add(new Defiance());
        allCards.add(new Excess());

        //Rare Attacks
        allCards.add(new LastHope());
        allCards.add(new MeanLook());
        allCards.add(new PowerUp());
        allCards.add(new Redemption());

        //Rare Skills
        allCards.add(new Entropy());
        allCards.add(new Indestructible());
        allCards.add(new Overcome());
        allCards.add(new Ritual());
        allCards.add(new SeeNoEvil());
        allCards.add(new Twilight());

        //Rare Powers
        allCards.add(new ChaosTheory());
        allCards.add(new EclipseForm());
        allCards.add(new Incantation());
        allCards.add(new Retribution());
    }
}
