package dregsmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import dregsmod.patches.variables.CardSealed;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MaledictionAction extends AbstractGameAction {
    private final AbstractCreature m;
    private final AbstractPlayer p;
    private final int energyOnUse;
    private final boolean freeToPlayOnce;

    public MaledictionAction(AbstractCreature target, int damage, boolean freeToPlayOnce, int energyOnUse) {
        m = target;
        p = AbstractDungeon.player;
        this.freeToPlayOnce = freeToPlayOnce;
        this.energyOnUse = energyOnUse;
        setValues(m, p, damage);
        duration = Settings.ACTION_DUR_XFAST;
        actionType = ActionType.SPECIAL;
    }

    @Override
    public void update() {
        int effect = EnergyPanel.totalCount;
        if (energyOnUse != -1) {
            effect = energyOnUse;
        }

        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }

        if(effect > 0) {
            Predicate<AbstractCard> cursesFilter = card -> card.type == AbstractCard.CardType.CURSE && !CardSealed.isSealed.get(card);
            ArrayList<AbstractCard> unsealedCurses = p.hand.group.stream().filter(cursesFilter).collect(Collectors.toCollection(ArrayList::new));
            if (unsealedCurses.size() >= effect) {
                addToBot(new SealAndPerformAction(
                        effect,
                        true,
                        p.hand,
                        cursesFilter,
                        new MaledictionDamageAction(m, new DamageInfo(p, amount, DamageInfo.DamageType.NORMAL))
                ));
                effect = 0;
            } else if (unsealedCurses.size() > 0) {
                addToBot(new SealAndPerformAction(
                        unsealedCurses.size(),
                        true,
                        p.hand,
                        cursesFilter,
                        new MaledictionDamageAction(m, new DamageInfo(p, amount, DamageInfo.DamageType.NORMAL))
                ));
                effect -= unsealedCurses.size();
            }
            if (effect > 0) {
                unsealedCurses = p.drawPile.group.stream().filter(cursesFilter).collect(Collectors.toCollection(ArrayList::new));
                if (unsealedCurses.size() >= effect) {
                    addToBot(new SealAndPerformAction(
                            effect,
                            true,
                            p.drawPile,
                            cursesFilter,
                            new MaledictionDamageAction(m, new DamageInfo(p, amount, DamageInfo.DamageType.NORMAL))
                    ));
                    effect = 0;
                } else if (unsealedCurses.size() > 0) {
                    addToBot(new SealAndPerformAction(
                            unsealedCurses.size(),
                            true,
                            p.drawPile,
                            cursesFilter,
                            new MaledictionDamageAction(m, new DamageInfo(p, amount, DamageInfo.DamageType.NORMAL))
                    ));
                    effect -= unsealedCurses.size();
                }
            }
            if (effect > 0) {
                unsealedCurses = p.discardPile.group.stream().filter(cursesFilter).collect(Collectors.toCollection(ArrayList::new));
                if (unsealedCurses.size() >= effect) {
                    addToBot(new SealAndPerformAction(
                            effect,
                            true,
                            p.discardPile,
                            cursesFilter,
                            new MaledictionDamageAction(m, new DamageInfo(p, amount, DamageInfo.DamageType.NORMAL))
                    ));
                } else if (unsealedCurses.size() > 0) {
                    addToBot(new SealAndPerformAction(
                            unsealedCurses.size(),
                            true,
                            p.discardPile,
                            cursesFilter,
                            new MaledictionDamageAction(m, new DamageInfo(p, amount, DamageInfo.DamageType.NORMAL))
                    ));
                }
            }
        }

        if (!freeToPlayOnce) {
            p.energy.use(EnergyPanel.totalCount);
        }

        isDone = true;
    }
}
