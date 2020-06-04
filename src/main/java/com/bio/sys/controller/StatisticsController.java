package com.bio.sys.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bio.common.utils.Result;
import com.bio.sys.dao.StatisticsDao;
import com.bio.sys.dao.UserDao;
import com.bio.sys.domain.StatisticalDO;
import com.bio.sys.domain.UserDO;
import com.bio.sys.service.StatisticsService;
import com.bio.sys.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/bio/statistics")
public class StatisticsController {

    @Autowired
   private StatisticsService statisticsService;

    @Autowired
    private UserService userService;

    @GetMapping()
    @RequiresPermissions("bio:statistics:add")
    public String Statistics() {
        return "bio/statistics/add";
    }


    @ResponseBody
    @GetMapping("/list")
    @RequiresPermissions("bio:statistics:add")
    public Result<List<StatisticalDO>> list() throws ParseException {
        long roleId=3;
        List<UserDO> myUser= userService.getUsersByRoleId(roleId);
        List<StatisticalDO> mylist=new ArrayList<>();
        for(int i=0;i<myUser.size();i++){
            mylist.add(new StatisticalDO(myUser.get(i).getName(), 0.0));
        }
        return Result.ok(mylist);
    }

    @ResponseBody
    @GetMapping("/edit")
    @RequiresPermissions("bio:statistics:add")
    public Result<String> edit(String getTableData, String datetimepicker1, String datetimepicker2, String sum) throws ParseException {
        datetimepicker1=datetimepicker1+" 00:00:00";
        datetimepicker2=datetimepicker2+" 23:59:59";
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date startDate=sdf.parse(datetimepicker1);
        Date endDate=sdf.parse(datetimepicker2);
        JSONArray jsonArray= JSON.parseArray(getTableData);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(endDate);
        int month=calendar.get(calendar.MONTH)+1;
        int year=calendar.get(calendar.YEAR);
        List<StatisticalDO> statisticalDOS=statisticsService.queryByDate(month,year);
        if(statisticalDOS.size()!=0){
            statisticsService.deleteByDate(month,year);
        }
        for(int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            String useName=jsonObject.getString("userName");
            Double absenceRate=jsonObject.getDouble("absenceRate");
            Double Isum=Double.parseDouble(sum);
            Double aRate=absenceRate/(Isum*1.0);
            statisticsService.insertStatistics(useName,aRate,startDate,endDate);
        }
        return Result.ok();
    }

}
