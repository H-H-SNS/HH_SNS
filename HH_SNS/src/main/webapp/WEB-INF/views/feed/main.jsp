<%@page import="edu.spring.ex06.domain.UserInfoVO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<script src="https://code.jquery.com/jquery-3.4.1.min.js"></script>
<style type="text/css">
.input_feed {
	display: block;
	flex-wrap: wrap;
	align-items: center;
	justify-content: space-between;
	width: 30%;
	margin: 0 auto;
	margin-bottom: 20px;
	margin-top: 20px;
	padding: 20px;
	background-color: #f7f7f7;
	border: 1px solid #ddd;
	height: 200px;
	position: relative; /* 추가 */
}

.feedContent {
	width: 70%;
	height: 70%;
	margin-right: 10px;
}

.btn_add {
	position: absolute;
	bottom: 0;
	width: 20%;
	height: 30px;
	background-color: #1da1f2;
	color: #fff;
	border: none;
	border-radius: 4px;
	font-size: 14px;
	cursor: pointer;
	margin-bottom: 1; /* 추가 */
	margin-right: 1px;
}

.div_post {
	display: block; /* flex에서 block으로 변경 */
	flex-wrap: wrap;
	justify-content: center;
	align-items: center;
	width: 80%;
	margin: 0 auto;
}

.post_item {
	width: 30%;
	margin: 0 auto; /* 가운데 정렬을 위해 추가 */
	margin-bottom: 20px;
	padding: 20px;
	background-color: #f7f7f7;
	border: 1px solid #ddd;
	margin: 0 auto; /* 가운데 정렬을 위해 추가 */
	margin-bottom: 20px;
	padding: 20px;
	background-color: #f7f7f7;
	margin-bottom: 20px;
	padding: 20px;
	background-color: #f7f7f7;
	padding: 20px;
	background-color: #f7f7f7;
	background-color: #f7f7f7;
}

.div_btn {
	display: flex;
	justify-content: space-between;
	margin-top: 10px;
}

.update_btn, .delete_btn {
	background-color: transparent;
	border: none;
	color: #1da1f2;
	font-size: 14px;
	cursor: pointer;
}

</style>
<meta charset="UTF-8">
<title>H&H</title>
</head>
<body>

	<%
	String userId = (String) session.getAttribute("userId");
	%>

	<!-- ▼ 아래 히든인 이유는 1씩 올리는것뿐인걸 굳이 보일필요 X -->
	<input type="hidden" id="feedId" value="1">

	<!-- 
	▼ 등록버튼은 절 대 form안에 넣지말기 ㅎ-ㅎ 
	-->
	
		<div class="input_feed">
			<p id="userProfile"><a href="../feed/list"><img width="100px" height="100px" src="display?fileName=${userinfovo.userProfile}" /></a></p>
			<p id="userId"><b>${userId}</b></p>
			<p id="userNickname">${userinfovo.userNickname }</p>
			<input type="text" id="feedContent" placeholder="피드 작성하기" required>
			<input type="submit" id="btn_add" value="등록">
		</div>
	
	
	<br>
	<hr>
	<br>
	
	<!-- ▼ 이건 RESTController랑 관계 X 그냥 여기서 보여줄 태그일 뿐임 ㅎ-ㅎ -->
	<div style="text-align: center;">
		<div id="feeds"></div>
	</div>

	<!--  BoardController -> registerPOST()에서 보낸 데이터 저장
	<input type="hidden" id="insertAlert" value="${insert_result }">
	 -->

	<script type="text/javascript">
		// - css 선택자 :
		//	'p' : 태그(요소)
		//	'#p1' : 아이디
		//	'.c1' : 클래스

		$(document).ready(function() {
			//$('.input_feed').prependTo('body');
			getAllMain();
			// 피드 작성버튼
			$('#btn_add').click(function() {
				var feedId = $('#feedId').val();
				const userId = document.getElementById("userId").textContent;
				var feedContent = $('#feedContent').val();
				console.log(feedContent);

				var obj = {
				'feedId' : feedId,
				'userId' : userId,
				'feedContent' : feedContent

				}
				console.log(obj);

				$.ajax({
					type : 'POST',
					url : '../feeds',
					headers : {
						'Content-Type' : 'application/json'
					},
					data : JSON.stringify(obj),
					success : function(result) {
						console.log(result);
						if (result == 1) {
							console.log('★ 피드작성 완료');
							getAllMain();
						} else {
							console.log('★ 피드작성 실패');
						}
					}
				});// end ajax()
			});// end btn_add.click();
			
					// - css 선택자 :
					//	'p' : 태그(요소)
					//	'#p1' : 아이디
					//	'.c1' : 클래스

					function getAllMain() {
						var feedId = $('#feedId').val();

						var url = '../feeds/all/' + feedId;
							$.getJSON(
								url,
								function(data) {
									console.log(data);
									const userId = document.getElementById("userId").textContent;
									console.log(userId);
									var list = '';
										$(data).each(function() {
											console.log(this);

											var feedDate = new Date(this.feedDate);
											var yyyy = feedDate.getFullYear();
											var mm = String(feedDate.getMonth() + 1).padStart(2, '0'); // 0부터 시작하므로 +1
											var dd = String(feedDate.getDate()).padStart(2, '0');
											var feedDate = yyyy + '년 ' + mm + '월 ' + dd + '일';
											var disabled = 'disabled';
											var readonly = 'readonly';
											
											// 회원이어야지만 작성은 가능하나
											// 회원이 아니여도 피드를 볼 수 있고
											// 피드에 있는 링크를 통해 그 회원의 개인 피드(=list), 회원의 전체 피드(=main / detail) 볼 수 있다.
											console.log('★');
											console.log(userId); // 접속한 회원
											console.log(this.userId); // 작성 된 회원
											

											if (userId == this.userId) {
											disabled = '';
											readonly = '';
										}
											list += '<div class="div_post">'
											+ '<div class="post_item">'
											+ '<input type="hidden" id="feedId" value="' + this.feedId + '">'
											+ '<p>' + '<a href="../feed/list?feedId=' + this.feedId + '">' + '<img width="100px" height="100px" src="display?fileName=' + this.userProfile + '" />' + '</a>' +'</p>'
											+ '<p>' + '<a href="../feed/list?feedId=' + this.feedId + '">' + '<b>' + this.userId +'</b>' +'</a>' + '</p>'
											+ '<p>' + this.userNickname + '</p>'
											+ feedDate
											+ '&nbsp;&nbsp;'
											+'<p id="feedContent">' + '<a href="../feed/detail?feedId=' + this.feedId + '">' + this.feedContent +'</p>'
											+ '&nbsp;&nbsp;'
											+ '<br>'
											+ '<button class="btn_update" ' + disabled + '>수정</button>'
											+ '<button class="btn_delete" ' + disabled + '>삭제</button>'
											+ '</div>'
											+ '</div>';

									});// end data.funchion;
									
									$('#feeds').html(list);
								}//end function(data);
							);// end getJSON();
						}// end getAllMain();
						
						$('#feeds').on('click', '.div_post .btn_update', function(){
							console.log(this);
							
							var feedId = $(this).prevAll('#feedId').val();
							var feedContent = $(this).prevAll('#feedContent').val();
							console.log("선택된 피드 번호 : " + feedId + ", 피드 내용 : " + feedContent);
							
							$.ajax({
								type : 'PUT', 
								url : '../feeds/' + feedId,
								headers : {
									'Content-Type' : 'application/json'
								},
								data : feedContent, 
								success : function(result) {
									console.log(result);
									if(result == 1) {
										console.log('★ 피드수정 완료');
										getAllMain();
									} else {
										console.log('★ 피드수정 실패');
									}
								}
							});// end ajax
							
						});// end feeds.update
						
						// 삭제 버튼을 클릭하면 선택된 댓글 삭제
						$('#feeds').on('click', '.div_post .btn_delete', function(){
							console.log(this);
							
							var feedId = $(this).prevAll('#feedId').val();
							
							$.ajax({
								type : 'DELETE', 
								url : '../feeds/' + feedId, 
								headers : {
									'Content-Type' : 'application/json'
								},
								data : feedId,
								success : function(result) {
									console.log(result);
									if(result == 1) {
										console.log('★ 피드삭제 완료');
										getAllMain();
									} else {
										console.log('★ 피드삭제 실패');
									}
								}
							});//end ajax
						}); // end feeds.delete
					}); // end ready();
	</script>


</body>
</html>