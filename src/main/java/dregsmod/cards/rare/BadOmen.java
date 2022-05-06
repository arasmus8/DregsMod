package dregsmod.cards.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;
import dregsmod.vfx.BadOmenEffect;

public class BadOmen extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(BadOmen.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 40;

    public BadOmen() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseDamage = DAMAGE;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster target) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (!m.isDeadOrEscaped()) {
                addToBot(new VFXAction(new BadOmenEffect(m.hb.cX, m.hb.cY)));
            }
        }
        addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY));
    }

    private boolean active() {
        AbstractPlayer p = AbstractDungeon.player;
        return (p.drawPile.size() == 13 || p.discardPile.size() == 13);
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (active()) {
            glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (super.canUse(p, m)) {
            boolean active = active();
            if (!active) {
                cantUseMessage = EXTENDED_DESCRIPTION[0];
            }
            return active;
        }
        return false;
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            selfRetain = true;
            rawDescription = UPGRADE_DESCRIPTION;
            upgradeName();
            initializeDescription();
        }
    }
}