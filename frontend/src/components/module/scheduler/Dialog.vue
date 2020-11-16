<template>
  <v-dialog max-width="600px" persistent v-model="dialog">
    <v-card>
      <v-card-title>
        <h3>일정 추가</h3>
      </v-card-title>
      <v-card-text>
        <v-form class="px-3" ref="form">
          <v-text-field
            label="일정"
            v-model="scheduler.event.title"
            prepend-icon="mdi-folder-marker"
          ></v-text-field>
          <v-textarea
            label="상세설명"
            v-model="scheduler.event.content"
            prepend-icon="mdi-pencil"
          ></v-textarea>
          <v-row>
            <v-col cols="6" class="pb-0">
              <v-menu>
                <template v-slot:activator="{on}">
                  <v-text-field
                    slot="activator"
                    label="시작일"
                    readonly
                    prepend-icon="mdi-calendar-month"
                    v-on="on"
                    :value="scheduler.event.startDate"
                    class
                  ></v-text-field>
                </template>
                <v-date-picker v-model="scheduler.event.startDate"></v-date-picker>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pb-0">
              <v-menu :close-on-content-click="false" v-model="startTimer" offset-y>
                <template v-slot:activator="{ on }">
                  <v-text-field
                    label="시작 시간"
                    readonly
                    :value="scheduler.event.startTime"
                    prepend-icon="mdi-timer"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker v-if="startTimer" v-model="scheduler.event.startTime">
                  <v-btn class="mx-auto" @click="selectTime">선택</v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
          </v-row>

          <v-row>
            <v-col cols="6" class="pt-0">
              <v-menu>
                <template v-slot:activator="{on}">
                  <v-text-field
                    slot="activator"
                    label="종료일"
                    readonly
                    prepend-icon="mdi-calendar-month"
                    v-on="on"
                    :value="scheduler.event.endDate"
                    class
                  ></v-text-field>
                </template>
                <v-date-picker v-model="scheduler.event.endDate" :allowed-dates="allowedDates"></v-date-picker>
              </v-menu>
            </v-col>
            <v-col cols="6" class="pt-0">
              <v-menu :close-on-content-click="false" v-model="endTimer" offset-y>
                <template v-slot:activator="{ on }">
                  <v-text-field
                    label="종료 시간"
                    readonly
                    :value="scheduler.event.endTime"
                    prepend-icon="mdi-timer"
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-time-picker v-if="endTimer" v-model="scheduler.event.endTime">
                  <v-btn class="mx-auto" @click="selectTime">선택</v-btn>
                </v-time-picker>
              </v-menu>
            </v-col>
          </v-row>

          <div class="text-center">
            <v-btn text class="primary white--text mx-2 mt-3" @click="submit">추가</v-btn>
            <v-btn text class="primary white--text mx-2 mt-3" @click="close">닫기</v-btn>
          </div>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data() {
    return {
      startTimer: false,
      endTimer: false
    }
  },
  computed: {
    dialog() {
      return this.$store.state.scheduler.dialog;
    },
    scheduler() {
      return this.$store.state.scheduler;
    }
  },
  methods: {
    submit() {
      if (this.$refs.form.validate()) {
          this.$store.dispatch('REQUEST_ADD_EVENT', this.scheduler.event);
      }
      this.$store.commit('toggleUpdate');
    },
    // submit() {
    //     if (this.event.title === '' || this.event.endDate === '') {
    //         store.commit('SET_SNACKBAR',
    //          setSnackBarInfo('제목과 종료일자를 작성해주세요.', 'error', 'top'));
    //     } else {
    //         this.$store.dispatch('REQUEST_ADD_EVENT', this.event);
    //     }
    // },
    close() {
      this.$store.commit("CLOSE_SCHEDULER_DIALOG");
    },
    selectTime() {
      this.endTimer = false;
      this.startTimer = false;
    },
    allowedDates(val) {
      let endDate = val.split("-").reduce((a, b) => a + b);
      let startDate = this.scheduler.event.startDate
        .split("-")
        .reduce((a, b) => a + b);
      return endDate >= startDate;
    }
  }
};
</script>