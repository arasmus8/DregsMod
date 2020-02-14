package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class MaledictionDamageAction extends AbstractGameAction {
    private AbstractCreature target;
    private DamageInfo info;

    public MaledictionDamageAction(AbstractCreature target, DamageInfo info) {
        setValues(target, info);
        this.target = target;
        this.info = info;
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        for(AbstractCard card : SealAndPerformAction.sealedCards) {
            if(card != null) {
                addToTop(new DamageAction(target, info, AttackEffect.FIRE));
                AbstractDungeon.player.hand.refreshHandLayout();
            }
        }
        SealAndPerformAction.sealedCards.clear();
        isDone = true;
    }
}
