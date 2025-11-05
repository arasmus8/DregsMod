package dregsmod;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.curses.*;

import java.util.ArrayList;
import java.util.Arrays;

public class CleanseCurseLibrary {
    private static final ArrayList<AbstractCard> cleanseCurses = new ArrayList<>(Arrays.asList(
            new Catastrophe(),
            new Cower(),
            new Death(),
            new Doom(),
            new Dread(),
            new Envy(),
            new Gloom(),
            new Greed(),
            new Infection(),
            new Inquisition(),
            new Jealousy(),
            new Spire(),
            new WeakPoint()
    ));

    public static AbstractCard getRandomCleanseCurse() {
        AbstractCard randomChoice = cleanseCurses.get(AbstractDungeon.cardRng.random(0, cleanseCurses.size() - 1));
        return randomChoice.makeCopy();
    }
}
