package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.cards.TriggerOnSelfSealedCard;
import dregsmod.characters.Dregs;

public class Guardian extends AbstractDregsCard implements TriggerOnSelfSealedCard {
    public static final String ID = DregsMod.makeID(Guardian.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int BLOCK = 5;
    private static final int MAGIC = 12;
    private static final int UPGRADE_PLUS_MAGIC = 3;

    public Guardian() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.AWAKEN_SKILL);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, block));
    }

    public void triggerOnSealed() {
        addToBot(new GainBlockAction(AbstractDungeon.player, magicNumber));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}
