package cn.knet.domain.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;

import java.util.Arrays;
import java.util.Date;


/**
 * @author xuxiannian
 */
public class MapBuilder extends LinkedMultiValueMap<String, String> {


    public static MapBuilder build() {
        return build(null, null, null);

    }

    public static MapBuilder build(String k, Object v) {
        return build(k, v, null);
    }

    public static MapBuilder build(String k, Object v, String f) {
        MapBuilder b = new MapBuilder();
        return b.ad(k, v, f);
    }

    public MapBuilder ad(String k, Object v, String f) {
        if (v == null) return this;
        String v1;
        if (v instanceof Date) {
            if (StringUtils.isNotBlank(f)) {
                v1 = DateUtils.formatDate((Date) v, f);
            } else {
                v1 = DateUtils.formatDate((Date) v, "yyyy-MM-dd");
            }
        } else {
            v1 = v.toString();
        }
        this.add(k, v1);
        return this;
    }

    public MapBuilder ad(String k, Object v) {
        return ad(k, v, null);
    }

    public MapBuilder ad(String k, Object[] v, String f) {
        Arrays.asList(v).forEach(x -> ad(k, x, f));
        return this;
    }

    public MapBuilder ad(String k, Object[] v) {
        return ad(k, v, null);
    }

    public static void main(String[] args) {
        String[] str = {"daihuib", "asdfawet"};
        System.out.println(MapBuilder.build().ad("e", new Date()).ad("s", new Date(), "yyyy-MM-dd HH:mm:ss").ad("aaa", str).ad("aaa", "ccc"));
    }


}
