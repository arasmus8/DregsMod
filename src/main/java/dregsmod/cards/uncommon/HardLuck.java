package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;

public class HardLuck extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(HardLuck.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 6;
    private static final int UPGRADED_COST = 5;

    private static final int BLOCK = 13;

    public HardLuck() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseBlock = BLOCK;
    }

    public void configureCost() {
        if (AbstractDungeon.isPlayerInDungeon() &&
                AbstractDungeon.currMapNode != null &&
                AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            int curseCount = AbstractDungeon.player.masterDeck.getCardsOfType(CardType.CURSE).size();
            modifyCostForCombat(-curseCount);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new GainBlockAction(p, p, block));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            configureCost();
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new HardLuck();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        HardLuck copy = (HardLuck) super.makeStatEquivalentCopy();
        copy.configureCost();
        return copy;
    }
}
