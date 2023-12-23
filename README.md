# dockerWebUI


#### 介绍
dockerWebUI是一款docker服务web图形化管理工具, 是一个管理docker服务器的神器.

github: https://github.com/cym1102/dockerWebUI

QQ技术交流群1: 1106758598

QQ技术交流群2: 560797506

邮箱: cym1102@qq.com

微信捐赠二维码

<img src="http://www.nginxwebui.cn/img/weixin.png"  height="200" width="200">


#### 技术说明

本项目是基于solon的java项目, 数据库使用h2, 因此服务器上不需要安装任何数据库, 同时也兼容使用mysql

使用本软件，请先安装docker


#### 安装说明

1.安装java运行环境和docker

Ubuntu:

```
apt update
apt install openjdk-11-jdk
apt install docker.io
```

Centos:

```
yum install java-11-openjdk
yum install docker
```


2.下载最新版发行包jar

```
Linux:  mkdir /home/dockerWebUI/
        wget -O /home/dockerWebUI/dockerWebUI.jar http://file.nginxwebui.cn/dockerWebUI-1.0.2.jar

```

有新版本只需要修改路径中的版本即可

3.启动程序

```
Linux: nohup java -jar -Dfile.encoding=UTF-8 /home/dockerWebUI/dockerWebUI.jar --server.port=7070 > /dev/null &

Windows: java -jar -Dfile.encoding=UTF-8 D:/home/dockerWebUI/dockerWebUI.jar --server.port=7070
```

参数说明(都是非必填)

--server.port 占用端口, 默认以7070端口启动

--project.home 项目配置文件目录，存放仓库文件, 数据库文件等, 默认为/home/dockerWebUI/

--database.type=mysql 使用其他数据库，不填为使用本地h2数据库

--database.url=jdbc:mysql://ip:port/dbname 数据库url

--database.username=root 数据库用户

--database.password=pass 数据库密码

注意命令最后加一个&号, 表示项目后台运行


#### 添加开机启动


1. 编辑service配置

```
vim /etc/systemd/system/dockerwebui.service
```

```
[Unit]
Description=DockerWebUI
After=syslog.target
After=network.target
 
[Service]
Type=simple
User=root
Group=root
WorkingDirectory=/home/dockerWebUI
ExecStart=/usr/bin/java -jar -Dfile.encoding=UTF-8 /home/dockerWebUI/dockerWebUI.jar
Restart=always
 
[Install]
WantedBy=multi-user.target
```

之后执行

```
systemctl daemon-reload
systemctl enable dockerwebui.service
systemctl start dockerwebui.service
```

#### 使用说明

打开 http://ip:7070 进入主页

![输入图片说明](http://www.nginxwebui.cn/img/docker/注册用户.png "login.jpg")

首次打开页面, 需要注册管理员账户

![输入图片说明](http://www.nginxwebui.cn/img/docker/登录界面.png "login.jpg")

注册好管理员后, 可在此页面进行登录

![输入图片说明](http://www.nginxwebui.cn/img/docker/系统状态.png "login.jpg")

系统状态页面,可查看系统负载情况

![输入图片说明](http://www.nginxwebui.cn/img/docker/镜像管理.png "login.jpg")

镜像管理页面,可查看已拉取的镜像,可搜索镜像进行拉取

![输入图片说明](http://www.nginxwebui.cn/img/docker/容器管理.png "login.jpg")

容器管理页面,可查看已创建的容器,可使用图像界面创建容器,可查看容器日志等

![输入图片说明](http://www.nginxwebui.cn/img/docker/用户管理.png "login.jpg")

用户管理,可添加删除本系统的用户账号


#### 找回密码

如果忘记了登录密码，可按如下教程找回密码

1.停止dockerWebUI

```
pkill java
```

2.使用找回密码参数运行dockerWebUI.jar

```
java -jar dockerWebUI.jar --project.home=/home/dockerWebUI/ --project.findPass=true
```

--project.home 为项目文件所在目录

--project.findPass 为是否打印用户名密码

运行成功后即可打印出全部用户名密码