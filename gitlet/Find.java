package gitlet;

import java.util.List;

/**
 * Manages the find command.
 * @author Wei-Min Chou
 */
public class Find {
    /**
     * Find the commit with the message.
     * @param mess message;
     */
    public void find(String mess) {
        boolean has = false;
        List<String> files = Utils.plainFilenamesIn(".gitlet/commit");
        for (String s : files) {
            if (!s.equals("curr")) {
                Commit me = Utils.readObject(Utils.join(Main.COMMIT, s),
                        Commit.class);
                if (me.getmess().equals(mess)) {
                    has = true;
                    System.out.println(me.getsha());
                }
            }
        }
        if (!has) {
            Utils.message("Found no commit with that message.");
            System.exit(0);
        }
    }
}
