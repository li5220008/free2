package models.iquantCommon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 12-12-4
 * Time: 下午1:52
 * 功能描述: 权限菜单信息
 */
public class FunctionInfoDto extends BaseDtoSupport implements Cloneable{
    @Expose
    @SerializedName(value = "text")
    public String name;
    @Expose(serialize = false)
    public String action;
    @Expose(serialize = false)
    public String code;
    @Expose
    public long pid;
    //ligerUI Tree中根据ischecked值来确定当前菜单是否处于选中状态
    @SerializedName(value = "ischecked")
    public boolean isChecked;

    @SerializedName(value = "children")
    public List<FunctionInfoDto> subs = null;
    @Expose
    public int status;
    /**
     * 复制对像
     * @param src
     * @return
     */
    public static FunctionInfoDto copy(FunctionInfoDto src){
        FunctionInfoDto copyOfSystemTree = new FunctionInfoDto();
        copyOfSystemTree.id = src.id;
        copyOfSystemTree.subs = new ArrayList<FunctionInfoDto>(src.subs);
        copyOfSystemTree.action = src.action;
        copyOfSystemTree.pid = src.pid;
        copyOfSystemTree.name = src.name;
        return copyOfSystemTree;
    }


}
