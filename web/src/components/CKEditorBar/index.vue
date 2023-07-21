<template>
  <div class="ck-editor-wrapper">
    <div class="document-editor__toolbar" ref="toolBar"/>
    <div class="document-editor__editable-container">
      <div class="document-editor__editable" ref="editor"/>
    </div>
  </div>
</template>
<script>
import CKEditorDocument from '@dr/ckeditor5-document'
import {debounce} from 'lodash-es';

const editorConfig = {
  ckfinder: {
    //后端处理上传逻辑返回json数据,包括uploaded(选项true/false)和url两个字段
    uploadUrl: '/api/compilationtask/uploadImg?command=QuickUpload&type=Images&responseType=json'
  },
}
export default {
  props: {
    value: {type: String},
    //是否禁用ckeditor的编辑功能,默认不禁用
    readOnly: {type: Boolean, default: false},
    //是否显示toolbar,默认显示
    toolbar: {type: Boolean, default: true},
  },
  data() {
    return {
      ckEditor: null,
      $_lastEditorData: {
        type: String,
        default: ''
      }
    }
  },
  async mounted() {
    await this.createEditor()
  },
  methods: {
    async createEditor() {
      if (!this.ckEditor) {
        try {
          this.ckEditor = await CKEditorDocument.create(this.$refs.editor, Object.assign({initialData: this.value || ''}, editorConfig))
          this.$_setUpEditorEvents()
          if (this.toolbar) {
            this.$refs.toolBar.appendChild(this.ckEditor.ui.view.toolbar.element);
          }
        } catch (ignore) {
          console.error(`初始化编辑器失败${ignore}`)
        }
      }
      if (this.ckEditor) {
        // this.ckEditor.isReadOnly = this.readOnly
      }
    },
    $_setUpEditorEvents() {
      const editor = this.ckEditor;
      const emitDebouncedInputEvent = debounce(evt => {
        const data = this.$_lastEditorData = editor.getData();
        this.$emit('input', data, evt, editor);
      }, 300, {leading: true});

      editor.model.document.on('change:data', emitDebouncedInputEvent);

      editor.editing.view.document.on('focus', evt => {
        this.$emit('focus', evt, editor);
      });

      editor.editing.view.document.on('blur', evt => {
        this.$emit('blur', evt, editor);
      });
    }
  },
  watch: {
    value(newValue, oldValue) {
      if (newValue !== oldValue && newValue !== this.$_lastEditorData) {
        if (this.ckEditor) {
          this.ckEditor.setData(newValue || '');
        }
      }
    }
  },
  async beforeDestroy() {
    if (this.ckEditor) {
      await this.ckEditor.ui.view.toolbar.destroy();
      await this.ckEditor.destroy();
      this.ckEditor = null;
    }
  }
}
</script>
<style lang="scss">
.ck-editor-wrapper {

  border: 1px solid $--border-color-base;
  border-radius: $--border-radius-base;

  display: flex;
  flex-flow: column nowrap;

  .document-editor__toolbar {
    z-index: 1;
    box-shadow: 0 0 5px hsla(0, 0%, 0%, .2);
    border-bottom: 1px solid var(--ck-color-toolbar-border);

    .ck-toolbar {
      border: 0;
      border-radius: 0;
    }
  }

  .document-editor__editable-container {
    flex: 1;
    overflow: auto;
    padding: calc(2 * var(--ck-spacing-large));
    background: var(--ck-color-base-foreground);

    overflow-y: scroll;

    .ck-editor__editable {
      width: 21cm;
      overflow: auto;
      min-height: 15cm;

      padding: 1cm 2cm 2cm;

      border: 1px hsl(0, 0%, 82.7%) solid;
      border-radius: var(--ck-border-radius);
      background: white;

      box-shadow: 0 0 5px hsla(0, 0%, 0%, .1);

      margin: 0 auto;
    }

    .ck-source-editing-area {
      width: 25cm;
      overflow: auto;
      min-height: 15cm;
      margin: 0 auto;
    }
  }

  .ck-content, .ck-heading-dropdown .ck-list .ck-button__label {
    font: 16px/1.6 "Helvetica Neue", Helvetica, Arial, sans-serif;
  }

  .ck-heading-dropdown .ck-list .ck-button__label {
    line-height: calc(1.7 * var(--ck-line-height-base) * var(--ck-font-size-base));
    min-width: 6em;
  }

  .ck-heading-dropdown .ck-list .ck-button:not(.ck-heading_paragraph) .ck-button__label {
    transform: scale(0.8);
    transform-origin: left;
  }

  .ck-content h2,
  .ck-heading-dropdown .ck-heading_heading1 .ck-button__label {
    font-size: 2.18em;
    font-weight: normal;
  }

  .ck-content h2 {
    line-height: 1.37em;
    padding-top: .342em;
    margin-bottom: .142em;
  }

  .ck-content h3, .ck-heading-dropdown .ck-heading_heading2 .ck-button__label {
    font-size: 1.75em;
    font-weight: normal;
    color: hsl(203, 100%, 50%);
  }

  .ck-heading-dropdown .ck-heading_heading2.ck-on .ck-button__label {
    color: var(--ck-color-list-button-on-text);
  }

  .ck-content h3 {
    line-height: 1.86em;
    padding-top: .171em;
    margin-bottom: .357em;
  }

  .ck-content h4,
  .ck-heading-dropdown .ck-heading_heading3 .ck-button__label {
    font-size: 1.31em;
    font-weight: bold;
  }

  .ck-content h4 {
    line-height: 1.24em;
    padding-top: .286em;
    margin-bottom: .952em;
  }

  .ck-content p {
    font-size: 1em;
    line-height: 1.63em;
    padding-top: .5em;
    margin-bottom: 1.13em;
  }

  .ck-content blockquote {
    font-family: Georgia, serif;
    margin-left: calc(2 * var(--ck-spacing-large));
    margin-right: calc(2 * var(--ck-spacing-large));
  }
}
</style>