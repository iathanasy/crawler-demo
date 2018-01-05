package com.crawler.demo2;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
/**
 *  edu.uci.ics.crawler4j.crawler 基本逻辑和配置 
	edu.uci.ics.crawler4j.fetcher 爬取 
	edu.uci.ics.crawler4j.frontier URL队列相关 
	edu.uci.ics.crawler4j.parser 对爬取结果进行解析 
	edu.uci.ics.crawler4j.robotstxt 检查robots.txt是否存在 
	edu.uci.ics.crawler4j.url URL相关，主要是WebURL 
	edu.uci.ics.crawler4j.util 是工具类 
* @Project: crawler-demo 
* @Class demos 
* @Description: TODO
* @author cd 14163548@qq.com
* @date 2018年1月5日 下午6:10:44 
* @version V1.0
 */
public class demos {
	public static void main(String[] args) throws Exception {
		
		int numberOfCrawlers = 7; // 定义7个爬虫，也就是7个线程
		String crawlStorageFolder = "d://crawler";
        CrawlConfig config = new CrawlConfig(); // 定义爬虫配置
        config.setCrawlStorageFolder(crawlStorageFolder); // 设置爬虫文件存储位置
        //config.setMaxDepthOfCrawling(0);//抓取深度
        /*
         * 实例化爬虫控制器
         */
        PageFetcher pageFetcher = new PageFetcher(config); // 实例化页面获取器
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig(); // 实例化爬虫机器人配置 比如可以设置 user-agent
        // 实例化爬虫机器人对目标服务器的配置，每个网站都有一个robots.txt文件 规定了该网站哪些页面可以爬，哪些页面禁止爬，该类是对robots.txt规范的实现
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher); 
        // 实例化爬虫控制器
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        
        /**
         * 配置爬虫种子页面，就是规定的从哪里开始爬，可以配置多个种子页面
         */
        //controller.addSeed("http://music.163.com/");
        controller.addSeed("http://music.163.com/discover/playlist/?order=hot");
        
        /**
         * 启动爬虫，爬虫从此刻开始执行爬虫任务，根据以上配置
         */
        controller.start(MyCrawler.class, numberOfCrawlers);
	}
}
