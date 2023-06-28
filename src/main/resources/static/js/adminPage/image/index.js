

function search() {
	$("input[name='pageNum']").val(1);
	$("#searchForm").submit();
}



function del(id) {
	if (confirm("确认删除?")) {
		var cmdId = guid();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/image/delImage',
			data: {
				id: id,
				cmdId: cmdId
			},
			dataType: 'json',
			success: function(data) {
				closeLoad();
				if (data.success) {
					showConsole(cmdId);
				} else {
					layer.msg(data.msg)
				}
			},
			error: function() {
				closeLoad();
				alert("出错了,请联系技术人员!");
			}
		});
	}
}

function pull() {
	$("#name").val("");
	form.render();

	layer.open({
		type: 1,
		title: "拉取镜像",
		area: ['400px', '300px'], // 宽高
		content: $('#windowDiv')
	});
}

function pullOver() {
	if ($("#name").val() == "") {
		layer.msg("名称为空");
		return;
	}

	startPullOver($("#name").val());
}

function pullOnline(name){
	if(confirm("确认拉取" + name + "?")){
		layer.close(searchIndex);
		startPullOver(name);
	}
}

function startPullOver(name){
	var cmdId = guid();
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/image/pullOver',
		data: {
			cmdId: cmdId,
			name: name
		},
		dataType: 'json',
		success: function(data) {
			closeLoad();
			if (data.success) {
				showConsole(cmdId);
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			closeLoad();
			alert("出错了,请联系技术人员!");
		}
	});
}

var searchIndex;
function openSearchImage() {
	$("#imageTable").html("");

	searchIndex = layer.open({
		type: 1,
		title: "搜索镜像",
		area: ['1000px', '650px'], // 宽高
		content: $('#searchDiv')
	});

}

function searchImage() {
	if ($("#imageName").val() == '') {
		layer.msg("未填写名称");
		return;
	}
	showLoad();
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/image/searchImage',
		data: {
			imageName: $("#imageName").val()
		},
		dataType: 'json',
		success: function(data) {
			closeLoad();
			if (data.success) {
				var list = data.obj;
				var html = '';
				for (var i = 0; i < list.length; i++) {
					var searchItem = list[i];
					
					html += `
					<tr>
						<td>
							${searchItem.name}
						</td>
						<td>
							${searchItem.isOfficial?"官方":""}
						</td>
						<td>
							${searchItem.description}
						</td>
						<td>
							<button class="layui-btn layui-btn-normal layui-btn-sm" onclick="pullOnline('${searchItem.name}')">拉取</button>
						</td>
					</tr>
					`;
				}
				
				$("#imageTable").html(html);
			} else {
				layer.msg(data.msg);
			}
		},
		error: function() {
			closeLoad();
			alert("出错了,请联系技术人员!");
		}
	});

}
