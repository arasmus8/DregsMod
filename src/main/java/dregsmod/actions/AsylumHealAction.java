package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class AsylumHealAction extends AbstractGameAction {
    public AsylumHealAction() {
        setValues(AbstractDungeon.player, AbstractDungeon.player);
        duration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        int healAmount = 0;
        for(AbstractCard card : SealAndPerformAction.sealedCards) {
            int cost = card.costForTurn;
            if(cost > 0) {
                healAmount += card.costForTurn;
            } else if(cost == -1) {
                healAmount += EnergyPanel.totalCount;
            }
        }
        if(healAmount > 0) {
            addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, healAmount));
        }
        isDone = true;
    }
}
