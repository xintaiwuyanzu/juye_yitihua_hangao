import {inject, provide, reactive} from "vue";
import {http} from "@dr/framework/src/plugins/http";
import {Message} from 'element-ui'
import {useRouter} from "@dr/auto/lib";

/**
 * 提供查档人员相关上下文
 * @type {string}
 */
const providerKey = '$user'
/**
 * 上下文容器
 * @returns {UnwrapRef<{user: {}}>}
 */
export const useUserContext = () => {
    const user = reactive({user: {}})
    provide(providerKey, user)
    return user
}
/**
 * 共享用户的属性
 * @type {string[]}
 */
export const userAttrs = ['userName', 'birthday', 'ethnic', 'sex', 'idNo', 'valid', 'agency', 'idType']
/**
 * 子组件使用用户信息
 * @returns {*}
 */
export const useUser = () => {
    const user = inject(providerKey)
    const {router} = useRouter()

    //更新当前登录人信息
    const updateUser = (v) => {
        if (v) {
            userAttrs.forEach(a => {
                if (v[a]) {
                    user.user[a] = v[a]
                }
            })
        }
    }
    const logout = async () => {
        await http().post('/utilization/consult/user/logout')
        Message.success('登出成功')
        user.user = {}
        router.push('/login')
    }
    return {user: user.user, updateUser, logout}
}

export const AbstractUser = {
    setup() {
        return useUser()
    }
}