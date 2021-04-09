package cn.knet.wz.web;

import cn.knet.domain.vo.APIResult;
import cn.knet.wz.service.AccessService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("access")
public class AccessController {
    @Resource
    AccessService accessService;


    @RequestMapping(value = "/countByDate", method = {
            RequestMethod.GET, RequestMethod.POST})
    public APIResult getAccessTotal(Date s, Date e) {
        Validate.isTrue(s != null, "开始日期不能为空");
        Validate.isTrue(e != null, "结束日期不能为空");
        return APIResult.success(accessService.getAccessTotal(s, e));
    }

    @RequestMapping(value = "/getAccessGroup", method = {
            RequestMethod.GET, RequestMethod.POST})
    public APIResult getAccessGroup(Date s, Date e, String type) {
        Validate.isTrue(s != null, "开始日期不能为空");
        Validate.isTrue(e != null, "结束日期不能为空");
        Validate.isTrue(type != null, "分组类型不能为空");
        return APIResult.success(accessService.getAccessGroup(s, e, type));
    }

    @RequestMapping(value = "/getNotLandAccess", method = {
            RequestMethod.GET, RequestMethod.POST})
    public APIResult getNotLandAccess(Date s, Date e, String type) {
        Validate.isTrue(s != null, "开始日期不能为空");
        Validate.isTrue(e != null, "结束日期不能为空");
        return APIResult.success(accessService.getNotLandAccess(s, e));
    }


}
