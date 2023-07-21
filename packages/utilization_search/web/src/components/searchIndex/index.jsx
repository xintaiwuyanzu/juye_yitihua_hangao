import searchContainer from "./searchContainer";

export const searchOperator = {AND: '并且', OR: '或者'}
/**
 * 查询容器
 */
export default searchContainer
/**
 * 查询历史按钮
 */
export {default as searchHistory} from './searchHistory'
/**
 * 根据门类查询按钮
 */
export {default as searchByCategory} from './searchByCategory'