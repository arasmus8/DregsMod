package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.TriggerOnGainBlockCard;

@SpirePatch(
        clz = AbstractCreature.class,
        method = "addBlock"
)
public class TriggerOnGainBlockCardPatch {
    public static void Prefix(AbstractCreature _instance, int blockAmount) {
        if (_instance.isPlayer && blockAmount > 0) {
            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                if (c instanceof TriggerOnGainBlockCard) {
                    ((TriggerOnGainBlockCard) c).triggerOnGainBlock(blockAmount);
                }
            }
        }
    }
}
