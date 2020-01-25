package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.powers.EclipseFormPower;

@SpirePatch(
        clz = AbstractCard.class,
        method = "canUse"
)
public class PlayCursesEclipseFormPatch {
    public static boolean Postfix(boolean _return, AbstractCard _instance, AbstractPlayer p, AbstractMonster m) {
        if(!_return) {
            if (_instance.type == AbstractCard.CardType.CURSE && _instance.costForTurn < -1 && p.hasPower(EclipseFormPower.POWER_ID)) {
                return true;
            }
        }
        return _return;
    }
}
