package dregsmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.characters.Dregs;

public class IcyTouch extends AbstractCurseHoldingCard {

    public static final String ID = DregsMod.makeID(IcyTouch.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 6;

    public IcyTouch() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        if (holdingCurse || upgraded) {
            showEvokeValue = true;
            showEvokeOrbCount = 2;
        } else {
            showEvokeValue = false;
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (!upgraded) {
            super.triggerOnGlowCheck();
        } else {
            glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        if (holdingCurse || upgraded) {
            addToBot(new ChannelAction(new Frost()));
            addToBot(new ChannelAction(new Frost()));
        }
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