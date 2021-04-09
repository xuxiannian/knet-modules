package cn.knet.wz.service;


import cn.knet.domain.util.DateUtil;
import cn.knet.pandora.modules.utils.language.punycode.PunycodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.ValueCountAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j

public class AccessService {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchTemplate;


    public long getAccessTotal(Date s, Date e) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery("@timestamp").from(s.getTime()).to(e.getTime()));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .build();
        long count = elasticsearchTemplate.count(searchQuery, IndexCoordinates.of("logstash-wz-dns-log-*"));
        log.info("[{}到{}]解析量为：{}", DateUtil.convert2String(s, "yyyy-MM-dd HH:mm:ss"),
                DateUtil.convert2String(e, "yyyy-MM-dd HH:mm:ss"), count);
        return count;
    }

    public List<Map<String, Object>> getNotLandAccess(Date s, Date e) {

        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();

        //查询国外
        BoolQueryBuilder boolQuery1 = QueryBuilders.boolQuery();
        boolQuery1.must(QueryBuilders.rangeQuery("@timestamp").from(s.getTime()).to(e.getTime()));
        boolQuery1.mustNot(QueryBuilders.matchQuery("geoip.country_name.keyword", "China"));
        boolQuery1.mustNot(QueryBuilders.matchQuery("geoip.country_name.keyword", "Hong Kong"));
        boolQuery1.mustNot(QueryBuilders.matchQuery("geoip.country_name.keyword", "Taiwan"));
        boolQuery1.mustNot(QueryBuilders.matchQuery("geoip.country_name.keyword", "Macao"));
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery1)
                .build();
        long count = elasticsearchTemplate.count(searchQuery, IndexCoordinates.of("logstash-wz-dns-log-*"));
        log.info("[{}到{}][其他]解析量为：{}", DateUtil.convert2String(s, "yyyy-MM-dd HH:mm:ss"),
                DateUtil.convert2String(e, "yyyy-MM-dd HH:mm:ss"), count);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("key", "其他");
        m.put("count", count);
        l.add(m);

        BoolQueryBuilder boolQuery2 = QueryBuilders.boolQuery();
        boolQuery2.must(QueryBuilders.rangeQuery("@timestamp").from(s.getTime()).to(e.getTime()));
        boolQuery2.must(QueryBuilders.matchQuery("geoip.coyuntry_name.keyword", "Hong Kong"));
        searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery2)
                .build();
        count = elasticsearchTemplate.count(searchQuery, IndexCoordinates.of("logstash-wz-dns-log-*"));
        log.info("[{}到{}][香港]解析量为：{}", DateUtil.convert2String(s, "yyyy-MM-dd HH:mm:ss"),
                DateUtil.convert2String(e, "yyyy-MM-dd HH:mm:ss"), count);
        m = new HashMap<String, Object>();
        m.put("key", "香港");
        m.put("count", count);
        l.add(m);

        BoolQueryBuilder boolQuery3 = QueryBuilders.boolQuery();
        boolQuery3.must(QueryBuilders.rangeQuery("@timestamp").from(s.getTime()).to(e.getTime()));
        boolQuery3.must(QueryBuilders.matchQuery("geoip.country_name.keyword", "Macao"));
        searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery3)
                .build();
        count = elasticsearchTemplate.count(searchQuery, IndexCoordinates.of("logstash-wz-dns-log-*"));
        log.info("[{}到{}][澳门]解析量为：{}", DateUtil.convert2String(s, "yyyy-MM-dd HH:mm:ss"),
                DateUtil.convert2String(e, "yyyy-MM-dd HH:mm:ss"), count);
        m = new HashMap<String, Object>();
        m.put("key", "澳门");
        m.put("count", count);
        l.add(m);


        BoolQueryBuilder boolQuery4 = QueryBuilders.boolQuery();
        boolQuery4.must(QueryBuilders.rangeQuery("@timestamp").from(s.getTime()).to(e.getTime()));
        boolQuery4.must(QueryBuilders.matchQuery("geoip.country_name.keyword", "Taiwan"));
        searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery4)
                .build();
        count = elasticsearchTemplate.count(searchQuery, IndexCoordinates.of("logstash-wz-dns-log-*"));
        log.info("[{}到{}][香港]解析量为：{}", DateUtil.convert2String(s, "yyyy-MM-dd HH:mm:ss"),
                DateUtil.convert2String(e, "yyyy-MM-dd HH:mm:ss"), count);
        m = new HashMap<String, Object>();
        m.put("key", "台湾");
        m.put("count", count);
        l.add(m);

        return l;
    }

    public List<Map<String, Object>> getAccessGroup(Date s, Date e, String type) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.must(QueryBuilders.rangeQuery("@timestamp").from(s.getTime()).to(e.getTime()));
        if (type == null) {
            throw new RuntimeException("请输入分组类型");
        }
        if ("geoip.region_name.keyword".equals(type)) {
            boolQuery.must(QueryBuilders.matchQuery("geoip.country_name.keyword", "China"));
        }
        ValueCountAggregationBuilder countByFloor = AggregationBuilders.count("count").field(type);

        TermsAggregationBuilder gourpByFloorId = AggregationBuilders.terms("sum_sales").field(type)
                .subAggregation(countByFloor).size(5000);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQuery)
                .addAggregation(gourpByFloorId)
                .build();
        searchQuery.setMaxResults(10);
        log.info("开始查询");

        SearchHits<Map> sh = elasticsearchTemplate.search(searchQuery, Map.class, IndexCoordinates.of("logstash-wz-dns-log-*"));
        Terms t = (Terms) Objects.requireNonNull(sh.getAggregations()).asMap().get("sum_sales");
        List<Map<String, Object>> l = new ArrayList<Map<String, Object>>();
        for (Terms.Bucket b : t.getBuckets()) {
            Map<String, Object> m = new HashMap<String, Object>();
            if ("L2D.keyword".equals(type)) {
                m.put("key", PunycodeUtil.punycode2chinese((String) b.getKey()));
            } else {
                m.put("key", (String) b.getKey());
            }
            m.put("count", b.getDocCount());
            l.add(m);
        }
        log.info("查询结束,数量：" + l.size());
        return l;
    }


}