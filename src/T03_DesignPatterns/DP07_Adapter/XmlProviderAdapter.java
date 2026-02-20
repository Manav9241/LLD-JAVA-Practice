package T03_DesignPatterns.DP07_Adapter;

public class XmlProviderAdapter implements IReports{
    private final XmlDataProvider dataProvider;

    public XmlProviderAdapter(XmlDataProvider provider) {
        this.dataProvider = provider;
    }

    @Override
    public String getJsonData(String data) {
        String xml = dataProvider.getXmlData(data);

        int startName = xml.indexOf("<name>") + 6;
        int endName   = xml.indexOf("</name>");
        String name   = xml.substring(startName, endName);

        int startId = xml.indexOf("<id>") + 4;
        int endId   = xml.indexOf("</id>");
        String id    = xml.substring(startId, endId);

        // 3. Build and return JSON
        return "{\"name\":\"" + name + "\", \"id\":" + id + "}";
    }
}
