<%--
  Created by IntelliJ IDEA.
  User: LiveAn
  Date: 2017/9/14
  Time: 11:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>usrlocmap</title>
</head>
<body>
<!-- 为ECharts准备一个具备大小（宽高）的Dom -->
<div id="main" style="height:400px"></div>
<!-- ECharts单文件引入 -->
<script src="http://echarts.baidu.com/build/dist/echarts-all.js"></script>
<script src="js/jquery.min.js"></script>
<script type="text/javascript">
    // 基于准备好的dom，初始化echarts图表
    var myChart = echarts.init(document.getElementById('main'));
    var datas;

    loadDATA();

    var option = {
        title: {
            text: '用户活跃度区域分布',
            //subtext: '纯属虚构',
            left: 'center'
        },
        tooltip: {
            trigger: 'item'
        },
        legend: {
            orient: 'vertical',
            left: 'left',
            data:['PV']
        },
        visualMap: {
            min: 0,
            max: 2500,
            left: 'left',
            top: 'bottom',
            text: ['高','低'],           // 文本，默认为数值文本
            calculable: true
        },
        toolbox: {
            show: true,
            orient: 'vertical',
            left: 'right',
            top: 'center',
            feature: {
                dataView: {readOnly: false},
                restore: {},
                saveAsImage: {}
            }
        },
        series: [
            {
                name: 'PV',
                type: 'map',
                mapType: 'china',
                roam: false,
                label: {
                    normal: {
                        show: true
                    },
                    emphasis: {
                        show: true
                    }
                },
                data:[
                    {name: '北京',value: datas[0].value},
                    {name: '天津',value: datas[1].value},
                    {name: '上海',value: datas[2].value},
                    {name: '重庆',value: datas[3].value},
                    {name: '河北',value: datas[4].value },
                    {name: '河南',value: datas[5].value},
                    {name: '云南',value: datas[6].value},
                    {name: '辽宁',value: datas[7].value},
                    {name: '黑龙江',value: datas[8].value},
                    {name: '湖南',value: datas[9].value },
                    {name: '安徽',value: datas[10].value },
                    {name: '山东',value: datas[11].value },
                    {name: '新疆',value: datas[12].value },
                    {name: '江苏',value: datas[13].value },
                    {name: '浙江',value: datas[14].value },
                    {name: '江西',value: datas[15].value },
                    {name: '湖北',value: datas[16].value },
                    {name: '广西',value: datas[17].value },
                    {name: '甘肃',value: datas[18].value },
                    {name: '山西',value: datas[19].value },
                    {name: '内蒙古',value: datas[20].value },
                    {name: '陕西',value: datas[21].value },
                    {name: '吉林',value: datas[22].value },
                    {name: '福建',value: datas[23].value },
                    {name: '贵州',value: datas[24].value },
                    {name: '广东',value: datas[25].value },
                    {name: '青海',value: datas[26].value },
                    {name: '西藏',value: datas[27].value },
                    {name: '四川',value: datas[28].value },
                    {name: '宁夏',value: datas[29].value },
                    {name: '海南',value: datas[30].value },
                    {name: '台湾',value: datas[31].value },
                    {name: '香港',value: datas[32].value },
                    {name: '澳门',value: datas[33].value }
                ]
            }

        ]
    };

    // 为echarts对象加载数据
    //loadDATA(option);
    myChart.setOption(option);

    function randomData() {
        var a = datas[0].value;
        //var a =10;
        return a;
    }
    function getdata(i) {
        var a = datas[i].value;
        //var a =10;
        return a;
    }
    function loadDATA(){
        $.ajax({
            type : "post",
            async : false, //异步执行
            url : "usrloc.do",
            data : {},
            dataType : "json", //返回数据形式为json
            success : function(result) {
                if(result){
                    datas = result;
                    //option.series[0].data.push(result);
                    alert(datas[0].value);
                }

            }
        });
    }

</script>
</body>
</html>
