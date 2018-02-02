## 说明

jremoter为一款基于注解实现的IOC容器框架,功能类似于Spring,且无第三方依赖,类库使用简单,灵活

## 注解说明
* @JRemoterApplication
	> runner class 必须配置此注解,此注解可设置读取的配置文件名称和类型
* @Configuration
	> 配置类,可设置扫描的包信息
* @Service
	> 指定类通过JRemoter进行实例化,可标注在方法上.并通过手动实例化
* @Autowired
	> 自动注入指定类型的类
* @Value
	> 自动注入配置项中的
* @InitialMethod
	> 标注在实例化的类中的方法上,容器属性注入结束后会执行当前标注方法,可标注多个方法,且可以排序
* @DestoryMethod
	> 标注在实例化的类中的方法上,容器属性销毁结束前会执行当前标注方法,可标注多个方法,且可以排序

## 扩展点(系统底层使用SPI机制扩展点,特殊需求可自定义扩展点实现底层业务扩展)
* com.jremoter.core.bean.BeanContainerFactory(Bean容器工厂扩展点)
	> 此扩展点可管理Bean容器的初始化,销毁,注册等,并可管理BeanDefinition对象集合
* com.jremoter.core.bean.BeanDefinitionFactory(Bean定义工厂扩展点)
	> 此扩展点可管理Bean定义对象信息,或者扩展Bean定义
* com.jremoter.core.option.Configuration(全局配置项扩展点)
	> 此扩展点可根据需求定义配置文件读取格式等,目前内置properties属性文件,可通过扩展实现读取json文件,数据库或网络等
* com.jremoter.core.pattern.PatternMatcher(路径匹配扩展点)
	> 此扩展点功能代码来此Spring的AntPathMatcher
* com.jremoter.core.scanner.PackageScanner(包扫描扩展点)
	> 此扩展点实现了class文件搜索功能(搜索file或jar)
* com.jremoter.core.plugin.PluginManager(插件管理器扩展点)
	> 此扩展点用来管理所有的插件及插件的生命周期
* com.jremoter.core.plugin.Plugin(插件扩展点)
	> 此扩展点提供给使用者自定义插件使用,用户可实现Plugin接口或继承AbstractPlugin实现自定义插件