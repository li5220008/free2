package util;

/**
 * 分页信息
 * User: liangbing
 * Date: 12-11-9
 * Time: 上午10:12
 */
public class Page {
    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int SHOW_PAGE_NUMBER = 3; //当前页前后要显示的页的个数

    public int rowTotal;// 总记录数
    public int pageSize = DEFAULT_PAGE_SIZE;// 每页记录数

    public int pageNo;// 当前页码

    public int totalPage;// 总页数
    public int beginIndex;//起始记录下标
    public int endIndex;//截止记录下标

    /**
     * 使用总记录数、当前页码构造
     *
     * @param totalRow
     * @param pageNo    页码，从1开始
     */
    public Page(int totalRow, int pageNo) {
        this(totalRow, pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 使用总记录数、当前页码和每页记录数构造
     *
     * @param totalRow
     * @param pageNo    页码，从1开始
     * @param pageSize  每页记录数
     */
    public Page(int totalRow, int pageNo, int pageSize) {
        if(totalRow < 0){
            totalRow = 0;
        }
        if(pageNo <=0){
            pageNo = 1;
        }
        this.rowTotal = totalRow;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        calculate();
    }

    /**
     * 之前页是否有省略号
     * @return
     */
    public boolean preHasEllipsis(){
        int[] arr = curPagePreShow();
        if (arr.length == 0) {
            return false;
        }else{
            return arr[0] > 1;
        }
    }

    /**
     * 返回当前页之前要显示的页.
     * 如当前页是  1, 则返回 []
     * 如当前页是  2, 则返回 [1]
     * 如当前页是  3, 则返回 [1, 2]
     * 如当前页是  4, 则返回 [1, 2, 4]
     * 如当前页是  5, 则返回 [2, 3, 4]
     * @return
     */
    public int[] curPagePreShow(){
        if(pageNo <= 1){
            return new int[0];
        }

        int t = pageNo - SHOW_PAGE_NUMBER;
        if(t < 1){
            t = 1;
        }
        int[] arr = new int[pageNo - t];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = t + i;
        }

        return arr;
    }

    public boolean postHasEllipsis(){
        int[] arr = curPagePostShow();
        if(arr.length == 0){
            return false;
        }else {
            return arr[arr.length - 1] + 1 < totalPage;
        }
    }

    /**
     * 返回当前页之前要显示的页
     * 如当前页是 1, 总页数是 1. 则返回 []
     * 如当前页是 1, 总页数是 2. 则返回 [2]
     * 如当前页是 3, 总页数是 5. 则返回 [4,5]
     * 如当前页是 3, 总页数是 6. 则返回 [4,5,6]
     * 如当前页是 3, 总页数是 7. 则返回 [4,5,6]
     * @return
     */
    public int[] curPagePostShow(){
        int index = pageNo;
        if(totalPage <= 1){
            return new int[0];
        }
        int t = pageNo + SHOW_PAGE_NUMBER;
        if(t > totalPage ){
            t = totalPage;
        }
        if(pageNo>totalPage){
            index = totalPage;
        }
        int[] arr = new int[t - index];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = pageNo + i + 1;
        }

        return arr;
    }

    private void calculate() {
        totalPage = rowTotal / pageSize + ((rowTotal % pageSize) > 0 ? 1 : 0);

     /* *//*  if (pageNo > totalPage) {
            pageNo = totalPage;
        } else*/
        if (pageNo < 1) {
            pageNo = 1;
        }

        beginIndex = (pageNo - 1) * pageSize;
        if(beginIndex < 0){
            beginIndex = 0;
        }
        endIndex = beginIndex + pageSize;
        if (endIndex > rowTotal) {
            endIndex = rowTotal;
        }
    }
}
