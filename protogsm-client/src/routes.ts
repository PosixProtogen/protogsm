import type { RouteRecordRaw } from "vue-router";

import IndexPage from "./pages/index.vue";

const routes: RouteRecordRaw[] = [
    {
        path: "/",
        name: "index",
        component: IndexPage
    }
];

export default routes;
