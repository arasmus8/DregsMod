package dregsmod.orbs;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.curses.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.OrbStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.OrbFlareEffect;
import dregsmod.DregsMod;
import dregsmod.actions.CardAwokenAction;

public class CurseOrb extends AbstractOrb {
    public static final String ORB_ID = DregsMod.makeID("CurseOrb");
    private static final OrbStrings orbString = CardCrawlGame.languagePack.getOrbString(ORB_ID);
    public static final String[] DESC = orbString.DESCRIPTION;
    private static final float ORB_BORDER_SCALE = 1.2F;
    private float vfxTimer = 0.5F;
    private static final float VFX_INTERVAL_TIME = 0.25F;
    private ParticleEffect particle;
    private static String[] curseList = {
            Clumsy.ID,
            Decay.ID,
            Doubt.ID,
            Injury.ID,
            Normality.ID,
            Pain.ID,
            Parasite.ID,
            Regret.ID,
            Shame.ID,
            Writhe.ID
    };

    public CurseOrb() {
        img = ImageMaster.loadImage(DregsMod.makeOrbPath("Curse.png"));
        this.ID = ORB_ID;
        this.name = orbString.NAME;
        this.baseEvokeAmount = 1;
        this.evokeAmount = this.baseEvokeAmount;
        this.basePassiveAmount = 1;
        this.passiveAmount = this.basePassiveAmount;
        this.updateDescription();
        this.channelAnimTimer = 0.5F;
        particle = new ParticleEffect();
        particle.load(Gdx.files.internal("particleSettings/DripParticle"), Gdx.files.internal("particleSettings"));
    }

    @Override
    public void updateDescription() {
        applyFocus();
        description = DESC[0] +
                passiveAmount +
                ((passiveAmount == 1) ? DESC[1] : DESC[2]);
    }

    @Override
    public void applyFocus() {
        AbstractPower power = AbstractDungeon.player.getPower("Focus");
        if (power != null) {
            passiveAmount = Math.max(0, basePassiveAmount + power.amount);
        } else {
            passiveAmount = basePassiveAmount;
        }
    }

    @Override
    public void onStartOfTurn() {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new OrbFlareEffect(this, OrbFlareEffect.OrbFlareColor.DARK), 0.1F));
        for (int i = 0; i < passiveAmount; i++) {
            String cardId = curseList[AbstractDungeon.cardRng.random(curseList.length - 1)];
            AbstractCard card = CardLibrary.getCopy(cardId);
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card));
        }
    }

    @Override
    public void onEvoke() {
        AbstractDungeon.actionManager.addToBottom(new CardAwokenAction());
    }

    @Override
    public AbstractOrb makeCopy() {
        return new CurseOrb();
    }

    @Override
    public void updateAnimation() {
        super.updateAnimation();
        this.angle += Gdx.graphics.getDeltaTime() * 120.0F;
        this.vfxTimer -= Gdx.graphics.getDeltaTime();
        particle.update(Gdx.graphics.getDeltaTime());
        if (this.vfxTimer < 0.0F) {
            float x = this.cX + MathUtils.random(-30f, 30f);
            float y = this.cY + MathUtils.random(-40f, -20f);
            particle.setPosition(x, y);
            particle.start();
            this.vfxTimer = VFX_INTERVAL_TIME;
        }

    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.c);
        int w = img.getWidth();
        int h = img.getHeight();
        float w2 = w / 2f;
        float h2 = h / 2f;
        sb.draw(img, cX - w2, cY - h2,
                w2, h2, w, h, scale, scale, this.angle,
                0, 0, w, h, false, false);
        this.shineColor.a = this.c.a / 3.0F;
        sb.setColor(this.shineColor);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);
        sb.draw(img, cX - w2, cY - h2, w2, h2, w, h,
                scale * ORB_BORDER_SCALE, scale * ORB_BORDER_SCALE,
                angle / ORB_BORDER_SCALE, 0, 0, w, h, false, false);
        sb.draw(img, cX - w2, cY - h2, w2, h2, w, h,
                scale * 1.5F, scale * 1.5F,
                angle / 1.4F, 0, 0, w, h, false, false);
        sb.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        particle.draw(sb);
        renderText(sb);
        hb.render(sb);
    }

    public void playChannelSFX() {
        CardCrawlGame.sound.play("ORB_DARK_CHANNEL", 0.8F);
    }
}
