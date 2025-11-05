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

@SuppressWarnings({"unused"})
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
            return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), methodCallMatcher);
        }
    }

    @SuppressWarnings({"unused"})
    @SpirePatch2(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {}
    )
    private static class PatchNoParamsGetCurse {
        @SuppressWarnings({"unused"})
        @SpireInsertPatch(
                locator = BeforeReturn.class,
                localvars = {"tmp"}
        )
        public static void Insert(@ByRef ArrayList<String>[] tmp) {
            // tmp is an arraylist of cardids -- we want to remove dregs-specific curses
            tmp[0].removeIf(dregsCurseIds::contains);
        }
    }

    @SuppressWarnings({"unused"})
    @SpirePatch2(
            clz = CardLibrary.class,
            method = "getCurse",
            paramtypez = {AbstractCard.class, Random.class}
    )
    private static class PatchTwoParamsGetCurse {
        @SuppressWarnings({"unused"})
        @SpireInsertPatch(
                locator = BeforeReturn.class,
                localvars = {"tmp"}
        )
        public static void Insert(@ByRef ArrayList<String>[] tmp) {
            // tmp is an arraylist of cardids -- we want to remove dregs-specific curses
            tmp[0].removeIf(dregsCurseIds::contains);
        }
    }

}
