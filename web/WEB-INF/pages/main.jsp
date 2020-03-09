<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!-- Main landing page after a login, contains links to other parts of the site -->
<%@ page contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

<section class="clean-block about-us" style="background-color: rgb(35, 35, 35); height:90%; background-image: url(${pageContext.request.contextPath}/resources/images/music-notes-wallpaper-16213-16712-hd-wallpapers.jpg)">
 <h1>Hello World</h1>
 <h1 class="warning_label">${message}</h1>

 <h2>
  <a href="/login/portal" class="text-warning">Login</a>
 </h2>
 <h2>
  <a href="/registration/portal" class="text-warning">Register</a>
 </h2>
</section>
