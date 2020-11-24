package cn.knet.domain;

import com.baidubce.services.aipage.AiPageClient;
import com.baidubce.services.aipage.model.AiPageListRequest;
import com.baidubce.services.aipage.model.AiPageResponse;

public class AipageUtils {

    private static String AK = "e5d7515f4c114asdfawettgv17dad18";
    private static String SK = "5df5wettweee98abb0ec7d679300b9";


    public static void main(String[] args) {
        //创建站点
//        AiPageClient client = AiPageClient.createAiPageClient(AK, SK, "aipage.baidubce.com");
//        AiPageCreateRequest c = new AiPageCreateRequest(new SiteCreateItemModel("基础版测试", 1, 1));
//        AiPageListResponse res = client.createAiPage(c);
//        System.out.println(res);

        //站点续费
//        AiPageClient client = AiPageClient.createAiPageClient(AK, SK, "aipage.baidubce.com");
//        AiPageRenewRequest c = new AiPageRenewRequest(new SiteRenewItemModel("16778c56-9a47-43c1-b1d7-19a1acdd94a7", "1"));
//        AiPageListResponse res = client.renewAiPage (c);
//        System.out.println(res);


        //站点列表
//        AiPageClient client = AiPageClient.createAiPageClient(AK, SK, "aipage.baidubce.com");
//        AiPageListResponse res=client.listAiPage(new AiPageListRequest());
//        System.out.println(res);


        //站点详细
        AiPageClient client = AiPageClient.createAiPageClient(AK, SK, "aipage.baidubce.com");
        AiPageResponse res=client.infoAiPage(new AiPageListRequest());
        System.out.println("res.getMsg():"+res.getMsg()+" res.getSuccess():"+res.getSuccess()+" res.getStatus():"+res.getStatus()+"");
    }
}