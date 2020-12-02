package cn.knet.domain.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.vod.model.v20170321.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AliUploadUtils {

    private static final Logger logger = LoggerFactory.getLogger(AliUploadUtils.class);


    public static DefaultAcsClient initVodClient(String accessKeyId, String accessKeySecret){
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessKeySecret);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        logger.info(client.toString());
        return client;
    }

    /**
     * 刷新视频上传凭证
     * @param client 发送请求客户端
     * @return RefreshUploadVideoResponse 刷新视频上传凭证响应数据
     * @throws Exception
     */
    public static RefreshUploadVideoResponse refreshUploadVideo(DefaultAcsClient client,String videoId) throws Exception {
        RefreshUploadVideoRequest request = new RefreshUploadVideoRequest();
        request.setVideoId(videoId);
        return client.getAcsResponse(request);
    }


    /**
     * 获取视频上传地址和凭证
     * @param client 发送请求客户端
     * @return CreateUploadVideoResponse 获取视频上传地址和凭证响应数据
     * @throws Exception
     */
    public static CreateUploadVideoResponse createUploadVideo(DefaultAcsClient client,String title, String fileName) throws Exception {
        CreateUploadVideoRequest request = new CreateUploadVideoRequest();
        request.setTitle(title);
        request.setFileName(fileName);
        return client.getAcsResponse(request);
    }

    /**
     * 提交媒体截图处理作业调用函数
     */
    public static SubmitSnapshotJobResponse submitSnapshotJob(DefaultAcsClient client,String videoId) throws Exception {
        SubmitSnapshotJobRequest request = new SubmitSnapshotJobRequest();
        //需要截图的视频ID(推荐传递截图模板ID)
        request.setVideoId(videoId);
        //截图模板ID
        request.setSnapshotTemplateId("06f770701da7ebd6073fd4e4745f03de");
        return client.getAcsResponse(request);
    }

    
    /**
     * 获取视频地址
     * @param client
     * @return
     * @throws Exception
     */
    public static GetPlayInfoResponse getPlayInfo(DefaultAcsClient client,String vidoId) throws Exception {
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        request.setVideoId(vidoId);
        return client.getAcsResponse(request);
    }


    /**
     * 查询截图数据
     */
    public static ListSnapshotsResponse listSnapshots(DefaultAcsClient client,String videoId) throws ClientException {
        ListSnapshotsRequest request = new ListSnapshotsRequest();
        //视频ID
        request.setVideoId(videoId);
        //截图类型
        request.setSnapshotType("NormalSnapshot");
        request.setPageNo("1");
        request.setPageSize("8");

        return client.getAcsResponse(request);
    }
}
