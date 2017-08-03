package net.skeyurt.lit.commons.event;

/**
 * User : liulu
 * Date : 2017/8/3 20:27
 * version $Id: EventPublisher.java, v 0.1 Exp $
 */
public interface EventPublisher {

    /**
     * 注册事件监听
     *
     * @param event
     */
    void register(Object event);

    /**
     * 注销事件监听
     *
     * @param event
     */
    void unregister(Object event);

    /**
     * 发布事件
     *
     * @param event
     */
    void publish(Object event);

}
