package dregsmod.patches.curses;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import dregsmod.cards.AbstractCleansingCurse;

@SpirePatch(
        clz = CardLibrary.class,
        method = "getCopy",
        paramtypez = {String.class, int.class, int.class}
)
public class CleansingCursePatch {
    public static AbstractCard Postfix(AbstractCard _return, String id, int upgrades, int misc) {
        if (_return instanceof AbstractCleansingCurse && misc != 0) {
            _return.baseMagicNumber = _return.magicNumber = ((AbstractCleansingCurse) _return).cleanseAmount - misc;
            _return.initializeDescription();
        }

        return _return;
    }
}
