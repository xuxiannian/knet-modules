package cn.knet.domain.single;

import cn.knet.domain.Lock;
import cn.knet.gtld.epp.client.EPPClient;
import cn.knet.gtld.epp.client.EPPClientFactory;
import cn.knet.gtld.epp.codec.gen.interfaces.EPPResponse;
import cn.knet.gtld.epp.codec.gen.pojo.EPPResult;
import cn.knet.gtld.epp.transport.client.EPPSSLConfig;
import cn.knet.gtld.epp.transport.client.EPPSSLImpl;
import cn.knet.gtld.epp.util.EPPEnv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.IDN;
import java.util.Vector;

@Service
public class EppConnect {

    Logger logger = LoggerFactory.getLogger(getClass());

    String clientId;
    String password;
    String sslFile;
    String sslPass;
    String host;
    int port;

    EPPClient client = null;

    static int n = 0;

    private static EPPClientFactory factory = new EPPClientFactory();

    @Autowired(required = true)
    private EppConnect(@Value("${epp.clientId}") String clientId, @Value("${epp.password}") String password,
                       @Value("${epp.sslFile}") String sslFile, @Value("${epp.sslPass}") String sslPass,
                       @Value("${epp.host}") String host, @Value("${epp.port}") int port) throws Exception {
        this.password = password;
        this.clientId = clientId;
        this.sslFile = sslFile;
        this.sslPass = sslPass;
        this.port = port;
        this.host = host;
        this.init();
    }

    public EPPClient getClient() throws Exception {

        logger.info("当前客户端：" + client.toString());
		/*	logger.info("第{}次链接" ,n);
		if(n>=50){
			n=0;
			reConnect();
		}
		n++;n*/
        Vector<String> domainVec = new Vector<String>();
        String domain = IDN.toASCII("test.网址");
        domainVec.addElement(domain);
        EPPResponse resp = new EPPResponse();
        synchronized (Lock.getLOCK()) {
            resp = client.checkEPPDomain(domainVec);
        }
        if (resp == null || resp.getResult() == null) {
            logger.info("链接已断开！尝试重新连接！");
            reConnect();
        } else if (!resp.isSuccess()) {
            try {
                logger.info("未知错误：" + resp.getResult().getExtValues().get(0).getReason() + "，正尝试重新连接。");
            } catch (Exception e) {
                logger.info("未知错误，正尝试重新连接。");
            }
            reConnect();
        } else {
            logger.info("客户端链接正常！");
        }
        return client;
    }


    public void setClient(EPPClient client) {
        this.client = client;
    }

    public void reConnect() throws Exception {
        this.init();
    }

    void init() throws Exception {
        try {
            logger.info("{" + clientId + "/" + password + "}");

            EPPEnv.getInstance("epp-client.config");

            EPPSSLConfig theConfig = new EPPSSLConfig(EPPEnv.getSSLProtocol(), EPPEnv.getKeyStore(), sslFile, sslPass);
            theConfig.setIdentityKeyPassPhrase(EPPEnv.getSSLKeyPassPhrase());
            theConfig.setSslDebug(EPPEnv.getSSLDebug());
            theConfig.setTrustStore(EPPEnv.getTrustStoreType(), sslFile, sslPass);
            theConfig.setSSLEnabledProtocols(EPPEnv.getSSLEnabledProtocols());
            theConfig.setSSLEnabledCipherSuites(EPPEnv.getSSLEnabledCipherSuites());


            synchronized (this) {
                client = factory.getClientInstance(clientId, password, host, port, EPPSSLImpl.initialize(theConfig));// 获取客户端实例

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EPPResponse resp = client.login(null); // 获取登录命令的响应
        if (resp.getResult().getCode() == EPPResult.SUCCESS) { // 判断登录是否成功
            logger.info("EPP客户端初始化成功！");
        } else {
            logger.error("EPP客户端初始化失败！");
            throw new Exception("EPP客户端初始化失败！");

        }
    }

}
