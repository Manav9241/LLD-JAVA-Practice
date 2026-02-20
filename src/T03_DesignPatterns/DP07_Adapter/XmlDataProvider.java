package T03_DesignPatterns.DP07_Adapter;

public class XmlDataProvider {
    public String getXmlData(String data) {
        int separator = data.indexOf(':');
        String name = data.substring(0, separator);
        String id = data.substring(separator + 1);

        return "<user>"
                + "<name>" + name + "</name>"
                + "<id>" + id + "</id>"
                + "</user>";
    }
}
