package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCleansingCurse;

public class Infection extends AbstractCleansingCurse {
    public static final String ID = DregsMod.makeID(Infection.class.getSimpleName());

    private static final CardTarget TARGET = CardTarget.SELF;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = 1;
    private static final int CLEANSE_AMOUNT = 6;

    public Infection() {
        super(ID, COST, TARGET, CLEANSE_AMOUNT);
    }

    @Override
    public void triggerOnEndOfTurnForPlayingCard() {
        dontTriggerOnUseCard = true;
        AbstractDungeon.actionManager.cardQueue.add(new CardQueueItem(this, true));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (dontTriggerOnUseCard) {
            addToBot(new VFXAction(new ShowCardAndAddToDiscardEffect(this.makeSameInstanceOf())));
        } else {
            exhaust = true;
            addToBot(new DamageAction(p, new DamageInfo(p, 5, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON));
            cleanseBy(1);
        }
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }
}