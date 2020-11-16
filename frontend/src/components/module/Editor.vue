<template>
    <div id="EditorMain" class="MoveableBox editor">
        <div v-if="edit.isHidden">
            <div class="row m-0 subtitle">
                <v-btn
                    color="blue-grey lighten-4"
                    @click="changeHidden"
                    class="my-auto mx-1"
                    >
                    <v-icon
                        center
                        light
                    >
                        mdi-arrow-down-drop-circle-outline
                    </v-icon>
                </v-btn>
                <div v-if="edit.title" class="align-middle my-auto mx-1"> {{ subtitle }} </div>
                <div v-if="!edit.title" class="align-middle my-auto mx-1"> 제목 없음 </div>                
            </div>
        </div>
        <div v-if="!edit.isHidden">
            <div class="title row m-0">
                <div class="col-10 p-0 m-0">
                    <div class="semititle row m-0">
                        <v-btn
                        color="blue-grey lighten-4"
                        @click="changeHidden"
                        class="my-auto mx-1"
                        >
                            <v-icon
                                center
                                dark
                            >
                                mdi-arrow-up-drop-circle
                            </v-icon>
                        </v-btn>
                        <input type="text" placeholder="제목" v-model="edit.title" class="ml-1">
                    </div>
                </div>
                <v-btn
                    :loading="isLoading"
                    :disabled="isLoading"
                    color="blue-grey"
                    class="m-auto  white--text"
                    @click="saveEditor"
                    >
                    저장하기
                    <v-icon
                        right
                        dark
                    >
                        mdi-cloud-download
                    </v-icon>
                </v-btn>
            </div>
            <Editor        
                height="500px"
                :initialValue="edit.text"
                ref="toastuiEditor"
                @change = "onEditorChange"
                class="bg-white"
            />
        </div>
    </div>
</template>

<script src="https://cdn.jsdelivr.net/npm/marked/marked.min.js"></script>
<script src="lodash.js"></script>
<script>
import _ from "lodash"
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';

import { Editor } from '@toast-ui/vue-editor';
import { saveAs } from 'file-saver';

var editSet;

export default {
    data() {
        return {
            isLoading: false,
            edit: Object,
            test: 1,
        }
    },
    components: {
        Editor
    },
    props: {
        editor: Object,
    },
    computed: {
        subtitle() {
            if (this.edit.title.length <= 8) {
                return this.edit.title
            } else {
                return this.edit.title.slice(0, 8)
            }
        },
    },
    watch: {
        'editor.text': function() {
            this.textChange()
        },
        'editor.title': function() {
            this.edit.title = this.editor.title;
        },
        'editor.isHidden': function() {
            this.edit.isHidden = this.editor.isHidden
        },
        'edit.title': function() {
            this.editChange();
        },
    },
    methods: {
        // 글 입력시 Editor에 입력된 값을 소켓에 전송
        onEditorChange() {
            this.edit.text = this.$refs.toastuiEditor.invoke("getMarkdown");
            this.editChange();
        },

        // 다른 사람이 입력하여 내용 변동시 Editor에 변동한 값 적용
        textChange() {
            if (this.editor.text != this.$refs.toastuiEditor.invoke("getMarkdown")) {
                this.$refs.toastuiEditor.invoke("setMarkdown", this.editor.text);
            }
        },

        // md 파일로 저장
        saveEditor() {     
            this.isLoading = true;
            var FileSaver = require ('file-saver');
            var blob = new Blob([this.edit.text], { type : "text / plain; charset = utf-8" });
            var name = "untitle";
            if (this.edit.title) {
                name = this.edit.title
            };
            FileSaver.saveAs (blob, name+".md");
            setTimeout(() => (this.isLoading = false), 1000);
        },

        // 에디터 숨기기/펼치기
        changeHidden() {
            this.edit.isHidden = !this.edit.isHidden;
            this.editChange();
        },

        // 변경된 값을 받고 emit하기 전 debounce를 줌
        editChange() {
            if (editSet) {
                clearTimeout(editSet);
            }
            editSet = setTimeout(() => {
                this.$emit('changeEditor', this.edit);
            }, 500);
        },
    },
    created() {
        this.edit = this.editor;
    },
}
</script>

<style scoped>
html, body, #EditorMain {
    margin: 0;
    width: 860px;
    height: 610px;
    font-family: 'Helvetica Neue', Arial, sans-serif;
    color: #333;
    background-color: #f6f6f6;
}

.editor {
    padding: 15px;
}
.title {
    width: 800px;
    height: 50px;
    border: 1px solid #ccc;
    border-radius: 5px 5px 0 0;
    background-color: #f6f6f6;
}
.subtitle {
    height: 50px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #f6f6f6;
}
.semititle {
    width: 666.66px;
    height: 50px;
}
</style>