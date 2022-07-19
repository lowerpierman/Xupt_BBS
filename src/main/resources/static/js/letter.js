$(function(){
	$("#sendBtn").click(send_letter);
	$(".close").click(delete_msg);
});

function send_letter() {
	$("#sendModal").modal("hide");
	//获取发送私信的标题以及内容

	var toName=$("#recipient-name").val();
	var letterContent = $("#message-text").val();
	//发送异步请求
	$.post(
		CONTEXT_PATH+"/letter/add",
		{"toName":toName,"content":letterContent},
		function(data){
			//alert("开始异步请求");
			data = $parseJSON(data);
			//在提示框中显示提示信息
			$("#hintModal").text(data.msg);
			$("#hintModal").modal("show");
			setTimeout(function(){
				$("#hintModal").modal("hide");
				if(data.code==0) window.location.reload();
			}, 2000);
		}
	);
}

function delete_msg() {
	// TODO 删除数据
	$(this).parents(".media").remove();
}