package dregsmod.cards.rare;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.UpgradeTextChangingCard;
import dregsmod.characters.Dregs;
import dregsmod.vfx.BadOmenEffect;

import static dregsmod.DregsMod.makeCardPath;

public class BadOmen extends CustomCard implements UpgradeTextChangingCard {

    public static final String ID = DregsMod.makeID(BadOmen.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("BadOmen.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 40;

    public BadOmen() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        isMultiDamage = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster target) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            addToBot(new VFXAction(new BadOmenEffect(m.hb.cX, m.hb.cY)));
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
                cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            }
            return active;
        }
        return false;
    }

    @Override
    public String upgradePreviewText() {
        return diffText(cardStrings.DESCRIPTION, cardStrings.UPGRADE_DESCRIPTION);
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            selfRetain = true;
            rawDescription = cardStrings.UPGRADE_DESCRIPTION;
            upgradeName();
            initializeDescription();
        }
    }
}