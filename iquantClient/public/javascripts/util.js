/**
 * Created with IntelliJ IDEA.
 * User: liujl
 * Date: 13-1-24
 * Time: 下午1:50
 * To change this template use File | Settings | File Templates.
 */
/**
 *  hasMap 啊 hashMap
 */
function HashMap() {
    this.length = 0;
    this.entryCollect = {};
}
/**
 * put操作
 */
HashMap.prototype.put = function (key, value) {
    this.entryCollect[key] = value;
    this.length ++;
}
/**
 * get操作 不存时返回null
 */
HashMap.prototype.get = function(key) {
    return typeof(this.entryCollect[key]) == "undefined" ? null : this.entryCollect[key];
}
/**
 * get操作 不存时返回default
 */
HashMap.prototype.getDefaultIfAbsent = function(key,defaultValue) {
    return typeof(this.entryCollect[key]) == "undefined" ? defaultValue : this.entryCollect[key];
}
/**
 * 从HashMap中获取所有key的集合，以数组形式返回
 */
HashMap.prototype.keySet = function() {
    var arrKeySet = new Array();
    var index = 0;
    for(var key in this.entryCollect) {
        arrKeySet[index ++] = key;
    }
    return arrKeySet.length == 0 ? null : arrKeySet;
}
/**
 * 从HashMap中获取value的集合，以数组形式返回
 */
HashMap.prototype.values = function() {
    var arrValues = new Array();
    var index = 0;
    for(var key in this.entryCollect) {
        arrValues[index ++] = this.entryCollect[key];
    }
    return arrValues.length == 0 ? null : arrValues;
}
/**
 * 获取HashMap的value值数量
 */
HashMap.prototype.size = function() {
    return this.length;
}
/**
 * 删除指定的值
 */
HashMap.prototype.remove = function(key) {
    if(this.containsKey(key)){
        delete this.entryCollect[key];
        this.length --;
        return true;
    }else{
        return false;//key不存在直接返回false
    }
}
/**
 * 清空HashMap
 */
HashMap.prototype.clear = function() {
    for(var key in this.entryCollect) {
        delete this.entryCollect[key];
    }
    this.length = 0;
}
/**
 * 判断HashMap是否为空
 */
HashMap.prototype.isEmpty = function() {
    return this.length == 0;
}
/**
 * 判断HashMap是否存在某个key
 */
HashMap.prototype.containsKey = function(key) {
    for(var tmpKey in this.entryCollect){
        if(tmpKey == key){
            return true;
        }
    }
    return false;
}
/**
 * 判断HashMap是否存在某个value
 */
HashMap.prototype.containsValue = function(value) {
    for(var key in this.entryCollect) {
        if(this.entryCollect[key] == value){
            return true;
        }
    }
    return false;
}
/**
 * 把一个HashMap的值加入到另一个HashMap中，参数必须是HashMap
 */
HashMap.prototype.putAll = function(map) {
    if(!map){ // null undefine false NaN
        return;
    }
    if(map.constructor != HashMap){
        return;
    }
    var arrKey = map.keySet();
    var arrValue = map.values();
    for(var i in arrKey){
        this.put(arrKey[i],arrValue[i]);
        this.length++;
    }
}
//toString
HashMap.prototype.toString = function() {
    var str = "";
    for(var key in this.entryCollect) {
        str += key+ " : " + this.entryCollect[key] + "\r\n";
    }
    return str;
}