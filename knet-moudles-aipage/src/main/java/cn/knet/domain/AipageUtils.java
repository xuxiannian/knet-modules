package cn.knet.domain;

import com.baidubce.services.aipage.AiPageClient;
import com.baidubce.services.aipage.model.AiPageCreateRequest;
import com.baidubce.services.aipage.model.AiPageCreateResponse;
import com.baidubce.services.aipage.model.SiteCreateItemModel;

public class AipageUtils {

    private static String AK = "e5d7515f4c114174baad193f017dad18";
    private static String SK = "5df5c5ccbf664398abb0ec7d679300b9";


    public static void main(String[] args) {
//


        AiPageClient client = AiPageClient.createAiPageClient(AK, SK, "aipage.baidubce.com");
        AiPageCreateRequest c = new AiPageCreateRequest(new SiteCreateItemModel("test1", 1, 1));
        AiPageCreateResponse res = client.createAiPage(c);
        // AiPageListResponse res=client.infoAiPage(new AiPageListRequest());
        System.out.println(res);
    }
}