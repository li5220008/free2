package protoc;

/**
 * User: 刘建力(liujianli@gtadata.com))
 * Date: 13-7-2
 * Time: 下午5:42
 * 功能描述:
 */
public class HttpBody {

    public Long length;
    public Object body;

   public HttpBody(){

    }
    public HttpBody(Object body){
       this.body = body;
    }
    public long getLength() {
        if (body == null) {
            return 0;
        } else {
            //待改进
            return String.valueOf(body).length();
        }
    }
}
