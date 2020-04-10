package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ScryAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class AwakenRandomEffectAction extends AbstractGameAction {
    private static final int EFFECT_COUNT = 6;

    public AwakenRandomEffectAction() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractMonster m = AbstractDungeon.getRandomMonster();
        setValues(m, p);
    }

    private AbstractGameAction getEffect(int i) {
        switch (i) {
            case 0:
                return new ApplyPowerAction(source, source, new BlurPower(source, 1), 1);
            case 1:
                return new ApplyPowerAction(target, source, new ChokePower(target, 3), 3);
            case 2:
                return new ApplyPowerAction(source, source, new DrawCardNextTurnPower(source, 1), 1);
            case 3:
                return new ApplyPowerAction(source, source, new VigorPower(source, 3), 3);
            case 4:
                return new ApplyPowerAction(target, source, new PoisonPower(target, source, 3), 3);
            case 5:
                return new ApplyPowerAction(source, source, new EnergizedPower(source, 1), 1);
            case 6:
                return new ApplyPowerAction(source, source, new DuplicationPower(source, 1), 1);
            default:
                return new ScryAction(3);
        }
    }

    @Override
    public void update() {
        isDone = true;
        int effect = AbstractDungeon.miscRng.random(EFFECT_COUNT);
        addToBot(getEffect(effect));
    }
}
