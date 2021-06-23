package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import dregsmod.DregsMod;
import dregsmod.actions.SealRandomCurseAction;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.characters.Dregs;

public class SealingWish extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(SealingWish.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_PLUS_DMG = 4;

    public SealingWish() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseDamage = DAMAGE;
        isMultiDamage = true;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster monster) {
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            CardCrawlGame.sound.play("ATTACK_MAGIC_SLOW_1");
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                addToBot(new VFXAction(new WallopEffect(damage, m.hb.cX, m.hb.cY)));
            }
            addToBot(new DamageAllEnemiesAction(p, multiDamage, damageTypeForTurn, AbstractGameAction.AttackEffect.NONE));
        }

        addToBot(new SealRandomCurseAction());
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            initializeDescription();
        }
    }
}