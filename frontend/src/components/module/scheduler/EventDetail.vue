<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" persistent max-width="400">
      <v-card>
        <v-card-title
          class="headline"
          style="color: white; background-color: #007bff"
        >
          <div v-if="!isModify">{{ event.name }}</div>
          <v-text-field v-if="isModify" v-model="event.name"> </v-text-field>
          <v-spacer></v-spacer>
          <v-icon color="white" @click="close()">mdi-close</v-icon>
        </v-card-title>
        <v-card-text
          style="font-size: 1rem; min-height: 300px; padding: 16px 16px;"
          class="font-weight-bold"
        >
          <div v-if="!isModify">{{ event.content }}</div>
          <v-text-field v-if="isModify" v-model="event.content"> </v-text-field>
        </v-card-text>

        <div class="pl-4 font-weight-light">
          <div>시작 시간: {{ getEventStart() }}</div>
          <div>종료 시간: {{ getEventEnd() }}</div>
        </div>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="modify()">
            <div v-if="!isModify">수정</div>
            <div v-if="isModify">확인</div>
          </v-btn>
          <v-btn color="red darken-1" text @click="remove()">삭제</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  name: "EventDetail",
  computed: {
    dialog() {
      return this.$store.state.scheduler.eventDetail;
    },
    event() {
      return this.$store.state.scheduler.event;
    },
  },
  data() {
    return {
      isModify: false,
    };
  },
  methods: {
    getEventStart() {
      return this.event.startDate + this.getTime(this.event.startTime);
    },
    getEventEnd() {
      return this.event.endDate + this.getTime(this.event.endTime);
    },
    close() {
      for (var i = 0; i < this.$store.state.scheduler.events.length; i++) {
        if (this.$store.state.scheduler.events[i].id == 1) {
          this.$store.state.scheduler.events[i].id = null;
          break;
        }
      }
      return this.$store.commit("CLOSE_SCHEDULER_EVENT");
    },
    modify() {
      if (this.isModify) {
        //console.log(this.$store.state.scheduler.events);
        for (var i = 0; i < this.$store.state.scheduler.events.length; i++) {
          if (this.$store.state.scheduler.events[i].id == 1) {
            this.$store.state.scheduler.events[
              i
            ].name = this.$store.state.scheduler.event.name;
            this.$store.state.scheduler.events[
              i
            ].content = this.$store.state.scheduler.event.content;
            break;
          }
        }
      }
      this.isModify = !this.isModify;
      this.$store.commit('toggleUpdate');
    },
    remove() {
      for (var i = 0; i < this.$store.state.scheduler.events.length; i++) {
        if (this.$store.state.scheduler.events[i].id == 1) {
          this.$store.state.scheduler.events.splice(i, 1);
          this.close();
          break;
        }
      }
      this.$store.commit('toggleUpdate');
    },
    getTime(time) {
      return time === null ? "" : ` ${time}`;
    },
  },
}
</script>

<style scoped>
.contents {
  min-height: 150px;
}
</style>