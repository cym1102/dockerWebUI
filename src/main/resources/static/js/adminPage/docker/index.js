var load;
$(function() {
	
})


function search() {
	$("input[name='pageNum']").val(1);
	$("#searchForm").submit();
}


function start(id) {
	if (confirm("确认启动?")) {
		var cmdId = guid();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/docker/start',
			data: {
				id: id,
				cmdId: cmdId
			},
			dataType: 'json',
			success: function(data) {
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
}



function stop(id) {
	if (confirm("确认停止?")) {
		var cmdId = guid();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/docker/stop',
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
					layer.msg(data.msg);
				}
			},
			error: function() {
				closeLoad();
				alert("出错了,请联系技术人员!");
			}
		});
	}
}



function add() {
	$("#name").val("");
	$("#image").val("");
	$("#localPort").val("");
	$("#dockerPort").val("");
	$("#localDir").val("");
	$("#dockerDir").val("");

	$("#portTable").html("");
	$("#dirTable").html("");
	$("#paramTable").html("");
	$("#cmdTable").html("");

	$("#env").val("");
	$("#param").val("");
	$("#member").val("");
	$("#cpu").val("");
	$("#cmd").val("");

	$("#autoStart").prop("checked", false);

	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/docker/getAllImages',
		dataType: 'json',
		success: function(data) {
			if (data.success) {
				var html = '';
				for (let i = 0; i < data.obj.length; i++) {
					var image = data.obj[i];
					html += '<option>' + image.repoTags.join(" ") + '</option>';
				}
				$("#image").html(html);
				form.render();

				layer.open({
					type: 1,
					title: "添加docker容器",
					area: ['700px', '90%'], // 宽高
					content: $('#windowDiv')
				});
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

function addOver(dockerCmd) {
	var cmdId = guid();
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/docker/addOver',
		data: {
			cmdId: cmdId,
			dockerCmd: dockerCmd,
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

function buildDockerCmd() {
	
	if ($("#name").val() == '') {
		layer.msg("名称未填写完整");
		return;
	}
	if ($("#image").val() == '') {
		layer.msg("镜像未填写完整");
		return;
	}
	
	
	var ports = [];
	$(".port").each(function() {
		var localPort = $(this).children("td").get(0).innerHTML;
		var dockerPort = $(this).children("td").get(1).innerHTML;

		ports.push({ localPort: localPort, dockerPort: dockerPort });
	})

	var dirs = [];
	$(".dir").each(function() {
		var localDir = $(this).children("td").get(0).innerHTML;
		var dockerDir = $(this).children("td").get(1).innerHTML;

		dirs.push({ localDir: localDir, dockerDir: dockerDir });
	})

	var params = [];
	$(".param").each(function() {
		var paramName = $(this).children("td").get(0).innerHTML;
		var paramValue = $(this).children("td").get(1).innerHTML;

		params.push({ paramName: paramName, paramValue: paramValue });
	})
	
	var cmds = [];
	$(".cmd").each(function() {
		var cmdName = $(this).children("td").get(0).innerHTML;
		var cmdValue = $(this).children("td").get(1).innerHTML;

		cmds.push({ cmdName: cmdName, cmdValue: cmdValue });
	})


	var cmd = "docker run -itd --name=" + $("#name").val();

	if ($("#autoStart").prop("checked")) {
		cmd += " --restart=on-failure:3";
	}
	if ($("#root").prop("checked")) {
		cmd += " --privileged=true";
	}
	
	for (let i = 0; i < ports.length; i++) {
		cmd += " -p " + ports[i].localPort + ":" + ports[i].dockerPort;
	}
	for (let i = 0; i < dirs.length; i++) {
		cmd += " -v " + dirs[i].localDir + ":" + dirs[i].dockerDir;
	}
	for (let i = 0; i < params.length; i++) {
		cmd += " -e " + params[i].paramName + "=\"" + params[i].paramValue + "\"";
	}
	for (let i = 0; i < cmds.length; i++) {
		cmd += " --" + cmds[i].cmdName + "=" + cmds[i].cmdValue;

	}

	if ($("#cpu").val() != '') {
		cmd += " --cpu-shares=" + $("#cpu").val();
	}
	if ($("#member").val() != '') {
		cmd += " --memory=" + $("#member").val() + "M";
	}


	cmd += " " + $("#image").val();
	cmd += " " + $("#command").val();


	layer.prompt({
		formType: 2,
		value: cmd,
		title: '执行命令',
		area: ['400px', '300px'] //自定义文本域宽高
	}, function(value, index, elem) {
		//alert(value); //得到value
		layer.close(index);
		
		addOver(value);
	});

}

function run(){
	layer.prompt({
		formType: 2,
		value: "",
		title: '执行命令',
		area: ['400px', '300px'] //自定义文本域宽高
	}, function(value, index, elem) {
		//alert(value); //得到value
		layer.close(index);
		
		addOver(value);
	});
}

function del(id) {
	if (confirm("确认删除?")) {
		var cmdId = guid();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/docker/del',
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

function uninstall() {
	if (confirm("是否确认卸载?")) {
		var cmdId = guid();
		$.ajax({
			type: 'POST',
			url: ctx + '/adminPage/docker/uninstallOver',
			data: {
				cmdId: cmdId
			},
			dataType: 'json',
			success: function(data) {
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

}


function logs(id) {
	var cmdId = guid();
	$.ajax({
		type: 'POST',
		url: ctx + '/adminPage/docker/logs',
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
				layer.msg(data.msg);
			}
		},
		error: function() {
			closeLoad();
			alert("出错了,请联系技术人员!");
		}
	});
}


function addPort() {
	var localPort = $("#localPort").val();
	var dockerPort = $("#dockerPort").val();

	if (localPort == '' || dockerPort == '') {
		layer.msg("端口未填写完整");
		return;
	}

	var id = guid();
	var html = `
		<tr id="${id}" class="port">
			<td>${localPort}</td>
			<td>${dockerPort}</td>
			<td><a href="javascript:delId('${id}')" class="red">删除</a></td>
		</tr>
	`;

	$("#portTable").append(html);
	$("#localPort").val("");
	$("#dockerPort").val("");
}


function addDir() {
	var localDir = $("#localDir").val();
	var dockerDir = $("#dockerDir").val();

	if (localDir == '' || dockerDir == '') {
		layer.msg("目录未填写完整");
		return;
	}

	var id = guid();
	var html = `
		<tr id="${id}" class="dir">
			<td>${localDir}</td>
			<td>${dockerDir}</td>
			<td><a href="javascript:delId('${id}')" class="red">删除</a></td>
		</tr>
	`;

	$("#dirTable").append(html);
	$("#localDir").val("");
	$("#dockerDir").val("");
}



function addParam() {
	var paramName = $("#paramName").val();
	var paramValue = $("#paramValue").val();

	if (paramName == '' || paramValue == '') {
		layer.msg("参数未填写完整");
		return;
	}

	var id = guid();
	var html = `
		<tr id="${id}" class="param">
			<td>${paramName}</td>
			<td>${paramValue}</td>
			<td><a href="javascript:delId('${id}')" class="red">删除</a></td>
		</tr>
	`;

	$("#paramTable").append(html);
	$("#paramName").val("");
	$("#paramValue").val("");
}

function addCmd() {
	var cmdName = $("#cmdName").val();
	var cmdValue = $("#cmdValue").val();

	if (cmdName == '' || cmdValue == '') {
		layer.msg("参数未填写完整");
		return;
	}

	var id = guid();
	var html = `
		<tr id="${id}" class="cmd">
			<td>${cmdName}</td>
			<td>${cmdValue}</td>
			<td><a href="javascript:delId('${id}')" class="red">删除</a></td>
		</tr>
	`;

	$("#cmdTable").append(html);
	$("#cmdName").val("");
	$("#cmdValue").val("");
}


function delId(id) {
	$("#" + id).remove();
}

var sshId = null;
var sshIndex = null;
function ssh(id){
	sshId = id;
	
	sshIndex = layer.open({
		type: 1,
		area: ['400px', '300px;'], // 宽高
		title: "终端命令",
		resize: false,
		content: $('#sshDiv')
	});
}

function sshOver(){
	layer.close(sshIndex);
	
	var cmd = $("#cmd").val();
	layer.open({
		type: 2,
		area: ['80%', '80%'], // 宽高
		title: "终端",
		resize: false,
		content: ctx + "/adminPage/ssh?id=" + sshId + "&cmd=" + encodeURIComponent(cmd)
	});
}