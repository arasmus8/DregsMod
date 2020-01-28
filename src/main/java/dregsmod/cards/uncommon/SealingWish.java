package dregsmod.cards.uncommon;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.WallopEffect;
import dregsmod.DregsMod;
import dregsmod.actions.SealAndPerformAction;
import dregsmod.characters.Dregs;
import dregsmod.patches.variables.CardSealed;

import java.util.Optional;
import java.util.function.Predicate;

import static dregsmod.DregsMod.makeCardPath;

public class SealingWish extends CustomCard {

    public static final String ID = DregsMod.makeID(SealingWish.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    public static final String IMG = makeCardPath("Attack.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 2;
    private static final int DAMAGE = 14;
    private static final int UPGRADE_PLUS_DMG = 4;

    public SealingWish() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseDamage = DAMAGE;
        isMultiDamage = true;
    }

    private Optional<AbstractGameAction> sealCurseFromGroupIfPresent(CardGroup group) {
        Predicate<AbstractCard> unsealedCurseFilter = card -> card.type == AbstractCard.CardType.CURSE && !CardSealed.isSealed.get(card);
        long unsealedCurses = group.group.stream().filter(unsealedCurseFilter).count();
        if (unsealedCurses > 0) {
            return Optional.of(new SealAndPerformAction(1, true, group, unsealedCurseFilter, null));
        }
        return Optional.empty();
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
        Optional<AbstractGameAction> sealAction = sealCurseFromGroupIfPresent(p.hand);
        if (sealAction.isPresent()) {
            addToBot(sealAction.get());
        } else {
            sealAction = sealCurseFromGroupIfPresent(p.drawPile);
            if (sealAction.isPresent()) {
                addToBot(sealAction.get());
            } else {
                sealAction = sealCurseFromGroupIfPresent(p.discardPile);
                sealAction.ifPresent(this::addToBot);
            }
        }
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