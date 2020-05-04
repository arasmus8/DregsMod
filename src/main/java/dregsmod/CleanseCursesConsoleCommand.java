package dregsmod;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.AbstractCleansingCurse;

public class CleanseCursesConsoleCommand extends ConsoleCommand {

    public CleanseCursesConsoleCommand() {
        maxExtraTokens = 0;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (AbstractDungeon.player == null) {
            DevConsole.log("Cannot generate card - player is null");
        } else {
            AbstractDungeon.player.hand.group.stream()
                    .filter(c -> c instanceof AbstractCleansingCurse)
                    .forEach(c -> ((AbstractCleansingCurse) c).cleanseBy(99));
        }

    }
}
