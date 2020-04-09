package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
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

        ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs.stream()
                .filter(orb -> !(orb instanceof EmptyOrbSlot))
                .collect(Collectors.toCollection(ArrayList::new));
        if (orbs.size() == 0) {
            AbstractOrb orb = new Sludge();
            addToBot(new ChannelAction(orb));
        }
    }
}
