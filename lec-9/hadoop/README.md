Password: cloud
The whole process will take about 10 minutes,
Java is alreay installed on your node, so you can skip step one. 

1. Update source list and install java, install vim 
```
$ sudo apt-get update
$ sudo apt-get install default-jdk
$ java -version
$ sudo apt-get install vim
```

2. Adding Hadoop user
```
$ sudo adduser timwoo0 sudo
```

3. Switch to hadoop user, and genrate ssh key for log in without password
```
$ su timwoo0
$ ssh-keygen -t rsa -P ""
$ cat $HOME/.ssh/id_rsa.pub >> $HOME/.ssh/authorized_keys
$ ssh localhost
```

4. Intsall Hadoop 
```
$ sudo wget http://mirrors.sonic.net/apache/hadoop/common/hadoop-2.6.0/hadoop-2.6.0.tar.gz
$ sudo tar -zxvf ./hadoop-2.6.0.tar.gz -C /usr/local
$ sudo mv ./hadoop-2.6.0/ ./hadoop 
$ sudo chown -R timwoo0 ./hadoop 
```

5. Setup Configuration for Hadoop
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
#HADOOP VARIABLES END


$ source ~/.bashrc

$ readlink -f /usr/bin/javac

$ export PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/usr/games:/usr/local/games:/usr/local/hadoop/sbin:/usr/local/hadoop/bin:/usr/local/hadoop/sbin:/usr/local/hadoop/bin
```

5.2. all hadoop config files are in Install_dir/etc/hadoop
```
$ cd Install_dir/etc/hadoop
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


5.5 Execute 
```
$ cd /usr/local/hadoop
$ ./bin/hdfs namenode -format
```

all scripts are located in sbin folder

  * using `$start-all.sh` to start 
  * using `$ jps` to test 
  * using `$ stop-all.sh` to stop

```
$ ./sbin/start-dfs.sh
$ jps
$ ./sbin/stop-dfs.sh
```

5.6 Example: WordCount

```
Using jar file: 

$ ./bin/hdfs dfs -mkdir -p /user/hadoop
$ ./bin/hdfs dfs -mkdir /input
$ ./bin/hdfs dfs -put etc/hadoop/*.xml /input 
$ ./bin/hadoop jar share/hadoop/mapreduce/hadoop-mapreduce-examples-2.6.0.jar grep input output 'dfs[a-z.]+'
$ ./bin/hdfs dfs -cat output/*

Compile from source:

$ cd ur_dir
$ vim WordCount.java   (Sample file: https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html)
$ echo $JAVA_HOME
$ export PATH=${JAVA_HOME}/bin:${PATH}
$ export HADOOP_CLASSPATH=${JAVA_HOME}/lib/tools.jar
$ /usr/local/hadoop/bin/hadoop com.sun.tools.javac.Main WordCount.java
$ jar cf wc.jar WordCount*.class
$ /usr/local/hadoop/bin/hdfs dfs -mkdir /input
$ /usr/local/hadoop/bin/hdfs dfs -put etc/hadoop/*.xml /input 
$ /usr/local/hadoop/bin/hadoop jar wc.jar WordCount /input output-0
$ /usr/local/hadoop/bin/hadoop fs -cat output-0/part-r-00000
```
