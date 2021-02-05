package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractSealedCard;
import dregsmod.cards.AwakenedMod;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;

import java.util.Optional;

public class Torment extends AbstractSealedCard {
    public static final String ID = DregsMod.makeID(Torment.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = -2;

    private static final int MAGIC = 1;
    private static final int UPGRADE_PLUS_MAGIC = 1;

    public Torment() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET, DregsCardTags.CANT_AWAKEN);
        baseMagicNumber = magicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void triggerWhileSealed(AbstractPlayer p) {
        addToBot(new GainEnergyAction(magicNumber));
        Optional<AwakenedMod> awakenedMod = AwakenedMod.getForCard(this);
        awakenedMod.ifPresent(mod -> mod.onUse(this, p, null));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
