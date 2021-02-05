package dregsmod.cards.curses;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.SoulboundField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlueCandle;
import dregsmod.DregsMod;
import dregsmod.actions.SealAndPerformAction;
import dregsmod.cards.AbstractCurseHoldingCard;
import dregsmod.cards.rare.EclipseForm;

public class Jealousy extends AbstractCurseHoldingCard {
    public static final String ID = DregsMod.makeID(Jealousy.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = 1;

    public Jealousy() {
        super(ID, COST, TYPE, COLOR, RARITY, TARGET);
        SoulboundField.soulbound.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (holdingCurse) {
            addToBot(new SealAndPerformAction(
                    1,
                    true,
                    p.hand,
                    card -> card != this && card.type == CardType.CURSE,
                    null
            ));
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean fromSuper = super.canUse(p, m);
        cantUseMessage = EXTENDED_DESCRIPTION[0];
        return fromSuper && (holdingCurse || p.hasPower(EclipseForm.ID) || p.hasRelic(BlueCandle.ID));
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }
}
