import {useUser} from "../../useUser";

/**
 * 登录父类，存储所有登录相关的方法
 */
export default {
    data() {
        return {
            title: document.title,
            dataLoading: false,
            //登录表单对象
            loginForm: {
                //用户名
                idNo: ''
            }
        }
    },
    setup() {
        return useUser()
    },
    methods: {
        async doLogin(form) {
            const {data} = await this.$post('/utilization/consult/user/validate', form)
            if (data.data) {
                this.updateUser(data.data)
            } else {
                this.updateUser(this.loginForm)
            }
            await this.$router.push('/home')
        }
    }
}
