package utilities;

/*
 * 프로젝트 공통 값들을 Singleton으로 관리하는 클래스
 * 데이터베이스에 사용되는 값들 이외에도 사용할 수 있음
 *
 * @author      KYEONGSOO YOO
 * @Date        2018-06-04
 * @Update
 *  2019-08-26 YKS mysql-connector 8.0.17 기준, 기존 DriverClassName이 Deprecate되어 변경함.
 * */
public class CommonVariables {

    // ================================================================================
    // Properties
    // ================================================================================
    private static CommonVariables _instance = new CommonVariables();   // 실제 인스턴스
    private String jdbcDriverClassName;                                 // 디비 드라이버 명
    private String jdbcConnectionString;                                // 디비 커넥션 스트링
    private String jdbcUserAccount;                                     // 디비 사용자 명
    private String jdbcUserPassword;                                    // 디비 사용자 비밀번호

    // 디비 변수 관련 열거형
    public enum CV_DB_VARIABLE {
        DRIVER_NAME, CONNECTION_STRING, USER_ACCOUNT, USER_PASSWORD
    }

    // ================================================================================
    // Constructor
    // ================================================================================
    private CommonVariables() {
        jdbcDriverClassName = "com.mysql.cj.jdbc.Driver";

        // 본 프로젝트는 예제 프로젝트로, 실제로 사용될때에는 해당 부분이 변경되어야 함
        jdbcConnectionString = "jdbc:mysql://localhost:3306/jspTest?characterEncoding=UTF-8&serverTimezone=UTC";
        jdbcUserAccount = "root";
        jdbcUserPassword = "12345678";
    }

    // ================================================================================
    // Methods
    // ================================================================================
    /*
     * 공통 변수 클래스의 인스턴스를 가져온다. (본 클래스는 따로 Constructor를 실행하지 않음)
     *
     * @author                  KYEONGSOO YOO
     * @return  Class           현재 클래스의 인스턴스 (singleton)
     */
    public static synchronized CommonVariables getInstance() {
        return _instance;
    }

    /*
     * 데이터베이스에 관련된 항목들만 가져온다
     *
     * @param   type            가져올 항목의 열거형 코드
     * @return  String          각 코드에 맞는 private 변수들
     */
    public String getDBInfo(CV_DB_VARIABLE type) {
        String ret;
        switch (type) {
            case DRIVER_NAME:
                ret = jdbcDriverClassName;
                break;
            case CONNECTION_STRING:
                ret = jdbcConnectionString;
                break;
            case USER_ACCOUNT:
                ret = jdbcUserAccount;
                break;
            case USER_PASSWORD:
                ret = jdbcUserPassword;
                break;
            default:
                ret = "";
                break;
        }

        return ret;
    }
}
