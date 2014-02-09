//研究机构
var orgData = [
    {
        text:'机构A--C',
        children:[
            {text:'爱建证券', 'data-orgId':'1093'},
            {text:'安邦证券', 'data-orgId':'10100899'},
            {text:'安信证券', 'data-orgId':'106035'},
            {text:'宝通证券', 'data-orgId':'10100875'},
            {text:'渤海证券', 'data-orgId':'106034'},
            {text:'财达证券', 'data-orgId':'1090'},
            {text:'财富证券', 'data-orgId':'1095'},
            {text:'财通证券', 'data-orgId':'10134'},
            {text:'长城证券', 'data-orgId':'106033'},
            {text:'长江证券', 'data-orgId':'101434'}
        ]
    },
    {
        text:'机构D--F',
        children:[
            {text:'大和证券', 'data-orgId':'10515'},
            {text:'大盛证券', 'data-orgId':'10104940'},
            {text:'大通证券', 'data-orgId':'10126'},
            {text:'大同证券', 'data-orgId':'103963'},
            {text:'德邦证券', 'data-orgId':'10133||108054'},
            {text:'德银证券', 'data-orgId':'10244'},
            {text:'第一创业证券', 'data-orgId':'106072'},
            {text:'东北证券', 'data-orgId':'102333'},
            {text:'东方证券', 'data-orgId':'106071'},
            {text:'东海证券', 'data-orgId':'106070'},
            {text:'东莞证券', 'data-orgId':'106068'},
            {text:'东吴证券', 'data-orgId':'107633'},
            {text:'东兴证券', 'data-orgId':'10884'},
            {text:'法巴证券', 'data-orgId':'10100905'},
            {text:'法兴证券', 'data-orgId':'10100907'},
            {text:'方正证券', 'data-orgId':'104161'},
            {text:'富邦证券', 'data-orgId':'10105072'},
            {text:'富昌证券', 'data-orgId':'10100761'}
        ]
    },
    {
        text:'机构G',
        children:[
            {text:'高华证券', 'data-orgId':'106066||106067'},
            {text:'高信证券', 'data-orgId':'10100821||10100869'},
            {text:'光大证券', 'data-orgId':'10100670||104059'},
            {text:'广发证券', 'data-orgId':'101506'},
            {text:'广州证券', 'data-orgId':'104472'},
            {text:'国都证券', 'data-orgId':'106065'},
            {text:'国海证券', 'data-orgId':'108486'},
            {text:'国金证券', 'data-orgId':'105738'},
            {text:'国联证券', 'data-orgId':'104493'},
            {text:'国盛证券', 'data-orgId':'10129'},
            {text:'国泰证券', 'data-orgId':'104516'},
            {text:'国信证券', 'data-orgId':'106031'},
            {text:'国元证券', 'data-orgId':'10100704||104988'}
        ]
    },
    {
        text:'机构H',
        children:[
            {text:'海通证券', 'data-orgId':'10416'},
            {text:'航空证券', 'data-orgId':'10161'},
            {text:'航天证券', 'data-orgId':'106030'},
            {text:'恒利证券', 'data-orgId':'10100841'},
            {text:'恒明珠证券', 'data-orgId':'10100806'},
            {text:'恒泰证券', 'data-orgId':'104272'},
            {text:'宏源证券', 'data-orgId':'10100677||102306'},
            {text:'宏远证券', 'data-orgId':'10105074'},
            {text:'泓福证券', 'data-orgId':'10100788'},
            {text:'华安证券', 'data-orgId':'104302'},
            {text:'华宝证券', 'data-orgId':'1084'},
            {text:'华创证券', 'data-orgId':'1083'},
            {text:'华福证券', 'data-orgId':'104011'},
            {text:'华富嘉洛证券', 'data-orgId':'10100694'},
            {text:'华林证券', 'data-orgId':'10158'},
            {text:'华龙证券', 'data-orgId':'104297'},
            {text:'华南证券', 'data-orgId':'10100847'},
            {text:'华融证券', 'data-orgId':'106036'},
            {text:'华泰联合证券', 'data-orgId':'104180'},
            {text:'华西证券', 'data-orgId':'103952'},
            {text:'华鑫证券', 'data-orgId':'104340'},
            {text:'辉立证券', 'data-orgId':'10100981'},
            {text:'汇富证券', 'data-orgId':'10100687'},
            {text:'汇业证券', 'data-orgId':'10100828'}
        ]
    },
    {
        text:'机构J--L',
        children:[
            {text:'江海证券', 'data-orgId':'104517'},
            {text:'江南证券', 'data-orgId':'104531'},
            {text:'金利丰证券', 'data-orgId':'10100799||102189'},
            {text:'金元证券', 'data-orgId':'1097'},
            {text:'开源证券', 'data-orgId':'104175'},
            {text:'凯基证券', 'data-orgId':'101070'},
            {text:'康和证券', 'data-orgId':'10100891'},
            {text:'里昂证券', 'data-orgId':'10170||104271'},
            {text:'联讯证券', 'data-orgId':'104171'},
            {text:'民生证券', 'data-orgId':'104169'},
            {text:'民族证券', 'data-orgId':'104179'},
            {text:'摩根大通(摩根大通证券(亚太)有限公司)', 'data-orgId':'10105238'}
        ]
    },
    {
        text:'机构M--S',
        children:[
            {text:'南华证券', 'data-orgId':'10397'},
            {text:'南京证券', 'data-orgId':'104042'},
            {text:'平安证券', 'data-orgId':'104149'},
            {text:'齐鲁证券', 'data-orgId':'104104'},
            {text:'群益证券', 'data-orgId':'10202'},
            {text:'日信证券', 'data-orgId':'106037'},
            {text:'瑞信证券', 'data-orgId':'10100825||10100876||104103'},
            {text:'瑞银证券', 'data-orgId':'104102'},
            {text:'山西证券', 'data-orgId':'103137'},
            {text:'上海证券', 'data-orgId':'104101'},
            {text:'申银万国', 'data-orgId':'10101837'},
            {text:'胜利证券', 'data-orgId':'10100676'},
            {text:'时富证券', 'data-orgId':'10100673'},
            {text:'世纪证券', 'data-orgId':'104534'}
        ]
    },
    {
        text:'机构T--X',
        children:[
            {text:'台新证券', 'data-orgId':'10105079'},
            {text:'太平洋证券', 'data-orgId':'10884'},
            {text:'天风证券', 'data-orgId':'10102417||104384'},
            {text:'同信证券', 'data-orgId':'104274'},
            {text:'万联证券', 'data-orgId':'10119'},
            {text:'西部证券', 'data-orgId':'107618'},
            {text:'西藏证券', 'data-orgId':'104459'},
            {text:'西南证券', 'data-orgId':'105188'},
            {text:'厦门证券', 'data-orgId':'104236'},
            {text:'湘财证券', 'data-orgId':'104098'},
            {text:'新时代证券', 'data-orgId':'10157'},
            {text:'信诚证券', 'data-orgId':'10100688'},
            {text:'信达证券', 'data-orgId':'104097'},
            {text:'兴业证券', 'data-orgId':'105045'}
        ]
    },
    {
        text:'机构Y--Z',
        children:[
            {text:'耀才证券', 'data-orgId':'102685'},
            {text:'银河证券', 'data-orgId':'104096||107726'},
            {text:'英大证券', 'data-orgId':'104275'},
            {text:'永丰金证券', 'data-orgId':'10105078'},
            {text:'永丰证券', 'data-orgId':'10100826'},
            {text:'元大证券', 'data-orgId':'10100892'},
            {text:'元富证券', 'data-orgId':'10100838||10512'},
            {text:'粤海证券', 'data-orgId':'104123'},
            {text:'招商证券', 'data-orgId':'10974'},
            {text:'浙商证券', 'data-orgId':'104094'},
            {text:'致富证券', 'data-orgId':'10100698'},
            {text:'中航证券', 'data-orgId':'104273'},
            {text:'中山证券', 'data-orgId':'104647'},
            {text:'中天证券', 'data-orgId':'104653'},
            {text:'中投证券', 'data-orgId':'107689'},
            {text:'中信金通证券', 'data-orgId':'1087'},
            {text:'中信证券', 'data-orgId':'104776'},
            {text:'中银财富', 'data-orgId':'104230'},
            {text:'中邮证券', 'data-orgId':'1098'},
            {text:'中原证券', 'data-orgId':'104229'},
            {text:'众成证券', 'data-orgId':'1082'}
        ]
    }

]