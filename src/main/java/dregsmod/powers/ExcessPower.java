package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;
import dregsmod.orbs.Sludge;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ExcessPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(ExcessPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ExcessPower(AbstractCreature owner, int amount) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;

        loadRegion("evolve");
        updateDescription();
    }

    @Override
    public void onCardDraw(AbstractCard card) {
        if (card.type == AbstractCard.CardType.CURSE) {
            flash();
            ArrayList<AbstractOrb> sludgeOrbs = AbstractDungeon.player.orbs.stream()
                    .filter(orb -> orb != null && orb.ID != null && orb.ID.equals(Sludge.ORB_ID))
                    .collect(Collectors.toCollection(ArrayList::new));
            if (sludgeOrbs.size() == 0) {
                AbstractOrb orb = new Sludge();
                addToBot(new ChannelAction(orb));
                for (int i = 1; i < amount; i++) {
                    orb.onStartOfTurn();
                    orb.onEndOfTurn();
                }
            } else {
                sludgeOrbs.forEach(orb -> {
                    for (int i = 0; i < amount; i++) {
                        orb.onStartOfTurn();
                        orb.onEndOfTurn();
                    }
                });
            }
        }
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + (amount > 1 ? DESCRIPTIONS[2] : DESCRIPTIONS[1]) + DESCRIPTIONS[3];
    }

    @Override
    public AbstractPower makeCopy() {
        return new ExcessPower(owner, amount);
    }
}
