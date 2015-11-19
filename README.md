#Doujin-Moe

啊哈...这是一个Doujin-Moe的Android客户端.

主要代码参考自[NHentai-android](https://github.com/fython/NHentai-android).

#特別聲明

該應用程式所供應的內容不適合未成年人觀看，所有內容通過 Jsoup 解析 Doujin-Moe 官網獲得，內容有任何異議或造成心理甚至生理上的問題均與本項目無關。

觀看時請留意是否適用於當地法律法規。( > _ < )

#修改

1.改用fresco,在图库界面使用fresco设置placeHolder.
2.用PhotoDraweeView来兼容Fresco和PhotoView.

#问题

|问题     | 解决方案                                  |
| ---------------------------------------------- | --------------------------------------------- |
|用picasso来读取漫画页(大图)好慢..而且没法保存.     | 用fresco代替                                  |

