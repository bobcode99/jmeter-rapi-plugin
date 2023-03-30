/*
 * @link    https://github.com/fourcolor/sideex-api-java
 * @author  fourcolor
 */

package team.sideex.api.config;

public class Report {
    private String type = "json";
    private int snapshot = 0;
    private int snapshotQuality = 20;
    private boolean oneLine = true;

    public int getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(int snapshot) {
        if (snapshot >= 0 && snapshot <= 2)
            this.snapshot = snapshot;
        else {
            throw new Error("Unsupport snapshot value " + snapshot);
        }
    }

    public int getSnapshotQuality() {
        return snapshotQuality;
    }

    public void setSnapshotQuality(int snapshotQuality) {
        if (snapshotQuality >= 0 && snapshotQuality <= 100)
            this.snapshotQuality = snapshotQuality;
        else {
            throw new Error("Unsupport snapshot value " + snapshotQuality);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        if (type.equals("json") || type.equals("html") || type.equals("all"))
            this.type = type;
        else {
            throw new Error("Unknown type " + type);
        }
    }

    public boolean getOneLine() {
        return oneLine;
    }

    public void setOneLine(boolean oneLine){
        this.oneLine = oneLine;
    }
}
