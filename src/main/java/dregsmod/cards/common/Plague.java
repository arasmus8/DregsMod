package dregsmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractSealedCard;
import dregsmod.cards.AwakenSkillTag;
import dregsmod.cards.AwakenedMod;
import dregsmod.characters.Dregs;

import java.util.Optional;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static dregsmod.DregsMod.makeCardPath;

public class Plague extends AbstractSealedCard {

    // TEXT DECLARATION

    public static final String ID = DregsMod.makeID(Plague.class.getSimpleName());
    public static final String IMG = makeCardPath("Skill.png");
    public static CardStrings CARD_STRINGS = languagePack.getCardStrings(ID);
    // Must have an image with the same NAME as the card in your image folder!

    // /TEXT DECLARATION/

    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = -2;

    private static final int MAGIC = 5;
    private static final int UPGRADE_MAGIC_AMT = 3;

    // /STAT DECLARATION/


    public Plague() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = MAGIC;
        magicNumber = MAGIC;
        tags.add(AwakenSkillTag.AWAKEN_SKILL);
    }

    @Override
    public void triggerWhileSealed(AbstractPlayer player) {
        int[] damage = DamageInfo.createDamageMatrix(magicNumber);
        addToBot(new DamageAllEnemiesAction(player, damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.POISON));
        Optional<AwakenedMod> awakenedMod = AwakenedMod.getForCard(this);
        awakenedMod.ifPresent(mod -> mod.onUse(this, player, null));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC_AMT);
            initializeDescription();
        }
    }
}