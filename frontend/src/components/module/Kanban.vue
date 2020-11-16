<template>
  <div class="kanban MoveableBox" ref="kanban">
    <div class="flex justify-center">
      <div class="d-flex">

        <div
          v-for="column in this.$store.state.kanban.states"
          :key="column.columnTitle"
          @click="kanbanClickEvent"
          class="kanban-column bg-gray-100 rounded-lg px-3 py-3 column-width rounded ml-2"
        >
          <p class="text-gray-700 font-semibold font-sans tracking-wide text-sm">{{column.columnTitle}}</p>
          
          <draggable
            :list="column.tasks"
            :animation="200"
            class="draggable-box"
            ghost-class="ghost-card"
            group="tasks"

          >
            <!-- Each element from here will be draggable and animated. Note :key is very important here to be unique both for draggable and animations to be smooth & consistent. -->

            <div v-for="(task,idx) in column.tasks"
             :key="idx" class="cursor-move mb-3">
              <div 
              @click="showTask(column.columnTitle, task)" 
              @dragend="$store.commit('toggleUpdate')"
              class="bg-white shadow rounded px-3 pt-3 pb-5 border border-white" 
              style="cursor: pointer;">
                <div class="kanban-task d-flex justify-space-between ">
                  <p
                    class="text-truncate text-gray-700 font-semibold font-sans text-sm word-break:keep-all;"
                    style="word-break:keep-all; "
                    
                  >
                  {{ task.taskTitle }}
                  </p>
                 

                  <div>
                    <v-icon v-on:click="deleteTask(column.columnTitle,task)">mdi-delete</v-icon>
                  </div>
                </div>
              </div>
            </div>

          </draggable>
          <div v-on:click="addTask(column.columnTitle)" class="pressDownButton mt-3">+ add another card</div>
        </div>
      </div>
    </div>
    
    <!-- 클릭시 나오는 dialog -->
    <v-dialog max-width="600px" persistent v-model="dialog">
      <v-card>
        <v-card-title>
          <h3>{{ this.newColumnTitle }} </h3>
        </v-card-title>
        <v-card-text>
          <v-form class="px-3" ref="form">
            <v-text-field
              label="제목"
              prepend-icon="mdi-subtitles"
              v-model="newTask.taskTitle"
            ></v-text-field>
            <v-row>
              <!-- assign 멤버 -->
              <v-col cols="6" class="member-modal py-0">
                <div>
                  <v-icon>mdi-account</v-icon>
                  <span>멤버</span><br>
                  <div  
                    v-for="(member, idx) in newTask.taskAssigner" 
                    :key="idx"
                    class="assigner">
                    {{ member }}
                  </div>
                  <v-icon class="add-member" @click="showMember()">mdi-plus</v-icon>
                </div>
                <memberModal 
                  v-if="isMemberModal"
                  :assigners="newTask.taskAssigner"
                  @add-member="addAssigner"
                  @close-member="isMemberModal=false"/>
              </v-col>
              <!-- Due date 설정 -->
              <v-col cols="6" class="date-modal py-0">
                <dateModal
                @add-dates="addDates"
                />
              </v-col>
            </v-row>
            <v-textarea
              label="내용"
              prepend-icon="mdi-pencil"
              v-model="newTask.taskContents"
            ></v-textarea>
            <div class="text-center">
              <v-btn text class="primary white--text mx-2 mt-3" @click="submit" v-if="isAdd">추가</v-btn>
              <v-btn text class="primary white--text mx-2 mt-3" @click="close">닫기</v-btn>
            </div>
          </v-form>
        </v-card-text>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import draggable from "vuedraggable";
import memberModal from "./kanban/memberModal";
import dateModal from "./kanban/dateModal";

export default {
  name: "App",
  components: {
    draggable,
    memberModal,
    dateModal,
  },
  props:{kanban:Object},
  data() {
    return {
      task: {
        taskTitle:"",
        taskContents:"",
        taskAssigner:[],
      },
      states: [
        {
          columnTitle: "TO DO",
          tasks: [],
        },
        {
          columnTitle: "IN PROGRESS",
          tasks: [],
        },
        {
          columnTitle: "DONE",
          tasks: [],
        },
      ],
      dialog: false,
      isAdd: false,
      newTask: {
        taskTitle : "",
        taskContents : "",
        taskAssigner : [],
        taskDates : [],
      },
      newColumnTitle: '',
      isMemberModal: false,
    };
  },
  methods: {
    addTask(columnTitle) {
      this.isAdd = true
      this.dialog = true
      this.newColumnTitle = columnTitle;
    },
    deleteTask(columnTitle, task) {
      event.stopPropagation();
      var index = this.states
        .find((column) => column.columnTitle === columnTitle)
        .tasks.indexOf(task);

      // var index = tasks.indexOf(task);

      this.states
        .find((column) => column.columnTitle === columnTitle)
        .tasks.splice(index, 1);
      this.$store.state.kanban.states
        .find((column) => column.columnTitle === columnTitle)
        .tasks.splice(index, 1);
      this.$store.commit('toggleUpdate');
    },
    showTask(columnTitle, task) {
      var index = this.states
        .find((column) => column.columnTitle === columnTitle)
        .tasks.indexOf(task);
        this.newColumnTitle = columnTitle
        this.newTask = task;
        this.isAdd = false; 
        this.dialog = true;
    },
    submit() {
      this.states.find((column) => column.columnTitle === this.newColumnTitle).tasks.push(this.newTask);
      this.$store.state.kanban.states.find((column) => column.columnTitle === this.newColumnTitle).tasks.push(this.newTask);
      if(this.newTask.taskDates[0] === undefined) {
        var event = {
          "name": this.newTask.taskTitle, 
          "content": this.newTask.taskContents, 
        }
      }
      else {
        var event = {
          "name": this.newTask.taskTitle, 
          "content": this.newTask.taskContents, 
          "start": this.newTask.taskDates[0]+'T:00', 
          "end": this.newTask.taskDates[0]+'T:00',
        }
        if(this.newTask.taskDates[1] !== undefined) {
          event['end'] = this.newTask.taskDates[1]+'T:00';
        }
      }
      this.$store.state.scheduler.events.push(event)
      this.dialog = false;
      this.newTask = {
        taskTitle : "",
        taskContents : "",
        taskAssigner : [],
        taskDates: [],
      }
      this.$store.commit('toggleUpdate');
    },
    close() {
      this.dialog = false; 
      this.newTask = {
        taskTitle : "",
        taskContents : "",
        taskAssigner : [],
        taskDates: [],
      }
      this.$store.commit('toggleUpdate');
    },
    kanbanClickEvent({target}){
      target.focus();
    },
    showMember() {
      this.isMemberModal = true 
    },
    addAssigner(assigners) {
      this.newTask.assigners = assigners
      //console.log(assigners)
      this.isMemberModal = false
    },
    addDates(dates) {
      this.newTask.taskDates = dates
    },
  },
};
</script>
<style src="../../assets/css/my-component.css"></style>
<style scoped>
.column-width {
  min-width: 250px;
  width: 250px;
  margin-right: 0;
}
/* Unfortunately @apply cannot be setup in codesandbox, 
but you'd use "@apply border opacity-50 border-blue-500 bg-gray-200" here */
.ghost-card {
  opacity: 0.5;
  background: #f7fafc;
  border: 1px solid #4299e1;
}
.kanban 
{
  padding: 15px;    
  border-radius: 5px;
}
.kanban-column {
  background-color: #F5F5F5;
  
  box-shadow: .5rem 1rem 2rem rgba(0,0,0,.4)!important;
}
.pressDownButton {
  cursor: pointer;
  background-color: #d6d6d6;
  color: rgb(68, 68, 68);
  border-bottom: 0px;
  transition: all 0.2s ease-in-out;
}

.pressDownButton:hover {
  background: rgba(0,0,0,0.4);
  color: white;
}

.draggable-box {
  margin-right: 0; 
  min-height: 50px;
  border: dashed 2px #d6d6d6;
}

.assigner {
  display: inline-block;
  background: #eeeeee;
  border-radius: 8px;
  padding: 4px 8px;
  margin-right: 4px;
}

.add-member{
  background: #ddddee;
  border-radius: 16px;
  padding: 4px;
  margin-left: 8px;
}

</style>
