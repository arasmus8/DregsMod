package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.CardGroup;
import dregsmod.cards.curses.Jealousy;

import java.util.stream.Collectors;

@SpirePatch(
        clz = CardGroup.class,
        method = "getPurgeableCards"
)
public class PurgeableCardsPatch {
    public static CardGroup Postfix(CardGroup _result, CardGroup _instance) {
        CardGroup ret = new CardGroup(_result.type);
        ret.group.addAll(
                _result.group.stream()
                        .filter(card -> !card.cardID.equals(Jealousy.ID))
                        .collect(Collectors.toList())
        );
        return ret;
    }
}
