Figo 公司模块
=========

### 一、 网络框架
- 第一步
```kotlin
    private val retrofit = BaseRetrofit.getRetrofit("http://127.0.0.1:8080/")
    private val apiService = retrofit.create(FigoApiService::class.java)
```
- 第二步
```kotlin
    //参数可使用 FigoRetrofitParam 构建
    val map = FigoRetrofitParam().newBuilder()
                .addPage(0)
                .addSize(20)
                .build()
    //http请求
    val observable = apiService.getData("./countDown:countDowns/", map)
```
- 第三步
```kotlin
    observable
        //对返回数据进行预处理 [FigoHttpResultUtils]
        //注意：预处理不要再UI线程上操作
        .map(HandleResultList<TestBean>())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<ListData<TestBean>?>{
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {

            }

            override fun onNext(t: ListData<TestBean>) {

            }

            override fun onError(e: Throwable) {

            }
        })
```

### 二、阿里云OSS
```
    //文件上传服务
    OssUploadsService
```

### 三、支付 （支付宝 + 微信）
```
    PayHelper
```

### 三、友盟 （分享 + 统计 + bug收集）
```
    //分享
    FigoShareHelper
```