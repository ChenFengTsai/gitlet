package gitlet;

/**
 * Manages the init command.
 * @author Wei-Min Chou
 */
public class Init {
    /**
     * The init command process.
     */
    public void init() {
        if (Utils.plainFilenamesIn(".gitlet") != null) {
            Utils.message("A Gitlet version-control system already exists "
                    + "in the current directory.");
            System.exit(0);
        } else {
            setuppersistence();
            Commit c = new Commit();
            c.commit();
        }
    }

    /**
     * Set up all the files for persistence.
     */
    public void setuppersistence() {
        Main.MAIN_FOLDER.mkdirs();
        Main.ADDDING.mkdirs();
        Main.COMMIT.mkdirs();
        Main.REMOVE.mkdirs();
        Main.BLOB.mkdirs();
        Main.BRANCH.mkdirs();
        Utils.writeObject(Utils.join(Main.BRANCH, "curr"), "master");
    }
}
