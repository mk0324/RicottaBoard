<template>
  <div class="member-modal">
    <div class="header">
      <span>Board Members</span>
      <v-icon class="close-icon" @click="$emit('close-member')">mdi-close</v-icon>
    </div>
    <div class="body">
      <div 
      class="member"
      v-for="(member, idx) in memberList"
      :key="idx"
      @click="addAssigner(member)">
        {{ member }}
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // assigners: [],
      memberList: this.$store.state.memberList,
    }
  },
  props: {
    assigners: Array,
  },
  methods: {
    addAssigner(member) {
      if(this.assigners.includes(member)) {
        this.assigners.splice(this.assigners.indexOf(member),1);
      }
      else this.assigners.push(member)
    },
  }
}
</script>

<style scoped>
.member-modal {
  width: 300px;
  padding: 8px;
  background: #ffffff;
  border-radius: 4px;
  position: absolute;
  display: inline-block;
  max-width: 80%;
  contain: content;
  will-change: transform;
  box-shadow: 0px 5px 5px -3px rgba(0, 0, 0, 0.2), 0px 8px 10px 1px rgba(0, 0, 0, 0.14), 0px 3px 14px 2px rgba(0, 0, 0, 0.12);
  border-radius: 4px;
  z-index: 10000;
}
.header {
  display: flex;
}
.member {
  cursor: pointer;
}
.close-icon {
  margin-left: auto;
}
</style>