package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dregsmod.powers.CursedPower;
import dregsmod.powers.FortitudePower;
import dregsmod.powers.MightPower;
import dregsmod.powers.SturdinessPower;

public class JinxAction extends AbstractGameAction {
    private AbstractCreature m;
    private AbstractPlayer p;
    private int energyOnUse;
    private boolean freeToPlayOnce;

    public JinxAction(AbstractCreature target, int amount, boolean freeToPlayOnce, int energyOnUse) {
        m = target;
        p = AbstractDungeon.player;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        setValues(m, p, amount);
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

        effect += amount;

        addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, effect, false), effect));
        addToBot(new ApplyPowerAction(m, p, new WeakPower(m, effect, false), effect));
        addToBot(new ApplyPowerAction(m, p, new CursedPower(m, effect), effect));

        if (!freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

        isDone = true;
    }
}
