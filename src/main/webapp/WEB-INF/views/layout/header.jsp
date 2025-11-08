<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="loginStatus" value="${empty sessionScope.userNum ? 'login' : 'logout'}" />
<header class="header">
	<h1>JAPUB</h1>
	<nav>
		<ul>
			<c:if test="${empty sessionScope.userNum}">
			<li>
				<a href='<c:url value="/join" />'>회원가입</a>
			</li>
			</c:if>	
			<li><a href='<c:url value="/${loginStatus}" />'>${loginStatus}</a>
				<ul class="submenu">
				<c:if test="${not empty sessionScope.userNum}">
					<li><a href="<c:url value='/mypage/check-password' />">마이페이지</a></li>
					<li><a href="<c:url value='/mypage/check-password?isDelete=true' />">회원탈퇴</a></li>
				</c:if>
					<li><a href="#">menu1-2</a></li>
				</ul>
			</li>

			<li><a href="<c:url value='/item/list' />">상품목록</a>
				<ul class="submenu">
					<li><a href="#">menu2-1</a></li>
					<li><a href="#">menu2-2</a></li>
					<li><a href="#">menu2-3</a></li>
					<li><a href="#">menu2-4</a></li>
				</ul></li>

			<li><a href="#">게시판</a>
				<ul class="submenu">
					<li><a href="<c:url value='/board/list?category=free' />">자유게시판</a></li>
					<li><a href="<c:url value='/board/list?category=notice' />">공지사항</a></li>
					<li><a href="<c:url value='/board/list?category=media' />">미디어 리뷰</a></li>
					<li><a href="<c:url value='/board/list?category=download' />">자료실</a></li>
				</ul></li>

			<li><a href="<c:url value='/item/register' />">상품등록</a>
				<ul class="submenu">
					<li><a href="#"></a></li>
					<li><a href="#">menu4-2</a></li>
				</ul></li>
		</ul>
	</nav>
</header>