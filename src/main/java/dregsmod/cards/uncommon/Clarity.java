package dregsmod.cards.uncommon;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.ChangeStanceAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConservePower;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;
import dregsmod.stances.AcceptanceStance;

public class Clarity extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Clarity.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 1;
    private static final int UPGRADED_COST = 0;

    public Clarity() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.CANT_AWAKEN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p, new ConservePower(p, 1), 1));
        addToBot(new ChangeStanceAction(AcceptanceStance.STANCE_ID));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(UPGRADED_COST);
            initializeDescription();
        }
    }
}
