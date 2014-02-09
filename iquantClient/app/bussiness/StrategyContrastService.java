package bussiness;

import com.google.common.base.Joiner;
import models.iquantCommon.StrategyContrastDto;
import protoc.URILib;
import protoc.parser.ActionResult;

import java.util.ArrayList;
import java.util.List;

/**
 * 策略对比业务方法
 * User: liangbing
 * Date: 13-7-1
 * Time: 下午5:13
 */
public class StrategyContrastService extends BasicService {

    public static List<StrategyContrastDto> strategyContrast(String idArray[]) {
        List<StrategyContrastDto> strategyContrastlist = new ArrayList<StrategyContrastDto>();
        Joiner joiner = Joiner.on(",");
        String stids = joiner.join(idArray);
        String url_comp= URILib.STRATEGY_COMPARE_STRATEGY_LIST;
        ActionResult<List<StrategyContrastDto>> actionResult = remoteRequestService.getList(url_comp,StrategyContrastDto.class,stids);

        if(actionResult!=null){
            strategyContrastlist = actionResult.data;
        }

        return strategyContrastlist;
    }


    public static List<String> strategyContrastForPictrue(String idArray[]) {
        List<String> strings = new ArrayList<String>();

        Joiner joiner = Joiner.on(",");
        String stids = joiner.join(idArray);

        String url= URILib.STRATEGY_CONTRAST_CHART_INFO;

        ActionResult<List<String>> stringActionResult = remoteRequestService.getList(url,String.class,stids);

        if(stringActionResult!=null){
           strings = stringActionResult.data;
        }
        return  strings;
    }



}
