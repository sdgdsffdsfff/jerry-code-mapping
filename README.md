jerry-code-mapping
==================

自动生成dao、manager、service、model类
使用方法：

在pom文件配置mvn的参数
<plugin>
    <groupId>com.qufaya</groupId>
    <artifactId>qufaya-maven-plugin</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <configuration> 
        <goalPrefix>qufaya</goalPrefix>
        <host>10.10.1.200</host>    
        <port>3306</port>
        <database>qufaya</database>
        <user>dev</user>
        <password>qufaya888</password>
        <basePackage>com.qufaya</basePackage>
        <overwrite>false</overwrite>
    </configuration>
</plugin>
执行mvn命令如下：
mvn qufaya:generate -Dfc.table=xxx
