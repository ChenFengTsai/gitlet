package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * Class that manages the blob command.
 * @author Wei-Min Chou
 */
public class Blob implements Serializable {
    /**
     * Blob.
     */
    private byte[] _file;

    /**
     * SHA-1 of blob.
     */
    private String _blobsha;

    /**
     * The constuctor for blob.
     * @param f pass in value of a blob
     */
    public Blob(byte[] f) {
        _file = f;
        this._blobsha = getblobsha();
    }

    /**
     * Retrieve the blob SHA-1.
     * @return String the SHA-1.
     */
    public String getblobsha() {
        _blobsha = Utils.sha1(_file);
        return _blobsha;
    }

    /**
     * Serialize the Blob.
     */
    public void saveblob() {
        Utils.writeObject(Utils.join(Main.BLOB, this._blobsha), this);
    }

    /**
     * Serialize the Blob.
     * @param name the name of the blob.
     */
    public void saveblobtoadd(String name) {
        List<String> files = Utils.plainFilenamesIn(Main.ADDDING);
        if (files != null && !files.contains(_blobsha)) {
            Utils.writeObject(Utils.join(Main.ADDDING, name), this);
        }
    }

    /**
     * Get the blob that has the SHA-1 value of name.
     * @param name SHA-1.
     * @return Blob returns the value of the blob
     */
    public static Blob getblob(String name) {
        File b = Utils.join(Main.BLOB, name);
        if (!b.exists()) {
            throw new IllegalArgumentException(
                    "No blob with that SHA-1");
        }
        return Utils.readObject(b, Blob.class);
    }

    /**
     * Get the file information of the blob.
     * @return byte[] the contents.
     */
    public byte[] getfile() {
        return this._file;
    }

    @Override
    public boolean equals(Object obj) {
        Blob c = (Blob) obj;
        return Arrays.equals(c._file, _file) && _blobsha.equals(c._blobsha);
    }

    @Override
    public int hashCode() {
        int result = 0;
        for (int i = 0; i < 6; i++) {
            result *= 5;
            result += (int) this._blobsha.charAt(i);
        }
        return result;
    }
}
