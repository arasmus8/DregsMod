package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import basemod.abstracts.CustomSavable;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class FracturedPrism extends CustomRelic implements CustomSavable<String> {
    public static final String ID = DregsMod.makeID(FracturedPrism.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("FracturedPrism.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("FracturedPrism.png"));

    private boolean cardSelected;
    private boolean interruptingScreen = false;
    private AbstractRoom.RoomPhase previousPhase;
    public String cardId = null;

    public FracturedPrism() {
        super(ID, IMG, OUTLINE, RelicTier.SHOP, LandingSound.MAGICAL);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    @Override
    public void onEquip() {
        cardSelected = false;
        counter = 5;
        if (AbstractDungeon.isScreenUp) {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.overlayMenu.cancelButton.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
        }

        previousPhase = AbstractDungeon.getCurrRoom().phase;
        AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
        AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, DESCRIPTIONS[1], false, false, false, false);
        interruptingScreen = true;
    }

    @Override
    public void update() {
        super.update();
        if (cardId != null) {
            cardSelected = true;
            if (interruptingScreen) {
                interruptingScreen = false;
                AbstractDungeon.getCurrRoom().phase = previousPhase;
            }
            return;
        }
        if (!cardSelected && AbstractDungeon.gridSelectScreen.selectedCards.size() == 1) {
            cardSelected = true;
            cardId = AbstractDungeon.gridSelectScreen.selectedCards.get(0).cardID;
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.COMPLETE;
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public String onSave() {
        return cardId;
    }

    @Override
    public void onLoad(String s) {
        cardId = s;
        if (cardId == null && counter > 0) {
            if (AbstractDungeon.isScreenUp) {
                AbstractDungeon.dynamicBanner.hide();
                AbstractDungeon.overlayMenu.cancelButton.hide();
                AbstractDungeon.previousScreen = AbstractDungeon.screen;
            }

            cardSelected = false;
            previousPhase = AbstractDungeon.getCurrRoom().phase;
            AbstractDungeon.getCurrRoom().phase = AbstractRoom.RoomPhase.INCOMPLETE;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, DESCRIPTIONS[1], false, false, false, false);
            interruptingScreen = true;
        } else {
            cardSelected = true;
        }
    }
}
