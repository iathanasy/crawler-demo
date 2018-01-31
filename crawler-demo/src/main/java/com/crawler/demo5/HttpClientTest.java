package com.crawler.demo5;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.crawler.demo5.queue.LinkQueue;
import com.crawler.demo5.queue.ProxyIp;
import com.crawler.demo5.queue.Queue;

/**
 * 
* @Project: crawler-demo 
* @Class HttpClientTest httpclient 动态代理对象
* @Description: TODO
* @author cd 14163548@qq.com
* @date 2018年1月29日 上午10:39:48 
* @version V1.0
 */
public class HttpClientTest {

	private final static Logger logger = Logger.getLogger(HttpClientTest.class);
	private static Queue queue = new Queue();
	private static ArrayBlockingQueue q = new ArrayBlockingQueue<>(100);
	
	//知乎首页
    public final static String INDEX_URL = "https://www.zhihu.com";
    
    /**
     * 爬虫入口
     */
    public static String  startURL = "https://www.zhihu.com/people/wo-yan-chen-mo/following";
    
	private final static String charset = "gb2312";
	public final static String USER_FOLLOWEES_URL = "https://www.zhihu.com/api/v4/members/%s/followees?" +
            "include=data[*].educations,employments,answer_count,business,locations,articles_count,follower_count," +
            "gender,following_count,question_count,voteup_count,thanked_count,is_followed,is_following," +
            "badge[?(type=best_answerer)].topics&offset=%d&limit=20";
	public static void main(String[] args) throws Exception {
		showProxy();
		while (!queue.isQueueEmpty()) {
			ProxyIp proxyIp = (ProxyIp) queue.deQueue();
			crawlerHtml(INDEX_URL, charset, proxyIp);
		}
		
		while(!q.isEmpty()){
			logger.info("可用的ip代理："+q.peek());
		}
	}
	
	/**
	 * 动态获取代理
	 */
	public static void showProxy(){
		//需要爬取得ip url
		int page = 8;
		for (int i = 1; i <= page; i++) {
			//待访问url
			LinkQueue.addUnvisitedUrl("http://www.66ip.cn/" + i + ".html");
		}
		
		//待访问的url
		while (!LinkQueue.unVisitedUrlEmpty()) {
			String url = (String) LinkQueue.unvisitedUrlDeQueue();
			getPage(url, charset);
			/*logger.info("-----------------已访问的url---------------------");
			logger.info("url:"+url);*/
		}
		
		/*logger.info("-----------------未访问的url---------------------");
		while (!LinkQueue.unVisitedUrlEmpty()) {
			logger.info("unurl:"+LinkQueue.getUnvisitedUrl());
		}
		logger.info("-----------------代理ip---------------------");
		while (!queue.isQueueEmpty()) {
			ProxyIp proxyIp = (ProxyIp) queue.deQueue();
			logger.info("proxyIp:"+proxyIp.toString());
		}*/
	}
	
	
	
	/**
	 * 获取当前响应的页面
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public static void getPage(String url,String charset){
		CloseableHttpClient httpClient=HttpClients.createDefault(); // 创建httpClient实例
		HttpGet httpGet = new HttpGet(url);
		httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0"); // 设置请求头消息User-Agent
		//连接超时及读取超时
		RequestConfig config=RequestConfig.custom()
                .setConnectTimeout(5000)
                .setSocketTimeout(5000)
                .build();
		httpGet.setConfig(config);
		CloseableHttpResponse response= null;
       try {
    	    response = httpClient.execute(httpGet); // 执行http get请求
			HttpEntity entity=response.getEntity(); // 获取返回实体
			//响应码
			int status = response.getStatusLine().getStatusCode();
			if(200 != status){
				return;
			}
			//添加已访问的url
			LinkQueue.addVisitedUrl(url);
			//获取内容
			String content = EntityUtils.toString(entity, charset);
			Header header = entity.getContentType();
			/*logger.info("header:"+header);
			logger.info("content:"+content);*/
			Document document = Jsoup.parse(content);
	        Elements elements = document.select("table tr:gt(1)");
	        
	        for (Element element : elements){
	            String ip = element.select("td:eq(0)").first().text();
	            String port  = element.select("td:eq(1)").first().text();
	            String isAnonymous = element.select("td:eq(3)").first().text();
	            //代理ip放入队列
	            queue.enQueue(new ProxyIp(ip, Integer.parseInt(port), isAnonymous));
//	            if(!anonymousFlag || isAnonymous.contains("匿")){
	               
//	            }
	        }
		} catch (Exception e) {
			logger.error("error:{"+e.getMessage()+"}");
		}finally{
			try {
				 httpClient.close(); // httpClient关闭
				 // response关闭
				 response.close();
			} catch (Exception e) {
				e.printStackTrace();
			} 
	       
		}
	}
	
	/**
	 * 代理抓取网页
	 * @param url
	 * @param charset
	 * @throws Exception
	 */
	public static void crawlerHtml(String url,String charset,ProxyIp proxyIp){
		CloseableHttpClient httpClient=HttpClients.createDefault(); // 创建httpClient实例
        HttpGet httpGet=new HttpGet(url); // 创建httpget实例
        HttpHost proxy=new HttpHost(proxyIp.getIp(), proxyIp.getPort());
        //设置超时 代理
        RequestConfig requestConfig=RequestConfig.custom()
        		.setConnectTimeout(2000)
                .setSocketTimeout(3000)
        		.setProxy(proxy).build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
        CloseableHttpResponse response= null;
        try {
        	response = httpClient.execute(httpGet); // 执行http get请求
			HttpEntity entity = response.getEntity(); // 获取返回实体
			if (200 != response.getStatusLine().getStatusCode()) {
				return;
			}
			//可用的ip代理
			q.add(proxyIp);
			logger.info("网页内容：" + EntityUtils.toString(entity, charset)); // 获取网页内容
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			try {
				if(response != null){
					// response关闭
					response.close();
				}
				if(httpClient != null){
					httpClient.close(); // httpClient关闭
				}
		       
				
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
		
	}
	
	/**  
	 * 正则获取字符编码  
	 * @param content  
	 * @return  
	 */    
	private static String getCharSet(String content){    
	    String regex = ".*charset=([^;]*).*";    
	    Pattern pattern = Pattern.compile(regex);    
	    Matcher matcher = pattern.matcher(content);    
	    if(matcher.find())    
	        return matcher.group(1);    
	    else    
	        return null;    
	} 
}
