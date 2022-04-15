package dregsmod.cards.rare;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.OvercomeAction;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;

public class Overcome extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Overcome.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = -1;

    private static final int MAGIC = 0;

    public Overcome() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.CANT_AWAKEN);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new OvercomeAction(freeToPlayOnce, energyOnUse, upgraded));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
