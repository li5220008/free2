package models.iquantCommon;

import com.google.gson.annotations.Expose;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Set;

/**
 * User: wenzhihong
 * Date: 12-11-7
 * Time: 上午9:44
 */

public class RoleInfo{
    @Expose
    public long id;
    @Expose
    public String name;
    @Expose
    public String desp;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_function",
        joinColumns = @JoinColumn(name = "rid"),
        inverseJoinColumns = @JoinColumn(name = "fid")
    )
    public Set<FunctionInfo> functions;
}
