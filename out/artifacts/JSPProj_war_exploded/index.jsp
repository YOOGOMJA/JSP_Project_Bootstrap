<%--
  Created by IntelliJ IDEA.
  User: kyeongsoo-yoo
  Date: 26/08/2019
  Time: 9:29 오후
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <meta http-equiv="x-ua-compatible" content="ie=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1" />

  <title>JSP INDEX</title>

  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
  <script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
</head>

<body>


  <div id="testApp" class="container">
    <h1>Hello World</h1>
    <table class="table">
      <thead>
        <tr>
          <th>seq</th>
          <th>name</th>
          <th>email</th>
          <th>hp</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in list">
          <td>{{ item.idUser }}</td>
          <td>{{ item.name }}</td>
          <td>{{ item.email }}</td>
          <td>{{ item.hp }}</td>
        </tr>
      </tbody>
    </table>
  </div>

  <script type="text/javascript">
    let app = new Vue({
      el : '#testApp',
      data : {
        list : [
          { idUser : 1 , name : "아무개" , email : "abc@abc.com" , hp : "010-1234-1234" },
          { idUser : 2 , name : "아무개" , email : "abc@abc.com" , hp : "010-1234-1234" },
          { idUser : 3 , name : "아무개" , email : "abc@abc.com" , hp : "010-1234-1234" },
          { idUser : 4 , name : "아무개" , email : "abc@abc.com" , hp : "010-1234-1234" }
        ]
      },
      created(){

        fetch('http://localhost:8080/API/test?fn=mockup_select_allUsers')
        .then(res =>{
          if(res.ok){
            return res.json();
          }
          throw new Error('네트워크 응답이 올바르지 않습니다.');
        })
        .then(data=>{
          console.log(data);
          this.list = data.RESULT_DATA.METHOD_RESULT_DATA;
        })
        .catch(err=>{
          console.log(err);
        });
      }
    });
  </script>
</body>
</html>