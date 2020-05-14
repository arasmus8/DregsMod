package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.InstantKillAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;
import dregsmod.cards.curses.Catastrophe;
import dregsmod.cards.curses.Doom;
import dregsmod.cards.curses.Gloom;
import dregsmod.relics.CursedLocket;
import dregsmod.util.TextureLoader;

public class CursedPower extends AbstractPower implements CloneablePowerInterface, HealthBarRenderPower {

    public static final String POWER_ID = DregsMod.makeID(CursedPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture texture = TextureLoader.getTexture(DregsMod.makePowerPath("cursed.png"));

    public CursedPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        region48 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        region128 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        updateDescription();
        type = PowerType.DEBUFF;
        if (AbstractDungeon.player.hasRelic(CursedLocket.ID)) {
            AbstractDungeon.player.getRelic(CursedLocket.ID).flash();
            this.amount += 5;
        }
    }

    @Override
    public void stackPower(int stackAmount) {
        super.stackPower(stackAmount);
        if (AbstractDungeon.player.hasRelic(CursedLocket.ID)) {
            AbstractDungeon.player.getRelic(CursedLocket.ID).flash();
            amount += 5;
        }
    }

    @Override
    public void atStartOfTurn() {
        this.flash();
        int healAmount = (owner.maxHealth - owner.currentHealth) / 10;
        if (amount >= owner.currentHealth) {
            addToBot(new InstantKillAction(owner));
            addToBot(new FastShakeAction(owner, 0.5F, 0.2F));// 119
            AbstractMonster.EnemyType type = ((AbstractMonster) owner).type;
            AbstractCard curseToAdd;
            if (owner.hasPower("Minion")) {
                return;
            }
            switch (type) {
                case BOSS:
                    curseToAdd = new Catastrophe();
                    break;
                case ELITE:
                    curseToAdd = new Doom();
                    break;
                default:
                    curseToAdd = new Gloom();
                    break;
            }
            addToBot(new AddCardToDeckAction(curseToAdd));
        } else {
            addToBot(new HealAction(owner, owner, healAmount));
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new CursedPower(owner, amount);
    }

    @Override
    public int getHealthBarAmount() {
        return amount;
    }

    @Override
    public Color getColor() {
        return CardHelper.getColor(36, 11, 71);
    }
}
