<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="/tags" prefix="date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>小红花</title>
<link href="/ui/css/bootstrap.css" rel="stylesheet" />
<link href="/ui/css/font-awesome.css" rel="stylesheet" />
<link href="/ui/js/morris/morris-0.4.3.min.css" rel="stylesheet" />
<link href="/ui/css/custom-styles.css" rel="stylesheet" />

</head>

<body>
	<div id="wrapper">
		<nav class="navbar navbar-default top-navbar" role="navigation">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle" data-toggle="collapse"
					data-target=".sidebar-collapse">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="/">初六小宝贝</a>
			</div>

			<ul class="nav navbar-top-links navbar-right">

			</ul>
		</nav>
		<!--/. NAV TOP  -->
		<nav class="navbar-default navbar-side" role="navigation">
			<div class="sidebar-collapse">
				<ul class="nav" id="main-menu">

					<li><a class="active-menu" href="/"><i
							class="fa fa-dashboard"></i> 桌面</a></li>
					<li><a href="###"><i class="fa fa-edit"></i> 奖状墙 </a></li>
				</ul>

			</div>

		</nav>
		<!-- /. NAV SIDE  -->
		<div id="page-wrapper">
			<div id="page-inner">


				<div class="row">
					<div class="col-md-12">
						<h1 class="page-header">
							初六的桌面 <small></small>
						</h1>
					</div>
				</div>
				<!-- /. ROW  -->

				<div class="row">
					<div class="col-md-6 col-sm-12 col-xs-12">
						<div class="panel panel-primary text-center no-boder bg-color-red">
							<div class="panel-body">
								<div style="min-height: 74px">
									<c:if test="${num>0 }">
										<c:if test="${num>=10 }">
											<c:forEach begin="1" end="${num/10}" var="i">
												<img alt="" src="/ui/image/h2.png" height="74px">
											</c:forEach>
										</c:if>
										<c:forEach begin="1" end="${num%10}" var="i">
											<img alt="" src="/ui/image/h1.png" height="50px">
											</span>
										</c:forEach>
									</c:if>

									<c:if test="${num<=0 }">
										<span class="glyphicon glyphicon-thumbs-down fa-5x"
											style="color: rgb(255, 0, 66);"></span>小红花用光了！
								</c:if>
								</div>
								<h3>${numTen*10+num }</h3>
							</div>
							<div class="panel-footer back-footer-red">小红花</div>
						</div>
					</div>
					<div class="col-md-3 col-sm-6 col-xs-6">
						<div
							class="panel panel-primary text-center no-boder bg-color-green">
							<div class="panel-body" id="add">
								<span class="glyphicon glyphicon-thumbs-up fa-5x"></span>
								<h3>${in }</h3>
							</div>
							<div class="panel-footer back-footer-green">获得总数</div>
						</div>
					</div>
					<div class="col-md-3 col-sm-6 col-xs-6">
						<div
							class="panel panel-primary text-center no-boder bg-color-blue">
							<div class="panel-body" id="remove">
								<span class="glyphicon glyphicon-shopping-cart fa-5x"></span>
								<h3>${out }</h3>
							</div>
							<div class="panel-footer back-footer-blue">消费数量</div>
						</div>
					</div>
				</div>


				<div class="row">


					<div class="col-md-9 col-sm-12 col-xs-12">
						<div class="panel panel-default">
							<div class="panel-heading">日期图</div>
							<div class="panel-body">
								<div id="morris-bar-chart"></div>
							</div>
						</div>
					</div>
					<div class="col-md-3 col-sm-12 col-xs-12">
						<div class="panel panel-default">
							<div class="panel-heading">分量图</div>
							<div class="panel-body">
								<div id="morris-donut-chart"></div>
							</div>
						</div>
					</div>

				</div>
				<!-- /. ROW  -->
				<div class="row">
					<div class="col-md-6 col-sm-12 col-xs-12">

						<div class="panel panel-default">
							<div class="panel-heading">获取记录</div>
							<div class="panel-body">
								<div class="table-responsive">
									<table class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>日期</th>
												<th>数量</th>
												<th style="width: 50%">事件</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${inList }" var="i">
												<tr>
													<td><fmt:formatDate value="${i.date}"
															pattern="yyyy-MM-dd HH:mm:ss" /></td>
													<td>${i.num }</td>
													<td>${i.content }</td>
												</tr>
											</c:forEach>


										</tbody>
									</table>
								</div>
							</div>
						</div>

					</div>
					<div class="col-md-6 col-sm-12 col-xs-12">

						<div class="panel panel-default">
							<div class="panel-heading">消费记录</div>
							<div class="panel-body">
								<div class="table-responsive">
									<table class="table table-striped table-bordered table-hover">
										<thead>
											<tr>
												<th>日期</th>
												<th>数量</th>
												<th style="width: 50%">事件</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${outList }" var="i">
												<tr>
													<td><fmt:formatDate value="${i.date}"
															pattern="yyyy-MM-dd HH:mm:ss" /></td>
													<td>${i.num }</td>
													<td>${i.content }</td>
												</tr>
											</c:forEach>



										</tbody>
									</table>
								</div>
							</div>
						</div>

					</div>
				</div>
			</div>
			<!-- /. ROW  -->
			<footer> </footer>
		</div>
		<!-- /. PAGE INNER  -->
	</div>
	<!-- /. PAGE WRAPPER  -->
	</div>
	<!-- /. WRAPPER  -->
	<!-- JS Scripts-->
	<!-- jQuery Js -->
	<script src="/ui/js/jquery-1.10.2.js"></script>
	<!-- Bootstrap Js -->
	<script src="/ui/js/bootstrap.min.js"></script>
	<!-- Metis Menu Js -->
	<script src="/ui/js/jquery.metisMenu.js"></script>
	<!-- Morris Chart Js -->
	<script src="/ui/js/morris/raphael-2.1.0.min.js"></script>
	<script src="/ui/js/morris/morris.js"></script>
	<script type="text/javascript">
		(function($) {
			"use strict";
			var mainApp = {

				initFunction : function() {
					/*MENU 
					------------------------------------*/
					$('#main-menu').metisMenu();

					$(window).bind("load resize", function() {
						if ($(this).width() < 768) {
							$('div.sidebar-collapse').addClass('collapse')
						} else {
							$('div.sidebar-collapse').removeClass('collapse')
						}
					});

					/* MORRIS BAR CHART
					-----------------------------------------*/
					 Morris.Line({
						element : 'morris-bar-chart',
						data : ${dateNum},
						xkey : 'date1',
						ykeys : [ 'innum', 'outnum' ],
						labels : [ '获取', '消费' ],
						hideHover : 'auto',
						resize : true,
						lineColors:['#5cb85c','#4CB1CF']
					}); 

					/* MORRIS DONUT CHART
					----------------------------------------*/
					Morris.Donut({
						element : 'morris-donut-chart',
						data : [ {
							label : "获取",
							value : '${in}'
						},{
							label : "消费",
							value : '${out}'
						}],
						colors :['#5cb85c','#4CB1CF'],
						resize : true
					}); 

				},

				initialization : function() {
					mainApp.initFunction();

				}

			}
			// Initializing ///

			$(document).ready(function() {
				mainApp.initFunction();
				$("#add").on("click", function() {
					$("#form1")[0].reset();
					$('#myModal1').modal('show');

				})
				$("#remove").on("click", function() {
					$("#form2")[0].reset();
					$('#myModal2').modal('show');

				})
				$("#s1").on("click", function() {
					if(!$("#content1").val()){
						$("#content1").parent().addClass("has-error");
						return;
					}
					$.post("/save", $("#form1").serialize(), function() {
						$('#myModal1').modal('hide');
						window.location.reload();
					})
				})
				$("#s2").on("click", function() {
					if(!$("#content2").val()){
						$("#content2").parent().addClass("has-error");
						return;
					}
					$.post("/save", $("#form2").serialize(), function() {
						$('#myModal2').modal('hide');
						window.location.reload();
					})
				})
			});

		}(jQuery));
	</script>
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal1" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">获取小红花</h4>
				</div>
				<div class="modal-body">
					<form id="form1">
						<input type="hidden" name="type" value="0">
						<div class="form-group">
							<label for="content">说明：</label> <input type="text"
								class="form-control" name="content" id="content1" placeholder="">
						</div>
						<div class="form-group">
							<label for="num">数量：</label> <input type="number"
								class="form-control" name="num" placeholder="" value="1">
						</div>
					</form>
				</div>
				<div class="modal-footer">

					<button type="button" class="btn btn-success" id="s1">增加小红花</button>
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>

		<!-- /.modal-dialog -->

	</div>
	<!-- /.modal -->
	<!-- 模态框（Modal） -->
	<div class="modal fade" id="myModal2" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">消费小红花</h4>
				</div>
				<div class="modal-body">
					<form id="form2">
						<input type="hidden" name="type" value="1">
						<div class="form-group">
							<label for="content">说明：</label> <input type="text"
								class="form-control" name="content" id="content2" placeholder="">
						</div>
						<div class="form-group">
							<label for="num">数量：</label> <input type="number"
								class="form-control" name="num" placeholder="" value="1">
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button id="s2" type="button" class="btn btn-info ">消费小红花</button>
					</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- /.modal -->
</body>

</html>