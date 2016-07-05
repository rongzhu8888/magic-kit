package pers.zr.opensource.magic.kit.hbase;

/**
 * HBase表结构元数据
 * Created by zhurong on 2016/1/28.
 */
public class HBaseTableMetaData {

    private String tableName; //表名

    private String[] columnFamilies; //列簇

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumnFamilies() {
        return columnFamilies;
    }

    public void setColumnFamilies(String[] columnFamilies) {
        this.columnFamilies = columnFamilies;
    }
}
