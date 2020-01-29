package dregsmod.patches.cards;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.cards.TriggerOnMonsterDeathCard;

@SpirePatch(
        clz = AbstractMonster.class,
        method = "die",
        paramtypez = { boolean.class }
)
public class TriggerOnMonsterDeathCardPatch {
    public static void Prefix(AbstractMonster _instance, boolean triggerRelics) {
        if(!_instance.isDying && triggerRelics) {
            for(AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card instanceof TriggerOnMonsterDeathCard) {
                    ((TriggerOnMonsterDeathCard) card).triggerOnMonsterDeath(_instance);
                }
            }
        }
    }
}
