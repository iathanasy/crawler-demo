package com.crawler.demo2;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler{

	private final static Logger logger = Logger.getLogger(MyCrawler.class);
	
	 /**
     * 正则匹配指定的后缀文件
     */
    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg"
                                                           + "|png|mp3|mp3|zip|gz))$");
    
    /**
     * 这个方法主要是决定哪些url我们需要抓取，返回true表示是我们需要的，返回false表示不是我们需要的Url
     * 第一个参数referringPage封装了当前爬取的页面信息
     * 第二个参数url封装了当前爬取的页面url信息
     */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		 String href = url.getURL().toLowerCase();  // 得到小写的url
		 return !FILTERS.matcher(href).matches()   // 正则匹配，过滤掉我们不需要的后缀文件
	                && href.startsWith("http://music.163.com/discover/playlist/?order=hot");  // url必须是http://wwwbaidu.com/开头，规定站点
	}
	
	
	/**
     * 当我们爬到我们需要的页面，这个方法会被调用，我们可以尽情的处理这个页面
     * page参数封装了所有页面信息
     */
	@Override
	public void visit(Page page) {
        String url = page.getWebURL().getURL();  // 获取url
        logger.info("url:"+url);
         if (page.getParseData() instanceof HtmlParseData) {  // 判断是否是html数据
             HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); // 强制类型转换，获取html数据对象
             //String text = htmlParseData.getText();  // 获取页面纯文本（无html标签）
             String html = htmlParseData.getHtml();  // 获取页面Html
             //Set<WebURL> links = htmlParseData.getOutgoingUrls();  // 获取页面输出链接
             Document doc = Jsoup.parse(html); 
             Elements elementsByTag = doc.getElementById("m-pl-container").getElementsByTag("li");
             for (Element element : elementsByTag) {
				//歌单名称
            	String title = element.getElementsByClass("s-fc0").first().text();
            	//歌单链接
            	String links = element.select(".msk").attr("href");
            	//歌单图片
            	String img = element.select(".j-flag").attr("src");
            	//上传人
            	String uploads = element.getElementsByClass("s-fc3").first().text();
            	
            	logger.info("歌单名称:"+title);
            	logger.info("歌单链接:"+links);
            	logger.info("歌单图片:"+img);
            	logger.info("上传人:"+uploads);
			}
             
          
         }
	}
}
