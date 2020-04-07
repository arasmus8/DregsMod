package dregsmod.cards;

import basemod.BaseMod;
import com.github.difflib.algorithm.DiffException;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface UpgradeTextChangingCard {
    String upgradePreviewText();

    default String diffText(String orig, String upgraded) {
        try {
            DiffRowGenerator generator = DiffRowGenerator.create()
                    .showInlineDiffs(true)
                    .inlineDiffByWord(true)
                    .newTag(start -> start ? "@DIFFSTART@" : "@DIFFEND@")
                    .build();
            List<DiffRow> rows = generator.generateDiffRows(Collections.singletonList(orig), Collections.singletonList(upgraded));
            String diffStr = rows.get(0).getNewLine();
            String[] parts = diffStr.split("@DIFFSTART@", 0);
            if (parts.length < 2) {
                // no diffs
                return upgraded;
            }
            String[] modified = Arrays.stream(parts)
                    .skip(1)
                    .map(s -> {
                        String[] parts2 = s.split("@DIFFEND@", 0);
                        if (parts2.length < 2) {
                            return s;
                        }
                        String[] words = parts2[0].split(" +", 0);
                        String[] wordsWithColor = Arrays.stream(words)
                                .map(s1 -> {
                                    if (s1.matches("NL|\\[E]")) return s1;
                                    if (BaseMod.getKeywordUnique(s1.toLowerCase()) != null) return s1;
                                    return "[#7fff00]" + s1 + "[]";
                                })
                                .toArray(String[]::new);
                        return String.join(" ", wordsWithColor) +
                                " " +
                                String.join(" ", Arrays.copyOfRange(parts2, 1, parts2.length));
                    })
                    .toArray(String[]::new);
            return parts[0] + " " + String.join(" ", modified);
        } catch (DiffException e) {
            e.printStackTrace();
            return upgraded;
        }
    }
}
