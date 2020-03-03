package com.yocan.crawl.weibo.app;

import com.yocan.crawl.weibo.client.WeiboClient;
import com.yocan.crawl.weibo.dto.ParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author yocan
 */
@RestController
@RequestMapping("/crawl")
public class CrawlWeiboTextController {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    WeiboClient weiboClient;

    /**
     *单次发送
     * @param userUrl 需要快速获取的微博url
     * @param WeChatUrl server 酱提供的url
     */
    @RequestMapping("/weiBo")
    public void crawlText(@RequestBody ParamDto paramDto){
        weiboClient.getText(paramDto.getUserUrl(),paramDto.getWeChatUrl());

    }

    /**
     * 一直发送
     * @param paramDto
     */
    @RequestMapping("/weiBo/keep")
    public void crawlTest(@RequestBody ParamDto paramDto){
        weiboClient.getTextUtil(paramDto.getUserUrl(),paramDto.getWeChatUrl());
    }

}
