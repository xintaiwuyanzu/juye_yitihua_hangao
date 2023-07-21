import util from "@dr/framework/src/components/login/util";
import {Base64} from 'js-base64'

/**
 * 登录父类，存储所有登录相关的方法
 */
export default {
    data() {
        const loginType = 'default'
        return {
            dataLoading: false,
            title: document.title,
            //登录校验地址
            validatePath: 'ssoLogin/validate',
            //登录表单对象
            loginForm: {
                loginType,
                //用户名
                username: '',
                //密码
                password: '',
                //记住密码
                rememberMe: false
            }
        }
    },
    methods: {
        /**
         * 登录方法
         * @param username
         * @param password
         */
        async doLogin({username, password}) {
            if (username && password) {
                this.dataLoading = true
                const {data} = await this.$post(this.validatePath, {
                    username: username,
                    password: this.securityPassword(password),
                    loginType: this.loginForm.loginType
                })
                if (data.success) {
                    //设置前端缓存
                    util.setToken(data.data)
                    //this.$message.success('登录成功！')
                    this.$message({duration: 1000, message: '登录成功！', type: 'success'})
                    await this.$router.push('/main')
                } else {
                    this.$message.error(data.message)
                }
                this.dataLoading = false
            } else {
                this.$message.error('请输入用户名或密码！')
            }
        },
        /**
         * 加密密码
         * @param password
         * @returns {string}
         */
        securityPassword(password) {
            return Base64.encode(password)
        }
    }
}
