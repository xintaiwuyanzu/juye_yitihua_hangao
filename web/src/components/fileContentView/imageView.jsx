import abstractViewer from "./abstractViewer";
import imageview from '../imageView'

export default {
    extends: abstractViewer,
    computed: {
        imgUrl() {
            return this.buildUrl()
        }
    },
    render() {
        return <imageview imgSrc={this.imgUrl}/>
    }
}
