$(function() {
	if ($("#adminCount").val() > 0) {
		layer.open({
			type: 1,
			shade: false,
			title: "登录全能神器",
			closeBtn: false,
			area: ['450px', '350px'], //宽高
			content: $('#windowDiv')
		});
	} else {
		layer.open({
			type: 1,
			shade: false,
			title: "初始化管理员",
			closeBtn: false,
			area: ['450px', '400px'], //宽高
			content: $('#addUserDiv')
		});
	}
	
})



function login() {
	showLoad();
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/login/login',
		data : $("#loginForm").serialize(),
		dataType : 'json',
		success : function(data) {
			closeLoad();
			if (data.success) {
				location.href = ctx + "/adminPage/monitor";
			} else {
				alert(data.msg);
				refreshCode('codeImg');
			}
		},
		error : function() {
			closeLoad();
			alert("出错了,请联系技术人员!");
		}
	});
}


function refreshCode(id) {
	$("#" + id).attr("src", ctx + "/adminPage/login/getCode?t=" + (new Date()).getTime());
}


function getKey() {
	if (event.keyCode == 13) {
		login();
	}
}

function addAdmin() {
	if ($("#trueName").val() == '') {
		layer.msg("姓名为空");
		return;
	}
	if ($("#adminName").val() == '') {
		layer.msg("登录名为空");
		return;
	}
	if ($("#adminPass").val() == '' || $("#repeatPass").val() == '') {
		layer.msg("密码为空");
		return;
	}
	if ($("#adminPass").val() != $("#repeatPass").val()) {
		layer.msg("两次密码不一致");
		return;
	}


	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/login/addAdmin',
		data: $("#adminForm").serialize(),
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				location.reload();
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			layer.alert(commonStr.errorInfo);
		}
	});
}