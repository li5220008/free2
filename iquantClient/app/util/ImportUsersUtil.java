package util;

import bussiness.SaleDepartmentService;
import models.iquantCommon.SaleDepartMentDto;
import models.iquantCommon.UserInfoDto;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import play.data.validation.Validation;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-6
 * Time: 下午4:41
 * 功能描述:
 */
public class ImportUsersUtil {
    private static List<SaleDepartMentDto> saleDepartMentDtoList = null;
    /**
     * 解析excel文件  引入POI
     * @param excelFile
     * @return
     */
    public static List<UserInfoDto> parseUserFromExcel(File excelFile)throws Exception{

        saleDepartMentDtoList = SaleDepartmentService.findAll();
        InputStream is = new FileInputStream(excelFile);
        Workbook workbook = WorkbookFactory.create(is);
        UserInfoDto userInfoDto = null;
        List<UserInfoDto> list = new ArrayList<UserInfoDto>();
        // 循环工作表Sheet
        for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
            Sheet hssfSheet = workbook.getSheetAt(numSheet);
            //添加表头校验2013-03-12 和数据有效性校验
            if (hssfSheet == null ) {
                continue;
            }
            if(numSheet == 0 && !validateSheetHead(hssfSheet.getRow(1))){
                continue;
            }
            // 循环行Row
            int rowNum = numSheet == 0 ? 2: 0;
            for (; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                Row hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow == null) {
                    continue;
                }
                userInfoDto = new UserInfoDto();
                // 循环列Cell
                // 0用户名 1登入账号 2密码 3联系电话 4 email 5身份证号码  6营业部 7联系地址 8 邮编 9 资金账号
                Cell xh = hssfRow.getCell(0);
                if (xh == null || StringUtils.isBlank(getStringValue(xh))) {
                    continue;
                }
                userInfoDto.name =getStringValue(xh);
                if( userInfoDto.name.length()<2 || userInfoDto.name.length() >10){
                    continue;
                }
                xh = hssfRow.getCell(1);
                if (xh == null) {
                    continue;
                }
                userInfoDto.account=getStringValue(xh);
                if( userInfoDto.account.length()<4 || userInfoDto.account.length() >16){
                    continue;
                }
                xh = hssfRow.getCell(2);
                if (xh == null) {
                    continue;
                }
                userInfoDto.password = getStringValue(xh);
                if( userInfoDto.password.length()<6 || userInfoDto.password.length() >15){
                    continue;
                }
                xh = hssfRow.getCell(3);
                if (xh != null) {
                    //电话号码 可以为空
                    userInfoDto.phone =getStringValue(xh);
                    if(userInfoDto.phone.length() >30){
                        continue;
                    }
                }
                //email
                xh = hssfRow.getCell(4);
                if (xh == null) {
                    continue;
                }
                userInfoDto.email =getStringValue(xh);
                if (! Validation.email("", userInfoDto.email).ok) {
                    continue;
                }
                //身份证
                xh = hssfRow.getCell(5);
                if (xh != null) {
                    userInfoDto.idCard = getStringValue(xh);
                    if( userInfoDto.idCard.length() >20){
                        continue;
                    }
                }
                //营业部
                xh = hssfRow.getCell(6);
                if (xh != null) {
                    long depId = getDepartmentIdFromString(getStringValue(xh));
                    if(depId > 0){
                      userInfoDto.saleDept = String.valueOf(depId);
                    }
                }
                //联系地址
                xh = hssfRow.getCell(7);
                if (xh != null) {
                    userInfoDto.address= getStringValue(xh);
                }
                //邮编
                xh = hssfRow.getCell(8);
                if (xh != null) {
                    userInfoDto.postCode = getStringValue(xh);
                    if(!StringUtils.isNumeric(userInfoDto.postCode)){
                        continue;
                    }
                }
                //资金账号
                xh = hssfRow.getCell(9);
                if (xh != null) {
                    userInfoDto.capitalAccount = getStringValue(xh);
                    if( userInfoDto.capitalAccount.length() >40){
                        continue;
                    }
                }
                list.add(userInfoDto);
            }
        }
        return list;
    }
 //表头校验
    private static boolean validateSheetHead(Row row){
        if(row == null ){
            return false;
        }
        //10至20个字符
        if(!"用户名".equals(getStringValue(row.getCell(0)))){
            return false;
        }
        if(!"登入账号".equals(getStringValue(row.getCell(1)))){
            return false;
        }
        if(!"密码".equals(getStringValue(row.getCell(2)))){
            return false;
        }
        if(!"联系电话".equals(getStringValue(row.getCell(3)))){
            return false;
        }
        if(!"邮件地址".equals(getStringValue(row.getCell(4)))){
            return false;
        }
        if(!"身份证号码".equals(getStringValue(row.getCell(5)))){
            return false;
        }
        if(!"营业部门".equals(getStringValue(row.getCell(6)))){
            return false;
        }
        if(!"联系地址".equals(getStringValue(row.getCell(7)))){
            return false;
        }
        if(!"邮编".equals(getStringValue(row.getCell(8)))){
            return false;
        }
        if(!"资金账号".equals(getStringValue(row.getCell(9)))){
            return false;
        }
        return true;
    }
    private static String getStringValue(Cell xssfCell){
        if(xssfCell.getCellType() == xssfCell.CELL_TYPE_BOOLEAN){
            return String.valueOf( xssfCell.getBooleanCellValue());
        }else if(xssfCell.getCellType() == xssfCell.CELL_TYPE_NUMERIC){
            return String.valueOf( (int)(xssfCell.getNumericCellValue()));
        }else{
            return String.valueOf( xssfCell.getStringCellValue());
        }
    }
    public static  long getDepartmentIdFromString(String dpName){
        if(dpName == null || "".equals(dpName.trim())){
            return -1;
        }
        for(SaleDepartMentDto saleDepartMentDto:saleDepartMentDtoList){
            if(saleDepartMentDto.name.trim().equals(dpName.trim())){
                return saleDepartMentDto.id;
            }
        }
        return -1;
    }
}
