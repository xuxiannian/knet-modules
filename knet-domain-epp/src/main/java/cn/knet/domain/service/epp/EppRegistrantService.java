package cn.knet.domain.service.epp;

import java.net.IDN;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.knet.domain.Lock;
import cn.knet.domain.single.EppConnect;
import cn.knet.domain.vo.DomainResult;
import cn.knet.gtld.epp.client.EPPClient;
import cn.knet.gtld.epp.client.pojo.ContactBasicInfo;
import cn.knet.gtld.epp.codec.contact.pojo.EPPContactAddChange;
import cn.knet.gtld.epp.codec.contact.pojo.EPPContactAddress;
import cn.knet.gtld.epp.codec.contact.pojo.EPPContactDisclose;
import cn.knet.gtld.epp.codec.contact.pojo.EPPContactPostalDefinition;
import cn.knet.gtld.epp.codec.contact.resp.EPPContactCheckResp;
import cn.knet.gtld.epp.codec.contact.resp.EPPContactInfoResp;
import cn.knet.gtld.epp.codec.domain.cmd.EPPDomainInfoCmd;
import cn.knet.gtld.epp.codec.domain.pojo.EPPDomainAddRemove;
import cn.knet.gtld.epp.codec.domain.pojo.EPPDomainContact;
import cn.knet.gtld.epp.codec.domain.resp.EPPDomainInfoResp;
import cn.knet.gtld.epp.codec.gen.interfaces.EPPResponse;
import cn.knet.gtld.epp.codec.gen.pojo.EPPResult;

@Service
public class EppRegistrantService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    EppConnect eppConnect;
    @Value("${domainSuffix}")
    String domainSuffix;

    /**
     * 创建联系人
     *
     * @param registrantId 网址联系人ID
     * @param password     密码
     * @param email        邮箱
     * @param fax          传真
     * @param faxExt       传真分机号
     * @param tel          电话
     * @param telExt       电话分机号
     * @param address      地址
     * @param city         城市
     * @param rovince      省份
     * @param postalCode   邮编
     * @param countryCode  国家（cn）
     * @param name         联系人名称
     * @param org          组织机构
     * @return 结果 @重要：回写epp联系人ID 新规则:"wz_"+registrantId
     */
    public DomainResult create(String registrantId, String password, String email, String fax, String faxExt,
                               String tel, String telExt, String address, String city, String rovince, String postalCode,
                               String countryCode, String name, String org) throws Exception {
        // 设置联系人基本信息
        ContactBasicInfo conInfo = new ContactBasicInfo(getEppRegistrantId(registrantId), password, email,
                addCountryNum(fax), faxExt, addCountryNum(tel), telExt);
        // 设置联系人地址信息
        Vector<EPPContactPostalDefinition> postal = new Vector<EPPContactPostalDefinition>();
        Vector<String> streets1 = new Vector<String>();
        streets1.addElement(address);
        streets1.addElement("");
        EPPContactAddress addr1 = new EPPContactAddress(streets1, city, rovince, postalCode, countryCode);
        postal.addElement(new EPPContactPostalDefinition(name, org, EPPContactPostalDefinition.ATTR_TYPE_LOC, addr1));
        // 设置隐藏注册人电话
        EPPContactDisclose newDisclose = new EPPContactDisclose();
        newDisclose.setVoice("");
        newDisclose.setFlag(EPPContactDisclose.ATTR_FLAG_FALSE);
        EPPResult epprs;
        synchronized (Lock.getLOCK()) {
            epprs = getClient().createEPPContact(conInfo, newDisclose, postal).getResult();
        }
        if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
            logger.info("[联系人][{}]创建成功！", registrantId);
            return DomainResult.success(getEppRegistrantId(registrantId));
        } else {
            logger.info("[联系人][{}]创建失败：{}", registrantId, epprs);
            return DomainResult.error(epprs);
        }

    }

    /**
     * 修改联系人
     *
     * @param registrantId 网址联系人ID
     * @param password     密码
     * @param email        邮箱
     * @param fax          传真
     * @param faxExt       传真分机号
     * @param tel          电话
     * @param telExt       电话分机号
     * @param address      地址
     * @param city         城市
     * @param rovince      省份
     * @param postalCode   邮编
     * @param countryCode  国家（cn）
     * @param name         联系人名称
     * @param org          组织机构
     * @return DomainResult
     */
    public DomainResult update(String eppRegistrantId, String password, String email, String fax, String faxExt,
                               String tel, String telExt, String address, String city, String rovince, String postalCode,
                               String countryCode, String name, String org) throws Exception {
        // 设置联系人地址信息
        Vector<EPPContactPostalDefinition> postal = new Vector<EPPContactPostalDefinition>();
        Vector<String> streets1 = new Vector<String>();
        streets1.addElement(address);
        streets1.addElement("");
        EPPContactAddress addr1 = new EPPContactAddress(streets1, city, rovince, postalCode, countryCode);
        postal.addElement(new EPPContactPostalDefinition(name, org, EPPContactPostalDefinition.ATTR_TYPE_LOC, addr1));

        EPPContactAddChange change = new EPPContactAddChange(postal, addCountryNum(tel), addCountryNum(fax), email,
                null);
        change.setFaxExt(faxExt);
        change.setVoiceExt(telExt);
        // 设置隐藏注册人电话
        EPPContactDisclose newDisclose = new EPPContactDisclose();
        newDisclose.setVoice("");
        newDisclose.setFlag(EPPContactDisclose.ATTR_FLAG_FALSE);
        change.setDisclose(newDisclose);
        EPPResult epprs;
        synchronized (Lock.getLOCK()) {
            epprs = getClient().updateEPPContact(eppRegistrantId, null, null, change).getResult();
        }
        if (epprs.getCode() == 1000 || epprs.getCode() == 1001) {
            logger.info("[联系人][{}]修改成功！", eppRegistrantId);
            return DomainResult.success();
        } else {
            logger.info("[联系人][{}]修改失败：{}", eppRegistrantId, epprs);
            return DomainResult.error(epprs);
        }
    }

    /**
     * 查询联系人信息
     *
     * @param eppRegistrantId epp联系人Id
     * @param password        密码
     * @return 联系人信息data 强转：EPPContactInfoResp
     */
    public DomainResult info(String eppRegistrantId, String password) throws Exception {
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = getClient().infoEPPContact(eppRegistrantId, password);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            logger.info("[联系人][{}]查询成功！", eppRegistrantId);
            return DomainResult.success((EPPContactInfoResp) epprs);
        } else {
            logger.info("[联系人][{}]查询失败：{}", eppRegistrantId, epprs.getResult());
            return DomainResult.error(epprs.getResult());
        }

    }

    /**
     * 删除联系人信息
     *
     * @param eppRegistrantId epp联系人Id
     * @param password        密码
     * @return 联系人信息
     */
    public DomainResult delete(String eppRegistrantId) throws Exception {
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = getClient().deleteEPPContact(eppRegistrantId);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            logger.info("[联系人][{}]删除成功！", eppRegistrantId);
            return DomainResult.success((EPPContactInfoResp) epprs);
        } else {
            logger.info("[联系人][{}]删除失败：{}", eppRegistrantId, epprs.getResult());
            return DomainResult.error(epprs.getResult());
        }

    }

    /**
     * 查看联系人ID是否可用
     *
     * @param eppRegistrantId epp联系人ID
     * @return true：可用 false：不可用
     */
    public boolean checkToBoolean(String registrantId) throws Exception {
        return check(getEppRegistrantId(registrantId)).code() == 1000;
    }

    /**
     * 查看联系人ID是否可用
     *
     * @param eppRegistrantId epp联系人ID
     * @return
     */
    public DomainResult check(String eppRegistrantId) throws Exception {
        Vector<String> contactVec = new Vector<String>();
        contactVec.addElement(eppRegistrantId);
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = getClient().checkEPPContact(contactVec);
        }
        if (epprs.getResult().getCode() == 1000 || epprs.getResult().getCode() == 1001) {
            boolean b = ((EPPContactCheckResp) epprs).getCheckResults().get(0).isAvailable();
            logger.info("[联系人][{}]检测成功:{}", eppRegistrantId, b);
            if (b) {
                return DomainResult.success();
            } else {
                return DomainResult.error(1001, "不可用");
            }
        } else {
            logger.info("[联系人][{}]查询失败：{}", eppRegistrantId, epprs.getResult());
            return DomainResult.error(epprs.getResult());
        }

    }

    public EPPClient getClient() throws Exception {
        return eppConnect.getClient();
    }

    public String getEppRegistrantId(String registrantId) throws Exception {
        return "w_" + registrantId;
    }

    public String addCountryNum(String num) throws Exception {
        if (num == null)
            return null;
        return num.startsWith("+") ? num : "+86." + num;
    }

    public DomainResult synch(String domainRegistrantId, String domain) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        String[] conectors = new String[]{domainRegistrantId, domainRegistrantId, domainRegistrantId};
        // 增加项
        EPPDomainAddRemove addItem = new EPPDomainAddRemove(null, getContactVector(conectors), null);
        addItem.setMode(EPPDomainAddRemove.MODE_ADD);
        // 删除项
        List<EPPDomainContact> contacts = getRemoveRegistrant(domain1);
        if (contacts == null) {
            logger.info("[{}]EPP不存在该域名！", domain1);
            return DomainResult.error(1002, "EPP不存在该域名" + domain);
        }
        EPPDomainAddRemove removeItem = new EPPDomainAddRemove(null, getRemoveRegistrant(domain1), null);
        removeItem.setMode(EPPDomainAddRemove.MODE_REMOVE);
        Map<String, EPPDomainAddRemove> itemMap = new Hashtable<String, EPPDomainAddRemove>();
        itemMap.put("add", addItem);
        itemMap.put("remove", removeItem);
        if (compareItem(addItem, removeItem)) {
            logger.info("[网址][{}]不需要进行联系人同步！", domain1);
            return DomainResult.error(1002, "不需要进行同步！！！");
        }
        // 发送命令并获取响应
        EPPResponse thisresp = new EPPResponse();
        synchronized (Lock.getLOCK()) {
            thisresp = getClient().updateEPPDomain(domain1, itemMap, null, null, false, false);
        }
        EPPResult eppResult = thisresp.getResult();
        if (eppResult.getCode() != 1000 && eppResult.getCode() != 1001) {
            return DomainResult.error(eppResult);
        } else {
            return DomainResult.success();
        }

    }

    private Vector<EPPDomainContact> getContactVector(final String[] contacts) {
        Vector<EPPDomainContact> vec = null;
        if (contacts != null && contacts.length == 3) {
            vec = new Vector<EPPDomainContact>();
            // 注意联系人数组以管理、技术、财务的顺序排列
            vec.addElement(new EPPDomainContact(contacts[0], EPPDomainContact.TYPE_ADMINISTRATIVE));
            vec.addElement(new EPPDomainContact(contacts[1], EPPDomainContact.TYPE_TECHNICAL));
            vec.addElement(new EPPDomainContact(contacts[2], EPPDomainContact.TYPE_BILLING));
        }
        return vec;
    }

    private Vector<EPPDomainContact> getRemoveRegistrant(String domain) throws Exception {
        String hosts = EPPDomainInfoCmd.HOSTS_ALL;
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = getClient().infoEPPDomain(domain, hosts, null);
        }
        if (epprs.isSuccess()) {
            EPPDomainInfoResp domainInfoResp = (EPPDomainInfoResp) epprs;
            List<EPPDomainContact> list = domainInfoResp.getContacts();
            if (list != null && list.size() > 0) {
                String admin = null;
                String tech = null;
                String billing = null;
                for (EPPDomainContact eppDomainContact : list) {
                    if (EPPDomainContact.TYPE_ADMINISTRATIVE.equals(eppDomainContact.getType())) {
                        admin = eppDomainContact.getName();// 管理人
                    } else if (EPPDomainContact.TYPE_TECHNICAL.equals(eppDomainContact.getType())) {
                        tech = eppDomainContact.getName();// 技术人
                    } else if (EPPDomainContact.TYPE_BILLING.equals(eppDomainContact.getType())) {
                        billing = eppDomainContact.getName();// 财务人
                    }
                }
                String[] contacts = {admin, tech, billing};
                return getContactVector(contacts);
            }
        }
        return null;

    }

    // 比对目前的三个联系人信息是否一样
    public boolean compareItem(EPPDomainAddRemove addItem, EPPDomainAddRemove removeItem) {
        List<EPPDomainContact> newlist = addItem.getContacts();
        List<EPPDomainContact> oldlist = removeItem.getContacts();
        for (int i = 0; i < newlist.size(); i++) {
            EPPDomainContact oldeContact = oldlist.get(i);
            EPPDomainContact neweContact = newlist.get(i);
            if (oldeContact.getName().equalsIgnoreCase(neweContact.getName())) {
                continue;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * 查找网址的联系人
     *
     * @param domain
     * @return
     * @throws Exception
     */
    public DomainResult getAllContacts(String domain) throws Exception {
        String domain1 = IDN.toASCII(domain + "." + domainSuffix);
        String hosts = EPPDomainInfoCmd.HOSTS_ALL;
        EPPResponse epprs;
        synchronized (Lock.getLOCK()) {
            epprs = getClient().infoEPPDomain(domain1, hosts, null);
        }
        if (epprs.isSuccess()) {
            EPPDomainInfoResp domainInfoResp = (EPPDomainInfoResp) epprs;
            List<EPPDomainContact> list = domainInfoResp.getContacts();
            if (list != null && list.size() > 0) {
                String admin = null;
                String tech = null;
                String billing = null;
                for (EPPDomainContact eppDomainContact : list) {
                    if (EPPDomainContact.TYPE_ADMINISTRATIVE.equals(eppDomainContact.getType())) {
                        admin = eppDomainContact.getName();// 管理人
                    } else if (EPPDomainContact.TYPE_TECHNICAL.equals(eppDomainContact.getType())) {
                        tech = eppDomainContact.getName();// 技术人
                    } else if (EPPDomainContact.TYPE_BILLING.equals(eppDomainContact.getType())) {
                        billing = eppDomainContact.getName();// 财务人
                    }
                }
                String[] contacts = {admin, tech, billing};
                return DomainResult.success(contacts);
            }
        }
        return DomainResult.error(epprs.getResult());

    }
}
