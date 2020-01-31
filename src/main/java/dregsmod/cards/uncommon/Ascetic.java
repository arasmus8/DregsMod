package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.MantraPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractSealedCard;
import dregsmod.characters.Dregs;

import static dregsmod.DregsMod.makeCardPath;

public class Ascetic extends AbstractSealedCard {

// TEXT DECLARATION

    public static final String ID = DregsMod.makeID(Ascetic.class.getSimpleName());
    private static final CardStrings CARD_STRINGS = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String IMG = makeCardPath("Skill.png");
// Must have an image with the same NAME as the card in your image folder!

// /TEXT DECLARATION/

// STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.NONE;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = -2;

    private static final int MAGIC = 3;
    private static final int UPGRADE_PLUS_MAGIC = 1;

// /STAT DECLARATION/

    public Ascetic() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = MAGIC;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return false;
    }

    @Override
    public void triggerWhileSealed(AbstractPlayer p) {
        AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(this.makeSameInstanceOf(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
        addToBot(new ApplyPowerAction(p, p, new MantraPower(p, magicNumber), magicNumber));
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MAGIC);
            initializeDescription();
        }
    }
}