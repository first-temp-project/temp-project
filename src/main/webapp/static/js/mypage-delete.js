$(".user-delete-btn").on("click", function(e) {
	e.preventDefault();
	if (!confirm("정말로 탈퇴 하시겠습니까?")) { return; }
	$(this).closest("form").submit();
});