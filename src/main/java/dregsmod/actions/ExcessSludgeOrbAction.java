package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import dregsmod.orbs.Sludge;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ExcessSludgeOrbAction extends AbstractGameAction {
    public ExcessSludgeOrbAction(int amount) {
        AbstractCreature p = AbstractDungeon.player;
        setValues(p, p, amount);
    }

    @Override
    public void update() {
        isDone = true;

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
