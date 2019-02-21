package io.vickze.entity;

import java.util.LinkedHashMap;
import java.util.Map;

import io.vickze.xss.SQLFilter;

/**
 * 查询参数
 *
 * @author vick.zeng
 * @email zyk@yk95.top
 * @create 2017-09-08 22:07
 */
public class QueryDO extends LinkedHashMap<String, Object> {
    private static final long serialVersionUID = 1L;
    //当前页码
    private int page;
    //每页条数
    private int limit;

    public QueryDO(Map<String, Object> params) {
        this.putAll(params);
        Object pageObj = params.get("page");
        Object limitObj = params.get("limit");

        //分页参数
        this.page = pageObj == null ? 1 : Integer.parseInt(pageObj.toString());
        this.limit = limitObj == null ? 10 : Integer.parseInt(limitObj.toString());
        this.put("offset", (page - 1) * limit);
        this.put("page", page);
        this.put("limit", limit);

        Object sidxObj = params.get("sidx");
        Object orderObj = params.get("order");
        if (sidxObj != null && orderObj != null) {
            //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
            String sidx = sidxObj.toString();
            String order = orderObj.toString();
            this.put("sidx", SQLFilter.sqlInject(sidx));
            this.put("order", SQLFilter.sqlInject(order));
        }
    }


    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
