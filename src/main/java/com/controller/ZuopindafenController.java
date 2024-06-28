package com.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

import com.utils.ValidatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.annotation.IgnoreAuth;

import com.entity.ZuopindafenEntity;
import com.entity.view.ZuopindafenView;

import com.service.ZuopindafenService;
import com.service.TokenService;
import com.utils.PageUtils;
import com.utils.R;
import com.utils.MD5Util;
import com.utils.MPUtil;
import com.utils.CommonUtil;


/**
 * 作品打分
 * 后端接口
 * @author 
 * @email 
 * @date 2021-05-03 11:24:10
 */
@RestController
@RequestMapping("/zuopindafen")
public class ZuopindafenController {
    @Autowired
    private ZuopindafenService zuopindafenService;
    


    /**
     * 后端列表
     */
    @RequestMapping("/page")
    public R page(@RequestParam Map<String, Object> params,ZuopindafenEntity zuopindafen,
		HttpServletRequest request){
		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("xuesheng")) {
			zuopindafen.setXuehao((String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("jiaoshi")) {
			zuopindafen.setGonghao((String)request.getSession().getAttribute("username"));
		}
        EntityWrapper<ZuopindafenEntity> ew = new EntityWrapper<ZuopindafenEntity>();
		PageUtils page = zuopindafenService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zuopindafen), params), params));

        return R.ok().put("data", page);
    }
    
    /**
     * 前端列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params,ZuopindafenEntity zuopindafen, 
		HttpServletRequest request){
        EntityWrapper<ZuopindafenEntity> ew = new EntityWrapper<ZuopindafenEntity>();
		PageUtils page = zuopindafenService.queryPage(params, MPUtil.sort(MPUtil.between(MPUtil.likeOrEq(ew, zuopindafen), params), params));
        return R.ok().put("data", page);
    }

	/**
     * 列表
     */
    @RequestMapping("/lists")
    public R list( ZuopindafenEntity zuopindafen){
       	EntityWrapper<ZuopindafenEntity> ew = new EntityWrapper<ZuopindafenEntity>();
      	ew.allEq(MPUtil.allEQMapPre( zuopindafen, "zuopindafen")); 
        return R.ok().put("data", zuopindafenService.selectListView(ew));
    }

	 /**
     * 查询
     */
    @RequestMapping("/query")
    public R query(ZuopindafenEntity zuopindafen){
        EntityWrapper< ZuopindafenEntity> ew = new EntityWrapper< ZuopindafenEntity>();
 		ew.allEq(MPUtil.allEQMapPre( zuopindafen, "zuopindafen")); 
		ZuopindafenView zuopindafenView =  zuopindafenService.selectView(ew);
		return R.ok("查询作品打分成功").put("data", zuopindafenView);
    }
	
    /**
     * 后端详情
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
        ZuopindafenEntity zuopindafen = zuopindafenService.selectById(id);
        return R.ok().put("data", zuopindafen);
    }

    /**
     * 前端详情
     */
    @RequestMapping("/detail/{id}")
    public R detail(@PathVariable("id") Long id){
        ZuopindafenEntity zuopindafen = zuopindafenService.selectById(id);
        return R.ok().put("data", zuopindafen);
    }
    



    /**
     * 后端保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody ZuopindafenEntity zuopindafen, HttpServletRequest request){
    	zuopindafen.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(zuopindafen);
        zuopindafenService.insert(zuopindafen);
        return R.ok();
    }
    
    /**
     * 前端保存
     */
    @RequestMapping("/add")
    public R add(@RequestBody ZuopindafenEntity zuopindafen, HttpServletRequest request){
    	zuopindafen.setId(new Date().getTime()+new Double(Math.floor(Math.random()*1000)).longValue());
    	//ValidatorUtils.validateEntity(zuopindafen);
        zuopindafenService.insert(zuopindafen);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody ZuopindafenEntity zuopindafen, HttpServletRequest request){
        //ValidatorUtils.validateEntity(zuopindafen);
        zuopindafenService.updateById(zuopindafen);//全部更新
        return R.ok();
    }
    

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
        zuopindafenService.deleteBatchIds(Arrays.asList(ids));
        return R.ok();
    }
    
    /**
     * 提醒接口
     */
	@RequestMapping("/remind/{columnName}/{type}")
	public R remindCount(@PathVariable("columnName") String columnName, HttpServletRequest request, 
						 @PathVariable("type") String type,@RequestParam Map<String, Object> map) {
		map.put("column", columnName);
		map.put("type", type);
		
		if(type.equals("2")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar c = Calendar.getInstance();
			Date remindStartDate = null;
			Date remindEndDate = null;
			if(map.get("remindstart")!=null) {
				Integer remindStart = Integer.parseInt(map.get("remindstart").toString());
				c.setTime(new Date()); 
				c.add(Calendar.DAY_OF_MONTH,remindStart);
				remindStartDate = c.getTime();
				map.put("remindstart", sdf.format(remindStartDate));
			}
			if(map.get("remindend")!=null) {
				Integer remindEnd = Integer.parseInt(map.get("remindend").toString());
				c.setTime(new Date());
				c.add(Calendar.DAY_OF_MONTH,remindEnd);
				remindEndDate = c.getTime();
				map.put("remindend", sdf.format(remindEndDate));
			}
		}
		
		Wrapper<ZuopindafenEntity> wrapper = new EntityWrapper<ZuopindafenEntity>();
		if(map.get("remindstart")!=null) {
			wrapper.ge(columnName, map.get("remindstart"));
		}
		if(map.get("remindend")!=null) {
			wrapper.le(columnName, map.get("remindend"));
		}

		String tableName = request.getSession().getAttribute("tableName").toString();
		if(tableName.equals("xuesheng")) {
			wrapper.eq("xuehao", (String)request.getSession().getAttribute("username"));
		}
		if(tableName.equals("jiaoshi")) {
			wrapper.eq("gonghao", (String)request.getSession().getAttribute("username"));
		}

		int count = zuopindafenService.selectCount(wrapper);
		return R.ok().put("count", count);
	}
	


}
