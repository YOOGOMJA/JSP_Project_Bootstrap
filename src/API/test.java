package API;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.sql.*;
import utilities.*;
import org.json.simple.*;
import org.json.simple.parser.*;

/**
 * Servlet implementation class users
 */
@WebServlet("/API/test")
public class test extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static ReflectionMethod rm;
    private CommonVariables cv;

    private HttpSession _session = null;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public test() {
        super();
        // TODO Auto-generated constructor stub
        rm = new ReflectionMethod();
        cv = CommonVariables.getInstance();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */

    // REQUEST METHOD가 GET일때 실행되는 부분
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // 현재 사용자의 세션 정보를 불러옴
        _session = request.getSession();

        // 쿼리스트링으로 전송될 내용들을 JSON으로 파싱할 준비
        JSONParser parser = new JSONParser();
        JSONObject prms = new JSONObject();

        try {
            // 만약 params이라는 쿼리스트링 정보를 넘겨받을 경우 JSON으로 변환
            // parameter안에 params라는 키가 존재할 경우에만 실행
            if(request.getParameterMap().containsKey("params")){
                prms = (JSONObject) parser.parse(request.getParameter("params"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // 넘겨받은 정보를 기준으로 현재 서블릿 파일 안에서 함수를 찾아 실행 후 결과를 JSON으로 돌려받음
        JSONObject jsonObj = rm.callAndGetResult(request.getParameter("fn"), prms ,this);
        // 응답 정보의 캐릭터 셋을 변경
        response.setCharacterEncoding("UTF-8");
        // 응답 결과의 형태가 JSON 형태임을 알려
        response.setHeader("Content-Type" , "application/json");
        // 돌려받은 JSON값을 response에 담아 요청 전송
        response.getWriter().append(jsonObj.toJSONString());

        // POST도 동일한 과정을 거침
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    // REQUEST METHOD가 POST일때 실행되는 부분
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        _session = request.getSession();

        JSONParser parser = new JSONParser();
        JSONObject prms = new JSONObject();
        try {
            if(request.getParameterMap().containsKey("params")){
                prms = (JSONObject) parser.parse(request.getParameter("params"));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }



        JSONObject jsonObj = rm.callAndGetResult(request.getParameter("fn"), prms, this);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type" , "application/json");
        response.getWriter().append(jsonObj.toJSONString());

    }

    // ================================================================================
    // Sample
    // ================================================================================

    /*
    * 예시 함수
    * reflection이 실행되는지 확인하는데 사용될 함수
    * */
    public JSONObject mockup (JSONObject params)  {
        JSONObject ret = new JSONObject();
        ret.put("RESULT" , "Hello");
        return ret;
    }

    /*
     * 예시 함수
     * 조회를 시행해야 하는 경우.
     * 메소드 리플렉션과 데이터베이스 요청을 포함하기 때문에 예외처리 항목에
     * ClassNotFoundException / SQLException 항목이 포함되어야 한다.
     * NOTE : Params를 사용하지 않고 데이터 베이스를 사용하는 경우
     * */
    public JSONObject mockup_select_allUsers(JSONObject params){
        JSONObject ret = new JSONObject();

        String query = "select * from User";

        DBConnector dc = new DBConnector();

        JSONArray db_result = null;

        try{
            db_result = dc.excute(query);
        } catch (SQLException e) {
            e.printStackTrace();

            ret.put("METHOD_RESULT_CD" , -1);
            ret.put("METHOD_ERR_LOCALE_MSG", e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

            ret.put("METHOD_RESULT_CD", -2);
            ret.put("METHOD_ERR_LOCALE_MSG", e.getLocalizedMessage());
        }
        finally{
            // JSONArray 객체는 배열이 아니라 리스트 클래스를 상속받는다.
            // 배열처럼 사용하기 위해서는 먼저 변환해주어야 한다.
            Object[] items = db_result.toArray();

            if(items.length > 0) {
                ret.put("METHOD_RESULT_CD", 1);
                ret.put("METHOD_RESULT_DATA", db_result);
            }
            else {
                ret.put("METHOD_RESULT_CD" , 0);
                ret.put("METHOD_ERR_MSG", "데이터가 없음");
            }
        }

        return ret;
    }

    /*
     * 예시 함수
     * 조회를 시행해야 하는 경우.
     * 메소드 리플렉션과 데이터베이스 요청을 포함하기 때문에 예외처리 항목에
     * ClassNotFoundException / SQLException 항목이 포함되어야 한다.
     * NOTE : 로그인을 예제로 사용함
     * */
    public JSONObject mockup_select (JSONObject params) throws ClassNotFoundException, SQLException{
        JSONObject ret = new JSONObject();


        /*
         * JSONObject params에 아래와 같은 변수들이 들어있는 경우
         * */
        String mail = (String) params.get("userMail");
        String pw = (String) params.get("userPW");

        // 데이터 베이스 요청을 위한 문자열
        // 잦은 String append는 성능과 가독성에 좋지 않아 String.format을 사용하도록 함
        String query = String.format("select * from User where mail = '%s' and password = '%' ", mail , pw);

        // DB에 연결하기 위함 클래스를 초기화한다.
        // 본 클래스는 util 패키지에 들어있다.
        DBConnector dc = new DBConnector();

        // 셀렉트 결과를 가져온다.
        // 셀렉트시에는 반드시 execute함수를 사용해야 한다.
        // 아래 함수는 insert , update , delete시 사용하지 않도록 조심해야한다.
        // DB 연결 함수들은 항상 try catch문으로 감싸두어야 한다. 오류 검출을 위
        JSONArray db_result = null;
        try {
            db_result = dc.excute(query);
        }
        catch(SQLException e) {
            // 실제 에러가 발생한 부분을 서버 콘솔상에 출력한다.
            e.printStackTrace(System.out);

            // 실패한 결과에는 반드시 ret JSON에 아래 키들을 포함해야한다.
            /*
             * METHOD_RESULT_CD         integer             : 결과 코드이다. 실패한 경우 0보다 작은 값을 순서대로 넣는다.
             * METHOD_ERR_MSG           String              : 요구 사항에 따라 직접 메시지를 삽입.
             * METHOD_ERR_LOCALE_MSG    String              : 예외처리를 통해 오류가 발생한 경우에만 삽입한다.
             * */
            ret.put("METHOD_RESULT_CD" , -1);
            ret.put("METHOD_ERR_LOCALE_MSG", e.getLocalizedMessage());
        }

        // JSONArray 객체는 배열이 아니라 리스트 클래스를 상속받는다.
        // 배열처럼 사용하기 위해서는 먼저 변환해주어야 한다.
        Object[] items = db_result.toArray();

        if(items.length > 0) {
            // 하나 이상 값이 조회되었을 경우 1보다 갯수가 크다.
            // 로그인이 성공한 것으로 판단하고 데이터를 돌려보낸다.

            // 성공한 결과에는 반드시 ret JSON에 아래 키들을 포함해야한다.
            /*
             * METHOD_RESULT_CD     integer                 : 결과 코드이다. 성공한 경우 1을 출력한다.
             *                                              만약 실패 한다면 0보다 작은 값으로 코드를 변경한다. (ex : -1)
             * METHOD_RESULT_DATA   JSONObject/JSONArray    : 조회된 결과 데이터이다. JSONObject 혹은 JSONArray를 넣어주도록 한다.
             * */
            ret.put("METHOD_RESULT_CD", 1);
            ret.put("METHOD_RESULT_DATA", db_result);
        }
        else {
            // 로그인이 실패한 경우이다.
            // 이 경우에는 직접 메시지를 넣어주면된다. 서버 오류로 인한 예외가 아니라 사용자 입력에 따른 예외기 떄
            ret.put("METHOD_RESULT_CD" , -2);
            ret.put("METHOD_ERR_MSG", "아이디 혹은 비밀번호를 확인해주세요");
        }


        return ret;
    }

    /*
     * 예시 함수
     * 업데이트를 시행해야 하는 경우.
     * 메소드 리플렉션과 데이터베이스 요청을 포함하기 때문에 예외처리 항목에
     * ClassNotFoundException / SQLException 항목이 포함되어야 한다.
     * NOTE : 사용자 이름 변경하는 예제. 아래의 내용은 insert / update / delete에 공통으로 사용
     * */
    public JSONObject mockup_update(JSONObject params) throws ClassNotFoundException, SQLException{
        JSONObject ret = new JSONObject();
        // Db와 통신 후 확인할 객체이다.
        JSONObject dbJSON = null;

        // 사용할 값을 가져온다.
        String userName = (String) params.get("userName");
        int userSeq = (int) params.get("userSeq");

        // 항목이 null 혹은 공백문자가 아닌지 확인
        // 일부러 비워두도록 업데이트하는것이 아니라면 항상 확인해야한다.
        // insert / update / delete 항목들은 되도록 엄격하게 유효성 검사를 거쳐야 한다.
        if(userName == null || userName.isEmpty()) {
            ret.put("METHOD_RESULT_CD", -1);
            ret.put("METHOD_ERR_MSG", "이름을 입력해주세요");
        }
        else {
            // 업데이트에 필요한 쿼리문을 작성
            String query = String.format("update TB_User set userName = '%s' where userSeq = '%s'", userName , userSeq);

            // 데이터 베이스 연결용 클래스를 초기화해준다.
            DBConnector dc = new DBConnector();

            try {
                // 위와 동일하다
                // insert / update / delete를 사용할때는 아래 함수를 사용하도록 한다.
                // 아래 함수는 오류가 발생하면 다시 디비를 이전 상태로 복구한다.
                dbJSON = dc.update(query);
            }
            catch(SQLException e) {
                e.printStackTrace(System.out);
                ret.put("METHOD_RESULT_CD", -1);
                ret.put("METHOD_ERR_MSG", "통신 중 오류가 발생했습니다");
                ret.put("METHOD_ERR_LOCALE_MSG", e.getLocalizedMessage());
            }

            // 실제 결과를 담아둔다.
            // 이 데이터에는 실제로 영향을 받은 데이터들의 row count가 들어간다.
            // 한건의 업데이트만 발생하면 1만 넘어간다. 아무것도 영향받지 못하면 0이 넘어간다.
            ret.put("METHOD_RESULT_DATA", dbJSON.get("affectedRow"));
        }

        return ret;
    }

}