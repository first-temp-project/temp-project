

$(".find-btn-right").on("click", function(e) {
	e.preventDefault();
	const userEmail = $(this).closest(".find-container").find("input[name=userEmail").val().trim();
	if (!userEmail) { alert("이메일을 입력하세요"); return; }
	if (!validateEmail(userEmail)) { alert("이메일 형식이 아닙니다."); return; }
	findByEmail({ userEmail });
});

function findByEmail(email) {
	const $dimmed = $(".dimmed-container");
	$.ajax({
		url: `${contextPath}/find-account`,
		method: 'post',
		data: JSON.stringify(email),
		contentType: 'application/json;charset=UTF-8',
		beforeSend() { $dimmed.show(); },
		complete() { $dimmed.hide(); },
		success: msg => alert(msg),
		error: xhr => alert(xhr.responseText)
	});
}

function validateEmail(email) { /*이메일정규식*/
	const regex = /^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,4}$/i;
	if (!regex.test(email)) {
		return false;
	}
	return true;
}






























































/*
const findByEmail = email => {
	$.ajax({
		url: `${contextPath}/find-account`,
		method: 'post',
		data: JSON.stringify(email),
		contentType: 'application/json;charset=UTF-8',
		success: function(msg) { alert(msg); },
		error: function(xhr) { alert(xhr.responseText); }
	});
}

$(".find-btn-right").on("click", function(e) {
	e.preventDefault();
	const userEmail = $(this).closest(".find-container").find("input[name=userEmail]").val().trim();
	if (!userEmail) { alert("이메일을 입력하세요"); return; }
	if (!validateEmail(userEmail)) { alert("잘못된 이메일 형식 입니다."); return; }
	findByEmail({ userEmail });
	alert("정상적으로 요청되었습니다. 잠시만 기다려주세요.");
});

*/













































/*const emailService = (email) => {
	$.ajax({
		url: `${contextPath}/find-account`,
		method: 'post',
		data: JSON.stringify(email),
		contentType: 'application/json;charaset=UTF-8',
		success: function(msg) { alert(msg); },
		error: function(xhr) { alert(xhr.responseText); }
	});
}

$(".find-btn").on("click", function(e) {
	e.preventDefault();
	const $input = $("input[name=userEmail]");
	const userEmail = $input.val().trim();
	if (!userEmail) { alert("이메일을 입력해 주세요."); return; }
	if (!validateEmail(userEmail)) { alert("잘못된 이메일 형식입니다."); return; }
	emailService({ userEmail });
	alert("정상적으로 요청되었습니다. 확인메세지가 나올때까지 기다려주세요");
});
*/

