package dregsmod.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import dregsmod.DregsMod;
import dregsmod.util.TextureLoader;

import static dregsmod.DregsMod.makeRelicOutlinePath;
import static dregsmod.DregsMod.makeRelicPath;

public class LuckyClover extends CustomRelic {

    public static final String ID = DregsMod.makeID(LuckyClover.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("LuckyClover.png"));
    private static final Texture OUTLINE = TextureLoader.getTexture(makeRelicOutlinePath("LuckyClover.png"));

    public LuckyClover() {
        super(ID, IMG, OUTLINE, RelicTier.RARE, LandingSound.FLAT);
        tips.clear();
        tips.add(new PowerTip(this.name, this.description));
        initializeTips();
    }

    private int curseCount() {
        return (int) AbstractDungeon.player.masterDeck
                .getCardsOfType(AbstractCard.CardType.CURSE).group.stream()
                .map(card -> card.cardID)
                .distinct()
                .count();
    }

    @Override
    public void atBattleStartPreDraw() {
        flash();
        int curseCount = curseCount();
        counter = curseCount;
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new PlatedArmorPower(AbstractDungeon.player, curseCount)));
    }

    @Override
    public void onObtainCard(AbstractCard c) {
        counter = curseCount();
    }

    @Override
    public void onEquip() {
        counter = curseCount();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }
}
