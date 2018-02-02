## 说明

jremoter为一款基于注解实现的IOC容器框架,功能类似于Spring,且无第三方依赖,类库使用简单,灵活

## 注解说明
* @JRemoterApplication
	> runner class 必须配置次注解,此注解可设置读取的配置文件名称和类型
* @Configuration
	> 配置类,可设置扫描的包信息
* @Service
	> 指定类通过JRemoter进行实例化
* @Autowired
	> 自动注入指定类型的类
* @Value
	> 自动注入配置项中的
* @InitialMethod
	> 标注在实例化的类中的方法上,容器属性注入结束后会执行当前标注方法,可标注多个方法,且可以排序
* @DestoryMethod
	> 标注在实例化的类中的方法上,容器属性销毁结束前会执行当前标注方法,可标注多个方法,且可以排序