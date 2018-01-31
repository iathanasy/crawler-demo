package com.crawler.demo5.queue;

import java.util.HashSet;
import java.util.Set;

public class LinkQueue {

	//已访问的url集合
	private static Set visitedUrl = new HashSet<>();
	//待访问的url
	private static Queue unvisitedUrl = new Queue();
	
	//获取url队列
	public static Queue getUnvisitedUrl(){
		return unvisitedUrl;
	}
	
	//添加到访问过的url队列中
	public static void addVisitedUrl(String url){
		visitedUrl.add(url);
	}
	
	//移除已访问的url
	public static void removeVisitedUrl(String url){
		visitedUrl.remove(url);
	}
	
	//未访问的url 出队列
	public static Object unvisitedUrlDeQueue(){
		return unvisitedUrl.deQueue();
	}
	
	//保证每个url只被访问一次
	public static void addUnvisitedUrl(String url){
		if(url != null && !url.trim().equals("")
				&& !visitedUrl.contains(url)
				&& !unvisitedUrl.contains(url))
			unvisitedUrl.enQueue(url);
	}
	
	//获得已访问的url数目
	public static int getVisitedUrlNum(){
		return visitedUrl.size();
	}
	
	//判断未访问的url队列中是否为空
	public static boolean unVisitedUrlEmpty(){
		return unvisitedUrl.isQueueEmpty();
	}
}
