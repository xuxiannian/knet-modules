package cn.xxn.web;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.xxn.domain.MyRecord;
import cn.xxn.domain.MyRecordExample;
import cn.xxn.repository.MyRecordMapper;
import cn.xxn.repository.MyRecordStatMapper;
import cn.xxn.utils.JsonUtils;

@Controller
public class IndexController {
	@Resource
	MyRecordMapper myRecordMapper;

	@Resource
	MyRecordStatMapper myRecordStatMapper;

	@RequestMapping(value = { "/" }, method = RequestMethod.GET)
	public String index(HttpServletRequest request, Model model) throws UnsupportedEncodingException {
		int in = myRecordStatMapper.totalByIn();
		int out = myRecordStatMapper.totalByOut();

		model.addAttribute("in", in);
		model.addAttribute("out", out);
		model.addAttribute("num", in - out);

		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -15);
		List<Map<?, ?>> l = myRecordStatMapper.statByDate(c.getTime());
		List<String> dl = new ArrayList<String>();
		for (Map<?, ?> m : l) {
			dl.add((String) m.get("date1"));
		}
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < 15; i++) {
			String d = dateFormat.format(c.getTime());
			if (!dl.contains(d)) {
				Map<String, String> m = new HashMap<String, String>();
				m.put("date1", d);
				m.put("innum", "0");
				m.put("outnum", "0");
				l.add(m);
			}
			c.add(Calendar.DAY_OF_YEAR, 1);
		}
		l.sort((x, y) -> {
			return ((String) x.get("date1")).compareTo((String) y.get("date1"));
		});

		model.addAttribute("dateNum", JsonUtils.toJson(l));
		MyRecordExample e = new MyRecordExample();
		e.createCriteria().andTypeEqualTo("0");
		e.setOrderByClause("date desc");
		model.addAttribute("inList", myRecordMapper.selectByExample(e));

		MyRecordExample e1 = new MyRecordExample();
		e1.createCriteria().andTypeEqualTo("1");
		e1.setOrderByClause("date desc");
		model.addAttribute("outList", myRecordMapper.selectByExample(e1));

		return "index";
	}

	@RequestMapping(value = { "/save" }, method = RequestMethod.POST)
	@ResponseBody
	public String save(MyRecord r) throws UnsupportedEncodingException {
		r.setDate(new Date());
		myRecordMapper.insert(r);
		return "0";
	}

}
