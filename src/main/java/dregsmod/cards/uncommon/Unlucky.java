package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;

public class Unlucky extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Unlucky.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 6;
    private static final int DAMAGE = 13;

    public Unlucky() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = 1;
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
        if (upgraded) {
            addToBot(new DrawCardAction(p, magicNumber));
        }
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            configureCost();
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Unlucky();
    }

    @Override
    public AbstractCard makeStatEquivalentCopy() {
        Unlucky copy = (Unlucky) super.makeStatEquivalentCopy();
        copy.configureCost();
        return copy;
    }
}