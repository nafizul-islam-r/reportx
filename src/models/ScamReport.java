package models;

public class ScamReport {
    private int userId;
    private String scamType;
    private String scammerName;
    private String scammerInfo;
    private String description;
    private String status;

    public ScamReport(int userId, String scamType, String scammerName, String scammerInfo, String description) {
        this.userId = userId;
        this.scamType = scamType;
        this.scammerName = scammerName;
        this.scammerInfo = scammerInfo;
        this.description = description;
        this.status = "Pending";
    }

    public int getUserId() { return userId; }
    public String getScamType() { return scamType; }
    public String getScammerName() { return scammerName; }
    public String getScammerInfo() { return scammerInfo; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
}
