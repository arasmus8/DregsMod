package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import dregsmod.CardList;
import dregsmod.cards.curses.Catastrophe;
import dregsmod.cards.curses.Doom;
import dregsmod.cards.curses.Gloom;
import dregsmod.cards.curses.Jealousy;

public class ExcludeCursesFromRandomList {

    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {}
    )
    public static class WithNoParams {
        public static AbstractCard Postfix(AbstractCard _returned) {
            if (
                    _returned.cardID.equals(Jealousy.ID) ||
                            _returned.cardID.equals(Catastrophe.ID) ||
                            _returned.cardID.equals(Doom.ID) ||
                            _returned.cardID.equals(Gloom.ID)
            ) {
                return CardList.getRandomCleanseCurse();
            }
            return _returned;
        }
    }

    @SpirePatch(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {AbstractCard.class, Random.class}
    )
    public static class WithParams {
        public static AbstractCard Postfix(AbstractCard _returned, AbstractCard prohibitedCard, Random rng) {
            if (
                    _returned.cardID.equals(Jealousy.ID) ||
                            _returned.cardID.equals(Catastrophe.ID) ||
                            _returned.cardID.equals(Doom.ID) ||
                            _returned.cardID.equals(Gloom.ID)
            ) {
                return CardList.getRandomCleanseCurse();
            }
            return _returned;
        }
    }
}
