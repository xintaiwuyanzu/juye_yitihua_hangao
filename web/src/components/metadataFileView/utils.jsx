import {useModules} from "@dr/framework/lib/module";

const makeArr = v => {
    if (Array.isArray(v)) {
        return v
    } else {
        return [v]
    }
}
/**
 * 过滤指定的组件
 * @param moduleName 模块名称
 * @param includes 包含的模块
 * @param excludes 排除的模块
 * @param context 上下文
 */
export const filterComponents = (moduleName, includes, excludes, context) => {
    let modules = useModules(moduleName)

    includes = makeArr(includes)
    excludes = makeArr(excludes)

    modules = modules
        .filter(m => {
            if (excludes.includes(m.name)) {
                return false
            }
            //指定包含了
            if (includes.includes(m.name)) {
                return true
            }
            //判断角色
            if (m.role) {
                return context.hasRole(m.role)
            } else {
                return true
            }
        })
    //排序
    modules.sort((a, b) => {
        if (a.order && b.order) {
            return a.order - b.order
        }
        return 0
    })
    return modules
}