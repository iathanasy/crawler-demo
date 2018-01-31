package com.crawler.demo4;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.common.io.Files;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler {

	 private Parser parser;
	 private PageFetcher pageFetcher;
	 private StringBuffer buffer;
	 
	 String baseUrlPrefix = "http://kaijiang.zhcw.com/zhcw/html/ssq/list_";
     String baseUrlSuffix = ".html";
     String homeUrl = "http://kaijiang.zhcw.com/zhcw/html/ssq/list_1.html";
	 
	 public MyCrawler() {
		CrawlConfig config = new CrawlConfig();//实例化爬虫配置
		parser = new Parser(config);//实例化解析器
		pageFetcher = new PageFetcher(config);//实例化页面获取器
		buffer = new StringBuffer();
	}
	 
	/**
	 * 处理结果
	 * @throws IOException 
	 */
	public void handleResults() throws IOException{
		//获取总页数
		int count = getPageCount(getHtml());
		if(count < 1) return;
		for (int i = 1; i < count; i++) {
			String url = baseUrlPrefix + i + baseUrlSuffix ;
			Page page = page(url);
	        System.out.println("url :"+ url);
	        if (page.getParseData() instanceof HtmlParseData) {  // 判断是否是html数据
	            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); // 强制类型转换，获取html数据对象
	            String html = htmlParseData.getHtml();  // 获取页面Html
	            Document doc = Jsoup.parse(html); 
	            Elements elementsByTable = doc.getElementsByTag("table");
	            String table = elementsByTable.first().toString();
	            //System.out.println(table);
	            Elements elementsByTr = doc.getElementsByTag("tr");
	            //获取第一个tr title
	            String th = elementsByTr.first().toString();
	        	 //获取标题
	            getTitle(th, doc);
	            //获取内容
	            //获取td
	            String td = elementsByTr.toString();
	            //获取内容
	            getContent(td, doc);
	       
		    	//睡眠
		    	try {
		            Thread.sleep(1200);
		        } catch (Exception e) {
		            // TODO: handle exception
		        }
			}
		}
        //拼接内容
        System.out.println(buffer.toString());
        try{
	        // 把爬取到的文件存储到指定文件
	        File file = new File("D://福彩.txt");
	        if (file.exists()) {
	            file.delete();
	        }
	        FileWriter writer = new FileWriter(file);
	        BufferedWriter bufferedWriter = new BufferedWriter(writer);
	        bufferedWriter.write(buffer.toString());
	        bufferedWriter.close();
	        writer.close();
        }catch(Exception e){
        	
        }
	}
	
	/**
	 * 获取整个页面
	 * @return
	 */
	public String getHtml(){
		Page page = page(homeUrl);
    	if (page.getParseData() instanceof HtmlParseData) {  // 判断是否是html数据
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData(); // 强制类型转换，获取html数据对象
            String html = htmlParseData.getHtml();  // 获取页面Html
            return html;
    	}
    	return null;
	}
	
	/**
	 * 获取头部
	 * @param th
	 */
	public void getTitle(String th,Document doc){
		Elements elementsByTh = doc.getElementsByTag("th");
		for (int i = 0; i < elementsByTh.size(); i++) {
			if(i < (elementsByTh.size() - 2)){
				buffer.append(elementsByTh.get(i).text()).append("\t\t");
			}
			
		}
		buffer.append("\n");
	}
	
	/**
	 * 获取内容
	 * @param tr
	 * @param doc
	 */
	public void getContent(String td,Document doc){
		Elements elementsByTr = doc.getElementsByTag("tr");
		for (int i = 0; i < elementsByTr.size(); i++) {
			if(i == 0 || i == 1){
				continue;
			}
			
			Element element = elementsByTr.get(i);
			if(element.children().size() > 3){
				for (int j = 0; j < element.children().size(); j++) {
					buffer.append(element.children().get(j).text()).append("\t ");
				}
				buffer.append("\n");
			}
			
		}
		
	}
	
	/**
     * 获取总页数
     * @param result
     */
    private int getPageCount(String result) {
        String regex = "\\d+\">末页";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(result);
        String[] splits = null;
        while (matcher.find()) {
            String content = matcher.group();
            splits = content.split("\"");
            break;
        }
        if (splits != null && splits.length == 2) {
            String countString = splits[0];
            if (countString != null && !countString.equals("")) {
                return Integer.parseInt(countString);
            }

        }
        return 0;
    }
	
	/**
	 * 获取页面
	 * @param url
	 * @return
	 */
	private Page page(String url) {
		WebURL curURL = new WebURL(); // 实例化weburl
		curURL.setURL(url); // 设置url
		PageFetchResult fetchResult = null;
		try {
			fetchResult = pageFetcher.fetchPage(curURL); // 获取爬取结果
			if (fetchResult.getStatusCode() == HttpStatus.SC_OK) { // 判断http状态是否是200
				Page page = new Page(curURL); // 封装Page
				fetchResult.fetchContent(page); // 设置内容
				parser.parse(page, curURL.getURL()); // 解析page
				return page; // 返回page
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fetchResult != null) { // 假如jvm没有回收 用代码回收对象 防止内存溢出
				fetchResult.discardContentIfNotConsumed(); // 销毁获取内容
			}
		}
		return null;
	}
	
	
	
	public static void main(String[] args) throws IOException {
		MyCrawler crawler = new MyCrawler();
		crawler.handleResults();
	}
}
