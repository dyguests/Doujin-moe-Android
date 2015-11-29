#Doujin-Moe

啊哈...这是一个Doujin-Moe的Android客户端.

主要代码参考自[NHentai-android](https://github.com/fython/NHentai-android).

<a href="./graphics/device-2015-11-28-222059.png"><img src="./graphics/device-2015-11-28-222059.png" width="40%"/></a>

#特別聲明

該應用程式所供應的內容不適合未成年人觀看，所有內容通過 Jsoup 解析 Doujin-Moe 官網獲得，內容有任何異議或造成心理甚至生理上的問題均與本項目無關。

觀看時請留意是否適用於當地法律法規。( > _ < )

#功能

1.书籍下载.

#备注

1.书籍中的page命名规则按 1.jpg(png ...) ,2.jpg 的方式来命名,不采用网站上的"乱码".

2.若已下载,添加一个重新下载的处理???

3.有空把所有null判断/验证判断全换成抛异常.

4.在detail页面给下载中的MenuItem加动画.bookList页面给book加动画.

5.最近阅读,喜欢(bookmark),已下载(本地\[本地导入和网站上下载的分开放??\]).

6.navigate不够放,改成网格型吧..

7.加排序(排序方式(date,ranking,name)

8.把超过一个月(?)没看的未下载的书(只缓存了book.json,position等)删掉.

9.在Gallery页面,加上 重新下载当前页menuItem 的功能

#修改

~~1.改用fresco,在图库界面使用fresco设置placeHolder.~~

~~2.用PhotoDraweeView来兼容Fresco和PhotoView.~~

3.换回Picasso.Fresco完全不好用.![image](graphics/face1.png)

4.用PhotoViewEx来处理横屏时图片显示的位置.

#问题

|问题     | 解决方案                                  |
| ---------------------------------------------- | --------------------------------------------- |
|~~用picasso来读取漫画页(大图)好慢..而且没法保存.~~     | ~~用fresco代替~~                                  |

