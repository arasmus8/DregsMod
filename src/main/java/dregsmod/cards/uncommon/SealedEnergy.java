package dregsmod.cards.uncommon;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;
import dregsmod.patches.variables.CardSealed;

public class SealedEnergy extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(SealedEnergy.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 2;

    public SealedEnergy() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseDamage = DAMAGE;
        baseMagicNumber = magicNumber = MAGIC;
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int origBaseDamage = baseDamage;
        baseDamage = MathUtils.floor(calculateModifiedCardDamage(AbstractDungeon.player, baseDamage));
        super.calculateCardDamage(mo);
        baseDamage = origBaseDamage;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public void applyPowers() {
        int origBaseDamage = baseDamage;
        baseDamage = MathUtils.floor(calculateModifiedCardDamage(AbstractDungeon.player, baseDamage));
        super.applyPowers();
        baseDamage = origBaseDamage;
        isDamageModified = damage != baseDamage;
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, float damage) {
        super.calculateModifiedCardDamage(player, damage);
        long sealedCurses = player.discardPile.group.stream()
                .filter(card -> CardSealed.isSealed.get(card) && card.type == CardType.CURSE)
                .count();
        return damage + magicNumber * sealedCurses;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, damage, damageTypeForTurn), AbstractGameAction.AttackEffect.SLASH_HEAVY));
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