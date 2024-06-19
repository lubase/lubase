# LuBase低代码框架 

#### 框架特性
多应用 快速开发平台 低代码开发框架 低代码框架 低代码 低代码工具 低代码开发工具

#### 框架定位
面向开发人员，针对管理软件领域，对页面交互和通用功能进行高阶封装，逐步打造成平台型、生态型开发工具。

涓涓细流 ，汇聚成海，基于PBC（组件式开发）开发理念，让功能模块的复用更简单。

让管理软件开发回归到对需求的深入思考和求解。

#### 框架简介
LuBase是以数据模型驱动，可视化表单和页面设计，内置工作流引擎以及集多应用管理功能于一体的软件快速开发工具。
后端基于SpringBoot框架，前端委托Layui团队基于Layui-vue脚手架进行开发。

#### 部分使用者
|  使用者名称 |   应用范围  |使用心得|
|---|---|-|
|  XX汽车股份有限公司 |  研发领域数字化项目建设   | 我们调研了很多款低代码工具，lubase吸引我们的是它的每个应用前后端均可以单独部署，并采用不同的数据库，打消了我们对多应用间互相影响可能存在性能问题的顾虑，同时它参考欧美工业设计软件提供了诸多的扩展点，极大方便我们对业务逻辑进行扩展。|
|天津XX金融有限公司|用于公司业务系统的建设|我们是个小众金融行业，国内这个行业业务系统主要来自国外厂家提供，产品费用及收费费用极高。lubase的表单和流程引擎及流程的扩展性，让我们3个月时间完成了平替系统的开发，给公司节省了大笔费用的同时，更大的价值是我们可以应对市场、政策的变化快速改进我们的业务系统，在这行业内快速响应。|
|XX软件有限公司|用于管理软件产品线的打造|在我们的管理软件产品线需要在技术方面更新的时候，误打误撞遇到了fastdev（lubase前身框架）框架，至此结下了善缘。通过fastdev框架及作者的帮助，我们基于一个大客户项目快速完成的新一代产品线的打造，花费的时间、费用以及质量，都远超我们的想象，十分感谢！|

#### 获取系统演示地址

扫码入群获取演示环境地址：  ![输入图片说明](https://foruda.gitee.com/images/1718806856978290173/b5dfdb1d_10523320.png "40057494_10523320.png")

#### 面向人员及定位
此工具是一款软件开发人员的工具，所以此平台本质上是低代码平台，面向业务人员使用的易用性并不是此平台的重点功能。在使用场景上，主要面向企业信息化领域的软件开发人员，是一款提升管理软件开发、功能迭代效率的工具，一直以来我们从没有想过让业务人员来通过此工具搭建系统。

#### 平台设计理念
 **1. 数据模型驱动** ：无论使用工具与否，良好的数据库设计都是快速开发软件的基础；

 **2. 无所不在的扩展点** ：工具的存在一定是提升某些环节的效率，在进行页面级别功能高阶封装时不以牺牲二次开发的灵活性为原则；平台在一个页面的全生命周期前后端多个节点均有扩展点，满足扩展需求；同时提供原生开发模式重写页面，满足个性化页面场景

 **3. 面向私有化部署** ：支持多租户多应用管理，提供私有化部署。每个应用的前后端均可以单独进行部署，并且使用不同的数据库，解决性能、互相影响的问题

 **4. 只做最擅长的部分** ：未规划报表模块、监控模块等，所以使用本工具需要和您现有的工具链做拉通

#### 平台主要功能
 **1. 多应用管理（含权限）** ：不同应用可以使用不同的数据库，并且单独部署前后端，应用有自己的一套角色管理体系，可方便地对应用进行管理和维护。如下图所示：
![输入图片说明](https://foruda.gitee.com/images/1716113367290461652/99117d04_10523320.png "屏幕截图")

 **2. 数据模型引擎** ：提供了字典表的管理、物理表创建、表关系维护等功能。

 **3. 表单设计引擎** ：基于已有的数据模型，进行表单的可视化拖拽设计。

 **4. 页面设计引擎** ：对于常见的页面板式以及常见的按钮交互进行了封装，可通过对数据源的引用快速实现列表页面的配置。

 **5. 工作流引擎** ：与表单引擎深度集成的工作流引擎满足常见的流程配置，并且通过MQ方式对外暴露了流程全生命周期的所有事件，可基于事件监听完成业务功能开发。

#### 项目代码
项目代码采用前后端分离，同时分为业务端和管理端两部分代码。管理端主要负责应用的管理和配置，业务端负应用的渲染，进行分离的主要作用是为了降低管理端的功能迭代优化对业务端应用的影响。

业务端代码包含3个仓库，地址如下：
|  代码功能 | 仓库地址  |
|---|---|
|  后端-核心代码 | https://gitee.com/lubase/lu-base.git  |
|  前端-应用端代码-Vue3版本（完善中） |  https://gitee.com/lubase/web-layui.git |
|  前端-应用端代码-Vue2版本 |  https://gitee.com/lubase/web-layui.git |

管理端代码包含2个仓库，目前属于私有仓库，根据需要进行权限开放。
|  代码功能 | 仓库地址  |
|---|---|
|  后端-管理端代码 | --  |
|  前端-管理端代码 | -- |

#### 私有化部署步骤
详见： [私有化部署步骤](https://gitee.com/lubase/lu-base/issues/I9U4LD)

#### 完整使用文档
[语雀-Lubase使用手册](https://www.yuque.com/jiaque-okoeu/zt6gq2/kclxspyo1pgydl1p)
#### 友情链接
[layui - vue](http://www.layui-vue.com/)


#### 关键词
多应用 快速开发平台 低代码开发框架 低代码框架 低代码 低代码工具 低代码开发工具