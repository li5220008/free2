package job.common;

import play.jobs.Job;

/**
 * User: wenzhihong
 * Date: 13-2-20
 * Time: 下午12:14
 */
public class TestDataJob extends Job {
   /* @Override
    public void doJob() throws Exception {
        //TODO 修改计数
        int count = 10000;
        JPAPlugin.autoTxs = false;
        EntityManager em = JPA.em();
        em.getTransaction().begin();

        String[] tableArr = {"sale_department", "user_role", "user_info", "strategy_baseinfo",
                "user_stock_pool_collect", "user_stock_pool_discuss", "user_strategy_collect", "user_strategy_discuss",
                "user_strategy_order", "strategy_yield", "strategy_performance_qicore"};

        DBConfig dbConfig = DB.getDBConfig();
        Connection conn = dbConfig.getConnection();
        exec(conn, "SET foreign_key_checks = 0;");
        for (String t : tableArr) {
            exec(conn, "TRUNCATE TABLE " + t + ";");
        }
        exec(conn,"SET foreign_key_checks = 1;");

        //清掉股票池测试数据
        exec(conn,"DELETE FROM stp_stockpool WHERE stockpoolcode >= 10000;");
        exec(conn,"DELETE  FROM stp_dailyreturn WHERE StockPoolCode >= 10000;");
        exec(conn,"DELETE  FROM stp_sample WHERE StockPoolCode >= 10000;");

        Fixtures.loadModels("initTestData.yml");
        clearCommitBegin(em);

        List<SaleDepartment> sdList = Lists.newArrayListWithCapacity(10);
        //生成10个部门
        for (int i = 1; i <= 10; i++) {
            SaleDepartment d1 = new SaleDepartment();
            d1.name = "部门" + i;
            d1.save();
            sdList.add(d1);
        }

        //取角色
        List<RoleInfo> rList = Lists.newArrayListWithCapacity(3);
        rList.add(  RoleInfo.<RoleInfo>findById(1L) );

        Date applyDate = CommonUtils.parseDate("2013-02-08");
        Date sdate = CommonUtils.parseDate("2012-01-01");
        Date edate = CommonUtils.parseDate("2014-01-01");

        //用户跟权限信息
        for (int i = 1; i <= count; i++) {
            UserInfo u = new UserInfo();
            u.name = "name" + i;
            u.account="acc" + i;
            u.setPwdWithHash("123");
            u.phone = "1" + i;
            u.email = "email" + i + "@126.com";
            u.idcard = "card" + i;
            u.saleDep = sdList.get( i % sdList.size());
            u.capitalAccount = "capi" + i;
            u.address = "aa"+ i;
            u.post = "23";
            u.sdate = sdate;
            u.edate = edate;
            u.applyDate = applyDate;
            u.utype = 1;
            u.status = 10;
            u.roles = Sets.newHashSet(rList.get(i % rList.size()));
            u.save();

            if(i % 300 == 0){
                clearCommitBegin(em);
                System.out.println("生成300个用户...");
            }
        }
        clearCommitBegin(em);

        //生成1w策略信息
        Date upD = CommonUtils.parseDate("2013-01-01");
        Date downTime = CommonUtils.parseDate("2014-01-01");
        Date lookbackStime = CommonUtils.parseDate("2010-01-01");
        Date lookbackEtime = CommonUtils.parseDate("2012-01-01");

        List<UserInfo> uList = Lists.newArrayListWithCapacity(10);
        for (int i = 1; i < 10; i++) {
            uList.add(UserInfo.<UserInfo>findById(1L));
        }

        //日期
        String[] dateArr = new String[20];
        for (int i = 0; i < dateArr.length; i++) {
            dateArr[i] = DateFormatUtils.format(DateUtils.addDays(CommonUtils.parseDate("2010-03-01"), i), "yyyy-MM-dd");
        }

        for (int i = 1; i <= count; i++) {
            StrategyBaseinfo b1 = new StrategyBaseinfo();
            b1.stUuid = Codec.UUID();
            b1.name = "策略" + i;
            b1.tradeType = i % 4 + 1;
            b1.tradeVariety =1;
            b1.upTime = upD;
            b1.downTime = downTime;
            b1.provider = "提供者" + ( i % 10 + 1);
            b1.providerDesp = "描述" + i;
            b1.desp = "简介" + i;
            b1.lookbackStime = lookbackStime;
            b1.lookbackEtime = lookbackEtime;
            b1.status = i % 6 + 1;
            b1.upUser = uList.get( i % uList.size());
            b1.save();

            long id = b1.id;

            for (String d : dateArr) {
                exec(conn,String.format("INSERT INTO strategy_yield(strategy_id,update_time) VALUES ('%d','%s');", id, d));
            }

            if(i % 300 == 0){
                clearCommitBegin(em);
                System.out.println("生成300个策略信息...");
            }
        }
        clearCommitBegin(em);

        //100个策略
        List<StrategyBaseinfo> sbList = Lists.newArrayListWithCapacity(100);
        for (int i = 1; i <= 100; i++) {
            sbList.add(StrategyBaseinfo.<StrategyBaseinfo>findById(new Long(i)));
        }

        //用户策略
        for (int i = 1; i <= count; i++) { //用户
            StrategyBaseinfo strategyBaseinfo = sbList.get(i % sbList.size());
            Long sid = strategyBaseinfo.id;
            exec(conn,String.format("INSERT INTO qic_db.user_strategy_collect (uid,stid) VALUES ('%d','%s');", i, sid));
            exec(conn,String.format("INSERT INTO user_strategy_order(uid,stid,order_stime,order_etime) VALUES ('%d','%d','2012-03-01','2014-02-01');", i, sid));
            exec(conn,String.format("INSERT INTO user_strategy_discuss(uid,stid,dis_time,content,star) VALUES ('%d','%d','2012-01-01','内容','2');", i, sid));
            if(i % 300 == 0){
                dbConfig.getConnection().commit();
                clearCommitBegin(em);
            }
        }
        clearCommitBegin(em);

        stock(em, dbConfig);
        em.getTransaction().commit();
    }

    //股票池
    private void stock(EntityManager em, DBConfig dbConfig) throws SQLException {
        Connection conn = dbConfig.getConnection();
        List<StockInfo> stockList = StockInfo.find(null).fetch(10);
        Long[] idArr = new Long[stockList.size()];
        int index = 0;
        for (StockInfo s : stockList) {
            idArr[index] = s.securityId;
            index++;
        }

        //TODO 修改计数
        int count = 8000;
        String[] dateArr = new String[10];
        String tmp = "2013-02-1";
        for (int i = 0; i < dateArr.length; i++) {
            dateArr[i] = tmp + i;
        }

        for (long i = 10000; i <= 10000 + count; i++) {
            exec(conn,String.format("INSERT INTO stp_stockpool(StockPoolCode,StockPoolName) VALUES ('%d','%s');", i, "test"+i));

            for (String d : dateArr) {
                exec(conn,String.format("INSERT INTO stp_dailyreturn(StockPoolCode,TradingDate) VALUES ('%d','%s');", i, d));
            }

            for (Long id : idArr) {
                exec(conn,String.format("INSERT INTO stp_sample (StockPoolCode,SecurityID, ADJUSTDATE) VALUES ('%d','%d', '2010-01-01');", i, id));
            }

            for (int uid = 10; uid < 20; uid++) {
                exec(conn,String.format("INSERT INTO user_stock_pool_collect(uid,spid) VALUES ('%d', '%d');", uid, i));
                exec(conn,String.format("INSERT INTO user_stock_pool_discuss(uid,spid,star) VALUES ('%d','%d','2');", uid, i));
            }

            if (i % 50 == 0) {
                dbConfig.getConnection().commit();
                clearCommitBegin(em);
            }
        }
        clearCommitBegin(em);
    }

    private void clearCommitBegin(EntityManager em) {
        em.flush();
        em.clear();
        em.getTransaction().commit();
        em.getTransaction().begin();
    }

    private void exec(Connection conn, String sql)  {
        Statement statement = null;
        try {
            statement = conn.createStatement();
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {}
            }
        }
    }*/
}
