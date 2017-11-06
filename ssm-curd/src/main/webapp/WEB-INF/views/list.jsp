<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
	pageContext.setAttribute("LOCAL_PATH", request.getContextPath());
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工列表</title>
<script type="text/javascript" src="${LOCAL_PATH }/static/js/jquery-1.12.4.min.js"></script>
<link href="${LOCAL_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="${LOCAL_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>
	<!-- 搭建页面 -->
	<div class="container">
		<!-- 标题显示 -->
		<div class="row">
			<div class="col-md-">
				<h1>SSM_CURD</h1>
			</div>
		</div>
		<!-- 删除、新增按钮 -->
		<div class="row">
			<div class="col-md-4 col-md-offset-8">
				<button class="btn btn-primary">新增</button>
				<button class="btn btn-danger">删除</button>
			</div>
		</div>
		<!-- 数据列 -->
		<div class="row">
			<div class="col-md-12">
				<table class="table table-striped" style="margin-top:5px;">
					<tr>
						<th>#</th>
						<th>empName</th>
						<th>gender</th>
						<th>email</th>
						<th>deptName</th>
						<th>操作</th>
					</tr>
					<c:forEach items="${pageInfo.list }" var="emp">
						<tr>
							<th>${emp.empId }</th>
							<th>${emp.empName }</th>
							<th>${emp.gender =="M"?"男":"女" }</th>
							<th>${email }</th>
							<th>${emp.department.deptName }</th>
							<th>
								<button class="btn btn-primary btn-sm">
									<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>编辑
								</button>
								<button class="btn btn-danger btn-sm">
									<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>删除
								</button>
							</th>
						</tr>
					</c:forEach>
				</table>
			</div>
		</div>
		<!-- 分页信息 -->
		<div class="row">
			<!-- 分页文字信息 -->
			<div class="col-md-6">
				当前${pageInfo.pageNum }页，总${pageInfo.pages }页，总${pageInfo.total }条记录
			</div>
			
			<!-- 分页条 -->
			<div class="col-md-6">
				<nav aria-label="Page navigation">
				  <ul class="pagination">
				    <li><a href="${LOCAL_PATH }/emps?pn=1">首页</a></li>
				    <c:if test="${pageInfo.hasPreviousPage }">
					    <li>
					      <a href="${LOCAL_PATH }/emps?pn=${pageInfo.pageNum-1 }" aria-label="Previous">
					        <span aria-hidden="true">&laquo;</span>
					      </a>
					    </li>
				    </c:if>
				    <c:forEach items="${pageInfo.navigatepageNums }" var="page_Num">
				    	<c:if test="${page_Num ==pageInfo.pageNum }">
				    		<li class="active"><a href="#">${page_Num }</a></li>
				    	</c:if>
					    <c:if test="${page_Num !=pageInfo.pageNum }">
				    		<li><a href="${LOCAL_PATH }/emps?pn=${page_Num }">${page_Num }</a></li>
				    	</c:if>
				    </c:forEach>
				    <c:if test="${pageInfo.hasNextPage }">
					    <li>
					      <a href="${LOCAL_PATH }/emps?pn=${pageInfo.pageNum+1 }" aria-label="Next">
					        <span aria-hidden="true">&raquo;</span>
					      </a>
					    </li>
				    </c:if>
				    <li><a href="${LOCAL_PATH }/emps?pn=${pageInfo.pages }">末页</a></li>
				  </ul>
				</nav>
			</div>
		</div>
	</div>
</body>
</html>