// 定时器
var interval;
$(function() {
	interval = setInterval(startTail, 1000);
})

function startTail() {
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/tail/tailCmd',
		data: {
			cmdId: $("#cmdId").val()
		},
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				// 接收服务端的实时日志并添加到HTML页面中
				$("#log-container").append(data.obj);
				// 滚动条滚动到最低部
				if (data.obj != "") {
					window.scrollTo(0, document.body.scrollHeight);
				}
			}
		},
		error: function() {
			//layer.alert(commonStr.errorInfo);
		}
	});
}
