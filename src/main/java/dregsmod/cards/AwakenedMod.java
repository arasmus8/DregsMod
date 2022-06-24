package dregsmod.cards;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import dregsmod.characters.Dregs;
import dregsmod.powers.ScapegoatPower;
import dregsmod.vfx.AwakenedParticleEffect;
import javassist.ClassPool;
import javassist.CtMethod;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Predicate;

public class AwakenedMod extends AbstractCardModifier {
    private static final int[] modifierValues;
    private static final String[] roman;
    public static final String ID = "dregsmod:AwakenedMod";
    private int level;
    private float vfxDuration;
    private float blinkDelay;
    private float blinkDuration;
    private float blinkFullDuration;
    private final ArrayList<AbstractGameEffect> eyeEffects;
    private TextureAtlas.AtlasRegion img;

    private static boolean usesMagicNumber(AbstractCard card) {
        final boolean[] usesMagic = {false};
        if (card.baseMagicNumber > 0
                && StringUtils.containsIgnoreCase(card.rawDescription, "!M!")) {
            try {
                ClassPool pool = Loader.getClassPool();
                CtMethod ctClass = pool.get(card.getClass().getName()).getDeclaredMethod("use");

                ctClass.instrument(new ExprEditor() {
                    @Override
                    public void edit(FieldAccess f) {

                        if (f.getFieldName().equals("magicNumber") && !f.isWriter()) {
                            usesMagic[0] = true;
                        }

                    }
                });

            } catch (Exception ignored) {
            }
        }
        if (usesMagic[0]) {
            AbstractCard base = card.makeCopy();
            AbstractCard upgraded = card.makeCopy();
            upgraded.upgrade();
            if (upgraded.magicNumber < base.magicNumber) {
                return false;
            }
        }
        return usesMagic[0];
    }

    public static final Predicate<AbstractCard> eligibleToAwaken = card -> {
        if (card.type == AbstractCard.CardType.ATTACK) {
            return true;
        } else if (card.type == AbstractCard.CardType.SKILL) {
            if (card.color == Dregs.Enums.COLOR_BLACK) {
                return !card.tags.contains(DregsCardTags.CANT_AWAKEN);
            }
            return (card.baseBlock > 0 || (card.baseMagicNumber > 0 && usesMagicNumber(card)));
        }
        return false;
    };

    public AwakenedMod(int level) {
        this.level = level;
        vfxDuration = 0.1f;
        eyeEffects = new ArrayList<>();
        img = ImageMaster.EYE_ANIM_6;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    public static void awakenCard(AbstractCard card) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
        if (modifiers.size() > 0) {
            AwakenedMod m = (AwakenedMod) modifiers.get(0);
            m.level = MathUtils.clamp(m.level + 1, 1, modifierValues.length - 1);
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
        m.level = MathUtils.clamp(m.level + times - 1, 1, modifierValues.length - 1);
        card.applyPowers();
        card.initializeDescription();
    }

    public static Optional<AwakenedMod> getForCard(AbstractCard card) {
        ArrayList<AbstractCardModifier> modifiers = CardModifierManager.getModifiers(card, ID);
        if (modifiers.size() > 0) {
            AwakenedMod m = (AwakenedMod) modifiers.get(0);
            return Optional.of(m);
        } else {
            return Optional.empty();
        }
    }

    private int bonusFromScapegoat() {
        AbstractPlayer p = AbstractDungeon.player;
        AbstractPower scapegoatPower = p.getPower(ScapegoatPower.POWER_ID);
        if (scapegoatPower != null) {
            ScapegoatPower actual = (ScapegoatPower) scapegoatPower;
            return actual.amount2;
        }
        return 0;
    }

    @Override
    public float modifyDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            return damage + modifierValues[level] + bonusFromScapegoat();
        } else {
            return damage;
        }
    }

    @Override
    public float modifyBlock(float block, AbstractCard card) {
        if (card.type == AbstractCard.CardType.SKILL) {
            return block + modifierValues[level - 1] + bonusFromScapegoat();
        } else {
            return block;
        }
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        super.onApplyPowers(card);
        if (card.tags.contains(DregsCardTags.AWAKEN_SKILL)) {
            card.magicNumber = card.baseMagicNumber + modifierValues[level - 1];
            if (card.magicNumber != card.baseMagicNumber) {
                card.isMagicNumberModified = true;
            }
        } else if (card.type == AbstractCard.CardType.SKILL &&
                card.color != Dregs.Enums.COLOR_BLACK &&
                card.baseBlock <= 0 &&
                card.baseMagicNumber > 0
        ) {
            int adjustBy = modifierValues[level - 1];
            card.magicNumber = card.baseMagicNumber + adjustBy;
            if (card.magicNumber != card.baseMagicNumber) {
                card.isMagicNumberModified = true;
            }
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
            actions.add(new HealAction(p, p, modifierValues[level - 4]));
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
            vfxDuration = MathUtils.random(0.1f, Interpolation.exp5In.apply(1.0f, 0.3f, (float) level / (float) modifierValues.length));
            eyeEffects.add(new AwakenedParticleEffect(card));
        }

        blinkDelay -= dt;
        blinkDuration -= dt;
        if (blinkDelay < 0f) {
            blinkDelay = MathUtils.random(2f, 5f);
            blinkFullDuration = blinkDuration = MathUtils.random(1f, 1.4f);
        }

        if (blinkDuration > blinkFullDuration * 0.85F) {
            img = ImageMaster.EYE_ANIM_6;
        } else if (blinkDuration > blinkFullDuration * 0.8F) {
            img = ImageMaster.EYE_ANIM_5;
        } else if (blinkDuration > blinkFullDuration * 0.75F) {
            img = ImageMaster.EYE_ANIM_4;
        } else if (blinkDuration > blinkFullDuration * 0.7F) {
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
            img = ImageMaster.EYE_ANIM_3;
        } else if (blinkDuration > blinkFullDuration * 0.2F) {
            img = ImageMaster.EYE_ANIM_4;
        } else if (blinkDuration > blinkFullDuration * 0.15F) {
            img = ImageMaster.EYE_ANIM_5;
        } else {
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
        float lvlScale = Interpolation.exp5In.apply(1f, 1.75f, (float) level / (float) modifierValues.length);
        sb.draw(img,
                card.hb.cX + vec.x - img.packedWidth / 2f,
                card.hb.cY + vec.y - img.packedHeight / 2f,
                (float) img.packedWidth / 2.0F,
                (float) img.packedHeight / 2.0F,
                (float) img.packedWidth,
                (float) img.packedHeight,
                card.drawScale * lvlScale,
                card.drawScale * lvlScale,
                card.angle);
        if (level > 3) {
            int healAmount = modifierValues[level - 4];
            vec = new Vector2(0, 0);
            vec.scl(card.drawScale * Settings.scale);
            vec.rotate(card.angle);
            color = Color.WHITE.cpy();
            color.a = card.transparency;
            sb.setColor(color);
            Texture hpImg = ImageMaster.TP_HP;
            sb.draw(hpImg,
                    card.hb.cX + vec.x - hpImg.getWidth() / 2f,
                    card.hb.cY + vec.y - hpImg.getHeight() / 2f,
                    (float) hpImg.getWidth() / 2.0F,
                    (float) hpImg.getHeight() / 2.0F,
                    (float) hpImg.getWidth(),
                    (float) hpImg.getHeight(),
                    card.drawScale * 1.25f,
                    card.drawScale * 1.25f,
                    card.angle,
                    0,
                    0,
                    hpImg.getWidth(),
                    hpImg.getHeight(),
                    false,
                    false);
            String text = "" + healAmount;
            FontHelper.cardEnergyFont_L.getData().setScale(card.drawScale * 0.5f);
            BitmapFont font = FontHelper.cardEnergyFont_L;
            FontHelper.renderRotatedText(sb,
                    font,
                    text,
                    card.hb.cX,
                    card.hb.cY,
                    vec.x,
                    vec.y,
                    card.angle,
                    false,
                    color);
        }
    }

    static {
        modifierValues = new int[]{
                2,
                3,
                5,
                8,
                12,
                16,
                20,
                24,
                28,
                32,
                36
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
