package dregsmod.cards.curses;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.powers.CursedPower;

public class Catastrophe extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Catastrophe.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.SPECIAL;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.CURSE;
    public static final CardColor COLOR = CardColor.CURSE;

    private static final int COST = 3;
    private static final int MAGIC = 20;

    public Catastrophe() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR);
        baseMagicNumber = MAGIC;
        magicNumber = baseMagicNumber;
        isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.getMonsters().monsters.stream()
                .filter(monster -> !(monster.isDead || monster.isDying || monster.escaped))
                .forEach(monster -> addToBot(new ApplyPowerAction(
                        monster,
                        p,
                        new CursedPower(monster, magicNumber),
                        magicNumber
                )));
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upgrade() {
    }
}
