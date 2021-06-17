package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import dregsmod.powers.BlightedPower;

public class RuinAction extends AbstractGameAction {
    private static final float DURATION = 0.1f;
    private final AbstractMonster m;
    private final DamageInfo info;

    public RuinAction(AbstractMonster target, DamageInfo damageInfo) {
        this.duration = DURATION;
        this.actionType = ActionType.DAMAGE;
        setValues(target, damageInfo);
        this.m = target;
        this.info = damageInfo;
    }

    @Override
    public void update() {
        if (this.target == null) {
            this.isDone = true;
        } else {
            if (this.m.hasPower(BlightedPower.POWER_ID)) {
                if (this.duration == DURATION && this.target != null && this.target.currentHealth > 0) {
                    if (this.info.type != DamageInfo.DamageType.THORNS && this.info.owner.isDying) {
                        this.isDone = true;
                        return;
                    }

                    AbstractDungeon.effectList.add(new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.SLASH_HORIZONTAL));
                }

                this.tickDuration();
                if (this.isDone && this.target != null && this.target.currentHealth > 0) {
                    this.target.damage(this.info);
                    if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                        AbstractDungeon.actionManager.clearPostCombatActions();
                    }

                    this.addToTop(new WaitAction(0.1F));
                }
            } else {
                this.isDone = true;
            }

        }
    }
}
