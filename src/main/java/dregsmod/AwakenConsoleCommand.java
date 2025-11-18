package dregsmod;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import dregsmod.cards.AwakenedMod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

public class AwakenConsoleCommand extends ConsoleCommand {

    public AwakenConsoleCommand() {
        maxExtraTokens = 2;
        minExtraTokens = 0;
        requiresPlayer = true;
        simpleCheck = true;
    }

    private String withoutSpaces(String orig) {
        return orig.replaceAll(" ", "_");
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        if (depth == 1) {
            return (ArrayList<String>) AbstractDungeon.player.hand.group.stream()
                    .filter(AwakenedMod.eligibleToAwaken)
                    .map(c -> withoutSpaces(c.cardID))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        @SuppressWarnings("unused") String command = tokens[0];
        String cardId = tokens[1];
        if (AbstractDungeon.player == null) {
            DevConsole.log("Cannot awaken card - player is null");
            errorMsg(tokens);
            return;
        }
        if (depth < 1) {
            errorMsg(tokens);
            return;
        }
        Optional<AbstractCard> cardOrMissing = AbstractDungeon.player.hand.group.stream()
                .filter(c -> withoutSpaces(c.cardID).equals(cardId))
                .findFirst();
        if (!cardOrMissing.isPresent()) {
            DevConsole.log("Couldn't find card with id: " + cardId);
            errorMsg(tokens);
            return;
        }
        AbstractCard card = cardOrMissing.get();
        int times = 1;
        if (tokens.length > 2) {
            String timesToken = tokens[2];
            try {
                times = Integer.parseInt(timesToken);
            } catch (NumberFormatException e) {
                // Couldn't parse as an int, print the usage message
                DevConsole.log("Couldn't parse the times to awaken (got " + timesToken + ")");
                return;
            }
        }
        AwakenedMod.awakenCard(card, times);
    }

    @Override
    protected void errorMsg(String[] tokens) {
        DevConsole.couldNotParse();
        DevConsole.log("Usage: awaken {cardId} times(optional)");
    }
}
