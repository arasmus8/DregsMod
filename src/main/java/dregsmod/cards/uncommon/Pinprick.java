package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.TriggerMarksAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.watcher.MarkPower;
import com.megacrit.cardcrawl.vfx.combat.PressurePointEffect;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;

import static com.megacrit.cardcrawl.core.CardCrawlGame.languagePack;
import static dregsmod.DregsMod.makeCardPath;

public class Pinprick extends AbstractCurseHoldingCard {

    public static final String ID = DregsMod.makeID(Pinprick.class.getSimpleName());
    public static final String IMG = makeCardPath("Pinprick.png");
    public static CardStrings CARD_STRINGS = languagePack.getCardStrings(ID);

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;

    private static final int MAGIC = 5;

    public Pinprick() {
        super(ID, CARD_STRINGS.NAME, IMG, COST, CARD_STRINGS.DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        magicNumber = baseMagicNumber = MAGIC;
        tags.add(DregsCardTags.AWAKEN_SKILL);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (!holdingCurse) {
            return;
        }
        if (upgraded) {
            for (AbstractMonster mm : AbstractDungeon.getMonsters().monsters) {
                if (mm != null) {
                    addToBot(new ApplyPowerAction(mm, p, new VulnerablePower(mm, 2, false), 2));
                    addToBot(new VFXAction(new PressurePointEffect(mm.hb.cX, mm.hb.cY)));
                    addToBot(new ApplyPowerAction(mm, p, new MarkPower(mm, magicNumber), magicNumber));
                }
            }
        } else {
            addToBot(new ApplyPowerAction(m, p, new VulnerablePower(m, 2, false), 2));
            addToBot(new VFXAction(new PressurePointEffect(m.hb.cX, m.hb.cY)));
            addToBot(new ApplyPowerAction(m, p, new MarkPower(m, magicNumber), magicNumber));
        }
        addToBot(new TriggerMarksAction(this));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        if (super.canUse(p, m)) {
            if (!holdingCurse) {
                cantUseMessage = CARD_STRINGS.EXTENDED_DESCRIPTION[0];
            }
            return holdingCurse;
        } else {
            return false;
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            target = CardTarget.ALL_ENEMY;
            rawDescription = CARD_STRINGS.UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
