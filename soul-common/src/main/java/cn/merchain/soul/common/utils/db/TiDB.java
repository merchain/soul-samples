package cn.merchain.soul.common.utils.db;

import cn.merchain.soul.common.metadata.PageTable;
import com.alibaba.fastjson.JSONArray;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TiDB {
    private static Logger log = Logger.getLogger(TiDB.class.getName());

    static TiDB _tDB = null;

    private DBUtil dbUtil;

    public DBUtil getDbUtil() {
        return dbUtil;
    }

    public void setDbUtil(DBUtil dbUtil) {
        this.dbUtil = dbUtil;
    }

    // 单例
    public static TiDB getTiDB(DBUtil dbUtil) {
        if (_tDB == null) {
            _tDB = new TiDB();
            _tDB.setDbUtil(dbUtil);
        }
        return _tDB;
    }

    public Boolean executeSql(String aSql, List<String> aErrors) {
        Boolean aFlag = false;
        try {
            aFlag = dbUtil.Execute(aSql);
        } catch (Exception er) {
            if (aErrors != null) {
                aErrors.add(aSql);
                aErrors.add(er.getMessage());
            }
            log.info(er.getMessage());
        }
        return aFlag;
    }

    public Boolean executeSql(String aSql, Map<String, String> aPs,
                              List<String> aErrors) {
        String sql = getSql(aSql, aPs, aErrors);
        return executeSql(sql, aErrors);
    }

    /**
     * @param aSql
     * @param aPs     参数
     * @param aErrors
     * @return String
     * @Title: getSql
     * @Description: 替换sql参数
     */
    public String getSql(String aSql, Map<String, String> aPs,
                         List<String> aErrors) {
        try {
            for (String aKey : aPs.keySet()) {
                String aOld = "{" + aKey + "}";
                // String aNew = aPs.get(aKey);//可能是Int类型
                String aNew = String.valueOf(aPs.get(aKey));
                aSql = aSql.replace(aOld, aNew);
            }
        } catch (Exception er) {
            aErrors.add(er.getMessage());
            log.info(er.getMessage());
        }
        log.info("sql: " + aSql);
        return aSql;
    }

    public JSONArray getTable(String aSql, Map<String, String> aPs,
                              List<String> aErrors) {
        // 获得真正sql语句
        String sql = aPs.size() > 0 ? getSql(aSql, aPs, aErrors) : aSql;
        return getTableInfo(sql, aErrors);
    }

    public ResultSet getResultSet(String aSql, Map<String, String> aPs,
                                  List<String> aErrors) {
        // 获得真正sql语句
        String sql = aPs.size() > 0 ? getSql(aSql, aPs, aErrors) : aSql;
        ResultSet rs = null;
        try {
            rs = dbUtil.Select(sql);
        } catch (Exception er) {
            if (aErrors != null) {
                aErrors.add(aSql);
                aErrors.add(er.getMessage());
            }
            log.info(er.getMessage());
        }
        return rs;
    }

    public int getResult(String aSql, Map<String, String> aPs,
                         List<String> aErrors) {
        String sql = aSql;
        // 获得真正sql语句
        if (aPs != null && aPs.size() > 0)
            sql = getSql(aSql, aPs, aErrors);
        int count = 0;
        try {
            ResultSet rs = dbUtil.Select(sql);
            if (rs.next())
                count = rs.getInt(1);
        } catch (Exception er) {
            if (aErrors != null) {
                aErrors.add(aSql);
                aErrors.add(er.getMessage());
            }
            log.info(er.getMessage());
        }
        return count;
    }

    public String getResultByParams(String aSql, Map<String, String> aPs, int num,
                                    List<String> aErrors) {
        String sql = aSql;
        if (aPs != null && aPs.size() > 0)
            sql = getSql(aSql, aPs, aErrors);
        String res = "";
        try {
            ResultSet rs = dbUtil.Select(sql);
            if (rs.next()) {
                for (int i = 1; i <= num; i++) {
                    res += rs.getString(i);
                    if (i < num)
                        res += ",";
                }
            }
        } catch (Exception er) {
            if (aErrors != null) {
                aErrors.add(aSql);
                aErrors.add(er.getMessage());
            }
            log.info(er.getMessage());
        }
        return res;
    }

    public JSONArray getTableInfo(String aSql, List<String> aErrors) {
        JSONArray aDBJson = null;
        try {
            ResultSet rs = dbUtil.Select(aSql);
            aDBJson = JsonDBUtil.rSToJson(rs);
        } catch (Exception er) {
            if (aErrors != null) {
                aErrors.add(aSql);
                aErrors.add(er.getMessage());
            }
            log.info(er.getMessage());
        }
        return aDBJson;
    }

    public PageTable getPageTable(String aSql, String aOrderFields,
                                  String aPageSize, String aPageIndex, Map<String, String> aPs,
                                  List<String> aErrors) {
        PageTable aRes = new PageTable();
        ResultSet rs = null;
        // 获得真正sql语句
        aSql = getSql(aSql, aPs, aErrors);
        try {
            aRes.PageSize = Integer.valueOf(aPageSize);
            aRes.OrderFields = aOrderFields;
            String aRowCountSql = "Select COUNT(*) as F_RowCount from ( "
                    + aSql + " ) a";
            log.info("aRowCountSql: " + aRowCountSql);
            try {
                rs = dbUtil.Select(aRowCountSql);
                rs.next();
                aRes.RowCount = rs.getInt("F_RowCount");
            } catch (Exception e) {
                aErrors.add(e.getMessage());
            }
            if (aRes.RowCount == 0) {

            } else {
                aRes.PageCount = (int) Math.ceil(aRes.RowCount * 1.0
                        / aRes.PageSize);
                aRes.PageIndex = Integer.valueOf(aPageIndex);
                aRes.PageIndex = aRes.PageIndex > aRes.PageCount ? aRes.PageCount
                        : aRes.PageIndex;
                aRes.PageIndex = aRes.PageIndex < 1 ? 1 : aRes.PageIndex;
                String aDataSql = "Select * from (	Select ROW_NUMBER() over (order By [OrderFields]) as F_RowNumber , a.* from ( [SrcSql]) a) s where F_RowNumber<= [PageIndex] * [PageSize] and F_RowNumber> ([PageIndex]-1)*[PageSize]";
                aDataSql = aDataSql.replace("[SrcSql]", aSql);
                aDataSql = aDataSql.replace("[OrderFields]", aOrderFields);
                aDataSql = aDataSql.replace("[PageSize]",
                        String.valueOf(aRes.PageSize));
                aDataSql = aDataSql.replace("[PageIndex]",
                        String.valueOf(aRes.PageIndex));
                log.info("pageTableSql: " + aDataSql);
                rs = dbUtil.Select(aDataSql);
                aRes.Datajson = JsonDBUtil.rSetToJson(rs);
            }

        } catch (Exception er) {
            if (aErrors != null) {
                aErrors.add(aSql);
                aErrors.add(er.getMessage());
            }
            log.info(er.getMessage());
        }
        return aRes;
    }

    /* 重载函数 */
    public PageTable getPageTable(String aSql, String orderFields,
                                  int pageSize, int pageIndex, Map<String, String> aPs,
                                  List<String> aErrors) {
        return getPageTable(aSql, orderFields, String.valueOf(pageSize),
                String.valueOf(pageIndex), aPs, aErrors);
    }

    public Map<String, Object> getDataSet(Map<String, String> aTables,
                                          Map<String, String> aPs, List<String> aErrors) {
        Map<String, Object> maps = new HashMap<String, Object>();
        try {
            for (String aTable : aTables.keySet()) {
                JSONArray aJson = getTable(aTables.get(aTable), aPs, aErrors);
                maps.put(aTable, aJson);
            }
        } catch (Exception er) {
            log.info(er.getMessage());
            aErrors.add(er.getMessage());
        }
        return maps;
    }

}
