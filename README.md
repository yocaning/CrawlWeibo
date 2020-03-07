# CrawlWeibo

Crawl Someone's Weibo text (for quick get and quick view )

爬取某个人的微博最新内容，并发送到微信端，方便快速获取，不必一直刷微博

（笔者是为了偷懒不想刷微博，不想影响工作状态，又想快速实时获取某炒股博主的最新weibo）

---

- 前期参数准备
   - 发送给微信端采用[【server酱】](http://sc.ftqq.com/3.version)进行微信信息端发送，在server 酱中获取SCKEY参数
   - 爬取某个人的微博时，需先去weibo获取网页链接 如：
   
   ``https://weibo.com/p/1005051646174132/home?from=page_100505_profile&wvr=6&mod=data&is_all=1#place``
   
- 如果使用（how to use）
    - 将代码打成jar包，直接在jdk环境中执行，java -jar
    - 在服务器本地调用 
        - localhost:11176/crawl/weiBo 为单次获取
        - localhost:11176/crawl/weiBo/keep 为持续获取，每隔10分钟查看一次
    - 参数为ParamDto
        - ~~示例~~：
       
            ```json
            {"userUrl":"https://weibo.com/p/1005051646174132/home?from=page_100505_profile&wvr=6&mod=data&is_all=1#place","weChatUrl":"https://sc.ftqq.com/SCU40692Tccb6b97f9f07146f0b5ac909ca1bfcf35c3c6e3cd17d2.send?text="}
            ```
        - 示例（2020-03-06更新）
            - cookie值可使用账号登录微博之后，打开浏览器控制台（F12），在控制台复制相关cookie
               
            ```json
            {"userUrl":"https://weibo.com/p/1005051646174132/home?from=page_100505_profile&wvr=6&mod=data&is_all=1#place","weChatUrl":"https://sc.ftqq.com/SCU40692Tccb6b97f9f07146f0b5ac909ca1bfcf35c3c6e3cd17d2.send?text=","cookie":"SCF=ApaZZIFhzwZli4y-vPAyVRZkOUyDhLZcvk-LByWeRR8-DzpfT_VcsM0rDtwOhRYyYFKI4tftavGPvS4rMnGWm5Q.; SUB=_2A25zW3ecDeRhGeNK6FAZ9S7Kyz6IHXVQEe5UrDV8PUNbmtAfLRHgkW9NSXnchG0KrQ01hy5sjy99wNhdJgnI39yf; SUHB=0nl-P7iOgtSxUT; UOR=www.dahepiao.com,widget.weibo.com,login.sina.com.cn; webim_unReadCount=%7B%22time%22%3A183288980935%2C%22dm_pub_total%22%3A0%2C%22chat_group_client%22%3A0%2C%22allcountNum%22%3A3%2C%22msgbox%22%3A0%2D"}
            ```

- 效果图
    - ![公众号](crawl-weibo-rest/src/main/resources/41583203175_.pic.jpg "效果图")
***
- 后续
    - 期待做成通过OAuth授权调用weibo api的做法来获取用户微博内容
    - 功能简单，还有更多不完善的期待一起完善

- 致谢
    -    [【server酱】](http://sc.ftqq.com/3.version)

- 联系我
    - 如有什么疑问或者建议，我的[邮箱](yocaning@gmail.com)
 
- 赞赏
    - ![赞赏](crawl-weibo-rest/src/main/resources/51583205231_.pic.jpg "效果图")
