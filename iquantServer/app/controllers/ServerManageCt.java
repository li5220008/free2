package controllers;


import bussiness.strategy.IBackTestService;
import models.iquantCommon.StrageServerDto;
import play.mvc.Controller;

import javax.inject.Inject;
import java.util.List;

/**
 * 加入回测服务器
 * User: panzhiwei
 * Date: 13-5-28
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */

public class ServerManageCt extends Controller {
    @Inject
    static IBackTestService backTestService;

    public static void serverInfo() {
        render();
    }

    public static void addServer(StrageServerDto backTestServerDto) {
        if (backTestServerDto != null) {
            backTestServerDto.status = 0;
            backTestService.addServer(backTestServerDto);
        }
        serverList();
    }

    public static void serverList() {
        List<StrageServerDto> list = backTestService.findAllServer();
        render(list);
    }

    public static void delBackTestServer(int id) {
        backTestService.delBackTestServer(id);
        renderText("删除成功!");
    }

    public static void changeStatus(int id, int status) {
        backTestService.updateServerStatus(status, id);
    }
}
