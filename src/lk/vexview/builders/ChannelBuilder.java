/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * Reserved.FileName: ChannelBuilder.java@author: karlatemp@vip.qq.com: 2020/1/27 下午7:50@version: 2.0
 */

package lk.vexview.builders;

import lk.vexview.api.VexViewAPI;
import lk.vexview.chat.VexChatChannel;
import lk.vexview.event.VexChannelChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredListener;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * VexView Channel的快速构造器
 * <p>
 * <pre>{@code
 * ChannelBuilder.builder()
 *      .name("Channel From Builder")
 *      <b>.register()</b> // 别忘记使用
 *      .onPlayerChat(event->{ // 监听可有可无
 *          if(event.getMessage().equals("lbwnb")){
 *              event.setMessage("卢本伟挂壁");
 *          }
 *      })
 *      .onPlayerChat(....);// 可有多个监听
 * }</pre>
 *
 * @author Karlatemp
 * @since 2.6
 */
public class ChannelBuilder implements Listener {
    static final Plugin plugin = Bukkit.getPluginManager().getPlugin("VexView");

    protected List<UUID>
            whites = new ArrayList<>(),
            blacks = new ArrayList<>();
    protected boolean whiteEnable;
    protected String name;
    protected VexChatChannel channel;
    private Collection<Consumer<VexChannelChatEvent>> handlers = new ConcurrentLinkedQueue<>();
    private boolean eventHandlerRegistered;

    protected ChannelBuilder() {
    }

    public static ChannelBuilder builder() {
        return new ChannelBuilder();
    }

    /**
     * 定义此Channel的名字
     *
     * @param name 通道名
     * @return 构造器本身
     */
    public ChannelBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * 通道黑名单加值
     *
     * @param target 玩家UUID
     * @return 构造器本身
     */
    public ChannelBuilder black(UUID target) {
        blacks.add(target);
        return this;
    }

    /**
     * 启动<strike>蓝洞</strike>白名单
     *
     * @return 构造器本身
     */
    public ChannelBuilder enableWhiteList() {
        whiteEnable = true;
        if (channel != null) {
            if (!channel.isWhiteEnable()) {
                channel.setWhiteList(true);
            }
            final List<UUID> whiteList = channel.getWhiteList();
            if (whiteList != whites) {
                whiteList.addAll(whites);
                whites = whiteList;
            }
        }
        return this;
    }

    /**
     * 往白名单加值并启动白名单
     *
     * @param target 玩家UUID
     * @return 构造器本身
     */
    public ChannelBuilder white(UUID target) {
        whites.add(target);
        enableWhiteList();
        return this;
    }

    /**
     * 注册此Channel
     *
     * @return 构造器本身
     */
    public synchronized ChannelBuilder register() {
        if (channel != null) return this;
        Objects.requireNonNull(name, "Channel name not set");
        channel = new VexChatChannel(name);
        if (whiteEnable) {
            channel.setWhiteList(true);
            channel.getWhiteList().addAll(whites);
        }
        final List<UUID> blackList = channel.getBlackList();
        blackList.addAll(blacks);
        blacks = blackList;
        VexViewAPI.addChatChannel(channel);
        eventInitialize();
        return this;
    }

    /**
     * 监听玩家在此通道聊天
     *
     * @param handler 回调
     * @return 构造器本身
     */
    public ChannelBuilder onPlayerChat(Consumer<VexChannelChatEvent> handler) {
        if (handler == null) return this;
        this.handlers.add(handler);
        eventInitialize();
        return this;
    }

    private synchronized void eventInitialize() {
        if (eventHandlerRegistered) return;
        if (channel == null) return;
        if (handlers.isEmpty()) return;
        VexChannelChatEvent.getHandlerList().register(new RegisteredListener(
                this, (listener, event) -> {
            if (event instanceof VexChannelChatEvent) {
                VexChannelChatEvent event0 = (VexChannelChatEvent) event;
                if (event0.getChannel() == channel) {
                    for (Consumer<VexChannelChatEvent> handler : handlers) {
                        handler.accept(event0);
                    }
                }
            }
        }, EventPriority.MONITOR, plugin, false));
        eventHandlerRegistered = true;
    }
}
