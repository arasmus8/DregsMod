package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dregsmod.powers.FortitudePower;
import dregsmod.powers.MightPower;
import dregsmod.powers.SturdinessPower;

public class OvercomeAction extends AbstractGameAction {
    private final AbstractPlayer p;
    private final int energyOnUse;
    private final boolean freeToPlayOnce;
    private final boolean upgraded;

    public OvercomeAction(boolean freeToPlayOnce, int energyOnUse, boolean upgraded) {
        p = AbstractDungeon.player;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        this.upgraded = upgraded;
        setValues(p, p);
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }

        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        if(effect > 0) {
            addToBot(new ApplyPowerAction(p, p, new FortitudePower(p, effect), effect));
            addToBot(new ApplyPowerAction(p, p, new MightPower(p, effect), effect));
            addToBot(new ApplyPowerAction(p, p, new SturdinessPower(p, effect), effect));
            if (upgraded) {
                addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, effect), effect));
            }
        }

        if (!freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

        isDone = true;
    }
}
