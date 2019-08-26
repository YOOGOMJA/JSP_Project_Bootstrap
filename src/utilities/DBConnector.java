package utilities;

import java.sql.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/*
 * 데이터베이스와의 연결을 위한 클래스
 *
 * @author          KYEONGSOO YOO
 * @Date            2018-06-05
 * @Update
 *  2019-08-26 YKS connect와 몇몇 에러에서 추가적인 로그를 출력하도록함
 * */
public class DBConnector {
    // ================================================================================
    // Properties
    // ================================================================================
    private CommonVariables _cv;

    // ================================================================================
    // Constructor
    // ================================================================================
    public DBConnector() {
        super();
        _cv = CommonVariables.getInstance();
    }

    // ================================================================================
    // private Methods
    // ================================================================================
    private Connection connect() throws ClassNotFoundException, SQLException {
        Class.forName(_cv.getDBInfo(CommonVariables.CV_DB_VARIABLE.DRIVER_NAME));
        String sConn = _cv.getDBInfo(CommonVariables.CV_DB_VARIABLE.CONNECTION_STRING);
        String sAcc = _cv.getDBInfo(CommonVariables.CV_DB_VARIABLE.USER_ACCOUNT);
        String sPw = _cv.getDBInfo(CommonVariables.CV_DB_VARIABLE.USER_PASSWORD);
        Connection conn = DriverManager.getConnection(sConn, sAcc, sPw);

        System.out.println("[DBConnector] 성공적으로 데이터베이스 서버에 연결되었습니다.");
        return conn;
    }

    // ================================================================================
    // public Methods
    // ================================================================================

    /*
     * 디비에 인서트 / 업데이트를 진행할때 사용할 함수.
     * 쿼리 자체를 트랜잭션을 짤 수 없어서 실제 문제 없이 처리되었을때만 commit되도록 처리하였다.
     *
     * @author              KYEONGSOO YOO
     * @param   query       업데이트에 사용할 쿼리
     * @return  JSONObject  affectedRow라는 변수를 담고있다. 실제 영향을 미친 row 수 이다.
     * */
    public JSONObject update(String query) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        int affectedRowCnt = 0;
        JSONObject ret = new JSONObject();
        try {
            conn = connect();
            conn.setAutoCommit(false);
            stmt = conn.createStatement();
            affectedRowCnt = stmt.executeUpdate(query);

            conn.commit();
        } catch (SQLException e) {
            System.out.println("[DBConnector] update 함수를 실행하는 동안 오류가 발생했습니다.");
            System.out.println("[DBConnector] localized Error Message : " + e.getLocalizedMessage());
            conn.rollback();
        } finally {
            if (stmt != null)
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            ret.put("affectedRow", affectedRowCnt);
        }

        return ret;
    }

    /*
     * 데이터베이스 조회용 함수
     *
     * @author              KYEONGSOO YOO
     * @param   query       조회에 사용할 함수, insert/update문을 이곳에서 사용하지 말것.
     * @return  JSONArray   조회된 내용들
     * */
    public JSONArray excute (String query) throws ClassNotFoundException, SQLException {
        System.out.println("ex0-start");
        System.out.println(query);
        System.out.println("ex0-end");
        Connection conn = connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        JSONArray result = new JSONArray();

        int colCnt = rs.getMetaData().getColumnCount();
        while (rs.next()) {
            JSONObject item = new JSONObject();
            for (int i = 1; i < colCnt; i++) {
                item.put(rs.getMetaData().getColumnName(i), rs.getString(i));

            }
            result.add(item);
        }

        if (stmt != null)
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if (conn != null)
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        if (rs != null)
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        return result;
    }
}
