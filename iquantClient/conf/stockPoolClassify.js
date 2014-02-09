//股票池分类信息配制文件
var stockPoolConfig =
    [
        {
            "name":"投资风格",
            "code":"I01",
            "pcode":"I01",
            "children":[
                {
                    "name":"价值型",
                    "code":"I0101",
                    "pcode":"I01"
                },
                {
                    "name":"成长型",
                    "code":"I0102",
                    "pcode":"I01"
                },
                {
                    "name":"平衡型",
                    "code":"I0103",
                    "pcode":"I01"
                }

            ]
        },
        {
            "name":"风险偏好",
            "code":"I02",
            "pcode":"I02",
            "children":[
                {
                    "name":"保守型",
                    "code":"I0202",
                    "pcode":"I02"
                },
                {
                    "name":"稳健型",
                    "code":"I0203",
                    "pcode":"I02"
                },
                {
                    "name":"积极型",
                    "code":"I0204",
                    "pcode":"I02"
                },
                {
                    "name":"激进型",
                    "code":"I0205",
                    "pcode":"I02"
                }
            ]
        },
        {
            "name":"行业概念",
            "code":"I03",
            "pcode":"I03",
            "children":[
                {
                    "name":"行业类",
                    "code":"I0301",
                    "pcode":"I03"
                },
                {
                    "name":"概念类",
                    "code":"I0302",
                    "pcode":"I03"
                }

            ]
        },
        {
            "name":"周期热股",
            "code":"I04",
            "pcode":"I04",
            "children":[
                {
                    "name":"日",
                    "code":"I0401",
                    "pcode":"I04"
                },
                {
                    "name":"周",
                    "code":"I0402",
                    "pcode":"I04"
                },
                {
                    "name":"月",
                    "code":"I0403",
                    "pcode":"I04"
                },
                {
                    "name":"季",
                    "code":"I0404",
                    "pcode":"I04"
                },
                {
                    "name":"年",
                    "code":"I0405",
                    "pcode":"I04"
                }
            ]
        },
        {
            "name":"策略类",
            "code":"I05",
            "pcode":"I05"
        },
        {
            "name":"百家争鸣",
            "code":"I06",
            "pcode":"I06"
        }
    ]