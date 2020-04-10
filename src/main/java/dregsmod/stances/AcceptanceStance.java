package dregsmod.stances;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import dregsmod.vfx.AcceptanceParticleEffect;
import dregsmod.vfx.CustomStanceAuraEffect;
import dregsmod.vfx.CustomStanceChangeParticleGenerator;

public class AcceptanceStance extends AbstractStance {
    public static final String STANCE_ID = "dregsmod:Acceptance";
    private static final StanceStrings stanceString;
    private static long sfxId;

    public AcceptanceStance() {
        this.ID = STANCE_ID;
        this.name = stanceString.NAME;
        this.updateDescription();
    }

    public float atDamageGive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * 2.0F : damage;
    }

    public void updateAnimation() {
        if (!Settings.DISABLE_EFFECTS) {
            this.particleTimer -= Gdx.graphics.getDeltaTime();
            if (this.particleTimer < 0.0F) {
                this.particleTimer = 0.05F;
                AbstractDungeon.effectsQueue.add(new AcceptanceParticleEffect());
            }
        }

        this.particleTimer2 -= Gdx.graphics.getDeltaTime();
        if (this.particleTimer2 < 0.0F) {
            this.particleTimer2 = MathUtils.random(0.3F, 0.4F);
            AbstractDungeon.effectsQueue.add(new CustomStanceAuraEffect(STANCE_ID));
        }

    }

    public static void adjustAttackCosts(int amt) {
        AbstractDungeon.player.hand.getCardsOfType(AbstractCard.CardType.ATTACK).group.forEach(card -> card.setCostForTurn(card.costForTurn + amt));
    }

    public void updateDescription() {
        this.description = stanceString.DESCRIPTION[0];
    }

    public void onEnterStance() {
        if (sfxId != -1L) {
            this.stopIdleSfx();
        }

        CardCrawlGame.sound.play("ENEMY_TURN");
        sfxId = CardCrawlGame.sound.playAndLoop("STANCE_LOOP_WRATH");
        AbstractDungeon.effectsQueue.add(new BorderFlashEffect(Color.GOLDENROD, true));
        AbstractDungeon.effectsQueue.add(new CustomStanceChangeParticleGenerator(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, STANCE_ID));
        adjustAttackCosts(1);
    }

    public void onExitStance() {
        this.stopIdleSfx();
        adjustAttackCosts(-1);
    }

    public void stopIdleSfx() {
        if (sfxId != -1L) {
            CardCrawlGame.sound.stop("STANCE_LOOP_WRATH", sfxId);
            sfxId = -1L;
        }

    }

    static {
        stanceString = CardCrawlGame.languagePack.getStanceString(STANCE_ID);
        sfxId = -1L;
    }
}
