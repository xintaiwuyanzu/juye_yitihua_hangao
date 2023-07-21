package com.dr.archive.receive.online.service;

import com.dr.archive.receive.online.bo.ArchiveReceiveBo;
import com.dr.archive.receive.online.entity.ArchiveBatchReceiveOnline;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 真正实现在线接收功能的接口
 *
 * @author dr
 */
public interface OnLineReceiveProvider<T> {
    /**
     * 在线接收类型提供类
     */
    interface ReceiveTypeProvider {
        /**
         * 类型编码
         *
         * @return
         */
        String code();

        /**
         * 类型名称
         *
         * @return
         */
        String name();
    }

    /**
     * 能否支持指定类型的在线接收数据处理
     *
     * @param context
     * @return
     */
    boolean canHandle(OnlineReceiveBatchContext context);

    /**
     * 同步在线接收，返回在线接收消息
     * <p>
     * 因为每个系统批次信息可能不同，所以需要在同步方法中设置
     * {@link OnlineReceiveBatchContext#setBatchReceiveOnline(ArchiveBatchReceiveOnline)}}
     * 在线接收批次数据
     *
     * @param context
     * @return T 这个方法返回的结果会被{@link  com.dr.framework.common.entity.ResultEntity}封装后直接返回给业务系统了
     */
    T syncReceiveResult(OnlineReceiveBatchContext context);

    /**
     * 根据上下文判断上下文中是否还有下一条要接收的档案数据
     *
     * @param context
     * @return
     */
    boolean hasNext(OnlineReceiveBatchContext context);

    /**
     * 接收下一条档案数据，实现类只需要实现具体数据的创建即可，
     * <p>
     * 数据的转换和保存交给{@link  ArchiveOnlineReceiveService#receiveOnline(HttpServletRequest, HttpServletResponse, ArchiveReceiveBo)} 处理
     *
     * @param context
     * @return
     */
    OnlineReceiveBatchContext.ReceiveDataContext receiveNext(OnlineReceiveBatchContext context);
}
