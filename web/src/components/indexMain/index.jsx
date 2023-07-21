import indexMain from "@dr/framework/src/components/indexMain";
import {useArchiveCarContext} from "@archive/manage-filecar/src/components/archiveCar/useArchiveCar";
// import warning from "./warning";

export default {
    name: 'ArchiveIndexMain',
    setup() {
        useArchiveCarContext()
        return () => {
            return (
                <indexMain>
                    {/*<warning/>*/}
                    <archiveCar style="position: fixed;right: 20px;bottom: 10%;z-index:2000" ref='addCar'/>
                </indexMain>
            )
        }
    },
    mounted() {
        if (this.$route.query.params) {
            localStorage.setItem("params", this.$route.query.params)
        }
    }
}