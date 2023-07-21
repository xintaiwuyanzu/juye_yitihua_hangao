<template>
  <div class="img_editor" v-loading="loading">
    <div class="canvas">
      <img ref="image" :src="imgSrc" @loadstart="start" @load="start">
    </div>
    <div class="toolbar" @click="click">
      <button class="toolbar__button" data-action="move" title="移动">
        <icon icon="move"/>
      </button>
      <button class="toolbar__button" data-action="zoom-in" title="放大">
        <icon icon="zoom-in"/>
      </button>
      <button class="toolbar__button" data-action="zoom-out" title="缩小">
        <icon icon="zoom-out"/>
      </button>
      <button class="toolbar__button" data-action="rotate-left" title="向左旋转">
        <icon icon="rotate-ccw"/>
      </button>
      <button class="toolbar__button" data-action="rotate-right" title="向右旋转">
        <icon icon="rotate-cw"/>
      </button>
      <button class="toolbar__button" data-action="flip-horizontal" title="水平镜像">
        <icon icon="more-horizontal"/>
      </button>
      <button class="toolbar__button" data-action="flip-vertical" title="垂直镜像">
        <icon icon="more-vertical"/>
      </button>
    </div>
  </div>
</template>
<script>
import Cropper from 'cropperjs';
import 'cropperjs/dist/cropper.css';

/**
 * 图片渲染控件，只需要传图片的路径即可，自带放大缩小旋转等功能
 */
/**
 * 这里所有的代码都是从
 * https://github.com/fengyuanchen/photo-editor/
 * 复制过来的，需要修改详细参考上述仓库
 */
export default {
  name: "imageView",
  props: {
    /**
     * 图片路径
     */
    imgSrc: {type: String}
  },
  data() {
    return {loading: false}
  },
  methods: {
    click({target}) {
      const {cropper} = this;
      const action = target.getAttribute('data-action') || target.parentElement.getAttribute('data-action');
      switch (action) {
        case 'move':
          cropper.setDragMode(action);
          break;
        case 'zoom-in':
          cropper.zoom(0.1);
          break;
        case 'zoom-out':
          cropper.zoom(-0.1);
          break;
        case 'rotate-left':
          cropper.rotate(-90);
          break;
        case 'rotate-right':
          cropper.rotate(90);
          break;
        case 'flip-horizontal':
          cropper.scaleX(-cropper.getData().scaleX || -1);
          break;
        case 'flip-vertical':
          cropper.scaleY(-cropper.getData().scaleY || -1);
          break;
        default:
      }
    },
    async stop() {
      if (this.cropper) {
        await this.cropper.destroy();
        this.cropper = null;
      }
    },
    async start() {
      this.loading = true
      await this.stop()
      if (this.$refs.image) {
        this.cropper = new Cropper(this.$refs.image, {
          autoCrop: false,
          dragMode: 'move',
          background: false
        });
      }
      this.loading = false
    },
  },
  beforeDestroy() {
    this.stop();
  }
}
</script>
<style lang="scss">
.img_editor {
  height: 100%;

  .canvas {
    align-items: center;
    display: flex;
    height: 100%;
    justify-content: center;

    & > img {
      max-height: 100%;
      max-width: 100%;
    }
  }

  .toolbar {
    background-color: rgba(0, 0, 0, .5);
    bottom: 1rem;
    color: #fff;
    height: 2rem;
    left: 50%;
    margin-left: -8rem;
    position: absolute;
    width: 14rem;
    z-index: 2015;

    .toolbar__button {
      background-color: transparent;
      border-width: 0;
      color: #fff;
      cursor: pointer;
      display: block;
      float: left;
      font-size: 0.9rem;
      height: 2rem;
      text-align: center;
      width: 2rem;

      &:focus {
        outline: none;
      }

      &:hover {
        background-color: $--color-primary;
        color: #fff;
      }
    }
  }
}
</style>
