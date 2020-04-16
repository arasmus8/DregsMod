package dregsmod.potions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dregsmod.DregsMod;
import dregsmod.actions.CardAwokenAction;

public class InsightPotion extends AbstractPotion {
    public static final String POTION_ID = DregsMod.makeID(InsightPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public static final Color LIQUID_COLOR = Color.BLACK.cpy();
    public static final Color HYBRID_COLOR = Color.MAROON.cpy();
    public static final Color SPOTS_COLOR = Color.CLEAR.cpy();

    public InsightPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPHERE, PotionColor.SMOKE);
        isThrown = false;
    }


    @Override
    public void use(AbstractCreature abstractCreature) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card.type == AbstractCard.CardType.ATTACK || card.type == AbstractCard.CardType.SKILL) {
                    addToBot(new CardAwokenAction(card, potency));
                }
            }
        }
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        if (potency > 1) {
            description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[2];
        } else {
            description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
        }
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public int getPotency(int i) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new InsightPotion();
    }
}
