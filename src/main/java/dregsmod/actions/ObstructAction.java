package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.ThoughtBubble;
import dregsmod.powers.SturdinessPower;

public class ObstructAction extends AbstractGameAction {

    private static final UIStrings uiStrings;
    public static final String[] TEXT;
    private final AbstractPlayer p;
    private final AbstractMonster m;

    public ObstructAction(AbstractPlayer player, AbstractMonster target, int amount) {
        duration = 0.0F;
        this.p = player;
        this.m = target;
        setValues(target, player, amount);
        actionType = ActionType.WAIT;
    }

    @Override
    public void update() {
        if (m != null && m.getIntentBaseDmg() >= 0) {
            addToBot(new ApplyPowerAction(m, p, new WeakPower(m, 2, false), 2));
            addToBot(new ApplyPowerAction(p, p, new SturdinessPower(p, amount), amount));
        } else {
            AbstractDungeon.effectList.add(new ThoughtBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 3.0F, TEXT[0], true));
        }

        isDone = true;
    }

    static {
        uiStrings = CardCrawlGame.languagePack.getUIString("OpeningAction");
        TEXT = uiStrings.TEXT;
    }
}
