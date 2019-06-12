<html>
<body>
<h2>Hello World!</h2>
<%
    String staticUrl = "http://static.taotao.com";
    request.setAttribute("staticUrl", staticUrl);
%>
<h2>${staticUrl}</h2>
<script type="text/javascript">
    alert(${staticUrl});
</script>
</body>
</html>
