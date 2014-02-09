
//策略对比JS
$(function () {
    $.qicLoading({
        target: 'body',
        text: "努力加载中...",
        modal: false,
        width: 220,
        top: '35%',
        left:'40%',
        postion: "absolute",
        zIndex: 2000
    });
    window.constraChart = new Highcharts.Chart({
        chart: {
            renderTo: 'container',
            type: 'line'
        },
        colors:['#059af8','#a67ec7','#2bb431','#fb3118','#ef9c00'],
        title: {
            text: null
        },
        credits:{
            enabled:false,
            position: {
                align: 'left',
                x: 350,
                verticalAlign: 'bottom'
            }
        },
        legend: {
            enabled:true

        },

        xAxis: {
            type: 'datetime',
            min:minDate,  //0表示1月 以此类推
            max:maxDate,
            tickInterval :20*24*3600*1000,//X轴点间隔
            labels: {
                formatter: function() {
                    return Highcharts.dateFormat('%Y-%m-%d', this.value);
                }
            }
        },
        yAxis:  [
            {
                lineWidth:1,
                gridLineDashStyle: 'shortdash',
                max:maxYield,
                min:-maxYield,
                tickInterval:pre_Yield,
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        if (this.value >0) {
                            return '<span style="fill: red;">' + this.value + '%</span>';
                        }
                        else if(this.value <0){
                            return '<span style="fill: green;">' + -(this.value)+'%</span>';
                        }
                        else {
                            return this.value+"%";
                        }
                    }
                }
            }
        ],
        tooltip: {
            formatter: function() {
                return   this.series.name+'<br/><strong>日期:</strong>'+
                    Highcharts.dateFormat('%Y%m%d', this.x) +'<strong>收益率:</strong> '+ this.y+'%' ;
            }
        },
        plotOptions: {
            line: {
                dataGrouping: {
                    enabled: false
                }
            },
            series: {
                lineWidth:1,
                marker: {
                    radius: 1 //鼠标接触时 显示点的大小
                }

            }

        },
        series: strategys

    });
    $.qicLoading({remove: true});//移除loading。。。
});
