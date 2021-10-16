const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}
};
$(".search-result").hide();
const search = () => {
	let query = $("#search").val();
	if (!query) {
		$(".search-result").hide();
	} else {
		let url = `http://localhost:8080/search/${query}`;
		fetch(url)
			.then((res) => {
				return res.json();
			})
			.then((data) => {
				let text = `<div class="list-group">`;
				data.forEach((res) => {
					text += `<a href="/user/${res.id}/contact" class="list-group-item list-group-item-action">
                    ${res.name}</a>`;
				});
				text += `</div>`;
				$(".search-result").html(text);
				$(".search-result").show();
			});
	}
};
