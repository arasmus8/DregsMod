package dregsmod.cards.common;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractSealedCard;
import dregsmod.cards.AwakenedMod;
import dregsmod.characters.Dregs;

import java.util.Optional;

import static dregsmod.cards.DregsCardTags.AWAKEN_SKILL;

public class Plague extends AbstractSealedCard {
    public static final String ID = DregsMod.makeID(Plague.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = -2;

    private static final int MAGIC = 5;
    private static final int UPGRADE_MAGIC_AMT = 3;

    public Plague() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET, AWAKEN_SKILL);
        magicNumber = baseMagicNumber = MAGIC;
    }

    @Override
    public void triggerWhileSealed(AbstractPlayer player) {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            addToBot(new DamageAction(m, new DamageInfo(player, magicNumber, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.POISON, true));
        }
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

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MAGIC_AMT);
            initializeDescription();
        }
    }
}