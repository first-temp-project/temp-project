import { classNames, getCategory, isUpdate, showThumbnails, createHiddenInputs } from './file.js';

(function() {
	const boardNum = $(".container").data("boardNum");
	const isDetail = $(".container").data("boardDetail");
	if (isDetail && getCategory() === "download") { showThumbnails(boardNum, true); }
	if (isUpdate()) { showThumbnails(boardNum, false); }
})();




(function() {

	$(".write-board-btn,.update-board-btn").on("click", function(e) {
		e.preventDefault();
		const $form = $("form");
		if (!validateForm($form)) { return; }
		const html = isUpdate() ? createHiddenInputs(classNames.NEW) + createHiddenInputs(classNames.REMOVE) : createHiddenInputs(classNames.ORIGINAL);
		$form.append(html).submit();
	});

	$(".deleteBoardBtn").on("click", function(e) {
		e.preventDefault();
		if (!confirm("정말로 삭제 하시겠습니까?")) { return; }
		location.href = $(this).attr("href");
	});

	$("a.page").on("click", function(e) { /*board-list page click*/
		e.preventDefault();
		const $pageForm = $("form[name=pageForm]");
		$pageForm.find("input[name=page]").val($(this).attr("href"));
		$pageForm.submit();
	});

	$("form[name=searchForm").on("submit", function(e) { /*게시판 검색*/
		e.preventDefault();
		if (!$(this).find("select[name=type]").val()) {
			alert("카테고리를 선택하세요");
			return;
		}
		if (!$(this).find("input[name=keyword]").val()) {
			alert("내용을 입력하세요");
			return;
		}
		$(this).find("input[name=page]").val(1);
		this.submit();
	});

})();


function validateForm($form) {
	if (!$form.find("input[name=boardTitle]").val().trim()) { alert("제목을 입력해주세요"); return false; }
	if (!$form.find("textarea[name=boardContent]").val().trim()) { alert("내용을 입력해주세요"); return false; }
	return true;
}



























































