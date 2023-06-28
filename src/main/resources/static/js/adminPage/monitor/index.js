var element;
var netList = [];

$(function(){
	netDiv = echarts.init(document.getElementById('netDiv'));
	
	layui.use('element', function(){
		  element = layui.element; // Tab的切换功能，切换事件监听等，需要依赖element模块
	});

	setInterval(() => {
		load();
	}, 1000);
	
	initEchart();
	network();
	
	
	// 安装docker
	/*
	if ($("#hasDocker").val() == 0) {
		layer.confirm('本机未安装docker,无法正常使用,是否立刻自动安装docker?', function(index) {
			//do something
			var cmdId = guid();
			$.ajax({
				type: 'POST',
				url: ctx + '/adminPage/docker/installDocker',
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
		});
	}
	*/
})



function load(){

	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/monitor/check',
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				var monitorInfo = data.obj;
				element.progress('cpu', monitorInfo.cpuRatio);
				element.progress('mem', monitorInfo.memRatio);
				
				$("#memContent").html("( 已用:" + monitorInfo.usedMemory + " / 总共:" + monitorInfo.totalMemorySize + " )");
				$("#cpuCount").html("( 核心数:" + monitorInfo.cpuCount + " / 线程数:" + monitorInfo.threadCount+ " )");
				
			
			}
		},
		error : function() {
			//layer.alert(commonStr.errorInfo);
		}
	});
}


function network(){
	
	$.ajax({
		type : 'POST',
		url : ctx + '/adminPage/monitor/network',
		dataType : 'json',
		success : function(data) {
			if (data.success) {
				var networkInfo = data.obj;
				netList.push(networkInfo);
				
				if(netList.length > 10){
					netList.splice(0, 1); 
				}
				
				initEchart();
				network();
			}
		},
		error : function() {
			//layer.alert(commonStr.errorInfo);
		}
	});
	
}

function initEchart(){
	
	var time = [];
	var send = [];
	var receive = [];
	for(let i=0; i<netList.length; i++){
		time.push(netList[i].time);
		send.push(netList[i].send);
		receive.push(netList[i].receive);
	}
	
	var option = {
		title: {
			text: '网速统计',
			left: 'left'
		},
		tooltip: {
			trigger: 'axis',
			formatter(params) {
				return `
	            	发送: ${params[0].value} kB/s<br>
	            	接收: ${params[1].value}  kB/s
	            `;
			},
		},
	    legend: {
            data:[ '发送', '接收']
        },
		xAxis: {
			name: '时间',
			type: 'category',
			data: time
		},
		yAxis: {
			type: 'value',
			axisLabel: {
	           formatter: '{value} kB/s'
	        }
		},
		series: [{
			name: '发送',
			data: send,
			type: 'line',
			showBackground: true,
			backgroundStyle: {
				color: 'rgba(108,80,243,0.3)'
			}
		}, {
			name: '接收',
			data: receive,
			type: 'line',
			showBackground: true,
			backgroundStyle: {
				color: 'rgba(0,202,149,0.3)'
			}
		}

		]
	};

	netDiv.setOption(option);
}


