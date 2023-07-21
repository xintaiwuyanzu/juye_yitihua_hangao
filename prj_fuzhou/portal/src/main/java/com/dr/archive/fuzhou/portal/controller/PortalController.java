package com.dr.archive.fuzhou.portal.controller;

import com.dr.archive.appraisal.entity.ArchiveAppraisalTask;
import com.dr.archive.appraisal.service.AppraisalStatisticsService;
import com.dr.archive.fuzhou.portal.service.PortalService;
import com.dr.archive.utilization.search.service.EsDataService;
import com.dr.archive.utilization.search.to.SearchQuerysTo;
import com.dr.framework.common.entity.ResultEntity;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.process.query.TaskInstanceQuery;
import com.dr.framework.core.process.service.TaskInstanceService;
import com.dr.framework.core.web.annotations.Current;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"${common.api-path:/api}/portal"})
public class PortalController {
    @Autowired
    PortalService portalService;
    @Autowired
    AppraisalStatisticsService appraisalStatisticsService;
    @Autowired
    EsDataService esHandleService;
    @Autowired
    private TaskInstanceService taskInstanceService;

    public TaskInstanceService getTaskInstanceService() {
        return this.taskInstanceService;
    }

    @RequestMapping({"/todo"})
    public ResultEntity todo(@Current Person person, TaskInstanceQuery query, @RequestParam(defaultValue = "0") int pageIndex, @RequestParam(defaultValue = "15") int pageSize, @RequestParam(defaultValue = "true") boolean page) {
        query.assigneeEqual(person.getId());
        return page ? ResultEntity.success(this.getTaskInstanceService().taskPage(query, pageIndex, pageSize)) : ResultEntity.success(this.getTaskInstanceService().taskList(query));
    }

    /*
     * 门户系统-代办事项*/
//    @RequestMapping("/todo")
//    public ResultEntity todo(@RequestParam(name = "userId") String userId) {
//        List<Map> list = new ArrayList<>();
//        String[] ids = new String[]{"11", "22", "33", "44", "55"};
//        String[] titles = new String[]{"这是个档案室待办事项！", "这是个档案馆待办事项1！", "这是个档案馆待办事项2！", "这是个在线监督指导待办事项1！", "这是个在线监督指导待办事项2！"};
//        String[] times = new String[]{"1632985562109", "1625622421501", "1625022854825", "1624843721160", "1624507300369"};
//        String[] types = new String[]{"1", "2", "2", "3", "3"};
//        for (int a = 0; a < ids.length; a++) {
//            Map map1 = new HashMap();
//            map1.put("id", ids[a]);
//            map1.put("title", titles[a]);//待办事项标题
//            map1.put("time", times[a]);//待办事项发布时间 时间戳
//            map1.put("type", types[a]);//待办事项类型（1 档案室，2 档案馆，3 在线监督指导）
//            list.add(map1);
//        }
//        return ResultEntity.success(list);
//    }

    /*
     * 门户系统-通知公告*/
    @RequestMapping("/notices")
    public ResultEntity notices(@RequestParam(name = "userId") String userId) {
        List<Map> list = new ArrayList<>();
        String[] ids = new String[]{"11", "22", "33", "44", "55"};
        String[] titles = new String[]{"这是个档案室通知公告！", "这是个档案馆通知公告1！", "这是个档案馆通知公告2！", "这是个在线监督指导通知公告1！", "这是个在线监督指导通知公告2！"};
        String[] times = new String[]{"1632985562109", "1625622421501", "1625022854825", "1624843721160", "1624507300369"};
        String[] types = new String[]{"1", "2", "2", "3", "3"};
        for (int a = 0; a < ids.length; a++) {
            Map map1 = new HashMap();
            map1.put("id", ids[a]);
            map1.put("title", titles[a]);//通知公告标题
            map1.put("time", times[a]);//通知公告发布时间 时间戳
            map1.put("type", types[a]);//通知公告类型（1 档案室，2 档案馆，3 在线监督指导）
            map1.put("url", ""); //点击该通知公告时 跳转的地址
            list.add(map1);
        }
        return ResultEntity.success(list);
    }

    /*
    门户系统-重点档案*/
    @RequestMapping("/keyFile")
    public ResultEntity keyFile(@RequestParam(name = "userId") String userId) {
        List<Map> list = new ArrayList<>();
        String[] aa = new String[]{"政务类", "经济类", "科技类"};
        for (int a = 0; a < aa.length; a++) {
            Map map1 = new HashMap();
            map1.put("name", aa[a]);
            map1.put("value", 1243 + a);
            list.add(map1);
        }
        return ResultEntity.success(list);
    }

    /*
     * 门户系统-档案开放数量*/
    @RequestMapping("/openCount")
    public ResultEntity openCount(@RequestParam(name = "userId") String userId) {

        List<ArchiveAppraisalTask> archiveAppraisalTaskList = appraisalStatisticsService.countByFondAndVintagesAndType(userId, null, null, "KFJD");
        List<Map> list = new ArrayList<>();

        for (ArchiveAppraisalTask archiveAppraisalTask : archiveAppraisalTaskList) {
            Map map = new HashMap();
            map.put("name", archiveAppraisalTask.getVintages());
            map.put("value", archiveAppraisalTask.getId());
            list.add(map);
        }
        return ResultEntity.success(list);
    }

    /*
     * 门户系统-档案共享数量*/
    @RequestMapping("/shareCount")
    public ResultEntity shareCount(@RequestParam(name = "userId") String userId) {
        //todo 目前统计的到期档案数量
        List<ArchiveAppraisalTask> archiveAppraisalTaskList = appraisalStatisticsService.countByFondAndVintagesAndType(userId, null, null, "DQJD");
        archiveAppraisalTaskList.forEach(i -> i.setAppraisalType("KFJD".equalsIgnoreCase(i.getAppraisalType()) ? "开放鉴定数量" : "到期鉴定数量"));

        List<Map> list = new ArrayList<>();
        for (ArchiveAppraisalTask archiveAppraisalTask : archiveAppraisalTaskList) {
            Map map = new HashMap();
            map.put("name", archiveAppraisalTask.getVintages());
            map.put("value", archiveAppraisalTask.getId());
            list.add(map);
        }
        return ResultEntity.success(list);
    }

    /*
     * 门户系统-档案分类 //TODO 没用，用的ResourceStatisticsController,page方法
     *
     */
    /*@RequestMapping("/resourceByCateGory")
    public ResultEntity resourceByCateGory(@RequestParam(name = "userId") String userId) {
        return ResultEntity.success(portalService.resourceByCateGory(userId));
    }*/

    /**
     * 门户系统-搜索功能
     *
     * @param searchQuerysTo
     * @param index
     * @param size
     * @return
     */
    @RequestMapping("/searchBykeyWords")
    public ResultEntity searchBykeyWord(SearchQuerysTo searchQuerysTo,
                                        @RequestParam(value = "index", defaultValue = "0") Integer index,
                                        @RequestParam(value = "size", defaultValue = "25") Integer size) {
        return ResultEntity.success(esHandleService.searchBykeyWord2(searchQuerysTo, index, size));
    }

    /**
     * 门户系统-分类统计
     *
     * @param searchQuerysTo
     * @param index
     * @param size
     * @return
     */
    @RequestMapping("/resourceByCateGory")
    public ResultEntity resourceByCateGory(SearchQuerysTo searchQuerysTo,
                                           @RequestParam(value = "index", defaultValue = "0") Integer index,
                                           @RequestParam(value = "size", defaultValue = "25") Integer size) {
        return ResultEntity.success(esHandleService.searchBykeyWord2(searchQuerysTo, index, size));
    }

    /**
     * 门户系统-获取门户地址信息
     */
    @RequestMapping("/getPortalSystem")
    public ResultEntity getPortalSystem() {
        return ResultEntity.success(portalService.getPortalSystem());
    }
}
