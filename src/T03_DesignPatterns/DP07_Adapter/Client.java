package T03_DesignPatterns.DP07_Adapter;

public class Client {
    public void getReport(IReports report, String rawData) {
        System.out.println("Processed JSON: \n" + report.getJsonData(rawData));
    }
}
