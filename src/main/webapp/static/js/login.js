const $form = $("form[name=loginForm]");
$form.find("input[type=submit]").on("click", function(e) {
	e.preventDefault();
	if (!$form.find("input[name=userId]").val().trim()) {
		alert("아이디를 입력해 주세요.");
		return;
	}
	if (!$form.find("input[name=userPassword]").val().trim()) {
		alert("비밀번호를 입력해주세요");
		return;
	}
	$form.submit();
});

