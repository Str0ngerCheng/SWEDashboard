package com.bio.common.utils;

import com.baomidou.mybatisplus.plugins.Page;
import com.bio.sys.domain.ReportDO;

import java.util.ArrayList;
import java.util.List;

public class PageUtil {
    static public Page getPage(Integer pageNumber, Integer pageSize, List list){
        Page<Object> page = new Page<>(pageNumber, pageSize);
        List<Object> listPage = new ArrayList<Object>();
        if(pageNumber>=1) {
            for(int i=(pageNumber-1)*pageSize ;i<pageNumber*pageSize&&i<list.size();i++) {
                listPage.add(list.get(i)) ;
            }
            page.setRecords(listPage) ;
        }
        else {
            page.setRecords(list);
        }
        page.setTotal(list.size());
        return page ;
    }

}
