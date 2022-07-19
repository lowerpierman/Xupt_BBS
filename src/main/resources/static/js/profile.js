$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	var followedUserName = $(btn).prev().val();
	alert($(btn).prev().val());
	$.post(
		CONTEXT_PATH + "/follow",
		{"followedUserName":$(btn).prev().val()},
		function (data) {
			data = $.parseJSON(data);
			if(data.code == 0) {
				if(data.followStatus=="关注")
					$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
				else
				$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
			} else {
				alert(data.msg);
			}
		}

	);
	/*if($(btn).hasClass("btn-info")) {
		// 关注TA
		$(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
	} else {
		// 取消关注
		$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
	}*/
}