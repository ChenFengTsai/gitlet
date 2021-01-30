package gitlet;

import java.util.List;

/**
 * Manages the global log command.
 * @author Wei-Min Chou
 */
public class GloLog {
    /**
     * Controls the global log.
     */
    public void glolog() {
        List<String> files = Utils.plainFilenamesIn(".gitlet/commit");
        for (String s : files) {
            if (!s.equals("curr")) {
                Commit me = Utils.readObject(Utils.join(Main.COMMIT, s),
                        Commit.class);
                Utils.message(me.toString());
            }
        }
    }
}
