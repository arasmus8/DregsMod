package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;

public class ChaosTheoryUpgradedPower extends AbstractDregsPower implements CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(ChaosTheoryUpgradedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ChaosTheoryUpgradedPower(AbstractPlayer owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        loadRegion("chaostheory");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public void atStartOfTurn() {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            flash();
            for (int i = 0; i < amount; ++i) {
                AbstractCard card = ((AbstractPlayer) owner).masterDeck.getRandomCard(AbstractDungeon.cardRandomRng);
                card = card.makeCopy();
                card.upgrade();
                addToBot(new MakeTempCardInHandAction(card, 1, false));
            }
        }
    }

    @Override
    public AbstractPower makeCopy() {
        return new ChaosTheoryUpgradedPower((AbstractPlayer) owner, amount);
    }
}
