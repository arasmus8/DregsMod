package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class GashAction extends AbstractGameAction {
    private AbstractCard c;

    public GashAction(AbstractCard card, AbstractPlayer player, AbstractMonster target, int damage) {
        c = card;
        setValues(target, player, damage);
        duration = Settings.ACTION_DUR_FAST;
        actionType = ActionType.DAMAGE;
    }

    @Override
    public void update() {
        if (duration == Settings.ACTION_DUR_FAST) {
            isDone = true;
            c.applyPowers();

            addToBot(new DamageAction(target, new DamageInfo(source, c.damage, c.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
            addToBot(new DamageAction(target, new DamageInfo(source, c.damage, c.damageTypeForTurn), AbstractGameAction.AttackEffect.NONE));
        }
    }
}
