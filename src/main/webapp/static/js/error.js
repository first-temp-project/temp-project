$(".back-btn").on("click", function(e) {
	e.preventDefault();
	location.histroy > 1 ? location.href = history.back : location.href = $(this).attr("href");
});