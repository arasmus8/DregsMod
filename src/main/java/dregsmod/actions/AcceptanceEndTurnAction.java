package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.stances.AcceptanceStance;

public class AcceptanceEndTurnAction extends AbstractGameAction {
    public AcceptanceEndTurnAction() {
        duration = startDuration = Settings.ACTION_DUR_XFAST;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            isDone = true;
            AbstractPlayer p = AbstractDungeon.player;
            if (p.stance.ID.equals(AcceptanceStance.STANCE_ID)) {
                AcceptanceStance.adjustAttackCosts(1);
            }
        }
    }
}
