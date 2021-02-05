package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;
import dregsmod.powers.FortitudePower;

public class EmbraceDarkness extends AbstractCurseHoldingCard {
    public static final String ID = DregsMod.makeID(EmbraceDarkness.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 2;
    private static final int UPGRADED_MAGIC = 1;

    public EmbraceDarkness() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = MAGIC;
        magicNumber = baseMagicNumber;
        tags.add(DregsCardTags.CANT_AWAKEN);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (holdingCurse) {
            showEvokeValue = true;
            showEvokeOrbCount = 1;
        } else {
            showEvokeValue = false;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new FortitudePower(p, magicNumber), magicNumber));
        if (holdingCurse) {
            addToBot(new ChannelAction(new Dark()));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_MAGIC);
            initializeDescription();
        }
    }
}
