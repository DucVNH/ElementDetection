package core.entity;

public class HistoryEntry {
    private String updateDate;
    private String oldRobulaXPath;
    private String newRobulaXPath;

    private String oldMontotoXPath;
    private String newMontotoXPath;

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getOldRobulaXPath() {
        return oldRobulaXPath;
    }

    public void setOldRobulaXPath(String oldXPath) {
        this.oldRobulaXPath = oldXPath;
    }

    public String getNewRobulaXPath() {
        return newRobulaXPath;
    }

    public void setNewRobulaXPath(String newXPath) {
        this.newRobulaXPath = newXPath;
    }

    public String getOldMontotoXPath() {
        return oldMontotoXPath;
    }

    public void setOldMontotoXPath(String oldXPath) {
        this.oldMontotoXPath = oldXPath;
    }

    public String getNewMontotoXPath() {
        return newMontotoXPath;
    }

    public void setNewMontotoXPath(String newXPath) {
        this.newMontotoXPath = newXPath;
    }
}
