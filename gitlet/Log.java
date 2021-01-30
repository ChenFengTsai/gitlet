package gitlet;

/**
 * Manages the log.
 * @author Wei-Min Chou
 */
public class Log {
    /**
     * curr.
     */
    private String curr = "curr";

    /**
     * Manages the log command.
     */
    public void log() {
        String w = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
                String.class);
        Commit me = Utils.readObject(Utils.join(Main.BRANCH, w), Commit.class);
        while (me != null) {
            Utils.message(me.toString());
            if (me.getparent() != null) {
                me = Utils.readObject(Utils.join(Main.COMMIT,
                        me.getparent().getsha()),
                        Commit.class);
            } else {
                me = null;
            }
        }
    }
}
