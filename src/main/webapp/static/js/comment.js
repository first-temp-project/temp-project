const commentService = (function() {

	function insert(boardNum, comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${boardNum}`,
			method: 'post',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(comment),
			success: callback
		})
	}

	function update(commentNum, comment, callback) {
		$.ajax({
			url: `${contextPath}/comments/${commentNum}`,
			method: 'patch',
			contentType: 'application/json;charset=UTF-8',
			data: JSON.stringify(comment),
			success: callback
		})
	}

	function remove(commentNum, callback) {
		$.ajax({
			url: `${contextPath}/comments/${commentNum}`,
			method: 'delete',
			success: callback
		})
	}

	function getCommentsDto(boardNum, page, callback) {
		$.ajax({
			url: `${contextPath}/comments/${boardNum}?page=${page}`,
			method: 'get',
			success: callback
		})
	}

	return { insert, update, remove, getCommentsDto }
})();



(function() {
	const boardNum = $(".container").data("boardNum");
	const $updateLi = $("li.update-li");
	const editStatus = { isEditing: false };
	const $commentUl = $("ul.comment-ul");
	const { $updateTextarea, $cancelBtn, $updateSaveBtn, $replySaveBtn } = getEleFromUpdateLi($updateLi);
	let page = 1;
	showComments(boardNum, page);

	$(".comment-insert-btn").on("click", function(e) {
		e.preventDefault();
		if (!sessionUserNum) { alert("로그인 후 사용하실 수 있습니다."); return; }
		const $insertTextarea = $(this).closest(".comment-container").find("textarea");
		const commentContent = $insertTextarea.val().trim();
		if (!commentContent) { alert("댓글을 입력하세요"); return; }
		commentService.insert(boardNum, { commentContent: commentContent }, () => {
			$insertTextarea.val("");
			showComments(boardNum, page);
		});
	});

	$commentUl.on("click", ".comment-remove-btn", function(e) {
		e.preventDefault();
		if (!confirm("정말로 삭제 하시겠습니까?")) { return; }
		const commentNum = $(this).closest("li").data("commentNum");
		commentService.remove(commentNum, () => showComments(boardNum, page));
	});

	$cancelBtn.on("click", function(e) {
		e.preventDefault();
		resetUpdateLi($updateLi);
		editStatus.isEditing = false;
	});

	$commentUl.on("click", ".comment-update-btn", function(e) {
		e.preventDefault();
		if (editStatus.isEditing) { return; }
		editStatus.isEditing = true;
		const $li = $(this).closest("li");
		const commentNum = $li.data("commentNum");
		const commentContent = $li.find("span.comment-content").text();
		$li.hide().after($updateLi);
		$updateTextarea.val(commentContent);
		$updateLi.data("commentNum", commentNum).show();
	});

	$updateSaveBtn.on("click", function(e) {
		e.preventDefault();
		const commentContent = $updateTextarea.val().trim();
		if (!commentContent) { alert("댓글을 입력하세요."); return; }
		const commentNum = $updateLi.data("commentNum");
		commentService.update(commentNum, { commentContent }, () => {
			resetUpdateLi($updateLi);
			editStatus.isEditing = false;
			showComments(boardNum, page);
		});
	});

	$commentUl.on("click", ".comment-reply-btn", function(e) {
		e.preventDefault();
		if (editStatus.isEditing) { return; }
		editStatus.isEditing = true;
		const $li = $(this).closest("li");
		$replySaveBtn.css("display", "inline-block");
		$updateSaveBtn.hide();
		$li.after($updateLi);
		$updateLi.data("parentNum", $li.data("commentNum")).data("parentId", $li.data("userId")).show();
	});

	$replySaveBtn.on("click", function(e) {
		e.preventDefault();
		const commentContent = $updateTextarea.val().trim();
		if (!commentContent) { alert("댓글을 입력하세요"); return; }
		const comment = { commentParentNum: $updateLi.data("parentNum"), commentParentId: $updateLi.data("parentId"), commentContent };
		commentService.insert(boardNum, comment, () => {
			resetUpdateLi($updateLi);
			editStatus.isEditing = false;
			showComments(boardNum, page);
		});
	});

	$commentUl.on("click", ".comment-more-btn", function(e) {
		e.preventDefault();
		showComments(boardNum, ++page);
	});

})();




function getEleFromUpdateLi($updateLi) {
	return {
		$updateTextarea: $updateLi.find("textarea"),
		$cancelBtn: $updateLi.find(".comment-cancel-btn"),
		$updateSaveBtn: $updateLi.find(".comment-update-done"),
		$replySaveBtn: $updateLi.find(".comment-reply-done")
	}
}

function resetUpdateLi($updateLi) {
	const { $updateTextarea, $updateSaveBtn, $replySaveBtn } = getEleFromUpdateLi($updateLi);
	$updateTextarea.val("");
	$updateSaveBtn.css("display", "inline-block");
	$replySaveBtn.hide();
	$updateLi.prev().show();
	$updateLi.hide().appendTo($("body"));
}

function showComments(boardNum, page) {
	commentService.getCommentsDto(boardNum, page, commentsDto => appendComments(commentsDto));
}

function appendComments(commentsDto) {
	const comments = createComments(commentsDto);
	$("ul.comment-ul").empty().append(comments);
}

function createComments(commentsDto) {
	const comments = commentsDto.comments;
	const nextCountPage = commentsDto.nextCountPage;
	let html = "";
	comments.forEach(comment => {
		html += `${comment.commentNum != comment.commentParentNum ? '⤷' : ''}`;
		html += `<li data-comment-num="${comment.commentNum}" data-parent-num="${comment.commentParentNum}" data-user-id="${comment.userId}" data-user-num="${comment.userNum}">`;
		html += `<span class="profile">`;
		html += `<i class="fa fa-user-circle" aria-hidden="true"></i>`;
		html += `</span>`;
		html += `<section>`;
		html += `<div>`;
		html += `<span class="writer">${comment.userId}</span>`;
		html += `</div>`;
		html += `<div>`;
		html += `<span class="comment-parent-id">${comment.commentParentId == null ? '' : '@' + comment.commentParentId + ' '}</span>`;
		html += `<span class="comment-content">${comment.commentContent}</span>`;
		html += `</div>`;
		html += `<div class="comment-footer">`;
		html += `<span>${elapsetTime(comment.commentRegisterDate)}</span>`;
		html += createCommentBtns(comment, sessionUserNum, isAdmin);
		html += `</div>`;
		html += `</section>`;
		html += `</li>`;
	});
	if (nextCountPage > 0) {
		html += `<a href="#" class="comment-more-btn" >댓글 더보기</a>`;
	}
	return html;
}

function createCommentBtns(comment, sessionUserNum, isAdmin) {
	let html = "";
	if (!sessionUserNum) { return html; }
	const isMyComment = comment.userNum == sessionUserNum;
	const canReply = !isMyComment && (comment.commentNum == comment.commentParentNum);

	if (isMyComment || isAdmin) {
		html += `<a href="#" class="comment-update-btn">수정</a>`;
		html += `<a href="#" class="comment-remove-btn">삭제</a>`;
	}
	if (canReply) {
		html += `<a href="#" class="comment-reply-btn">답글</a>`;
	}
	return html;
}

function elapsetTime(date) {
	const currentDate = new Date();
	const registerDate = new Date(date);
	const gap = (currentDate - registerDate) / 1000;
	const times = [
		{ name: '년', milliseconds: 60 * 60 * 24 * 365 },
		{ name: '개월', milliseconds: 60 * 60 * 24 * 30 },
		{ name: '일', milliseconds: 60 * 60 * 24 },
		{ name: '시간', milliseconds: 60 * 60 },
		{ name: '분', milliseconds: 60 },
	];

	for (const time of times) {
		const result = Math.floor(gap / time.milliseconds);
		if (result > 0) { return `${result}${time.name}전`; }
	}
	return "방금전";
}







