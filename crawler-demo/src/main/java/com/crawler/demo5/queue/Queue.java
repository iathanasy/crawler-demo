package com.crawler.demo5.queue;

import java.util.LinkedList;

/**
 * 
* @Project: crawler-demo 
* @Class Queue 队列保存将要访问的url
* @Description: TODO
* @author cd 14163548@qq.com
* @date 2018年1月26日 上午10:03:14 
* @version V1.0
 */
public class Queue {

	//使用链表实现队列
	private LinkedList queue = new LinkedList();
	
	//入队列
	public void enQueue(Object object){
		queue.addLast(object);
	}
	
	//出队列
	public Object deQueue(){
		return queue.removeFirst();
	}
	
	//判断是否为空
	public boolean isQueueEmpty(){
		return queue.isEmpty();
	}
	
	//判断是否包含
	public boolean contains(Object object){
		return queue.contains(object);
	}
}
