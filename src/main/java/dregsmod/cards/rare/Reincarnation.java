package dregsmod.cards.rare;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.defect.AnimateOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeOrbAction;
import com.megacrit.cardcrawl.actions.defect.EvokeWithoutRemovingOrbAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.cards.tempCards.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;

public class Reincarnation extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Reincarnation.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 3;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public Reincarnation() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.CANT_AWAKEN);
        baseMagicNumber = magicNumber = MAGIC;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new StrengthPower(p, magicNumber), magicNumber));
        addToBot(new MakeTempCardInHandAction(new Shiv(), magicNumber));
        if (magicNumber > 1) {
            addToBot(new AnimateOrbAction(1));
            addToBot(new EvokeWithoutRemovingOrbAction(1));
        }
        addToBot(new AnimateOrbAction(1));
        addToBot(new EvokeOrbAction(1));
        addToBot(new MakeTempCardInHandAction(new Miracle(), magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
