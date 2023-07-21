import logo from './logo.svg'
import './styles.scss'

/**
 * 头部左侧默认是系统名称
 */
export default {
    name: 'headerLeft',
    methods: {
        home() {
            if (this.$route.path !== '/home') {
                this.$router.push('/home')
            }
        }
    },
    render() {
        const title = document.title
        return (
            <section onClick={this.home} class='header-left'>
                <img src={logo}/>
                <span class='title' style='line-height:36px'>{title}</span>
            </section>
        )
    }
}