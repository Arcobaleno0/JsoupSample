# JsoupSample
jsoupSample

使用`Jsoup`抓取一些网站的数据，抓取数据的网站已经列在下面，如果有侵权嫌疑，请及时联系我删除相关数据

* fiction

	>浏览小说
	
	[81中文网](http://www.81zw.com/book/8012/)<br>
	
* image

	>抓取一些图片
	
	[豆瓣美女](http://www.dbmeinv.com/)<br>
	[妹子图](http://www.mzitu.com/)<br>
	
* movie

	>抓取一些电影
	
	[dytt8](http://www.dytt8.net/index.htm)<br>
	[dy2018](http://www.dy2018.com/)<br>
	[xiaopian](http://www.xiaopian.com/html/)<br>
	
	
整个框架围绕着`framework`这个`modules`展开。

里面包含了`jsoup`开发的常用控件以及`MVP`的Base类。

## 框架介绍：

* base

	包含了基本的`BaseActivity` `BaseFragment` 之类的基类
	
* util

	网络加载，html加载，图片，UI类的方法
	
* widget

	包括了 自适应高度的`ImageView`,可检测滑动底部的`RecyclerView` 等等 一些控件
	

为了以后看起来不乱，项目是按照一个网站一个模块来区分的，例如`movie` 每个网站的数据都是一个独立的模块

## 非常感谢以下开源框架：

> android.support
		
	android
		
> [glide](https://github.com/bumptech/glide)
		
	加载图片
		
> [rxJsoupNetWork](https://github.com/7449/RxNetWork/tree/RxJsoupNetWork)

	Jsup加载网络框架
	
> [klog](https://github.com/ZhaoKaiQiang/KLog)

	打印Log
	
> [flowlayout](https://github.com/ApmeM/android-flowlayout)

	flowlayout


	
	