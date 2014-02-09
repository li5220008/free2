//收益率走势图 (日线)

$(function () {
    $.qicLoading({
        target: 'body',
        text: "努力加载中...",
        modal: false,
        width: 220,
        top: '29%',
        left:'28%',
        postion: "absolute",
        zIndex: 2000
    });

//日线图
    window.dailyChart = new Highcharts.Chart({
        chart: {
            renderTo: 'day_trategy_container',
            type: 'areaspline'
          //  spacingRight: 20

        },
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
            enabled:false,
          // align:'right',
            verticalAlign:'top',
            x:300,
            y:-10

        },
        xAxis: {
            min:dayminDate,  //0表示1月 以此类推
            max:daymaxDate,
            tickInterval :30*24*3600*1000,//X轴点间隔
           // showFirstLabel: false,
            labels: {
                //align: 'left',
                formatter: function() {
                    return Highcharts.dateFormat('%Y-%m-%d', this.value);
                }
            }
        },
        yAxis:  [
            {
                lineWidth:1,
                gridLineColor: '#2B2B2B',
                gridLineDashStyle: 'shortdash',
                max:daymaxYield,
                min:-daymaxYield,
                opposite: true,
                tickInterval:day_pre_Yield,
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        if (this.value >0) {
                            return '<span style="fill: red;">' + this.value  + '%</span>';
                        }
                        else if(this.value <0){
                                return '<span style="fill: green;">' + -(this.value)+'%</span>';
                            }
                        else {
                                return this.value+"%";
                            }
                    }
                }
            },
            {

                lineWidth:1,
                max:daymaxYield,
                min:-daymaxYield,
                tickInterval:day_pre_Yield,
                title: {
                    text:null
                },
                labels: {
                    formatter: function() {
                        if (this.value >0) {
                            return '<span style="fill: red;">' + this.value  + '%</span>';
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
                return '<strong>日期:</strong>'+ Highcharts.dateFormat('%Y%m%d', this.x) + '<br/><strong>收益率:</strong>'+(this.y).toFixed(2)+"%";
            }
        },
        series:
            [{  name:'',
                data:[],
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                        [0, '#6390b4'],
                        [1, '#264877']
                    ]
                },
                lineWidth: 2,
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            radius: 5
                        }
                    }
                },
                shadow: false,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null,
                lineColor: '#53b6f4',
                color:'#53b6f4'

            }, { name:'',
                data:[],
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                        [0, '#f2b758'],
                        [1, '#96631b']
                    ]
                },
                lineWidth: 2,
                marker: {
                    enabled: false,
                    states: {
                        hover: {
                            enabled: true,
                            radius: 5
                        }
                    }
                },
                shadow: false,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null,
                lineColor: '#f9b65f',
                color:'#f9b65f'
            } ]


    });
});




//收益率走势图 (周线)
$(function () {
    window.weeklyChart = new Highcharts.Chart({
        chart: {
            renderTo: 'week_trategy_container',
            type: 'areaspline'
        },
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
            enabled:false

        },

        xAxis: {
            min:weekminDate,  //0表示1月 以此类推
            max:weekmaxDate,
            tickInterval :30*24*3600*1000,//X轴点间隔
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
                max:weekmaxYield,
                min:-weekmaxYield,
                opposite: true,
                tickInterval:week_pre_Yield,
                title: {
                    text: null
                },
                labels: {
                    formatter: function() {
                        if (this.value >0) {
                            return '<span style="fill: red;">' + this.value + '%</span>';
                        }
                        else if(this.value <0){
                                return '<span style="fill: green;">' + -(this.value) + '%</span>';
                            }
                        else {
                                return this.value+"%";
                            }
                    }
                }
            },
            {
                lineWidth:1,
                gridLineDashStyle: 'shortdash',
                max:weekmaxYield,
                min:-weekmaxYield,
                tickInterval:week_pre_Yield,
                title: {
                    text:null
                },
                labels: {
                  //  formatter: function() {
                   //     return (this.value/100*weekInitCapital+weekInitCapital).toFixed(0)
                   // }
                    formatter: function() {
                        if (this.value >0) {
                            return '<span style="fill: red;">' + this.value + '%</span>';
                        }
                        else if(this.value <0){
                            return '<span style="fill: green;">' + -(this.value) + '%</span>';
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
                return '<strong>日期:</strong>'+ Highcharts.dateFormat('%Y%m%d', this.x) + '<br/><strong>收益率:</strong>'+(this.y).toFixed(2)+"%";
            }
        },
        series:
            [{  name:'',
                data:[],
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                        [0, '#6390b4'],
                        [1, '#264877']
                        //[1, 'rgba(2,0,0,0)']
                    ]
                },
                lineWidth: 2,
                marker: {
                    enabled: false,
                    //symbol: 'circle',
                    states: {
                        hover: {
                            enabled: true,
                            radius: 5
                        }
                    }
                },
                shadow: false,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null,
                lineColor: '#53b6f4',
                color:'#53b6f4'

            }, { name:'',
                data:[],
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                        // [0, '#41aef3'],
                        [0, '#f2b758'],
                        [1, '#96631b']
                        //[1, 'rgba(2,0,0,0)']
                    ]
                },
                lineWidth: 2,
                marker: {
                    enabled: false,
                    //symbol: 'circle',
                    states: {
                        hover: {
                            enabled: true,
                            radius: 5
                        }
                    }
                },
                shadow: false,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null,
                lineColor: '#f9b65f',
                color:'#f9b65f'
            } ]

    });
    $.qicLoading({remove: true});//移除loading。。。
});