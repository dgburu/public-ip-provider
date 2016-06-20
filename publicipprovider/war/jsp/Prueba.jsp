<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.logging.Logger"%>
<%!
	public static Logger logger = Logger.getLogger("Prueba.jsp");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Dgb Place Films</title>
</head>
<body>
<%-- if (request.getSession().getAttribute("login") == null) { --%>
<%-- <jsp:forward page="/index.html"/> --%>
<%--} else {--%>
<h1>Films</h1>
<nav>
	<ul style="list-style-type:none">
		<li><a style="float:left;margin-right:10px" heref="#" onclick="showSection('Pelis')">Pelis</a></li>
		<li><a style="float:left;margin-right:10px" heref="#" onclick="showSection('Vistas')">Vistas</a></li>
		<li><a style="float:left;margin-right:10px" heref="#" onclick="showSection('Series')">Series</a></li>
		<li><a style="float:left" heref="#" onclick="showSection('Dibujos')">Dibujos</a></li>
	</ul>
</nav>
<br/><hr/><br/>
<section id="section">
	<article id="Pelis">
	<%for( int i = 1; i < 4; i++) {%>
		<table>
		<tr>
		<td rowspan="2"><b><%out.println(i + ".-");%></b></td>
		<td rowspan="2"><%out.println("img");%></td>
		<td><b><%="title"%></b></td>
		</tr>
		<tr>
		<td><%="description"%></td>
		</tr>
		</table>
	<%}%>
	</article>
	<article id="Vistas" style="visibility:hidden">
	Vistas
	</article>
	<article id="Series" style="visibility:hidden">
	Series
	</article>
	<article id="Dibujos" style="visibility:hidden">
	Dibujos
	</article>
</section>
<script>
function showSection(selectedArticle) {
	var section = document.getElementById("section");
	var articles = section.getElementsByTagName("article");
	
	for (i = 0; i < articles.length; i++) {
		if (articles[i].id == selectedArticle) {
			articles[i].style.visibility="visible";
			articles[i].style.height="5em";
		} else {
			articles[i].style.visibility="hidden";
			articles[i].style.height="0";
		}
	}
}
</script>
<%--}--%>
</body>
</html>
