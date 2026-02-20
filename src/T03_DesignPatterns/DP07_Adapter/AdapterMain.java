package T03_DesignPatterns.DP07_Adapter;

public class AdapterMain {
    public static void main(String[] args) {
        XmlDataProvider xmlProvider = new XmlDataProvider();

        IReports adapter = new XmlProviderAdapter(xmlProvider);

        String rawData = "Alice:24";

        System.out.println("Processed JSON: \n" +
            adapter.getJsonData(rawData));

        (new Client()).getReport(adapter, rawData);
    }
}
