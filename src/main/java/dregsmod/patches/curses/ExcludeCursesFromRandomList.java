package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import dregsmod.cards.curses.*;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ExcludeCursesFromRandomList {

    private static final ArrayList<String> dregsCurseIds = new ArrayList<>(Arrays.asList(
            Catastrophe.ID,
            Cower.ID,
            Death.ID,
            Doom.ID,
            Dread.ID,
            Envy.ID,
            Gloom.ID,
            Greed.ID,
            Infection.ID,
            Inquisition.ID,
            Jealousy.ID,
            Spire.ID,
            WeakPoint.ID
    ));

    /*
       match this line:
         > return (AbstractCard)cards.get(tmp.get(AbstractDungeon.cardRng.random(0, tmp.size() - 1)));
     */
    private static class BeforeReturn extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher methodCallMatcher = new Matcher.MethodCallMatcher(HashMap.class, "get");
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(), methodCallMatcher);
        }
    }

    @SpirePatch2(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {}
    )
    private static class PatchNoParamsGetCurse {
        @SpireInsertPatch(
                locator = BeforeReturn.class,
                localvars = {"tmp"}
        )
        public static void Insert(@ByRef ArrayList<String>[] tmp) {
            // tmp is an arraylist of cardids -- we want to remove dregs-specific curses
            tmp[0].removeIf(dregsCurseIds::contains);
        }
    }

    @SpirePatch2(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {AbstractCard.class, Random.class}
    )
    private static class PatchTwoParamsGetCurse {
        @SpireInsertPatch(
                locator = BeforeReturn.class,
                localvars = {"tmp"}
        )
        public static void Insert(@ByRef ArrayList<String>[] tmp) {
            // tmp is an arraylist of cardids -- we want to remove dregs-specific curses
            tmp[0].removeIf(dregsCurseIds::contains);
        }
    }

    /*
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
     */
}
