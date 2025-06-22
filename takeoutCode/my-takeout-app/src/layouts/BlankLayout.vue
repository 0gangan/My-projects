<!-- src/views/layouts/BlankLayout.vue -->
<template>
  <div class="blank-layout">
    <router-view />
  </div>
</template>

<script setup></script>

<style scoped>
.blank-layout {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}
</style>
