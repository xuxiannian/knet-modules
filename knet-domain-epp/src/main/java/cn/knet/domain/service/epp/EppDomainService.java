package cn.knet.domain.service.epp;

import cn.knet.domain.Lock;
import cn.knet.domain.exception.EppException;
import cn.knet.domain.single.EppConnect;
import cn.knet.domain.utils.DomUtil;
import cn.knet.domain.utils.EppUtils;
import cn.knet.domain.utils.StringUtil;
import cn.knet.domain.vo.DomainResult;
import cn.knet.gtld.epp.client.consts.EPPLaunchPhaseParam;
import cn.knet.gtld.epp.codec.domain.cmd.EPPDomainInfoCmd;
import cn.knet.gtld.epp.codec.domain.pojo.*;
import cn.knet.gtld.epp.codec.domain.resp.EPPDomainCheckResp;
import cn.knet.gtld.epp.codec.domain.resp.EPPDomainInfoResp;
import cn.knet.gtld.epp.codec.fee.*;
import cn.knet.gtld.epp.codec.gen.interfaces.EPPCommand;
import cn.knet.gtld.epp.codec.gen.interfaces.EPPResponse;
import cn.knet.gtld.epp.codec.gen.pojo.EPPAuthInfo;
import cn.knet.gtld.epp.codec.gen.pojo.EPPResult;
import cn.knet.gtld.epp.codec.launch.EPPLaunchCheckResult;
import cn.knet.gtld.epp.codec.launch.EPPLaunchChkData;
import cn.knet.gtld.epp.codec.launch.EPPLaunchNotice;
import cn.knet.gtld.epp.codec.rgp.ext.cmd.EPPRgpExtReport;
import cn.knet.gtld.epp.codec.rgp.ext.cmd.EPPRgpExtReportText;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.IDN;
import java.util.*;

@Service
public class EppDomainService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    EppConnect eppConnect;

    @Value("${domainSuffix}")
    String domainSuffix;

    /**
     * 新注
     *
     * @param domain   网址名称
     * @param rId      联系人ID（域名）
     * @param year     年数
     * @param password 密码
     * @return
     */
    public DomainResult create(String domain, String rId, int year, String password, String domainHosts, String lookupkey)
            throws Exception {

        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        // 获取价格
        EPPFeeDomain aDomains1 = new EPPFeeDomain();
        aDomains1.setCommand(new EPPFeeCommand("create"));
        aDomains1.setCurrency("CNY");
        aDomains1.setPeriod(new EPPFeePeriod(year));
        aDomains1.setName(domain1);
        EPPFeeCheck feeCheck = new EPPFeeCheck();
        feeCheck.addDomain(aDomains1);
        Vector<String> domainVec = new Vector<String>();
        domainVec.addElement(domain1);
        EPPResponse r;
        synchronized (Lock.getLOCK()) {
            r = eppConnect.getClient().checkEPPDomain(domainVec, feeCheck);
        }
        if (r instanceof EPPDomainCheckResp) {
            EPPDomainCheckResp res = (EPPDomainCheckResp) r;
            EPPFeeChkData feeData = (EPPFeeChkData) res.getExtension(EPPFeeChkData.class);
            EPPFeeValue feeVa = new EPPFeeValue(feeData.getCheckResults().get(0).getFees().get(0).getFee());
            List<EPPFeeValue> fees = new ArrayList<EPPFeeValue>();
            fees.add(feeVa);
            EPPFeeCreate eppFee = new EPPFeeCreate();
            eppFee.setCurrency("CNY");
            eppFee.setFees(fees);

            // 注册
            Vector<String> hostVector = EppUtils.getVector(domainHosts);
            Vector<EPPDomainContact> contactVector = EppUtils.getVector(
                    new EPPDomainContact(rId, EPPDomainContact.TYPE_ADMINISTRATIVE),
                    new EPPDomainContact(rId, EPPDomainContact.TYPE_BILLING),
                    new EPPDomainContact(rId, EPPDomainContact.TYPE_TECHNICAL));
            EPPDomainPeriod period = new EPPDomainPeriod(EPPDomainPeriod.PERIOD_UNIT_YEAR, year);
            EPPAuthInfo authInfo = new EPPAuthInfo(StringUtil.randomString(8, "1l0o5s"));
            EPPResult epprs;
            if (StringUtils.isBlank(lookupkey)) {
                synchronized (Lock.getLOCK()) {
                    epprs = eppConnect.getClient().createEPPDomain(domain1, hostVector, contactVector, period, authInfo,
                            rId, null, null, null, eppFee).getResult();
                }
            } else {
                ////调用tmdb接口取得 noticeId+two date
                Map<String, Object> maprs = this.getTmdbInfo(lookupkey);
                String noticeId = (String) maprs.get("id");
                String notBefore = (String) maprs.get("notBefore");
                String notAfter = (String) maprs.get("notAfter");

                Date notBeforeDate = EppUtils.utcDateFormart(notBefore);
                Date notAfterDate = EppUtils.utcDateFormart(notAfter);

                EPPLaunchNotice epplaunchnotice = new EPPLaunchNotice();
                epplaunchnotice.setAcceptedDate(notBeforeDate);
                epplaunchnotice.setNotAfterDate(notAfterDate);
                epplaunchnotice.setNoticeId(noticeId);
                synchronized (Lock.getLOCK()) {
                    epprs = eppConnect.getClient().createEPPDomainClaimsForm(domain1, hostVector, contactVector, period,
                            authInfo, rId, null, EPPLaunchPhaseParam.CLAIMS, epplaunchnotice, null, eppFee).getResult();
                }
            }

            if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
                logger.info("[网址][{}]创建成功！", domain);
                return DomainResult.success();
            } else {
                logger.info("[网址][{}]创建失败：{}", domain, epprs);
                return DomainResult.error(epprs);
            }
        } else {
            logger.info("[网址][{}]创建失败：{}", domain, r.getResult());
            return DomainResult.error(r.getResult());

        }

    }


    /**
     * 检查网址
     *
     * @param domain 网址名称
     * @return
     */
    public DomainResult check(String domain) throws Exception {
        List<DomainResult> l = checks(domain);
        return l.get(0);
    }

    /**
     * 检查一组网址
     *
     * @param domains 网址名称数组
     * @return
     */
    public List<DomainResult> checks(String... domains) throws Exception {
        List<DomainResult> l = new ArrayList<DomainResult>();
        Vector<String> domainVec = new Vector<String>();
        EPPFeeCheck feeCheck = new EPPFeeCheck();

        for (String domain : domains) {
            String domain1 = IDN.toASCII(domain + "." + domainSuffix);
            domainVec.addElement(domain1);
            EPPFeeDomain aDomains1 = new EPPFeeDomain();
            aDomains1.setCommand(new EPPFeeCommand("create"));
            aDomains1.setCurrency("CNY");
            aDomains1.setPeriod(new EPPFeePeriod(1));
            aDomains1.setName(domain1);
            feeCheck.addDomain(aDomains1);
        }
        EPPResponse resp = new EPPResponse();
        synchronized (Lock.getLOCK()) {
            resp = eppConnect.getClient().checkEPPDomain(domainVec, feeCheck);
        }
        int code = resp.getResult().getCode();
        if (code == 1000) {
            List<EPPDomainCheckResult> a = ((EPPDomainCheckResp) resp).getCheckResults();
            for (EPPDomainCheckResult r : a) {
                l.add(DomainResult.success().add("status", r.isAvailable()).add("domain", IDN.toUnicode(r.getName())));
            }
            return l;
        } else {
            throw new EppException(DomainResult.error(resp.getResult()));

        }
    }

    /**
     * 续费
     *
     * @param domain   网址
     * @param compDate 到期日期
     * @param year     续费年数
     * @return
     */
    public DomainResult renew(String domain, Date compDate, int year) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        EPPDomainPeriod period = new EPPDomainPeriod(year);
        EPPResult epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().renewEPPDomain(domain1, compDate, period).getResult();
        }
        if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
            logger.info("[网址][{}]续费{}年成功！", domain, year);
            return DomainResult.success();
        } else {
            logger.info("[网址][{}]续费{}年失败:" + epprs.getExtValues().get(0).getReason(), domain, year);
            return DomainResult.error(epprs);
        }
    }

    /**
     * 详情
     *
     * @param domain 网址
     * @return 网址信息data 强转：EPPDomainInfoResp
     */
    public DomainResult info(String domain) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().infoEPPDomain(domain1, EPPDomainInfoCmd.HOSTS_ALL, null);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            logger.info("[网址][{}]详情成功！", domain);
            return DomainResult.success((EPPDomainInfoResp) epprs);
        } else {
            logger.info("[网址][{}]详情失败:" + epprs.getResult(), domain);
            return DomainResult.error(epprs.getResult());
        }
    }

    /**
     * 删除
     *
     * @param domain 网址
     * @return
     * @throws Exception
     */
    public DomainResult delete(String domain) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        EPPResult epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().deleteEPPDomain(domain1).getResult();
        }
        if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
            logger.info("[网址][{}]删除成功！", domain);
            return DomainResult.success();
        } else {
            logger.info("[网址][{}]删除失败:" + epprs, domain);
            return DomainResult.error(epprs);
        }
    }

    /**
     * 赎回请求
     *
     * @param domain 网址
     * @return
     */
    public DomainResult restoreRequest(String domain) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        EPPResult epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().restoreRequestEPPDomain(domain1).getResult();
        }
        if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
            logger.info("[网址][{}]赎回请求成功！", domain);
            return DomainResult.success(epprs);
        } else {
            logger.info("[网址][{}]赎回请求失败:" + epprs, domain);
            return DomainResult.error(epprs);
        }
    }

    /**
     * 赎回报告
     *
     * @param domain     网址
     * @param deleteDate 删除日期（到期日期）
     * @return
     */
    public DomainResult restoreReport(String domain, Date deleteDate) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        // 这堆参数不知道是干啥，说是不能为空，可以瞎写
        EPPRgpExtReportText rgpReason = new EPPRgpExtReportText();
        rgpReason.setTagName(EPPRgpExtReportText.ELM_RES_REASON);
        rgpReason.setMessage("none");
        EPPRgpExtReportText rgpStatement1 = new EPPRgpExtReportText();
        rgpStatement1.setTagName(EPPRgpExtReportText.ELM_STATEMENT);
        rgpStatement1.setMessage("none");
        EPPRgpExtReportText rgpStatement2 = new EPPRgpExtReportText();
        rgpStatement2.setTagName(EPPRgpExtReportText.ELM_STATEMENT);
        rgpStatement2.setMessage("none");
        EPPRgpExtReport eppRgpExtReport = new EPPRgpExtReport("none", "none", deleteDate, new Date(), rgpReason,
                rgpStatement1);
        eppRgpExtReport.setStatement2(rgpStatement2);
        EPPResult epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().restoreReportEPPDomain(domain1, eppRgpExtReport).getResult();
        }
        if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
            logger.info("[网址][{}]赎回报告成功！", domain);
            return DomainResult.success(epprs);
        } else {
            logger.info("[网址][{}]赎回报告失败:" + epprs, domain);
            return DomainResult.error(epprs);
        }
    }

    /**
     * 过户
     *
     * @param domain          网址
     * @param eppRegistrantId 新注册人
     * @return
     */
    public DomainResult guohu(String domain, String eppRegistrantId, String oldEppRegistrantId) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        Map<String, EPPDomainAddRemove> itemMap = new Hashtable<String, EPPDomainAddRemove>();
        // 增加项
        EPPDomainAddRemove addItem = new EPPDomainAddRemove(null,
                EppUtils.getVector(new EPPDomainContact(eppRegistrantId, EPPDomainContact.TYPE_ADMINISTRATIVE),
                        new EPPDomainContact(eppRegistrantId, EPPDomainContact.TYPE_BILLING),
                        new EPPDomainContact(eppRegistrantId, EPPDomainContact.TYPE_TECHNICAL)),
                null);
        addItem.setMode(EPPDomainAddRemove.MODE_ADD);
        // 删除项
        EPPDomainAddRemove removeItem = new EPPDomainAddRemove(null,
                EppUtils.getVector(new EPPDomainContact(oldEppRegistrantId, EPPDomainContact.TYPE_ADMINISTRATIVE),
                        new EPPDomainContact(oldEppRegistrantId, EPPDomainContact.TYPE_BILLING),
                        new EPPDomainContact(oldEppRegistrantId, EPPDomainContact.TYPE_TECHNICAL)),
                null);
        removeItem.setMode(EPPDomainAddRemove.MODE_REMOVE);
        EPPDomainAddRemove changeItem = new EPPDomainAddRemove(eppRegistrantId, null);// 不需要密码
        changeItem.setMode(EPPDomainAddRemove.MODE_CHANGE);
        itemMap.put("add", addItem);
        itemMap.put("remove", removeItem);
        itemMap.put("change", changeItem);
        System.out.println(itemMap);
        EPPResult epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().updateEPPDomain(domain1, itemMap, null, null, false, false).getResult();
        }
        if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
            logger.info("[网址][{}]过户请求成功！", domain);
            return DomainResult.success(epprs);
        } else {
            logger.info("[网址][{}]过户请求失败:" + epprs, domain);
            return DomainResult.error(epprs);
        }
    }

    /**
     * 删除状态
     *
     * @param domain
     * @param addStatus
     * @return
     * @throws Exception
     */
    public DomainResult addStatus(String domain, String[] addStatus) throws Exception {
        return this.updateStatus(domain, null, addStatus);
    }

    public void clientHold(String domain) {
        try {
            updateStatus(domain, null, new String[]{"clientHold"});
        } catch (Exception e) {
            logger.error("[网址][{}]锁定失败:" + e, domain);
            e.printStackTrace();
        }
    }

    public void unClientHold(String domain) {
        try {
            updateStatus(domain, new String[]{"clientHold"}, null);
        } catch (Exception e) {
            logger.error("[网址][{}]解锁失败:" + e, domain);
            e.printStackTrace();
        }
    }

    /**
     * 增加状态
     *
     * @param domain
     * @param removeStatus
     * @return
     * @throws Exception
     */
    public DomainResult removeStatus(String domain, String removeStatus[]) {
        try {
            return this.updateStatus(domain, removeStatus, null);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * 变更状态
     *
     * @param domain       网址
     * @param removeStatus 要删除的状态:EPPDomainStatus 枚举
     * @param addStatus    要添加的状态:EPPDomainStatus 枚举
     * @return
     */
    public DomainResult updateStatus(String domain, String removeStatus[], String[] addStatus) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        try {
            Map<String, EPPDomainAddRemove> itemMap = new Hashtable<String, EPPDomainAddRemove>();

            Vector<EPPDomainStatus> removeStatusVec = EppUtils.getVector(EPPDomainStatus.class, removeStatus);
            Vector<EPPDomainStatus> addStatusVec = EppUtils.getVector(EPPDomainStatus.class, addStatus);
            EPPDomainAddRemove addItem = new EPPDomainAddRemove(null, null, addStatusVec);
            EPPDomainAddRemove removeItem = new EPPDomainAddRemove(null, null, removeStatusVec);
            itemMap.put("add", addItem);
            itemMap.put("remove", removeItem);
            EPPResult epprs;
            synchronized (Lock.getLOCK()) {
                epprs = eppConnect.getClient().updateEPPDomain(domain1, itemMap, null, null, false, false).getResult();
            }
            if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
                logger.info("[网址][{}]状态变更成功！", domain);
                return DomainResult.success(epprs);
            } else {
                logger.info("[网址][{}]状态变更失败:" + epprs, domain);
                return DomainResult.error(epprs);
            }
        } catch (Exception e) {
            logger.info("[网址][{}]状态变更失败:系统异常", domain);
            return DomainResult.error(1099, "系统异常");

        }

    }

    /**
     * 变更ns记录
     *
     * @param domain
     * @param removeHost
     * @param addHost
     * @return
     * @throws Exception
     */
    public DomainResult updateHost(String domain, String removeHost[], String[] addHost) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        try {
            Map<String, EPPDomainAddRemove> itemMap = new Hashtable<String, EPPDomainAddRemove>();
            Vector<String> removeHotsVec = EppUtils.getVector(removeHost);
            Vector<String> addHostVec = EppUtils.getVector(addHost);
            EPPDomainAddRemove addItem = new EPPDomainAddRemove(addHostVec, null, null);
            EPPDomainAddRemove removeItem = new EPPDomainAddRemove(removeHotsVec, null, null);
            itemMap.put("add", addItem);
            itemMap.put("remove", removeItem);
            EPPResult epprs;
            synchronized (Lock.getLOCK()) {
                epprs = eppConnect.getClient().updateEPPDomain(domain1, itemMap, null, null, false, false).getResult();
            }
            if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
                logger.info("[网址][{}]HOST变更成功！", domain);
                return DomainResult.success(epprs);
            } else {
                logger.info("[网址][{}]HOST变更失败:" + epprs, domain);
                return DomainResult.error(epprs);
            }
        } catch (Exception e) {
            logger.info("[网址][{}]HOST变更失败:系统异常", domain);
            return DomainResult.error(1099, "系统异常");
        }
    }

    public DomainResult updatePassword(String domain, String password) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        // 构造待添加数据
        try {
            Map<String, EPPDomainAddRemove> itemMap = new Hashtable<String, EPPDomainAddRemove>();
            EPPDomainAddRemove changeItem = new EPPDomainAddRemove(null, new EPPAuthInfo(password));
            itemMap.put("change", changeItem);
            EPPResult epprs;
            synchronized (Lock.getLOCK()) {
                epprs = eppConnect.getClient().updateEPPDomain(domain1, itemMap, null, null, false, false).getResult();
            }
            if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
                logger.info("[网址][{}]密码变更成功！", domain);
                return DomainResult.success(epprs);
            } else {
                logger.info("[网址][{}]密码变更失败:" + epprs, domain);
                return DomainResult.error(epprs);
            }
        } catch (Exception e) {
            logger.info("[网址][{}]状态变更失败:系统异常", domain);
            return DomainResult.error(1099, "系统异常");

        }

    }

    public DomainResult getLookUpKey(String domain) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        Vector<String> domainVector = EppUtils.getVector(domain1);
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().checkEPPDomainLaunchClaims(domainVector, EPPLaunchPhaseParam.CLAIMS);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            return DomainResult.success(epprs);
        } else {
            return DomainResult.error(epprs.getResult());
        }
    }

    public void createEPPHost(String a) throws Exception {
        synchronized (Lock.getLOCK()) {
            eppConnect.getClient().createEPPHost(a, null);
        }
    }

    public DomainResult poll() throws Exception {
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = eppConnect.getClient().poll();
        }
        return DomainResult.success(epprs).add("class", epprs.getClass().toString());
    }

    public DomainResult tranApprove(String domain) throws Exception {
        EPPResponse epprs;
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        synchronized (Lock.getLOCK()) {
            // 发送命令并获取响应
            epprs = eppConnect.getClient().transferEPPDomain(domain1, EPPCommand.OP_APPROVE, null, null);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            return DomainResult.success(epprs);
        } else {
            return DomainResult.error(epprs.getResult());
        }
    }

    public DomainResult tranRequest(String domain, String tranPwd) throws Exception {
        EPPResponse epprs;
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        synchronized (Lock.getLOCK()) {
            // 发送命令并获取响应
            EPPAuthInfo authInfo = new EPPAuthInfo(tranPwd);
            EPPDomainPeriod period = new EPPDomainPeriod(EPPDomainPeriod.PERIOD_UNIT_YEAR, 1);
            epprs = eppConnect.getClient().transferEPPDomain(domain1, EPPCommand.OP_REQUEST, authInfo, period);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            return DomainResult.success(epprs);
        } else {
            return DomainResult.error(epprs.getResult());
        }
    }

    public DomainResult checkTMCH(String domain) throws Exception {
        EPPResponse epprs;
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        Vector<String> domains = new Vector<String>();
        domains.addElement(domain1);
        synchronized (Lock.getLOCK()) {
            // 发送命令并获取响应
            epprs = eppConnect.getClient().checkEPPDomainLaunchClaims(domains, EPPLaunchPhaseParam.CLAIMS);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            EPPLaunchChkData ckData = (EPPLaunchChkData) epprs.getExtension(EPPLaunchChkData.class);
            EPPLaunchCheckResult result = ckData.getCheckResults().get(0);
            System.out.println("check is exist:" + result.isExists());
            System.out.println("check rs lookUpKey:" + result.getClaimKey());
            System.out.println("check domain:" + result.getName());

            if (result.isExists()) {
                return DomainResult.success(result.getClaimKey());
            } else {
                return DomainResult.error(epprs.getResult());
            }
        } else {
            return DomainResult.error(epprs.getResult());
        }
    }


    public Map<String, Object> getTmdbInfo(String lookUpKey) throws HttpException, IOException, DocumentException {


        Map<String, Object> map = new HashMap<String, Object>();


        HttpClient client = new HttpClient();

        client.getState().setCredentials(new AuthScope("tmcnis.org", 443),
                new UsernamePasswordCredentials("cnis4123", "rjs2016wz"));

        GetMethod get = new GetMethod("https://tmcnis.org/cnis/" + lookUpKey + ".xml");

        get.setDoAuthentication(true);

        int status = client.executeMethod(get);
        String info = get.getResponseBodyAsString();
        Document document = DocumentHelper.parseText(info);
        map = DomUtil.Dom2Map(document);
        return map;

    }

}
