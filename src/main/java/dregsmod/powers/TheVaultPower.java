package dregsmod.powers;

import basemod.interfaces.CloneablePowerInterface;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import dregsmod.DregsMod;
import dregsmod.actions.SealAndPerformAction;
import dregsmod.patches.variables.CardSealed;
import dregsmod.util.TextureLoader;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TheVaultPower extends AbstractPower implements CloneablePowerInterface {

    public static final String POWER_ID = DregsMod.makeID(TheVaultPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    private static final Texture texture = TextureLoader.getTexture(DregsMod.makePowerPath("thevault.png"));

    private static ArrayList<AbstractCard> ignoredCards;

    public TheVaultPower(AbstractCreature owner) {
        name = NAME;
        ID = POWER_ID;
        this.owner = owner;
        this.amount = -1;

        region48 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        region128 = new TextureAtlas.AtlasRegion(texture, 0, 0, 32, 32);
        updateDescription();
        if (!AbstractDungeon.player.hasPower(POWER_ID)) {
            ignoredCards = new ArrayList<>(AbstractDungeon.player.discardPile.group);
        }
    }

    @Override
    public void onDrawOrDiscard() {
        ignoredCards.removeAll(AbstractDungeon.player.hand.group);
        ArrayList<AbstractCard> discardedCurses = AbstractDungeon.player.discardPile
                .getCardsOfType(AbstractCard.CardType.CURSE)
                .group
                .stream()
                .filter(card -> !CardSealed.isSealed.get(card))
                .collect(Collectors.toCollection(ArrayList::new));
        discardedCurses.removeAll(DregsMod.postSealedCards);
        discardedCurses.removeAll(ignoredCards);
        discardedCurses.forEach(card -> addToBot(new SealAndPerformAction(card, null)));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }

    @Override
    public AbstractPower makeCopy() {
        return new TheVaultPower(owner);
    }
}
