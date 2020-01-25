package dregsmod.patches.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import dregsmod.cards.uncommon.Pinprick;

@SpirePatch(
        clz = MarkPower.class,
        method = "triggerMarks"
)
public class TriggerMarksPatch {
    public static void Postfix(MarkPower _instance, AbstractCard card) {
        if(card.cardID.equals(Pinprick.ID)) {
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(
                    _instance.owner,
                    null,
                    _instance.amount,
                    AbstractGameAction.AttackEffect.FIRE));
        }
    }
}
