package dregsmod.cards.rare;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;
import dregsmod.DregsMod;
import dregsmod.cards.AbstractDregsCard;
import dregsmod.cards.DregsCardTags;
import dregsmod.characters.Dregs;
import dregsmod.powers.CursedPower;

public class Ritual extends AbstractDregsCard {
    public static final String ID = DregsMod.makeID(Ritual.class.getSimpleName());

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = Dregs.Enums.COLOR_BLACK;

    private static final int COST = 2;

    private static final int MAGIC = 25;
    private static final int UPGRADED_MAGIC = 10;

    public Ritual() {
        super(ID, COST, TYPE, RARITY, TARGET, COLOR, DregsCardTags.AWAKEN_SKILL);
        magicNumber = baseMagicNumber = MAGIC;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new SFXAction("ATTACK_PIERCING_WAIL"));
        if (Settings.FAST_MODE) {
            addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.PURPLE_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), Settings.ACTION_DUR_FAST));
        } else {
            addToBot(new VFXAction(p, new ShockWaveEffect(p.hb.cX, p.hb.cY, Settings.PURPLE_COLOR, ShockWaveEffect.ShockWaveType.CHAOTIC), Settings.ACTION_DUR_XLONG));
        }
        AbstractDungeon.getMonsters().monsters.stream()
                .filter(monster -> !(monster.isDead || monster.isDying || monster.escaped))
                .forEach(monster -> addToBot(new ApplyPowerAction(
                        monster,
                        p,
                        new CursedPower(monster, magicNumber),
                        magicNumber
                )));
        int lifeToRecover = MathUtils.floor((float) (p.maxHealth - p.currentHealth) * 0.5f);
        addToBot(new HealAction(p, p, lifeToRecover));
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADED_MAGIC);
            initializeDescription();
        }
    }
}
