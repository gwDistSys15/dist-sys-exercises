# Hadoop Cluster Installation


1.Log onto one of the machines based on your group number:
```
## Be sure to get the dist_sys SSH key from webpage first
##  https://gist.github.com/twood02/e8382cde6803b7e00949
node-0  pcvm601-12      pcvm    ssh -p 30778 timwoo0@pc601.emulab.net   
node-1  pcvm601-13      pcvm    ssh -p 30779 timwoo0@pc601.emulab.net   
node-2  pcvm601-14      pcvm    ssh -p 30780 timwoo0@pc601.emulab.net   
node-3  pcvm601-15      pcvm    ssh -p 30781 timwoo0@pc601.emulab.net   
node-4  pcvm601-16      pcvm    ssh -p 30782 timwoo0@pc601.emulab.net   
node-5  pcvm601-17      pcvm    ssh -p 30783 timwoo0@pc601.emulab.net   
node-6  pcvm601-18      pcvm    ssh -p 30784 timwoo0@pc601.emulab.net   
node-7  pcvm602-5       pcvm    ssh -p 30778 timwoo0@pc602.emulab.net   
node-8  pcvm601-19      pcvm    ssh -p 30785 timwoo0@pc601.emulab.net   
node-9  pcvm602-6       pcvm    ssh -p 30779 timwoo0@pc602.emulab.net
```

2.Update source list and install java, install vim 

Java is alreay installed on your node, *so you can skip this step*. 
```
##### SKIP THIS STEP!!!!!!!!!!
$ sudo apt-get update
$ sudo apt-get install default-jdk
$ java -version
```


3.Install vim, genrate ssh key for log in without password
```
$ sudo apt-get install vim
$ ssh-keygen -t rsa -P ""
$ cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys
$ ssh localhost  # verify you don't need to enter a password
```

4.Install Hadoop 
```
$ sudo wget http://mirrors.sonic.net/apache/hadoop/common/hadoop-2.6.0/hadoop-2.6.0.tar.gz
$ sudo tar -zxvf ./hadoop-2.6.0.tar.gz -C /usr/local
$ cd /usr/local
$ sudo mv ./hadoop-2.6.0/ ./hadoop 
$ sudo chown -R timwoo0 ./hadoop 
```

5.Setup Configuration for Hadoop 


5.1 
```
$ vi ~/.bashrc  ## Add these lines:

#HADOOP VARIABLES START
export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64
export HADOOP_INSTALL=/usr/local/hadoop
export HADOOP_MAPRED_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_HOME=$HADOOP_INSTALL
export HADOOP_HDFS_HOME=$HADOOP_INSTALL
export YARN_HOME=$HADOOP_INSTALL
export HADOOP_COMMON_LIB_NATIVE_DIR=$HADOOP_INSTALL/lib/native
export PATH=$PATH:$HADOOP_INSTALL/sbin
export PATH=$PATH:$HADOOP_INSTALL/bin
export PATH=$PATH::/usr/bin:/usr/sbin:/bin:/sbin
export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar

#HADOOP VARIABLES END


$ source ~/.bashrc

$ readlink -f /usr/bin/javac
```

5.2. all hadoop config files are in Install_dir/etc/hadoop
```
$ cd /usr/local/hadoop/etc/hadoop
# add "export JAVA_HOME=/usr/lib/jvm/java-7-openjdk-amd64" to "hadoop-env.sh"
```

5.3 update core-file.xml
```
$ vim /usr/local/hadoop/etc/hadoop/core-site.xml

<configuration>
    <property>
        <name>hadoop.tmp.dir</name>
        <value>file:/usr/local/hadoop/tmp</value>
        <description>Abase for other temporary directories.</description>
    </property>
    <property>
        <name>fs.defaultFS</name>
        <value>hdfs://localhost:9000</value>
    </property>
</configuration>
```

5.4 hdfs-site.xml
```
$ vim /usr/local/hadoop/etc/hadoop/hdfs-site.xml

<configuration>
    <property>
        <name>dfs.replication</name>
        <value>1</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>file:/usr/local/hadoop/tmp/dfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>file:/usr/local/hadoop/tmp/dfs/data</value>
    </property>
</configuration>
```


5.5 Clean out HDFS storage 
```
$ cd /usr/local/hadoop
$ ./bin/hdfs namenode -format
```

all scripts are located in /usr/local/hadoop/sbin folder

  * using `$start-all.sh` to start 
  * using `$ jps` to test 
  * using `$ stop-all.sh` to stop

```
$ ./sbin/start-dfs.sh
$ jps
$ ./sbin/stop-dfs.sh
```

5.6 Examples

Please remeber to format datanode first using `$  /usr/local/hadoop/bin/hdfs namenode -format`

After starting hadoop using `$ /usr/local/hadoop/sbin/start-all.sh` , then process your example. 

After finished process using `$ /usr/local/hadoop/sbin/stop-all.sh` to close hadoop. 

```
Option 1: Using jar file to run grep: 

$ ./bin/hdfs dfs -mkdir -p /user/hadoop
$ ./bin/hdfs dfs -mkdir /input
$ ./bin/hdfs dfs -put etc/hadoop/*.xml /input 
$ ./bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.0.jar grep input output 'dfs[a-z.]+'
$ ./bin/hdfs dfs -cat output/*

Option 2: Get WordCount from source:

$ cd ur_dir
$ vim WordCount.java   (Sample file: https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html)
$ echo $JAVA_HOME
$ export PATH=${JAVA_HOME}/bin:${PATH}
$ /usr/local/hadoop/bin/hadoop com.sun.tools.javac.Main WordCount.java
$ jar cf wc.jar WordCount*.class
$ /usr/local/hadoop/bin/hdfs dfs -mkdir /input
$ /usr/local/hadoop/bin/hdfs dfs -put etc/hadoop/*.xml /input 
$ /usr/local/hadoop/bin/hadoop jar wc.jar WordCount /input output-0
$ /usr/local/hadoop/bin/hadoop fs -cat output-0/part-r-00000
```
