package cn.knet.domain.vo;

import cn.knet.gtld.epp.codec.gen.pojo.EPPResult;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class DomainResult extends HashMap<String, Object> {
    private static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("Object status prohibits operation", "域名状态不允许该操作");
        map.put("maximum 10 years renew period supported", "续费年限过大，最多保有10年");
        map.put("rgp request supported when domain is in RGP pendindDelete status", "该网址不在赎回期");
        map.put("Command was not sent to server correctly. Please confirm whether the params are available",
                "服务器连接失败，请重试");
        map.put("illegal domain name.", "无效的网址名称");
        map.put("illegal server name", "无效的主机记录");
        map.put("Command was not sent to server correctly. Please confirm whether the params are available.", "服务器连接异常，请重试");
        map.put("The input starts with the ACE Prefix", "无效的网址名称");


    }

    private static final long serialVersionUID = 1L;

    public DomainResult() {
        setCode(1000);
        setMsg("执行成功");
    }

    public DomainResult(int code, String msg) {
        setCode(code);
        setMsg(msg);
    }

    public DomainResult(int count, Object data) {
        setCode(1000);
        setMsg("执行成功");
        setCount(count);
        setData(data);
    }

    public static DomainResult success(int count, Object data) {
        return new DomainResult(count, data);
    }

    public static DomainResult success(Object data) {
        return success(-1, data);
    }

    public static DomainResult success() {
        return success(-1, null);
    }

    public static DomainResult error(int code, String msg) {
        return new DomainResult(code, msg);
    }

    public static DomainResult error(EPPResult r) {
        try {
            return new DomainResult(r.getCode(), r.getExtValues().get(0).getReason());
        } catch (Exception e) {
            return new DomainResult(r.getCode(), r.getMessage());
        }
    }

    public DomainResult add(String key, Object value) {
        this.put(key, value);
        return this;
    }

    public void setCode(int code) {
        super.put("code", code);
    }

    public void setMsg(String msg) {
        if (msg != null) {
            String m1 = map.get(msg);
            msg = StringUtils.isNotBlank(m1) ? m1 : msg;
        }
        super.put("msg", msg);
    }

    public void setCount(int count) {
        if (count >= 0)
            super.put("count", count);
    }

    public void setData(Object data) {
        if (data != null)
            super.put("data", data);
    }

    public int code() {
        return (int) super.get("code");
    }

    public int count() {
        return (int) super.get("count");
    }

    public String msg() {
        return (String) super.get("msg");
    }

    public Object data() {
        return (Object) super.get("data");
    }

}
