let submenuItems = document.querySelectorAll('ul.submenu');
let heightArray = new Array(submenuItems.length);

submenuItems.forEach((submenu, i) => {
	heightArray[i] = submenu.offsetHeight;
	submenu.style.height = '0px';
	submenu.style.visibility = 'visible';
});

document.querySelectorAll('nav>ul>li').forEach((li, i) => {
	li.addEventListener('mouseover', () => {
		submenuItems[i].style.height = `${heightArray[i]}px`;
	});
	li.addEventListener('mouseout', () => {
		submenuItems[i].style.height = '0px';
	});
});

$("form").on("submit", function(e) {
	const $form = $(this);
	if ($form.data("submit")) {
		e.preventDefault();
		return;
	}
	$form.data("submit", true);
});