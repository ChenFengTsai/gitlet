package gitlet;


import java.util.List;
import java.io.File;


/**
 * A class that manages the add command.
 * @author Wei-Min Chou
 */
public class Add {
    /**
     * The filename that of the file is being added.
     */
    private String _file;

    /**
     * Current Commit.
     */
    private Commit curr;

    /**
     * The constructor of the add.
     * @param file Pass in the file
     */
    public void add(String file) {
        List<String> files = Utils.plainFilenamesIn(".");
        if (!files.contains(file)) {
            Utils.message("File does not exist.");
            System.exit(0);
        } else {
            _file = file;
            List<String> removed = Utils.plainFilenamesIn(Main.REMOVE);
            File comm = Utils.join(Main.COMMIT, "curr");
            Blob b = new Blob(Utils.readContents(Utils.join(".", file)));
            curr = Utils.readObject(comm, Commit.class);
            b.saveblob();
            Blob cu = null;
            if (curr.getcont().containsKey(file)) {
                cu = curr.getcont().get(file);
            }
            if (removed.contains(_file)) {
                File needrem = Utils.join(Main.REMOVE, _file);
                needrem.delete();
            }
            if (cu == null || !b.equals(cu)) {
                Utils.writeObject(Utils.join(Main.ADDDING, file), b);
                b.saveblob();
            }
        }
    }
}
