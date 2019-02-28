package cn.company.figo.http.result

/**
 * @author Weir.
 * @date   2019/2/28.
 */
class ListData<T> {

    /**
     * 列表数据
     */
    var content: ArrayList<T> = arrayListOf()
    /**
     * 总数据数量
     */
    var totalElements: Int = 0
    /**
     *  总页码数
     */
    var totalPages: Int = 0
    /**
     *  是否为最后一页
     */
    var last: Boolean = false
    /**
     *  是否为第一页
     */
    var first: Boolean = false
    /**
     *  当前实际返回数据条数
     */
    var numberOfElements: Int = 0
    /**
     *  请求期望返回数据条数
     */
    var size: Int = 0
    /**
     *  请求期望返回数据页码
     */
    var number: Int = 0
}