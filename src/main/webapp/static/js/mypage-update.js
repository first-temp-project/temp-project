import { changeCss, setValidationChecks, emptyCheck, validationChecks } from './join.js';
const $passwordInput = $("input[name=userPassword]");
const $passwordCheckInput = $("input[name=userPasswordCheck]");
const { $updateReadyBtn, $updateDoneBtn, $cancelBtn } = getBtns();
let password = "";


$updateReadyBtn.on("click", function(e) {
	e.preventDefault();
	$updateReadyBtn.hide();
	$updateDoneBtn.show();
	$cancelBtn.show();
	password = $passwordInput.val().trim();
	$passwordCheckInput.attr("type", "password");
	$passwordInput.prop("readOnly", false).val("").focus();
});


$updateDoneBtn.on("click", function(e) {
	e.preventDefault();
	if (!emptyCheck()) { return; }
	const { userPassword, userPasswordCheck } = validationChecks;
	if (!userPassword || !userPasswordCheck) { alert("모든 필드를 정확히 입력하세요."); return; }
	$(this).closest("form").submit();
});


$cancelBtn.on("click", function(e) {
	e.preventDefault();
	$updateReadyBtn.show();
	$updateDoneBtn.hide();
	$cancelBtn.hide();
	changeCss($passwordInput, true);
	changeCss($passwordCheckInput, true);
	setValidationChecks($passwordInput, false);
	setValidationChecks($passwordCheckInput, false);
	$passwordCheckInput.attr("type", "hidden");
	$passwordInput.prop("readOnly", true).val(password);
	password = "";
});



function getBtns() {
	return {
		$updateReadyBtn: $("input.changePwBtn"),
		$updateDoneBtn: $("input.changePwOkbtn"),
		$cancelBtn: $("input.cancelChangePwBtn")
	}
}




































