package Utils;

import java.util.HashMap;

public class ReportMap {

    private HashMap<Long, Long> reports;

    public ReportMap() {
        this.reports = new HashMap<>();
    }

    public void addReport(long messageId, long userId) {
        this.reports.put(messageId, userId);
    }

    public long getReport(long messageId) {
        if (this.reports.get(messageId) != null) {
            return this.reports.get(messageId);
        } else {
            return 0L;
        }
    }

    public int getReportSize() {
        return this.reports.size();
    }
}
