package dregsmod.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import dregsmod.relics.FracturedPrism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getRewardCards"
)
public class FracturedPrismPatches {
    public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> retVal) {
        FracturedPrism f = (FracturedPrism) AbstractDungeon.player.getRelic(FracturedPrism.ID);
        if (f != null && f.counter > 0) {
            f.counter -= 1;
            if (retVal.stream().noneMatch(c -> c.cardID.equals(f.cardId))) {
                Collections.shuffle(retVal, AbstractDungeon.cardRng.random);
                ArrayList<AbstractCard> altered = retVal.stream()
                        .limit(retVal.size() - 1)
                        .collect(Collectors.toCollection(ArrayList::new));
                AbstractCard toAdd = CardLibrary.getCard(f.cardId).makeCopy();
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    r.onPreviewObtainCard(toAdd);
                }
                altered.add(toAdd);
                Collections.shuffle(altered, AbstractDungeon.cardRng.random);
                return altered;
            }
        }
        return retVal;
    }
}
