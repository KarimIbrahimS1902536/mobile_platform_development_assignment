//ibrahim_karim_s1902536
package finals.task.ibrahim_karims_1902536.api;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import finals.task.ibrahim_karims_1902536.modelclasses.WorksModel;

public class FetchApis implements Callable<ArrayList<WorksModel>> {
    private final String urlString;
    private ArrayList<WorksModel> arrayList = new ArrayList<>();
    private Boolean itemTagStart = false;

    public FetchApis(String url) {
        this.urlString = url;
    }

    @Override
    public ArrayList<WorksModel> call() throws IOException, XmlPullParserException {
        URLConnection yc;
        BufferedReader in;
        String inputLine;
        URL url = new URL(urlString);
        yc = url.openConnection();
        in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        StringBuilder result = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            result.append(inputLine);
        }
        in.close();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        WorksModel worksModel = null;

        xpp.setInput(new StringReader(result.toString()));
        int eventType = xpp.getEventType();
        String tag = "", text = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {
            tag = xpp.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tag.equals("item")) {
                        itemTagStart = true;
                        worksModel = new WorksModel();
                    }
                    break;
                case XmlPullParser.TEXT:
                    text = xpp.getText();
                    break;
                case XmlPullParser.END_TAG:
                    if (itemTagStart) {
                        switch (tag) {
                            case "title":
                                worksModel.setTitle(text);
                                break;
                            case "description":
                                worksModel.setDescription(text);
                                break;
                            case "location":
                                worksModel.setLink(text);
                                break;
                            case "pubDate":
                                worksModel.setPublishDate(text);
                                break;
                            case "point":
                                worksModel.setLocPoints(text);
                                break;
                            case "item":
                                WorksModel worksModeltemp = new WorksModel();
                                worksModeltemp.setTitle(worksModel.getTitle());
                                worksModeltemp.setDescription(worksModel.getDescription());
                                worksModeltemp.setLink(worksModel.getLink());
                                worksModeltemp.setPublishDate(worksModel.getPublishDate());
                                worksModeltemp.setLocPoints(worksModel.getLocPoints());
                                arrayList.add(worksModeltemp);
                                itemTagStart = false;
                                break;
                        }
                    }
                    break;
            }
            eventType = xpp.next();
        }

        return arrayList;
    }
}