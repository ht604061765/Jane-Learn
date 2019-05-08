package com.tenfine.napoleon.smzPlatform.client;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.tenfine.napoleon.user.dao.po.Device;

import io.netty.channel.Channel;


/**
 * 接收器缓存管理
 * 将有效连接进行缓存管理
 * 单件模式
 * @author Hunter
 */
public class CacheManager {
    /**
     *  单件模式前提：构造器为私有方法，不能被其他对象实例化
      */
	private CacheManager() {}

    /**
     * 懒汉式单例模式加载方式。volatile 保证了新值能立即同步到主内存
     */
	private volatile static ConcurrentHashMap<String, ClientHandler> handlerCache;
	private volatile static ConcurrentHashMap<String, Thread> threadsCache;
	private volatile static ConcurrentHashMap<String, Object> heartbeatCache;

    /**
     * 饿汉式单例模式加载方式
     */
	private volatile static LinkedHashMap<String, Object> sessionCache_832 = new LinkedHashMap<String, Object>() {
		private static final long serialVersionUID = 8180689756849496081L;
		int maximumSize = 50;
		@Override
        protected boolean removeEldestEntry(Map.Entry<String, Object> eldest) {
            return size() > maximumSize;
        }
    };
    
    private volatile static LinkedHashMap<String, List<Map<String, Object>>> sessionCache_825_842 = new LinkedHashMap<String, List<Map<String, Object>>>() {
		private static final long serialVersionUID = 7441716376305543977L;
		int maximumSize = 50;
		@Override
        protected boolean removeEldestEntry(Map.Entry<String, List<Map<String, Object>>> eldest) {
            return size() > maximumSize;
        }
    };
	
    /**
	 * 获取【825/842】指令会话缓存实例
	 */
	public static Map<String, List<Map<String, Object>>> getSessionCache_attend() {
		return sessionCache_825_842;
	}
    
    /**
	 * 获取【832】指令会话缓存实例
	 */
	public static Map<String, Object> getSessionCache_832() {
		return sessionCache_832;
	}
    
	/**
	 * 获取心跳时间缓存实例
	 */
	public static Map<String, Object> getHeartbeatCache() {
		if(heartbeatCache == null) {
			synchronized (CacheManager.class) {
				if(heartbeatCache == null) {
					heartbeatCache = new ConcurrentHashMap<String, Object>();
				}
			}
		}
		return heartbeatCache;
	}
	
	/**
	 * 获取线程缓存实例
	 */
	public static Map<String, Thread> getThreadCache() {
		if(threadsCache == null) {
			synchronized (CacheManager.class) {
				if(threadsCache == null) {
					threadsCache = new ConcurrentHashMap<String, Thread>();
				}
			}
		}
		return threadsCache;
	}
	
	/**
	 * 增加线程
	 */
	public static void putThreadCache(String deviceNo, Thread thread) {
		threadsCache.put(deviceNo, thread);
	}
	
	/**
	 * 获取Handler缓存实例
	 */
	private static Map<String, ClientHandler> getHandlerCache() {
		if(handlerCache == null) {
			synchronized (CacheManager.class) {
				if(handlerCache == null) {
					handlerCache = new ConcurrentHashMap<String, ClientHandler>();
				}
			}
		}
		return handlerCache;
	}
	
	/**
	 * 通过设备号获取接收器
	 */
	static ClientHandler getHandlerByDevice(String deviceNo) {
		Map<String, ClientHandler> handlerMap = getHandlerCache();
		if(handlerMap != null) {
			Set<String> handlerSet = handlerMap.keySet();
			Iterator<String> it = handlerSet.iterator();
			while(it.hasNext()){
				String cacheKey = it.next();
				if(cacheKey.contains(deviceNo)) {
					ClientHandler handler = handlerMap.get(cacheKey);
					return handler;
				}
			}
		}
		return null;
	}
	
	/**
	 * 获取项目下一个有效连接
	 */
	public static Channel getProjectChannel(String projectId) {
		Map<String, ClientHandler> handlerCache = getHandlerCache();
		if(handlerCache != null) {
			Set<String> keySet = handlerCache.keySet();
			Iterator<String> it = keySet.iterator();
			while(it.hasNext()){
				String cacheKey = it.next();
                //截取"_"之前的字符串
				String proId = cacheKey.substring(0, cacheKey.indexOf("_"));
				if(projectId.equals(proId)) {
					ClientHandler handler = handlerCache.get(cacheKey);
					Channel channel = handler.channel;
					if(channel.isActive() && channel.isOpen()) {
						return channel;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 添加设备连接接收器至缓存
	 */
	public static void putHandler(Device device, ClientHandler clientHandler) {
		Map<String, ClientHandler> handlerCache = getHandlerCache();
		String cacheKey;
		if(device.getPlatformProId() == null || device.getPlatformProId().isEmpty()) {
            // 项目Id为空时
			cacheKey = "_" + device.getDeviceNo();
		}else {
			cacheKey = device.getPlatformProId() + "_" + device.getDeviceNo();
		}
		handlerCache.put(cacheKey, clientHandler);
	}
	
	/**
	 * 移除所有包含这个deviceNo的缓存
	 * @param deviceNo
	 */
	public static void removeHandler(String deviceNo) {
		Map<String, ClientHandler> handlerMap = getHandlerCache();
		if(handlerMap != null) {
			Set<String> handlerSet = handlerMap.keySet();
			Iterator<String> it = handlerSet.iterator();
			while(it.hasNext()){
				String cacheKey = it.next();
				if(cacheKey.contains(deviceNo)) {
                    // 移出缓存
					handlerMap.remove(cacheKey);
				}
			}
		}
	}

}
