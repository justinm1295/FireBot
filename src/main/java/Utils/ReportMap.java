package Utils;

import java.util.HashMap;

public class ReportMap {

    private HashMap<Long, Long> reports;

    public ReportMap() {
        this.reports = new HashMap<>();
    }

    public void addReport(long messageId, long userId) {
        reports.put(messageId, userId);
    }

    public long getReport(long messageId) {
        if (reports.get(messageId) != null) {
            return reports.get(messageId);
        } else {
            return 0L;
        }
    }

    public int getReportSize() {
        return reports.size();
    }
}
