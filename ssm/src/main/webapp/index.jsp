<%@ page contentType="text/html;charset=utf-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Hello World!</title>
    <script src="https://cdn.bootcss.com/jquery/3.4.1/jquery.min.js"></script>
</head>
<body>

<script>
    $(function () {
        $("#box").click(function () {
            $.ajax({
                url:"http://localhost:8080/login",
                type:'post',
                data:{
                    username:"sssss",
                    password:"Ssssssssssss"
                },
                success:function (res) {
                    console.log(res);
                }
            })
        })
    })
</script>
</body>
</html>