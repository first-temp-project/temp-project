const $thumbnailImg = $("#thumbnailPreview");
const $fileInput = $("input[name=multipartFile]");
const $form = $("form#productForm");

$fileInput.on("change", function(e) {
	const file = e.target.files[0];
	if (!file) { $thumbnailImg.hide(); return; }
	if (!file.type.startsWith("image/")) {
		alert("이미지형식만 업로드 가능합니다.");
		$thumbnailImg.hide();
		$(this).val("");
		return;
	}
	const reader = new FileReader();
	reader.onload = (e) => {
		$thumbnailImg.attr("src", e.target.result);
		$thumbnailImg.show();
	}
	reader.readAsDataURL(file);
});

$("button.product-register-btn").on("click", function() {
	if (!validateProductForm($form)) { return; }
	$form.submit();
});


function validateProductForm($form, isUpdate = false) {
	if (!isUpdate) {
		if (!$fileInput[0].files[0]) {
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
	if (!$form.find("select[name=itemCategory]").val().trim()) {
		alert("카테고리를 선택하세요");
		return false;
	}
	return true;
}