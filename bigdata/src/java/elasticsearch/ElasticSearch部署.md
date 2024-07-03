# ElasticSearch8.14部署文档

## 修改主机名称
(选一即可)，也可不修改 a. 命令： hostnamectl set-hostname dev_70_75

b. 修改配置文件（没用过）： vim /etc/sysconfig/network

NETWORKING=yes HOSTNAME=dev_70_75

下载es需要的版本并解压 创建elastic目录 mkdir elasticsearch
wget https://artifacts.elastic.co/downloads/elasticsearch/elasticsearch-8.14.0-linux-x86_64.tar.gz
tar -zxvf elasticsearch-8.14.0-linux-x86_64.tar.gz

## 关闭Swap （
选一即可） 
### a. vim /etc/sysctl.conf

```
fs.file-max=70000
# 禁用内存与磁盘交换
vm.swappiness=1
# 设置虚拟内存大小
vm.max_map_count=655360
```

![img.png](img.png)
执行加载参数命令使其生效

```
sysctl -p 
```
（否则启动会报错：
[1] bootstrap checks failed
[1]: max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
）

### b. vim /etc/fstab

文件句柄设置 vim /etc/security/limits.conf

```
# 文件句柄数

*       soft    nofile  131072
*       hard    nofile  131072

# 进程线程数

*       soft    nproc   131072
*       hard    nproc   131072

# JVM 内存与锁定交换

*       soft    memlock unlimited
*       hard    memlock unlimited
```

创建es专用用户 ⚠️ 因为 es 用 root 用户启动会报错，所以此处需要新建一个用户，然后用新建的用户启动es。

## 创建es用户
```
#创建es用户 
useradd elastic
#设置es用户的密码 
passwd elastic
#本次的用户名密码都是 elastic
```
## 创建es数据、日志存放目录并赋权
```
mkdir data && mkdir log
```
将目录权限授权给elastic账户 
```
chown -R elastic /elastic 
```
![img_1.png](img_1.png)
## 修改配置
修改elasticsearch.yml 配置 
```
vim ${elastic}/config/elasticsearch.yml
```

```
# ======================== Elasticsearch Configuration =========================
#
# NOTE: Elasticsearch comes with reasonable defaults for most settings.
#       Before you set out to tweak and tune the configuration, make sure you
#       understand what are you trying to accomplish and the consequences.
#
# The primary way of configuring a node is via this file. This template lists
# the most important settings you may want to configure for a production cluster.
#
# Please consult the documentation for further information on configuration options:
# https://www.elastic.co/guide/en/elasticsearch/reference/index.html
#
# ---------------------------------- Cluster -----------------------------------
#
# Use a descriptive name for your cluster:
# 集群名称
#
cluster.name: es-cluster1
#
# ------------------------------------ Node ------------------------------------
#
# Use a descriptive name for the node:
# 节点名称
#
node.name: node-1
#
# Add custom attributes to the node:
#
#node.attr.rack: r1
#
# ----------------------------------- Paths ------------------------------------
#
# Path to directory where to store the data (separate multiple locations by comma):
# 数据和日志存放目录
#
path.data: /component/elasticsearch/data
#
# Path to log files:
#
path.logs: /component/elasticsearch/log
#
# ----------------------------------- Memory -----------------------------------
#
# Lock the memory on startup:
#
#bootstrap.memory_lock: true
#
# Make sure that the heap size is set to about half the memory available
# on the system and that the owner of the process is allowed to use this
# limit.
#
# Elasticsearch performs poorly when the system is swapping the memory.
#
# ---------------------------------- Network -----------------------------------
#
# By default Elasticsearch is only accessible on localhost. Set a different
# address here to expose this node on the network:
# 
#ip地址，设置0.0.0.0允许外网访问
network.bind_host: 0.0.0.0
#http.host: 0.0.0.0
#
# By default Elasticsearch listens for HTTP traffic on the first free port it
# finds starting at 9200. Set a specific HTTP port here:
# http和tcp访问端口
#
http.port: 9200
transport.port: 9300
#
# For more information, consult the network module documentation.
#
# --------------------------------- Discovery ----------------------------------
#
# Pass an initial list of hosts to perform discovery when this node is started:
# The default list of hosts is ["127.0.0.1", "[::1]"]
#
#discovery.seed_hosts: ["172.16.0.3"]
#
# Bootstrap the cluster using an initial set of master-eligible nodes:
# 集群初始主节点当
#

cluster.initial_master_nodes: ["node-1"]
#
# For more information, consult the discovery and cluster formation module documentation.
#
# ---------------------------------- Various -----------------------------------
#
# Allow wildcard deletion of indices:
#
#action.destructive_requires_name: false

#----------------------- BEGIN SECURITY AUTO CONFIGURATION -----------------------
#
# The following settings, TLS certificates, and keys have been automatically      
# generated to configure Elasticsearch security features on 28-06-2024 06:51:09
#
# --------------------------------------------------------------------------------
# 是否开启xpack认证（没有xpack需求一定要配置false不然会访问不了）
# Enable security features
xpack.security.enabled: false

xpack.security.enrollment.enabled: true

# Enable encryption for HTTP API client connections, such as Kibana, Logstash, and Agents
xpack.security.http.ssl:
  enabled: true
  keystore.path: certs/http.p12

# Enable encryption and mutual authentication between cluster nodes
xpack.security.transport.ssl:
  enabled: true
  verification_mode: certificate
  keystore.path: certs/transport.p12
  truststore.path: certs/transport.p12
#----------------------- END SECURITY AUTO CONFIGURATION -------------------------
```

直接启动es 
切换账户并后台启动
```
elastic sudo su elastic
 ./elastcsearch -d &

```
访问9200端口，表示启动成功
![img_4.png](img_4.png)
## 服务化elasticsearch
```
 vim /usr/lib/systemd/system/elasticsearch.service
```
```
[Unit]
Description=elasticsearch
Requires=network.target
After=network.target

[Service]
Type=simple
User=elastic
Group=elastic
#Environment=JAVA_HOME=/component/java/jdk1.8.0_341
ExecStart=/component/elasticsearch/elasticsearch-8.14.0/bin/elasticsearch
Restart=no

# Specifies the maximum file descriptor number that can be opened by this process
LimitNOFILE=65536

# Specifies the maximum number of processes
LimitNPROC=65536

LimitMEMLOCK=infinity
# Specifies the maximum size of virtual memory
LimitAS=infinity

# Specifies the maximum file size
LimitFSIZE=infinity

# Disable timeout logic and wait until process is stopped
TimeoutStopSec=0

# SIGTERM signal is used to stop the Java process
KillSignal=SIGTERM

# Send the signal only to the JVM rather than its control group
KillMode=process

# Java process is never killed
SendSIGKILL=no
# When a JVM receives a SIGTERM signal it exits with code 143
SuccessExitStatus=143
[Install]
WantedBy=multi-user.target
Alias=elasticsearch.service

```
![img_2.png](img_2.png)

服务化 启动 | 停止 | 重启 | 状态
```
systemctl start elasticsearch.service 
systemctl stop elasticsearch.service 
systemctl restart elasticsearch.service 
systemctl status elasticsearch.service
```

如果不按照上面操作顺序执行，可能遇到的问题： 
1、创建的账户权限问题 
2、修改完sysctl.conf未加载 
3、未指定集群初始master节点

# Kibana安装
## 部署
安装kibana 创建kibana以及log目录 
```
mkdir kibaba
cd /kibaba mkdir log 
```
下载并解压kibana
```
wget https://artifacts.elastic.co/downloads/kibana/kibana-8.14.0-linux-x86_64.tar.gz
tar -zxvf kibana-8.14.0-linux-x86_64.tar.gz
```
修改kibana.yml
```
server.port: 5601
server.host: 0.0.0.0
elasticsearch.hosts: ["http://localhost:9200"]
```
赋权给elastic用户 将目录权限授权给elastic账户 
```
chown -R elastic /kibana 
```
启动kibana 
```
./kibana-7.10.2-linux-x86_64/bin/kibana &
```
## 服务化Kibana
创建kibana.service文件
```
vim /usr/lib/systemd/system/kibana.service
```
```
[Unit]
Description=kibana
Requires=network.target
After=network.target

[Service]
Type=simple
User=elastic
Group=elastic
#Environment=JAVA_HOME=/zorkdata/zkce/service/java
ExecStart=/component/elasticsearch/kibana-8.14.0/bin/kibana
Restart=no

# Specifies the maximum file descriptor number that can be opened by this process
LimitNOFILE=65536

# Specifies the maximum number of processes
LimitNPROC=65536

LimitMEMLOCK=infinity
# Specifies the maximum size of virtual memory
LimitAS=infinity

# Specifies the maximum file size
LimitFSIZE=infinity

# Disable timeout logic and wait until process is stopped
TimeoutStopSec=0

# SIGTERM signal is used to stop the Java process
KillSignal=SIGTERM

# Send the signal only to the JVM rather than its control group
KillMode=process

# Java process is never killed
SendSIGKILL=no
# When a JVM receives a SIGTERM signal it exits with code 143
SuccessExitStatus=143
[Install]
WantedBy=multi-user.target
Alias=kibana.service
```
![img_3.png](img_3.png)

服务化 启动 | 停止 | 重启 | 状态
```
systemctl start kibana.service 
systemctl stop kibana.service 
systemctl restart kibana.service 
systemctl status kibana.service
```
访问5601端口，表示启动成功
![img_5.png](img_5.png)