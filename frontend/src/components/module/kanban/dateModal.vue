<template>
  <div>
    <v-text-field
      v-model="dateRangeText"
      label="일정 설정"
      prepend-icon="mdi-calendar"
      readonly
      @click="isDatePicker=true"
    ></v-text-field>
    <v-dialog
      ref="dialog"
      v-model="isDatePicker"
      persistent
      width="290px"
    >
      <v-date-picker
        class="date-modal"
        v-model="dates"
        range
        v-if="isDatePicker"
        no-title
      >
      <v-spacer></v-spacer>
      <v-btn text color="primary" @click="datePick()">
        OK
      </v-btn>
      </v-date-picker>
    </v-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return{
      dates: [],
      isDatePicker: false,
      modal: false,
    }
  },
  computed: {
    dateRangeText () {
      return this.dates.join(' ~ ')
    },
  },
  methods: {
    datePick() {
      if(this.dates[0] > this.dates[1]){
        var temp = this.dates[0]
        this.dates[0] = this.dates[1]
        this.dates[1] = temp
      }
      this.$emit('add-dates',this.dates)
      this.isDatePicker = false;
    }
  }
}
</script>

<style scoped>
.date-modal {
  border-radius: 4px;
  /* margin: 24px; */
  overflow-y: auto;
  pointer-events: auto;
  transition: .3s cubic-bezier(.25,.8,.25,1);
  width: 100%;
  z-index: inherit;
  box-shadow: 0 11px 15px -7px rgba(0,0,0,.2), 0 24px 38px 3px rgba(0,0,0,.14), 0 9px 46px 8px rgba(0,0,0,.12);
}
</style>