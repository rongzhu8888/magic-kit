package pers.zr.opensource.magic.kit.hbase;

import com.alibaba.fastjson.JSON;
import lombok.NonNull;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * HBase Util<br>
 * 适用于存储符合以下条件的数据：<br>
 * 1、随着时间的推移爆炸性增长（如几十亿条记录，Mysql必然要考虑分库分表和归档等问题，而HBase只需通过添加存储设备可以做到平滑扩容）<br>
 * 2、数据通常只按主键进行查询并且要求历史数据能够索引<br>
 * 比如搜索引擎的每条结果都存在HBase里，搜索引擎根据关键字查找索引列表，然后对每个索引从HBase里拉取明细信息
 * <br>
 * ***注意***<br>
 * Windows环境下测试必须将plugins目录下的hadoop-common-2.2.0-bin-master.zip解压到本地并且设置环境变量HADOOP_HOME
 *
 */
public class HBaseClient {

    private final String ZOOKEEPER_CLUSTER_HOST = "hbase.zookeeper.quorum";
    private final String ZOOKEEPER_CLUSTER_PORT = "hbase.zookeeper.property.clientPort";
    private final String ZOOKEEPER_ZNODE_PARENT = "zookeeper.znode.parent";

    private static HBaseClient client = new HBaseClient();

    private final Logger log = LoggerFactory.getLogger(getClass());

    private HBaseClient() {
        //Load HBase config and initialize HConnection & HBaseAdmin
        try{
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("hbase.properties");
            Properties properties = new Properties();
            properties.load(is);

            configure = new HBaseConfigure();
            configure.setZookeeperQuorum(properties.getProperty(ZOOKEEPER_CLUSTER_HOST));
            configure.setZookeeperClientPort(properties.getProperty(ZOOKEEPER_CLUSTER_PORT));
            configure.setZookeeperZnodeParent(properties.getProperty(ZOOKEEPER_ZNODE_PARENT));

            Configuration config = HBaseConfiguration.create();
            config.set(ZOOKEEPER_CLUSTER_HOST, configure.getZookeeperQuorum());
            config.set(ZOOKEEPER_CLUSTER_PORT, configure.getZookeeperClientPort());
            config.set(ZOOKEEPER_ZNODE_PARENT, configure.getZookeeperZnodeParent());

            conn = HConnectionManager.createConnection(config);
            admin = new HBaseAdmin(config);
        } catch (IOException e) {
            log.error("Failed to create HBase client", e);
        }

    }

    private HConnection conn = null;

    private HBaseAdmin admin = null;

    private HBaseConfigure configure = null;


    public static HBaseClient getInstance() {
        return client;
    }

    public HBaseConfigure getConfigure() {
        return this.configure;
    }

    public void createTable(HBaseTableMetaData tableMetaData) throws Exception {
        if(!admin.tableExists(tableMetaData.getTableName())) {
            if(log.isInfoEnabled()) {
                log.info("create hbase table [" + tableMetaData.getTableName() + "]>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            }
            HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableMetaData.getTableName()));
            for (String cf : tableMetaData.getColumnFamilies()) {
                tableDescriptor.addFamily(new HColumnDescriptor(cf));
            }
            admin.createTable(tableDescriptor);

        }

    }

    /**
     * 插入/修改HBase记录
     * @param tableMetaData 表结构元数据
     * @param rowKey 行主键
     * @param data 数据
     * @throws Exception
     */
    public void setData(@NonNull HBaseTableMetaData tableMetaData, @NonNull String rowKey,
                                  @NonNull Map data) throws Exception {
        long startTime = System.currentTimeMillis();
        HTableInterface hTable = null;
        try{

            //写入数据
            hTable = conn.getTable(tableMetaData.getTableName());
            Put put = new Put(rowKey.getBytes());
            for(String family : tableMetaData.getColumnFamilies()) {
                put.add(family.getBytes(), null, String.valueOf(data.get(family)).getBytes());
            }
            hTable.put(put);
        }finally {
            if(hTable != null) {
                hTable.close();
            }
            long endTime = System.currentTimeMillis();
            if(log.isInfoEnabled()) {
                log.info("HBase data insert costs[" + (endTime-startTime) + "ms], " +
                        "table=[" + JSON.toJSONString(tableMetaData) + "], " +
                        "rowKey=[" + rowKey + "], " +
                        "data=" + JSON.toJSONString(data));
            }

        }

    }

    /**
     * 根据rowKey读取HBase记录
     * @param tableMetaData 表结构元数据
     * @param rowKey 行主键
     * @return HBase记录
     * @throws Exception
     */
    public Map<String, String> getData(@NonNull HBaseTableMetaData tableMetaData,
                                              @NonNull String rowKey) throws Exception {
        long startTime = System.currentTimeMillis();
        HTableInterface hTable = null;
        Map<String, String> valueMap = null;
        try{
            hTable = conn.getTable(tableMetaData.getTableName());
            Get get = new Get(rowKey.getBytes());
            Result result = hTable.get(get);
            if(result != null) {
                valueMap = new HashMap<>();
                for (KeyValue kv : result.raw()) {
                    valueMap.put(new String(kv.getFamily()), new String(kv.getValue()));
                }
            }
            return valueMap;
        }finally {
            if(hTable != null) {
                hTable.close();
            }
            long endTime = System.currentTimeMillis();
            if(log.isInfoEnabled()) {
                log.info("HBase data query costs[" + (endTime-startTime) + "ms], " +
                        "table=[" + JSON.toJSONString(tableMetaData) + "], " +
                        "rowKey=[" + rowKey + "], " +
                        "data=" + JSON.toJSONString(valueMap));
            }

        }


    }

    public List<Map<String, String>> getListByColumn(@NonNull HBaseTableMetaData tableMetaData,
                                                            @NonNull String columnName, @NonNull String columnValue) throws Exception {
        List<Map<String, String>> list = new ArrayList<>();
        HTableInterface hTable = null;
        ResultScanner resultScanner = null;
        try{
            hTable = conn.getTable(tableMetaData.getTableName());
            Filter filter = new SingleColumnValueFilter(
                    columnName.getBytes(), null, CompareFilter.CompareOp.EQUAL, columnValue.getBytes()
            );
            Scan scan = new Scan();
            scan.setFilter(filter);
            resultScanner = hTable.getScanner(scan);
            if(resultScanner != null) {
                for(Result result : resultScanner) {
                    if(result != null) {
                        Map<String, String> valueMap = new HashMap<>();
                        for (KeyValue kv : result.raw()) {
                            valueMap.put(new String(kv.getFamily()), new String(kv.getValue()));
                        }
                        list.add(valueMap);
                    }
                }
            }

        }finally {
            if(resultScanner != null) {
                resultScanner.close();
            }
            if(hTable != null) {
                hTable.close();
            }
        }
        return list;


    }

}
