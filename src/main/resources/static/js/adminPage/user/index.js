var load;
$(function(){

})


function search() {
	$("input[name='pageNum']").val(1);
	$("#searchForm").submit();
}

function add() {
	$("#id").val(""); 
	$("#name").val("");
	$("#pass").val("");
	$("#open option:first").prop("select", true);
	form.render();
	showWindow("添加用户");
}


function showWindow(title){
	layer.open({
		type : 1,
		title : title,
		area : [ '500px', '420px' ], // 宽高
		content : $('#windowDiv')
	});
}

function addOver() {
	if ($("#name").val() == "") {
		layer.msg("登录名为空");
		return;
	}
	if ($("#pass").val() == "") {
		layer.msg("密码为空");
		return;
	}
	showLoad();
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/user/addOver',
		data : $('#addForm').serialize(),
		dataType : 'json',
		success : function(data) {
			closeLoad();
			if (data.success) {
				location.reload();
			} else {
				layer.msg(data.msg);
			}
		},
		error : function() {
			closeLoad();
			alert("出错了,请联系技术人员!");
		}
	});
	
	
}

function edit(id) {
	showLoad();
	$.ajax({
		type : 'GET',
		url : ctx + '/adminPage/user/detail',
		dataType : 'json',
		data : {
			id : id
		},
		success : function(data) {
			closeLoad();
			if (data.success) {
				var user = data.obj;
				$("#id").val(user.id); 
				$("#name").val(user.name);
				$("#pass").val(user.pass);
				$("#open").val(user.open);
				form.render();
				showWindow("编辑用户");
			}else{
				layer.msg(data.msg);
			}
		},
		error : function() {
			closeLoad();
			alert("出错了,请联系技术人员!");
		}
	});
}

function del(id){
	if(confirm("确认删除?")){
		showLoad();
		$.ajax({
			type : 'POST',
			url : ctx + '/adminPage/user/del',
			data : {
				id : id
			},
			dataType : 'json',
			success : function(data) {
				closeLoad();
				if (data.success) {
					location.reload();
				}else{
					layer.msg(data.msg)
				}
			},
			error : function() {
				closeLoad();
				alert("出错了,请联系技术人员!");
			}
		});
	}
}


