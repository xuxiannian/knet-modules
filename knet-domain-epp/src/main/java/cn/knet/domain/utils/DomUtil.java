package cn.knet.domain.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class DomUtil {

    public static void main(String[] args) throws Exception {
        HttpClient client = new HttpClient();
        String username = "cnis404";
        String password = "gtld@0909";

        client.getState().setCredentials(
                new AuthScope("test.tmcnis.org", 443),
                new UsernamePasswordCredentials(username, password)
        );

        String lookUpKey = "2014062000/2/a/2/KiWzNbmFuBxUSYMUFk";
        String url = "https://test.tmcnis.org/cnis/" + lookUpKey + ".xml";

        GetMethod get = new GetMethod(url);

        get.setDoAuthentication(true);

        try {
            int status = client.executeMethod(get);
            System.out.println(status + "\n" + get.getResponseBodyAsString());

            //InputStream inputStream =  get.getResponseBodyAsStream();
            String info = get.getResponseBodyAsString();

            Document document = DocumentHelper.parseText(info);

            DomUtil e = new DomUtil();
            Map<String, Object> map = e.Dom2Map(document);
            System.out.println(map);

        } finally {
            // release any connection resources used by the method
            get.releaseConnection();
        }
    }


    public static Map<String, Object> Dom2Map(Document doc) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
            return map;
        Element root = doc.getRootElement();
        //Element root2 = (Element) doc.getDocumentElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            //System.out.println(e.getName());
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), Dom2Map(e));
            } else
                map.put(e.getName(), e.getText());
        }
        return map;
    }

    public static Map Dom2Map(Element e) {
        Map map = new HashMap();
        List list = e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List mapList = new ArrayList();

                if (iter.elements().size() > 0) {
                    Map m = Dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), m);
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), iter.getText());
                }
            }
        } else
            map.put(e.getName(), e.getText());
        return map;
    }


}
