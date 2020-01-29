package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import dregsmod.cards.uncommon.HardLuck;
import dregsmod.cards.uncommon.Unlucky;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class RedemptionAction extends AbstractGameAction {
    private DamageInfo info;

    public RedemptionAction(AbstractCreature target, DamageInfo info) {
        this.info = info;
        this.setValues(target, info);
        this.actionType = ActionType.DAMAGE;
        this.duration = 0.1F;
    }

    @Override
    public void update() {
        if (this.duration == 0.1F && this.target != null) {
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, AttackEffect.SLASH_HEAVY));
            target.damage(info);
            if ((target.isDying || target.currentHealth <= 0) && !target.halfDead && !target.hasPower(MinionPower.POWER_ID)) {
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                group.group.addAll(
                        AbstractDungeon.player.masterDeck.getPurgeableCards().group.stream()
                                .filter(card -> card.type == AbstractCard.CardType.CURSE)
                                .collect(Collectors.toList())
                );

                if (group.size() > 0) {
                    AbstractCard curseToRemove = group.getRandomCard(AbstractDungeon.cardRandomRng);
                    CardCrawlGame.sound.play("CARD_EXHAUST");
                    AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(curseToRemove, (float) (Settings.WIDTH / 2), (float) (Settings.HEIGHT / 2)));
                    AbstractDungeon.player.masterDeck.removeCard(curseToRemove);
                    ArrayList<AbstractCard> cards = new ArrayList<>();
                    cards.addAll(AbstractDungeon.player.hand.group);
                    cards.addAll(AbstractDungeon.player.drawPile.group);
                    cards.addAll(AbstractDungeon.player.discardPile.group);
                    cards.addAll(AbstractDungeon.player.exhaustPile.group);
                    for (AbstractCard c : cards) {
                        if (c instanceof HardLuck) {
                            ((HardLuck) c).configureCost();
                        } else if (c instanceof Unlucky) {
                            ((Unlucky) c).configureCost();
                        }
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        tickDuration();
    }
}
