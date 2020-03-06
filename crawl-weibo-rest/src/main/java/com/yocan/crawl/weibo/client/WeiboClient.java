package com.yocan.crawl.weibo.client;

import com.yocan.crawl.weibo.dto.MvcWeiboReptile;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.sun.xml.internal.fastinfoset.stax.events.XMLConstants.ENCODING;

@Slf4j
@Service
public class WeiboClient {

    /**
     * 缓存已发消息，避免重复发送
     */
    private volatile String msg ="";
    private volatile int size =0;

    /**
     * 基于HttpClient 4.3的通用Get方法--微博Cookie
     *
     * @param url 提交的URL
     * @return 提交响应
     */
    public static String get_byCookie(String url, String cookie) {
        if ("1".equals(cookie)) {
            cookie =
"SCF=ApaZZIFhzwZli4y-vPAyVRZkOUyDhLZcvk-LByWeRR8-DzpfT_VcsM0rDtwOhRYyYFKI4tftavGPvS4rMnGWm5Q.; SUB=_2A25zW3ecDeRhGeNK6FAZ9S7Kyz6IHXVQEe5UrDV8PUNbmtAfLRHgkW9NSXnchG0KrQ01hy5sjy99wNhdJgnI39yf; SUHB=0nl-P7iOgtSxUT; UOR=www.dahepiao.com,widget.weibo.com,login.sina.com.cn; webim_unReadCount=%7B%22time%22%3A1583288980935%2C%22dm_pub_total%22%3A0%2C%22chat_group_client%22%3A0%2C%22allcountNum%22%3A3%2C%22msgbox%22%3A0%7D";       }
        CloseableHttpClient client = HttpClients.createDefault();
        String responseText = "";
        CloseableHttpResponse response = null;
        try {
            HttpGet method = new HttpGet(url);
            method.addHeader(new BasicHeader("Cookie", cookie));
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(20 * 1000) // 连接建立超时，毫秒。
                    .build();
            method.setConfig(config);
            response = client.execute(method);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseText = EntityUtils.toString(entity, ENCODING);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return responseText;
    }

    /**
     * 爬取微博JS内容
     *
     * @param
     * @return
     */
    public static List<MvcWeiboReptile> get_js_html_byuid(String cookie) {
        //默认无数据
        List<MvcWeiboReptile> weiboReptileList = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(WeiboClient.get_byCookie("https://weibo.com/p/1005051646174132/home?from=page_100505_profile&wvr=6&mod=data&is_all=1#place", cookie));
        if (stringBuffer != null) {
            Document document = Jsoup.parse(stringBuffer.toString());
            Elements scripts = document.select("script");
            for (Element script : scripts) {
                String[] ss = script.html().split("<script>FM.view");
                stringBuffer = new StringBuffer();
                for (String x : ss) {
                    if (x.contains("\"html\":\"")) {
                        stringBuffer.append(getHtml(x));
                    }
                }
                document = Jsoup.parse(stringBuffer.toString());
                Elements WB_details = document.getElementsByClass("WB_detail");
                for (Element WB_detail : WB_details) {
                    Elements WB_infos = WB_detail.getElementsByClass("WB_info");
                    if (!WB_infos.isEmpty() && WB_infos.size() == 1) {
                        for (Element WB_info : WB_infos) {
                            Elements WB_text = WB_detail.getElementsByClass("WB_text");
                            Elements WB_from = WB_detail.getElementsByClass("WB_from S_txt2");
                            String text = WB_text.html();
                            MvcWeiboReptile weiboReptile = new MvcWeiboReptile();
                            weiboReptile.setContext(text);
                            weiboReptileList.add(weiboReptile);

                        }
                    } else {
                    }
                }
            }
        }
        return weiboReptileList;
    }

    private static String getHtml(String str) {
        str = str.replaceAll("FM.view\\(", "").replaceAll("\\)", "");
        JSONObject json = JSONObject.fromObject(str);
        return json.getString("html");
    }


    public void getText(String url, String wechatUrl,String cookie) {
            List<MvcWeiboReptile> weiboReptiles = WeiboClient.get_js_html_byuid(cookie);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForEntity(wechatUrl + weiboReptiles.get(1).getContext(), String.class);
    }

    /**
     *String s = WeiboClient.get_byCookie(url, cookie);
     */
    public void getTextUtil(String url, String wechatUrl,String cookie) {
        while (true){
            List<MvcWeiboReptile> weiboReptiles = WeiboClient.get_js_html_byuid( cookie);
            // 确保超过2条（除首页外还有一条最新），且最新消息没有推送过，如推送过不再推送
            if (weiboReptiles.size()>=2 && !msg.equals(weiboReptiles.get(1).getContext())){
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getForEntity(wechatUrl + weiboReptiles.get(1).getContext(), String.class);
                msg =weiboReptiles.get(1).getContext();
                size =weiboReptiles.size();
            }
            try {
                Thread.sleep(1000 * 60 * 10);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    public static void main(String[] args) {

        while (true) {
            List<MvcWeiboReptile> weiboReptiles = WeiboClient.get_js_html_byuid( "1");
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getForEntity("https://sc.ftqq.com/SCU40692Tccb6b97f9f07146f0b5ac909ca1bfcf35c3c6e3cd17d2.send?text=" + weiboReptiles.get(1).getContext(), String.class);

        }
    }
}