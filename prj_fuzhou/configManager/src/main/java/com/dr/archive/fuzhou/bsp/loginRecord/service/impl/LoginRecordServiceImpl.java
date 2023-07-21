package com.dr.archive.fuzhou.bsp.loginRecord.service.impl;

import com.dr.archive.fuzhou.bsp.loginRecord.entity.LoginHandle;
import com.dr.archive.fuzhou.bsp.loginRecord.entity.LoginHandleInfo;
import com.dr.archive.fuzhou.bsp.loginRecord.entity.LoginRecord;
import com.dr.archive.fuzhou.bsp.loginRecord.entity.LoginRecordInfo;
import com.dr.archive.fuzhou.bsp.loginRecord.service.LoginHandleKey;
import com.dr.archive.fuzhou.bsp.loginRecord.service.LoginRecordService;
import com.dr.archive.util.UUIDUtils;
import com.dr.framework.common.service.CommonService;
import com.dr.framework.common.service.DefaultBaseService;
import com.dr.framework.core.organise.entity.Person;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lych
 * @date 2023/1/31 上午 11:24
 * @mood happy
 */
@Service
public class LoginRecordServiceImpl extends DefaultBaseService<LoginRecord> implements LoginRecordService, ApplicationRunner {

    @Autowired
    CommonService commonService;
    @Value("${loginLongTime}")
    Long loginTime;
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    @Override
    public Map<String, Long> maxLoginNumber() {

        //获取最大的人员信息，
        //在线状态为 status 1,离线 0.  根据创建时间排序，时间最老的放在上面。获取第一个时不会获取到新的数据，防止最大数出现错误。
        List<LoginRecord> loginRecords = commonMapper.selectByQuery(SqlQuery.from(LoginRecord.class).equal(LoginRecordInfo.STATUS, "1").lessThan().orderByDesc(LoginRecordInfo.CREATEDATE));

        List<LoginHandle> loginHandles = commonMapper.selectByQuery(SqlQuery.from(LoginHandle.class));
        Map<String,Long> map =loginHandles.stream().collect(Collectors.toMap(LoginHandle::getLoginKey,value-> {
            String cs = value.getLoginValue()==null?"":value.getLoginValue();
            long l = Long.parseLong(cs);
            return l;
        }));
        map.put("current_person", (long) loginRecords.size());
        return map;
    }

    @Override
    public void insertPerson(Person person) {
        LoginRecord loginRecord = commonMapper.selectOneByQuery(SqlQuery.from(LoginRecord.class).equal(LoginRecordInfo.PERSONID, person.getId()));
        if (loginRecord == null) {
            LoginRecord data = new LoginRecord();
            data.setMaxLoginNumber((long) 0);
            data.setPersonId(person.getId());
            data.setStatus("1");
            data.setName(person.getUserCode());
            data.setCreateDate(System.currentTimeMillis());
            commonService.insert(data);
        } else {
            commonMapper.updateByQuery(SqlQuery.from(LoginRecord.class).equal(LoginRecordInfo.PERSONID, person.getId())
                    .set(LoginRecordInfo.STATUS, "1")
                    .set(LoginRecordInfo.CREATEDATE, System.currentTimeMillis()));
        }
        //添加历史登录个数
        LoginHandle handle = getHandle(LoginHandleKey.HISTORY_PERSON);
        if (handle != null) {
            int i = Integer.parseInt(handle.getLoginValue());
            insertLoginHandle(LoginHandleKey.HISTORY_PERSON, i + 1 + "");
        } else {
            insertLoginHandle(LoginHandleKey.HISTORY_PERSON, "1");
        }

        //查询当天最大个数
        List<LoginRecord> loginRecords =
                commonMapper.selectByQuery(SqlQuery.from(LoginRecord.class)
                        .equal(LoginRecordInfo.STATUS, "1")
                        .lessThanEqual(LoginRecordInfo.CREATEDATE, getEndTime())
                        .greaterThanEqual(LoginRecordInfo.CREATEDATE, getStartTime())
                        .orderByDesc(LoginRecordInfo.CREATEDATE));

        //查询当天最大人数和存储的最大人数那个大。
        LoginHandle current_day_person = getHandle(LoginHandleKey.CURRENT_DAY_PERSON);
        if (current_day_person != null) {
            int i = Integer.parseInt(current_day_person.getLoginValue());
            if (i < loginRecords.size()) {
                insertLoginHandle(LoginHandleKey.CURRENT_DAY_PERSON, loginRecords.size() + "");
            }
        } else {
            insertLoginHandle(LoginHandleKey.CURRENT_DAY_PERSON, loginRecords.size() + "");
        }

        //查询历史最大个数和当天个数那个大。
        LoginHandle max_person = getHandle(LoginHandleKey.MAX_PERSON);
        LoginHandle day_person = getHandle(LoginHandleKey.CURRENT_DAY_PERSON);
        int j = Integer.parseInt(day_person.getLoginValue());
        if (max_person != null) {
            int i = Integer.parseInt(max_person.getLoginValue());
            if (i < j) {
                insertLoginHandle(LoginHandleKey.MAX_PERSON, j + "");
            }
        } else {
            insertLoginHandle(LoginHandleKey.MAX_PERSON, j + "");
        }
        //定义一个任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                offLine(person);
            }
        };
        // 计时器
        Timer timer = new Timer();
        // 开始执行任务
        timer.schedule(timerTask, loginTime);
    }

    /**
     * 添加系统运行数据
     *
     * @param key
     * @param value
     */
    public void insertLoginHandle(String key, String value) {
        if (StringUtils.hasText(key) && StringUtils.hasText(value)) {
            List<LoginHandle> loginHandles = commonMapper.selectByQuery(SqlQuery.from(LoginHandle.class).equal(LoginHandleInfo.LOGINKEY, key));
            if (loginHandles.size() > 0) {
                commonMapper.updateByQuery(SqlQuery.from(LoginHandle.class).equal(LoginHandleInfo.LOGINKEY, key).set(LoginHandleInfo.LOGINVALUE, value));
            } else {
                LoginHandle loginHandle = new LoginHandle();
                loginHandle.setLoginKey(key);
                loginHandle.setLoginValue(value);
                loginHandle.setId(UUIDUtils.getUUID());
                commonMapper.insert(loginHandle);
            }
        }
    }

    public LoginHandle getHandle(String key) {
        return commonMapper.selectOneByQuery(SqlQuery.from(LoginHandle.class).equal(LoginHandleInfo.LOGINKEY, key));
    }

    @Override
    public void offLine() {
        Person person = SecurityHolder.get().currentPerson();
        if (person != null) {
            commonMapper.updateByQuery(SqlQuery.from(LoginRecord.class).equal(LoginRecordInfo.PERSONID, person.getId()).set(LoginRecordInfo.STATUS, "0"));
        }
    }

    @Override
    public void offLine(Person person) {
        if (person != null) {
            commonMapper.updateByQuery(SqlQuery.from(LoginRecord.class).equal(LoginRecordInfo.PERSONID, person.getId())
                    .set(LoginRecordInfo.STATUS, "0").set(LoginRecordInfo.CREATEDATE, System.currentTimeMillis()));

        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//删除人员表里登录人
        delete(SqlQuery.from(LoginRecord.class));
        //删除运行天数，删除当天最大在线人数
        commonMapper.deleteByQuery(SqlQuery.from(LoginHandle.class).equal(LoginHandleInfo.LOGINKEY, LoginHandleKey.SYSTEM_DAY));
        commonMapper.deleteByQuery(SqlQuery.from(LoginHandle.class).equal(LoginHandleInfo.LOGINKEY, LoginHandleKey.CURRENT_DAY_PERSON));
        LoginHandle loginHandle = new LoginHandle();
        loginHandle.setId(UUIDUtils.getUUID());
        loginHandle.setLoginKey(LoginHandleKey.CURRENT_DAY_PERSON);
        loginHandle.setLoginValue("0");
        commonMapper.insert(loginHandle);
        //统计系统运行天数
        insertLoginHandle(LoginHandleKey.SYSTEM_DAY, "1");
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                LoginHandle handle = getHandle(LoginHandleKey.SYSTEM_DAY);
                if (handle != null) {
                    int i = Integer.parseInt(handle.getLoginValue());
                    insertLoginHandle(LoginHandleKey.SYSTEM_DAY, i + 1 + "");
                } else {
                    insertLoginHandle(LoginHandleKey.SYSTEM_DAY, "1");
                }
                //删除当天最大登录数，这里跟着系统运行天数走，不是从0-23小时，需要的话再写一个定时器。
                commonMapper.deleteByQuery(SqlQuery.from(LoginHandle.class).equal(LoginHandleInfo.LOGINKEY, LoginHandleKey.CURRENT_DAY_PERSON));
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 1); //凌晨1点
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        Date date = calendar.getTime(); //第一次执行定时任务的时间
        //如果第一次执行定时任务的时间 小于当前的时间
        //此时要在 第一次执行定时任务的时间加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
        if (date.before(new Date())) {
            date = addDay(date, 1);
        }
        //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
        Timer timer = new Timer();
        timer.schedule(timerTask, date, PERIOD_DAY);
        System.out.println("执行方法");
    }

    // 增加或减少天数
    public Date addDay(Date date, int num) {
        Calendar startDT = Calendar.getInstance();
        startDT.setTime(date);
        startDT.add(Calendar.DAY_OF_MONTH, num);
        return startDT.getTime();
    }

    /**
     * 当天开始时间
     *
     * @return
     */
    private long getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime().getTime();
    }

    /**
     * 当天结束时间
     *
     * @return
     */
    private long getEndTime() {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR, 23);
        todayEnd.set(Calendar.MINUTE, 59);
        todayEnd.set(Calendar.SECOND, 59);
        todayEnd.set(Calendar.MILLISECOND, 999);
        return todayEnd.getTime().getTime();
    }
}
