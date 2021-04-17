# 博物馆导览 Android App

## 项目信息
- 博物馆项目的Android 部分
- minSDK为Android 6.0
- 啥库都还没引入，除了自带的Material Design
- HMS部分需要一个Android Studio插件： **HMS toolkit**

## 项目目录结构
- gradle 项目gradle
- app Android项目源码
- SQL 本地缓存数据库的SQL语句
- 如果还有的话再加，记得在README里标注用途

## java目录结构
- BackendServices 后端服务
    - Entity 实体类包，用于直接从数据库或JSON解析，通过Service层将Entity中的类型转换为VO中的类型，并向前台提供VO中的对象。
    - Service 服务类包，提供数据，前台通过调用Service层提供的方法存取数据，具体包括博物馆信息、藏品信息、用户信息、评论信息、收藏夹等。
        - 
    - Util 工具类包，提供进行JSON解析，网络请求解析，数据库存取等工具方法，与具体业务实现无关。
    - VO 映射类包，主要包括博物馆类、藏品类等互相关联而无法直接在数据库或后台中存取的数据，需通过Service层转换而得。
        - Status 枚举类型包，提供各种类型
    
- FrontendServices 前端服务
    - Service 服务类包，前端数据解析
    - Util 工具类包，在后台工具类包基础之上的拓展
    - View 界面包，包含APP的界面文件
        - Activity 活动包，包含APP中的Activity
        - Fragment　碎片包，包含APP中的Fragment
        
## 测试
测试类包的结构与源码目录结构相似，注意保留功能的测试代码

