package dregsmod.cards;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import dregsmod.vfx.AwakenedParticleEffect;

import java.util.ArrayList;

public class AwakenedMod extends AbstractCardModifier {
    private static final int[] primes;
    private static final String[] roman;
    public static final String ID = "dregsmod:AwakenedMod";
    private int level;
    private float vfxDuration;
    private float blinkDelay;
    private float blinkDuration;
    private float blinkFullDuration;
    private float vY;
    private ArrayList<AbstractGameEffect> eyeEffects;
    private TextureAtlas.AtlasRegion img;

    public AwakenedMod(int level) {
        this.level = level;
        vfxDuration = 0.1f;
        eyeEffects = new ArrayList<>();
        img = ImageMaster.EYE_ANIM_6;
        vY = 12.0F * Settings.scale;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    public static void awakenCard(AbstractCard card) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
        if (modifiers.size() > 0) {
            AwakenedMod m = (AwakenedMod) modifiers.get(0);
            m.level = MathUtils.clamp(m.level + 1, 1, primes.length - 1);
        } else {
            CardModifierManager.addModifier(card, new AwakenedMod(1));
        }
        card.applyPowers();
        card.initializeDescription();
    }

    public static void awakenCard(AbstractCard card, int times) {
        awakenCard(card);
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
        AwakenedMod m = (AwakenedMod) modifiers.get(0);
        m.level = MathUtils.clamp(m.level + times - 1, 1, primes.length - 1);
    }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            return damage + primes[level];
        } else {
            return damage;
        }
    }

    @Override
    public float modifyBlock(float block, AbstractCard card) {
        if (card.type == AbstractCard.CardType.SKILL) {
            return block + primes[level - 1];
        } else {
            return block;
        }
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction useCardAction) {
        AbstractPlayer p = AbstractDungeon.player;
        ArrayList<AbstractGameAction> actions = new ArrayList<>();
        if (level > 2) {
            actions.add(new DrawCardAction(p, 1));
        }
        if (level > 3) {
            actions.add(new LoseHPAction(p, p, primes[level - 3]));
        }
        // queue actions
        for (AbstractGameAction action : actions) {
            AbstractDungeon.actionManager.addToBottom(action);
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (level == 1) {
            return rawDescription + " NL dregsmod:Awoken";
        } else {
            return rawDescription + " NL dregsmod:Awoken\u00A0" + roman[level];
        }
        /*
        return String.format("%s NL [#87ceeb]Awoken[]%s",
                rawDescription,
                level > 1 ? " - [#87ceeb]Level[] [#87ceeb]" + level + "[]" : "");
        */
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AwakenedMod(level);
    }

    @Override
    public void onUpdate(AbstractCard card) {
        float dt = Gdx.graphics.getDeltaTime();
        vfxDuration -= dt;
        if (vfxDuration < 0f) {
            vfxDuration = MathUtils.random(0.1f, Interpolation.exp5In.apply(1.0f, 0.3f, (float) level / (float) primes.length));
            eyeEffects.add(new AwakenedParticleEffect(card));
        }

        blinkDelay -= dt;
        blinkDuration -= dt;
        if (blinkDelay < 0f) {
            blinkDelay = MathUtils.random(2f, 5f);
            blinkFullDuration = blinkDuration = MathUtils.random(1f, 1.4f);
        }

        if (blinkDuration > blinkFullDuration * 0.85F) {
            vY = 12.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_6;
        } else if (blinkDuration > blinkFullDuration * 0.8F) {
            vY = 8.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_5;
        } else if (blinkDuration > blinkFullDuration * 0.75F) {
            vY = 4.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_4;
        } else if (blinkDuration > blinkFullDuration * 0.7F) {
            vY = 3.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_3;
        } else if (blinkDuration > blinkFullDuration * 0.65F) {
            img = ImageMaster.EYE_ANIM_2;
        } else if (blinkDuration > blinkFullDuration * 0.6F) {
            img = ImageMaster.EYE_ANIM_1;
        } else if (blinkDuration > blinkFullDuration * 0.55F) {
            img = ImageMaster.EYE_ANIM_0;
        } else if (blinkDuration > blinkFullDuration * 0.38F) {
            img = ImageMaster.EYE_ANIM_1;
        } else if (blinkDuration > blinkFullDuration * 0.3F) {
            img = ImageMaster.EYE_ANIM_2;
        } else if (blinkDuration > blinkFullDuration * 0.25F) {
            vY = 3.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_3;
        } else if (blinkDuration > blinkFullDuration * 0.2F) {
            vY = 4.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_4;
        } else if (blinkDuration > blinkFullDuration * 0.15F) {
            vY = 8.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_5;
        } else {
            vY = 12.0F * Settings.scale;
            img = ImageMaster.EYE_ANIM_6;
        }
        for (AbstractGameEffect e : eyeEffects) {
            e.update();
        }
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        for (AbstractGameEffect e : eyeEffects) {
            e.render(sb);
        }

        Vector2 vec = new Vector2(0, 195f);
        vec.scl(card.drawScale * Settings.scale);
        vec.rotate(card.angle);
        Color color = Color.BLACK.cpy();
        color.a = card.transparency;
        sb.setColor(color);
        sb.draw(img,
                card.hb.cX + vec.x - img.packedWidth / 2f,
                card.hb.cY + vec.y - img.packedHeight / 2f,
                (float) img.packedWidth / 2.0F,
                (float) img.packedHeight / 2.0F,
                (float) img.packedWidth,
                (float) img.packedHeight,
                card.drawScale,
                card.drawScale,
                card.angle);
    }

    static {
        primes = new int[]{
                2,
                3,
                5,
                7,
                11,
                13,
                17,
                23,
                29,
                31,
                37
        };

        roman = new String[]{
                "",
                "",
                "II",
                "III",
                "IV",
                "V",
                "VI",
                "VII",
                "VIII",
                "IX",
                "X"
        };
    }
}
