# 工程简介

# 延伸阅读
### test 项目下 tool目录用于存放代码生成等工具类文件，tool目录下的单元测试需要手工谨慎的允许
### auto.generator.entity 目录是自动生成的，请勿手动修改。手动那修改的内容可能被test项目tool目录下单元测试运行后覆盖

# DbCollection 强类型对象使用
java的泛型是编译阶段生效的，所以无法做到C#那样运行时的动态类型的效果，所以在DBCollection对象增加了两个方法
* 方法1：getGenericData(Class<?> type) 可以返回一个强类的对象列表，注意此列表与collection.getData()已经是两个对象
* 方法2：setGenericData(List<T extends DBEntity> list)，将强类型的对象列表转成标准的collection对象，用于dataaccess.update