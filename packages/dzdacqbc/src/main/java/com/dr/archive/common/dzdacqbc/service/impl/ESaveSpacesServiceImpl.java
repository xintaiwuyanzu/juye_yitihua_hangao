package com.dr.archive.common.dzdacqbc.service.impl;

import com.dr.archive.common.dzdacqbc.entity.ESaveSpaces;
import com.dr.archive.common.dzdacqbc.entity.ESaveSpacesInfo;
import com.dr.archive.common.dzdacqbc.service.ESaveSpacesService;
import com.dr.archive.common.dzdacqbc.vo.MonitorDataVo;
import com.dr.framework.common.page.Page;
import com.dr.framework.common.service.CacheAbleService;
import com.dr.framework.core.orm.sql.support.SqlQuery;
import com.dr.framework.core.security.SecurityHolder;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.util.Util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;

@Service
@Transactional(rollbackFor = Exception.class)
public class ESaveSpacesServiceImpl extends CacheAbleService<ESaveSpaces> implements ESaveSpacesService {

    /**
     * 设置默认
     *
     * @param spaces
     */
    @Override
    public void updateDefault(ESaveSpaces spaces) {
        SqlQuery<ESaveSpaces> sqlQuery = SqlQuery.from(ESaveSpaces.class).equal(ESaveSpacesInfo.ISDEFAULT, "true");
        ESaveSpaces o = commonMapper.selectOneByQuery(sqlQuery);
        if (ObjectUtils.isNotEmpty(o)) {
            o.setIsDefault("false");
            //查询出现在为默认的，修改为非默认
            long result1 = commonMapper.updateById(o);
            if (result1 > 0L) {
                this.cache.evictIfPresent(o.getId());
            }
        }
        //将待修改的改为默认
        long result2 = commonMapper.updateById(spaces);
        if (result2 > 0L) {
            this.cache.evictIfPresent(spaces.getId());
        }
    }

    @Override
    public long insert(ESaveSpaces entity) {
        entity.setIsDefault(Optional.ofNullable(entity.getIsDefault()).orElse("false"));
        entity.setStatus(Optional.ofNullable(entity.getStatus()).orElse("健康"));
        entity.setOrganiseId(SecurityHolder.get().currentOrganise().getId());
        entity.setOrganiseId(SecurityHolder.get().currentOrganise().getOrganiseName());
        entity.setCapacity(getCapacity(entity.getCatalogue()));
        return super.insert(entity);
    }

    @Override
    public String getCapacity(String catalogue) {
        File file = new File(catalogue);
        if (!file.isDirectory()) {
            return "0";
        }
        return (file.getTotalSpace() / 1024 / 1024 / 1024) + "";
    }


    private static final String DECIMAL_FORMAT_PATTERN = "######0.0";


    @Override
    public List<MonitorDataVo> getMonitorData() {
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
        SystemInfo systemInfo = new SystemInfo();
        List<MonitorDataVo> list = new ArrayList<>();
        list.add(readCpu(systemInfo, df));
        list.add(readMem(systemInfo, df));
        //TODO应该还需要读取硬盘的信息
        return list;
    }

    @Override
    public File buildRootDir(String id) {
        ESaveSpaces saveSpaces = selectById(id);
        return new File(saveSpaces.getCatalogue());
    }


    /**
     * 读取cpu使用情况
     *
     * @param systemInfo
     * @param df
     * @return
     */
    private synchronized MonitorDataVo readCpu(SystemInfo systemInfo, DecimalFormat df) {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTicks = processor.getSystemCpuLoadTicks();
        //睡一秒，详细计算时间
        Util.sleep(1000);
        long[] ticks = processor.getSystemCpuLoadTicks();
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = LongStream.of(ticks).sum() - LongStream.of(prevTicks).sum();
        double result = (1.0 - (idle * 1.0 / totalCpu)) * 100;
        MonitorDataVo monitorDataVo = new MonitorDataVo();
        monitorDataVo.setLabel("CPU使用率");
        monitorDataVo.setValue(Double.parseDouble(df.format(result)));
        monitorDataVo.setMax(100);
        return monitorDataVo;
    }

    /**
     * 读取内存使用情况
     *
     * @param systemInfo
     * @param df
     * @return
     */
    public synchronized MonitorDataVo readMem(SystemInfo systemInfo, DecimalFormat df) {
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        long totalByte = memory.getTotal();
        long avaliable = memory.getAvailable();
        int MB = 1024 * 1024;
        MonitorDataVo monitorDataVo = new MonitorDataVo();
        monitorDataVo.setLabel("内存使用情况");
        monitorDataVo.setValue(Double.parseDouble(df.format((totalByte - avaliable) / MB)));
        monitorDataVo.setMax(Double.parseDouble(df.format(totalByte / MB)));
        return monitorDataVo;
    }

    @Override
    public List<ESaveSpaces> selectList(SqlQuery<ESaveSpaces> sqlQuery) {
        List<ESaveSpaces> saveSpaces = super.selectList(sqlQuery);
        for (ESaveSpaces saveSpace : saveSpaces) {
            fixSpace(saveSpace);
        }
        return saveSpaces;
    }

    @Override
    public Page<ESaveSpaces> selectPage(SqlQuery<ESaveSpaces> sqlQuery, int pageIndex, int pageSize) {
        Page<ESaveSpaces> result = super.selectPage(sqlQuery, pageIndex, pageSize);
        for (ESaveSpaces datum : result.getData()) {
            fixSpace(datum);
        }
        return result;
    }

    @Override
    protected String getCacheName() {
        return "ESaveSpaces";
    }

    private void fixSpace(ESaveSpaces saveSpace) {
        DecimalFormat df = new DecimalFormat(DECIMAL_FORMAT_PATTERN);
        File file = new File(saveSpace.getCatalogue());
        if (!file.isDirectory()) {
            return;
        }
        double totalSpace = (double) file.getTotalSpace() / 1024 / 1024 / 1024;
        double usableSpace = (double) file.getUsableSpace() / 1024 / 1024 / 1024;
        double percent = Double.parseDouble(df.format((totalSpace - usableSpace) / totalSpace * 100));
        saveSpace.setPercent(percent);
        saveSpace.setUsedSpace(String.valueOf((totalSpace - usableSpace)));
    }
}
