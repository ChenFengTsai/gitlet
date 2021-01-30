package gitlet;


import java.io.File;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


/**
 * Manages the commit command.
 * @author Wei-Min Chou
 */
public class Commit implements Serializable {
    /**
     * Commit Time.
     */
    private Date _committime;
    /**
     * Commit message.
     */
    private String _message;
    /**
     * Parent Commit.
     */
    private Commit _parent;
    /**
     * SHA-1.
     */
    private String _commitsha;

    /**
     * Curr.
     */
    private String curr = "curr";

    /**
     * merge parent.
     */
    private Commit _merge = null;

    /**
     * Maps all the files name to its blob.
     */
    private HashMap<String, Blob> _contents = new HashMap<>();

    /**
     * All cases of commit.
     * @param message the commit message
     * @param committime the time of commit
     * @param parentcommit parent commit
     * @param content hashmap of all the blobs
     * @param mergeparent the parent caused by merge
     */
    public void commit(String message,
                       Date committime,
                       Commit parentcommit,
                       Commit mergeparent,
                       HashMap<String, Blob> content) {
        _message = message;
        _committime = getdate(true);
        _parent = parentcommit;
        _contents = content;
        _merge = mergeparent;
        List<String> files = Utils.plainFilenamesIn(".gitlet/add");
        List<String> rmfiles = Utils.plainFilenamesIn(".gitlet/remove");
        if (_parent != null) {
            if (files.size() == 0 && rmfiles.size() == 0) {
                Utils.message("No changes added to the commit.");
                System.exit(0);
            }
        } else {
            _committime = getdate(false);
        }
        if (_contents == null) {
            _contents = new HashMap<>();
        }
        _commitsha = getcommitsha1();
        for (String s : files) {
            File f = Utils.join(Main.ADDDING, s);

            _contents.put(s, Utils.readObject(f, Blob.class));
            f.delete();
        }
        for (String s : rmfiles) {
            if (_contents != null && _contents.keySet().contains(s)) {
                File r = Utils.join(Main.REMOVE, s);
                _contents.remove(s);
                r.delete();
            }
        }
        savecommit();
    }

    /**
     * The initial commit.
     */
    public void commit() {
        commit("initial commit", getdate(false), null, null, null);
        Utils.writeObject(Utils.join(Main.BRANCH, "master"), this);
    }

    /**
     * Get the time for the commit.
     * @param time if it is the first commit
     * @return The date of the current or first
     */
    public Date getdate(boolean time) {
        if (time) {
            Timestamp t = new Timestamp(System.currentTimeMillis());
            _committime = new Date(t.getTime());
        } else {
            _committime = new Date(0);
        }
        return _committime;
    }

    /**
     * Reads the commit with the SHA1 name.
     * @param name the name of the SHA
     * @return commit class
     */
    public static Commit getcommit(String name) {
        File commitfile = Utils.join(Main.COMMIT, name);
        if (!commitfile.exists()) {
            throw new IllegalArgumentException(
                    "No commit with that SHA-1");
        }
        return Utils.readObject(commitfile, Commit.class);
    }

    /**
     * Serialize the commit.
     */
    public void savecommit() {
        String branch = Utils.readObject(Utils.join(Main.BRANCH, "curr"),
                String.class);
        Utils.writeObject(Utils.join(Main.COMMIT, this._commitsha), this);
        Utils.writeObject(Utils.join(Main.COMMIT, this.curr), this);
        Utils.writeObject(Utils.join(Main.BRANCH, branch), this);
    }

    /**
     * Retrieve the commit SHA-1.
     * @return the SHA value
     */
    public String getcommitsha1() {
        byte[] res = Utils.serialize(this);
        return Utils.sha1(res);
    }

    /**
     * Make the commit a string.
     */
    @Override
    public String toString() {
        String time = _committime.toString().replace(" PST", "");
        time += " -0800";
        return String.format(
                "===\ncommit %s\nDate: %s\n%s\n",
                _commitsha, time, _message);
    }

    /**
     * Get the contents.
     * @return Hashmap
     */
    public HashMap<String, Blob> getcont() {
        return _contents;
    }

    /**
     * Get the parent of the commit.
     * @return the parent of the commit
     */
    public Commit getparent() {
        return _parent;
    }

    /**
     * Get the merge of the commit.
     * @return the merge parent.
     */
    public Commit getmerge() {
        return _merge;
    }

    /**
     * Get the sha of the commit.
     * @return sha value
     */
    public String getsha() {
        return _commitsha;
    }

    /**
     * Get the message.
     * @return message
     */
    public String getmess() {
        return _message;
    }
}
