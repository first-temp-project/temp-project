(function() {

})();


function validateProductForm($form, isUpdate = false) {
	if (!isUpdate) {
		if (!$("input[name=multipartFile]")[0].files[0]) {
			alert("이미지를 업로드 하세요.");
			return false;
		}
	}
	if (!$form.find("input[name=itemTitle]").val().trim()) {
		alert("상품명을 입력하세요.");
		return false;
	}
	if (!$form.find("input[name=itemPrice]").val().trim()) {
		alert("가격을 입력하세요.");
		return false;
	}
	if (!$form.find("input[name=itemUrl]").val().trim()) {
		alert("url을 입력하세요");
		return false;
	}
	if (!$form.find("select[name=itemCategory]").val()) {
		alert("카테고리를 선택하세요");
		return false;
	}
	return true;
}





















































































