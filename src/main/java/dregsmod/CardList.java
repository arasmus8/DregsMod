package dregsmod;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.*;
import dregsmod.cards.common.*;
import dregsmod.cards.curses.*;
import dregsmod.cards.rare.*;
import dregsmod.cards.uncommon.*;
import dregsmod.patches.enums.CustomCardTags;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CardList {
    public static final ArrayList<AbstractCard> allCards;

    public static AbstractCard getRandomCleanseCurse() {
        ArrayList<AbstractCard> cleanseCurses = allCards.stream()
                .filter(card -> card.hasTag(CustomCardTags.CLEANSE_CURSE))
                .collect(Collectors.toCollection(ArrayList::new));
        return cleanseCurses.get(AbstractDungeon.cardRng.random(0, cleanseCurses.size() - 1));
    }

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
        allCards.add(new Death());
        allCards.add(new Tower());
        allCards.add(new Judgement());
        allCards.add(new Dread());
        allCards.add(new Cower());
        allCards.add(new Greed());
        allCards.add(new Envy());
        allCards.add(new Infection());
        allCards.add(new WeakPoint());

        // Special Cards
        allCards.add(new CleansedCurse());

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
        allCards.add(new SealedEnergy());
        allCards.add(new SealingWish());
        allCards.add(new Unlucky());
        allCards.add(new WaveOfDarkness());

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
        allCards.add(new Torment());

        //Uncommon Powers
        allCards.add(new Asylum());
        allCards.add(new Defiance());
        allCards.add(new Excess());
        allCards.add(new Scapegoat());
        allCards.add(new ShiftingSands());
        allCards.add(new TheVault());

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
