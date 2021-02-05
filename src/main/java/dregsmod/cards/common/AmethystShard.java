package dregsmod.cards.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.actions.GemShardAction;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;

public class AmethystShard extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(AmethystShard.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 0;

    public AmethystShard() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.CANT_AWAKEN);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GemShardAction(CardColor.PURPLE, upgraded));
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
